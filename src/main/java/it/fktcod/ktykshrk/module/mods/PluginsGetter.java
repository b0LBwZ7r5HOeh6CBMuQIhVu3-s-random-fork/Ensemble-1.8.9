package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import joptsimple.internal.Strings;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class PluginsGetter extends Module{
    
	public PluginsGetter() {
		super("PluginsGetter", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[74]);
	}

	
	@Override
	public String getDescription() {
		return "Show all plugins on current server.";
	}
	
	@Override
	public void onEnable() {
		if(Wrapper.INSTANCE.player() == null) {
            return;
		}
        Wrapper.INSTANCE.sendPacket(new C14PacketTabComplete("/"));
		super.onEnable();
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		 if(packet instanceof S3APacketTabComplete) {
	            S3APacketTabComplete s3APacketTabComplete = (S3APacketTabComplete) packet;
	 
	            List<String> plugins = new ArrayList<String>();
	            String[] commands = s3APacketTabComplete.func_149630_c();
	 
	            for(int i = 0; i < commands.length; i++) {
	                String[] command = commands[i].split(":");
	 
	                if(command.length > 1) {
	                    String pluginName = command[0].replace("/", "");
	 
	                    if(!plugins.contains(pluginName)) {
	                        plugins.add(pluginName);
	                    }
	                }
	            }
	            
	            Collections.sort(plugins);
	            
	            if(!plugins.isEmpty()) {
	                ChatUtils.message("Plugins \u00a77(\u00a78" + plugins.size() + "\u00a77): \u00a79" + Strings.join(plugins.toArray(new String[0]), "\u00a77, \u00a79"));
	            }
	            else
	            {
	                ChatUtils.error("No plugins found.");
	            }
	            this.setToggled(false);   
	        }
		return true;
	}
}
