package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import ensemble.mixin.cc.mixin.client.MixinNetworkManager;
import ensemble.mixin.cc.mixin.interfaces.INetworkManager;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventMotion;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class NoFall extends Module {
    public ModeValue mode;
    public BooleanValue VOID;
    public NumberValue delay;

    public NoFall() {
        super("NoFall", HackCategory.PLAYER);

        this.VOID = new BooleanValue("VOID", true);
        delay = new NumberValue("MLGDelay", 100.0, 1.0, 1000.0);
        this.mode = new ModeValue("Mode",
                new Mode("AAC", true),
                new Mode("Simple", false),
                new Mode("Hypixel", false),
                new Mode("AAC2", false),
                new Mode("AAC5", false), new Mode("AAC5.0.4", false), new Mode("MLG", false));

        this.addValue(mode, VOID, delay);
        this.setChinese(Core.Translate_CN[64]);
    }

    @Override
    public void onEnable() {
        aac5Check = false;
        aac5doFlag = false;
        aac5Timer = 0;
        isDmgFalling = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        aac5Check = false;
        aac5doFlag = false;
        aac5Timer = 0;
        isDmgFalling = false;
        super.onDisable();
    }

    @Override
    public String getDescription() {
        return "Gives you zero damage on fall.";
    }

    private boolean aac5doFlag = false;
    private boolean aac5Check = false;
    private int aac5Timer = 0;
    private boolean isDmgFalling = false;

    @Override
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (mode.getMode("MLG").isToggled()) {
            if (mc.thePlayer.fallDistance > 4 && getSlotWaterBucket() != -1 && isMLGNeeded()) {
                mc.thePlayer.rotationPitch = 90f;
                swapToWaterBucket(getSlotWaterBucket());
            }

            if (mc.thePlayer.fallDistance > 4 && isMLGNeeded() && !mc.thePlayer.isOnLadder() && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - BlockUtils.getDistanceToFall() - 1, mc.thePlayer.posZ);
                this.placeWater(pos, EnumFacing.UP);


                if (mc.thePlayer.getHeldItem().getItem() == Items.bucket) {
                    Thread thr = new Thread(() -> {
                        try {
                            Thread.sleep(delay.getValue().longValue());
                        } catch (Exception ignored) {
                        }
                        Wrapper.rightClickMouse(mc);
                    });
                    thr.start();
                }

                mc.thePlayer.fallDistance = 0;
            }
        }
        super.onLivingUpdate(event);
    }

    @Override
    public void onClientTick(ClientTickEvent event) {


        if (mode.getMode("Simple").isToggled()) {
            if (Wrapper.INSTANCE.player().fallDistance > 2)
                Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(true));
        }

        if (mode.getMode("Hypixel").isToggled()) {
            if (Wrapper.INSTANCE.player().fallDistance > 2)
                Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(true));
        }

        if (mode.getMode("AAC2").isToggled()) {
            if (mc.thePlayer.ticksExisted == 1 && Wrapper.INSTANCE.player().fallDistance > 2) {
                C03PacketPlayer.C04PacketPlayerPosition p = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, true);
                mc.thePlayer.sendQueue.addToSendQueue(p);
            }

        }

        if (mode.getMode("AAC5").isToggled()) {

            double offsetYs = 0.0;
            aac5Check = false;
            while (mc.thePlayer.motionY - 1.5 < offsetYs) {
                BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + offsetYs, mc.thePlayer.posZ);
                Block block = BlockUtils.getBlock(blockPos);
                AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox(mc.theWorld, blockPos, BlockUtils.getState(blockPos));
                if (axisAlignedBB != null) {
                    offsetYs = -999.9;
                    aac5Check = true;
                }
                offsetYs -= 0.5;
            }
            if (mc.thePlayer.onGround) {
                mc.thePlayer.fallDistance = -2f;
                aac5Check = false;
            }
            if (aac5Timer > 0) {
                aac5Timer -= 1;
            }
            if (aac5Check && mc.thePlayer.fallDistance > 2.5 && !mc.thePlayer.onGround) {
                aac5doFlag = true;
                aac5Timer = 18;
            } else {
                if (aac5Timer < 2) aac5doFlag = false;
            }
            if (aac5doFlag) {
                if (mc.thePlayer.onGround) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ, true));
                } else {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, true));
                }
            }

        }

        if (mode.getMode("AAC5.0.4").isToggled()) {
            if (mc.thePlayer.fallDistance > 3) {
                isDmgFalling = true;
            }
        }

        super.onClientTick(event);
    }

    @Override
    public boolean onPacket(Object packet, Side side) {
        if (side == Side.OUT) {
            if (packet instanceof C03PacketPlayer) {
                final C03PacketPlayer p = (C03PacketPlayer) packet;

                if (mode.getMode("AAC").isToggled()) {
                    Field field = ReflectionHelper.findField(C03PacketPlayer.class,
                            new String[]{"onGround", "field_149474_g"});
                    try {

                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }

                        field.setBoolean(p, true);

                    } catch (Exception e) {
                        ////System.out.println(e);
                    }
                } else if (mode.getMode("AAC5.0.4").isToggled() && isDmgFalling) {
                    Field field = ReflectionHelper.findField(C03PacketPlayer.class,
                            new String[]{"onGround", "field_149474_g"});
                    Field fx = ReflectionHelper.findField(C03PacketPlayer.class, new String[]{"x", "field_149479_a"});
                    Field fy = ReflectionHelper.findField(C03PacketPlayer.class, new String[]{"y", "field_149477_b"});
                    Field fz = ReflectionHelper.findField(C03PacketPlayer.class, new String[]{"z", "field_149478_c"});
                    try {

                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        if (!fx.isAccessible()) {
                            fx.setAccessible(true);
                        }
                        if (!fy.isAccessible()) {
                            fy.setAccessible(true);
                        }
                        if (!fz.isAccessible()) {
                            fz.setAccessible(true);
                        }

                        if (field.getBoolean(p) && Wrapper.INSTANCE.player().onGround) {
                            isDmgFalling = false;
                            field.setBoolean(p, true);
                            Wrapper.INSTANCE.player().onGround = false;
                            double y = fy.getDouble(p);
                            double x = fx.getDouble(p);
                            double z = fx.getDouble(p);
                            fy.setDouble(p, y + 1.0);
                            // ChatUtils.message("ok");
                            Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1.0784, z, false));
                            Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.5, z, true));
                        }

                    } catch (Exception e) {
                        ////System.out.println(e);
                    }
                }

            }

        }
        return true;
    }

    @Override
    public void onMotionUpdate(EventMotion event) {
        if (mode.getMode("Hypixel").isToggled()) {
            if (event.isPre()) {
                if (Wrapper.INSTANCE.player().capabilities.isFlying
                        || Wrapper.INSTANCE.player().capabilities.disableDamage
                        || Wrapper.INSTANCE.player().motionY >= 0.0d)
                    return;

                if (Wrapper.INSTANCE.player().fallDistance > 3.0f)
                    if ((!VOID.getValue() || this.isBlockUnderJudge())) {
                        INetworkManager manager = (INetworkManager) Wrapper.INSTANCE.mc().getNetHandler()
                                .getNetworkManager();
                        manager.sendPacketNoEvent(new C03PacketPlayer(true));
                    }

            }
        }
        super.onMotionUpdate(event);
    }

    @Override
    public void onPacketEvent(EventPacket event) {
        if (!mode.getMode("Hypixel").isToggled())
            return;


        if (event.getPacket() instanceof C03PacketPlayer && event.getType() == EventType.SEND) {

            if (Wrapper.INSTANCE.player().capabilities.isFlying || Wrapper.INSTANCE.player().capabilities.disableDamage
                    || Wrapper.INSTANCE.player().motionY >= 0.0d)
                return;

            C03PacketPlayer e = (C03PacketPlayer) event.getPacket();

            if (e.isMoving()) {

                if ((Wrapper.INSTANCE.player().fallDistance > 2.0f) && (!VOID.getValue() || this.isBlockUnderJudge())) {

                    ((EventPacket) event).setCancelled(true);
                    INetworkManager manager = (INetworkManager) Wrapper.INSTANCE.mc().getNetHandler()
                            .getNetworkManager();

                    manager.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(e.getPositionX(),
                            e.getPositionY(), e.getPositionZ(), e.isOnGround()));

                    // Do not send Pos Look Packet

                }

            }
        }
    }

    private boolean isBlockUnderJudge() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }

        return false;
    }


    private void swapToWaterBucket(int blockSlot) {
        mc.thePlayer.inventory.currentItem = blockSlot;
        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(blockSlot));
    }

    private int getSlotWaterBucket() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].getItem().getUnlocalizedName().contains("bucketWater"))
                return i;
        }
        return -1;
    }

    private void placeWater(BlockPos pos, EnumFacing facing) {
        ItemStack heldItem = mc.thePlayer.inventory.getCurrentItem();
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), pos, facing, new Vec3((double) pos.getX() + 0.5, (double) pos.getY() + 1, (double) pos.getZ() + 0.5));
        if (heldItem != null) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, heldItem);
            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
        }
    }

    private boolean isMLGNeeded() {
        if (mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE || mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR || mc.thePlayer.capabilities.isFlying || mc.thePlayer.capabilities.allowFlying)
            return false;

        for (double y = mc.getMinecraft().thePlayer.posY; y > 0.0; --y) {
            final Block block = BlockUtils.getBlock(new BlockPos(mc.getMinecraft().thePlayer.posX, y, mc.getMinecraft().thePlayer.posZ));
            if (block.getMaterial() == Material.water) {
                return false;
            }

            if (block.getMaterial() != Material.air)
                return true;

            if (y < 0.0) {
                break;
            }
        }

        return true;
    }
}
