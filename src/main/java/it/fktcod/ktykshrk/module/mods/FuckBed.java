package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FuckBed extends Module {
	public static BlockPos blockBreaking;
	TimerUtils timer = new TimerUtils();
	List<BlockPos> beds = new ArrayList<>();

	public BooleanValue instant;
	public BooleanValue ground;

	public FuckBed() {
		super("FuckBed", HackCategory.PLAYER);
		instant = new BooleanValue("Instant", false);
		ground = new BooleanValue("OnGround", true);
		// TODO Auto-generated constructor stub
		this.addValue(instant, ground);
		this.setChinese(Core.Translate_CN[46]);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (mc.theWorld == null || mc.thePlayer == null)
			return;
		int reach = 6;
		for (int y = reach; y >= -reach; --y) {
			for (int x = -reach; x <= reach; ++x) {
				for (int z = -reach; z <= reach; ++z) {
					if (mc.thePlayer.isSneaking()) {
						return;
					}
					BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
					if (blockChecks(mc.theWorld.getBlockState(pos).getBlock())
							&& mc.thePlayer.getDistance(mc.thePlayer.posX + x, mc.thePlayer.posY + y,
									mc.thePlayer.posZ + z) < mc.playerController.getBlockReachDistance() - 0.2) {
						if (!beds.contains(pos))
							beds.add(pos);
					}
				}
			}
		}
		BlockPos closest = null;
		if (!beds.isEmpty())
			for (int i = 0; i < beds.size(); i++) {
				BlockPos bed = beds.get(i);



				if (mc.thePlayer.getDistance(bed.getX(), bed.getY(),
						bed.getZ()) > mc.playerController.getBlockReachDistance() - 0.2
						|| mc.theWorld.getBlockState(bed).getBlock() != Blocks.bed) {
					beds.remove(i);
				}
				if (closest == null || (mc.thePlayer.getDistance(bed.getX(), bed.getY(), bed.getZ()) < mc.thePlayer
						.getDistance(closest.getX(), closest.getY(), closest.getZ()))
						&& mc.thePlayer.ticksExisted % 50 == 0) {
					closest = bed;
				}
			}

		if (closest != null) {
			float[] rot = getRotations(closest, getClosestEnum(closest));
			blockBreaking = closest;
			return;
		}

		blockBreaking = null;
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		if (blockBreaking != null) {

			if (instant.getValue()) {

				mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockBreaking, EnumFacing.DOWN));

				mc.thePlayer.swingItem();

				mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockBreaking, EnumFacing.DOWN));
			} else {
				Field field = ReflectionHelper.findField(PlayerControllerMP.class,
						new String[] { "curBlockDamageMP", "field_78770_f" });
				Field blockdelay = ReflectionHelper.findField(PlayerControllerMP.class,
						new String[] { "blockHitDelay", "field_78781_i" });
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					if (!blockdelay.isAccessible()) {
						blockdelay.setAccessible(true);
					}
					if (field.getFloat(Wrapper.INSTANCE.mc().playerController) > 1) {
						blockdelay.setInt(Wrapper.INSTANCE.mc().playerController, 1);
					}
					mc.thePlayer.swingItem();
					EnumFacing direction = getClosestEnum(blockBreaking);
					if (direction != null) {
						if (ground.getValue()) {
							Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(true));
						}

						mc.playerController.onPlayerDamageBlock(blockBreaking, direction);
					}
				} catch (Exception e) {

				}

			}
		}
		super.onPlayerTick(event);
	}

	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (blockBreaking != null) {
			GlStateManager.pushMatrix();
			GlStateManager.disableDepth();
			RenderUtils.drawBoundingBox(blockBreaking.getX() - mc.getRenderManager().viewerPosX + .5f,
					blockBreaking.getY() - mc.getRenderManager().viewerPosY,
					blockBreaking.getZ() - mc.getRenderManager().viewerPosZ + .5f, 0.5, 0.5625, 1, 0, 0, 0.25f);
			GlStateManager.enableDepth();
			GlStateManager.popMatrix();
		}
		super.onRenderWorldLast(event);
	}

	private boolean blockChecks(Block block) {
		return block == Blocks.bed;
	}

	public float[] getRotations(BlockPos block, EnumFacing face) {
		double x = block.getX() + 0.5 - mc.thePlayer.posX;
		double z = block.getZ() + 0.5 - mc.thePlayer.posZ;
		double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (block.getY() + 0.5);
		double d3 = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
		if (yaw < 0.0F) {
			yaw += 360f;
		}
		return new float[] { yaw, pitch };
	}

	private EnumFacing getClosestEnum(BlockPos pos) {
		EnumFacing closestEnum = EnumFacing.UP;
		float rotations = MathHelper.wrapAngleTo180_float(getRotations(pos, EnumFacing.UP)[0]);
		if (rotations >= 45 && rotations <= 135) {
			closestEnum = EnumFacing.EAST;
		} else if ((rotations >= 135 && rotations <= 180) || (rotations <= -135 && rotations >= -180)) {
			closestEnum = EnumFacing.SOUTH;
		} else if (rotations <= -45 && rotations >= -135) {
			closestEnum = EnumFacing.WEST;
		} else if ((rotations >= -45 && rotations <= 0) || (rotations <= 45 && rotations >= 0)) {
			closestEnum = EnumFacing.NORTH;
		}
		if (MathHelper.wrapAngleTo180_float(getRotations(pos, EnumFacing.UP)[1]) > 75
				|| MathHelper.wrapAngleTo180_float(getRotations(pos, EnumFacing.UP)[1]) < -75) {
			closestEnum = EnumFacing.UP;
		}
		return closestEnum;
	}

	private EnumFacing getFacingDirection(BlockPos pos) {
		EnumFacing direction = null;
		if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isFullCube()
				&& !(mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockBed)) {
			direction = EnumFacing.UP;
		} else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isFullCube()
				&& !(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockBed)) {
			direction = EnumFacing.DOWN;
		} else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isFullCube()
				&& !(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() instanceof BlockBed)) {
			direction = EnumFacing.EAST;
		} else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isFullCube()
				&& !(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof BlockBed)) {
			direction = EnumFacing.WEST;
		} else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube()
				&& !(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() instanceof BlockBed)) {
			direction = EnumFacing.SOUTH;
		} else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube()
				&& !(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() instanceof BlockBed)) {
			direction = EnumFacing.NORTH;
		}
		MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(
				new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
				new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
		if (rayResult != null && rayResult.getBlockPos() == pos) {
			return rayResult.sideHit;
		}
		return direction;
	}
}
