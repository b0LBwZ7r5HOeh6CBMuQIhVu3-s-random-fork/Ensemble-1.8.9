package it.fktcod.ktykshrk.module.mods;

import java.awt.Color;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import it.fktcod.ktykshrk.ui.clickgui.huangbai.ClickGuiRender;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.AnimationUtils;
import it.fktcod.ktykshrk.utils.visual.ColorUtils;
import it.fktcod.ktykshrk.utils.visual.Colors;
import it.fktcod.ktykshrk.utils.visual.Opacity;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.utils.visual.font.render.TTFFontRenderer;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class HUD extends Module {

    public BooleanValue effects;
    public BooleanValue keyboard;
    public static BooleanValue font;
    public static String HUDString = "Ensemble";
    public NumberValue speed;
    public BooleanValue notification;
    ModeValue mode;
    public BooleanValue armor;
    float hue2;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public AnimationUtils animationUtils = new AnimationUtils();

    private Opacity hue = new Opacity(0);

    public ColorUtils c = new ColorUtils();

    public static TTFFontRenderer sigmaFont = Core.fontManager.getFont("SFB 8");
    public static TTFFontRenderer jigsawFont = Core.fontManager.getFont("JIGR 19");
    public static TTFFontRenderer rfont = Core.fontManager.getFont("POP 16");
    public static FontRenderer vanillaFont;

    private static ArrayList<Long> times = new ArrayList<Long>();

    TimerUtils tpstimer = new TimerUtils();
    public static double lastTps = 20.0;

    int posX = 3;
    int posY = 2;
    int posX2 = 10 + 7 + 3 + 2;

    float am = 0;

    int rainbowTick = 0;
    // public static FontManager font = new FontManager();

    public static NumberValue MusicPlayerX;
    public static NumberValue MusicPlayerY;
    public static BooleanValue MusicPlayer;

    //inventoryhud
    BooleanValue inventory;
    NumberValue invx;
    NumberValue invy;

    public HUD() {
        super("HUD", HackCategory.VISUAL);
        this.setToggled(true);
        this.setShow(false);

        effects = new BooleanValue("Effects", false);
        keyboard = new BooleanValue("Keyboard", false);
        font = new BooleanValue("Font", true);
        mode = new ModeValue("Mode", new Mode("Rainbow", false), new Mode("RSimple", true));
        speed = new NumberValue("Speed", 3D, 1D, 15D);
        notification = new BooleanValue("Notification", true);
        MusicPlayer = new BooleanValue("MusicPlayer", true);
        MusicPlayerY = new NumberValue("MusicPlayerY", 5d, 0d, 200d);
        MusicPlayerX = new NumberValue("MusicPlayerX", 70d, 0d, 400d);
        armor = new BooleanValue("Armor", true);
        inventory = new BooleanValue("Inventory", true);
        invx = new NumberValue("InvX", 10D, 0D, 100D);
        invy = new NumberValue("InvY", 20D, 0D, 100D);
        this.addValue(effects, keyboard, font, mode, notification, armor, MusicPlayer, MusicPlayerY, MusicPlayerX, inventory, invx, invy);
        this.setChinese(Core.Translate_CN[51]);

    }

    public void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        enableGL2D();
        GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
        drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        RenderUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
        disableGL2D();
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        RenderUtils.drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderUtils.drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
    }

    public static void enableGL2D() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
    }

    public static void disableGL2D() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }

    @Override
    public String getDescription() {
        return "Heads-Up Display.";
    }

    @Override
    public void onRenderGameOverlay(Text event) {

        if (inventory.getValue()) {
            drawInventory();
        }

        float h_ = this.hue2;
        float h2 = this.hue2 + 85.0f;
        float h3 = this.hue2 + 170.0f;

        Color color33 = Color.getHSBColor((float) (h_ / 255.0f), (float) 0.9f, (float) 1.0f);
        Color color332 = Color.getHSBColor((float) (h2 / 255.0f), (float) 0.9f, (float) 1.0f);
        Color color333 = Color.getHSBColor((float) (h3 / 255.0f), (float) 0.9f, (float) 1.0f);
        int color1 = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();

        ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        if (armor.getValue()) {
            drewArmor(sr);

        }

        if (notification.getValue()) {
            Core.notificationManager.draw();
        }

        vanillaFont = Wrapper.INSTANCE.fontRenderer();
        // font=new FontManager();
        // font=new FontManager();
        int rainbowTickc = 0;

        // boolean isChatOpen = Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat
        // || Wrapper.INSTANCE.mc().currentScreen instanceof
        // i.gishreloaded.gishcode.gui.GuiConsole;

        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("KK:mm a", Locale.ENGLISH);
        String result = sdformat.format(date);
        String server = Wrapper.INSTANCE.mc().isSingleplayer() ? "local_server"
                : Wrapper.INSTANCE.mc().getCurrentServerData().serverIP.toLowerCase();//

        String text = null;
        String text2 = null;

        Field field = ReflectionHelper.findField(Minecraft.class, new String[]{"debugFPS", "field_71470_ab"});
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            text2 = field.getInt(Wrapper.INSTANCE.mc()) + " fps ";

            text = "\247f" + text2 + "\247f  " + "\247f | " + server + "\247f | " + result;
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }

        float width = Core.fontManager.getFont("SFB 8").getWidth(text);
        int height = 12;

        /*
         * if(mode.getMode("Rainbow").isToggled()) {
         *
         * Utils.rect(6f, 8f, 54, 20, Colors.getColor(50, 50, 50, 120));
         * Core.fontManager.getFont("SFB 11").drawString(" ", 6, 10,
         * Colors.getColor(200, 150, 180, 200));
         * Core.fontManager.getFont("SFB 11").drawString("E", 6, 10,
         * Colors.getColor(255, 0, 20, 200));
         * Core.fontManager.getFont("SFB 9").drawString("", 6 +
         * sigmaFont.getWidth("E"), 11, Colors.getColor(255, 255, 255, 200));
         * Core.fontManager.getFont("SFB 6").drawString( "ping:" +
         * (Wrapper.INSTANCE.mc().isSingleplayer() ? "0ms" :
         * Wrapper.INSTANCE.mc().getCurrentServerData().pingToServer + "ms"), 6, 22,
         * Colors.getColor(255, 255, 255, 200));
         *
         * }
         */
        sigmaFont.drawStringWithShadow(" ", 180, 180, color1);
        if (mode.getMode("Rainbow").isToggled()) {
            int index = 4;

            ArrayList<Module> modules = new ArrayList();
            modules.addAll(HackManager.modules);
            double x = Wrapper.INSTANCE.player().posX;
            double y = Wrapper.INSTANCE.player().posY;
            double z = Wrapper.INSTANCE.player().posZ;

            boolean isChatOpen = Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat;
            int heightCoords = isChatOpen ? sr.getScaledHeight() - 20 : sr.getScaledHeight() - 10;
            int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
            int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);
            int heightFPS = isChatOpen ? sr.getScaledHeight() - 37 : sr.getScaledHeight() - 22;

            String coords = String.format("\u00a77TPS: \u00a7f%s",
                    RenderUtils.DF(((float) (Math.round(lastTps * 100.0) / 100.0)), 1));

            // sigmaFont.drawString(coords, 4, heightCoords, Color.white.getRGB());

            for (Module h : HackManager.getSortedHacks2()) {
                String modeName = "";
                if (!h.isToggled())
                    continue;
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                for (Value value : h.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (modeValue.getModeName().equals("Mode")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    modeName = modeName + " \u00a77" + mode.getName();

                                }
                            }
                        }
                    }
                }
                int yPos = 18;
                int xPos = 4;
                xPos = 6;

                Color rainbow = new Color(
                        Color.HSBtoRGB((float) ((double) Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0
                                - Math.sin((double) rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f));
                if (font.getValue()) {
                    // GL11.glPushMatrix();
                    /*
                     * Utils.rect(sr.getScaledWidth() - sigmaFont.getWidth(h.getName() + modeName),
                     * index, sr.getScaledWidth(), index + sigmaFont.getHeight(h.getName() +
                     * modeName), rainbow.getRGB());
                     */
                    // GL11.glPopMatrix();
                    Utils.rect(sr.getScaledWidth() - sigmaFont.getWidth(h.getName() + modeName) - 2, index - 2,
                            sr.getScaledWidth(), index + sigmaFont.getHeight(h.getName()),
                            Colors.getColor(245, 192, 203, 150));
                    sigmaFont.drawStringWithShadow(h.getName() + modeName,
                            sr.getScaledWidth() - sigmaFont.getWidth(h.getName() + modeName), index, rainbow.getRGB());

                    index += sigmaFont.getHeight(h.getName()) + 2;

                } else {
                    Utils.rect(sr.getScaledWidth() - vanillaFont.getStringWidth(h.getName() + modeName) - 2, index,
                            sr.getScaledWidth(), index + vanillaFont.FONT_HEIGHT, Colors.getColor(180, 180, 180, 100));

                    vanillaFont.drawStringWithShadow(h.getName() + modeName,
                            sr.getScaledWidth() - vanillaFont.getStringWidth(h.getName() + modeName), index,
                            rainbow.getRGB());

                    index += 9;
                }

                if (keyboard.getValue()) {

                    RenderUtils.drawKeyStrokes();
                }

                if (effects.getValue()) {

                }

                ClickGuiRender.drawGradientSideways((double) (4), (double) (2),
                        (double) (4 + Core.fontLoaders.icon25.getStringWidth(HUDString) / 2),
                        (double) ((double) 2 + 2), (int) color1, (int) color2);
                ClickGuiRender.drawGradientSideways(
                        (double) (4 + Core.fontLoaders.icon25.getStringWidth(HUDString) / 2), (double) (2),
                        (double) (5 + Core.fontLoaders.icon25.getStringWidth(HUDString)), (double) ((double) 2 + 2),
                        (int) color2, (int) color3);
                Utils.rect(4, 4, 5 + Core.fontLoaders.icon25.getStringWidth(HUDString),
                        4 + Core.fontLoaders.icon25.getHeight(), Colors.getColor(0, 0, 0, 140));
                Core.fontLoaders.icon25.drawStringWithShadow(" ", 3, 5, rainbow.getRGB());
                Core.fontLoaders.icon25.drawStringWithShadow(HUDString, 4, 5, Colors.getColor(Color.white));

            }
        } else if (mode.getMode("RSimple").isToggled()) {

            Color rainbow = new Color(
                    Color.HSBtoRGB((float) ((double) Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0
                            - Math.sin((double) rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f));

            // logo
            this.hue2 = (float) ((double) this.hue2 + 0.5);

            ClickGuiRender.drawGradientSideways((double) (4), (double) (3),
                    (double) (4 + Core.fontLoaders.icon25.getStringWidth(HUDString) / 2), (double) ((double) 2 + 2),
                    (int) color1, (int) color2);
            ClickGuiRender.drawGradientSideways((double) (4 + Core.fontLoaders.icon25.getStringWidth(HUDString) / 2),
                    (double) (3), (double) (5 + Core.fontLoaders.icon25.getStringWidth(HUDString)),
                    (double) ((double) 2 + 2), (int) color2, (int) color3);
            Utils.rect(4, 4, 5 + Core.fontLoaders.icon25.getStringWidth(HUDString),
                    4 + Core.fontLoaders.icon25.getHeight(), Colors.getColor(0, 0, 0, 140));
            Core.fontLoaders.icon25.drawStringWithShadow(" ", 3, 5, rainbow.getRGB());
            Core.fontLoaders.icon25.drawStringWithShadow(HUDString, 4, 5, Colors.getColor(Color.white));

            if (keyboard.getValue()) {

                RenderUtils.drawKeyStrokes();
            }

            int index = 4;

            for (Module h : HackManager.getSortedHacks3()) {
                String modeName = "";
                if (!h.isToggled())
                    continue;
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                for (Value value : h.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (modeValue.getModeName().equals("Mode")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    modeName = modeName + " \u00a77" + mode.getName();

                                }
                            }
                        }
                    }
                }
                int yPos = 18;
                int xPos = 4;
                xPos = 6;

                /*
                 * if(h.doAnimate1) { am=animationUtils.animate(sr.getScaledWidth() -
                 * rfont.getWidth(h.getName() + modeName), am, 0.1f);
                 *
                 * rfont.drawStringWithShadow(h.getName() + modeName, am, index,
                 * rainbow.getRGB());
                 *
                 * if(tpstimer.hasReached(1500)) { h.doAnimate1=false; tpstimer.reset(); } }
                 */
                Color rainbow2 = new Color(
                        Color.HSBtoRGB((float) ((double) Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0
                                - Math.sin((double) rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f));

                rfont.drawStringWithShadow(h.getName() + modeName,
                        sr.getScaledWidth() - rfont.getWidth(h.getName() + modeName), index, rainbow2.getRGB());

                index += rfont.getHeight(h.getName()) + 2;

                // sigmaFont.drawStringWithShadow("KillAura",(sr.getScaledWidth()-sigmaFont.getWidth("KillAura"))/2,
                // sr.getScaledHeight()/2+5,
                // HackManager.getHack("KillAura").isToggled()?Colors.getColor(Color.green):Colors.getColor(Color.red));

            }

        }

        /*
         * Hack toggleHack = HackManager.getToggleHack(); if (toggleHack != null) {
         *
         * RenderUtils.drawSplash( toggleHack.isToggled() ? toggleHack.getName() +
         * "\u00a7a" + " - Enabled" : toggleHack.getName() + "\u00a7c" + " - Disabled",
         * toggleHack.isToggled() ? Colors.getColor(Color.GREEN) :
         * Colors.getColor(Color.RED));
         *
         * // Client.notificationManager.drawNotifications(); }
         */
        super.onRenderGameOverlay(event);

    }

    void drawArraylist(int rainbowTickc) {
        int y = 4;

        List<Module> modules = new CopyOnWriteArrayList<>();
        float h = hue.getOpacity();

        for (Module module : HackManager.getHacks()) {

            if (module.isToggled()) {
                if (module.getEnableTime() < 700) {
                    module.setEnableTime(module.getEnableTime() + 25);
                }
            } else {
                if (module.getEnableTime() > 0) {
                    module.setEnableTime(module.getEnableTime() - 30);
                } else if (module.getEnableTime() < 0) {
                    module.setEnableTime(0);
                }
            }

            if (module.getEnableTime() > 0 && module.isShow()) {
                modules.add(module);
            }

        }
        /*
         * if(font.getValue()) { modules.sort(Comparator.comparingDouble( m ->
         * -sigmaFont.getWidth(m.g != null && (Boolean)settings.get(SUFFIX).getValue() ?
         * m.getName() + " " + m.getSuffix() : m.getName())));
         *
         *
         * }else { modules.sort(Comparator.comparingDouble( m ->
         * -vanillaFont.getStringWidth(m. != null &&
         * (Boolean)settings.get(SUFFIX).getValue() ? m.getName() + " " + m.getSuffix()
         * : m.getName()))); }
         */
        hue.interp(256, speed.getValue().doubleValue() - 1);

        if (hue.getOpacity() > 255) {
            hue.setOpacity(0);
        }
        ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        float lastWidth = sr.getScaledWidth();

        float xres = sr.getScaledWidth();
        float yres = sr.getScaledHeight();
        int s = sr.getScaleFactor();

        for (Module module : modules) {
            if (h > 255) {
                h = 0;
            }

            float x = xres - (float) module.getEnableTime() / 100
                    * sigmaFont.getWidth(module.getName() + " " + mode.getName()) / 7;
            float maxXValue = xres - sigmaFont.getWidth(module.getName() + " " + mode.getName());

            if (!font.getValue()) {
                x = xres - (float) module.getEnableTime() / 100
                        * (float) vanillaFont.getStringWidth(module.getName() + " " + mode.getName()) / 7;
                maxXValue = mode.getName() == ""
                        ? xres - (float) vanillaFont.getStringWidth(module.getName() + " " + mode.getName()) - 1
                        : xres - (float) vanillaFont.getStringWidth(module.getName() + " " + mode.getName()) + 1;
            }

            if (x < maxXValue) {
                x = maxXValue;
            }
            // animate
            GL11.glPushMatrix();
            GL11.glScissor(0, (int) (yres - (y + module.getTranslate().getY())) * s + 3, (int) xres * s,
                    (int) (module.getTranslate().getY() + 4) * s);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            if (font.getValue()) {
                Utils.rect(x - 1, y - 4.3, xres, y + 5.7, Colors.getColor(0, 160));
            } else {

                Utils.rect(x - 2, y - 4.3, xres, y + 6.7, Colors.getColor(0, 160));
            }

            // int color = 0;
            int color = new Color(
                    Color.HSBtoRGB((float) ((double) Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0
                            - Math.sin((double) rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f)).getRGB();

            if (font.getValue()) {
                Utils.rect(x - 2, y - 4.3, x - 1, y + 6.7, color);
                if (module != modules.get(0)) {
                    Utils.rect(x - 2, y - 3.3, lastWidth - 1, y - 4.3, color);
                }

                if (module == modules.get(modules.size() - 1)) {
                    Utils.rect(x - 1, y + 5.5, xres, y + 6.7, color);
                }

                sigmaFont.drawStringWithShadow(module.getName(), x, y - 1, color);

            } else {
                if (x <= lastWidth && modules.get(0) != module) {
                    Utils.rect(x - 3, y - 3.3, x - 2, y + 7.7, color);
                } else {
                    Utils.rect(x - 3, y - 4.3, x - 2, y + 7.7, color);
                }

                if (module != modules.get(0) && x != lastWidth) {
                    Utils.rect(lastWidth - 2, y - 3.3, x - 3, y - 4.3, color);
                }

                if (module == modules.get(modules.size() - 1)) {
                    Utils.rect(x - 2, y + 6.5, xres, y + 7.7, color);
                }

                vanillaFont.drawStringWithShadow(module.getName(), x - 0.5f, y - 3, color);

            }

            lastWidth = x;
            if (!Objects.equals(mode.getName(), "")) {
                if (font.getValue()) {

                    sigmaFont.drawStringWithShadow(mode.getName(), x + sigmaFont.getWidth(module.getName()) - 2, y - 1,
                            Colors.getColor(Colors.getColor(150)));
                } else {
                    vanillaFont.drawStringWithShadow(mode.getName(),
                            x + vanillaFont.getStringWidth(module.getName()) - 2, y - 3,
                            Colors.getColor(Colors.getColor(150)));
                }

            }
            // width
            h += 20 - 0;

            y += module.getTranslate().getY();
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();

        }
    }

    public static Color rainbow1(long time, float count, float fade) {
        long color = Long.parseLong(
                Integer.toHexString(Color.HSBtoRGB((time + count * -1973000f) / 2 / 1.0E9f, 0.6f, 0.9f)), 16);
        Color c = new Color((int) color);
        return new Color((float) c.getRed() / 255.0F * fade, (float) c.getGreen() / 255.0F * fade,
                (float) c.getBlue() / 255.0F * fade, (float) c.getAlpha() / 255.0F);
    }

    @Override
    public boolean onPacket(Object packet, Side side) {
        if (side == Side.IN) {
            if (packet instanceof S03PacketTimeUpdate) {
                times.add(Math.max(0, tpstimer.getTime()));
                long timesAdded = 0;

                if (times.size() > 5) {
                    times.remove(0);
                }
                for (long l : times) {
                    timesAdded += l;
                }
                long roundedTps = timesAdded / times.size();
                lastTps = (20.0 / roundedTps) * 1000.0;
                lastTps = Math.min(lastTps, 20.0);
                tpstimer.reset();
            }

        }
        return super.onPacket(packet, side);
    }

    void drewArmor(ScaledResolution sr) {
        final boolean currentItem = true;
        GL11.glPushMatrix();
        final List<ItemStack> stuff = new ArrayList<ItemStack>();
        final boolean onwater = mc.thePlayer.isEntityAlive() && mc.thePlayer.isInsideOfMaterial(Material.water);
        int split = -3;

        for (int index = 3; index >= 0; --index) {
            final ItemStack armer = mc.thePlayer.inventory.armorInventory[index];

            if (armer != null) {
                stuff.add(armer);
            }
        }

        if (mc.thePlayer.getCurrentEquippedItem() != null && currentItem) {
            stuff.add(mc.thePlayer.getCurrentEquippedItem());
        }

        for (final ItemStack errything : stuff) {
            if (mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }

            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            mc.getRenderItem().zLevel = -150.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(errything, split + sr.getScaledWidth() / 2 - 4,
                    sr.getScaledHeight() - (onwater ? 65 : 55));
            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, errything, split + sr.getScaledWidth() / 2 - 4,
                    sr.getScaledHeight() - (onwater ? 65 : 55));
            mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }

        GL11.glPopMatrix();
    }

    public void drawInventory() {

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        int x = invx.getValue().intValue();
        int y = invy.getValue().intValue();
        Gui.drawRect(x, y, x + 167, y + 73, new Color(29, 29, 29, 255).getRGB());
        Gui.drawRect(x + 1, y + 13, x + 166, y + 72, new Color(40, 40, 40, 255).getRGB());
        Core.fontLoaders.default16.drawString("Your Inventory", x + 3, y + 3, 0xffffffff, true);
        boolean hasStacks = false;
        for (int i1 = 9; i1 < Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.size() - 9; ++i1) {
            Slot slot = Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.get(i1);
            if (slot.getHasStack()) hasStacks = true;
            int i = slot.xDisplayPosition;
            int j = slot.yDisplayPosition;
            mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), x + i - 4, y + j - 68);
            mc.getRenderItem().renderItemOverlayIntoGUI(Wrapper.INSTANCE.fontRenderer(), slot.getStack(), x + i - 4, y + j - 68, null);
        }
        if (mc.currentScreen instanceof GuiInventory) {
            Core.fontLoaders.default16.drawString("Already in inventory",
                    x + 167 / 2 - Core.fontLoaders.default16.getStringWidth("Already in inventory") / 2,
                    y + 72 / 2,
                    0xffffffff,
                    true);
        } else if (!hasStacks) {
            Core.fontLoaders.default16.drawString("Empty...",
                    x + 167 / 2 - Core.fontLoaders.default16.getStringWidth("Empty...") / 2,
                    y + 72 / 2,
                    0xffffffff,
                    true);
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();

    }

}
