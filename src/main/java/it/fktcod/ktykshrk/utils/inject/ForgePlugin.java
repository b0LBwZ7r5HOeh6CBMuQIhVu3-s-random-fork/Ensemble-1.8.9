package it.fktcod.ktykshrk.utils.inject;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;;


// -Dfml.coreMods.load=com.xue.vapu.ForgePlugin -DFORGE_FORCE_FRAME_RECALC=true
@Name("ForgePlugin")
public class ForgePlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"cn.zenwix.ensemble.ClassTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}

