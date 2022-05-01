package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class AutoReport extends Module {
	public ArrayList names = new ArrayList<String>();

	public AutoReport() {

		super("AutoReport", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[13]);
	}

	@Override
	public void onEnable() {
		Utils.nullCheck();
		fornames();
		super.onEnable();
	}

	public void fornames() {
		for (Entity e : Wrapper.INSTANCE.world().loadedEntityList) {
			if (e instanceof EntityPlayer && !e.getName().equals(Wrapper.INSTANCE.player().getName())) {
				if(e.getName()!=null) {
				Wrapper.INSTANCE.player().sendChatMessage("/report " + e.getName());
				}
			}

		}
	}
}
