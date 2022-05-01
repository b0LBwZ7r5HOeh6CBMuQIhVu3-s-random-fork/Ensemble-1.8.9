package it.fktcod.ktykshrk.utils;


import it.fktcod.ktykshrk.command.SetTeamSign;
import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.managers.FriendManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.AntiBot;
import it.fktcod.ktykshrk.utils.visual.RotationUtil;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

public class ValidUtils {
	
	public static boolean isLowHealth(EntityLivingBase entity, EntityLivingBase entityPriority) {
		return entityPriority == null || entity.getHealth() < entityPriority.getHealth();
	}
	
	public static boolean isClosest(EntityLivingBase entity, EntityLivingBase entityPriority) {
		return entityPriority == null || Wrapper.INSTANCE.player().getDistanceToEntity(entity) < Wrapper.INSTANCE.player().getDistanceSqToEntity(entityPriority);
	}
	
    public static boolean isInAttackFOV(EntityLivingBase entity, int fov) {
        return Utils.getDistanceFromMouse(entity) <= fov;
    }
    
    public static boolean isInAttackRange(EntityLivingBase entity, float range) {
        return entity.getDistanceToEntity(it.fktcod.ktykshrk.wrappers.Wrapper.INSTANCE.player()) <= range;
    }
    
    public static boolean isFov(EntityLivingBase entity, EntityLivingBase entityPriority) {
    	return entityPriority==null||RotationUtil
				.getDistanceBetweenAngles(Wrapper.INSTANCE.player().rotationPitch, RotationUtil.getRotations(entity)[0])<RotationUtil
						.getDistanceBetweenAngles(Wrapper.INSTANCE.player().rotationPitch, RotationUtil.getRotations(entityPriority)[0]);
    }
    
    public static boolean isArmor(EntityLivingBase entity, EntityLivingBase entityPriority) {
    	return  entityPriority==null||(entity instanceof EntityPlayer ? ((EntityPlayer) entity).inventory.getTotalArmorValue()
				: (int) entity.getHealth())< (entityPriority instanceof EntityPlayer ? ((EntityPlayer) entityPriority).inventory.getTotalArmorValue()
						: (int) entityPriority.getHealth());
    }
    
	/*
	 * public static boolean isAngle(EntityLivingBase entity, EntityLivingBase
	 * entityPriority) { return entityPriority==null|| () }
	 */
    public static boolean isValidEntity(EntityLivingBase e) {
		Module targets = HackManager.getHack("Targets");
		if(targets.isToggled()) {
			if(targets.isToggledValue("Players") && e instanceof EntityPlayer) {
				return false;
			} 
			else 
			if(targets.isToggledValue("Mobs") && e instanceof EntityLiving) {
				return false;
			}
		}
		return true;	
	}
    
	
	public static boolean pingCheck(EntityLivingBase entity) {
		Module module = HackManager.getHack("AntiBot");
		if(module.isToggled() && module.isToggledValue("PingCheck") && entity instanceof EntityPlayer) {
			if (Wrapper.INSTANCE.mc().getNetHandler().getPlayerInfo(entity.getUniqueID()) != null) {
				if (Wrapper.INSTANCE.mc().getNetHandler().getPlayerInfo(entity.getUniqueID()).getResponseTime() > 5) {
					return true;
				}
			}
        	return false;
		}
		return true;
	}
    
    public static boolean isBot(EntityLivingBase entity) {
    	if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
    		Module module = HackManager.getHack("AntiBot");
			return module.isToggled() && AntiBot.isBot(player);
    	}
    	return false;
    }
    
	public static boolean isFriendEnemy(EntityLivingBase entity) {
		if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
    		String ID = Utils.getPlayerName(player);
    		if(FriendManager.friendsList.contains(ID)) {
    			return false;
    		}
    		if(HackManager.getHack("Enemys").isToggled()) {
    			if(!EnemyManager.enemysList.contains(ID)) {
    				return false;
    			}
    		}
    	}
		return true;
	}
	public static boolean isTeam(EntityLivingBase entity) {
		Module teams = HackManager.getHack("Teams");
		if(teams.isToggled()) {
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if(teams.isToggledMode("Base")) {
					if(player.getTeam() != null && Wrapper.INSTANCE.player().getTeam() != null) {
						if(player.getTeam().isSameTeam(Wrapper.INSTANCE.player().getTeam())){
							return false;
						}
					}
				}
				if(teams.isToggledMode("ArmorColor")) {
					if(!Utils.checkEnemyColor(player)) {
						return false;
					}
				}
				if(teams.isToggledMode("NameColor")) {
					if(!Utils.checkEnemyNameColor(player)) {
						return false;
					}
				}
				if (teams.isToggledMode("TabList")){

					Collection<NetworkPlayerInfo> list = Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap();
					for (NetworkPlayerInfo networkplayerinfo : list)
					{;
						if (entity.getName() == Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkplayerinfo)){
							if (!Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkplayerinfo).contains(SetTeamSign.teamsign)){
								return false;
							}else{
								return true;
							}
						}
					}

				}
			}
		}
		return true;
	}
	public static boolean isInvisible(EntityLivingBase entity) {
		Module targets = HackManager.getHack("Targets");
		if(!targets.isToggledValue("Invisibles")) {
			if(entity.isInvisible()) {
				return false;
			}
		}
		return true;
	}
	public static boolean isNoScreen() {
		if(HackManager.getHack("NoGuiEvents").isToggled()) {
			if(!Utils.screenCheck()) {
				return false;
			}
		}
		return true;
	}
}
