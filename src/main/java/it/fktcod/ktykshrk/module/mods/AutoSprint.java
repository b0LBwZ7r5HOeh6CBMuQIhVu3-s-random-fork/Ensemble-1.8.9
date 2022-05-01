package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.clickgui.click.ClickGuiScreen;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoSprint extends Module{

	public AutoSprint() {

		super("AutoSprint", HackCategory.PLAYER);
		this.setChinese(Core.Translate_CN[17]);
	}
	
	@Override
    public String getDescription() {
        return "Sprints automatically when you should be walking.";
    }
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(isMoveInGui() && this.canSprint(false)) {
			Wrapper.INSTANCE.player().setSprinting(true); 
			return;
		}
		if(this.canSprint(true))
			Wrapper.INSTANCE.player().setSprinting(Utils.isMoving(Wrapper.INSTANCE.player()));
		super.onClientTick(event);
	}
	
	boolean isMoveInGui() {
		return Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode())
				&& (boolean)(Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer
				|| Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen) 
				&& HackManager.getHack("InvMove").isToggled();
	}
	
	boolean canSprint(boolean forward) {
		if(!Wrapper.INSTANCE.player().onGround) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isSprinting()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isOnLadder()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isInWater()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isInLava()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isCollidedHorizontally) {
			return false;
		}
		if(forward && Wrapper.INSTANCE.player().moveForward < 0.1F) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isSneaking()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().getFoodStats().getFoodLevel() < 6) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isRiding()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isPotionActive(Potion.blindness)) {
			return false;
		}
		if(HackManager.getHack("Scaffold").isToggled()) {
			return false;
		}
        return true;
    }
}
