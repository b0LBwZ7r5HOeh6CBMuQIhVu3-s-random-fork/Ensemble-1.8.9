package it.fktcod.ktykshrk.module.mods;//package i.gishreloaded.gishcode.hack.hacks;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Regen extends Module {

	public NumberValue speed;
	public NumberValue health;

	public Regen() {
		super("Regeneration", HackCategory.COMBAT);

		health = new NumberValue("Health", 16D, 4D, 20D);
		speed = new NumberValue("Speed", 100D, 10D, 1000D);

		this.addValue(speed, health);
		this.setChinese(Core.Translate_CN[78]);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (Wrapper.INSTANCE.player().capabilities.isCreativeMode || Wrapper.INSTANCE.player().getHealth() == 0)
			return;

		if (Wrapper.INSTANCE.player().getFoodStats().getFoodLevel() < 18)
			return;

		if (Wrapper.INSTANCE.player().getHealth() >= Wrapper.INSTANCE.player().getMaxHealth())
			return;

		if (Wrapper.INSTANCE.player().getHealth() <= health.getValue().floatValue()) {
			for (int i = 0; i < speed.getValue().intValue(); i++)
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer());
			
		
			super.onClientTick(event);
		}
	}
}
