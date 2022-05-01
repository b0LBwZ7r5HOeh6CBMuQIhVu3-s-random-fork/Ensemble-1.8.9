package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SpeedMine extends Module {

	public BlockPos blockPos;
	public EnumFacing facing;
	public C07PacketPlayerDigging curPacket;
	private boolean bzs = false;
	private float bzx = 0.0f;

	private BooleanValue C03=new BooleanValue("Boost",false);
	private BooleanValue pot=new BooleanValue("PotBoost",false);
	private BooleanValue ultraBoost=new BooleanValue("UltraBoost",false);
	private BooleanValue Reflection=new BooleanValue("Reflection",false);
	public SpeedMine() {
		super("SpeedMine", HackCategory.PLAYER);
		addValue(C03);
		addValue(pot);
		addValue(ultraBoost);
		addValue(Reflection);
		this.setChinese(Core.Translate_CN[88]);
	}

	@Override
	public boolean onPacket(Object packet, Connection.Side side) {

		if (Reflection.getValue()){
			if(!mc.playerController.isInCreativeMode()) {
				Field field = ReflectionHelper.findField(PlayerControllerMP.class,new String[] { "curBlockDamageMP", "field_78770_f" });
				Field blockdelay = ReflectionHelper.findField(PlayerControllerMP.class,new String[] { "blockHitDelay", "field_78781_i" });
				//mc.playerController.blockHitDelay = 0;
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					if (!blockdelay.isAccessible()) {
						blockdelay.setAccessible(true);
					}

					blockdelay.setInt(mc.playerController, 0);
					if (field.getFloat(mc.playerController) >= 0.7F)
					{
						field.setFloat(mc.playerController, 1F);
					}
				} catch (Exception e) {

				}
			}
		}else{
			try{
				if (packet instanceof C07PacketPlayerDigging && packet != curPacket && !mc.playerController.extendedReach() && mc.playerController != null) {

					C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)packet;

					if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
						this.bzs = true;
						this.blockPos = c07PacketPlayerDigging.getPosition();
						this.facing = c07PacketPlayerDigging.getFacing();
						this.bzx = 0.0f;
						onBreak(blockPos,facing);


					} else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
						this.bzs = false;
						this.blockPos = null;
						this.facing = null;
						onBreak(blockPos,facing);
					}

				}

			}catch (Exception ignore){

			}
		}
		return true;
	}

	private boolean canBreak(BlockPos pos) {
		try{
			final IBlockState blockState = mc.theWorld.getBlockState(pos);
			final Block block = blockState.getBlock();
			return block.getBlockHardness(mc.theWorld, pos) != -1;
		}catch (Exception ignore){}
		return true;

	}

	public boolean packet = true;
	public boolean damage = true;


	public void onBreak(BlockPos pos,EnumFacing facing) {
		try{
			if (canBreak(pos)) {
				if (ultraBoost.getValue()) {
					mc.thePlayer.swingItem();
					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK ,pos, facing));
					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
				}
				if (ultraBoost.getValue()) {
					Wrapper.setBlockHitDelay(mc,0);
					if (Wrapper.getCurBlockDamageMP(mc) >= 0.7F) {
						Wrapper.setCurBlockDamageMP(mc,1.0F);
					}
				}
			}
		}catch (Exception ignore){}
	}




	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		try{
			if (pot.getValue()) {
				mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, 1));

			}

			if (mc.playerController.extendedReach()) {
				Wrapper.setBlockHitDelay(mc,0);
				if (Wrapper.getCurBlockDamageMP(mc) >= 0.6F) {
					Wrapper.setCurBlockDamageMP(mc,1.0F);
				}

			} else if (this.bzs) {
				Block block = mc.theWorld.getBlockState(this.blockPos).getBlock();
				this.bzx += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, this.blockPos) * 1.4;
				if (this.bzx >= 1.0f) {
					mc.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
					C07PacketPlayerDigging packet = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing);
					curPacket = packet;
					if (C03.getValue()) {
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
					}
					mc.thePlayer.sendQueue.getNetworkManager().sendPacket(packet);

					this.bzx = 0.0f;
					this.bzs = false;
				}
			}}catch (Exception ignore){}
	}


}
