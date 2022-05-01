package it.fktcod.ktykshrk.managers;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.command.Command;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.script.CoreScript;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import javax.script.ScriptException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class ScriptManager {
    public List<CoreScript> scripts;

    public ScriptManager(){
        this.loadScripts();
    }

    /**
     * 该函数与插件系统共用一个文件夹
     */
    public void loadScripts(){
        File clientDir = FileManager.SCRIPT;
       // File scriptDir = new File(clientDir, "Plugins");

        File[] scriptsFiles = clientDir.listFiles((dir, name) -> name.endsWith(".js"));

        if (scriptsFiles == null) {
            return;
        }

        scripts = new ArrayList<>();
        for (File scriptFile : scriptsFiles) {
            CoreScript script = new CoreScript(scriptFile);
            scripts.add(script);
        }
    }

    public boolean isScriptEnabled(CoreScript script){
        for (Object value : Core.hackManager.pluginModsList.values()) {
            if (value instanceof CoreScript) {
                if (value.equals(script)) {
                    return true;
                }
            }
        }

        for (Object value : CommandManager.scriptcommands.values()) {
            if (value instanceof CoreScript) {
                if (value.equals(script)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setScriptState(CoreScript script, boolean state){
        AtomicReference<Module> tempMod = new AtomicReference<>();
        AtomicReference<Command> tempCmd = new AtomicReference<>();

        if (state) {
            Core.hackManager.disabledPluginList.forEach((mod, value) -> {
                if (value instanceof CoreScript) {
                    if (value.equals(script)) {
                        tempMod.set(mod);
                    }
                }
            });

            if (tempMod.get() != null) {
                Core.hackManager.disabledPluginList.remove(tempMod.get());
                Core.hackManager.pluginModsList.put(tempMod.get(), script);
                Core.hackManager.modules.add(tempMod.get());
            }


            CommandManager.disabledPluginCommands.forEach((cmd, value) -> {
                if (value instanceof CoreScript) {
                    if (value.equals(script)) {
                        tempCmd.set(cmd);
                    }
                }
            });

            CommandManager.disabledPluginCommands.remove(tempCmd.get());
            CommandManager.scriptcommands.put(tempCmd.get(), script);
            CommandManager.commands.add(tempCmd.get());
        } else {
            Core.hackManager.pluginModsList.forEach((mod, value) -> {
                if (value instanceof CoreScript) {
                    if (value.equals(script)) {
                        tempMod.set(mod);
                    }
                }
            });

            if (tempMod.get() != null) {
                Core.hackManager.pluginModsList.remove(tempMod.get());
                Core.hackManager.modules.remove(tempMod.get());
                Core.hackManager.disabledPluginList.put(tempMod.get(), script);
            }

            CommandManager.scriptcommands.forEach((cmd, value) -> {
                if (value instanceof CoreScript) {
                    if (value.equals(script)) {
                        tempCmd.set(cmd);
                    }
                }
            });

            CommandManager.scriptcommands.remove(tempCmd.get());
            CommandManager.commands.remove(tempCmd.get());
            CommandManager.disabledPluginCommands.put(tempCmd.get(), script);
        }
        Core.hackManager.sortModules();
    }

    public void unloadScript(String scriptName){
        Command removeCommand = null;
        Module removeModule = null;
        CoreScript removeScript = null;

        for (Command command : CommandManager.commands) {
            if(command.getCommand().equals(scriptName)){
                removeCommand = command;
            }
        }

        for (Module mod : Core.hackManager.modules) {
            if (mod.getName().equals(scriptName)) {
                removeModule = mod;
            }
        }

        for (CoreScript script : this.scripts) {
            if (script.name.equals(scriptName)) {
                removeScript = script;
            }
        }

        if (removeCommand != null) {
            CommandManager.commands.remove(removeCommand);
            CommandManager.scriptcommands.remove(removeCommand);
        }
        if (removeModule != null) {
            Core.hackManager.modules.remove(removeModule);
            Core.hackManager.pluginModsList.remove(removeModule);
        }
        if (removeScript != null) {
            this.scripts.remove(removeScript);
        }
    }


    public void onClientStart(Core Core) {
        for (CoreScript script : scripts) {
            try {
                script.invoke.invokeFunction("onClientStart", Core);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException ignored) { }
        }
    }

    public void onClientStop(Core Core) {
        for (CoreScript script : scripts) {
            try {
                script.invoke.invokeFunction("onClientStop", Core);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException ignored) { }
        }
    }
}