package it.fktcod.ktykshrk.module.mods;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.managers.FriendManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AimBot extends Module{

	public ModeValue mode;
	public ModeValue priority;
    public BooleanValue walls;
    public BooleanValue weapons;
    
    public static NumberValue yaw;
    public static NumberValue pitch;
    public NumberValue range;
    public NumberValue FOV;
    public NumberValue x;
    public NumberValue y;
    public NumberValue horizontal;
    public NumberValue vertical;
    
    public EntityLivingBase target;
    
	public AimBot() {
		super("AimBot", HackCategory.COMBAT);
		this.priority = new ModeValue("Priority", new Mode("Closest", true), new Mode("Health", false));

		walls = new BooleanValue("ThroughWalls", false);
		
		yaw = new NumberValue("Yaw", 15.0D, 0D, 50D);
		pitch = new NumberValue("Pitch", 15.0D, 0D, 50D);
		range = new NumberValue("Range", 4.7D, 0.1D, 10D);
		FOV = new NumberValue("FOV", 90D, 1D, 180D);
		weapons=new BooleanValue("Weapons", true);
		this.addValue(priority, walls, yaw, pitch, range, FOV,weapons);
        this.setChinese(Core.Translate_CN[0]);
	}
	
	@Override
	public String getDescription() {
		return "Automatically points towards player.";
	}

    @Override
	public void onDisable() {
		this.target = null;
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		updateTarget();
		 if (Wrapper.INSTANCE.player().getHeldItem() == null || Wrapper.INSTANCE.player().getHeldItem().getItem() == null) {
             return;
         }

         final Item heldItem = Wrapper.INSTANCE.player().getHeldItem().getItem();

         if (weapons.getValue() && heldItem != null) {
             if (!(heldItem instanceof ItemSword)) {
                 return;
             }
         }
		Utils.assistFaceEntity(
				this.target, 
				this.yaw.getValue().floatValue(),
				this.pitch.getValue().floatValue());
		this.target = null;
			
		
		super.onClientTick(event);
	}
	private void stepAngle()
    {
        float yawFactor = horizontal.getValue().floatValue();
        float pitchFactor = vertical.getValue().floatValue();
        double xz = x.getValue();
        double y1 = y.getValue();
        float targetYaw = Utils.getYawChange(Wrapper.INSTANCE.player().rotationYaw, target.posX + randomNumber() * xz, target.posZ + randomNumber() * xz);

        if (targetYaw > 0 && targetYaw > yawFactor)
        {
            Wrapper.INSTANCE.player().rotationYaw += yawFactor;
        }
        else if (targetYaw < 0 && targetYaw < -yawFactor)
        {
            Wrapper.INSTANCE.player().rotationYaw -= yawFactor;
        }
        else
        {
            Wrapper.INSTANCE.player().rotationYaw += targetYaw;
        }

        float targetPitch = Utils.getPitchChange(Wrapper.INSTANCE.player().rotationPitch, target, target.posY + randomNumber() * y1);

        if (targetPitch > 0 && targetPitch > pitchFactor)
        {
            Wrapper.INSTANCE.player().rotationPitch += pitchFactor;
        }
        else if (targetPitch < 0 && targetPitch < -pitchFactor)
        {
            Wrapper.INSTANCE.player().rotationPitch -= pitchFactor;
        }
        else
        {
            Wrapper.INSTANCE.player().rotationPitch += targetPitch;
        }
    }
    private int randomNumber()
    {
        return -1 + (int)(Math.random() * ((1 - (-1)) + 1));
    }
	void updateTarget(){
		for (Object object : Utils.getEntityList()) {
			if(!(object instanceof EntityLivingBase)) continue;
			EntityLivingBase entity = (EntityLivingBase) object;
			if(!check(entity)) continue;
			this.target = entity;
		}
	}
	
	public boolean check(EntityLivingBase entity) {
		if(entity instanceof EntityArmorStand) { return false; }
		if(ValidUtils.isValidEntity(entity)){ return false; }
		if(!ValidUtils.isNoScreen()) { return false; }
		if(entity == Wrapper.INSTANCE.player()) { return false; }
		if(entity.isDead) { return false; }
		if(ValidUtils.isBot(entity)) { return false; }
		if(!ValidUtils.isFriendEnemy(entity)) { return false; }
    	if(!ValidUtils.isInvisible(entity)) { return false; }
    	if(!ValidUtils.isInAttackFOV(entity, FOV.getValue().intValue())) { return false; }
		if(!ValidUtils.isInAttackRange(entity, range.getValue().floatValue())) { return false; }
		if(!ValidUtils.isTeam(entity)) { return false; }
    	if(!ValidUtils.pingCheck(entity)) { return false; }
    	if(!isPriority(entity)) { return false; }
		if(!this.walls.getValue()) { if(!Wrapper.INSTANCE.player().canEntityBeSeen(entity)) { return false; } }
		return true;
    }

	boolean isPriority(EntityLivingBase entity) {
		return priority.getMode("Closest").isToggled() && ValidUtils.isClosest(entity, target) 
				|| priority.getMode("Health").isToggled() && ValidUtils.isLowHealth(entity, target);
	}
	
	private EntityLivingBase getBestEntity()
    {
        List<EntityLivingBase> loaded = new CopyOnWriteArrayList<>();

        for (Object o : Wrapper.INSTANCE.world().getLoadedEntityList())
        {
            if (o instanceof EntityLivingBase)
            {
                EntityLivingBase ent = (EntityLivingBase) o;

                if (ent.isEntityAlive() && ent instanceof EntityPlayer && ent
                        .getDistanceToEntity(Wrapper.INSTANCE.player()) < range.getValue()
                        && fovCheck(ent))
                {
                    /*if (ent == Killaura.vip)
                    {
                        return ent;
                    }*/

                    loaded.add(ent);
                }
            }
        }

        if (loaded.isEmpty())
        {
            return null;
        }

        try
        {
            loaded.sort((o1, o2) ->
            {
                float[] rot1 = Utils.getRotations(o1);
                float[] rot2 = Utils.getRotations(o2);
                return (int)((Utils.getDistanceBetweenAngles( Wrapper.INSTANCE.player().rotationYaw, rot1[0])
                        + Utils.getDistanceBetweenAngles( Wrapper.INSTANCE.player().rotationPitch, rot1[1]))
                        - (Utils.getDistanceBetweenAngles( Wrapper.INSTANCE.player().rotationYaw, rot2[0])
                        + Utils.getDistanceBetweenAngles( Wrapper.INSTANCE.player().rotationPitch, rot2[1])));
            });
        }
        catch (Exception e)
        {
            ChatUtils.error("Exception with TM: " + e.getMessage());
        }

        return loaded.get(0);
    }
	 private boolean fovCheck(EntityLivingBase ent)
	    {
	        float[] rotations = Utils.getRotations(ent);
	        float dist = Wrapper.INSTANCE.player().getDistanceToEntity(ent);

	        if (dist == 0)
	        {
	            dist = 1;
	        }

	        float yawDist = Utils.getDistanceBetweenAngles( Wrapper.INSTANCE.player().rotationYaw, rotations[0]);
	        float pitchDist = Utils.getDistanceBetweenAngles( Wrapper.INSTANCE.player().rotationPitch, rotations[1]);
	        float fovYaw = yaw.getValue().floatValue() * 3 / dist;
	        float fovPitch = (pitch.getValue().floatValue() * 3) / dist;
	        return yawDist < fovYaw && pitchDist < fovPitch;
	    }

}
