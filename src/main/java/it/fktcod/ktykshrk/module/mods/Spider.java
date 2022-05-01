package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Spider extends Module{
	
	public Spider() {

		super("Spider", HackCategory.MOVEMENT);
		this.setChinese(Core.Translate_CN[89]);
	}
	
	@Override
	public String getDescription() {
		return "Allows you to climb up walls like a spider.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
        if(!Wrapper.INSTANCE.player().isOnLadder() 
        		&& Wrapper.INSTANCE.player().isCollidedHorizontally 
        		&& Wrapper.INSTANCE.player().motionY < 0.2) {
        	Wrapper.INSTANCE.player().motionY = 0.2;
        }
		super.onClientTick(event);
	}
	
}
