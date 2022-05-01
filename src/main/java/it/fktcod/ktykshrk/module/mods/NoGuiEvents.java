package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;

public class NoGuiEvents extends Module{

	public NoGuiEvents()
	{
		super("NoGuiEvents", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[65]);
	}
	
	@Override
	public String getDescription() {
		return "Disables events when the GUI is open.";
	}
}
