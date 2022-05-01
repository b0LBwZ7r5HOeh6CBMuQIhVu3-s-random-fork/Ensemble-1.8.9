package it.fktcod.ktykshrk.managers;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.ui.clickgui.click.ClickGui;
import it.fktcod.ktykshrk.ui.clickgui.click.base.Component;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.CheckButton;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.Dropdown;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.ExpandingButton;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.Frame;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.KeybindMods;
import it.fktcod.ktykshrk.ui.clickgui.click.elements.Slider;
import it.fktcod.ktykshrk.ui.clickgui.click.listener.CheckButtonClickListener;
import it.fktcod.ktykshrk.ui.clickgui.click.listener.ComponentClickListener;
import it.fktcod.ktykshrk.ui.clickgui.click.listener.SliderChangeListener;
import it.fktcod.ktykshrk.utils.visual.GLUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;


public class GuiManager extends ClickGui {

    public void Init() {
    	int right = GLUtils.getScreenWidth();
        int framePosX = 20;
        int framePosY = 20;

        for (HackCategory category : HackCategory.values()) {
        	int frameHeight = 180;
        	int frameWidth = 100;
        	int hacksCount = 0;
                String name = Character.toString(category.toString().toLowerCase().charAt(0)).toUpperCase() + category.toString().toLowerCase().substring(1);
                Frame frame = new Frame(framePosX, framePosY, frameWidth, frameHeight, name);

                for (final Module module : HackManager.getHacks()) {
                    if (module.getCategory() == category) {
                        final ExpandingButton expandingButton = new ExpandingButton(0, 0, frameWidth, 14, frame, module.getName(), module) {

                            @Override
                            public void onUpdate() {
                                setEnabled(module.isToggled());
                            }
                        };
                        expandingButton.addListner(new ComponentClickListener() {

							@Override
							public void onComponenetClick(Component component, int button) {
								module.toggle();	
							}
                        });
                        expandingButton.setEnabled(module.isToggled());
                        
                        if (!module.getValues().isEmpty()) {
                            for (Value value : module.getValues()) {
                                if (value instanceof BooleanValue) {
                                    final BooleanValue booleanValue = (BooleanValue) value;
                                    CheckButton button = new CheckButton(0, 0, expandingButton.getDimension().width, 14, expandingButton, booleanValue.getName(), booleanValue.getValue(), null);
                                    button.addListeners(new CheckButtonClickListener() {

										@Override
										public void onCheckButtonClick(CheckButton checkButton) {
											for (Value value1 : module.getValues()) {
	                                            if (value1.getName().equals(booleanValue.getName())) {
	                                                value1.setValue(checkButton.isEnabled());
	                                            }
	                                        }
										}
                                    	
                                    });
                                    expandingButton.addComponent(button);
                                
                                } else if (value instanceof NumberValue) {
                                    final NumberValue doubleValue = (NumberValue) value;
                                    Slider slider = new Slider(doubleValue.getMin(), doubleValue.getMax(), doubleValue.getValue(), expandingButton, doubleValue.getName());
                                    slider.addListener(new SliderChangeListener() {
										@Override
										public void onSliderChange(Slider slider) {
											for (Value value1 : module.getValues()) {
	                                            if (value1.getName().equals(value.getName())) {
	                                                value1.setValue(slider.getValue());
	                                            }
	                                        }
										}
                                    	
                                    });

                                    expandingButton.addComponent(slider);
                                
                                
                            } else if (value instanceof ModeValue) {
                            	Dropdown dropdown = new Dropdown(0, 0, frameWidth, 14, frame, value.getName());
                            	
                            	final ModeValue modeValue = (ModeValue) value;
                            	
                            	for(Mode mode : modeValue.getModes()) {
                            		CheckButton button = new CheckButton(0, 0, 
                            				expandingButton.getDimension().width, 14, expandingButton, 
                            				mode.getName(), mode.isToggled(), modeValue);
                            		
                            		button.addListeners(new CheckButtonClickListener() {
										@Override
										public void onCheckButtonClick(CheckButton checkButton) {
											for(Mode mode1 : modeValue.getModes()) {
                            					if (mode1.getName().equals(mode.getName())) {
                            						mode1.setToggled(checkButton.isEnabled());
                            					}
                            				}
										}
                            		});
                            			dropdown.addComponent(button);
                            		}
                            		expandingButton.addComponent(dropdown);
                            	}
                            }
                        }
                        KeybindMods keybind = new KeybindMods(0, 0, 8, 14, expandingButton, module);
                        expandingButton.addComponent(keybind);
                        frame.addComponent(expandingButton);
                        hacksCount++;
                    }
                }
                
                if (framePosX + frameWidth + 10 < right) {
                    framePosX += frameWidth + 10;
                } else {
                    framePosX = 20;
                    framePosY += 60;
                }

                frame.setMaximizible(true);
                frame.setPinnable(true);
                this.addFrame(frame);
        }
        if (!FileManager.CLICKGUI.exists()) 
        	FileManager.saveClickGui(); 
        else FileManager.loadClickGui();
    }
}
