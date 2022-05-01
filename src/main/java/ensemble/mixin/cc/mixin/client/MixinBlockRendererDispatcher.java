package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.fktcod.ktykshrk.event.EventRenderBlock;
import it.fktcod.ktykshrk.eventapi.EventManager;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

@Mixin(BlockRendererDispatcher.class)
public class MixinBlockRendererDispatcher {
	@Inject(method = "renderBlock", at = { @At("HEAD") })
	public void eventUpdate(IBlockState state, BlockPos pos, IBlockAccess blockAccess, WorldRenderer worldRendererIn, CallbackInfoReturnable info) {
		EventRenderBlock event = new EventRenderBlock(pos.getX(), pos.getY(), pos.getZ(), state.getBlock() , pos);
		EventManager.call(event);
	}
}
