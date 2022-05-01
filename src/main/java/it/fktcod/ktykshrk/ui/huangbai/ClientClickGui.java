package it.fktcod.ktykshrk.ui.huangbai;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import it.fktcod.ktykshrk.ForgeMod;
import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.HGLUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;
import it.fktcod.ktykshrk.wrappers.Wrapper;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClientClickGui extends GuiScreen {
	public static Minecraft mc = Minecraft.getMinecraft();
	public static ScaledResolution sr = new ScaledResolution(mc);
	HackCategory currentModuleType = HackCategory.VISUAL;
	Module currentModule = HackManager.getModulesInType(currentModuleType).get(0);
	public static float startX = sr.getScaledWidth() / 2 - 450 / 2, startY = sr.getScaledHeight() / 2 - 350 / 2;
	public static int moduleStart = 0;
	public static int valueStart = 0;
	boolean previousmouse = true;
	boolean mouse;
	public float moveX = 0, moveY = 0;
	boolean bind = false;
	float hue;
	public static int alpha;
	public static int alphe = 121;
	int time=0;
	
	int press = 0;
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRenderer fontc = mc.fontRendererObj;
		// startX = 100;
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
		// ////System.out.println(mc.displayWidth);
		HGLUtils.drawWindow((double) startX, (double) startY, (double) (startX + (float) 400),
				(double) (startY + (float) 300), (float) 0.5, (int) Colors2.getColor((int) 90, alpha),
				(int) Colors2.getColor((int) 0, alpha));
		HGLUtils.drawWindow((double) (startX + 1.0f), (double) (startY + 1.0f), (double) (startX + (float) 400 - 1.0f),
				(double) (startY + (float) 300 - 1.0f), (float) 1.0, (int) Colors2.getColor((int) 90, alpha),
				(int) Colors2.getColor((int) 61, alpha));
		HGLUtils.drawWindow((double) ((double) startX + 2.5), (double) ((double) startY + 2.5),
				(double) ((double) (startX + (float) 400) - 2.5), (double) ((double) (startY + (float) 300) - 2.5),
				(float) 0.5, (int) Colors2.getColor((int) 61, alpha), (int) Colors2.getColor((int) 0, alpha));
		HGLUtils.drawWindow((double) (startX + 3.0f), (double) (startY + 3.0f), (double) (startX + (float) 400 - 3.0f),
				(double) (startY + (float) 300 - 3.0f), (float) 0.5, (int) Colors2.getColor((int) 27, alpha),
				(int) Colors2.getColor((int) 61, alphe));
		// ////System.out.println();

		if (alpha >= 55) {
			ClickGuiRender.drawGradientSideways((double) (startX + 3.0f), (double) (startY + 3.0f),
					(double) (startX + (float) (400 / 2)), (double) ((double) startY + 4), (int) color1, (int) color2);
			ClickGuiRender.drawGradientSideways((double) (startX + (float) (400 / 2)), (double) (startY + 3.0f),
					(double) (startX + (float) 400 - 3.1f), (double) ((double) startY + 4), (int) color2, (int) color3);
		}

		ClickGuiRender.drawRect(startX + 98, startY + 100, startX + 290, startY + 108,
				new Color(30, 30, 30, alpha).getRGB());
		ClickGuiRender.drawRect(startX + 100, startY + 40, startX + 350, startY + 277,
				new Color(35, 35, 35, alpha).getRGB());
		ClickGuiRender.drawRect(startX + 200, startY + 100, startX + 350, startY + 277,
				new Color(37, 37, 37, alpha).getRGB());
		ClickGuiRender.drawRect(startX + 190, startY + 40, startX + 390, startY + 277,
				new Color(40, 40, 40, alpha).getRGB());// 中间横条
		// GLUtils.drawWindow(startX+3, startY+4, startX + 450, startY + 350, 0.1F,
		// -214748364, -214748364, -214748364);
		Wrapper.INSTANCE.fontRenderer().drawString("Ensemble", (int) (startX + 10), (int) startY + 15,
				new Color(180, 180, 180, alpha).getRGB());
		Wrapper.INSTANCE.fontRenderer().drawString(ForgeMod.VERSION, (int) startX + 57, (int) startY + 14,
				new Color(180, 180, 180, alpha).getRGB());
		Wrapper.INSTANCE.fontRenderer().drawString(
				"Hello, " + Minecraft.getMinecraft().getSession().getPlayerID() + " !",
				(int) (startX + 430 - 50
						- Wrapper.INSTANCE.fontRenderer().getStringWidth(
								"Hello, " + Minecraft.getMinecraft().getSession().getPlayerID() + " !")),
				(int) startY + 22, new Color(200, 200, 200, alpha).getRGB());

		int m = Mouse.getDWheel();
		if (this.isCategoryHovered(startX + 100, startY + 40, startX + 200, startY + 315, mouseX, mouseY)) {
			if (m < 0 && moduleStart < HackManager.getModulesInType(currentModuleType).size() - 1) {
				moduleStart++;
			}
			if (m > 0 && moduleStart > 0) {
				moduleStart--;
			}
		}
		if (this.isCategoryHovered(startX + 200, startY + 50, startX + 430, startY + 315, mouseX, mouseY)) {
			if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
				valueStart++;
			}
			if (m > 0 && valueStart > 0) {
				valueStart--;
			}
		}
		float mY = startY - 4.2F;
		for (int i = 0; i < HackManager.getModulesInType(currentModuleType).size(); i++) {
			Module module = HackManager.getModulesInType(currentModuleType).get(i);
			if (mY > startY + 250)
				break;
			if (i < moduleStart) {
				continue;
			}

			if (!module.isToggled()) {
				ClickGuiRender.drawRect(startX + 100, mY + 45, startX + 100, mY + 70,
						isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)
								? new Color(60, 60, 60, alpha).getRGB()
								: new Color(35, 35, 35, alpha).getRGB());
				ClickGuiRender.drawFilledCircle(startX
						+ (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 112
								: 110),
						mY + 58, 3, new Color(70, 70, 70, alpha).getRGB(), 5);
				Wrapper.INSTANCE.fontRenderer().drawString(
						module.getCName(), (int) startX + (int) (isSettingsButtonHovered(startX + 100, mY + 45,
								startX + 200, mY + 70, mouseX, mouseY) ? 122 : 120),
						(int) mY + 55, new Color(175, 175, 175, alpha).getRGB());
				// ////System.out.println(module.getCName());
			} else {
				ClickGuiRender.drawRect(startX + 100, mY + 45, startX + 190, mY + 70,
						isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)
								? new Color(60, 60, 60, alpha).getRGB()
								: new Color(35, 35, 35, alpha).getRGB());
				ClickGuiRender.drawFilledCircle(startX
						+ (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY) ? 112
								: 110),
						mY + 58, 3, new Color(100, 255, 100, alpha).getRGB(), 5);
				Wrapper.INSTANCE.fontRenderer().drawString(
						module.getCName(), (int) startX + (int) (isSettingsButtonHovered(startX + 100, mY + 45,
								startX + 200, mY + 70, mouseX, mouseY) ? 122 : 120),
						(int) mY + 55, new Color(255, 255, 255, alpha).getRGB());

			}

			if (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)) {
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
			if (isSettingsButtonHovered(startX + 100, mY + 45, startX + 200, mY + 70, mouseX, mouseY)
					&& Mouse.isButtonDown(1)) {

				for (Module mod : HackManager.getModulesInType(currentModuleType)) {
					// mod.clickanim = 115;
				}
				currentModule = module;
				valueStart = 0;
			}
			mY += 25;
		}
		mY = startY + 12;

		fontc.drawString(currentModule.getDescription(), (int) startX + 200, (int) mY + 36,
				new Color(170, 170, 170).getRGB());

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
						new Color(35, 35, 255, alpha).getRGB());
				ClickGuiRender.drawFilledCircle((float) ((double) x + render + 2D) + 3, mY + 52.25, 1.5,
						new Color(35, 35, 255, alpha).getRGB(), 5);
				Wrapper.INSTANCE.fontRenderer().drawString(value.getName(), (int) startX + 200, (int) mY + 50,
						new Color(175, 175, 175, alpha).getRGB());
				Wrapper.INSTANCE.fontRenderer().drawString(value.getValue().toString(),
						(int) startX + 300
								- (int) Wrapper.INSTANCE.fontRenderer().getStringWidth(value.getValue().toString()),
						(int) mY + 50, new Color(255, 255, 255, alpha).getRGB());
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
				Wrapper.INSTANCE.fontRenderer().drawString(value.getName(), (int) startX + 200, (int) mY + 50,
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
					ClickGuiRender.drawFilledCircle(x + x2x, mY + 54.5, 5, new Color(35, 35, 255, alpha).getRGB(), 10);
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
					ClickGuiRender.drawFilledCircle(x + xx, mY + 54.5, 5, new Color(56, 56, 56, alpha).getRGB(), 10);
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
				Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(value.getName(), startX + 200, mY + 52,
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
					Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(((ModeValue) value).getSelectMode().getName(),
							(float) (x + 40
									- Wrapper.INSTANCE.fontRenderer()
											.getStringWidth(((ModeValue) value).getSelectMode().getName()) / 2),
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
							/*
							 * if (mode.isToggled() == false) { t1 = mode.getName(); mode.setToggled(true);
							 * 
							 * break; }
							 */
						}
						if(press<=modes.size()) {
							////System.out.println(press);
						modes.get(press-1).setToggled(true);
						t1=modes.get(press-1).getName();
						}else {
							press=0;
						}
						
						  for (Mode mode : modeValue.getModes()) { if (mode.getName() != t1) {
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
		/*
		 * Wrapper.INSTANCE.fontRenderer().drawStringWithShadow("Bind", startX + 220,
		 * yyy + 50, new Color(170, 170, 170, alpha).getRGB());
		 * ClickGuiRender.drawRect(x + 5, yyy + 45, x + 75, yyy + 65, isHovered(x + 2,
		 * yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80,
		 * alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
		 * ClickGuiRender.drawRect(x + 2, yyy + 48, x + 78, yyy + 62, isHovered(x + 2,
		 * yyy + 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80,
		 * alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
		 * ClickGuiRender.drawFilledCircle(x + 5, yyy + 48, 3, isHovered(x + 2, yyy +
		 * 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB()
		 * : new Color(56, 56, 56, alpha).getRGB(), 5);
		 * ClickGuiRender.drawFilledCircle(x + 5, yyy + 62, 3, isHovered(x + 2, yyy +
		 * 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB()
		 * : new Color(56, 56, 56, alpha).getRGB(), 5);
		 * ClickGuiRender.drawFilledCircle(x + 75, yyy + 48, 3, isHovered(x + 2, yyy +
		 * 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB()
		 * : new Color(56, 56, 56, alpha).getRGB(), 5);
		 * ClickGuiRender.drawFilledCircle(x + 75, yyy + 62, 3, isHovered(x + 2, yyy +
		 * 45, x + 78, yyy + 65, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB()
		 * : new Color(56, 56, 56, alpha).getRGB(), 5);
		 * 
		 * 
		 * 
		 * Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(currentModule.getKey()==
		 * -1?"NONE":Keyboard.getKeyName(currentModule.getKey()), (float) (x + 40 -
		 * Wrapper.INSTANCE.fontRenderer() .getStringWidth(Keyboard.getKeyName((int)
		 * currentModule.getKey())) / 2), yyy + 53, new Color(255, 255, 255,
		 * alpha).getRGB());
		 */
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
//				startX = sr.getScaledWidth() / 2 - 450 / 2;
//				startY = sr.getScaledHeight() / 2 - 350 / 2;
			alpha = 0;
			alphe = 0;
		} else {
			alphe = 121;
		}
		// Gui.drawRect(sr.getScaledWidth() / 2 - 39, 0, sr.getScaledWidth() / 2 + 39,
		// 19, new Color(0,0,0,alpha / 2).getRGB());
		// Gui.drawRect(sr.getScaledWidth() / 2 - 40, 0, sr.getScaledWidth() / 2 + 40,
		// 20, new Color(0,0,0,alpha / 2).getRGB());
		// FontManager.INVETORY.getFont("SFR 8").drawString("Reset ClickUI",
		// sr.getScaledWidth() / 2 - 25, 6, new Color(255,255,255, alpha).getRGB());

		int j = 59;
		int l = 40;
		float k = startY + 10;
		float xx = startX + 5;
		float typey;
		for (int i = 0; i < HackCategory.values().length; i++) {
			HackCategory[] iterator = HackCategory.values();
			if (iterator[i] == currentModuleType) {
				typey = k + 5 + j + i * l;
				ClickGuiRender.drawRect(xx + 4, typey, xx + 25, typey + 2, color4);
			}

			Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(
					iterator[i].toString(), xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80,
							+k + 20 + j + i * l, mouseX, mouseY) ? 27 : 25),
					k + 50 + l * i, new Color(255, 255, 255, alpha).getRGB());
			if (iterator[i].toString() == "PLAYER") {
				Wrapper.INSTANCE.fontRenderer()
						.drawStringWithShadow("",
								xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l,
										mouseX, mouseY) ? 10 : 8),
								k + 50 + l * i, new Color(255, 255, 255, alpha).getRGB());
			} else if (iterator[i].toString() == "VISUAL") {
				Wrapper.INSTANCE.fontRenderer()
						.drawStringWithShadow("",
								xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l,
										mouseX, mouseY) ? 10 : 8),
								k + 50 + l * i, new Color(255, 255, 255, alpha).getRGB());
			} else if (iterator[i].toString() == "COMBAT") {
				Wrapper.INSTANCE.fontRenderer()
						.drawStringWithShadow("",
								xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l,
										mouseX, mouseY) ? 10 : 8),
								k + 50 + l * i, new Color(255, 255, 255, alpha).getRGB());
			} else if (iterator[i].toString() == "ANOTHER") {
				Wrapper.INSTANCE.fontRenderer()
						.drawStringWithShadow("",
								xx + (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l,
										mouseX, mouseY) ? 10 : 8),
								k + 50 + l * i, new Color(255, 255, 255, alpha).getRGB());

			}
			try {
				if (this.isCategoryHovered(xx + 8, k - 10 + j + i * l, xx + 80, +k + 20 + j + i * l, mouseX, mouseY)
						&& Mouse.isButtonDown((int) 0)) {
					currentModuleType = iterator[i];
					currentModule = HackManager.getModulesInType(currentModuleType).get(0);
					moduleStart = 0;
					valueStart = 0;
					for (int x1 = 0; x1 < currentModule.getValues().size(); x1++) {
						Value value = currentModule.getValues().get(x1);

					}
					for (Module mod : HackManager.getModulesInType(currentModuleType)) {
						// mod.clickanim = 115;
					}
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	public void initGui() {
		for (int i = 0; i < currentModule.getValues().size(); i++) {
			Value value = currentModule.getValues().get(i);

		}
		for (Module mod : HackManager.getModulesInType(currentModuleType)) {
			// mod.clickanim = 115;
		}

		super.initGui();
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
			// ModuleManager.getModuleByClass(ClickGui.class).setEnabled(false);
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
			return;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		float x = startX + 220;
		float mY = startY + 30;
		for (int i = 0; i < currentModule.getValues().size(); i++) {
			if (mY > startY + 350)
				break;
			if (i < valueStart) {
				continue;
			}
			Value value = currentModule.getValues().get(i);
			if (value instanceof NumberValue) {
				mY += 20;
			}
			if (value instanceof BooleanValue) {

				mY += 20;
			}
			if (value instanceof ModeValue) {

				mY += 25;
			}
		}
		float x1 = startX + 300;
		float yyy = startY + 200;
		if (isHovered(x1 + 2, yyy + 40, x1 + 78, yyy + 70, mouseX, mouseY)) {
			this.bind = true;
		}
		super.mouseClicked(mouseX, mouseY, button);
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
	public void onGuiClosed() {
		alpha = 0;
	}
}
