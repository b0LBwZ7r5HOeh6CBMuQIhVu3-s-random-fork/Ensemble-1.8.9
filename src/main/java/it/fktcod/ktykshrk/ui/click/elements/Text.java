package it.fktcod.ktykshrk.ui.click.elements;

import it.fktcod.ktykshrk.ui.click.base.Component;
import it.fktcod.ktykshrk.ui.click.base.ComponentType;

public class Text extends Component {

    private String[] text;

    public Text(int xPos, int yPos, int width, int height, Component component, String[] text) {

        super(xPos, yPos, width, height, ComponentType.TEXT, component, "");
        this.text = text;
    }

    public String[] getMessage() {

        return text;
    }
}
