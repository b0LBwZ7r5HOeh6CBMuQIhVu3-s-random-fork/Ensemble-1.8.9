package it.fktcod.ktykshrk.event;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ButtonEvent {
	public static final class Init extends Event {
		private final List<GuiButton> buttonList;

		public Init(List<GuiButton> buttonList) {
			this.buttonList = buttonList;
		}

		public List<GuiButton> getButtonList() {
			return buttonList;
		}
	}

	public static final class Press extends Event {
		private final GuiButton button;

		public Press(GuiButton button) {
			this.button = button;
		}

		public GuiButton getButton() {
			return button;
		}
	}
}
