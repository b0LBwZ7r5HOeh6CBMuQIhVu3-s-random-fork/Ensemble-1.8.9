package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FastPlace  extends Module{
	BooleanValue OBlock;

	public FastPlace() {
		super("FastPlace", HackCategory.PLAYER);
		// TODO Auto-generated constructor stub
		OBlock = new BooleanValue("OnlyBlock", true);
		addValue(OBlock);
		this.setChinese(Core.Translate_CN[41]);
	}
	@Override
	public void onClientTick(ClientTickEvent event) {
		if (OBlock.getValue()){
			if(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock){
				Field field = ReflectionHelper.findField(Minecraft.class, new String[] { "rightClickDelayTimer", "field_71467_ac" });
				try {

					if (!field.isAccessible()) {
						field.setAccessible(true);
					}

					field.setInt(Wrapper.INSTANCE.mc(), 0);

				} catch (Exception e) {
					////System.out.println(e);
				}
			}
		}else{
			Field field = ReflectionHelper.findField(Minecraft.class,
					new String[] { "rightClickDelayTimer", "field_71467_ac" });
			try {

				if (!field.isAccessible()) {
					field.setAccessible(true);
				}

				field.setInt(Wrapper.INSTANCE.mc(), 0);

			} catch (Exception e) {
				////System.out.println(e);
			}
		}


	}
	
	@Override
	public void onDisable() {
		Field field = ReflectionHelper.findField(Minecraft.class,
				new String[] { "rightClickDelayTimer", "field_71467_ac" });
		try {

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			field.setInt(Wrapper.INSTANCE.mc(), 4);

		} catch (Exception e) {
			////System.out.println(e);
		}
		super.onDisable();
	}
			
}
