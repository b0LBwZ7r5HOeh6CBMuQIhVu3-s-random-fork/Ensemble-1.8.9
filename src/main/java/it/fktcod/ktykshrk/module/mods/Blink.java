package it.fktcod.ktykshrk.module.mods;

import java.util.LinkedList;
import java.util.Queue;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;


public class Blink extends Module {


	Queue<C03PacketPlayer> packets = new LinkedList<>();
	boolean send = false;

	public Blink()
	{
		super("Blink", HackCategory.PLAYER);
		this.setChinese(Core.Translate_CN[22]);
	}

	@Override
	public String getDescription() {
		return "Allows you to move without sending it to the server.";
	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (side == Side.OUT && packet instanceof C03PacketPlayer) {

			ChatUtils.message("Packets:" + String.valueOf(packets.size()));

			send = false;
			packets.add((C03PacketPlayer) packet);
			return send;
		} else {
			send = true;
		}
		return send;
	}

	@Override
	public void onEnable() {
		if (Wrapper.INSTANCE.player() != null && Wrapper.INSTANCE.world() != null) {
		
	
		}
		super.onEnable();
	}

	@Override
	public void onDisable() {
		while (!packets.isEmpty())
			Wrapper.INSTANCE.sendPacket(packets.poll());

	
		super.onDisable();
	}
}
