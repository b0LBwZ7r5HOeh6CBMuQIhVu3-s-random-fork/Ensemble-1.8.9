package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FastUse extends Module{

	public static ModeValue mode = new ModeValue("FastUse",new Mode("Instant",false), new Mode("Hypixel",false), new Mode("Timer",true));

	net.minecraft.util.Timer timerS = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});
	TimerUtils timer = new TimerUtils();
	private boolean usedtimer = false;

	public FastUse() {
		super("FastUse", HackCategory.PLAYER);
		this.addValue(mode);
	}

	@Override
	public void onEnable() {
		usedtimer = false;
		timer.reset();
	}

	@Override
	public void onDisable() {
		if (usedtimer) {
			timerS.timerSpeed = 1F;
			usedtimer = false;
		}
	}


	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {

		if (mode.getMode("Instant").isToggled()) {
			if (mc.thePlayer.isUsingItem()) {
				Item usingItem = mc.thePlayer.getItemInUse().getItem();
				if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk
						|| usingItem instanceof ItemPotion) {
					if (timer.hasReached(750)) {
						mc.getNetHandler()
								.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
						mc.getNetHandler()
								.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getItemInUse()));
						for (int i = 0; i < 39; ++i) {
							mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
						}
						mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
								C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
						mc.getNetHandler()
								.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
						mc.playerController.onStoppedUsingItem(mc.thePlayer);
						timer.reset();
					}
				}
				if (!mc.thePlayer.isUsingItem()) {
					timer.reset();
				}
			}
		} else if (mode.getMode("Hypixel").isToggled()) {
			if (mc.thePlayer.isUsingItem()) {
				Item usingItem = mc.thePlayer.getItemInUse().getItem();
				if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk
						|| usingItem instanceof ItemPotion) {
					if (mc.thePlayer.getItemInUseDuration() >= 1) {
						mc.getNetHandler()
								.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					}
				}
				if (!mc.thePlayer.isUsingItem()) {
					timer.reset();
				}
			}
		} else if (mode.getMode("Timer").isToggled()) {
			if (usedtimer) {
				timerS.timerSpeed = 1F;
				usedtimer = false;
			}
			if (mc.thePlayer.isUsingItem()) {
				Item usingItem = mc.thePlayer.getItemInUse().getItem();
				if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk
						|| usingItem instanceof ItemPotion) {
					timerS.timerSpeed = 1.22f;
					usedtimer = true;
				}
			}
		}


	}
}
