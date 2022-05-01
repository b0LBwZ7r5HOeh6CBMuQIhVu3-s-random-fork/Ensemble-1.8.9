package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Mapping;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoWalk extends Module{
	
	public AutoWalk() {
		super("AutoWalk", HackCategory.PLAYER);
		this.setChinese(Core.Translate_CN[21]);
	}
	
	@Override
	public String getDescription() {
		return "Automatic walking.";
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode(), false);
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode(), true);
		super.onClientTick(event);
	}
	
}
