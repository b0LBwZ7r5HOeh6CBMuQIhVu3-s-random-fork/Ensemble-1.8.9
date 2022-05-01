package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.event.EventMove;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.PlayerUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.RotationUtil;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TargetStrafe extends Module {

	// SkidSense
	public static boolean dire = true;
	public static NumberValue range;
	public static BooleanValue key;
	public static BooleanValue manual;

	NumberValue motionXZ;

	TimerUtils timer = new TimerUtils();

	// AutoRound
	public final double Pi = 6.283185307179586;

	private static int strafes;

	public static EntityLivingBase player;

	public TargetStrafe() {
		super("TargetStrafe", HackCategory.COMBAT);
		range = new NumberValue("Range", 2D, 0.5D, 4.5D);
		key = new BooleanValue("Key", false);
		manual = new BooleanValue("Manual", false);
		motionXZ = new NumberValue("MotionXZ", 0.28, 0.01, 0.60);
		addValue(range, key, manual, motionXZ);
		this.setChinese(Core.Translate_CN[92]);
	}


	@Override
	public void onEnable() {
		strafes = -1;
		super.onEnable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (KillAura.getTarget() != null || TpAura.T != null) {

			if (KillAura.getTarget() != null) {
				player = KillAura.getTarget();
			} else if (TpAura.T != null) {
				player = TpAura.T;
			}

			if (Math.sqrt(
					Math.pow(mc.thePlayer.posX - player.posX, 2) + Math.pow(mc.thePlayer.posZ - player.posZ, 2)) != 0) {
				double c1 = (mc.thePlayer.posX - player.posX) / (Math.sqrt(
						Math.pow(mc.thePlayer.posX - player.posX, 2) + Math.pow(mc.thePlayer.posZ - player.posZ, 2)));
				double s1 = (mc.thePlayer.posZ - player.posZ) / (Math.sqrt(
						Math.pow(mc.thePlayer.posX - player.posX, 2) + Math.pow(mc.thePlayer.posZ - player.posZ, 2)));
				if (Math.sqrt(Math.pow(mc.thePlayer.posX - player.posX, 2)
						+ Math.pow(mc.thePlayer.posZ - player.posZ, 2)) <= range.getValue()) {
					if (Wrapper.INSTANCE.mcSettings().keyBindBack.isKeyDown()) {
						mc.thePlayer.motionX = -motionXZ.getValue() * s1 - 0.18 * motionXZ.getValue() * c1;
						mc.thePlayer.motionZ = motionXZ.getValue() * c1 - 0.18 * motionXZ.getValue() * s1;
					} else {
						mc.thePlayer.motionX = motionXZ.getValue() * s1 - 0.18 * motionXZ.getValue() * c1;
						mc.thePlayer.motionZ = -motionXZ.getValue() * c1 - 0.18 * motionXZ.getValue() * s1;
					}
				}
			}

		}
		super.onClientTick(event);
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		if (Wrapper.INSTANCE.player().movementInput.moveStrafe != 0 && manual.getValue()) {
			strafes = Wrapper.INSTANCE.player().movementInput.moveStrafe > 0 ? 1 : -1;
			return;
		}

		if (Wrapper.INSTANCE.player().isCollidedHorizontally
				|| (KillAura.target != null
						&& Wrapper.INSTANCE.player().posY > KillAura.target.getEntityBoundingBox().maxY)
				|| !isBlockUnder()) {
			strafes *= -1;
		}
		super.onPlayerTick(event);
	}

//skid skid skid skid skid boy
	private static boolean isBlockUnder() {
		for (int i = (int) (Minecraft.getMinecraft().thePlayer.posY - 1.0); i > 0; --i) {
			BlockPos pos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, i,
					Minecraft.getMinecraft().thePlayer.posZ);
			if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockAir)
				continue;
			return true;
		}
		return false;
	}

	public static boolean canStrafe() {
		boolean press = !key.getValue() || Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown();

		return (KillAura.target != null) && press
				&& (HackManager.getHack("KillAura").isToggled() && (HackManager.getHack("Speed").isToggled()));
	}

	public static boolean doingStrafe(EventMove event, double moveSpeed) {
		if (PlayerUtils.MovementInput()
				&& (!key.getValue() || Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()))
				&& KillAura.target != null) {
			float yaw = RotationUtil.getRotations(KillAura.target)[0];
			float strafe = mc.thePlayer.getDistanceToEntity(KillAura.target) > (range.getValue().floatValue() + 1) ? 0
					: strafes;
			float forward = mc.thePlayer.getDistanceToEntity(KillAura.target) <= range.getValue().floatValue() ? 0 : 1;
			if (forward == 0 && strafe == 0) {
				event.setX(0);
				event.setZ(0);
			} else {
				if (forward != 0) {
					if (strafe > 0) {
						yaw += (forward > 0 ? -45 : 45);
					} else if (strafe < 0) {
						yaw += (forward > 0 ? 45 : -45);
					}
					strafe = 0;
					if (forward > 0) {
						forward = 1;
					} else {
						forward = -1;
					}
				}
				final double cos = Math.cos(Math.toRadians(yaw + 90)), sin = Math.sin(Math.toRadians(yaw + 90));
				event.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
				event.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
			}
			return true;
		}
		return false;
	}

}
