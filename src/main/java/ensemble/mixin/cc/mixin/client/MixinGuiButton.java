package ensemble.mixin.cc.mixin.client;

import org.fusesource.jansi.Ansi.Color;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.FontManager;
import it.fktcod.ktykshrk.utils.visual.Colors;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

@Mixin(GuiButton.class)
public abstract class MixinGuiButton {

	@Shadow
	public int width;
	@Shadow
	public int height;
	@Shadow
	public int xPosition;
	@Shadow
	public int yPosition;
	@Shadow
	public String displayString;
	@Shadow
	public int id;
	@Shadow
	public boolean enabled;
	@Shadow
	public boolean visible;
	@Shadow
	protected boolean hovered;
	@Shadow
	public int packedFGColour; // FML

	@Shadow
	protected abstract int getHoverState(boolean mouseOver);

	@Shadow
	protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

	@Overwrite
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {

		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRendererObj;
			GL11.glColor4f(1, 1, 1, 1);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
					&& mouseY < this.yPosition + this.height;
			int i = this.getHoverState(this.hovered);
			Gui.drawRect(this.xPosition, this.yPosition, this.xPosition+this.width, this.yPosition+this.height, Colors.getColor(150, 150, 150, 100));

			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (packedFGColour != 0) {
				j = packedFGColour;
			} else if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
				Gui.drawRect(this.xPosition, this.yPosition+this.height-2, this.xPosition+this.width, this.yPosition+this.height, Colors.getColor(255,255,255,100));
			}

			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
					this.yPosition + (this.height - 8) / 2, j);
		}
	}

	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		fontRendererIn.drawStringWithShadow(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y,
				color);
	}

}