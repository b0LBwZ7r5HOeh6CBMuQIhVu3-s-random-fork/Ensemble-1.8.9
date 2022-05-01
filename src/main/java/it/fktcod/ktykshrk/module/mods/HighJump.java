package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class HighJump extends Module {
	TimerUtils timer = new TimerUtils();


	NumberValue horizontalValue;
	NumberValue verticalValue;

	public HighJump() {
		super("HighJump", HackCategory.MOVEMENT);
		horizontalValue = new NumberValue("Horizontal", 0D, 0D, 10D);
		verticalValue = new NumberValue("Vertical", 5D, 0D, 10D);
		this.addValue(horizontalValue, verticalValue);
		this.setChinese(Core.Translate_CN[49]);
	}

	@Override
	public void onEnable() {
		Utils.nullCheck();
		Jump();
		this.toggle();
		super.onEnable();
		
	}



	public void Jump() {
		double yaw = Math.toRadians(Wrapper.INSTANCE.player().rotationYaw);
		final double x = -Math.sin(yaw) * horizontalValue.getValue();
		final double z = Math.cos(yaw) * horizontalValue.getValue();

		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX,
				Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, true));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX,
				Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, true));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX + x,
				Wrapper.INSTANCE.player().posY + verticalValue.getValue(), Wrapper.INSTANCE.player().posZ + z, true));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX + 0.5,
				Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + 0.5, true));

		Wrapper.INSTANCE.player().setPosition(Wrapper.INSTANCE.player().posX + -Math.sin(yaw) * 0.04,
				Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + Math.cos(yaw) * 0.04);
	}
}
