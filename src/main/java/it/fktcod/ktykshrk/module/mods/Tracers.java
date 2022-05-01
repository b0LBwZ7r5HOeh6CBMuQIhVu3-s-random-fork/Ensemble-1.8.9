package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.managers.FriendManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.utils.visual.RotationUtil;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Tracers extends Module{
    
	public Tracers() {

		super("Tracers", HackCategory.VISUAL);
		this.setChinese(Core.Translate_CN[99]);
	}
	
	@Override
    public String getDescription() {
        return "Traces a line to the players.";
    }
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		for (Object object : Utils.getEntityList()) {
			if(object instanceof EntityLivingBase  && !(object instanceof EntityArmorStand)) {
				EntityLivingBase entity = (EntityLivingBase)object;
	    		  this.render(entity, event.partialTicks);
			}
		}
		super.onRenderWorldLast(event);
	}
	
	void render(EntityLivingBase entity, float ticks) {
		float f =Wrapper.INSTANCE.player().getDistanceToEntity(entity)/ 20F;
		
    	if(ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player()) { 
			return;
    	}
    	if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
    		String ID = Utils.getPlayerName(player);
    		if(EnemyManager.enemysList.contains(ID)) {
    			RenderUtils.drawTracer(entity, 0.8f, 0.3f, 0.0f, 1.0f, ticks);
    			return;
    		}
    		if(FriendManager.friendsList.contains(ID)) {
    			RenderUtils.drawTracer(entity, 0.0f, 0.7f, 1.0f, 1.0f, ticks);
    			return;
    		}
    	}
    	if(HackManager.getHack("Targets").isToggledValue("Murder")) {
    		if(Utils.isMurder(entity)) {
    			RenderUtils.drawTracer(entity, 1.0f, 0.0f, 0.8f, 1.0f, ticks);
        		return;
    		}
    		if(Utils.isDetect(entity)) {
    			RenderUtils.drawTracer(entity, 0.0f, 0.0f, 1.0f, 0.5f, ticks);
        		return;
    		}
		} 
    	if(entity.isInvisible()) {
    		RenderUtils.drawTracer(entity, 2 - f, f, 0, 0.5f, ticks);
			//RenderUtils.drawTracer(entity, 0.0f, 0.0f, 0.0f, 0.5f, ticks);
			return;
    	}
    	if(entity.hurtTime > 0) {
    		RenderUtils.drawTracer(entity, 1.0f, 0.0f, 0.0f, 1.0f, ticks);
    		return;
    	}
    	RenderUtils.drawTracer(entity, 2 - f, f, 0, 0.5f, ticks);
    }
}
