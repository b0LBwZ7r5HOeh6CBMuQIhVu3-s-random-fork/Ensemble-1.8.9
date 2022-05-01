package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.network.login.client.C00PacketLoginStart;

public class ChatRect extends Module{
	public ChatRect() {

		super("ChatRect", HackCategory.VISUAL);
		this.setChinese(Core.Translate_CN[25]);
	}
}
