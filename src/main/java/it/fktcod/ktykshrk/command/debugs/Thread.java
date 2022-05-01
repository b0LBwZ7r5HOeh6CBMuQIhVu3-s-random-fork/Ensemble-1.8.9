package it.fktcod.ktykshrk.command.debugs;

import it.fktcod.ktykshrk.command.Command;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.lang.reflect.Field;

public class Thread extends Command {

	public Thread() {
		super("thread");
	}

	@Override
	public void runCommand(String s, String[] args) {
		try {

			ThreadGroup currentGroup = java.lang.Thread.currentThread().getThreadGroup();
			int noThreads = currentGroup.activeCount();
			java.lang.Thread[] lstThreads = new java.lang.Thread[noThreads];
			currentGroup.enumerate(lstThreads);



			if (args[0] == "kill"){
				if (args[1] == "name"){
					for (int i = 0; i < noThreads; i++){
						if (lstThreads[i].getName().contains(args[2])){
							ChatUtils.message("Thread: " + i + " [ " + lstThreads[i].getName()+" ]");
							lstThreads[i].interrupt();
						}
					}
				}
				if (args[1] == "id"){
					for (int i = 0; i < noThreads; i++){
						if (i == Integer.parseInt(args[2])){
							ChatUtils.message("Thread: " + i + " [ " + lstThreads[i].getName()+" ]");
							lstThreads[i].interrupt();
						}
					}
				}
			}


		} catch (Exception var5) {
			var5.printStackTrace();

		}

	}

	@Override
	public String getDescription() {
		return "thread";
	}

	@Override
	public String getSyntax() {
		return "thread kill name/id <name/id>";
	}
}
