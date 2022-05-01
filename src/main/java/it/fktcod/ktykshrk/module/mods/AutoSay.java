package it.fktcod.ktykshrk.module.mods;

import java.util.Random;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoSay extends Module{
	NumberValue delayValue;
	TimerUtils timer =new TimerUtils();
	Random r=new Random();
	
	public AutoSay() {
		super("AutoSay", HackCategory.ANOTHER);
		delayValue=new NumberValue("Delay", 2D, 0.2D, 10D);
		this.addValue(delayValue);
		this.setChinese(Core.Translate_CN[14]);
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		int time=delayValue.getValue().intValue()*1000;
		if(timer.hasReached(time)) {
			
			Wrapper.INSTANCE.player().sendChatMessage("BUY ENSEMBLE-> 2523096009 || 1465234306"+"  ["+r.nextInt(100000)+"]");
			timer.reset();
		}
		
		super.onClientTick(event);
	}
}
