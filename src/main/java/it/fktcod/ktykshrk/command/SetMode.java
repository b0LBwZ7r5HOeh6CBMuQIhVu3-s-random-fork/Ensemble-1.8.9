package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;

public class SetMode extends Command {
	public SetMode() {
		super("setm");
	}

	@Override
	public void runCommand(String s, String[] subcommands) {

		Module module = HackManager.getHack(subcommands[0]);
		if (!module.getValues().isEmpty()) {
			// && value.getName().equalsIgnoreCase(subcommands[1])
			for (Value value : module.getValues()) {
				if (value instanceof ModeValue && ((ModeValue) value).getModeName().equalsIgnoreCase(subcommands[1])) {
					ModeValue modeValue = (ModeValue) value;
					for (Mode mode : modeValue.getModes()) {
						if (mode.getName().equalsIgnoreCase(subcommands[2])) {
							
							mode.setToggled(true);
							ChatUtils.message("Mode parameters set successfully!");
							FileManager.saveHacks();
							FileManager.saveClickGui();

						} else {
							mode.setToggled(false);
						}
					}

				}
			}
		}
	}

	@Override
	public String getDescription() {
		return "Set the Mode parameter";
	}

	@Override
	public String getSyntax() {
		return "setm <hack> <mode> <modevalue> <true/false>";
	}

}
