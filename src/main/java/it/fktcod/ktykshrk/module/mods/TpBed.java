package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventRenderBlock;
import it.fktcod.ktykshrk.event.EventWorld;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.AStarCustomPathFinder;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.math.Vec3;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class TpBed extends Module {
	public BlockPos playerBed;
	public BlockPos fuckingBed;
	public ArrayList<BlockPos> posList=new ArrayList<BlockPos>();
	public NumberValue TpDelay;
	TimerUtils timer = new TimerUtils();
	private ArrayList<Vec3> path = new ArrayList<>();
	
	public TpBed() {
		super("TPBed", HackCategory.MOVEMENT);
		TpDelay = new NumberValue("TpDelay", 600.0D, 200.0D, 3000D);
		this.addValue(TpDelay);
		// TODO Auto-generated constructor stub
		this.setChinese(Core.Translate_CN[98]);
	}
	@Override
	public void onDisable() {
		Wrapper.canSendMotionPacket = true;
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		try {
			posList.sort((o1, o2) -> {
				double distance1 = getDistanceToBlock(o1);
				double distance2 = getDistanceToBlock(o2);
				return (int) (distance1 - distance2);
			});
			
			if (posList.size() < 3) {
				//this.setToggled(false);
			}
			ArrayList<BlockPos> posListFor = new ArrayList<BlockPos>(posList);
			int index = 1;
			for (BlockPos kid : posListFor) {
				index++;
				if (index % 2 == 1) {
					posList.remove(kid);
				}
			}

			playerBed = posList.get(0);
			posList.remove(0);
			if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)) {
				for (int i = 0; i < 49; i++) {
					mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX, mc.thePlayer.posY + 0.06249D, mc.thePlayer.posZ, false));
					mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
				}
				mc.thePlayer.onGround = false;
				MoveUtils.setMotion(0);
				mc.thePlayer.jumpMovementFactor = 0;
			}
			fuckingBed = posList.get(0);
		}catch (Exception e) {
			// TODO: handle exception
		}
		super.onEnable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(posList.isEmpty())
			return;
		
		posList.removeIf(pos -> !(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockBed));
		for (BlockPos pos : posList)
			if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed)) {
				posList.remove(pos);
				posList.sort((o1, o2) -> {
					double distance1 = getDistanceToBlock(o1);
					double distance2 = getDistanceToBlock(o2);
					return (int) (distance1 - distance2);
				});
				fuckingBed = posList.get(0);
			}

		
		if (mc.thePlayer.getDistance(fuckingBed.getX(), fuckingBed.getY(), fuckingBed.getZ()) < 4) {
			Wrapper.canSendMotionPacket = true;
			this.setToggled(false);
		}

		if (timer.isDelayComplete(TpDelay.getValue())) {
			Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			Vec3 to = new Vec3(fuckingBed.getX() + 1, fuckingBed.getY(), fuckingBed.getZ() + 1);

			path = computePath(topFrom, to);

			if (mc.thePlayer.getDistance(fuckingBed.getX(), fuckingBed.getY(), fuckingBed.getZ()) > 4) {
				mc.thePlayer.setPosition(fuckingBed.getX(), fuckingBed.getY()+2, fuckingBed.getZ());
			}

			timer.reset();
		}
		if (posList.size() == 0) {
			this.setToggled(false);
		}
		super.onClientTick(event);
	}
	
	
	public double getDistanceToBlock(BlockPos pos) {
		return mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
	}

	private boolean canPassThrow(BlockPos pos) {
		Block block = Minecraft.getMinecraft().theWorld
				.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
		return block.getMaterial() == Material.air || block.getMaterial() == Material.plants
				|| block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water
				|| block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
	}
	
	private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
		if (!canPassThrow(new BlockPos(topFrom.mc2()))) {
			topFrom = topFrom.addVector(0, 1, 0);
		}
		AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
		pathfinder.compute();

		int i = 0;
		Vec3 lastLoc = null;
		Vec3 lastDashLoc = null;
		ArrayList<Vec3> path = new ArrayList<Vec3>();
		ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
		for (Vec3 pathElm : pathFinderPath) {
			if (i == 0 || i == pathFinderPath.size() - 1) {
				if (lastLoc != null) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
				}
				path.add(pathElm.addVector(0.5, 0, 0.5));
				lastDashLoc = pathElm;
			} else {
				boolean canContinue = true;
				if (pathElm.squareDistanceTo(lastDashLoc) > 5 * 5) {
					canContinue = false;
				} else {
					double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
					double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
					double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
					double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
					double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
					double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
					cordsLoop: for (int x = (int) smallX; x <= bigX; x++) {
						for (int y = (int) smallY; y <= bigY; y++) {
							for (int z = (int) smallZ; z <= bigZ; z++) {
								if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
									canContinue = false;
									break cordsLoop;
								}
							}
						}
					}
				}
				if (!canContinue) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
					lastDashLoc = lastLoc;
				}
			}
			lastLoc = pathElm;
			i++;
		}
		return path;
	}
	
	@Override
	public void onWorld(EventWorld event) {
		posList.clear();
		super.onWorld(event);
	}
	
	@Override
	public void onBlockRender(EventRenderBlock e) {
		 BlockPos pos = new BlockPos(e.x, e.y, e.z);
	      if( !posList.contains(pos)&&e.block instanceof BlockBed) {
	    	  Core.logger.info("TPbed Block");
	    	  posList.add(pos);
	      }
		super.onBlockRender(e);
	}
	
}

class PlayerPos {
	double x;
	double y;
	double z;

	public PlayerPos(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}
	
	
}
