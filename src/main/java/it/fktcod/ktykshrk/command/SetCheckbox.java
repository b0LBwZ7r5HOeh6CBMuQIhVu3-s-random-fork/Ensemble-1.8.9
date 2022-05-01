package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Value;

public class SetCheckbox extends Command {
	public SetCheckbox() {
		super("setc");
	}

	@Override
	public void runCommand(String s, String[] subcommands) {

		Module module = HackManager.getHack(subcommands[0]);
		if (!module.getValues().isEmpty()) {
			// value.getName().equals(subcommands[1].toLowerCase().replace("_", " "))
			for (Value value : module.getValues()) {

				if (value.getName().equalsIgnoreCase(subcommands[1]) && value instanceof BooleanValue) {
					if (subcommands[2].equalsIgnoreCase("true") || subcommands[2].equalsIgnoreCase("false")) {
						
						value.setValue(Boolean.parseBoolean(subcommands[2]));
						ChatUtils.message("Boolean parameters set successfully!");
						FileManager.saveHacks();
						FileManager.saveClickGui();
					} else {
						ChatUtils.error("Please use true or false for the third parameter");
						ChatUtils.message(subcommands[2]);
					}
				}

			}
		}
	}

	@Override
	public String getDescription() {
		return "Set the Boolean parameter";
	}

	@Override
	public String getSyntax() {
		return "setc <hack> <setting> <value>";
	}

}
