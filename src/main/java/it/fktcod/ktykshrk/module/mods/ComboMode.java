package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ComboMode extends Module{
	
	public static boolean enabled = false;
	
	public ComboMode() {
		super("ComboMode", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[29]);
	}
	
	@Override
	public String getDescription() {
		return "Disable all hacks.";
	}
	
	@Override
	public void onEnable() {
		
		enabled = true;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		enabled = false;
		super.onDisable();
	}
	
}
