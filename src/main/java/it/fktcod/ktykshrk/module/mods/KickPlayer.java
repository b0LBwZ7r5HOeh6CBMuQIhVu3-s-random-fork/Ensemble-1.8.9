package it.fktcod.ktykshrk.module.mods;

import com.mojang.authlib.GameProfile;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.login.client.C00PacketLoginStart;

public class KickPlayer extends Module {
	public static String kickname;

	public KickPlayer() {
		super("KickPlayer", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[57]);
	}

	@Override
	public void onEnable() {
		if (kickname == null)
			return;

		Wrapper.INSTANCE.sendPacket(new C00PacketLoginStart(new GameProfile(
				Wrapper.INSTANCE.player().getUUID(Wrapper.INSTANCE.player().getGameProfile()), kickname)));
		super.onEnable();
	}

}
