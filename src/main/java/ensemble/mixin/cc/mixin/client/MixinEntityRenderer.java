package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.fktcod.ktykshrk.managers.HackManager;

import org.spongepowered.asm.mixin.injection.At;


import net.minecraft.client.renderer.EntityRenderer;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    /*
    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffect(float ticks, CallbackInfo info) {
        if (HackManager.getHack("NoHurtCam").isToggled()) info.cancel();
    }

     */

}