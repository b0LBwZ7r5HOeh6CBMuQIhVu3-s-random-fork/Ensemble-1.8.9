package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

public class Hacks extends Command
{
	public Hacks()
	{
		super("hacks");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		for(Module module : HackManager.getHacks()) {
			ChatUtils.message(String.format("%s \u00a79| \u00a7f%s \u00a79| \u00a7f%s \u00a79| \u00a7f%s", module.getName(), module.getCategory(), module.getKey(), module.isToggled()));	
		}
		ChatUtils.message("Loaded " + HackManager.getHacks().size() + " Hacks.");
	}

	@Override
	public String getDescription()
	{
		return "Lists all hacks.";
	}

	@Override
	public String getSyntax()
	{
		return "hacks";
	}
}