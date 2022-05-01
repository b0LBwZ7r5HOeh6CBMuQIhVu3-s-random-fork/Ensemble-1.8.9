package it.fktcod.ktykshrk.ui.clickgui;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.ClickGui;
import it.fktcod.ktykshrk.ui.clickgui.ast.RenderUtil;
import it.fktcod.ktykshrk.ui.font.CFontRenderer;
import it.fktcod.ktykshrk.ui.font.FontLoaders;
import it.fktcod.ktykshrk.utils.TranslateUtil;
import it.fktcod.ktykshrk.value.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiClickUI extends GuiScreen {
    private static java.util.List<Module> inSetting = new CopyOnWriteArrayList<>();
    private static HackCategory currentCategory;
    private static int x, y, wheel;
    private boolean need2move;
    private int dragX, dragY;
    private TranslateUtil translate = new TranslateUtil(0, 0);

    public GuiClickUI() {
        need2move = false;
        dragX = 0;
        dragY = 0;
        translate.setX(0);
        translate.setY(0);
    }

    @Override
    public void initGui() {
        super.initGui();
        if (x > width)
            x = 30;
        if (y > height)
            y = 30;
        need2move = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // 字体获取
    CFontRenderer font1 = FontLoaders.default18;
    CFontRenderer font2 = FontLoaders.default16;
    CFontRenderer font3 = FontLoaders.default14;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean showSetting;
        int valueSizeY;
        float valueY;
        if (need2move) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
        if (!Mouse.isButtonDown(0) && need2move)
            need2move = false;

        String X = "E";
        String F = "nsemble";
        RenderUtil.drawBorderedRect(x, y, x + 273, y + 198, 3, new Color(20, 20, 20).getRGB(), getColor());
        RenderUtil.drawBorderedRect(x + 2, y + 2, x + 273 - 2, y + 198 - 2, 1, getColor(),
                new Color(20, 20, 20).getRGB());
        Gui.drawRect(x + 70, y + 35, x + 269, y + 195, new Color(0, 0, 0).getRGB());
        FontLoaders.default30.drawStringWithShadow(X+F, x + 10, y + 8, new Color(180, 180, 180).getRGB());
        font2.drawStringWithShadow(Core.VERSION, x + 12, y + 24, new Color(180, 180, 180).getRGB());

        RenderUtil.drawGradientSideways(x + 70, y + 35, x + 80, y + 195, new Color(20, 20, 20).getRGB(),
                new Color(0, 0, 0, 0).getRGB());

        int cateY = 0;
        for (HackCategory category : HackCategory.values()) {
            int strX = x + 40;
            int strY = y + 55 + cateY;
            boolean hover = mouseX > x + 5 && mouseX < x + 65 && mouseY > strY && mouseY < strY + 20;
            FontLoaders.default20.drawCenteredStringWithShadow(
                    category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase(),
                    strX - 1, strY + 6, (category == currentCategory) ? getColor()
                            : new Color(hover ? 255 : 140, hover ? 255 : 140, hover ? 255 : 140).getRGB());
            cateY += 20;
        }
        int startX = (x + 80) + 2;
        int startY = (y + 9) + 2 + 28;
        int length = 185;
        float moduleY = translate.getY();
        RenderUtil.startGlScissor(startX, startY + 14, length, 140);
        for (Module m : Core.hackManager.getHacks()) {
            if (m.getCategory() != currentCategory)
                continue;
            RenderUtil.drawRoundRect(startX, startY + moduleY, startX + length, startY + moduleY + 24, 3,
                    new Color(20, 20, 20).getRGB());
            font1.drawStringWithShadow(m.getName(), startX + 8, startY + 9 + moduleY, -1);
            RenderUtil.drawRoundRect(startX + length - 25, startY + moduleY + 7, startX + length - 5,
                    startY + moduleY + 17, 5, new Color(0, 0, 0).getRGB());
            boolean onToggleButton = mouseX > startX + length - 25 && mouseX < startX + length - 5
                    && mouseY > startY + moduleY + 7 && mouseY < startY + moduleY + 17;
            int left = m.isToggled() ? startX + length - 14 : startX + length - 24;
            int right = m.isToggled() ? startX + length - 6 : startX + length - 16;
            RenderUtil.drawRoundRect(left, startY + moduleY + 8, right, startY + moduleY + 16, 4, getColor());
            RenderUtil.drawRoundRect(startX + length - 24, startY + moduleY + 8, startX + length - 16,
                    startY + moduleY + 16, 4, new Color(0, 0, 0, 150).getRGB());
            if (onToggleButton) {
                RenderUtil.drawRoundRect(startX + length - 25, startY + moduleY + 7, startX + length - 5,
                        startY + moduleY + 17, 5, new Color(0, 0, 0, 100).getRGB());
            }
            showSetting = inSetting.contains(m);
            valueSizeY = (m.getValues().size() * 20) + 5;
            valueY = moduleY + 35;
            if (showSetting) {
                RenderUtil.drawRect(startX + 3, startY + moduleY + 24, startX + length - 3, startY + moduleY + 30,
                        new Color(30, 30, 30).getRGB());
                RenderUtil.drawRoundRect(startX + 3, startY + moduleY + 24, startX + length - 3,
                        startY + moduleY + 24 + valueSizeY, 3, new Color(30, 30, 30).getRGB());
                for (Value<?> setting : m.getValues()) {
                    if (setting instanceof ModeValue) {
                        ModeValue s = (ModeValue) setting;
                        font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 1, -1);
                        RenderUtil.drawRoundRect(startX + length - 85, startY + valueY - 4, startX + length - 6,
                                startY + valueY + 8, 3, new Color(10, 10, 10).getRGB());
                        int longValue = ((startX + length - 6) - (startX + length - 85)) / 2;
                        if (s.getSelectMode() != null && s.getSelectMode().getName() != null) {
                            font2.drawCenteredStringWithShadow(s.getSelectMode().getName(),
                                    (startX + length - 6) - longValue, startY + valueY - 0.5F, getColor());
                        }
                        boolean hover = mouseX > startX + length - 85 && mouseX < startX + length - 6
                                && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 8;
                        if (hover) {
                            RenderUtil.drawRoundRect(startX + length - 85, startY + valueY - 4, startX + length - 6,
                                    startY + valueY + 8, 3, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    if (setting instanceof NumberValue) {
                        NumberValue s = (NumberValue) setting;
                        font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 3, -1);
                        double max = s.getMax().doubleValue();
                        double min = s.getMin().doubleValue();
                        double valn = s.getValue().doubleValue();
                        int longValue = ((startX + length - 6) - (startX + length - 83));
                        font3.drawStringWithShadow(s.getValue().doubleValue() + "", startX + length - 84,
                                startY + valueY - 2, -1);
                        RenderUtil.drawRoundRect(startX + length - 85, startY + valueY + 5, startX + length - 6,
                                startY + valueY + 7, 1, new Color(10, 10, 10).getRGB());
                        RenderUtil.drawRoundRect(startX + length - 85, startY + valueY + 5,
                                (startX + length - 85) + (longValue * (valn - min) / (max - min)) + 2,
                                startY + valueY + 7, 1, getColor());
                        boolean hover = mouseX > startX + length - 88 && mouseX < startX + length - 3
                                && mouseY > startY + valueY + 2 && mouseY < startY + valueY + 11;
                        if (hover) {
                            RenderUtil.drawRoundRect(startX + length - 85, startY + valueY + 5, startX + length - 6,
                                    startY + valueY + 7, 1, new Color(0, 0, 0, 100).getRGB());
                            if (Mouse.isButtonDown(0)) {
                                double inc = 0.01;
                                double valAbs = mouseX - (startX + length - 85);
                                double perc = valAbs / ((longValue) * Math.max(Math.min(valn / max, 0), 1));
                                perc = Math.min(Math.max(0, perc), 1);
                                double valRel = (max - min) * perc;
                                double val = min + valRel;
                                val = Math.round(val * (1 / inc)) / (1 / inc);
                                s.setValue(val);
                            }
                        }
                    }
                    if (setting instanceof BooleanValue) {
                        BooleanValue s = (BooleanValue) setting;
                        font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 3, -1);
                        boolean hover = mouseX > startX + length - 18 && mouseX < startX + length - 6
                                && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 8;
                        RenderUtil.drawRoundRect(startX + length - 18, startY + valueY - 4, startX + length - 6,
                                startY + valueY + 8, 2, new Color(10, 10, 10).getRGB());
                        if (s.getValue()) {
                            RenderUtil.drawRoundRect(startX + length - 17, startY + valueY - 3, startX + length - 7,
                                    startY + valueY + 7, 2, getColor());
                        }
                        if (hover) {
                            RenderUtil.drawRoundRect(startX + length - 18, startY + valueY - 4, startX + length - 6,
                                    startY + valueY + 8, 2, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    valueY += 20;
                }
//				RenderUtil.drawGradientSidewaysV(startX + 3, startY + moduleY + 24, startX + length - 3,
//						startY + moduleY + 34, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0).getRGB());
            }
            moduleY += (showSetting ? (26 + valueSizeY) : 26);
        }
        RenderUtil.stopGlScissor();
        RenderUtil.drawGradientSidewaysV(x + 3, y + 35, x + 273 - 3, y + 45, new Color(0, 0, 0, 0).getRGB(),
                new Color(0, 0, 0).getRGB());
        int real = Mouse.getDWheel();
        float moduleHeight = moduleY - translate.getY();
        if (Mouse.hasWheel() && mouseX > startX && mouseY > startY && mouseX < startX + 270 && mouseY < startY + 237) {
            if (real > 0 && wheel < 0) {
                for (int i = 0; i < 5; i++) {
                    if (!(wheel < 0))
                        break;
                    wheel += 5;
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (!(real < 0 && moduleHeight > 158 && Math.abs(wheel) < (moduleHeight - (154))))
                        break;
                    wheel -= 5;
                }
            }
        }
        translate.interpolate(0, wheel, 0.15F);

//		GlStateManager.pushMatrix();
//		GlStateManager.enableBlend();
//		GL11.glEnable(GL11.GL_LINE_SMOOTH);
//		float[] colors = ColorUtil.OneToFourTranslator(HUD.getColor());
//		mc.getTextureManager().bindTexture(new ResourceLocation("rainy/arrow.png"));
//		GlStateManager.color(colors[0], colors[1], colors[2]);
//		int size = 16;
//		Gui.drawModalRectWithCustomSizedTexture(mouseX, mouseY, 0, 0, size, size, size, size);
//		GL11.glDisable(GL11.GL_LINE_SMOOTH);
//		GlStateManager.disableBlend();
//		GlStateManager.popMatrix();
    }

    int press = 0;

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hover2top = mouseX > x && mouseX < x + 273 && mouseY > y && mouseY < y + 35;
        if (hover2top && mouseButton == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            need2move = true;
        } else {
            int cateY = 0;
            for (HackCategory category : HackCategory.values()) {
                int strX = x + 40;
                int strY = y + 55 + cateY;
                boolean hover = mouseX > x + 5 && mouseX < x + 65 && mouseY > strY && mouseY < strY + 20;
                if (hover && mouseButton == 0) {
                    currentCategory = category;
                    wheel = 0;
                    translate.setY(0);
                    break;
                }
                cateY += 20;
            }
            int startX = (x + 80) + 2;
            int startY = (y + 9) + 2 + 25;
            int length = 185;
            float moduleY = translate.getY();
            for (Module m : Core.hackManager.getHacks()) {
                if (m.getCategory() != currentCategory)
                    continue;
                boolean onToggleButton = mouseX > startX + length - 25 && mouseX < startX + length - 5
                        && mouseY > startY + moduleY + 7 && mouseY < startY + moduleY + 20 && mouseY < startY + 14 + 140
                        && mouseY > startY;
                boolean onModuleRect = mouseX > startX && mouseX < startX + length && mouseY > startY + moduleY
                        && mouseY < startY + moduleY + 28 && mouseY < startY + 14 + 140 && mouseY > startY;
                if (onToggleButton && mouseButton == 0)


                m.setToggled(!m.isToggled());

                if (onModuleRect && mouseButton == 1) {
                    if (inSetting.contains(m))
                        inSetting.remove(m);
                    else if (!m.getValues().isEmpty())
                        inSetting.add(m);
                }
                boolean showSetting = inSetting.contains(m);
                int valueSizeY = (m.getValues().size() * 20) + 5;
                float valueY = moduleY + 35;
                if (showSetting) {
                    RenderUtil.drawRect(startX + 3, startY + moduleY + 24, startX + length - 3,
                            startY + moduleY + 24 + valueSizeY, new Color(30, 30, 30).getRGB());
                    for (Value<?> setting : m.getValues()) {
                        if (setting instanceof ModeValue) {
                            ModeValue s = (ModeValue) setting;
                            boolean hover = mouseX > startX + length - 85 && mouseX < startX + length - 6
                                    && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 11
                                    && mouseY < startY + 14 + 140 && mouseY > startY;
                            if (hover) {

                                if (mouseButton == 1) {
                                    press++;
                                    final ModeValue modeValue = (ModeValue) s;
                                    ArrayList<Mode> modes = new ArrayList<Mode>();

                                    String t1 = null;
                                    for (Mode mode : modeValue.getModes()) {
                                        modes.add(mode);

                                    }
                                    if (press <= modes.size()) {
                                        // //System.out.println(press);
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
                                }
                                if (mouseButton == 0) {
                                    press++;
                                    final ModeValue modeValue = (ModeValue) s;
                                    ArrayList<Mode> modes = new ArrayList<Mode>();

                                    String t1 = null;
                                    for (Mode mode : modeValue.getModes()) {
                                        modes.add(mode);

                                    }
                                    if (press <= modes.size()) {
                                        // //System.out.println(press);
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
                                }
                            }
                        }
                        if (setting instanceof BooleanValue) {
                            BooleanValue s = (BooleanValue) setting;
                            boolean hover = mouseX > startX + length - 18 && mouseX < startX + length - 6
                                    && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 11
                                    && mouseY < startY + 14 + 140 && mouseY > startY;
                            if (hover && (mouseButton == 0 || mouseButton == 2)) {
                                s.setValue(!s.getValue());
                            }
                        }
                        valueY += 20;
                    }
                }
                moduleY += (showSetting ? (26 + valueSizeY) : 26);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        int startY = (y + 9) + 2 + 28;
        boolean hover2top = mouseX > x + 1 && mouseX < x + 349 && mouseY > y + 1 && mouseY < y + 9
                && mouseY < startY + 14 + 140 && mouseY > startY;
        if (hover2top && state == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            need2move = false;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ClickGui.memoriseX = x;
        ClickGui.memoriseY = y;
        ClickGui.memoriseWheel = wheel;
        ClickGui.memoriseML = inSetting;
        ClickGui.memoriseCatecory = currentCategory;

        try {
            Mouse.setNativeCursor(null);
        } catch (Throwable ignore) {
        }
    }

    org.lwjgl.input.Cursor emptyCursor;

    public static void setInSetting(List<Module> moduleList) {
        inSetting = moduleList;
    }

    public static void setWheel(int state) {
        wheel = state;
    }

    public static void setX(int state) {
        x = state;
    }

    public static void setY(int state) {
        y = state;
    }

    public static void setCategory(HackCategory state) {
        currentCategory = state;
    }

    public static int getColor() {
        return new Color(ClickGui.red.getValue().intValue(), ClickGui.green.getValue().intValue(),
                ClickGui.blue.getValue().intValue(), 255).getRGB();
    }
}
