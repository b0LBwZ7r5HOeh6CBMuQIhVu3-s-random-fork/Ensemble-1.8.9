package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoGA extends Module {

	TimerUtils timer = new TimerUtils();

	ModeValue mode;
	NumberValue health;
	BooleanValue noABP;
	BooleanValue warn;
	NumberValue fapa;
	NumberValue delay;

	public AutoGA() {
		super("AutoGA", HackCategory.COMBAT);
		mode = new ModeValue("Mode", new Mode("Key", false), new Mode("Conti", true));
		health = new NumberValue("Health", 8D, 1D, 20D);
		noABP = new BooleanValue("NoAbsorption", true);
		warn = new BooleanValue("Warn", true);
		fapa = new NumberValue("FastPackets", 35D, 10D, 50D);
		delay = new NumberValue("Delay", 200D, 0D, 1000D);

		this.addValue(mode, health, fapa, delay, noABP, warn);
		this.setChinese(Core.Translate_CN[12]);
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		if (mode.getMode("Key").isToggled()) {
			Apple();
			this.toggle();
		} else if (mode.getMode("Conti").isToggled()) {
			if (timer.hasReached(delay.getValue().floatValue())) {
				if (Wrapper.INSTANCE.player().getHealth() <= health.getValue()) {
					Apple();
					timer.reset();

				}

			}

		}
		super.onPlayerTick(event);
	}

	void Apple() {
		if (noABP.getValue() && Wrapper.INSTANCE.player().getAbsorptionAmount() > 0) {

			return;
		}

		int hotbar = find(36, 45, Items.golden_apple);

		if (hotbar != -1) {
			Wrapper.INSTANCE.sendPacket(new C09PacketHeldItemChange(hotbar - 36));
			Wrapper.INSTANCE.sendPacket(new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().getHeldItem()));

			for (int i = 0; i < 35; i++) {
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(Wrapper.INSTANCE.player().onGround));
			}
			Wrapper.INSTANCE.sendPacket(new C09PacketHeldItemChange(Wrapper.INSTANCE.player().inventory.currentItem));

			if (warn.getValue()) {
				ChatUtils.report("EAT GAPPLE");
			}
		} else {

			if (warn.getValue()) {
				ChatUtils.warning("Failed EAT GAPPLE");
			}
		}

	}

	// Liquid Utils
	private int find(final int startSlot, final int endSlot, final Item item) {
		for (int i = startSlot; i < endSlot; i++) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (stack != null && stack.getItem() == item)
				return i;
		}
		return -1;
	}

}
