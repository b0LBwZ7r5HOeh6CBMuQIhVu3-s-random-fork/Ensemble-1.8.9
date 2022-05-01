package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.utils.LoginUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;


public class Say extends Command
{
	public Say()
	{
		super("say");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			String content = "";
			for(int i = 0; i < args.length; i++) {
				content = content + " " + args[i];
			}
			Wrapper.INSTANCE.sendPacket(new C01PacketChatMessage(content));
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "Send message to chat.";
	}

	@Override
	public String getSyntax()
	{
		return "say <message>";
	}
}