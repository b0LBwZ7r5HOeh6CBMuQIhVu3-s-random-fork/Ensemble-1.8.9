package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class KillerMark extends Module {

	private EntityLivingBase target;
	private EntityLivingBase var;

	public KillerMark() {
		super("KillerMark", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[60]);
	}

	@Override
	public void onEnable() {
		if (var != null) {
			EnemyManager.removeEnemy(var.getName());
		}
		target = null;
		super.onEnable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		Utils.nullCheck();
		TargetUpdate();
		super.onClientTick(event);
	}

	@Override
	public void onDisable() {
		if (var != null) {
			EnemyManager.removeEnemy(var.getName());
		}
		target = null;
		super.onDisable();
	}

	void TargetUpdate() {
		if (Utils.getEntityList() == null) {
			return;
		}
		for (Object object : Utils.getEntityList()) {
			if (object == null) {
				return;
			}
			if (!(object instanceof EntityLivingBase))
				continue;
			EntityLivingBase entity = (EntityLivingBase) object;
			if (entity instanceof EntityPlayer && entity.getHeldItem() != null&&entity.getHeldItem().getItem()!=null) {
	

				if (entity.getHeldItem().getItem() instanceof ItemSword
						&& entity.getName() != Wrapper.INSTANCE.player().getName()) {
					target = entity;
					var = target;
					// ChatUtils.message(target.getName());
				}
			}
		}
		if (target != null) {
			EnemyManager.addEnemy(target.getName());
			target = null;
		}

	}

}
