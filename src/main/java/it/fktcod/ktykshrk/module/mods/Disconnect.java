package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Disconnect extends Module{

	public NumberValue leaveHealth;
	
	public Disconnect() {
		super("Disconnect", HackCategory.COMBAT);
		
		leaveHealth = new NumberValue("LeaveHealth", 4.0D, 0D, 20D);
		
		this.addValue(leaveHealth);
		this.setChinese(Core.Translate_CN[36]);
	}
	
	@Override
	public String getDescription() {
		return "Automatically leaves the server when your health is low.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Wrapper.INSTANCE.player().getHealth() <= leaveHealth.getValue().floatValue()) {
			
			boolean flag = Wrapper.INSTANCE.mc().isIntegratedServerRunning();
			Wrapper.INSTANCE.world().sendQuittingDisconnectingPacket();
			Wrapper.INSTANCE.mc().loadWorld((WorldClient)null);
			
            if (flag)
            	Wrapper.INSTANCE.mc().displayGuiScreen(new GuiMainMenu()); else
            	Wrapper.INSTANCE.mc().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            this.setToggled(false);
		}
		super.onClientTick(event);
	}
}
