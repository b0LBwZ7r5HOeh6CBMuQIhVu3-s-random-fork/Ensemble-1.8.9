package ensemble.mixin.cc.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventLoop;
import it.fktcod.ktykshrk.eventapi.EventManager;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraft {
	@Shadow
	public int displayWidth;

	@Shadow
	public int displayHeight;

	@Shadow
	private ResourceLocation mojangLogo;

	@Shadow
	@Final
	private static Logger logger;

	@Mutable
	@Shadow
	@Final
	private static ResourceLocation locationMojangPng;

	@Shadow
	@Final
	public DefaultResourcePack mcDefaultResourcePack;


	@Inject(method = "run", at = @At("HEAD"))
	private void init(CallbackInfo callbackInfo) {

		if (displayWidth < 1280)
			displayWidth = 1280;

		if (displayHeight < 720)
			displayHeight = 720;
	}

	@Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void loadWorld(WorldClient p_loadWorld_1_, String p_loadWorld_2_, final CallbackInfo callbackInfo) {
		Core.INSTANCE().ReCheck();
    }
	
	@Overwrite
	public void drawSplashScreen(TextureManager p_drawSplashScreen_1_) {
		//textures/loading.png
		locationMojangPng = new ResourceLocation(Core.MODID,"client/loading.png");
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
		int i = scaledresolution.getScaleFactor();
		Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
		framebuffer.bindFramebuffer(false);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, (double) scaledresolution.getScaledWidth(), (double) scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		InputStream inputstream = null;

		try {
			inputstream = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
			this.mojangLogo = p_drawSplashScreen_1_.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstream)));
			p_drawSplashScreen_1_.bindTexture(this.mojangLogo);
		} catch (IOException var12) {
			logger.error("Unable to load logo: " + locationMojangPng, var12);
		} finally {
			IOUtils.closeQuietly(inputstream);
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Core.MODID,"client/loading.png"));
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int j = 256;
		int k = 256;
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		framebuffer.unbindFramebuffer();
		framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		this.updateDisplay();
	}

	@Shadow
	public abstract void updateDisplay();

	 @Inject(method = "runGameLoop", at = @At("HEAD"))
	    private void onLoop(CallbackInfo ci) {
	        EventManager.call(new EventLoop());
	    }

}