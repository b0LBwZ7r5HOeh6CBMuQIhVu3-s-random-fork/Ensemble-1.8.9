package it.fktcod.ktykshrk.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.fktcod.ktykshrk.command.*;
import it.fktcod.ktykshrk.command.debugs.Forge;
import it.fktcod.ktykshrk.command.irc.IRC;
import it.fktcod.ktykshrk.command.irc.SetHUDString;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;



	public class CommandManager
	{
		public static ArrayList<Command> commands = new ArrayList<Command>();
	    private volatile static CommandManager instance;
		public static Map<Command,Object> scriptcommands = new HashMap<>();
		public static Map<Command, Object> disabledPluginCommands = new HashMap<>();
		public static char cmdPrefix = '.';

		public CommandManager()
		{
			addCommands();
		}

		public void addCommands()
		{
			commands.add(new Help());
			commands.add(new Hacks());
			commands.add(new Key());
			commands.add(new VClip());
			commands.add(new Login());
			commands.add(new Say());
			commands.add(new Effect());
			commands.add(new DumpPlayers());
			commands.add(new DumpClasses());
			commands.add(new SkinSteal());
			commands.add(new Friend());
			commands.add(new Enemy());
			commands.add(new Toggle());
			commands.add(new PFilter());
			commands.add(new OpenDir());
			commands.add(new TP());
			commands.add(new SetTeamSign());
			commands.add(new SetName());
			commands.add(new SetCheckbox());
			commands.add(new SetSlider());
			commands.add(new SetMode());
			commands.add(new LoadFont());
			commands.add(new IRC());
			commands.add(new Config());
			commands.add(new Forge());
			commands.add(new LoadScript());
			commands.add(new SetHUDString());
			commands.add(new it.fktcod.ktykshrk.command.debugs.Thread());
			//commands.add(new ScriptCommand(scriptCommandName, invokable))
		}
		public static void addCommand(Command s,Object script){
			scriptcommands.put(s,script);
			commands.add(s);

		}

		public void runCommands(String s)
		{
			String readString = s.trim().substring(Character.toString(cmdPrefix).length()).trim();
			boolean commandResolved = false;
			boolean hasArgs = readString.trim().contains(" ");
			String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
			String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];

			for(Command command: commands)
			{	
				if(command.getCommand().trim().equalsIgnoreCase(commandName.trim())) 
				{
					command.runCommand(readString, args);
					commandResolved = true;
					break;
				}
			}
			if(!commandResolved){
				ChatUtils.error("Cannot resolve internal command: \u00a7c"+commandName);
			}
		}
		
		public static CommandManager getInstance(){
			if(instance == null){
				instance = new CommandManager();
			}
			return instance;
		}
	}