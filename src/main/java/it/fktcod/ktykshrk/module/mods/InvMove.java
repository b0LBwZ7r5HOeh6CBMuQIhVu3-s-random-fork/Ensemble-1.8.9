package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class InvMove extends Module {

	public ModeValue mode;
	boolean flag;

	public InvMove() {
		super("InvMove", HackCategory.PLAYER);

		mode = new ModeValue("Mode", new Mode("Basic", true), new Mode("Hypixel", false));
		this.addValue(mode);
		this.setChinese(Core.Translate_CN[53]);
	}

	@Override
	public String getDescription() {
		return "Allows you to walk while the gui is open.";
	}

	/*
	 * @Override public void onInputUpdate(InputUpdateEvent event) { if
	 * (!mode.getMode("AAC").isToggled()) { return; } if
	 * (!(Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer) &&
	 * !(Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen)) return;
	 * 
	 * if (Wrapper.INSTANCE.player().onGround &&
	 * Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindJump.getKeyCode())) {
	 * Wrapper.INSTANCE.player().jump(); } if
	 * (Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindRight.getKeyCode()))
	 * return;
	 * 
	 * super.onInputUpdate(event); }
	 */

	@Override
	public void onClientTick(ClientTickEvent event) {

		if (Wrapper.INSTANCE.mc().currentScreen != null && !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat)) {
			flag = true;

			if (mode.getMode("Hypixel").isToggled()) {
				try {
					int i = 0;
					for (; i < 8; i++) {
						ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
						if (stack == null)
							break;
						if (!(stack.getItem() instanceof ItemFood) && !(stack.getItem() instanceof ItemSword)
								&& Item.getIdFromItem(stack.getItem()) != 345)
							break; // ����ΪFood Sword ָ����
					}

					if (i == 8 && Item.getIdFromItem(mc.thePlayer.inventory.getStackInSlot(8).getItem()) == 345)
						i--;

					Wrapper.INSTANCE.sendPacket(new C09PacketHeldItemChange(i));
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

			KeyBinding[] keys = new KeyBinding[] { Wrapper.INSTANCE.mc().gameSettings.keyBindForward,
					Wrapper.INSTANCE.mc().gameSettings.keyBindBack, Wrapper.INSTANCE.mc().gameSettings.keyBindLeft,
					Wrapper.INSTANCE.mc().gameSettings.keyBindRight, Wrapper.INSTANCE.mc().gameSettings.keyBindJump };
			int length = keys.length;

			for (int i = 0; i < length; ++i) {
				KeyBinding.setKeyBindState(keys[i].getKeyCode(), Keyboard.isKeyDown(keys[i].getKeyCode()));
			}
		} else {
			if (flag) {

				if (mode.getMode("Hypixel").isToggled()) {
					Wrapper.INSTANCE.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				}
				flag = false;

			}

		}
	}

	void moveForward(double speed) {
		float direction = Utils.getDirection();
		Wrapper.INSTANCE.player().motionX -= (double) (MathHelper.sin(direction) * speed);
		Wrapper.INSTANCE.player().motionZ += (double) (MathHelper.cos(direction) * speed);
	}

	void moveBack(double speed) {
		float direction = Utils.getDirection();
		Wrapper.INSTANCE.player().motionX += (double) (MathHelper.sin(direction) * speed);
		Wrapper.INSTANCE.player().motionZ -= (double) (MathHelper.cos(direction) * speed);
	}

	void moveLeft(double speed) {
		float direction = Utils.getDirection();
		Wrapper.INSTANCE.player().motionZ += (double) (MathHelper.sin(direction) * speed);
		Wrapper.INSTANCE.player().motionX += (double) (MathHelper.cos(direction) * speed);
	}

	void moveRight(double speed) {
		float direction = Utils.getDirection();
		Wrapper.INSTANCE.player().motionZ -= (double) (MathHelper.sin(direction) * speed);
		Wrapper.INSTANCE.player().motionX -= (double) (MathHelper.cos(direction) * speed);
	}

	void handleForward(double speed) {
		if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode()))
			return;
		moveForward(speed);
	}

	void handleBack(double speed) {
		if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindBack.getKeyCode()))
			return;
		moveBack(speed);
	}

	void handleLeft(double speed) {
		if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindLeft.getKeyCode()))
			return;
		moveLeft(speed);
	}

	void handleRight(double speed) {
		if (!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindRight.getKeyCode()))
			return;
		moveRight(speed);
	}

	void handleJump() {
		if (Wrapper.INSTANCE.player().onGround
				&& Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindJump.getKeyCode()))
			Wrapper.INSTANCE.player().jump();
	}

}
