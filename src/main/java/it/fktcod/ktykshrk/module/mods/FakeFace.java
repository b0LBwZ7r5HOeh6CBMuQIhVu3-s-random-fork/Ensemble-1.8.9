package it.fktcod.ktykshrk.module.mods;//package cn.zenwix.ensemble.hack.hacks;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//
//import cn.zenwix.ensemble.hack.Hack;
//import cn.zenwix.ensemble.hack.HackCategory;
//import cn.zenwix.ensemble.managers.EnemyManager;
//import cn.zenwix.ensemble.managers.FriendManager;
//import cn.zenwix.ensemble.managers.HackManager;
//import cn.zenwix.ensemble.utils.BlockUtils;
//import cn.zenwix.ensemble.utils.RobotUtils;
//import cn.zenwix.ensemble.utils.Utils;
//import cn.zenwix.ensemble.utils.ValidUtils;
//import cn.zenwix.ensemble.utils.system.Wrapper;
//import cn.zenwix.ensemble.utils.visual.RenderUtils;
//import cn.zenwix.ensemble.value.BooleanValue;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockStaticLiquid;
//import net.minecraft.block.material.Material;
//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.item.EntityArmorStand;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraftforge.client.event.RenderWorldLastEvent;
//import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
//import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
//
//public class FakeFace extends Hack{
//	//-----------DEBUG ME-----------//
//	
//	public BooleanValue packet;
//	public EntityLivingBase target;
//	public BlockPos faceBlock;
//	
//	public FakeFace() {
//		super("FakeFace", HackCategory.COMBAT);
//		
//		packet = new BooleanValue("Packet", true);
//		this.addValue(packet);
//	}
//	
//	@Override
//	public void onRenderWorldLast(RenderWorldLastEvent event) {
//		if(this.faceBlock != null) {
//			RenderUtils.drawBlockESP(this.faceBlock, 0.0f, 1.0f, 1.0f);
//		}
//		super.onRenderWorldLast(event);
//	}
//	
//	@Override
//	public void onClientTick(ClientTickEvent event) {
//		this.bucket();
//		super.onClientTick(event);
//	}
//	
//	@Override
//	public void onDisable() {
//		this.target = null;
//		this.faceBlock = null;
//		super.onDisable();
//	}
//	
//	
//	void bucket() {
//		ItemStack currentItem = Wrapper.INSTANCE.inventory().getCurrentItem();
//		if(currentItem == null) {
//			return;
//		}
//		int dis = (int)Wrapper.INSTANCE.controller().getBlockReachDistance();
//		for(BlockPos block : BlockUtils.findBlocksNearEntity(Wrapper.INSTANCE.player(), Block.getIdFromBlock(Blocks.WATER), 0, dis)) {
//			if(currentItem.getItem() == Items.BUCKET) {
//				faceBlock = block;
//				break;
//			}
//		}
//		for(BlockPos block : BlockUtils.findBlocksNearEntity(Wrapper.INSTANCE.player(), Block.getIdFromBlock(Blocks.LAVA), 0, dis)) {
//			if(currentItem.getItem() == Items.BUCKET) {
//				faceBlock = block;
//				break;
//			}
//		}
//		
//		if(this.faceBlock != null && currentItem.getItem() == Items.BUCKET && Wrapper.INSTANCE.mcSettings().keyBindUseItem.isKeyDown()) {
//			if(Wrapper.INSTANCE.player().getDistanceSq(this.faceBlock) <= Wrapper.INSTANCE.controller().getBlockReachDistance()) {
//				faceBlock(this.faceBlock);
//				RobotUtils.clickMouse(1);
//				this.faceBlock = null;
//			}
//		}
//		
//		this.target = this.getClosestEntity();
//		if(this.target != null && currentItem.getItem() == Items.LAVA_BUCKET || currentItem.getItem() == Items.WATER_BUCKET) {
//			if(Wrapper.INSTANCE.player().getDistance(this.target) <= Wrapper.INSTANCE.controller().getBlockReachDistance()) {
//				this.faceBlock = new BlockPos(this.target);
//				if(Wrapper.INSTANCE.mcSettings().keyBindUseItem.isKeyDown()) {
//					faceBlock(this.faceBlock);
//					RobotUtils.clickMouse(1);
//					this.faceBlock = null;
//				}
//			}
//		}
//	}
//	
//	void faceBlock(BlockPos blockPos) {
//		if(this.packet.getValue()) {
//			BlockUtils.faceBlockPacket(blockPos);
//		} else {
//			BlockUtils.faceBlockClient(blockPos);
//		}
//	}
//	
//	public boolean check(EntityLivingBase entity) {
//		if(entity instanceof EntityArmorStand) {
//			return false;
//		}
//		if(ValidUtils.isValidEntity(entity)){
//			return false;
//		}
//		if(!ValidUtils.isNoScreen()) {
//			return false;
//		}
//		if(entity == Wrapper.INSTANCE.player()) {
//			return false;
//		}
//		if(entity.isDead) {
//			return false;
//		}
//		if(ValidUtils.isBot(entity)) {
//			return false;
//		}
//		if(!ValidUtils.isFriendEnemy(entity)) {
//			return false;
//		}
//    	if(!ValidUtils.isInvisible(entity)) {
//			return false;
//		}
//		if(!ValidUtils.isTeam(entity)) {
//			return false;
//		}
//		if(!Wrapper.INSTANCE.player().canEntityBeSeen(entity)) {
//			return false;
//		}
//		return true;
//    }
//	
//	EntityLivingBase getClosestEntity(){
//		EntityLivingBase closestEntity = null;
// 		for (Object o : Utils.getEntityList()) {
// 			if(o instanceof EntityLivingBase) {
// 				EntityLivingBase entity = (EntityLivingBase)o;
// 				if(check(entity)) {
// 					if(closestEntity == null || Wrapper.INSTANCE.player().getDistance(entity) < Wrapper.INSTANCE.player().getDistance(closestEntity)) {
// 						closestEntity = entity;
// 					}
// 				}
// 			}
// 		}
// 		return closestEntity;
// 	}
//	
//	
//	
//}
