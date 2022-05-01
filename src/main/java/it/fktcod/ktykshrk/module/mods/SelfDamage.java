package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class SelfDamage extends Module{
	
	public NumberValue damage;
	
	public SelfDamage() {
		super("SelfDamage", HackCategory.COMBAT);
		
		damage = new NumberValue("Damage", 0.0625D, 0.0125D, 0.35D);
		
		this.addValue(damage);
		this.setChinese(Core.Translate_CN[81]);
	}
	
	@Override
	public String getDescription() {
		return "Deals damage to you (useful for bypassing AC).";
	}
	
	@Override
	public void onEnable() {
		Utils.selfDamage(damage.getValue().doubleValue());
		this.toggle();
		super.onEnable();
	}
}
