package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import static it.fktcod.ktykshrk.utils.MoveUtils.isMoving;

public class SuperKB extends Module {


    private NumberValue delayValue = new NumberValue("Delay", 0D, 0D, 500D);
    private NumberValue hurtTimeValue = new NumberValue("HurtTime", 10D, 0D, 10D);
    private ModeValue modeValue = new ModeValue("Mode", new Mode("ExtraPacket",false), new Mode("WTap",false), new Mode("Packet",false));
    private BooleanValue onlyMoveValue = new BooleanValue("OnlyMove", false);
    private BooleanValue onlyGroundValue = new BooleanValue("OnlyGround", false);


    TimerUtils timer = new TimerUtils();

    public SuperKB() {
        super("SuperKB", HackCategory.PLAYER);
        addValue(delayValue,hurtTimeValue,modeValue,onlyMoveValue,onlyGroundValue);
    }

    @Override
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.target instanceof EntityLivingBase) {
            if (((EntityLivingBase) event.target).hurtTime > hurtTimeValue.getValue() || !timer.hasReached(delayValue.getValue().longValue()) ||
                    (!isMoving() && onlyMoveValue.getValue()) || (!mc.thePlayer.onGround && onlyGroundValue.getValue())) {
                return;
            }
            switch (modeValue.getSelectMode().getName().toLowerCase()) {
                case "extrapacket" : {
                    if (mc.thePlayer.isSprinting()) {
                        mc.thePlayer.setSprinting(true);
                    }
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }

                case "wtap" : {
                    if (mc.thePlayer.isSprinting()) {
                        mc.thePlayer.setSprinting(false);
                    }
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }
                case "packet" : {
                    if (mc.thePlayer.isSprinting()) {
                        mc.thePlayer.setSprinting(true);
                    }
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }
            }
            mc.thePlayer.setSprinting(true);
            timer.reset();
        }
    }
}