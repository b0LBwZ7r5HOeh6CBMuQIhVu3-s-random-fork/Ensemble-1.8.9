package ensemble.mixin.cc.mixin.client;

import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.FPS;
import it.fktcod.ktykshrk.utils.math.fps.FPSCore;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;

@Mixin(MathHelper.class)
public class MixinMathHelper {
    @Inject(method = "sin", at = @At("HEAD"), cancellable = true)
    private static void sin(float value, CallbackInfoReturnable<Float> callbackInfoReturnable){
        Float result=FPS.sin(value);

        if(result!=null)
            callbackInfoReturnable.setReturnValue(result);
    }

    @Inject(method = "cos", at = @At("HEAD"), cancellable = true)
    private static void cos(float value, CallbackInfoReturnable<Float> callbackInfoReturnable){
        Float result=FPS.cos(value);

        if(result!=null)
            callbackInfoReturnable.setReturnValue(result);
    }
}
