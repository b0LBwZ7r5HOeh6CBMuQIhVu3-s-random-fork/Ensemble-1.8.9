package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KeepSprint extends Module{

	public KeepSprint() {
		super("KeepSprint", HackCategory.MOVEMENT);
		this.setChinese(Core.Translate_CN[56]);
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if(side==Side.OUT) {
			if(packet instanceof C0BPacketEntityAction) {
				
				  C0BPacketEntityAction pac = (C0BPacketEntityAction) packet;

	                if (pac.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING)
	                {
	                    return false;
	                }
			}
			
		}
		return true;
	}

	@Override
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if(!mc.thePlayer.isSprinting()) mc.thePlayer.setSprinting(true);
	}
}
