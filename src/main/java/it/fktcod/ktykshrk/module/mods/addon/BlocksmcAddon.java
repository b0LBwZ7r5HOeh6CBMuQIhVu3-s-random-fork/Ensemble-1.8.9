package it.fktcod.ktykshrk.module.mods.addon;

import java.util.ArrayList;
import java.util.Random;

import ensemble.mixin.cc.mixin.interfaces.IC03Packet;
import ensemble.mixin.cc.mixin.interfaces.INetworkManager;
import ensemble.mixin.cc.mixin.interfaces.IS08Packet;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.event.EventWorld;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class BlocksmcAddon extends Module {

	BooleanValue verusSlientFlagApplyValue;
	Random random = new Random();

	private ArrayList packetBuffer = new ArrayList<Packet<INetHandler>>();
	
	private int currentTrans = 0;

	public BlocksmcAddon() {
		super("BMCAddon", HackCategory.ANOTHER);
		verusSlientFlagApplyValue = new BooleanValue("SlientFlag", true);
		addValue(verusSlientFlagApplyValue);

	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (mc.thePlayer.ticksExisted % 180 == 0) {
			// grab packets untill the queue size is 22 or less.
			while (packetBuffer.size() > 22) {
				// grab 1 packet, send and then remove it from the queue
				INetworkManager manager = (INetworkManager) Wrapper.INSTANCE.mc().getNetHandler().getNetworkManager();
				manager.sendPacketNoEvent((Packet) packetBuffer.get(0));
				packetBuffer.remove(0);
			}
		}
		super.onClientTick(event);
	}

	@Override
	public void onWorld(EventWorld event) {
		reset();
		super.onWorld(event);
	}

	private void reset() {
		currentTrans = 0;
		packetBuffer.clear();

	}

	@Override
	public void onPacketEvent(EventPacket event) {
		if(event.getType()==EventType.SEND) {
			//combat
			Packet packet=event.getPacket();
			 if (packet instanceof C0FPacketConfirmTransaction) {
                 if (currentTrans > 0) event.setCancelled(true);
                 currentTrans++;
                 ChatUtils.report("Packet C0F (Trans=$currentTrans)");
             } else if (packet instanceof C0BPacketEntityAction) {
                 event.setCancelled(true);
                 ChatUtils.report("C0B");
             }
			 
			 //move
			 
			 if (mc.thePlayer != null && mc.thePlayer.ticksExisted == 0) packetBuffer.clear();
             if (packet instanceof C03PacketPlayer) {
                 // Set position to a valid block height (so Spoof NoFall works)
                 float yPos = (float) ((mc.thePlayer.posY / 0.015625) * 0.015625);
                 mc.thePlayer.setPosition(mc.thePlayer.posX, yPos, mc.thePlayer.posZ);
                 if (mc.thePlayer.ticksExisted % 45 == 0) {
                	 INetworkManager manager = (INetworkManager) Wrapper.INSTANCE.mc().getNetHandler().getNetworkManager();
                     // Clip into ground and silently accept the teleport from the server. (This fucks with teleport compensation LOL)
                     manager.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                     manager.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 11.725, mc.thePlayer.posZ, false));
                     manager.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                 }
             } else if (packet instanceof S08PacketPlayerPosLook && verusSlientFlagApplyValue.getValue()) {
            	 IS08Packet packet_=(IS08Packet) packet;
            	 
                 double x = packet_.getX() - mc.thePlayer.posX;
                 double y = packet_.getY() - mc.thePlayer.posY;
                 double  z = packet_.getZ()- mc.thePlayer.posZ;
                						 float diff = MathHelper.sqrt_double(x * x + y * y + z * z);
                 // Cancel the teleport, and silently accept it.
                 if (diff <= 8) {
                	 INetworkManager manager = (INetworkManager) Wrapper.INSTANCE.mc().getNetHandler().getNetworkManager();
                     event.setCancelled(true);
                     // LATEST verus ALWAYS expects a c06 within 30 seconds of a teleport if packets have been sent from the client after the teleport.
                    manager.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet_.getX(), packet_.getY(), packet_.getZ(), packet_.getYaw(), packet_.getPitch(), true));
                 }
             } else if (packet instanceof C0FPacketConfirmTransaction) {
                 for (int i=0;i<randomNumber(1, 5);i++) {
                     // Make sure to dupe packets 4 times, since it will match up with the missing packets while keeping the anticheat disabled, in order to bypass ping spoof checks
                     // why the fuck do they not checked duped transactions? LMFAO
                     packetBuffer.add(packet);
                 }
               event.setCancelled(true);
             }
		}
		super.onPacketEvent(event);
	}

	private double randomNumber(int max, int min) {
		return Math.random() * (max - min) + min;
	}
}
