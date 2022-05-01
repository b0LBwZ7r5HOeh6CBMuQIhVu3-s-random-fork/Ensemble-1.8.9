package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import ensemble.mixin.cc.mixin.client.MixinC03Packet;
import ensemble.mixin.cc.mixin.interfaces.IC03Packet;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class AntiVoid extends Module {
	TimerUtils timer = new TimerUtils();
	NumberValue height;
	ModeValue mode;
	BooleanValue void_;

	public AntiVoid() {
		super("AntiVoid", HackCategory.PLAYER);
		this.mode = new ModeValue("Mode", new Mode("Hypixel", true), new Mode("Basic", false));
		this.height = new NumberValue("Height", 2d, 1d, 10d);
		this.void_ = new BooleanValue("Void", false);
		this.addValue(height, void_, mode);
		this.setChinese(Core.Translate_CN[7]);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {

		if (mode.getMode("Basic").isToggled()) {
			if (!isBlockUnder()) {
				Jump();
			}
		}
		super.onClientTick(event);
	}

	@Override
	public void onPacketEvent(EventPacket event) {
		if (event.getType() == EventType.SEND) {
			if (Wrapper.INSTANCE.player().fallDistance >= height.getValue().floatValue()
					&& mode.getMode("Hypixel").isToggled()) {
				if ((!void_.getValue()) || !isBlockUnder()) {
					if (event.getPacket() instanceof C03PacketPlayer) {
						IC03Packet IC03 = (IC03Packet) event.getPacket();
						double y = IC03.getY();
						IC03.setY((float) (y += height.getValue() * 2));
					}
				}
			}

		}
		super.onPacketEvent(event);
	}
	/*
	 * @Override public boolean onPacket(Object packet, Side side) { if
	 * (mode.getMode("Hypixel").isToggled()) { if (side == Side.OUT && packet
	 * instanceof C03PacketPlayer && !isBlockBelow()) { Field field =
	 * ReflectionHelper.findField(C03PacketPlayer.class, new String[] { "y",
	 * "field_149477_b" }); try {
	 * 
	 * if (!field.isAccessible()) { field.setAccessible(true); } double y =
	 * field.getDouble(packet); field.setDouble(packet, y += 11);
	 * 
	 * } catch (Exception e) { ////System.out.println(e); } }
	 * 
	 * } return super.onPacket(packet, side); }
	 */

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	private Boolean isBlockBelow() {
		for (int i = (int) (Wrapper.INSTANCE.player().posY - 1.0); i > 0; --i) {
			final BlockPos pos = new BlockPos(Wrapper.INSTANCE.player().posX, i, Wrapper.INSTANCE.player().posZ);
			if (!(Wrapper.INSTANCE.world().getBlockState(pos).getBlock() instanceof BlockAir)) {
				return true;
			}
		}
		return false;

	}

	void Jump() {
		Utils.nullCheck();
		Wrapper.INSTANCE.player().motionY = height.getValue();
	}

	private boolean isBlockUnder() {
		if (mc.thePlayer.posY < 0)
			return false;
		for (int off = 0; off < (int) mc.thePlayer.posY + 2; off += 2) {
			AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -off, 0);
			if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
