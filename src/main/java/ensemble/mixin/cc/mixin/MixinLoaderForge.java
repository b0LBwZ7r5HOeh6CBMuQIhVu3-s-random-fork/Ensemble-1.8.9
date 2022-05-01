package ensemble.mixin.cc.mixin;

import java.io.File;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;


import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import ensemble.mixin.cc.mixin.transform.MemoryTransformer;

import java.util.Map;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class MixinLoaderForge implements IFMLLoadingPlugin {

    private static boolean isObfuscatedEnvironment = false;

    public MixinLoaderForge() {
    
        MixinBootstrap.init();
        Mixins.addConfiguration("ensemble.forge.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");

    }

    @Override
    public String[] getASMTransformerClass() {
    	return new String[]{
    			MemoryTransformer.class.getName()
        };
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
        isObfuscatedEnvironment = (boolean) (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

	public static void main(String[] args) {
		
	}
}