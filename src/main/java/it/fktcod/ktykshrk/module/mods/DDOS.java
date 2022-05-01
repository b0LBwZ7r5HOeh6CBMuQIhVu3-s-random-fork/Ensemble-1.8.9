package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.addon.DDOSWindow;
import it.fktcod.ktykshrk.value.NumberValue;

public class DDOS extends Module{
	public static NumberValue threadNum;
	
	public DDOS() {
		super("DDOS", HackCategory.ANOTHER);
		threadNum=new NumberValue("Thread", 1D, 1D, 128D);
		addValue(threadNum);
		this.setChinese(Core.Translate_CN[33]);
	}
	
	@Override
	public void onEnable() {
		DDOSWindow.main(null);
		super.onEnable();
	}
	
}
