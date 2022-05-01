package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.utils.AbuseUtil;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoL extends Module {

	public static BooleanValue clientname;
	public static BooleanValue abuse;

	public AutoL() {
		super("AutoL", HackCategory.ANOTHER);

		clientname = new BooleanValue("ClientName", true);
		abuse = new BooleanValue("Abuse", true);

		this.addValue(clientname, abuse);
		this.setChinese(Core.Translate_CN[103]);
	}

	public static String getAutoLMessage(String PlayerName) {
		String abuse = "";
		abuse = AbuseUtil.getAbuseGlobal();

		return "" + (AutoL.clientname.getValue() ? "[" + "ENSEMBLE" + "] " : "") + PlayerName + " [L]"
				+ (AutoL.abuse.getValue() ? " " + abuse : "");
	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (side == Side.IN) {

			if (packet instanceof S02PacketChat) {

				S02PacketChat p = (S02PacketChat) packet;

				String m = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
				// String m = ((S02PacketChat) packet).getChatComponent().getFormattedText()
				// ChatUtils.message(m.toLowerCase());
				String[] strs = m.split(" ");

				// redesky
				if (m.contains("foi morto por " + Wrapper.INSTANCE.player().getName())) {
					Wrapper.INSTANCE.player().sendChatMessage(AutoL.getAutoLMessage(strs[0]));

				}
				// mineland
				if (m.contains("was slain by " + Wrapper.INSTANCE.player().getName())
						|| m.contains("was thrown out of the world by " + Wrapper.INSTANCE.player().getName())) {

					Wrapper.INSTANCE.player().sendChatMessage(AutoL.getAutoLMessage(strs[0]));
				}

				//blocksmc
				if (m.contains("was killed by " + Wrapper.INSTANCE.player().getName())) {
					Wrapper.INSTANCE.player().sendChatMessage(AutoL.getAutoLMessage(strs[0]));

				}
				//[!] Olnigans asesino a Olnigans   minemora
				if(m.contains(Wrapper.INSTANCE.player().getName()+" asesino a"))
					Wrapper.INSTANCE.player().sendChatMessage(AutoL.getAutoLMessage(strs[4]));
				
				//msj
				if(m.contains("����ɱ����ɱ�ߣ� "+Wrapper.INSTANCE.player().getName()))
					Wrapper.INSTANCE.player().sendChatMessage("@"+AutoL.getAutoLMessage(strs[0]));
			}

		}
		return true;
	}

}
