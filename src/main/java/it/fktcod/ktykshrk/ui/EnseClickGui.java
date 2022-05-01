package it.fktcod.ktykshrk.ui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL45;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.FontManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.huangbai.ClickGuiRender;
import it.fktcod.ktykshrk.ui.huangbai.Colors2;
import it.fktcod.ktykshrk.utils.visual.ColorUtils;
import it.fktcod.ktykshrk.utils.visual.Colors;
import it.fktcod.ktykshrk.utils.visual.HGLUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.utils.visual.font.render.TTFFontRenderer;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.ResourceLocation;

public class EnseClickGui extends GuiScreen {
	public static Minecraft mc = Minecraft.getMinecraft();
	public static ScaledResolution sr = new ScaledResolution(mc);
	HackCategory currentModuleType = HackCategory.PLAYER;
	Module currentModule = HackManager.getModulesInType(currentModuleType).get(0);
	public static float startX = sr.getScaledWidth() / 2 - 450 / 2, startY = sr.getScaledHeight() / 2 - 300 / 2;
	public static int moduleStart = 0;
	public static int valueStart = 0;
	boolean previousmouse = true;
	boolean mouse;
	public float moveX = 0, moveY = 0;
	boolean bind = false;
	float hue;
	public static int alpha;
	public static int alphe = 121;
	int time = 0;

	int press = 0;

	Boolean select1 = true;
	Boolean select2 = false;
	Boolean select3 = false;
	Boolean select4 = false;
	Boolean select5 = false;

	// resource
	private ResourceLocation LOGO = new ResourceLocation(Core.MODID, "ui/title.png");

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		TTFFontRenderer sigmaFont = Core.fontManager.getFont("SFB 8");
		TTFFontRenderer simpleton = Core.fontManager.getFont("SFB 6");

		sr = new ScaledResolution(mc);
		if (alpha < 255) {
			alpha += 5;
		}
		if (this.hue > 255.0f) {
			this.hue = 0.0f;
		}
		float h = this.hue;
		float h2 = this.hue + 85.0f;
		float h3 = this.hue + 170.0f;
		if (h > 255.0f) {
			h = 0.0f;
		}
		if (h2 > 255.0f) {
			h2 -= 255.0f;
		}
		if (h3 > 255.0f) {
			h3 -= 255.0f;
		}
		Color color33 = Color.getHSBColor((float) (h / 255.0f), (float) 0.9f, (float) 1.0f);
		Color color332 = Color.getHSBColor((float) (h2 / 255.0f), (float) 0.9f, (float) 1.0f);
		Color color333 = Color.getHSBColor((float) (h3 / 255.0f), (float) 0.9f, (float) 1.0f);
		int color1 = color33.getRGB();
		int color2 = color332.getRGB();
		int color3 = color333.getRGB();
		int color4 = new Color(255, 255, 255, alpha).getRGB();
		this.hue = (float) ((double) this.hue + 0.1);

		// ALL
		GL11.glPushMatrix();
		HGLUtils.drawRect(startX, startY, startX + 400, startY + 300, Colors.getColor(30, 30, 30));
		GL11.glPopMatrix();

		// SideWay

		ClickGuiRender.drawGradientSideways((double) (startX), (double) (startY + 49f),
				(double) (startX + (float) (400 / 2)), (double) ((double) startY + 50), (int) color1, (int) color2);
		ClickGuiRender.drawGradientSideways((double) (startX + (float) (400 / 2)), (double) (startY + 49f),
				(double) (startX + (float) 400), (double) ((double) startY + 50), (int) color2, (int) color3);

		// LOGO
		/*
		 * GL11.glPushMatrix(); GL11.glColor4f(1, 1, 1, 1);
		 * mc.getTextureManager().bindTexture(LOGO);
		 * Gui.drawModalRectWithCustomSizedTexture((int) startX + 10, (int) startY + 10,
		 * 0, 0, 140, 26, 140, 26);
		 * 
		 * GL11.glPopMatrix();
		 */

		// Line
		//ClickGuiRender.drawRect(startX + 170, startY + 50, startX + 171, startY + 300, Colors.getColor(59, 59, 59));

		// Categoty

		GL11.glPushMatrix();
		ClickGuiRender.drawRect(startX + 170, startY, startX + 170 + 40, ((double) startY + 49),
				Colors.getColor(63, 63, 63));
		ResourceLocation player = new ResourceLocation(Core.MODID, select1 ? "ui/player3.png" : "ui/player3.png");
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(player);
		//Gui.drawModalRectWithCustomSizedTexture((int) startX + 170 + 5, (int) startY + 6, 0, 0, 25, 25, 30, 30);
		GL11.glPopMatrix();
		
		sigmaFont.drawString(" ", startX + 160 + 5, startY + 40,
				select1 ? Color.green.getRGB() : Colors.getColor(255, 255, 255, 200));
		
		sigmaFont.drawString("PLAYER", startX + 170 + 5, startY + 40,
				select1 ? Color.green.getRGB() : Colors.getColor(255, 255, 255, 200));
		//Client.fontManager.getFont("ense 12").drawStringWithShadow(FontManager.ICON_PLAYER, startX + 170 + 5, startY + 40, Color.white.getRGB());

		if (this.isCategoryHovered(startX + 170, startY, startX + 170 + 40, startY + 49, mouseX, mouseY)
				&& Mouse.isButtonDown((int) 0)) {
			select1 = true;
			select5 = false;
			select2 = false;
			select3 = false;
			select4 = false;
			currentModuleType = HackCategory.PLAYER;
			currentModule = HackManager.getModulesInType(currentModuleType).get(0);
			moduleStart = 0;
			valueStart = 0;
			for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
				Value value = currentModule.getValues().get(x1);
			}

		}
		
		ClickGuiRender.drawRect(startX + 170 + 40, startY, startX + 170 + 40 + 40, ((double) startY + 49),
				Colors.getColor(63, 63, 63));
		ResourceLocation visual = new ResourceLocation(Core.MODID, select2 ? "ui/render3.png" : "ui/render3.png");
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(visual);
		//Gui.drawModalRectWithCustomSizedTexture((int) startX + 170 + 45, (int) startY + 8, 0, 0, 25, 25, 25, 25);
		sigmaFont.drawString("VISUAL", startX + 170 + 43, startY + 40,
				select2 ? Color.green.getRGB() : Colors.getColor(255, 255, 255, 200));

		if (this.isCategoryHovered(startX + 170 + 40, startY, startX + 170 + 40 + 40, (startY + 49), mouseX, mouseY)
				&& Mouse.isButtonDown((int) 0)) {
			select2 = true;
			select5 = false;
			select1 = false;
			select3 = false;
			select4 = false;

			currentModuleType = HackCategory.VISUAL;
			currentModule = HackManager.getModulesInType(currentModuleType).get(0);
			moduleStart = 0;
			valueStart = 0;
			for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
				Value value = currentModule.getValues().get(x1);
			}

		}
		

		ClickGuiRender.drawRect(startX + 170 + 80, startY, startX + 170 + 80 + 40, ((double) startY + 49),
				Colors.getColor(63, 63, 63));
		ResourceLocation combat = new ResourceLocation(Core.MODID, select3 ? "ui/combat3.png" : "ui/combat3.png");
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(combat);
		//Gui.drawModalRectWithCustomSizedTexture((int) startX + 170 + 85, (int) startY + 8, 0, 0, 25, 25, 25, 25);
		sigmaFont.drawString("COMBAT", startX + 170 + 78, startY + 40,
				select3 ? Color.green.getRGB() : Colors.getColor(255, 255, 255, 200));
		if (this.isCategoryHovered(startX + 170 + 80, startY, startX + 170 + 80 + 40, (startY + 49), mouseX, mouseY)
				&& Mouse.isButtonDown((int) 0)) {
			select3 = true;
			select5 = false;
			select1 = false;
			select2 = false;
			select4 = false;
			currentModuleType = HackCategory.COMBAT;
			currentModule = HackManager.getModulesInType(currentModuleType).get(0);
			moduleStart = 0;
			valueStart = 0;
			for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
				Value value = currentModule.getValues().get(x1);
			}

		}

		ClickGuiRender.drawRect(startX + 170 + 120, startY, startX + 170 + 120 + 40, ((double) startY + 49),
				Colors.getColor(63, 63, 63));
		ResourceLocation another = new ResourceLocation(Core.MODID, select4 ? "ui/another3.png" : "ui/another3.png");
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(another);
		//Gui.drawModalRectWithCustomSizedTexture((int) startX + 170 + 125, (int) startY + 8, 0, 0, 25, 25, 25, 25);
		sigmaFont.drawString("ANOTHER", startX + 170 + 120, startY + 40,
				select4 ? Color.green.getRGB() : Colors.getColor(255, 255, 255, 200));
		if (this.isCategoryHovered(startX + 170 + 120, startY, startX + 170 + 120 + 40, (startY + 49), mouseX, mouseY)
				&& Mouse.isButtonDown((int) 0)) {
			select4 = true;
			select5 = false;
			select1 = false;
			select2 = false;
			select3 = false;

			currentModuleType = HackCategory.ANOTHER;
			currentModule = HackManager.getModulesInType(currentModuleType).get(0);
			moduleStart = 0;
			valueStart = 0;
			for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
				Value value = currentModule.getValues().get(x1);
			}

		}

		ClickGuiRender.drawRect(startX + 170 + 160, startY, startX + 170 + 160 + 40, ((double) startY + 49),
				Colors.getColor(63, 63, 63));
		ResourceLocation movement = new ResourceLocation(Core.MODID,
				select5 ? "ui/movement3.png" : "ui/movement3.png");
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(movement);
		//Gui.drawModalRectWithCustomSizedTexture((int) startX + 170 + 165, (int) startY + 8, 0, 0, 25, 25, 25, 25);
		sigmaFont.drawString("MOVE", startX + 170 + 165, startY + 40,
				select5 ? Color.green.getRGB() : Colors.getColor(255, 255, 255, 200));
		if (this.isCategoryHovered(startX + 170 + 160, startY, startX + 170 + 160 + 40, (startY + 49), mouseX, mouseY)
				&& Mouse.isButtonDown((int) 0)) {
			select5 = true;
			select1 = false;
			select2 = false;
			select3 = false;
			select4 = false;

			currentModuleType = HackCategory.MOVEMENT;
			currentModule = HackManager.getModulesInType(currentModuleType).get(0);
			moduleStart = 0;
			valueStart = 0;
			for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
				Value value = currentModule.getValues().get(x1);
			}

		}

		ResourceLocation exit = new ResourceLocation(Core.MODID, "ui/exit.png");
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(exit);
		//Gui.drawModalRectWithCustomSizedTexture((int) startX + 170 + 202, (int) startY + 8, 0, 0, 25, 25, 25, 25);

		int m = Mouse.getDWheel();

		if (this.isCategoryHovered(startX, startY + 40, startX + 100, startY + 315, mouseX, mouseY)) {
			if (m < 0 && moduleStart < HackManager.getModulesInType(currentModuleType).size() - 1) {
				moduleStart++;
			}
			if (m > 0 && moduleStart > 0) {
				moduleStart--;
			}
		}
		if (this.isCategoryHovered(startX + 100, startY + 50, startX + 430, startY + 315, mouseX, mouseY)) {
			if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
				valueStart++;
			}
			if (m > 0 && valueStart > 0) {
				valueStart--;
			}
		}

		float mY = startY + 6;
		for (int i = 0; i < HackManager.getModulesInType(currentModuleType).size(); i++) {
			Module module = HackManager.getModulesInType(currentModuleType).get(i);
			if (mY > startY + 250)
				break;
			if (i < moduleStart) {
				continue;
			}

			if (!module.isToggled()) {
				RenderUtils.drawUnfilledRect(startX + 15, mY + 53, 15, 6, Colors.getColor(101, 101, 101), 1);
				ClickGuiRender.drawRect(startX + 16, mY + 54, startX + 16 + 5, mY + 54 + 5,
						Colors.getColor(216, 106, 106));
				ClickGuiRender.drawRect(startX, mY + 45, startX, mY + 70,
						isSettingsButtonHovered(startX, mY + 45, startX + 100, mY + 70, mouseX, mouseY)
								? new Color(60, 60, 60, alpha).getRGB()
								: new Color(35, 35, 35, alpha).getRGB());
				/*
				 * ClickGuiRender.drawFilledCircle(startX + (isSettingsButtonHovered(startX , mY
				 * + 45, startX + 100, mY + 70, mouseX, mouseY) ? 112 : 110), mY + 58, 3, new
				 * Color(70, 70, 70, alpha).getRGB(), 5);
				 */
				if (module.getKey() != -1) {
					sigmaFont.drawString("<Key:" + Keyboard.getKeyName(module.getKey()) + ">", startX + 100, mY + 55,
							Colors.getColor(142, 142, 142));
				} else {
					sigmaFont.drawString("<Key:" + "NONE" + ">", startX + 100, mY + 55, Colors.getColor(142, 142, 142));
				}

				sigmaFont.drawString(module.getName(), (int) startX + 40, (int) mY + 55,
						new Color(175, 175, 175, alpha).getRGB());
				if (isHovered(startX + 100, mY + 55, startX + 160, mY + 65, mouseX, mouseY) && Mouse.isButtonDown(0)) {
					this.bind = true;
					RenderUtils.drawUnfilledRect(startX + 100, mY + 50, 50, 14, Colors.getColor(101, 101, 101), 1);
					keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
					FileManager.saveHacks();

				}
			} else {
				RenderUtils.drawUnfilledRect(startX + 15, mY + 53, 15, 6, Colors.getColor(101, 101, 101), 1);
				ClickGuiRender.drawRect(startX + 30 - 5, mY + 54, startX + 30, mY + 54 + 5,
						Colors.getColor(106, 216, 137));
				if (module.getKey() != -1) {
					sigmaFont.drawString("<Key:" + Keyboard.getKeyName(module.getKey()) + ">", startX + 100, mY + 55,
							Colors.getColor(142, 142, 142));
				} else {
					sigmaFont.drawString("<Key:" + "NONE" + ">", startX + 100, mY + 55, Colors.getColor(142, 142, 142));
				}

				if (isHovered(startX + 100, mY + 55, startX + 160, mY + 65, mouseX, mouseY) && Mouse.isButtonDown(0)) {
					this.bind = true;
					RenderUtils.drawUnfilledRect(startX + 100, mY + 50, 50, 14, Colors.getColor(101, 101, 101), 1);
					keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());

				}

				sigmaFont.drawString(module.getName(), (int) startX + 40, (int) mY + 55,
						new Color(255, 255, 255, alpha).getRGB());

			}

			if (isSettingsButtonHovered(startX, mY + 45, startX + 100, mY + 70, mouseX, mouseY)) {
				if (!this.previousmouse && Mouse.isButtonDown(0)) {
					if (module.isToggled()) {
						module.setToggled(false);
						module.onDisable();
					} else {
						module.setToggled(true);
						module.onEnable();
					}
					previousmouse = true;
				}
				if (!this.previousmouse && Mouse.isButtonDown(1)) {
					previousmouse = true;
				}
			}

			if (!Mouse.isButtonDown(0)) {
				this.previousmouse = false;
			}
			if (isSettingsButtonHovered(startX, mY + 45, startX + 100, mY + 70, mouseX, mouseY)
					&& Mouse.isButtonDown(1)) {

				for (Module module1 : HackManager.getModulesInType(currentModuleType)) {
					// mod.clickanim = 115;
				}
				sigmaFont.drawString("+", startX + 5, mY + 54, Color.green.getRGB());
				currentModule = module;
				valueStart = 0;
			}
			mY += 25;
		}
		mY = startY + 12;

		if (currentModule != null && currentModule.getDescription() != null) {
			simpleton.drawString(currentModule.getDescription(), (int) startX + 200, (int) mY + 42,
					new Color(170, 170, 170).getRGB());
		}
		for (int i = 0; i < currentModule.getValues().size(); i++) {
			if (mY > startY + 220)
				break;
			if (i < valueStart) {
				continue;
			}
			Value value = currentModule.getValues().get(i);
			if (value instanceof NumberValue) {
				float x = startX + 300;
				double render = (double) (68.0F
						* (((NumberValue) value).getValue().floatValue() - ((NumberValue) value).getMin().floatValue())
						/ (((NumberValue) value).getMax().floatValue() - ((NumberValue) value).getMin().floatValue()));
				ClickGuiRender.drawRect((float) x + 2, mY + 52, (float) ((double) x + 75), mY + 53,
						isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY) && Mouse.isButtonDown((int) 0)
								? new Color(80, 80, 80, alpha).getRGB()
								: (new Color(30, 30, 30, alpha)).getRGB());
				ClickGuiRender.drawRect((float) x + 2, mY + 52, (float) ((double) x + render + 6.5D), mY + 53,
						new Color(39, 153, 91, alpha).getRGB());
				ClickGuiRender.drawFilledCircle((float) ((double) x + render + 2D) + 3, mY + 52.25, 2.5,
						new Color(39, 153, 91, alpha).getRGB(), 5);

				// RenderUtils.DrawTriangle(new Color(39, 153, 91, alpha).getRGB(),(float) ( x +
				// render + 2D) + 3,(float)(mY + 52.25),(float) ((double) x + render + 2D) +
				// 2,(float)(mY + 53.25),(float) ((double) x + render + 2D) + 2,(float)(mY +
				// 51.25));

				sigmaFont.drawString(value.getName(), (int) startX + 200, (int) mY + 50,
						new Color(175, 175, 175, alpha).getRGB());
				sigmaFont.drawString(value.getValue().toString(),
						(int) startX + 300 - (int) sigmaFont.getWidth(value.getValue().toString()), (int) mY + 50,
						new Color(255, 255, 255, alpha).getRGB());
				if (!Mouse.isButtonDown((int) 0)) {
					this.previousmouse = false;
				}
				if (this.isButtonHovered(x, mY + 45, x + 100, mY + 57, mouseX, mouseY) && Mouse.isButtonDown((int) 0)) {
					if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
						render = ((NumberValue) value).getMin().doubleValue();
						double max = ((NumberValue) value).getMax().doubleValue();
						double inc = 0.01;
						double valAbs = (double) mouseX - ((double) x + 1.0D);
						double perc = valAbs / 68.0D;
						perc = Math.min(Math.max(0.0D, perc), 1.0D);
						double valRel = (max - render) * perc;
						double val = render + valRel;
						val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
						value.setValue(val);
					}
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
				}
				mY += 20;

				FileManager.saveHacks();

			}
			if (value instanceof BooleanValue) {
				float x = startX + 300;
				int xx = 30;
				int x2x = 45;
				sigmaFont.drawString(value.getName(), (int) startX + 200, (int) mY + 50,
						new Color(175, 175, 175, alpha).getRGB());
				if ((boolean) value.getValue()) {
					ClickGuiRender.drawRect(x + xx, mY + 50, x + x2x, mY + 59,
							isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)
									? new Color(80, 80, 80, alpha).getRGB()
									: new Color(20, 20, 20, alpha).getRGB());
					ClickGuiRender.drawFilledCircle(x + xx, mY + 54.5, 4.5,
							isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)
									? new Color(80, 80, 80, alpha).getRGB()
									: new Color(20, 20, 20, alpha).getRGB(),
							10);
					ClickGuiRender.drawFilledCircle(x + x2x, mY + 54.5, 4.5,
							isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)
									? new Color(80, 80, 80, alpha).getRGB()
									: new Color(20, 20, 20, alpha).getRGB(),
							10);
					ClickGuiRender.drawFilledCircle(x + x2x, mY + 54.5, 4, new Color(60, 143, 113, alpha).getRGB(), 10);
				} else {
					ClickGuiRender.drawRect(x + xx, mY + 50, x + x2x, mY + 59,
							isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)
									? new Color(80, 80, 80, alpha).getRGB()
									: new Color(20, 20, 20, alpha).getRGB());
					ClickGuiRender.drawFilledCircle(x + xx, mY + 54.5, 4.5,
							isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)
									? new Color(80, 80, 80, alpha).getRGB()
									: new Color(20, 20, 20, alpha).getRGB(),
							10);
					ClickGuiRender.drawFilledCircle(x + x2x, mY + 54.5, 4.5,
							isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)
									? new Color(80, 80, 80, alpha).getRGB()
									: new Color(20, 20, 20, alpha).getRGB(),
							10);
					ClickGuiRender.drawFilledCircle(x + xx, mY + 54.5, 4, new Color(180, 93, 93, alpha).getRGB(), 10);
				}
				if (this.isCheckBoxHovered(x + xx - 5, mY + 50, x + x2x + 6, mY + 59, mouseX, mouseY)) {
					if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
						this.previousmouse = true;
						this.mouse = true;
					}

					if (this.mouse) {
						value.setValue(!(boolean) value.getValue());
						this.mouse = false;
					}
				}
				if (!Mouse.isButtonDown((int) 0)) {
					this.previousmouse = false;
				}
				mY += 20;
				FileManager.saveHacks();

			}
			if (value instanceof ModeValue) {
				float x = startX + 300;
				sigmaFont.drawStringWithShadow(value.getName(), startX + 200, mY + 52,
						new Color(175, 175, 175, alpha).getRGB());
				ClickGuiRender.drawRect(x + 5, mY + 45, x + 75, mY + 65,
						isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)
								? new Color(80, 80, 80, alpha).getRGB()
								: new Color(56, 56, 56, alpha).getRGB());
				ClickGuiRender.drawRect(x + 2, mY + 48, x + 78, mY + 62,
						isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)
								? new Color(80, 80, 80, alpha).getRGB()
								: new Color(56, 56, 56, alpha).getRGB());
				ClickGuiRender.drawFilledCircle(x + 5, mY + 48, 3,
						isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)
								? new Color(80, 80, 80, alpha).getRGB()
								: new Color(56, 56, 56, alpha).getRGB(),
						5);
				ClickGuiRender.drawFilledCircle(x + 5, mY + 62, 3,
						isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)
								? new Color(80, 80, 80, alpha).getRGB()
								: new Color(56, 56, 56, alpha).getRGB(),
						5);
				ClickGuiRender.drawFilledCircle(x + 75, mY + 48, 3,
						isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)
								? new Color(80, 80, 80, alpha).getRGB()
								: new Color(56, 56, 56, alpha).getRGB(),
						5);
				ClickGuiRender.drawFilledCircle(x + 75, mY + 62, 3,
						isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)
								? new Color(80, 80, 80, alpha).getRGB()
								: new Color(56, 56, 56, alpha).getRGB(),
						5);
				if (((ModeValue) value).getSelectMode() != null) {
					sigmaFont.drawStringWithShadow("<" + ((ModeValue) value).getSelectMode().getName() + ">", (float) (x
							+ 40 - sigmaFont.getWidth("<" + ((ModeValue) value).getSelectMode().getName() + ">") / 2),
							mY + 53, new Color(255, 255, 255, alpha).getRGB());
				}
				int l;
				if (this.isStringHovered(x, mY + 45, x + 75, mY + 65, mouseX, mouseY)) {
					if (Mouse.isButtonDown((int) 0) && !this.previousmouse) {
						press++;
						final ModeValue modeValue = (ModeValue) value;
						ArrayList<Mode> modes = new ArrayList<Mode>();

						String t1 = null;
						for (Mode mode : modeValue.getModes()) {
							modes.add(mode);

						}
						if (press <= modes.size()) {
							//System.out.println(press);
							modes.get(press - 1).setToggled(true);
							t1 = modes.get(press - 1).getName();
						} else {
							press = 0;
						}

						for (Mode mode : modeValue.getModes()) {
							if (mode.getName() != t1) {
								mode.setToggled(false);

							}

						}
						this.previousmouse = true;
					}

				}
				mY += 25;
			}
			FileManager.saveHacks();

		}

		float x = startX + 300;
		float yyy = startY + 200;

		if ((isHovered(startX, startY, startX + 450, startY + 50, mouseX, mouseY)
				|| isHovered(startX, startY + 315, startX + 450, startY + 350, mouseX, mouseY)
				|| isHovered(startX + 430, startY, startX + 450, startY + 350, mouseX, mouseY))
				&& Mouse.isButtonDown(0)) {
			if (moveX == 0 && moveY == 0) {
				moveX = mouseX - startX;
				moveY = mouseY - startY;
			} else {
				startX = mouseX - moveX;
				startY = mouseY - moveY;
			}
			this.previousmouse = true;
		} else if (moveX != 0 || moveY != 0) {
			moveX = 0;
			moveY = 0;
		}
		if (isHovered(sr.getScaledWidth() / 2 - 40, 0, sr.getScaledWidth() / 2 + 40, 20, mouseX, mouseY)
				&& Mouse.isButtonDown(0)) {
			alpha = 0;
			alphe = 0;
		} else {
			alphe = 121;
		}

		int j = 59;
		int l = 40;
		float k = startY + 10;
		float xx = startX + 5;
		float typey;

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void onGuiClosed() {
		alpha = 0;
	}

	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (this.bind) {
			currentModule.setKey(keyCode);
			if (keyCode == 1) {
				currentModule.setKey(0);
			}
			this.bind = false;
		} else if (keyCode == 1) {
			this.mc.displayGuiScreen((GuiScreen) null);
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
			return;
		}
	}

}
