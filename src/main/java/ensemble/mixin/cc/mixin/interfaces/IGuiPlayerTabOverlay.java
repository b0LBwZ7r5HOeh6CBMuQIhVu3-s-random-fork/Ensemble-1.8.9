package ensemble.mixin.cc.mixin.interfaces;

import com.google.common.collect.Ordering;

import net.minecraft.client.network.NetworkPlayerInfo;

public interface IGuiPlayerTabOverlay {
    Ordering<NetworkPlayerInfo> getField();
}
