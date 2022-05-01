package it.fktcod.ktykshrk.command;

import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

public class Key extends Command
{
	public Key()
	{
		super("key");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			for(Module module : HackManager.getHacks()) {
				if(module.getName().equalsIgnoreCase(args[1])) {
					module.setKey(Keyboard.getKeyIndex((args[0].toUpperCase())));
					FileManager.saveHacks();
			 		ChatUtils.message(module.getName() + " key changed to \u00a79" + Keyboard.getKeyName(module.getKey()));
				}
			}

		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "Change key for hack.";
	}

	@Override
	public String getSyntax()
	{
		return "key <key> <hack>";
	}
}