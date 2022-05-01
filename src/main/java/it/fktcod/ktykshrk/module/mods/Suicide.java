package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Suicide extends Module{
	
	public NumberValue damage;
	
	public Suicide() {
		super("Suicide", HackCategory.COMBAT);
		
		damage = new NumberValue("Damage", 0.35D, 0.0125D, 0.50D);
		
		this.addValue(damage);
		this.setChinese(Core.Translate_CN[90]);
	}
	
	@Override
	public String getDescription() {
		return "Kills you.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Wrapper.INSTANCE.player().isDead) this.toggle();
		Utils.selfDamage(damage.getValue().doubleValue());
		super.onClientTick(event);
	}
	
}
