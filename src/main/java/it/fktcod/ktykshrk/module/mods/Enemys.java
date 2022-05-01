package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.BooleanValue;

public class Enemys extends Module{

	public Enemys() {

		super("Enemys", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[37]);
	}
	
	@Override
	public String getDescription() {
		return "Target only in enemy list.";
	}
}
