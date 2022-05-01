package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.fktcod.ktykshrk.utils.PhysicUtils;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

	@Overwrite
	public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
		PhysicUtils.RenderEntityItem(entity, x, y, z, entityYaw, partialTicks);

	}

}
