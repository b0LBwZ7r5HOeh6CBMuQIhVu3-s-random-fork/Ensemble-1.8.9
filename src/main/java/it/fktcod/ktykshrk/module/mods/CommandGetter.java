package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.eventapi.EventTarget;
import it.fktcod.ktykshrk.irc.IRCChat;
import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class CommandGetter extends Module {
	public CommandGetter() {
		super("CommandGetter", HackCategory.ANOTHER);
		setToggled(true);
		setShow(false);
		this.setChinese(Core.Translate_CN[31]);
	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		boolean send=true;
		if (side == Side.OUT) {
			if (packet instanceof C01PacketChatMessage) {

				Field field = ReflectionHelper.findField(C01PacketChatMessage.class,
						new String[] { "message", "field_149440_a" });

				try {

					if (!field.isAccessible()) {
						field.setAccessible(true);
					}

					if (packet instanceof C01PacketChatMessage) {
						C01PacketChatMessage p = (C01PacketChatMessage) packet;
						if (p.getMessage().subSequence(0, 1).equals(".")) {
							send=false;
							CommandManager.getInstance().runCommands(p.getMessage());
							return send;
						}else {
							send=true;
						}
					}

				} catch (Exception e) { // e.printStackTrace();

				}

			}
		}
		return send;
	}
	

	
	

}
