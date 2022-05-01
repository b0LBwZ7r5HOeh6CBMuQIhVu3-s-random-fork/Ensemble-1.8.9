package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.managers.FriendManager;
import it.fktcod.ktykshrk.module.mods.AntiBot;
import it.fktcod.ktykshrk.utils.EntityBot;
import it.fktcod.ktykshrk.utils.LoginUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Friend extends Command
{
	public Friend()
	{
		super("friend");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{	
			if(args[0].equalsIgnoreCase("add")) {
				if(args[1].equalsIgnoreCase("all")) {
					for(Object object : Utils.getEntityList()) {
						if(object instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) object;
							if(!player.isInvisible()) {
								FriendManager.addFriend(Utils.getPlayerName(player));
							}
						}
					}
				} else {
					FriendManager.addFriend(args[1]);
				}
			}
			else
			if(args[0].equalsIgnoreCase("remove")) {
				FriendManager.removeFriend(args[1]);
			}
			else
			if(args[0].equalsIgnoreCase("clear")) {
				FriendManager.clear();
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
		return "Friend manager.";
	}

	@Override
	public String getSyntax()
	{
		return "friend <add/remove/clear> <nick>";
	}
}