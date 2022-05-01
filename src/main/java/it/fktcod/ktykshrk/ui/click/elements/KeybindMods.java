package it.fktcod.ktykshrk.ui.click.elements;

import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.click.base.Component;
import it.fktcod.ktykshrk.ui.click.base.ComponentType;
import it.fktcod.ktykshrk.wrappers.Wrapper;

public class KeybindMods extends Component {

    private Module module;

    private boolean editing;

    public KeybindMods(int xPos, int yPos, int width, int height, Component component, Module module) {

        super(xPos, yPos, width, height, ComponentType.KEYBIND, component, "");
        this.module = module;
    }

    @Override
    public void onUpdate() {
        if (Keyboard.getEventKeyState()) {
            if (editing) {
                if (Keyboard.getEventKey() == Keyboard.KEY_DELETE)
                    module.setKey(-1);
                else
                    module.setKey(Keyboard.getEventKey());
                editing = false;
            }
        }
    }


    @Override
    public void onMousePress(int x, int y, int buttonID) {
        if (x > this.getX() + Wrapper.INSTANCE.fontRenderer().getStringWidth("Key") + 6 && x < this.getX() + this.getDimension().width && y > this.getY() && y < this.getY() + this.getDimension().height) {
            editing = !editing;
        }
    }

    public Module getMod() {
        return module;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }
}
