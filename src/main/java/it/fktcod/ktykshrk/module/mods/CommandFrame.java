package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.frame.FCommand;

public class CommandFrame extends Module{
	public CommandFrame() {
		super("CommandFrame",HackCategory.ANOTHER);
		setShow(false);
		this.setChinese(Core.Translate_CN[30]);
	}
	@Override
	public void onEnable() {
		FCommand.main(null);
		this.setToggled(false);
	}
}
