package it.fktcod.ktykshrk.module.mods;

import java.util.Random;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FastLadder extends Module{
    
	public FastLadder() {

		super("FastLadder", HackCategory.MOVEMENT);
		this.setChinese(Core.Translate_CN[40]);
	}
	
	@Override
	public String getDescription() {
		return "Allows you to climb up ladders faster.";
	}
    
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(!Wrapper.INSTANCE.player().isOnLadder() || Wrapper.INSTANCE.player().moveForward == 0 && Wrapper.INSTANCE.player().moveStrafing == 0) return;
		Wrapper.INSTANCE.player().motionY = 0.169;
		super.onClientTick(event);
	}
	
}
