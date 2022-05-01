package it.fktcod.ktykshrk.script;

import javax.script.Invocable;
import javax.script.ScriptException;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.command.Command;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;


public class ScriptCommand extends Command {
    private Invocable invoke;
    private String name;

    public ScriptCommand(String scriptCommandName, Invocable invokable){
        super(scriptCommandName);
        this.name = scriptCommandName;
        this.invoke = invokable;
    }

    @Override
    public void runCommand(String s, String[] args) {
        try {
            invoke.invokeFunction("run", args);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Core.scriptManager.unloadScript(this.name);
            ChatUtils.error("脚本" + this.name + "没有必要的函数，已自动卸载");
        }
    }

    @Override
    public String getDescription() {
        return "Script Command";
    }

    @Override
    public String getSyntax() {
        return "Script Command";
    }
}
