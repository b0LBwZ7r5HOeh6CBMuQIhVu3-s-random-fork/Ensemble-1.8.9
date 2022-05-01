package it.fktcod.ktykshrk.command;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import net.minecraft.client.Minecraft;

public class SetName extends Command
{
	public SetName()
	{
		super("setname");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try {
            String message = "";

            for (int i = 0; i < args.length; i++) {
                String str = args[i];
                message = str;
            }
            Class var51 = Minecraft.getMinecraft().getSession().getClass();
            Field f = var51.getDeclaredFields()[0];
            f.setAccessible(true);
            f.set(Minecraft.getMinecraft().getSession(), message);
            ChatUtils.message("Successfully set the name");


        } catch (Exception var5) {
            var5.printStackTrace();

        }
	}

	@Override
	public String getDescription()
	{
		return "Reset your name";
	}

	@Override
	public String getSyntax()
	{
		return "setname <name>";
	}
}
