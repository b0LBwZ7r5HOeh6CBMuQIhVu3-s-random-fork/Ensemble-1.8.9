package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import ensemble.mixin.cc.mixin.interfaces.IKeyBind;
import net.minecraft.client.settings.KeyBinding;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements IKeyBind {

	@Shadow
	private boolean pressed;
	
	@Override
	public boolean getPress() {
		return pressed;
	}

	@Override
	public void setPress(boolean press) {
		pressed=press;
		
	}
	
}
