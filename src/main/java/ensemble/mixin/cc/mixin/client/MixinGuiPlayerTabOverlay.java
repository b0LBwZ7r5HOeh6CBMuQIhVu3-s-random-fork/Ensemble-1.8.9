package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Ordering;

import ensemble.mixin.cc.mixin.interfaces.IGuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay implements IGuiPlayerTabOverlay {
	@Shadow
	@Final
	private static Ordering<NetworkPlayerInfo> field_175252_a;

	@Override
	public Ordering<NetworkPlayerInfo> getField() {
		return field_175252_a;
	}

	/*
	 * @Overwrite public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
	 * { String prefix = ""; String result = "";
	 * 
	 * if (networkPlayerInfoIn.getDisplayName() != null) { result = prefix +
	 * networkPlayerInfoIn.getDisplayName().getFormattedText(); } else { result =
	 * prefix +
	 * ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(),
	 * networkPlayerInfoIn.getGameProfile().getName()); }
	 * 
	 * if (ModManager.getModule("NameProtect").getState()) { result =
	 * result.replace(Minecraft.getMinecraft().thePlayer.getName(), "PROTECTION"); }
	 * 
	 * return result; }
	 */
}