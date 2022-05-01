package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.opengl.GL11;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
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

public class ItemESP extends Module{
	
	public ItemESP()
	{
		super("ItemESP", HackCategory.VISUAL);
		this.setChinese(Core.Translate_CN[54]);
	}
	
	@Override
	public String getDescription() {
		return "Highlights nearby items.";
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		for (Object object : Utils.getEntityList()) {
			if(object instanceof EntityItem || object instanceof EntityArrow) {
				Entity item = (Entity)object;
				RenderUtils.drawESP(item, 1.0f, 1.0f, 1.0f, 1.0f, event.partialTicks);
			}
		}
		super.onRenderWorldLast(event);
	}
}
