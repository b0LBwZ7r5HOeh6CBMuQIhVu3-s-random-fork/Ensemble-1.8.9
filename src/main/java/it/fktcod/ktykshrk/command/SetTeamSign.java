package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;

import java.lang.reflect.Field;

public class SetTeamSign extends Command {
	public static String teamsign;

	public SetTeamSign() {
		super("setteamsign");
	}

	@Override
	public void runCommand(String s, String[] args) {
		teamsign = args[0];
		ChatUtils.message("New TeamSign: " +teamsign);

	}

	@Override
	public String getDescription() {
		return "set team sign for Team TabList Mode";
	}

	@Override
	public String getSyntax() {
		return "setteamsign <name>";
	}
}
