package it.fktcod.ktykshrk.module.mods;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.SkinChangerManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class SkinChanger extends Module { // TODO Add cape
	
	//public BooleanValue skin;
	//public BooleanValue cape;
	
	public ResourceLocation defaultSkin;
	//public ResourceLocation defaultCape;
	
	public SkinChanger() {
		super("SkinChanger", HackCategory.VISUAL);
		
		//skin = new BooleanValue("Skin", true);
		//cape = new BooleanValue("Cape", false);
		
		//this.addValue(skin, cape);
		this.setChinese(Core.Translate_CN[84]);
	}
	
	@Override
	public String getDescription() {
		return "Changing your skin/cape. (BETA)";
	}
	
	@Override
	public void onEnable() {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		
		//if(skin.getValue()) {
			if(defaultSkin == null) defaultSkin = player.getLocationSkin();
			ResourceLocation location = SkinChangerManager.playerTextures.get(Type.SKIN);
			if(location != null && !SkinChangerManager.setTexture(Type.SKIN, player, location)) failed();
		//}
		
//		if(cape.getValue()) {
//			if(defaultCape == null) defaultCape = player.getLocationSkin();
//			ResourceLocation location = SkinChangerManager.playerTextures.get(Type.CAPE);
//			if(location != null && !SkinChangerManager.setTexture(Type.CAPE, player, location)) failed();
//		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(defaultSkin != null 
				&& !SkinChangerManager.setTexture(Type.SKIN, Wrapper.INSTANCE.player(),
						defaultSkin)) failed();
//		if((defaultSkin != null && !SkinChangerManager.setTexture(Type.SKIN, Wrapper.INSTANCE.player(),
//				defaultSkin)) || (defaultCape != null && !SkinChangerManager.setTexture(Type.CAPE, Wrapper.INSTANCE.player(),
//						defaultCape))) failed();
		defaultSkin = null; 
		//defaultCape = null;
		super.onDisable();
	}
	void failed() { ChatUtils.error("SkinChanger: Set texture failed!"); }
	
}
