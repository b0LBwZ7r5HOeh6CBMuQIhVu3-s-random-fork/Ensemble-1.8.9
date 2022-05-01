package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.opengl.GL11;

import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

public class WallHack extends Module{
    
	public WallHack() {
		super("WallHack", HackCategory.VISUAL);
		this.setChinese(Core.Translate_CN[102]);
	}
	
	@Override
	public String getDescription() {
		return "The skin of the entities around you glows.";
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
		RenderHelper.enableStandardItemLighting();
		for (Object object : Utils.getEntityList()) {
	    	  Entity entity = (Entity)object;
	    	  this.render(entity, event.partialTicks);
		}
		super.onRenderWorldLast(event);
	}
	
	void render(Entity entity, float ticks) {
		Entity ent = checkEntity(entity);
    	if(ent == null || ent == Wrapper.INSTANCE.player()) return;
    	if (ent == Wrapper.INSTANCE.mc().getRenderViewEntity() 
    			&& Wrapper.INSTANCE.mcSettings().thirdPersonView == 0) return;
    	Wrapper.INSTANCE.mc().entityRenderer.disableLightmap();
		Wrapper.INSTANCE.mc().getRenderManager().renderEntityStatic(ent, ticks, false);
		Wrapper.INSTANCE.mc().entityRenderer.enableLightmap();
    }
	
	Entity checkEntity(Entity e) {
		Entity entity = null;
		Module targets = HackManager.getHack("Targets");
		if(targets.isToggledValue("Players") && e instanceof EntityPlayer) {
			entity = e;
		} else if(targets.isToggledValue("Mobs") && e instanceof EntityLiving) {
			entity = e;
		} else if(e instanceof EntityItem) {
			entity = e;
		} else if(e instanceof EntityArrow) {
			entity = e;
		}
		return entity;	
	}
}
