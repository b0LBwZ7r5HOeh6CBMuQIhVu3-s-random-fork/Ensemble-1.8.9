package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.font.FontLoaders;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by peanut on 18/02/2021
 */

public class Hotbar extends Module {

    static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static final DateTimeFormatter timeFormat12 = DateTimeFormatter.ofPattern("h:mm a");
    static final DateTimeFormatter timeFormat24 = DateTimeFormatter.ofPattern("HH:mm");

    public static BooleanValue FPS;
    public static BooleanValue Coordinates;
    public static BooleanValue TimeDate;
    public static BooleanValue TimeFormat;

    public Hotbar() {
        super("Hotbar", HackCategory.VISUAL);
        FPS = new BooleanValue("",true);
        Coordinates = new BooleanValue("Coordinates",true);
        TimeDate = new BooleanValue("Time / Date",true);
        TimeFormat = new BooleanValue("24H / 12H",true);
        this.addValue(FPS,Coordinates,TimeDate,TimeFormat);
        setChinese(Core.Translate_CN[107]);
    }

    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {


        Utils.drawRect(0, Utils.getScaledRes().getScaledHeight() - 23, Utils.getScaledRes().getScaledWidth() - 7, Utils.getScaledRes().getScaledHeight(), 0x44000000);
        Utils.drawRect(Utils.getScaledRes().getScaledWidth() - 7, Utils.getScaledRes().getScaledHeight() - 23,Utils.getScaledRes().getScaledWidth() , Utils.getScaledRes().getScaledHeight(), Color.WHITE.getRGB());

        EntityPlayer entityplayer = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();

        float needX = (Utils.getScaledRes().getScaledWidth() / 2 - 91 + entityplayer.inventory.currentItem * 20);
        float steps = 10f;


        boolean fps = FPS.getValue();
        boolean coord = Coordinates.getValue();
        boolean tdate = TimeDate.getValue();
        boolean ttf = HUD.font.getValue();

        addSlide(needX, steps);

        boolean timeformat = TimeFormat.getValue();
        LocalDateTime now = LocalDateTime.now();
        String date = dateFormat.format(now);
        String time = timeformat ? timeFormat24.format(now) : timeFormat12.format(now);
        String fps1 = String.format("FPS %s", mc.getDebugFPS());

        String x = String.valueOf((int) mc.thePlayer.posX);
        String y = String.valueOf((int) mc.thePlayer.posY);
        String z = String.valueOf((int) mc.thePlayer.posZ);

        String coordinates = String.format("X: %s Y: %s Z: %s", x, y, z);

        if (tdate) {
            if(ttf) {
                FontLoaders.default16.drawStringWithShadow(date, Utils.getScaledRes().getScaledWidth() - FontLoaders.default16.getStringWidth(date) - 9, Utils.getScaledRes().getScaledHeight() - 10, Color.white.getRGB());
                FontLoaders.default16.drawStringWithShadow(time, timeformat ? Utils.getScaledRes().getScaledWidth() - FontLoaders.default16.getStringWidth(time) - 10 : Utils.getScaledRes().getScaledWidth() - FontLoaders.default16.getStringWidth(time) - 10, Utils.getScaledRes().getScaledHeight() - 21, Color.white.getRGB());
            } else {
                mc.fontRendererObj.drawStringWithShadow(date, Utils.getScaledRes().getScaledWidth() - FontLoaders.default16.getStringWidth(date) - 16, Utils.getScaledRes().getScaledHeight() - 5, -1);
                mc.fontRendererObj.drawStringWithShadow(time, timeformat ? Utils.getScaledRes().getScaledWidth() - FontLoaders.default16.getStringWidth(time) - 15 : Utils.getScaledRes().getScaledWidth() - FontLoaders.default16.getStringWidth(time) - 15, Utils.getScaledRes().getScaledHeight() - 21, -1);
            }
        }

        if(ttf) {

        }else{

        }

        if (coord) {
            if(ttf) {
                FontLoaders.default16.drawStringWithShadow(coordinates, 2, Utils.getScaledRes().getScaledHeight() - 10, Color.white.getRGB());
            } else {
                mc.fontRendererObj.drawStringWithShadow(coordinates, 2, Utils.getScaledRes().getScaledHeight() - 5, -1);
            }
        }

        if (fps) {
            if(ttf) {
                FontLoaders.default16.drawStringWithShadow(fps1, 2, coord ? Utils.getScaledRes().getScaledHeight() - 20 : Utils.getScaledRes().getScaledHeight() - 6, Color.white.getRGB());
            } else {
                mc.fontRendererObj.drawStringWithShadow(fps1, 2, coord ? Utils.getScaledRes().getScaledHeight() - 12 : Utils.getScaledRes().getScaledHeight() - 5, -1);
            }
        }


    }


    public static double slide = 1D;
    public static void addSlide(double needX, double steps) {
        if (slide != needX) {
            if (slide < needX)
                if (slide <= needX - steps) {
                    slide += steps;
                } else if (slide > needX - steps) {
                    slide = needX;
                }
            if (slide > needX)
                if (slide >= needX + steps) {
                    slide -= steps;
                } else if (slide < needX + steps) {
                    slide = needX;
                }
        }
    }

}
