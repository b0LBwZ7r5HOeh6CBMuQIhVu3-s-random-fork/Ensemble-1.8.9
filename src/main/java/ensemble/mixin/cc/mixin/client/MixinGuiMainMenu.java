package ensemble.mixin.cc.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu {

	@Shadow
	public abstract void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_);

	@Shadow
	public int width;
	@Shadow
	public int height;

	@Shadow
	protected abstract void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor);

	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");

	@Shadow
	public abstract void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height);

	@Shadow
	private float updateCounter;

	@Shadow
	protected FontRenderer fontRendererObj;
	
	@Shadow
	  private String splashText;
	
	@Shadow
	public abstract void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color);
	
	@Shadow
	public abstract void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color);
	
	@Shadow
	 private String openGLWarning1;
	
	@Shadow
	 private String openGLWarning2;
	
	@Shadow
	   private int field_92024_r;
	@Shadow
	    private int field_92023_s;
	@Shadow
	    private int field_92022_t;
	@Shadow
	    private int field_92021_u;
	@Shadow
	    private int field_92020_v;
	@Shadow
	    private int field_92019_w;
	@Shadow
	 protected List<GuiButton> buttonList = Lists.<GuiButton>newArrayList();
	@Shadow
	 public abstract boolean func_183501_a();
	
	@Shadow
	public abstract void drawRect(int left, int top, int right, int bottom, int color);
	
	@Shadow
	  private GuiScreen field_183503_M;

	@Overwrite
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		int i = 274;
		int j = this.width / 2 - i / 2;
		int k = 30;
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		Minecraft.getMinecraft().getTextureManager().bindTexture(minecraftTitleTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if ((double) this.updateCounter < 1.0E-4D) {
			this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 99, 44);
			this.drawTexturedModalRect(j + 99, k + 0, 129, 0, 27, 44);
			this.drawTexturedModalRect(j + 99 + 26, k + 0, 126, 0, 3, 44);
			this.drawTexturedModalRect(j + 99 + 26 + 3, k + 0, 99, 0, 26, 44);
			this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
		} else {
			this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 155, 44);
			this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float f = 1.8F - MathHelper.abs(
				MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		f = f * 100.0F / (float) (this.fontRendererObj.getStringWidth(this.splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
		GlStateManager.popMatrix();
		String s = "Minecraft 1.8.9";

		if (Minecraft.getMinecraft().isDemo()) {
			s = s + " Demo";
		}

		java.util.List<String> brandings = com.google.common.collect.Lists
				.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
		for (int brdline = 0; brdline < brandings.size(); brdline++) {
			String brd = brandings.get(brdline);
			if (!com.google.common.base.Strings.isNullOrEmpty(brd)) {
				this.drawString(this.fontRendererObj, brd, 2,
						this.height - (10 + brdline * (this.fontRendererObj.FONT_HEIGHT + 1)), 16777215);
			}
		}
		//net.minecraftforge.client.ForgeHooksClient.renderMainMenu(this, this.fontRendererObj, this.width, this.height);
		String s1 = "Copyright Mojang AB. Do not distribute!";
		this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2,
				this.height - 10, -1);

		if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1,
					1428160512);
			this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
			this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2,
					((GuiButton) this.buttonList.get(0)).yPosition - 12, -1);
		}

		//super.drawScreen(mouseX, mouseY, partialTicks);

		if (this.func_183501_a()) {
			this.field_183503_M.drawScreen(mouseX, mouseY, partialTicks);
		}

	}

}
