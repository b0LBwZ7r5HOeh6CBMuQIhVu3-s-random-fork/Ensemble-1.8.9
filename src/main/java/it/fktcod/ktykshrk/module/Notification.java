package it.fktcod.ktykshrk.module;

import java.awt.Color;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.ui.clickgui.huangbai.ClickGuiRender;
import it.fktcod.ktykshrk.utils.visual.AnimationUtils;
import it.fktcod.ktykshrk.utils.visual.Colors;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.utils.visual.font.render.TTFFontRenderer;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {
    public String text;
    TTFFontRenderer sigmaFont = Core.fontManager.getFont("SFB 8");
    public double width = 30;
    public double height=20;
    public float x;
    Type type;
    public float y, position;
    public boolean in = true;
    public AnimationUtils animationUtils = new AnimationUtils();
    AnimationUtils yAnimationUtils = new AnimationUtils();


    public Notification(String text, Type type) {
        this.text = text;
        this.type = type;
        width = sigmaFont.getWidth(text) + 25;
        x = (float) width;
    }

    public void onRender() {
        int i = 0;
        for (Notification notification : Core.notificationManager.notifications) {
            if (notification == this) {
                break;
            }
            i++;
        }

        y = yAnimationUtils.animate((float) ((float) i * (height + 5)), y, 0.1f);
        ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        RenderUtils.drawRect(sr.getScaledWidth() + x - width, sr.getScaledHeight() - 55 - y - height, sr.getScaledWidth() + x, sr.getScaledHeight() - 55 - y, Colors.getColor(Color.black));
        ClickGuiRender.drawFilledCircle(sr.getScaledWidth() + x - width, sr.getScaledHeight() - 45 - y - height, 10, Colors.getColor(Color.black),5);
        
        // RenderUtils.drawShadow((float) (sr.getScaledWidth() + x - width), (float) (sr.getScaledHeight() - 50 - y - height), sr.getScaledWidth() + x, sr.getScaledHeight() - 50 - y, 5);
        sigmaFont.drawStringWithShadow(text, ((float) (sr.getScaledWidth() + x - width + 10)), ((float) (sr.getScaledHeight() - 50f - y - 18)), new Color(204, 204, 204, 232).getRGB());
    }

    public enum Type {
        Success,
        Error,
        Info
    }
}