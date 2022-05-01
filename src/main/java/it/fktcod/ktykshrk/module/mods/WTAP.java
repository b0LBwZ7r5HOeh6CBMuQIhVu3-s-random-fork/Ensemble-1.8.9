package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class WTAP extends Module {


    public static BooleanValue onlyPlayers;
    public static NumberValue minActionTicks, maxActionTicks, minOnceEvery, maxOnceEvery,range;
    public static double comboLasts;
    public static boolean comboing, hitCoolDown, alreadyHit;
    public static int hitTimeout, hitsWaited;
    public WTAP() {
        super("WTAP", HackCategory.MOVEMENT);

        this.addValue(onlyPlayers = new BooleanValue("Players Only" ,true));
        this.addValue(minActionTicks = new NumberValue("Min Delay: ", 5D, 1D, 100D));
        this.addValue(maxActionTicks = new NumberValue("Man Delay: ", 12D, 1D, 100D));
        this.addValue(minOnceEvery = new NumberValue("Min Hits: ", 1D, 1D, 10D));
        this.addValue(maxOnceEvery = new NumberValue("Max Hits: ", 1D, 1D, 10D));
        this.addValue(range = new NumberValue("Range: ", 3.0D, 1.0D, 6.0D));
        //this.setChinese(Drift.Translate_CN[15]);
    }

    public void guiUpdate() {
        correctSliders(minActionTicks, maxActionTicks);
        correctSliders(minOnceEvery, maxOnceEvery);
    }

    public static void correctSliders(NumberValue c, NumberValue d) {
        if (c.getValue() > d.getValue()) {
            double p = c.getValue();
            c.setValue(d.getValue());
            d.setValue(p);
        }

    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (Utils.nullCheck())
            return;

        if(comboing) {
            if(System.currentTimeMillis() >= comboLasts){
                comboing = false;
                finishCombo();
                return;
            }else {
                return;
            }
        }



        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown(0)) {
            Entity target = mc.objectMouseOver.entityHit;
            ////////////System.out.println(target.hurtResistantTime);
            if(target.isDead) {
                return;
            }

            if (mc.thePlayer.getDistanceToEntity(target) <= range.getValue()) {
                if (target.hurtResistantTime >= 10) {

                    if (onlyPlayers.getValue()){
                        if (!(target instanceof EntityPlayer)){
                            return;
                        }
                    }

                    if(!check((EntityLivingBase)target)){
                        return;
                    }


                    if (hitCoolDown && !alreadyHit) {
                        ////////////System.out.println("coolDownCheck");
                        hitsWaited++;
                        if(hitsWaited >= hitTimeout){
                            ////////////System.out.println("hiit cool down reached");
                            hitCoolDown = false;
                            hitsWaited = 0;
                        } else {
                            ////////////System.out.println("still waiting for cooldown");
                            alreadyHit = true;
                            return;
                        }
                    }

                    ////////////System.out.println("Continued");

                    if(!alreadyHit){
                        ////////////System.out.println("Startring combo code");
                        guiUpdate();
                        if(minOnceEvery.getValue() == maxOnceEvery.getValue()) {
                            hitTimeout =  minOnceEvery.getValue().intValue();
                        } else {

                            hitTimeout = ThreadLocalRandom.current().nextInt(minOnceEvery.getValue().intValue(), maxOnceEvery.getValue().intValue());
                        }
                        hitCoolDown = true;
                        hitsWaited = 0;

                        comboLasts = ThreadLocalRandom.current().nextDouble(minActionTicks.getValue(),  maxActionTicks.getValue()+0.01) + System.currentTimeMillis();
                        comboing = true;
                        startCombo();
                        ////////////System.out.println("Combo started");
                        alreadyHit = true;
                    }
                } else {
                    if(alreadyHit){
                        ////////////System.out.println("UnHit");
                    }
                    alreadyHit = false;
                    ////////////System.out.println("REEEEEEE");
                }
            }
        }
    }

    public boolean check(EntityLivingBase entity) {
        if(entity instanceof EntityArmorStand) { return false; }
        if(ValidUtils.isValidEntity(entity)){ return false; }
        if(!ValidUtils.isNoScreen()) { return false; }
        if(entity == Wrapper.INSTANCE.player()) { return false; }
        if(entity.isDead) { return false; }
        if(ValidUtils.isBot(entity)) { return false; }
        if(!ValidUtils.isFriendEnemy(entity)) { return false; }
        if(!ValidUtils.isInvisible(entity)) { return false; }
        //if(!ValidUtils.isInAttackFOV(entity, FOV.getValue().intValue())) { return false; }
        if(!ValidUtils.isInAttackRange(entity, range.getValue().floatValue())) { return false; }
        if(!ValidUtils.isTeam(entity)) { return false; }
        if(!ValidUtils.pingCheck(entity)) { return false; }
        //if(!isPriority(entity)) { return false; }
        //if(!this.walls.getValue()) { if(!Wrapper.INSTANCE.player().canEntityBeSeen(entity)) { return false; } }
        return true;
    }

    private static void finishCombo() {
        if(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())){
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }

    private static void startCombo() {
        if(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.onTick(mc.gameSettings.keyBindForward.getKeyCode());
        }
    }

}
