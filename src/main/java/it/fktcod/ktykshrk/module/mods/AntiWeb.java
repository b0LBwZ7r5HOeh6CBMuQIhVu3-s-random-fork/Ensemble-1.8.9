package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Mapping;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiWeb extends Module{
	
	public AntiWeb() {

		super("AntiWeb", HackCategory.PLAYER);
		this.setChinese(Core.Translate_CN[8]);
	}
	
	@Override
	public String getDescription() {
		return "Does not change walking speed in web.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		try {
			Field isInWeb = Entity.class.getDeclaredField(Mapping.isInWeb);
			isInWeb.setAccessible(true);
			isInWeb.setBoolean(Wrapper.INSTANCE.player(), false);
		} catch (Exception ex) {
			this.setToggled(false);
		}
		super.onClientTick(event);
	}
	
}
