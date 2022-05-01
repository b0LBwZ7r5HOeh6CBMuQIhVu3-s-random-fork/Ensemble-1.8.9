package it.fktcod.ktykshrk.ui.clickgui.click.theme.dark;

import it.fktcod.ktykshrk.ui.clickgui.click.base.Component;
import it.fktcod.ktykshrk.ui.clickgui.click.base.ComponentRenderer;
import it.fktcod.ktykshrk.ui.clickgui.click.base.ComponentType;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.Button;
import it.fktcod.ktykshrk.ui.clickgui.click.theme.Theme;
import it.fktcod.ktykshrk.utils.visual.ColorUtils;
import it.fktcod.ktykshrk.utils.visual.GLUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;

public class DarkButton extends ComponentRenderer {

    public DarkButton(Theme theme) {

        super(ComponentType.BUTTON, theme);
    }

    @Override
    public void drawComponent(Component component, int mouseX, int mouseY) {

        Button button = (Button) component;
        String text = button.getText();
        int color = ColorUtils.color(50, 50, 50, 100);
        int enable = ColorUtils.color(255, 255, 255, 255);

        if (GLUtils.isHovered(button.getX(), button.getY(), button.getDimension().width, button.getDimension().height, mouseX, mouseY)) {
            color = ColorUtils.color(70, 70, 70, 255);
        }

        if (button.isEnabled()) {
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 1, button.getY() + button.getDimension().height, enable);
        } else {
            RenderUtils.drawRect(button.getX(), button.getY(), button.getX() + button.getDimension().width - 1, button.getY() + button.getDimension().height, color);
        }

        theme.fontRenderer.drawString(text, button.getX() + 5, button.getY() + (button.getDimension().height / 2 - theme.fontRenderer.FONT_HEIGHT / 4), ColorUtils.color(255, 255, 255, 255));
    }

    @Override
    public void doInteractions(Component component, int mouseX, int mouseY) {}
}
