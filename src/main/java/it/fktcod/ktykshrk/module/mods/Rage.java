package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class Rage extends Module{
	
	public TimerUtils timer;
	
	public NumberValue delay;
	
	public Rage() {
		super("Rage", HackCategory.PLAYER);
		
		this.timer = new TimerUtils();
		delay = new NumberValue("Delay", 0.0D, 0.0D, 1000.0D);
		
		this.addValue(delay);
		this.setChinese(Core.Translate_CN[76]);
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(timer.isDelay(delay.getValue().longValue())) {
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(Utils.random(-160, 160), Utils.random(-160, 160), true));
			timer.setLastMS();
		}
		super.onClientTick(event);
	}
	
//	@Override // TODO Added camera fix
//	public void onCameraSetup(CameraSetup event) {
//		// TODO Auto-generated method stub
//		super.onCameraSetup(event);
//	}
}
