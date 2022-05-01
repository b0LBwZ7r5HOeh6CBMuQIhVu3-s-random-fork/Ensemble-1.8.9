package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.input.Mouse;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import it.fktcod.ktykshrk.managers.SkinChangerManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class SkinStealer extends Module{
	
	public EntityPlayer currentPlayer;
	
	public SkinStealer() {
		super("SkinStealer", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[85]);
	}
	
	@Override
	public String getDescription() {
		return "Left click on player - steal skin.";
	}
	
	@Override
	public void onDisable() {
		currentPlayer = null;
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		MovingObjectPosition object = Wrapper.INSTANCE.mc().objectMouseOver;
		if(object == null) return;
		if(object.typeOfHit == MovingObjectType.ENTITY) {
			Entity entity = object.entityHit;
			if(entity instanceof EntityPlayer 
					&& !(entity instanceof EntityArmorStand) 
					&& !Wrapper.INSTANCE.player().isDead 
					&& Wrapper.INSTANCE.player().canEntityBeSeen(entity))
			{
				EntityPlayer player = (EntityPlayer)entity;
				if(Mouse.isButtonDown(0) 
						&& Wrapper.INSTANCE.mc().currentScreen == null 
						&& player != currentPlayer 
						&& player.getGameProfile() != null) 
				{
					SkinChangerManager.addTexture(Type.SKIN, player.getGameProfile().getName());
					currentPlayer = player;
				}

			}
    	}
		super.onClientTick(event);
	}
}
