package it.fktcod.ktykshrk.command.irc;

import it.fktcod.ktykshrk.command.Command;
import it.fktcod.ktykshrk.module.mods.HUD;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

public class SetHUDString extends Command
{
	public SetHUDString()
	{
		super("sethudstring");
	}

	@Override
	public void runCommand(String s, String[] args)
	{

		HUD.HUDString = args[0];
		ChatUtils.message("NEW HUD TEXT: " +HUD.HUDString );
	}

	@Override
	public String getDescription()
	{
		return "Set Your Own WaterMark.";
	}

	@Override
	public String getSyntax()
	{
		return "sethudstring <text>";
	}
}