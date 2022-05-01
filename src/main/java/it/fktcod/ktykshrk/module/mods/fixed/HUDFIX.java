package it.fktcod.ktykshrk.module.mods.fixed;

import java.awt.Color;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Mapping;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class HUDFIX extends Module {

	public static TTFFontRenderer sigmaFont = Core.fontManager.getFont("SFB 6");
	public static TTFFontRenderer jigsawFont = Core.fontManager.getFont("JIGR 19");
	public static FontRenderer vanillaFont=Wrapper.INSTANCE.fontRenderer();
	
	public NumberValue listspeed;

	int posX = 3;
	int posY = 2;
	int posX2 = 10 + 7 + 3 + 2;

	int rainbowTick = 0;

	public HUDFIX() {
		super("HUDFIX", HackCategory.VISUAL);
		this.setShow(false);
		
		listspeed=new NumberValue("ListSpeed", 3D, 0D, 20D);
		
		this.addValue(listspeed);
		
		new Thread(() -> {
            while (true) {
                try {
                    if (!Mappings.running.getBoolean(mc)) break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep((long) listspeed.getValue().longValue());
                } catch (InterruptedException e) {
                }
                for (Module module : HackManager.getHacks()) {
                    if (module.isToggled()) {
                        if (module.getSlide() < sigmaFont.getWidth(module.getName())) {
                            module.setSlide(module.getSlide() + 1);
                        }

                    } else if (module.getSlide() != 0 && !module.isToggled()) {
                        if (module.getSlide() > 0) {
                            module.setSlide(module.getSlide() - 1);
                        }

                    }
                }
            }
        }, "smooth array").start();

	}

	

	@Override
	public void onRenderGameOverlay(Text event) {
		
		/*
		 * for (Hack mod : HackManager.getHacks()) { if (mod.isToggled()) { if
		 * (mod.getSlide() < sigmaFont.getWidth(mod.getName())) {
		 * mod.setSlide(mod.getSlide() + 1); }
		 * 
		 * } else if (mod.getSlide() != 0 && !mod.isToggled()) { if (mod.getSlide() > 0)
		 * { mod.setSlide(mod.getSlide() - 1); }
		 * 
		 * } }
		 */

		
		int count = 0;
		
		 for (int i = 0; i < HackManager.getSortedHacks2().size(); i++) {
			 
	
             ScaledResolution sr = new ScaledResolution(mc);
             Module module = HackManager.getToggledHacks().get(i);
             Color rainbow = ColorUtils.rainbow();
            // Color color = Hydrogen.getUClient().settingsManager.getSettingByName("List Color").getValString().equalsIgnoreCase("White") ? Color.white : (Hydrogen.getClient().settingsManager.getSettingByName("List Color").getValString().equalsIgnoreCase("Rainbow") ? rainbow : mod.getColor());
    
             int mheight = (count * 11 + i) + 1;
             double rectX = (sr.getScaledWidth() - module.getSlide() - 5);
             double rectX2 = rectX + sigmaFont.getWidth((HackManager.getToggledHacks().get(i)).getName()) + 3.0D;
             double rectY = (1 + i * 12);
             double rectY2 = rectY + sigmaFont.getHeight((HackManager.getToggledHacks().get(i)).getName()) - 2;
             int outlinecolor = 0x80000000;
             if (true) {
                 if (i == 0) {

                     // if first module, then draw side line 1px higher, so it connects with the top line

                     Utils.rect(rectX - 1.0D, rectY - 1.0D, rectX2 + 10, rectY, outlinecolor);

                     // top line

                     Utils.rect(rectX - 2.0D, rectY - 2, rectX - 1, rectY2 - 5, outlinecolor);
                 } else {

                     // side line

                     Utils.rect(rectX - 2.0D, rectY, rectX - 1, rectY2 - 5, outlinecolor);
                 }

                 if (i == HackManager.getToggledHacks().size() - 1) {

                     // bottom arraylist line

                     Utils.rect(rectX - 2.0D, rectY2 - 5, rectX2 + 10, rectY2 - 4, outlinecolor);
                 }

                 if (i != HackManager.getToggledHacks().size() - 1) {
                     double modwidth = (sigmaFont.getWidth((HackManager.getToggledHacks().get(i)).getName()));
                     double mwidthNext = (sigmaFont.getWidth((HackManager.getToggledHacks().get(i + 1)).getName()));
                     double difference = modwidth - mwidthNext;
                     if (modwidth < mwidthNext) {
                         if ((HackManager.getToggledHacks().get(i + 1)).getSlide() < sigmaFont.getWidth((HackManager.getToggledHacks().get(i + 1)).getName()) + 3) {
                             if ((HackManager.getToggledHacks().get(i)).getSlide() >= sigmaFont.getWidth((HackManager.getToggledHacks().get(i)).getName()) + 3) {
                                 rectX = rectX - (HackManager.getToggledHacks().get(i + 1)).getSlide() + sigmaFont.getWidth((HackManager.getToggledHacks().get(i)).getName()) - difference + 2.0D;
                             }
                         }
                     }
                     Utils.rect(rectX - 2, rectY2 - 5, rectX + 3.0D + difference - 5, rectY2 - 4, outlinecolor);
                 }
             }

             if (true) {
                 Utils.rect(sr.getScaledWidth() - module.getSlide() - 6, 1 + i * 12, sr.getScaledWidth(), i * 12 + 13, 0x66000000);
             }

             sigmaFont.drawStringWithShadow(module.getName(),(float) (sr.getScaledWidth() - module.getSlide() - 3),(float) mheight, Colors.getColor(Color.white));

             count++;

         }
     

		super.onRenderGameOverlay(event);
	}


}

