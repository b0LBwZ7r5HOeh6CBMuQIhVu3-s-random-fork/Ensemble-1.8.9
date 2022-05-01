package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Glide extends Module{

	public BooleanValue damage;
	public ModeValue mode;
	
    static int tick;
    static boolean fall;
    static int times;
	
	TimerUtils timer;
	
	public Glide() {
		super("Glide", HackCategory.MOVEMENT);
		
		damage = new BooleanValue("SelfDamage", false);
		this.mode = new ModeValue("Mode", new Mode("Falling", true), new Mode("Flat", false));
		
		this.addValue(mode, damage);
		this.setChinese(Core.Translate_CN[48]);
		timer = new TimerUtils();
	}
	
	@Override
	public String getDescription() {
		return "Makes you glide down slowly when falling.";
	}
	
	@Override
	public void onEnable() {
		if(damage.getValue())
			HackManager.getHack("SelfDamage").toggle();
		super.onEnable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		if(mode.getMode("Flat").isToggled()) {
			if (!player.capabilities.isFlying && player.fallDistance > 0.0f && !player.isSneaking()) {
				player.motionY = 0.0;
        	}
        	if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
        		player.motionY = -0.11;
        	}
        	if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
        		player.motionY = 0.11;
        	}
        	if (timer.delay(50)) {
        		player.onGround = false;
        		timer.setLastMS();
        	}
		}
		else if(mode.getMode("Falling").isToggled())
		{
			if (player.onGround) {
                times = 0;
            }
            if (player.fallDistance > 0.0f && times <= 1) {
                if (tick > 0 && fall) {
                	player.motionY = 0.0;
                    tick = 0;
                } else {
                    ++tick;
                }
                if (player.fallDistance >= 0.1) {
                    fall = false;
                }
                if (player.fallDistance >= 0.4) {
                    fall = true;
                    player.fallDistance = 0.0f;
                    
                }
            }
		}
		super.onClientTick(event);
	}

}
