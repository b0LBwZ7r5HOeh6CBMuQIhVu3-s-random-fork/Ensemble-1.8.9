package it.fktcod.ktykshrk.module.mods.addon;

import java.util.ArrayList;
import java.util.Random;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.mods.Criticals;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.pathfinder.NodeProcessor;
import net.minecraftforge.client.model.b3d.B3DModel.Node;

public class Infinite {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static Random rand = new Random();

	public static boolean spectator;

	public static ArrayList<Entity> blackList = new ArrayList<Entity>();

	static double x;
	static double y;
	static double z;
	static double xPreEn;
	static double yPreEn;
	static double zPreEn;
	static double xPre;
	static double yPre;
	static double zPre;

	public static boolean infiniteReach(double range, double maxXZTP, double maxYTP, ArrayList<Vec3> positionsBack,
			ArrayList<Vec3> positions, EntityLivingBase en) {

		int ind = 0;
		xPreEn = en.posX;
		yPreEn = en.posY;
		zPreEn = en.posZ;
		xPre = mc.thePlayer.posX;
		yPre = mc.thePlayer.posY;
		zPre = mc.thePlayer.posZ;
		boolean attack = true;
		boolean up = false;
		boolean tpUpOneBlock = false;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;

		boolean sneaking = mc.thePlayer.isSneaking();

		positions.clear();
		positionsBack.clear();

		// preInfiniteReach(range, maxXZTP, maxYTP, positionsBack, positions, new
		// Vec3(en.posX, en.posY, en.posZ), tpStraight, up, attack, tpUpOneBlock,
		// sneaking);
		double step = maxXZTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxXZTP * steps);
			if (maxXZTP * steps > range) {
				break;
			}
		}
		MovingObjectPosition rayTrace = null;
		MovingObjectPosition rayTrace1 = null;
		MovingObjectPosition rayTraceCarpet = null;
		if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
				new Vec3(en.posX, en.posY, en.posZ), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
						new Vec3(en.posX, en.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
					new Vec3(en.posX, mc.thePlayer.posY, en.posZ), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
									mc.thePlayer.posZ),
							new Vec3(en.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
							true)) != null) {
				MovingObjectPosition trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.thePlayer.posY;
					// yPreEn = mc.thePlayer.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							MovingObjectPosition tr = rayTracePos(
									new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
									new Vec3(en.posX, target.getY() + i, en.posZ), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							Block block = mc.theWorld.getBlockState(blockPos).getBlock();
							if (block.getMaterial() != Material.air) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.thePlayer.posX - xPreEn;
						double difZ = mc.thePlayer.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return false;
						}
					} else {
						attack = false;
						return false;
					}
				}
			} else {
				MovingObjectPosition ent = rayTracePos(
						new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
						new Vec3(en.posX, en.posY, en.posZ), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.thePlayer.posY;
					yPreEn = mc.thePlayer.posY;
				} else {
					y = mc.thePlayer.posY;
					yPreEn = en.posY;
				}

			}
		}
		if (!attack) {
			return false;
		}
		if (sneaking) {
			Wrapper.INSTANCE
					.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.thePlayer.posX;
				y = yPreEn;
				z = mc.thePlayer.posZ;
				sendPacket(false, positionsBack, positions);
			}
			if (i != steps - 1) {
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
			} else {
				// if last teleport
				{
					double difX = mc.thePlayer.posX - xPreEn;
					double difY = mc.thePlayer.posY - yPreEn;
					double difZ = mc.thePlayer.posZ - zPreEn;
					double divider = step * i;
					x = mc.thePlayer.posX - difX * divider;
					y = mc.thePlayer.posY - difY * (up ? 1 : divider);
					z = mc.thePlayer.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - en.posY;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				}
				if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistanceToEntity(en) >= 4) {
					x = xPreEn;
					y = en.posY;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
					if (HackManager.getHack("MegaKnockback").isToggled() && en.onGround) {
						for (int ii = 0; ii < 300; ii++) {
							mc.getNetHandler().getNetworkManager()
									.sendPacket(new C03PacketPlayer(mc.thePlayer.onGround));
						}
					}

					doattack(en);

				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).xCoord;
				y = positions.get(i).yCoord;
				z = positions.get(i).zCoord;
			}
			sendPacket(false, positionsBack, positions);
		}
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		sendPacket(false, positionsBack, positions);
		if (!attack) {
			if (sneaking) {
				Wrapper.INSTANCE.sendPacket(
						new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			return false;
		}
		if (sneaking) {
			Wrapper.INSTANCE
					.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
		return true;
	}

	public static boolean rayTraceWide(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid,
			boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
		float yaw = getFacePosRemote(vec32, vec31)[0];
		yaw = normalizeAngle(yaw);
		yaw += 180;
		yaw = MathHelper.wrapAngleTo180_float(yaw);
		double angleA = Math.toRadians(yaw);
		double angleB = Math.toRadians(yaw + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size);
		Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size);
		Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size);
		Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size);
		Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size2);
		Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size2);
		Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size2);
		Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size2);
		// MovingObjectPosition trace4 = mc.theWorld.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// MovingObjectPosition trace5 = mc.theWorld.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace4 = null;
		MovingObjectPosition trace5 = null;
		if (returnLastUncollidableBlock) {
			return (trace1 != null && getBlock(trace1.getBlockPos()).getMaterial() != Material.air)
					|| (trace2 != null && getBlock(trace2.getBlockPos()).getMaterial() != Material.air)
					|| (trace3 != null && getBlock(trace3.getBlockPos()).getMaterial() != Material.air)
					|| (trace4 != null && getBlock(trace4.getBlockPos()).getMaterial() != Material.air)
					|| (trace5 != null && getBlock(trace5.getBlockPos()).getMaterial() != Material.air);
		} else {
			return trace1 != null || trace2 != null || trace3 != null || trace5 != null || trace4 != null;
		}

	}

	public static double normalizeAngle(double angle) {
		return (angle + 360) % 360;
	}

	public static float normalizeAngle(float angle) {
		return (angle + 360) % 360;
	}

	public static float[] getFacePosRemote(Vec3 src, Vec3 dest) {
		double diffX = dest.xCoord - src.xCoord;
		double diffY = dest.yCoord - (src.yCoord);
		double diffZ = dest.zCoord - src.zCoord;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
	}

	public static Block getBlock(BlockPos pos) {
		return mc.theWorld.getBlockState(pos).getBlock();
	}

	public static void sendPacket(boolean goingBack, ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions) {
		C03PacketPlayer.C04PacketPlayerPosition playerPacket = new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true);
		Wrapper.INSTANCE.sendPacket(playerPacket);
		if (goingBack) {
			positionsBack.add(new Vec3(x, y, z));
			return;
		}
		positions.add(new Vec3(x, y, z));
	}

	@SuppressWarnings("unused")
	public static MovingObjectPosition rayTracePos(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid,
			boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
		float[] rots = getFacePosRemote(vec32, vec31);
		float yaw = rots[0];
		double angleA = Math.toRadians(normalizeAngle(yaw));
		double angleB = Math.toRadians(normalizeAngle(yaw) + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size);
		Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size);
		Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size);
		Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size);
		Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleA) * size2);
		Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
				vec31.zCoord + Math.sin(angleB) * size2);
		Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleA) * size2);
		Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
				vec32.zCoord + Math.sin(angleB) * size2);
		if (false) {
			MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
					ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
			return trace2;
		}
		// MovingObjectPosition trace4 = mc.theWorld.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// MovingObjectPosition trace5 = mc.theWorld.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		// positionsBack.add(rightA);
		// positionsBack.add(right2A);
		// positionsBack.add(leftA);
		// positionsBack.add(left2A);
		MovingObjectPosition trace4 = null;
		MovingObjectPosition trace5 = null;
		if (trace2 != null || trace1 != null || trace3 != null || trace4 != null || trace5 != null) {
			if (returnLastUncollidableBlock) {
				if (trace5 != null
						&& (getBlock(trace5.getBlockPos()).getMaterial() != Material.air || trace5.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace5;
				}
				if (trace4 != null
						&& (getBlock(trace4.getBlockPos()).getMaterial() != Material.air || trace4.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace4;
				}
				if (trace3 != null
						&& (getBlock(trace3.getBlockPos()).getMaterial() != Material.air || trace3.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null
						&& (getBlock(trace1.getBlockPos()).getMaterial() != Material.air || trace1.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null
						&& (getBlock(trace2.getBlockPos()).getMaterial() != Material.air || trace2.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			} else {
				if (trace5 != null) {
					return trace5;
				}
				if (trace4 != null) {
					return trace4;
				}
				if (trace3 != null) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			}
		}
		if (trace2 == null) {
			if (trace3 == null) {
				if (trace1 == null) {
					if (trace5 == null) {
						if (trace4 == null) {
							return null;
						}
						return trace4;
					}
					return trace5;
				}
				return trace1;
			}
			return trace3;
		}
		return trace2;
	}

	public static void doattack(EntityLivingBase en) {
		Criticals.crit(x, y, z);
		Criticals.disable = true;
		mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(en, C02PacketUseEntity.Action.ATTACK));
		Wrapper.INSTANCE.player().swingItem();
		Criticals.disable = false;
	}

	public static TeleportResult pathFinderTeleportTo(Vec3 from, Vec3 to) {
		boolean sneaking = mc.thePlayer.isSneaking() ;
		ArrayList<Vec3> positions = new ArrayList<Vec3>();
		ArrayList<it.fktcod.ktykshrk.utils.NodeProcessor.Node> triedPaths = new ArrayList<it.fktcod.ktykshrk.utils.NodeProcessor.Node>();
//		////System.out.println(to.toString());
		BlockPos targetBlockPos = new BlockPos(getBlockPos(to));
		BlockPos fromBlockPos = getBlockPos(from);
		if(!it.fktcod.ktykshrk.utils.NodeProcessor.isPassable(getBlock(fromBlockPos))) {
			
			float angle = (float) Math.toDegrees(Math.atan2(to.zCoord - from.zCoord, to.xCoord - from.xCoord));
			
			angle += 180;
		    if(angle < 0){
		        angle += 360;
		    }
			////System.out.println(angle);
		    from = getVec3(fromBlockPos.offset(EnumFacing.fromAngle(normalizeAngle(angle))).add(0.5, 0, 0.5));
		}
		
		BlockPos finalBlockPos = targetBlockPos;
		boolean passable = true;
		if(!it.fktcod.ktykshrk.utils.NodeProcessor.isPassable(getBlock(targetBlockPos))) {
			finalBlockPos = targetBlockPos.up();
			boolean lastIsPassable;
			if(!(lastIsPassable = it.fktcod.ktykshrk.utils.NodeProcessor.isPassable(getBlock(targetBlockPos.up())))) {
				finalBlockPos = targetBlockPos.up(2);
				if(!lastIsPassable) {
					passable = false;
				}
			}
		}
		
		if(!passable) {
			float angle = (float) Math.toDegrees(Math.atan2(to.zCoord - finalBlockPos.getZ(), to.xCoord - finalBlockPos.getX()));
			
			angle += 180;
		    if(angle < 0){
		        angle += 360;
		    }
		    finalBlockPos = targetBlockPos.offset(EnumFacing.fromAngle(normalizeAngle(angle)));
		}
		
		it.fktcod.ktykshrk.utils.NodeProcessor processor = new it.fktcod.ktykshrk.utils.NodeProcessor();
		
		processor.getPath(new BlockPos(from.xCoord, from.yCoord, from.zCoord), finalBlockPos);
		triedPaths = processor.triedPaths;
		if(processor.path == null) {
			return new TeleportResult(positions, null, triedPaths, null, null, false);
		}
		Vec3 lastPos = null;
		if (sneaking) {
			Wrapper.INSTANCE.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		for(it.fktcod.ktykshrk.utils.NodeProcessor.Node node : processor.path) {
			BlockPos pos = node.getBlockpos();
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(node.getBlockpos().getX() + 0.5, node.getBlockpos().getY(), node.getBlockpos().getZ() + 0.5, true));
			positions.add((lastPos = new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5)));
		}
		if (sneaking) {
			Wrapper.INSTANCE.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, null, triedPaths, processor.path, lastPos, true);
	}

	public static BlockPos getBlockPos(Vec3 vec) {
		return new BlockPos(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	
	public static Vec3 getVec3(BlockPos blockPos) {
		return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static TeleportResult pathFinderTeleportBack(ArrayList<Vec3> positions) {
		boolean sneaking = mc.thePlayer.isSneaking() ;
		ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
		if (sneaking) {
			Wrapper.INSTANCE.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = positions.size() - 1; i > -1; i--) {
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(positions.get(i).xCoord, positions.get(i).yCoord, positions.get(i).zCoord, true));
			positionsBack.add(positions.get(i));
		}
		if (sneaking) {
			Wrapper.INSTANCE.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, positionsBack, null, null, null, false);
	}
}
