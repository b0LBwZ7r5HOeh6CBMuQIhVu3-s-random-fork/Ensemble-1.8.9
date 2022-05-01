package it.fktcod.ktykshrk.irc;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

public class IRCTheard extends Thread {



	@Override
	public void run() {
		IRCChat.connect();
		while (true) {
			IRCChat.handleInput();
			try {
				IRCChat.socket.sendUrgentData(255);
			} catch (IOException e) {
				HackManager.getHack("IRCChat").setToggled(false);
				HackManager.getHack("IRCChat").onDisable();
				e.printStackTrace();
			}
			if (!HackManager.getHack("IRCChat").isToggled()) {
				ChatUtils.error("Disconnect from IRC :-(");
				break;
			}
		}
	}
}
