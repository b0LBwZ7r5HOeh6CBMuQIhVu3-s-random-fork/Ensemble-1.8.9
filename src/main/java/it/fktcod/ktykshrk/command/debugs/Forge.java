package it.fktcod.ktykshrk.command.debugs;

import it.fktcod.ktykshrk.command.Command;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.lang.reflect.Field;

public class Forge extends Command {

	public Forge() {
		super("forge");
	}

	@Override
	public void runCommand(String s, String[] args) {
		try {

			if (args[0] == "unregister"){
				ChatUtils.message("Unregister ClassName: "+args[1]);
				MinecraftForge.EVENT_BUS.unregister(Class.forName(args[1]));
				FMLCommonHandler.instance().bus().unregister(Class.forName(args[1]));
			}

			if (args[0] == "register"){
				ChatUtils.message("Unregister ClassName: "+args[1]);
				MinecraftForge.EVENT_BUS.unregister(Class.forName(args[1]));
				FMLCommonHandler.instance().bus().unregister(Class.forName(args[1]));
			}

			if (args[0] == "EventBusList"){
				Class EVENT_BUS = MinecraftForge.EVENT_BUS.getClass();
				Field listeners = EVENT_BUS.getDeclaredField("listeners");
				listeners.setAccessible(true);
				System.out.println("listeners = " + listeners);
				Field listenerOwners = EVENT_BUS.getDeclaredField("listenerOwners");
				listenerOwners.setAccessible(true);
				System.out.println("listenerOwners = " + listenerOwners);
			}

		} catch (Exception var5) {
			var5.printStackTrace();

		}

	}

	@Override
	public String getDescription() {
		return "unregist";
	}

	@Override
	public String getSyntax() {
		return "unregist <classname>";
	}
}
