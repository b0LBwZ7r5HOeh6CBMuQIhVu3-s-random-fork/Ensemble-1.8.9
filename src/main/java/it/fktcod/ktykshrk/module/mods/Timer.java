package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Timer extends Module{
	NumberValue timerspeed;
	net.minecraft.util.Timer	timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});
	
	public Timer() {
		super("Timer", HackCategory.PLAYER);
		timerspeed=new NumberValue("TimerSpeed", 2D, 1D, 5D);
		addValue(timerspeed);
		this.setChinese(Core.Translate_CN[96]);
	}
	
	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		
		 timer.timerSpeed = timerspeed.getValue().floatValue();
		super.onPlayerTick(event);
	}
	
	@Override
	public void onDisable() {
		
		 timer.timerSpeed =1.0f;
		super.onDisable();
	}
	
}
