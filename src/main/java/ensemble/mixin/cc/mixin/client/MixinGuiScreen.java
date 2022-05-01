package ensemble.mixin.cc.mixin.client;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

	@Shadow
	protected abstract void keyTyped(char typedChar, int keyCode) throws IOException;

	@Overwrite
	public void handleKeyboardInput() throws IOException {
		int k = Keyboard.getEventKey();
		char c = Keyboard.getEventCharacter();
		if ((Keyboard.getEventKeyState()) || ((k == 0) && (Character.isDefined(c)))) {
			keyTyped(c, k);
		}
	}

}
