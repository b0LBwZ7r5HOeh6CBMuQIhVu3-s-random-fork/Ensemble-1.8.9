package it.fktcod.ktykshrk.module.mods.addon;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.Notification;
import it.fktcod.ktykshrk.module.Notification.Type;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class MinelandAddon extends Module {
	public MinelandAddon() {
		super("MinelandAddon", HackCategory.ANOTHER);

	}

	@Override
	public void onPacketEvent(EventPacket event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook && event.getType() == EventType.RECIEVE) {
			Core.notificationManager.add(new Notification("MinelandAddon", Type.Success));

			Wrapper.INSTANCE.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
					BlockPos.ORIGIN, EnumFacing.DOWN));
			Wrapper.INSTANCE.sendPacket(
					new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().inventory.getCurrentItem()));
		}
		super.onPacketEvent(event);
	}
}
