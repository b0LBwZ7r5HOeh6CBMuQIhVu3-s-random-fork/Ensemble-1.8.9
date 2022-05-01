package it.fktcod.ktykshrk.command;

import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.managers.ScriptManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.script.CoreScript;
import it.fktcod.ktykshrk.script.ScriptCommand;
import it.fktcod.ktykshrk.script.ScriptModule;
import it.fktcod.ktykshrk.utils.FileUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;

public class LoadScript extends Command {

	//public ScriptManager scriptManager;

	public LoadScript() {
		super("plugin");

	}

	@Override
	public String getDescription() {

		return "load scripts";

	}

	@Override
	public String getSyntax() {
		// TODO 自动生成的方法存根
		return "script <scname>";

	}

	@Override
	public void runCommand(String s, String[] args) {
		if ("load".equalsIgnoreCase(args[0])) {
			Core.scriptManager = new ScriptManager();
			
		}
		if ("list".equalsIgnoreCase(args[0])) {
			//ChatUtils.message("ok");
			List<CoreScript> scripts = Core.scriptManager.scripts;
			//System.out.println(scripts.size());
			if (!scripts.isEmpty()) {
				ChatUtils.message("-------------Script-------------");

				for (CoreScript script : scripts) {
					int spaceTimes = script.name.length() <= 5 ? script.name.length() + 4 : script.name.length();
					StringBuilder msg = new StringBuilder(script.name
							+ (script.scriptCommand != null && !script.name.endsWith("Command") ? "Command" : ""));
					for (int j = 0; j < spaceTimes; j++) {
						msg.append(" ");
					}
					msg.append(script.version);
					for (int j = 0; j < spaceTimes; j++) {
						msg.append(" ");
					}
					msg.append(script.author);
					ChatUtils.message(msg.toString());
				}

				ChatUtils.message("--------------------------------");
			}
			if (scripts.isEmpty()) {
				ChatUtils.message("Nothing");
			}
		}
		if ("reload".equalsIgnoreCase(args[0])) {
			// Clean Module Manager
			for (Module mod : HackManager.pluginModsList.keySet()) {
				HackManager.modules.remove(mod);
			}
			HackManager.pluginModsList.clear();

			// Clean Command Manager
			// Reload
			Core.scriptManager.loadScripts();
			ChatUtils.message("Load Plugins");
		}

	}

}
