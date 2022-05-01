package it.fktcod.ktykshrk.ui.clickgui.click.theme.dark;

import it.fktcod.ktykshrk.module.mods.ClickGui;
import it.fktcod.ktykshrk.ui.clickgui.click.base.Component;
import it.fktcod.ktykshrk.ui.clickgui.click.base.ComponentRenderer;
import it.fktcod.ktykshrk.ui.clickgui.click.base.ComponentType;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.Slider;
import it.fktcod.ktykshrk.ui.clickgui.click.theme.Theme;
import it.fktcod.ktykshrk.utils.visual.ColorUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;

public class DarkSlider extends ComponentRenderer {

    public DarkSlider(Theme theme) {

        super(ComponentType.SLIDER, theme);
    }

    @Override
    public void drawComponent(Component component, int mouseX, int mouseY) {

        Slider slider = (Slider) component;
        int width = (int) ((slider.getDimension().getWidth()) * slider.getPercent());
        
        int mainColor = ClickGui.isLight ? ColorUtils.color(255, 255, 255, 255) : ColorUtils.color(0, 0, 0, 255);
        int mainColorInv = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(255, 255, 255, 255);
        int strColor = ClickGui.isLight ? ColorUtils.color(0.3f, 0.3f, 0.3f, 1.0f) : ColorUtils.color(0.5f, 0.5f, 0.5f, 1.0f);
        
        //GLUtils.glColor(ColorUtils.color(1.0f, 1.0f, 1.0f, 1.0f));
        
        theme.fontRenderer.drawString(slider.getText(), slider.getX() + 4, slider.getY() + 2, 
        		strColor);
        
        theme.fontRenderer.drawString(slider.getValue() + "", slider.getX() + slider.getDimension().width - theme.fontRenderer.getStringWidth(slider.getValue() + "") - 2, slider.getY() + 2, 
        		mainColorInv);
        
        RenderUtils.drawRect(slider.getX(), slider.getY() + slider.getDimension().height / 2 + 3, slider.getX() + (width) + 3, (slider.getY() + slider.getDimension().height / 2) + 6, 
        		mainColorInv);
        
        RenderUtils.drawRect(slider.getX(), slider.getY() + slider.getDimension().height / 2 + 3, slider.getX() + (width), (slider.getY() + slider.getDimension().height / 2) + 6, 
        		ClickGui.getColor());
    }

    @Override
    public void doInteractions(Component component, int mouseX, int mouseY) {

    }
}
