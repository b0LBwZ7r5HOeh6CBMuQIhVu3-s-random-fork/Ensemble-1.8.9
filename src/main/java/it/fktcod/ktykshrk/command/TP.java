package it.fktcod.ktykshrk.command;

import it.fktcod.ktykshrk.managers.FileManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class TP extends Command
{
	public TP()
	{
		super("tp");
	}

	@Override
	public void runCommand(String s, String[] args)
	{

		if(args[0] != null && args[1] != null && args[2] != null){
			int x = Integer.parseInt(args[0]);
			int y = Integer.parseInt(args[1]);
			int z = Integer.parseInt(args[2]);
			Minecraft.getMinecraft().thePlayer.setPosition(x,y,z);
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, Wrapper.INSTANCE.player().onGround));
		}
	}

	@Override
	public String getDescription()
	{
		return "TP Anywhere";
	}

	@Override
	public String getSyntax()
	{
		return "tp <X> <Y> <Z>";
	}
}