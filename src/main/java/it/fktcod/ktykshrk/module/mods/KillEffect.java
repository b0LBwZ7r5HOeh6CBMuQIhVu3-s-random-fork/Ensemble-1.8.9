package it.fktcod.ktykshrk.module.mods;

import java.util.Random;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class KillEffect extends Module {
	BooleanValue lightning;
	
	Random rand=new Random();

	public KillEffect() {
		super("KillEffect", HackCategory.ANOTHER);

		lightning = new BooleanValue("Lightning", true);
		addValue(lightning);
		this.setChinese(Core.Translate_CN[59]);

	}
	
	
	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		if(lightning.getValue()) {
			if(KillAura.getTarget()==null)
				return;
			
			if(KillAura.getTarget().isDead||KillAura.getTarget().getHealth()==0) {
				EntityLivingBase target=KillAura.getTarget();
				Wrapper.INSTANCE.world().spawnEntityInWorld(new EntityLightningBolt(Wrapper.INSTANCE.world(),target.posX , target.posY, target.posZ));
				// Wrapper.INSTANCE.world().playSoundEffect(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
				Wrapper.INSTANCE.player().playSound("ambient.weather.thunder", 1, 1);
				Wrapper.INSTANCE.player().playSound("random.explode", 1, 1);
				// Wrapper.INSTANCE.world().playSoundEffect(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
			}
			
		}
		super.onPlayerTick(event);
	}
}
