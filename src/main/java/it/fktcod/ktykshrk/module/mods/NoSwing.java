package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import net.minecraft.network.play.client.C0APacketAnimation;

public class NoSwing extends Module {
	public NoSwing() {
		super("NoSwing", HackCategory.PLAYER);
		this.setChinese(Core.Translate_CN[68]);
	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (side == Side.OUT) {
			if (packet instanceof C0APacketAnimation) {
				return false;
			}
		}
		return true;
	}
}
