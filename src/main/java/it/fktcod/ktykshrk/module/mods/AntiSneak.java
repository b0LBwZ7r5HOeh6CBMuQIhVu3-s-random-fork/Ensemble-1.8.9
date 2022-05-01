package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C0BPacketEntityAction;

import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiSneak extends Module{
	
	public BooleanValue fullSprint;
	public AntiSneak() {
		super("AntiSneak", HackCategory.PLAYER);
		fullSprint = new BooleanValue("FullSprint", true);
		this.addValue(fullSprint);
		this.setChinese(Core.Translate_CN[6]);
	}
	
	@Override
	public String getDescription() {
		return "Does not change walking speed when sneak.";
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if(side == Side.OUT && packet instanceof C0BPacketEntityAction) {
			C0BPacketEntityAction p = (C0BPacketEntityAction) packet;
			if(p.getAction() == C0BPacketEntityAction.Action.START_SNEAKING) {
				return false;
			}
		}
		return true;
	}
	@Override
	public void onClientTick(ClientTickEvent event) {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		GameSettings settings = Wrapper.INSTANCE.mcSettings();
		if(player.onGround && settings.keyBindSneak.isKeyDown()) {
			if(!fullSprint.getValue() && settings.keyBindForward.isKeyDown()) {
				player.setSprinting(Utils.isMoving(player));
			} 
			else if(fullSprint.getValue()) {
				player.setSprinting(Utils.isMoving(player));
			}
			if(settings.keyBindRight.isKeyDown() 
					|| settings.keyBindLeft.isKeyDown()
					|| settings.keyBindBack.isKeyDown()) {
				if(settings.keyBindBack.isKeyDown()) {
					player.motionX *= 1.268;
					player.motionZ *= 1.268;
				} else {
					player.motionX *= 1.252;
					player.motionZ *= 1.252;
				}
			} else {
				player.motionX *= 1.2848;
				player.motionZ *= 1.2848;
			}
		}
		super.onClientTick(event);
	}
}
