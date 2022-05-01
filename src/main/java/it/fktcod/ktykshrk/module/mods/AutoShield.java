package it.fktcod.ktykshrk.module.mods;

import akka.actor.Kill;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.RobotUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;

import net.minecraft.inventory.ContainerPlayer;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoShield extends Module {
	BooleanValue INTERACT;

	public AutoShield() {
		super("AutoShield", HackCategory.COMBAT);

		INTERACT = new BooleanValue("INTERACT", false);
		this.addValue(INTERACT);
		this.setChinese(Core.Translate_CN[15]);
	}

	@Override
	public String getDescription() {
		return "Manages your shield automatically.";
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (!Utils.screenCheck()) {
			this.blockByShield(false);
			
		}
		super.onClientTick(event);
	}

	@Override
	public void onDisable() {
		this.blockByShield(false);
		super.onDisable();
	}

	public void blockByShield(boolean state) {
		KillAura.blockstate=true;
		if (Wrapper.INSTANCE.player().getHeldItem() == null)
			return;
		if (!(Wrapper.INSTANCE.player().getHeldItem().getItem() instanceof ItemSword))
			return;
		if (KillAura.getTarget() == null) {
			KillAura.blockstate=false;
			return;
		}
		if (INTERACT.getValue()) {
			if (KillAura.getTarget() != null) {
				Wrapper.INSTANCE.sendPacket(
						new C02PacketUseEntity(KillAura.getTarget(), new Vec3((double) randomNumber(-50, 50) / 100.0D,
								(double) randomNumber(0, 200) / 100.0D, (double) randomNumber(-50, 50) / 100.0D)));
				Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity());

			}
		}
		 KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);
	//Wrapper.INSTANCE.sendPacket(new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().inventory.getCurrentItem()));
	 if (Wrapper.INSTANCE.controller().sendUseItem(Wrapper.INSTANCE.player(),Wrapper.INSTANCE.world(), Wrapper.INSTANCE.player().inventory.getCurrentItem())) {
         Wrapper.INSTANCE.mc().getItemRenderer().resetEquippedProgress2();
     }
	//KeyBinding.setKeyBindState(Wrapper.INSTANCE.mc().gameSettings.keyBindUseItem.getKeyCode(), true);

	}

	public static void block(boolean state) {
		AutoShield hack = (AutoShield) HackManager.getHack("AutoShield");
		if (hack.isToggled())
			hack.blockByShield(state);
	}

	public static int randomNumber(int max, int min) {
		return Math.round((float) min + (float) Math.random() * (float) (max - min));
	}
}
