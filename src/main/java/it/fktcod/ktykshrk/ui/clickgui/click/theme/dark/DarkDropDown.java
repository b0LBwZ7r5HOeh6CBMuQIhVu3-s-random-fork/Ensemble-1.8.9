package it.fktcod.ktykshrk.ui.clickgui.click.theme.dark;

import it.fktcod.ktykshrk.module.mods.ClickGui;
import it.fktcod.ktykshrk.ui.clickgui.click.base.Component;
import it.fktcod.ktykshrk.ui.clickgui.click.base.ComponentRenderer;
import it.fktcod.ktykshrk.ui.clickgui.click.base.ComponentType;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.Dropdown;
import it.fktcod.ktykshrk.ui.clickgui.click.theme.Theme;


public class DarkDropDown extends ComponentRenderer {

    public DarkDropDown(Theme theme) {

        super(ComponentType.DROPDOWN, theme);
    }

    @Override
    public void drawComponent(Component component, int mouseX, int mouseY) {

        Dropdown dropdown = (Dropdown) component;
        String text = dropdown.getText();

        theme.fontRenderer.drawString(text, dropdown.getX() + 5, dropdown.getY() + (dropdown.getDropdownHeight() / 2 - theme.fontRenderer.FONT_HEIGHT / 4), 
        		ClickGui.getColor());

        if (dropdown.isMaximized()) {
            dropdown.renderChildren(mouseX, mouseY);
        }
    }

    @Override
    public void doInteractions(Component component, int mouseX, int mouseY) {

    }
}
