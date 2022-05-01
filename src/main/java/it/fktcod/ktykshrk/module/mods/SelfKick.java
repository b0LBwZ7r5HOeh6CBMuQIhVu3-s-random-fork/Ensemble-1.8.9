package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class SelfKick extends Module{
	
	public SelfKick() {

		super("SelfKick", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[82]);
	}
	
	@Override
	public String getDescription() {
		return "Kick you from Server.";
	}
	
	@Override
	public void onEnable() {
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
		super.onEnable();
	}
}
