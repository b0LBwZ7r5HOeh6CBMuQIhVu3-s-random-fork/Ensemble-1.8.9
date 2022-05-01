package it.fktcod.ktykshrk.command;

import java.awt.Desktop;

import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.utils.LoginUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.item.ItemStack;


public class OpenDir extends Command
{
	public OpenDir()
	{
		super("opendir");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			Desktop.getDesktop().open(FileManager.DIR);
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "Opening directory of config.";
	}

	@Override
	public String getSyntax()
	{
		return "opendir";
	}
}