package it.fktcod.ktykshrk.module.mods.addon;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventMotion;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.utils.PlayerControllerUtils;
import it.fktcod.ktykshrk.utils.PlayerUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class JigFly extends Module {
	public Timer mctimer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[] { Mappings.timer });

	int wait = 6;
	double MACvelY = 0.02;
	double startingHeight;
	double fallSpeed = 0.05;
	double maxY;
	boolean damaging = false;
	private TimerUtils timer = new TimerUtils();
	public double flyHeight;
	private boolean aac;
	private double aad;
	boolean Up = false;
	boolean Start = false;
	private TimerUtils cubeTimer = new TimerUtils();
	private TimerUtils hypixelTimer = new TimerUtils();

	boolean simulateFall = false;
	// settings
	public ModeValue mode;
	public BooleanValue hypixelFlightOffsetOnEnable;
	public BooleanValue glideDmg;
	public BooleanValue Flightsmooth;
	public NumberValue FlightdefaultSpeed;
	public BooleanValue flightkick;

	// Skid from Jigsaw Client 2021/8/17

	public JigFly() {
		super("JigFly", HackCategory.MOVEMENT);
		mode = new ModeValue("Mode", new Mode("AAC", false), new Mode("AAC2", false), new Mode("Hypixel", true),
				new Mode("AAC3", false), new Mode("AirWalk", false), new Mode("Default", false), new Mode("MAC", false),
				new Mode("Glide", false));
		hypixelFlightOffsetOnEnable = new BooleanValue("HypixelOffset", true);
		glideDmg = new BooleanValue("GlideDamage", false);
		Flightsmooth = new BooleanValue("FlightSmooth", false);
		FlightdefaultSpeed = new NumberValue("DefaultSpeed", 1D, 0.1D, 9D);
		flightkick = new BooleanValue("FlightKick", false);
		
		this.addValue(mode,hypixelFlightOffsetOnEnable,glideDmg,flightkick,Flightsmooth,FlightdefaultSpeed);


	}

	@Override
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		mctimer.timerSpeed = 1;
		mc.thePlayer.stepHeight = 0.6F;
		super.onDisable();
	}

	@Override
	public void onPlayerEventPre(EventPlayerPre event) {
		if (mode.getMode("AAC").isToggled() || mode.getMode("AAC2").isToggled()) {

			mc.thePlayer.setSprinting(false);
			if ((mc.thePlayer.fallDistance >= 4.0F) && (!this.aac)) {
				this.aac = true;
				this.aad = (mc.thePlayer.posY + 3.0D);
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, true));
			}
			mc.thePlayer.capabilities.isFlying = false;
			if (this.aac) {
				if (mc.thePlayer.onGround) {
					this.aac = false;
				}
				if (mc.thePlayer.posY < this.aad) {
					Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, true));
					if (mc.gameSettings.keyBindSneak.isKeyDown()) {
						this.aad -= 2.0D;
					} else if ((mc.gameSettings.keyBindSneak.isKeyDown()) && (mc.thePlayer.posY < this.aad + 0.8D)) {
						this.aad += 2.0D;
					} else {
						mc.thePlayer.motionY = 0.7D;
						if (mode.getMode("AAC2").isToggled()) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ);
							Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
									mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, true));
						}
						move(0.8f);
					}
				} else {
					if (mode.getMode("AAC2").isToggled()) {
						if (mc.thePlayer.motionY <= 0) {
							event.setX(0.01);
						}
					}
				}
			} else {
				mc.thePlayer.capabilities.isFlying = false;
			}
		}
		if (mode.getMode("AAC3").isToggled() && Start) {
			if (!Up) {
				// event.y = 0.01;
				mc.thePlayer.motionY = 1;
				Up = true;
			} else {
				event.setY(-0.05);
				event.setX(event.getX() * 3);
				event.setZ(event.getZ() * 3);
				Up = false;
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, true));
			}
		}
		super.onPlayerEventPre(event);
	}

	public void move(float speed) {
		mc.thePlayer.motionX = (-(Math.sin(aan()) * speed));
		mc.thePlayer.motionZ = (Math.cos(aan()) * speed);
	}

	public float aan() {
		float var1 = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.thePlayer.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.thePlayer.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;

		return var1;
	}

	@Override
	public void onEnable() {
		timer.reset();
		if (mode.getMode("AirWalk").isToggled() || mode.getMode("Hypixel").isToggled()) {
			maxY = mc.thePlayer.posY + 10;
			damaging = true;
			hypixelTimer.reset();
			// Atlas.sendChatMessage(".damage");
		}
		if (mode.getMode("Hypixel").isToggled()) {
//			mc.player.setPosition (mc.player.posX, mc.player.posY + 0.7, mc.player.posZ);
			if (hypixelFlightOffsetOnEnable.getValue()) {
				double posX = mc.thePlayer.posX;
				double posY = mc.thePlayer.posY;
				double posZ = mc.thePlayer.posZ;
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.41999998688698D,
						posZ, mc.thePlayer.onGround));
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.7531999805212D,
						posZ, mc.thePlayer.onGround));
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 1.00133597911214D,
						posZ, mc.thePlayer.onGround));
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 1.16610926093821D,
						posZ, mc.thePlayer.onGround));
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 1.24918707874468D,
						posZ, mc.thePlayer.onGround));
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 1.1707870772188D,
						posZ, mc.thePlayer.onGround));
				mc.thePlayer.setPosition(posX, posY + 1, posZ);
			}
		}
		if (mode.getMode("Glide").isToggled()) {
			this.startingHeight = mc.thePlayer.posY + 1000;
			if (glideDmg.getValue()) {
				// Jigsaw.sendChatMessage(".damage");
			}
		}
		if (mode.getMode("MAC").isToggled()) {
			wait = 6;
			mc.thePlayer.motionY = 0.25;
		}
		if (mode.getMode("AAC3").isToggled()) {
			Up = true;
			Start = false;
		}
		super.onEnable();
	}

	public void updateFlyHeight() {
		double h = 1;
		AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625);
		for (flyHeight = 0; flyHeight < mc.thePlayer.posY; flyHeight += h) {
			AxisAlignedBB nextBox = box.offset(0, -flyHeight, 0);

			if (mc.theWorld.checkBlockCollision(nextBox)) {
				if (h < 0.0625)
					break;

				flyHeight -= h;
				h /= 2;
			}
		}
	}

	public void goToGround() {
		if (flyHeight > 320)
			return;

		double minY = mc.thePlayer.posY - flyHeight;

		if (minY <= 0)
			return;

		for (double y = mc.thePlayer.posY; y > minY;) {
			y -= 9.9;
			if (y < minY)
				y = minY;

			C03PacketPlayer packet = new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			Wrapper.INSTANCE.sendPacket(packet);
		}

		for (double y = minY; y < mc.thePlayer.posY;) {
			y += 9.9;
			if (y > mc.thePlayer.posY)
				y = mc.thePlayer.posY;

			C03PacketPlayer packet = new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			Wrapper.INSTANCE.sendPacket(packet);
		}
	}

	@Override
	public void onMotionUpdate(EventMotion event) {
		if (mode.getMode("AAC3").isToggled()) {
			event.onGround = true;
			Start = true;
		}
		if (mode.getMode("Default").isToggled()) {
			if (!Flightsmooth.getValue()) {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionY = 0;
			if (Flightsmooth.getValue()) {
				mc.thePlayer.jumpMovementFactor = (FlightdefaultSpeed.getValue().floatValue() / 10);
			} else {
				mc.thePlayer.jumpMovementFactor = (FlightdefaultSpeed.getValue().floatValue());
			}

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.thePlayer.motionY += FlightdefaultSpeed.getValue().floatValue() / 2;
			}
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY += -FlightdefaultSpeed.getValue().floatValue() / 2;
			}
			if (flightkick.getValue()) {
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (mode.getMode("MAC").isToggled()) {
			if (wait < 6) {
				wait++;
				if (mc.thePlayer.motionY < 0// If falling
						&& !mc.thePlayer.onGround) {
					mc.thePlayer.motionY = -MACvelY;
				}
				return;
			}
			if (mc.thePlayer.motionY < 0// If falling
					&& !mc.thePlayer.onGround) {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2, mc.thePlayer.posZ);
					wait = 3;
					return;
				} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					mc.thePlayer.motionY = -0.4;
					return;
				}
				mc.thePlayer.motionY = -MACvelY;

			}
			if (flightkick.getValue()) {
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (mode.getMode("Glide").isToggled()) {
			// if (mc.player.onGround) {
			// this.startingHeight = mc.player.posY;
			// }
			if (!mc.thePlayer.onGround) {
				if (mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.posY + 0.5 < startingHeight) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ);
				} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					mc.thePlayer.motionY = -0.2;
					return;
				}
				mc.thePlayer.motionY = -fallSpeed;

				if (simulateFall) {
					simulateFall = false;

					double posX = mc.thePlayer.posX;
					double posY = mc.thePlayer.posY;
					double posZ = mc.thePlayer.posZ;

					Wrapper.INSTANCE
							.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.049D, posZ, false));
					Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
					Wrapper.INSTANCE
							.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.049D, posZ, false));
					Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
					event.onGround = false;
				} else {
					simulateFall = true;
					event.onGround = true;
				}

			}
			if (flightkick.getValue()) {
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		if (mode.getMode("AirWalk").isToggled() || mode.getMode("Hypixel").isToggled()) {
			mc.thePlayer.motionY = 0;
			if (hypixelFlightOffsetOnEnable.getValue()) {
				if (mc.thePlayer.ticksExisted % 3 == 0 && !PlayerUtils.isEntityOnGround(mc.thePlayer)) {
					event.y += 1.0E-9D;
				}
				mc.thePlayer.stepHeight = 0.1f;
			} else {
				if (mc.thePlayer.ticksExisted % 3 == 0 && !PlayerUtils.isEntityOnGround(Wrapper.INSTANCE.player())) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ);
					Wrapper.INSTANCE.sendPacket(
							new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D,
									mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
				}
			}

			if (flightkick.getValue() && !mode.getMode("Hypixel").isToggled()) {
				Wrapper.INSTANCE.sendPacket(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
			if (mode.getMode("Hypixel").isToggled()) {
				event.onGround = true;
			}
		}
		super.onMotionUpdate(event);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (damaging) {
			damaging = false;
		}

		if (mode.getMode("AirWalk").isToggled() || mode.getMode("Hypixel").isToggled()) {
			mc.thePlayer.onGround = true;
		}
		super.onClientTick(event);
	}

}
