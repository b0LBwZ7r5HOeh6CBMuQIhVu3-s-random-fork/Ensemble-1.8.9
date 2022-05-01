package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiAfk extends Module{
	
	public NumberValue delay;
	public TimerUtils timer;
	
	public AntiAfk() {
		super("AntiAfk", HackCategory.ANOTHER);
		
		this.timer = new TimerUtils();
		delay = new NumberValue("DelaySec", 10.0D, 1.0D, 100.0D);
		
		this.addValue(delay);
		this.setChinese(Core.Translate_CN[2]);
	}
	
	@Override
	public String getDescription() {
		return "Prevents from being kicked for AFK.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) { 
		if(timer.isDelay((long)(1000 * delay.getValue()))) {
			Wrapper.INSTANCE.player().jump();
			timer.setLastMS();
		}
		super.onClientTick(event); 
	}
}
