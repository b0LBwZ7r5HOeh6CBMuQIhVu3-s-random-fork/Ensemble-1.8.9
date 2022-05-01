package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EnseChat extends Module {
	public EnseChat() {
		super("EnseChat", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[38]);
	}

	@Override
	public boolean onPacket(Object packet, Side side) {

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
						if (!p.getMessage().subSequence(0, 1).equals("/")
								&& !p.getMessage().subSequence(0, 1).equals("+")) {

							String chat = "[ENSEMBLE] " + p.getMessage();
							field.set(p, chat);
						}
					}

				} catch (Exception e) {

				}

				return true;

			}
		}
		return super.onPacket(packet, side);
	}

}
