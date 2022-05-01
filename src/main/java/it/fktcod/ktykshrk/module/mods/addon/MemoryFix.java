package it.fktcod.ktykshrk.module.mods.addon;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class MemoryFix extends Module{
	
	
	
	public MemoryFix() {
		super("MemoryFix", HackCategory.ANOTHER);
		setShow(false);
		this.setChinese(Core.Translate_CN[106]);

	}
	
	@Override
	public void onEnable() {
		Runtime.getRuntime().gc();
		toggle();
		super.onEnable();
	}
	
	
}
