package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiRain extends Module{

	public AntiRain() {
		super("AntiRain", HackCategory.VISUAL);
	}
	
	@Override
	public String getDescription() {
		return "Stops rain.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
        Wrapper.INSTANCE.world().setRainStrength(0.0f);
		super.onClientTick(event);
		this.setChinese(Core.Translate_CN[5]);
	}

}
