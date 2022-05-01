package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class NightVision extends Module{

	public ModeValue mode;
	
	public NightVision() {
		super("NightVision", HackCategory.VISUAL);
		
		this.mode = new ModeValue("Mode", new Mode("Brightness", true), new Mode("Effect", false));
		this.addValue(mode);
		this.setChinese(Core.Translate_CN[63]);
	}
	
	@Override
	public String getDescription() {
		return "Gets you night vision.";
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	@Override
	public void onDisable() {
		if(this.mode.getMode("Brightness").isToggled())
			Wrapper.INSTANCE.mcSettings().gammaSetting = 1;
		else
			//Utils.removeEffect(16);
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(this.mode.getMode("Brightness").isToggled())
			Wrapper.INSTANCE.mcSettings().gammaSetting = 10;
		else
			//Utils.addEffect(16, 1000, 3);
		super.onClientTick(event);
	}
}
