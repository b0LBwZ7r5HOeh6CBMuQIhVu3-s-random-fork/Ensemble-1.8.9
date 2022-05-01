package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;

public class Diana extends Module {
	int id = 1;
	TimerUtils timer=new TimerUtils();
	private ArrayList<ResourceLocation> list = new ArrayList<ResourceLocation>();

	public Diana() {
		super("Diana", HackCategory.VISUAL);
		this.setChinese(Core.Translate_CN[35]);
		for (int i = 1; i <52; i++) {
			list.add(new ResourceLocation(Core.MODID, "diana/Frame" + i + ".png"));

		}
	}

	
	@Override
	public void onEnable() {
	
		super.onEnable();
	}
	
	@Override
	public void onRenderGameOverlay(Post event) {
		if (event.type != ElementType.ALL || mc.gameSettings.showDebugInfo)
			return;
		if(timer.hasReached(100)){
		id++;
		timer.reset();
		}
		if (id == 52) {
			id = 1;
		}
		//ChatUtils.message(list.size());
		mc.getTextureManager().bindTexture(list.get(id));
		GL11.glColor4f(1, 1, 1, 1);
		ScaledResolution sr = new ScaledResolution(mc);
		int x = sr.getScaledWidth() / 2 + 44;
		int y = sr.getScaledHeight() - 51;

		Gui.drawModalRectWithCustomSizedTexture(100, 10, 0, 0, 295, 282, 295, 282);

		super.onRenderGameOverlay(event);
	}

}
