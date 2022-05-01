package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PacketFilter extends Module{
	
	public ModeValue mode;
	
	public BooleanValue cCPacketPlayer;
	public BooleanValue cCPacketCloseWindow;
	public BooleanValue cCPacketRotation;
	public BooleanValue cCPacketPosition;
	public BooleanValue cCPacketPositionRotation;
	public BooleanValue cCPacketClientStatus;
	public BooleanValue cCPacketInput;
	public BooleanValue cCPacketPlayerAbilities;
	public BooleanValue cCPacketPlayerDigging;
	public BooleanValue cCPacketUseEntity;
	public BooleanValue cCPacketVehicleMove;
	public BooleanValue cCPacketEntityAction;
	public BooleanValue cCPacketClickWindow;
	
	public PacketFilter() {
		super("PacketFilter", HackCategory.ANOTHER);
		
		this.mode = new ModeValue("Mode", new Mode("Output", true), new Mode("Input", false), new Mode("AllSides", false));
		
		cCPacketPlayer = new BooleanValue("Player", false);
		cCPacketEntityAction = new BooleanValue("EntityAction", false);
		cCPacketCloseWindow = new BooleanValue("CloseWindow", false);
		cCPacketRotation = new BooleanValue("Rotation", false);
		cCPacketPosition = new BooleanValue("Position", false);
		cCPacketPositionRotation = new BooleanValue("PositionRotation", false);
		cCPacketClientStatus = new BooleanValue("ClientStatus", false);
		cCPacketInput = new BooleanValue("Input", false);
		cCPacketPlayerAbilities = new BooleanValue("PlayerAbilities", false);
		cCPacketPlayerDigging = new BooleanValue("PlayerDigging", false);
		cCPacketUseEntity = new BooleanValue("UseEntity", false);
		cCPacketVehicleMove = new BooleanValue("VehicleMove", false);
		cCPacketEntityAction = new BooleanValue("EntityAction", false);
		cCPacketClickWindow = new BooleanValue("ClickWindow", false);
		
		this.addValue(
				this.mode,
				cCPacketPlayer,
				cCPacketEntityAction,
				cCPacketCloseWindow,
				cCPacketRotation,
				cCPacketPosition,
				cCPacketPositionRotation,
				cCPacketClientStatus,
				cCPacketInput,
				cCPacketPlayerAbilities,
				cCPacketPlayerDigging,
				cCPacketUseEntity,
				cCPacketVehicleMove,
				cCPacketEntityAction,
				cCPacketClickWindow
				);
		this.setChinese(Core.Translate_CN[70]);
	}
	
	@Override
	public String getDescription() {
		return "Packet filter.";
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if((this.mode.getMode("Output").isToggled() && side == Side.OUT) 
				|| (this.mode.getMode("Input").isToggled() && side == Side.IN)
				|| (this.mode.getMode("AllSides").isToggled()))
			return checkPacket(packet);
		return true;
	}
	
	public boolean checkPacket(Object packet) {
		if((cCPacketPlayer.getValue() &&  packet instanceof C03PacketPlayer)
				|| (cCPacketEntityAction.getValue() &&  packet instanceof C0BPacketEntityAction)
				|| (cCPacketCloseWindow.getValue() &&  packet instanceof C0DPacketCloseWindow)
				|| (cCPacketRotation.getValue() &&  packet instanceof C03PacketPlayer.C05PacketPlayerLook)
				|| (cCPacketPosition.getValue() &&  packet instanceof C03PacketPlayer)
				|| (cCPacketPositionRotation.getValue() &&  packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)
				|| (cCPacketClientStatus.getValue() &&  packet instanceof C16PacketClientStatus)
				|| (cCPacketInput.getValue() &&  packet instanceof C0CPacketInput)
				|| (cCPacketPlayerAbilities.getValue() &&  packet instanceof C13PacketPlayerAbilities)
				|| (cCPacketPlayerDigging.getValue() &&  packet instanceof C07PacketPlayerDigging)
				|| (cCPacketUseEntity.getValue() &&  packet instanceof C02PacketUseEntity)
				|| (cCPacketEntityAction.getValue() &&  packet instanceof C0BPacketEntityAction)
				|| (cCPacketClickWindow.getValue() &&  packet instanceof C0EPacketClickWindow))
			return false;
		return true;
	}
}
