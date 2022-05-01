/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C13PacketPlayerAbilities
 *  net.minecraft.potion.Potion
 */
package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.util.Random;

public class Trigger extends Module {
    private TimerUtils timer = new TimerUtils();

    static Minecraft mc =Minecraft.getMinecraft();

    public static NumberValue maxVal;
    public static NumberValue minVal;
    public static NumberValue range;
    public static BooleanValue docrit;
    public static BooleanValue blockHit;
    public static BooleanValue players;
    public static BooleanValue mobs;
    public static BooleanValue animals;
    public static BooleanValue invisible;

    public Trigger() {
        super("Trigger", HackCategory.COMBAT);

        minVal = new NumberValue("Min CPS", 9D, 0D, 20D);
        this.addValue(minVal);
        maxVal = new NumberValue("Max CPS", 13D, 0D, 20D);
        this.addValue(maxVal);
        range = new NumberValue("Range", 3.5D, 0.0D, 10.0D);
        this.addValue(range);
        blockHit = new BooleanValue("AutoBlock", false);
        addValue(blockHit);
        docrit = new BooleanValue("Critial", false);
        addValue(docrit);
        players = new BooleanValue("Players",true);
        this.addValue(players);
        mobs = new BooleanValue("Mobs",false);
        this.addValue(mobs);
        animals = new BooleanValue("Animal",false);
        this.addValue(animals);
        invisible = new BooleanValue("Invisible",false);

    }


    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!timer.hasTimeElapsed(1000 / getAPS(), true)) {
            return;
        }
        if (mc.objectMouseOver == null) {
            return;
        }
        if (mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            EntityLivingBase entityHit = (EntityLivingBase) mc.objectMouseOver.entityHit;
            if (!isValidType(entityHit)
                    || mc.thePlayer.getDistanceToEntity(entityHit) > getRange()) {
                return;
            }


            if(blockHit.getValue()){
                stopBlock();
            }

            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(mc.thePlayer, entityHit);

            if(blockHit.getValue()){
                startBlock();
            }


;
        }

    }

    public static void startBlock() {
        if(HackManager.getHack("NoSlow").isToggled()) {
            return;
        }
        if(mc.thePlayer.isBlocking()) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
            mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(),
                    mc.thePlayer.getHeldItem().getMaxItemUseDuration());
        }
    }
    public static void stopBlock() {
        if(HackManager.getHack("NoSlow").isToggled()) {
            return;
        }
        if(mc.thePlayer.isBlocking()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
        }
    }
    public static double getRange() {
        return range.getValue();
    }
    public static int getAPS() {
        return (int)nextFloat(minVal.getValue().floatValue(),maxVal.getValue().floatValue());
    }
    public static float nextFloat(final float startInclusive, final float endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0F)
            return startInclusive;

        return (float) (startInclusive + ((endInclusive - startInclusive) * Math.random()));
    }
    private static boolean isValidType(Entity entity) {
        return players.getValue() && entity instanceof EntityPlayer || mobs.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime) || animals.getValue() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || animals.getValue() && entity instanceof EntityAnimal;
    }
}

