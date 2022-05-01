package it.fktcod.ktykshrk.module.mods;

import java.util.Random;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Derp extends Module {
	public ModeValue mode;
	public static float tempYaw;
	public static float tempPitch;
	
	public Derp() {
		super("Derp", HackCategory.PLAYER);
		this.mode = new ModeValue("Mode", new Mode("All", true), new Mode("In-90", false));
		this.addValue(mode);
		// TODO Auto-generated constructor stub
		this.setChinese(Core.Translate_CN[34]);
	}
	@Override
	public void onClientTick(ClientTickEvent event) {
		// TODO Auto-generated method stub
	 	float f1 = new Random().nextFloat() * 360.0F;
        float f2 = new Random().nextFloat() * 360.0F;
        float f3 = new Random().nextFloat() * 360.0F;
       
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(f1, f2, true));
}
		
}
