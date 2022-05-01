package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.utils.LoginUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.item.ItemStack;

public class Toggle extends Command {
	public Toggle() {
		super("t");
	}

	@Override
	public void runCommand(String s, String[] args) {
		try {

			HackManager.getHack(args[0]).toggle();

			if (HackManager.getHack(args[0]).isToggled()) {
				HackManager.getHack(args[0]).onEnable();
			} else {
				HackManager.getHack(args[0]).onDisable();
			}

		} catch (Exception e) {
			e.printStackTrace();
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription() {
		return "Toggling selected hack.";
	}

	@Override
	public String getSyntax() {
		return "t <hackname>";
	}
}