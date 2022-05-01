package ensemble.mixin.cc.mixin.client;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import net.minecraft.network.NetworkManager;

@Mixin(NetworkManager.class)
public class MixinConnection {
	@Inject(method = { "exceptionCaught" }, at = {
			@org.spongepowered.asm.mixin.injection.At("HEAD") }, cancellable = true)
	private void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_,
			CallbackInfo info) {
		if (((p_exceptionCaught_2_ instanceof IOException)) && (HackManager.getHack("AntiPacketKick").isToggled())) {
			info.cancel();
		}
	}
}
