package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.value.Value;

public class SetSlider extends Command {
	public SetSlider() {
		super("sets");
	}

	@Override
	public void runCommand(String s, String[] subcommands) {

		Module module = HackManager.getHack(subcommands[0]);
		if (!module.getValues().isEmpty()) {
			// value.getName().equals(subcommands[1].toLowerCase().replace("_", " "))
			for (Value value : module.getValues()) {

				if (value.getName().equalsIgnoreCase(subcommands[1]) && value instanceof NumberValue) {

					value.setValue(Double.parseDouble(subcommands[2]));
					ChatUtils.message("Number parameters set successfully!");
					FileManager.saveHacks();
					FileManager.saveClickGui();

				}

			}
		}
	}

	@Override
	public String getDescription() {
		return "Set the Number parameter";
	}

	@Override
	public String getSyntax() {
		return "sets <hack> <setting> <value>";
	}

}
