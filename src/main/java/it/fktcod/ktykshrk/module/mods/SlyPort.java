package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.AimBot;
import it.fktcod.ktykshrk.module.mods.AntiBot;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;

public class SlyPort extends Module {

    public static NumberValue Range;
    public static BooleanValue Aim;
    public static BooleanValue Sound;
    public static BooleanValue Player;
    private final boolean s = false;

    public SlyPort() {
        super("SlyPort", HackCategory.ANOTHER);
        this.addValue(Range = new NumberValue("Range", 6.0D, 2.0D, 15.0D));
        this.addValue(Aim = new BooleanValue("Aim", true));
        this.addValue(Sound = new BooleanValue("Play sound", true));
        this.addValue(Player = new BooleanValue("Players only", true));
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {

        Entity en = this.ge();
        if (en != null) {
            this.tp(en);
        }



    }

    private void tp(Entity en) {
        if (Sound.getValue()) {
            mc.thePlayer.playSound("mob.endermen.portal", 1.0F, 1.0F);
        }

        Vec3 vec = en.getLookVec();
        double x = en.posX - vec.xCoord * 2.5D;
        double z = en.posZ - vec.zCoord * 2.5D;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, mc.thePlayer.posY, z, true));

        if (Aim.getValue()) {
            Utils.assistFaceEntity(en, AimBot.yaw.getValue().floatValue(), AimBot.pitch.getValue().floatValue());
        }

        this.onDisable();

    }

    private Entity ge() {
        Entity en = null;
        double r = Math.pow(Range.getValue(), 2.0D);
        double dist = r + 1.0D;
        Iterator var6 = mc.theWorld.loadedEntityList.iterator();

        while(true) {
            Entity ent;
            do {
                do {
                    do {
                        do {
                            if (!var6.hasNext()) {
                                return en;
                            }

                            ent = (Entity)var6.next();
                        } while(ent == mc.thePlayer);
                    } while(!(ent instanceof EntityLivingBase));
                } while(((EntityLivingBase)ent).deathTime != 0);
            } while(Player.getValue() && !(ent instanceof EntityPlayer));

            if (!AntiBot.isBot(ent)) {
                double d = mc.thePlayer.getDistanceSqToEntity(ent);
                if (!(d > r) && !(dist < d)) {
                    dist = d;
                    en = ent;
                }
            }
        }
    }
}