package it.fktcod.ktykshrk.command;

import org.lwjgl.input.Mouse;

import it.fktcod.ktykshrk.managers.PickupFilterManager;
import it.fktcod.ktykshrk.managers.XRayManager;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;


public class PFilter extends Command
{
	public PFilter()
	{
		super("pfilter");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args[0].equalsIgnoreCase("add")) {
				PickupFilterManager.addItem(Integer.parseInt(args[1]));
			}
			else
			if(args[0].equalsIgnoreCase("remove")) {
				PickupFilterManager.removeItem(Integer.parseInt(args[1]));
			}
			else
			if(args[0].equalsIgnoreCase("clear")) {
				PickupFilterManager.clear();
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
		return "PickupFilter manager.";
	}

	@Override
	public String getSyntax()
	{
		return "pfilter add <id> | remove <id> | clear";
	}
}