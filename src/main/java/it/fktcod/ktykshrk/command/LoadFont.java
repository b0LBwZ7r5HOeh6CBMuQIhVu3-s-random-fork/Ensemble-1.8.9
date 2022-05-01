package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.managers.FontManager;
import it.fktcod.ktykshrk.module.mods.HUD;
import it.fktcod.ktykshrk.module.mods.KillAura;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

public class LoadFont extends Command{
	public LoadFont()
	{
		super("loadfont");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		
	}

	@Override
	public String getDescription()
	{
		return "reload font";
	}

	@Override
	public String getSyntax()
	{
		return "loadfont";
	}
}
