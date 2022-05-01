package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Parkour extends Module{
	
	public Parkour() {

		super("Parkour", HackCategory.MOVEMENT);
		this.setChinese(Core.Translate_CN[71]);
	}
	
	@Override
	public String getDescription() {
		return "Jump when reaching a block's edge.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Utils.isBlockEdge(Wrapper.INSTANCE.player()) 
				&& !Wrapper.INSTANCE.player().isSneaking()) 
			Wrapper.INSTANCE.player().jump();
		super.onClientTick(event);
	}
	
}
