package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoSwim extends Module{

	public ModeValue mode;
	public AutoSwim() {
		super("AutoSwim", HackCategory.PLAYER);
		
		this.mode = new ModeValue("Mode", new Mode("Jump", true), new Mode("Dolphin", false), new Mode("Fish", false));
		this.addValue(mode);
		this.setChinese(Core.Translate_CN[19]);
	}
	
	@Override
    public String getDescription() {
        return "Jumps automatically when you in water.";
    }
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(!Wrapper.INSTANCE.player().isInWater() && !Wrapper.INSTANCE.player().isInLava()) {
			return;
		}
		if(Wrapper.INSTANCE.player().isSneaking() || Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
			return;
		}
		if(mode.getMode("Jump").isToggled()) {
			Wrapper.INSTANCE.player().jump();
		} 
		else if(mode.getMode("Dolphin").isToggled()) {
			Wrapper.INSTANCE.player().motionY += 0.04f;
		} 
		else if(mode.getMode("Fish").isToggled()) {
			Wrapper.INSTANCE.player().motionY += 0.02f;
		}
		super.onClientTick(event);
	}
}
