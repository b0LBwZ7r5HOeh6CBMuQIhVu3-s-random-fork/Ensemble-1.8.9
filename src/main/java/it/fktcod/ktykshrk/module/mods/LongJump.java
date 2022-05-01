package it.fktcod.ktykshrk.module.mods;

import com.ibm.icu.impl.duration.TimeUnit;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.PlayerControllerUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class LongJump extends Module {
	Module h = HackManager.getHack("Blink");
	TimerUtils timer = new TimerUtils();
	ModeValue mode;
	NumberValue distance;
	NumberValue blinktime;
	NumberValue y;

	int fstate = 0;
	int mstate = 0;
	int state2 = 0;
	int state3 = 0;
	int jumpstate;
	boolean teststate;
	boolean hasReset;
	boolean ncp;
	boolean hivestate;
	boolean dist;
	
	public LongJump() {
		super("LongJump", HackCategory.MOVEMENT);
		this.mode = new ModeValue("Mode", new Mode("Simple", false), new Mode("Blink", true), new Mode("HYT", false),
				new Mode("Rede", false));
		this.blinktime = new NumberValue("BlinkTime", 500D, 0D, 2000D);
		this.distance = new NumberValue("Distance", 4D, 2D, 8D);
		this.y = new NumberValue("MotionY", 0.4D, 0D, 2D);
		this.addValue(mode, blinktime, distance, y);
		this.setChinese(Core.Translate_CN[61]);

	}

	@Override
	public String getDescription() {
		return "Jump further";
	}

	@Override
	public void onEnable() {
		Utils.nullCheck();
		if (mode.getMode("Simple").isToggled()) {
			Jump();
		} else if (mode.getMode("Blink").isToggled()) {
			h.onEnable();
			h.setToggled(true);
			Jump();

		} else if (mode.getMode("HYT").isToggled()) {
			h.onEnable();
			h.setToggled(true);
			JumpHYT();
			if (timer.hasReached(blinktime.getValue().floatValue())) {
				ChatUtils.message("OK");
				this.onDisable();
				timer.reset();
			}
		}

		// this.setToggled(false);
		super.onEnable();
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		if (mode.getMode("Rede").isToggled()) {
			if (Wrapper.INSTANCE.player().onGround) {
				hClip2(10);
				vClip2(10);
				vClip(2);
				setSpeed(0.5);
			}
		}
		
		super.onPlayerTick(event);
	}

	/*
	 * @Override public boolean onPacket(Object packet, Side side) { if(packet
	 * instanceof S08PacketPlayerPosLook&& jumpstate == 1 &&
	 * Wrapper.INSTANCE.player().onGround) { jumpstate = 0; mc.thePlayer.motionX =
	 * 0; mc.thePlayer.motionZ = 0; } if (packet instanceof S12PacketEntityVelocity
	 * && ((S12PacketEntityVelocity) packet).getEntityID() ==
	 * Wrapper.INSTANCE.player().getEntityId() && hasReset) { hasReset = false;
	 * return false; } return super.onPacket(packet, side); }
	 */
	
	@Override
	public void onDisable() {
		h.onDisable();
		h.setToggled(false);
		super.onDisable();
	}

	public void Jump() {
		if (PlayerControllerUtils.isMoving()) {

			MoveUtils.strafeHYT(distance.getValue().floatValue());
			Wrapper.INSTANCE.player().motionY = y.getValue().floatValue();

			MoveUtils.strafeHYT(distance.getValue().floatValue());
		} else {
			Wrapper.INSTANCE.player().motionX = Wrapper.INSTANCE.player().motionZ = 0D;
		}
	}

	public void JumpHYT() {
		if (PlayerControllerUtils.isMoving()) {

			MoveUtils.strafeHYT(distance.getValue().floatValue());
			Wrapper.INSTANCE.player().motionY = y.getValue().floatValue();

			MoveUtils.strafeHYT(distance.getValue().floatValue());
		} else {
			Wrapper.INSTANCE.player().motionX = Wrapper.INSTANCE.player().motionZ = 0D;
		}
	}
	
	void setSpeed(double speed) {
		double playerYaw = Math.toRadians(Wrapper.INSTANCE.player().rotationYaw);
		mc.thePlayer.motionX = speed * -Math.sin(playerYaw);
		mc.thePlayer.motionZ = speed * Math.cos(playerYaw);
	}
	
	void hClip2(double d) {
		double playerYaw = Math.toRadians(Wrapper.INSTANCE.player().rotationYaw);
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX + d * -Math.sin(playerYaw), Wrapper.INSTANCE.player().posY,Wrapper.INSTANCE.player().posZ + d * Math.cos(playerYaw), false));
	}
	void vClip2(double d) {
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY+d, Wrapper.INSTANCE.player().posZ, false));
	}
	void vClip(double d) {
		mc.thePlayer.setPosition(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + d, Wrapper.INSTANCE.player().posZ);
	}
	
}
