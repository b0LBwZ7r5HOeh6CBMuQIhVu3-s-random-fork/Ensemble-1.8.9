package it.fktcod.ktykshrk.utils;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.utils.system.Mapping;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlayerControllerUtils {

	public static void setReach(Entity entity, double range) {
		class RangePlayerController extends PlayerControllerMP {
			// private float range = (float)
			// Wrapper.INSTANCE.player().getEntityAttribute().getAttributeValue();
			public RangePlayerController(Minecraft mcIn, NetHandlerPlayClient netHandler) {
				super(mcIn, netHandler);
			}
			/*
			 * @Override public float getBlockReachDistance() { return range; } public void
			 * setBlockReachDistance(float range) { this.range = range; }
			 */
		}
		Minecraft mc = Wrapper.INSTANCE.mc();
		EntityPlayer player = Wrapper.INSTANCE.player();
		/*
		 * if (player == entity){ if (!(mc.playerController instanceof
		 * RangePlayerController)){ GameType gameType =
		 * ReflectionHelper.getPrivateValue(PlayerControllerMP.class,
		 * mc.playerController, Mapping.currentGameType); NetHandlerPlayClient netClient
		 * = ReflectionHelper.getPrivateValue(PlayerControllerMP.class,
		 * mc.playerController, Mapping.connection); RangePlayerController controller =
		 * new RangePlayerController(mc, netClient); boolean isFlying =
		 * player.capabilities.isFlying; boolean allowFlying =
		 * player.capabilities.allowFlying; controller.setGameType(gameType);
		 * player.capabilities.isFlying = isFlying; player.capabilities.allowFlying =
		 * allowFlying; mc.playerController = controller; } ((RangePlayerController)
		 * mc.playerController).setBlockReachDistance((float) range); }
		 */
	}

	public static void setIsHittingBlock(boolean isHittingBlock) {
		try {
			Field field = PlayerControllerMP.class.getDeclaredField(Mapping.isHittingBlock);
			field.setAccessible(true);
			field.setBoolean(Wrapper.INSTANCE.controller(), isHittingBlock);
		} catch (Exception ex) {
		}
	}

	public static void setBlockHitDelay(final int blockHitDelay) {
		try {
			Field field = PlayerControllerMP.class.getDeclaredField(Mapping.blockHitDelay);
			field.setAccessible(true);
			field.setInt(Wrapper.INSTANCE.controller(), blockHitDelay);
		} catch (Exception ex) {
		}
	}

	public static float getCurBlockDamageMP() {
		float getCurBlockDamageMP = 0;
		try {
			Field field = PlayerControllerMP.class.getDeclaredField(Mapping.curBlockDamageMP);
			field.setAccessible(true);
			getCurBlockDamageMP = field.getFloat(Wrapper.INSTANCE.controller());
		} catch (Exception ex) {
		}
		return getCurBlockDamageMP;
	}

	public static boolean isMoving2() {
		return ((Wrapper.INSTANCE.player().moveForward != 0.0F || Wrapper.INSTANCE.player().moveStrafing != 0.0F));
	}

	public static boolean isMoving() {
		if ((!Wrapper.INSTANCE.player().isCollidedHorizontally) && (!Wrapper.INSTANCE.player().isSneaking())) {
			return ((Wrapper.INSTANCE.player().movementInput.moveForward != 0.0F
					|| Wrapper.INSTANCE.player().movementInput.moveStrafe != 0.0F));
		}

		return false;
	}

	public static BlockPos getHypixelBlockpos(String str) {
		int val = 89;

		if (str != null && str.length() > 1) {
			char[] chs = str.toCharArray();
			int lenght = chs.length;

			for (int i = 0; i < lenght; i++) {
				val += (int) chs[i] * str.length() * str.length() + (int) str.charAt(0) + (int) str.charAt(1);
			}

			val /= str.length();
		}

		return new BlockPos(val, -val % 255, val);
	}
}
