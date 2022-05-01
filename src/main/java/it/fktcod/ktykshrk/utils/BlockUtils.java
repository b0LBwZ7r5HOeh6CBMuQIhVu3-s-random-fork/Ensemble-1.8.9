package it.fktcod.ktykshrk.utils;

import java.util.LinkedList;

import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;



public final class BlockUtils
{

	public static double getDistanceToFall() {
		double distance = 0.0;
		for (double i = mc.thePlayer.posY; i > 0.0; --i) {
			final Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ));
			if (block.getMaterial() != Material.air && block.isBlockNormalCube() && block.isCollidable()) {
				distance = i;
				break;
			}
			if (i < 0.0) {
				break;
			}
		}
		final double distancetofall = mc.thePlayer.posY - distance - 1.0;
		return distancetofall;
	}


	public static IBlockState getState(BlockPos pos)
	{
		return Wrapper.INSTANCE.world().getBlockState(pos);
	}
	
	public static Block getBlock(BlockPos pos)
	{
		return getState(pos).getBlock();
	}
	
	public static Material getMaterial(BlockPos pos)
	{
		return getState(pos).getBlock().getMaterial();
	}
	
	public static boolean canBeClicked(BlockPos pos)
	{
		return getBlock(pos).canCollideCheck(getState(pos), false);
	}
	
	public static float getHardness(BlockPos pos) {
		return getState(pos).getBlock().getPlayerRelativeBlockHardness(Wrapper.INSTANCE.player(), Wrapper.INSTANCE.world(), pos);
	}
	
	public static boolean isBlockMaterial(BlockPos blockPos, Block block) {
    	return BlockUtils.getBlock(blockPos) == Blocks.air;
    }
	
    public static boolean isBlockMaterial(BlockPos blockPos, Material material) {
    	return BlockUtils.getState(blockPos).getBlock().getMaterial() == material;
    }
	
	public static boolean placeBlockLegit(BlockPos pos)
	{
		Vec3 eyesPos = new Vec3(Wrapper.INSTANCE.player().posX,
				Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
		
		for(EnumFacing side : EnumFacing.values())
		{
			BlockPos neighbor = pos.offset(side);
			EnumFacing side2 = side.getOpposite();
			
			// check if side is visible (facing away from player)
			// TODO: actual line-of-sight check
			if(eyesPos.squareDistanceTo(
				new Vec3(pos).addVector(0.5, 0.5, 0.5)) >= eyesPos
					.squareDistanceTo(
						new Vec3(neighbor).addVector(0.5, 0.5, 0.5)))
				continue;
			
			// check if neighbor can be right clicked
			if(!getBlock(neighbor)
				.canCollideCheck(Wrapper.INSTANCE.world().getBlockState(neighbor), false))
				continue;
			
			Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5)
				.add(new Vec3(side2.getDirectionVec()));
			
			// check if hitVec is within range (4.25 blocks)
			if(eyesPos.squareDistanceTo(hitVec) > 18.0625)
				continue;
			
			// place block
			faceVectorPacket(hitVec);
			//mc.playerController.processRightClickBlock(mc.player, mc.world,
				//neighbor, side2, hitVec, EnumHand.MAIN_HAND);
			//mc.player.swingArm(EnumHand.MAIN_HAND);
			//mc.rightClickDelayTimer = 4;
			
		}
		Wrapper.INSTANCE.sendPacket(new C08PacketPlayerBlockPlacement());
		Utils.swingMainHand();
		return true;
	}
	
	public static boolean placeBlockSimple(BlockPos pos)
	{
		Vec3 eyesPos = new Vec3(Wrapper.INSTANCE.player().posX,
				Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(), Wrapper.INSTANCE.player().posZ);
		
		for(EnumFacing side : EnumFacing.values())
		{
			BlockPos neighbor = pos.offset(side);
			EnumFacing side2 = side.getOpposite();
			
			// check if neighbor can be right clicked
			if(!getBlock(neighbor)
				.canCollideCheck(BlockUtils.getState(neighbor), false))
				continue;
			
			Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5)
				.add(new Vec3(side2.getDirectionVec()));
			
			// check if hitVec is within range (6 blocks)
			if(eyesPos.squareDistanceTo(hitVec) > 36)
				continue;
			
			// place block
			//Wrapper.INSTANCE.controller().	processRightClickBlock(Wrapper.INSTANCE.player(), Wrapper.INSTANCE.world(),
				//neighbor, side2, hitVec);
			
			return true;
		}
		
		return false;
	}
	
	// TODO: RotationUtils class for all the faceSomething() methods
	
	public static void faceVectorPacket(Vec3 vec)
	{
		double diffX = vec.xCoord - Wrapper.INSTANCE.player().posX;
		double diffY = vec.yCoord - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
		double diffZ = vec.zCoord - Wrapper.INSTANCE.player().posZ;
		
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		
		float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float)-Math.toDegrees(Math.atan2(diffY, dist));
		
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(
				Wrapper.INSTANCE.player().rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw - Wrapper.INSTANCE.player().rotationYaw),
				Wrapper.INSTANCE.player().rotationPitch
				+ MathHelper.wrapAngleTo180_float(pitch - Wrapper.INSTANCE.player().rotationPitch),
				Wrapper.INSTANCE.player().onGround));
	}
	
	public static void faceBlockClient(BlockPos blockPos)
	{
		double diffX = blockPos.getX() + 0.5 - Wrapper.INSTANCE.player().posX;
		double diffY =      //0.5
			blockPos.getY() + 0.0 - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
		double diffZ = blockPos.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		Wrapper.INSTANCE.player().rotationYaw = Wrapper.INSTANCE.player().rotationYaw
			+ MathHelper.wrapAngleTo180_float(yaw - Wrapper.INSTANCE.player().rotationYaw);
		Wrapper.INSTANCE.player().rotationPitch = Wrapper.INSTANCE.player().rotationPitch
			+ MathHelper.wrapAngleTo180_float(pitch -Wrapper.INSTANCE.player().rotationPitch);
	}
	
	public static void faceBlockPacket(BlockPos blockPos)
	{
		double diffX = blockPos.getX() + 0.5 - Wrapper.INSTANCE.player().posX;
		double diffY =      //0.5
			blockPos.getY() + 0.0 - (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
		double diffZ = blockPos.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(
				Wrapper.INSTANCE.player().rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw - Wrapper.INSTANCE.player().rotationYaw),
				Wrapper.INSTANCE.player().rotationPitch
				+ MathHelper.wrapAngleTo180_float(pitch - Wrapper.INSTANCE.player().rotationPitch),
				Wrapper.INSTANCE.player().onGround));
	}
	
	public static void faceBlockClientHorizontally(BlockPos blockPos)
	{
		double diffX = blockPos.getX() + 0.5 - Wrapper.INSTANCE.player().posX;
		double diffZ = blockPos.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ;
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		Wrapper.INSTANCE.player().rotationYaw = Wrapper.INSTANCE.player().rotationYaw
			+ MathHelper.wrapAngleTo180_float(yaw - Wrapper.INSTANCE.player().rotationYaw);
	}
	
	public static float getPlayerBlockDistance(BlockPos blockPos)
	{
		return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(),
			blockPos.getZ());
	}
	
	public static float getPlayerBlockDistance(double posX, double posY,
		double posZ)
	{
		float xDiff = (float)(Wrapper.INSTANCE.player().posX - posX);
		float yDiff = (float)(Wrapper.INSTANCE.player().posY - posY);
		float zDiff = (float)(Wrapper.INSTANCE.player().posZ - posZ);
		return getBlockDistance(xDiff, yDiff, zDiff);
	}
	
	public static float getBlockDistance(float xDiff, float yDiff, float zDiff)
	{
		return MathHelper.sqrt_double(
			(xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F) * (yDiff - 0.5F)
				+ (zDiff - 0.5F) * (zDiff - 0.5F));
	}
	
	public static float getHorizontalPlayerBlockDistance(BlockPos blockPos)
	{
		float xDiff = (float)(Wrapper.INSTANCE.player().posX - blockPos.getX());
		float zDiff = (float)(Wrapper.INSTANCE.player().posZ - blockPos.getZ());
		return MathHelper.sqrt_double(
			(xDiff - 0.5F) * (xDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
	}
	

	
	public static void breakBlocksPacketSpam(Iterable<BlockPos> blocks)
	{
		Vec3 eyesPos = Utils.getEyesPos();
		NetHandlerPlayClient connection = Wrapper.INSTANCE.player().sendQueue;
		
		for(BlockPos pos : blocks)
		{
			Vec3 posVec = new Vec3(pos).addVector(0.5, 0.5, 0.5);
			double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
			
			for(EnumFacing side : EnumFacing.values())
			{
				Vec3 hitVec = posVec.add(new Vec3(side.getDirectionVec()));
				
				if(eyesPos.squareDistanceTo(hitVec) >= distanceSqPosVec)
					continue;
				
				connection.addToSendQueue(new C07PacketPlayerDigging(
					Action.START_DESTROY_BLOCK, pos, side));
				connection.addToSendQueue(new C07PacketPlayerDigging(
					Action.STOP_DESTROY_BLOCK, pos, side));
				
				break;
			}
		}
	}
	
	public static LinkedList<BlockPos> findBlocksNearEntity(EntityLivingBase entity, int blockId, int blockMeta, int distance) {	
		LinkedList<BlockPos> blocks = new LinkedList<BlockPos>();
		
		for (int x = (int) Wrapper.INSTANCE.player().posX - distance; x <= (int) Wrapper.INSTANCE.player().posX + distance; ++x) {
            for (int z = (int) Wrapper.INSTANCE.player().posZ - distance; z <= (int) Wrapper.INSTANCE.player().posZ + distance; ++z) {
            	
                int height = Wrapper.INSTANCE.world().getHeight(); 
                block: for (int y = 0; y <= height; ++y) {
                	
                	BlockPos blockPos = new BlockPos(x, y, z);
                	IBlockState blockState = Wrapper.INSTANCE.world().getBlockState(blockPos);
                	
                	if(blockId == -1 || blockMeta == -1) {
                		blocks.add(blockPos);
            			continue block;
                	}
                	
                		int id = Block.getIdFromBlock(blockState.getBlock());
                		int meta =  blockState.getBlock().getMetaFromState(blockState);
                		
                		if(id == blockId && meta == blockMeta) {
                			
                			blocks.add(blockPos);
                			continue block;
                		}
                		
                	}
                }
            }
		return blocks;
	}
	public static Minecraft mc=Minecraft.getMinecraft();
	public static boolean isInLiquid() {
		 if (mc.thePlayer.isInWater()) {
	            return true;
	        }
	        boolean inLiquid = false;
	        final int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
	        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
	            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
	                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	                if (block != null && block != Blocks.air) {
	                    if (!(block instanceof BlockLiquid)) return false;
	                    inLiquid = true;
	                }
	            }
	        }
	        return inLiquid;
	}
	 public static BlockPos getHypixelBlockpos(String str)
	    {
	        int val = 89;

	        if (str != null && str.length() > 1)
	        {
	            char[] chs = str.toCharArray();
	            int lenght = chs.length;

	            for (int i = 0; i < lenght; i++)
	            {
	                val += (int)chs[i] * str.length() * str.length() + (int)str.charAt(0) + (int)str.charAt(1);
	            }

	            val /= str.length();
	        }

	        return new BlockPos(val, -val % 255, val);
	    }
	
}
