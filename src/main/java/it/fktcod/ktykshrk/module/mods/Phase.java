package it.fktcod.ktykshrk.module.mods;

import java.math.BigInteger;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class Phase extends Module {
	ModeValue mode;
	
	public Phase() {
		super("Phase", HackCategory.PLAYER);
		mode=new ModeValue("Mode", new Mode("Basic", false),new Mode("Normal", false),new Mode("AAC4", false));
		addValue(mode);
		this.setChinese(Core.Translate_CN[72]);
	}

	@Override
	public void onEnable() {
		if(mode.getMode("Basic").isToggled()) {
			Utils.nullCheck();
			if (Wrapper.INSTANCE.player().isCollidedHorizontally) {
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Wrapper.INSTANCE.player().posX,
						Wrapper.INSTANCE.player().posY + -0.00000001, Wrapper.INSTANCE.player().posZ,
						Wrapper.INSTANCE.player().rotationYaw, Wrapper.INSTANCE.player().rotationPitch, false));
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Wrapper.INSTANCE.player().posX,
						Wrapper.INSTANCE.player().posY - 1, Wrapper.INSTANCE.player().posZ,
						Wrapper.INSTANCE.player().rotationYaw, Wrapper.INSTANCE.player().rotationPitch, false));

				this.toggle();
			}
		}else if(mode.getMode("Normal").isToggled()) {
			for (int i = 0; i < 10; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX+0.100, mc.thePlayer.posY, mc.thePlayer.posZ+0.100, true));
			}
			this.setToggled(false);
		}else if(mode.getMode("AAC4").isToggled()) {
			mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + -0.00000001, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
			this.setToggled(false);
		}
		super.onEnable();
	}

}
