package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;

public class Teams extends Module{

	public ModeValue mode;
	
	public Teams() {
		super("Teams", HackCategory.ANOTHER);
		this.mode = new ModeValue("Mode", new Mode("Base", false), new Mode("TabList", false), new Mode("ArmorColor", false), new Mode("NameColor", true));
		this.addValue(mode);
		this.setChinese(Core.Translate_CN[93]);
	}
	
	@Override
	public String getDescription() {
		return "Ignore if player in your team.";
	}

}
