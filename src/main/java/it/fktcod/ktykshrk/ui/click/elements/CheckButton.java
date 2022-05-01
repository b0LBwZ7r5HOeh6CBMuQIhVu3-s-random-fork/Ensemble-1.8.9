package it.fktcod.ktykshrk.ui.click.elements;

import java.util.ArrayList;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.click.base.Component;
import it.fktcod.ktykshrk.ui.click.base.ComponentType;
import it.fktcod.ktykshrk.ui.click.listener.CheckButtonClickListener;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.Value;

public class CheckButton extends Component {

    public ArrayList<CheckButtonClickListener> listeners = new ArrayList<CheckButtonClickListener>();

    private boolean enabled = false;
    private ModeValue modeValue = null;

    public CheckButton(int xPos, int yPos, int width, int height, Component component, String text, boolean enabled, ModeValue modeValue) {

        super(xPos, yPos, width, height, ComponentType.CHECK_BUTTON, component, text);
        this.enabled = enabled;
        this.modeValue = modeValue;
    }

    @Override
    public void onMousePress(int x, int y, int buttonID) {
    	if(modeValue != null) {
    		for(Mode mode : modeValue.getModes()) {
    			mode.setToggled(false);
    		}
    		this.enabled = true;
    	} 
    	else {
    		this.enabled = !this.enabled;
    	}
        for (CheckButtonClickListener listener : listeners) {
            listener.onCheckButtonClick(this);
        }
    }

    public ArrayList<CheckButtonClickListener> getListeners() {

        return listeners;
    }

    public void addListeners(CheckButtonClickListener listener) {

        listeners.add(listener);
    }

    public boolean isEnabled() {

        return enabled;
    }
    
    public ModeValue getModeValue() {
		return modeValue;
	}

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }
}
