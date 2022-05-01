package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.RobotUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FireballReturn extends Module{
	
    public NumberValue yaw;
    public NumberValue pitch;
    public NumberValue range;
    
	public EntityFireball target;
	public TimerUtils timer;
	
	public FireballReturn() {
		super("FireballReturn", HackCategory.COMBAT);
		yaw = new NumberValue("Yaw", 25.0D, 0D, 50D);
		pitch = new NumberValue("Pitch", 25.0D, 0D, 50D);
		range = new NumberValue("Range", 10.0D, 0.1D, 10D);
		this.setChinese(Core.Translate_CN[43]);
		this.addValue(yaw, pitch, range);
		
		this.timer = new TimerUtils();
	}
	
	@Override
	public String getDescription() {
		return "Beats fireballs when they fly at you.";
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		updateTarget();
		attackTarget();
		super.onClientTick(event);
	}

	void updateTarget(){
		for (Object object : Utils.getEntityList()) {
			if(object instanceof EntityFireball) {
				EntityFireball entity = (EntityFireball) object;
				if(isInAttackRange(entity) && !entity.isDead && !entity.onGround && entity.canAttackWithItem()) {
					target = entity;
				}
			}
		}
	}
	
	void attackTarget() {
		if(target == null) {
			return;
		}
		Utils.assistFaceEntity(this.target, this.yaw.getValue().floatValue(), this.pitch.getValue().floatValue());
		int currentCPS = Utils.random(4, 7);
		if(timer.isDelay(1000 / currentCPS)) {
			RobotUtils.clickMouse(0);
			timer.setLastMS();
			target = null;
		}
	}
	
	public boolean isInAttackRange(EntityFireball entity) {
        return entity.getDistanceToEntity(Wrapper.INSTANCE.player()) <= this.range.getValue().floatValue();
    }
	
}
