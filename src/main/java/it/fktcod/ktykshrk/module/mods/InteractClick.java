package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.input.Mouse;

import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.managers.FriendManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class InteractClick extends Module{

	public InteractClick() {

		super("InteractClick", HackCategory.COMBAT);
		this.setChinese(Core.Translate_CN[52]);
	}
	
	@Override
	public String getDescription() {
		return "Left - Add to Enemys, Rigth - Add to Friends, Wheel - Remove from All.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		MovingObjectPosition object = Wrapper.INSTANCE.mc().objectMouseOver;
		if(object == null) {
			return;
		}
		if(object.typeOfHit == MovingObjectType.ENTITY) {
			Entity entity = object.entityHit;
			if(entity instanceof EntityPlayer && !(entity instanceof EntityArmorStand) && !Wrapper.INSTANCE.player().isDead && Wrapper.INSTANCE.player().canEntityBeSeen(entity)){
				EntityPlayer player = (EntityPlayer)entity;
				String ID = Utils.getPlayerName(player);
				if(Mouse.isButtonDown(1) && Wrapper.INSTANCE.mc().currentScreen == null) 
				{
					FriendManager.addFriend(ID);
				}
				else if(Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().currentScreen == null) 
				{
					EnemyManager.addEnemy(ID);
				}
				else if(Mouse.isButtonDown(2) && Wrapper.INSTANCE.mc().currentScreen == null) 
				{
					EnemyManager.removeEnemy(ID);
					FriendManager.removeFriend(ID);
				}
			}
    	}
		super.onClientTick(event);
	}

}
