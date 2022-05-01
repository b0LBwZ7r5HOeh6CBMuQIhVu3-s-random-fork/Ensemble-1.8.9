package it.fktcod.ktykshrk.script;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.script.api.Values;
import it.fktcod.ktykshrk.utils.FileUtils;
import it.fktcod.ktykshrk.utils.PlayerUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import net.minecraft.client.Minecraft;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.List;


public class CoreScript {
    private ScriptEngine scriptEngine;
    public String name, author, version, category;
    public ScriptModule scriptModule;
    public ScriptCommand scriptCommand;
    public Invocable invoke;

    public CoreScript(File scriptFile){
        //System.out.println("scriptFile " + scriptFile.getPath());
    	
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName("javascript");
  
        // 读入脚本内容
        String scriptContent = FileUtils.readFile(scriptFile);
        invoke = (Invocable) scriptEngine;

        if(scriptEngine==null) {
        	//System.out.println("manager null");
        }
        
        // 先跑一遍脚本 肯定报错 为了获取变量
        try {
            scriptEngine.eval(scriptContent);
        } catch (Exception ignored) {
        	ignored.printStackTrace();
        }

        // 获取必要信息
        this.name = (String) scriptEngine.get("name");
        this.author = (String) scriptEngine.get("author");
        this.version = (String) scriptEngine.get("version");
        this.category = (String) scriptEngine.get("category");
        String type = (String) scriptEngine.get("scriptType");

        if(name==null) {
        	//System.out.println("Null Name");
        }
        
        if (type == null) {
            type = "Module";
        }

        if ("Command".equals(type)) {
            this.registerCommand(name, invoke);
        }

        // 从字符串到ModCategory
        HackCategory modCategory = null;
        try {
            modCategory = HackCategory.valueOf(this.category);
        } catch (Exception e) {
            if (scriptCommand == null) {
                e.printStackTrace();
                ChatUtils.error("失败的操作去加载脚本：" + scriptFile.getAbsolutePath());
                ChatUtils.error("功能分类: " + this.category + " 未找到");
                ChatUtils.error("如果Category填写无误请检查语法错误");
                return;
            }
        }

        if ("Module".equals(type)) {
            //System.out.println("registerModule " + name);
            this.registerModule(name, modCategory, invoke);
        }

        // 传递变量
        if (scriptCommand == null) {
            manager.put("values", new Values(scriptModule));
        }
        manager.put("out", System.out);
        manager.put("mc", Minecraft.getMinecraft());

        // 再次加载 这次不应该出错 如果出错即为加载失败
        try {
            scriptEngine.eval(scriptContent);
        } catch (ScriptException e) {
            e.printStackTrace();
            ChatUtils.error("Failed to load script" + scriptFile.getAbsolutePath());
        }
    }


    public void registerCommand(String commandName, Invocable invocable) {
        scriptCommand = new ScriptCommand(commandName, invocable);
        CommandManager.addCommand(scriptCommand, this);
    }

    public void registerModule(String moduleName, HackCategory category, Invocable invocable){
        scriptModule = new ScriptModule(moduleName, category, invocable);
        Core.hackManager.addPluginModule(scriptModule, this);
        Core.hackManager.sortModules();
    }
}
