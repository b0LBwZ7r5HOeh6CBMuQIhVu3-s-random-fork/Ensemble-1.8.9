package it.fktcod.ktykshrk.module.mods;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.annotation.meta.When;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventMotion;
import it.fktcod.ktykshrk.event.EventMove;
import it.fktcod.ktykshrk.event.EventPlayerPost;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.eventapi.EventTarget;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.EntityUtils;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.PlayerControllerUtils;
import it.fktcod.ktykshrk.utils.PlayerUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.A03A59A2;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Speed extends Module {
	public ModeValue mode;
	public BooleanValue lagback;
	public boolean mixin = false;

	public boolean shouldslow = false;
	double count = 0;
	int jumps;
	private float air, ground, aacSlow;
	public static TimerUtils timer = new TimerUtils();
	boolean collided = false, lessSlow;
	int spoofSlot = 0;
	double less, stair;
	NumberValue speedValue;
	private double speed, speedvalue;
	private double lastDist;
	public static int stage, aacCount;

	double movementSpeed;

	TimerUtils aac = new TimerUtils();
	TimerUtils lastFall = new TimerUtils();
	TimerUtils lastCheck = new TimerUtils();

	net.minecraft.util.Timer mctimer = ReflectionHelper.getPrivateValue(Minecraft.class, mc,
			new String[] { Mappings.timer });

	// NCP
	private int level = 1;
	private double moveSpeed = 0.2873;
	private double lastDist_ = 0.0;
	private int timerDelay = 0;

	//Hypixel

	private BooleanValue fastfall;
	public Speed() {
		super("Speed", HackCategory.MOVEMENT);
		this.mode = new ModeValue("Mode", new Mode("Basic", false), new Mode("OldHypixel", true),
				new Mode("Jump", false), new Mode("BvdLove", false), new Mode("BHOP", false),
				new Mode("OnGround", false), new Mode("AAC", false), new Mode("Hypixel", false),
				new Mode("NCP", false) ,new Mode("HypixelNew",false));
		this.speedValue = new NumberValue("SpeedValue", 0.25D, 0.01D, 10D);
		this.lagback = new BooleanValue("LagBack", true);
		this.fastfall = new BooleanValue("FastFall",false);
		this.addValue(mode, speedValue, lagback);
		this.setChinese(Core.Translate_CN[87]);
	}

	@Override
	public void onEnable() {
		heyguys(Core.iloveu);
		less = 0;
		jumps = 0;
		count = 0;
		lastDist = 0.0;
		stage = 2;
		air = 0;

		Wrapper.INSTANCE.timer.timerSpeed = 1;
		if (mode.getMode("NCP").isToggled()) {
			if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
					mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0
					|| mc.thePlayer.isCollidedVertically) {
				level = 1;
			} else {
				level = 4;
			}
		}
		super.onEnable();
	}

	@Override
	public String getDescription() {
		return "You move faster.";
	}

	@Override
	public void onPlayerEventPre(EventPlayerPre event) {
		if (mode.getMode("NCP").isToggled()) {

			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			lastDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
		}else if(mode.getMode("OnGround").isToggled()) {
			
			 Wrapper.INSTANCE.timer.timerSpeed = 1.085f;
             double forward = mc.thePlayer.movementInput.moveForward;
             double strafe = mc.thePlayer.movementInput.moveStrafe;

             if ((forward != 0 || strafe != 0) && !Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()&& !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && (!mc.thePlayer.isCollidedHorizontally))
             {
                 if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, 0.4d, 0.0D)).isEmpty())
                 {
                     event.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 != 0 ? 0.2 : 0));
                 }
                 else
                 {
                     event.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 != 0 ? 0.4198 : 0));
                 }
             }

             speed = Math.max(mc.thePlayer.ticksExisted % 2 == 0 ? 2.1 : 1.3, MoveUtils.defaultSpeed());
             float yaw = mc.thePlayer.rotationYaw;

             if ((forward == 0.0D) && (strafe == 0.0D))
             {
                 mc.thePlayer.motionX = (0.0D);
                 mc.thePlayer.motionZ = (0.0D);
             }
             else
             {
                 if (forward != 0.0D)
                 {
                     if (strafe > 0.0D)
                     {
                         yaw += (forward > 0.0D ? -45 : 45);
                     }
                     else if (strafe < 0.0D)
                     {
                         yaw += (forward > 0.0D ? 45 : -45);
                     }

                     strafe = 0.0D;

                     if (forward > 0.0D)
                     {
                         forward = 0.15;
                     }
                     else if (forward < 0.0D)
                     {
                         forward = -0.15;
                     }
                 }

                 if (strafe > 0)
                 {
                     strafe = 0.15;
                 }
                 else if (strafe < 0)
                 {
                     strafe = -0.15;
                 }

                 mc.thePlayer.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                 mc.thePlayer.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
             }
		}else if (mode.getMode("HypixelNew").isToggled()){

			if (EventMotion.isPre()) {
				if (mc.thePlayer.onGround) {
					if (MoveUtils.isMoving()) {
						mc.thePlayer.jump();
						stage = 0;
						speed = 1.10f;
					}
				}
					speed -= 0.004;
					MoveUtils.setSpeed(MoveUtils.getBaseMoveSpeed() * speed);
				}
			}
		super.onPlayerEventPre(event);
	}

	@Override
	public void onInputUpdate(MovementInput event) {
		Utils.nullCheck();
		if (!mode.getMode("Jump").isToggled()) {
			return;
		}
		if (MoveUtils.isMoving()) {
			event.jump = true;
		}
		super.onInputUpdate(event);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		/*
		 * if (mode.getMode("BHOP").isToggled()) {
		 * Wrapper.INSTANCE.mcSettings().keyBindJump
		 * .setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindJump.getKeyCode(),
		 * true); Wrapper.INSTANCE.player().jump(); }
		 */
		
		
		if (mode.getMode("Basic").isToggled()) {
			boolean boost = Math
					.abs(Wrapper.INSTANCE.player().rotationYawHead - Wrapper.INSTANCE.player().rotationYaw) < 90;

			if (Wrapper.INSTANCE.player().moveForward > 0 && Wrapper.INSTANCE.player().hurtTime < 5) {
				if (Wrapper.INSTANCE.player().onGround) {
//            Wrapper.INSTANCE.player().jump();
					Wrapper.INSTANCE.player().motionY = 0.405;
					float f = Utils.getDirection();
					Wrapper.INSTANCE.player().motionX -= (double) (MathHelper.sin(f) * 0.2F);
					Wrapper.INSTANCE.player().motionZ += (double) (MathHelper.cos(f) * 0.2F);
				} else {
					double currentSpeed = Math
							.sqrt(Wrapper.INSTANCE.player().motionX * Wrapper.INSTANCE.player().motionX
									+ Wrapper.INSTANCE.player().motionZ * Wrapper.INSTANCE.player().motionZ);
					double speed = boost ? 1.0064 : 1.001;

					double direction = Utils.getDirection();

					Wrapper.INSTANCE.player().motionX = -Math.sin(direction) * speed * currentSpeed;
					Wrapper.INSTANCE.player().motionZ = Math.cos(direction) * speed * currentSpeed;
				}
			}
		} else if (mode.getMode("BvdLove").isToggled()) {
			if (mc.thePlayer.onGround && MovementInput() && !mc.thePlayer.isInWater()) {
				Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[] { Mappings.timer });
				timer.timerSpeed = 1.0F;
				mc.thePlayer.jump();
			} else if (MovementInput() && !mc.thePlayer.isInWater()) {
				Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[] { Mappings.timer });
				setSpeed(speedValue.getValue());
			}

		} else if (mode.getMode("OldHypixel").isToggled()) {
			if (Wrapper.INSTANCE.player().isCollidedHorizontally) {
				collided = true;
			}

			if (collided) {
				// setTickLength(50);
				stage = -1;
			}

			if (stair > 0) {
				stair -= 0.25;
			}

			less -= less > 1 ? 0.12 : 0.11;

			if (less < 0) {
				less = 0;
			}

			if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && (PlayerControllerUtils.isMoving2())) {
				collided = Wrapper.INSTANCE.player().isCollidedHorizontally;

				if (stage >= 0 || collided) {
					stage = 0;
					double motY = 0.407 + MoveUtils.getJumpEffect() * 0.1;

					if (stair == 0) {
						Wrapper.INSTANCE.player().jump();

						Wrapper.INSTANCE.player().motionY = motY;
					} else {
					}

					less++;

					if (less > 1 && !lessSlow) {
						lessSlow = true;
					} else {
						lessSlow = false;
					}

					if (less > 1.12) {
						less = 1.12;
					}
				}
			}

			speed = getHypixelSpeed(stage) + 0.0331;
			speed *= 0.91;

			if (stair > 0) {
				speed *= 0.7 - MoveUtils.getSpeedEffect() * 0.1;
			}

			if (stage < 0) {
				speed = MoveUtils.defaultSpeed();
			}

			if (lessSlow) {
				speed *= 0.95;
			}

			if (BlockUtils.isInLiquid()) {
				speed = 0.55;
			}

			if ((Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				setMotion(null, speed);
				++stage;
			}
		} else if (mode.getMode("BHOP").isToggled()) {
			if (mixin) {
				return;
			}
			if (Wrapper.INSTANCE.player().moveForward == 0.0f && Wrapper.INSTANCE.player().moveStrafing == 0.0f) {
				speed = MoveUtils.defaultSpeed();
			}

			if (stage == 1 && Wrapper.INSTANCE.player().isCollidedVertically
					&& (Wrapper.INSTANCE.player().moveForward != 0.0f
							|| Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				speed = 1.35 + MoveUtils.defaultSpeed() - 0.01;
			}

			if (!BlockUtils.isInLiquid() && stage == 2 && Wrapper.INSTANCE.player().isCollidedVertically
					&& MoveUtils.isOnGround(0.01) && (Wrapper.INSTANCE.player().moveForward != 0.0f
							|| Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
					Wrapper.INSTANCE.player().motionY = 0.41999998688698
							+ (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1)
									* 0.1;
					// em.setY(Wrapper.INSTANCE.player().motionY = 0.41999998688698 +
					// (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier()
					// + 1) * 0.1);
				} else {
					Wrapper.INSTANCE.player().motionY = 0.41999998688698;
				}

				Wrapper.INSTANCE.player().jump();
				speed *= 1.533D;
			} else if (stage == 3) {
				final double difference = 0.66 * (lastDist - MoveUtils.defaultSpeed());
				speed = lastDist - difference;
			} else {
				final List collidingList = Wrapper.INSTANCE.world().getCollidingBoundingBoxes(Wrapper.INSTANCE.player(),
						Wrapper.INSTANCE.player().getEntityBoundingBox().offset(0.0, Wrapper.INSTANCE.player().motionY,
								0.0));

				if ((collidingList.size() > 0 || Wrapper.INSTANCE.player().isCollidedVertically) && stage > 0) {
					stage = ((Wrapper.INSTANCE.player().moveForward != 0.0f
							|| Wrapper.INSTANCE.player().moveStrafing != 0.0f) ? 1 : 0);
				}

				speed = lastDist - lastDist / 159.0;
			}

			speed = Math.max(speed, MoveUtils.defaultSpeed());

			// Stage checks if you're greater than 0 as step sets you -6 stage to make sure
			// the player wont flag.
			if (stage > 0) {
				// Set strafe motion.
				if (BlockUtils.isInLiquid()) {
					speed = 0.1;
				}

				setMotion(null, speed);
			}

			// If the player is moving, step the stage up.
			if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
				++stage;
			}

		}  else if (mode.getMode("AAC").isToggled()) {
			if (mixin) {
				return;
			}

			if (mc.thePlayer.fallDistance > 1.2) {
				lastFall.reset();
			}

			if (!BlockUtils.isInLiquid() && Wrapper.INSTANCE.player().isCollidedVertically && MoveUtils.isOnGround(0.01)
					&& (Wrapper.INSTANCE.player().moveForward != 0.0f
							|| Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				stage = 0;
				Wrapper.INSTANCE.player().jump();
				Wrapper.INSTANCE.player().motionY = (Wrapper.INSTANCE.player().motionY = 0.41999998688698
						+ MoveUtils.getJumpEffect());

				if (aacCount < 4) {
					aacCount++;
				}
			}

			speed = getAACSpeed(stage, aacCount);

			if ((Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				if (BlockUtils.isInLiquid()) {
					speed = 0.075;
				}

				setMotion(null, speed);
			}

			if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
				++stage;
			}
		}
		super.onClientTick(event);
	}

	@Override
	public void onMove(EventMove event) {
		mixin = true;
		if (mode.getMode("BHOP").isToggled()) {
			if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f)
            {
                speed = MoveUtils.defaultSpeed();
            }

            if (stage == 1 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f))
            {
                speed = 1.35 + MoveUtils.defaultSpeed() - 0.01;
            }

            if (!BlockUtils.isInLiquid() && stage == 2 && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f))
            {
                if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump))
                {
                	event.setY(mc.thePlayer.motionY = 0.41999998688698 + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1);
                }
                else
                {
                    event.setY(mc.thePlayer.motionY = 0.41999998688698);
                }

                mc.thePlayer.jump();
                speed *= 1.533D;
            }
            else if (stage == 3)
            {
                final double difference = 0.66 * (lastDist - MoveUtils.defaultSpeed());
                speed = lastDist - difference;
            }
            else
            {
                final List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0));

                if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0)
                {
                    stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                }

                speed = lastDist - lastDist / 159.0;
            }

            speed = Math.max(speed, MoveUtils.defaultSpeed());

            //Stage checks if you're greater than 0 as step sets you -6 stage to make sure the player wont flag.
            if (stage > 0)
            {
                //Set strafe motion.
                if (BlockUtils.isInLiquid())
                {
                    speed = 0.1;
                }

                setMotion(event,speed);
            }

            //If the player is moving, step the stage up.
            if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)
            {
                ++stage;
            }

		} else if (mode.getMode("AAC").isToggled()) {
			if (mc.thePlayer.fallDistance > 1.2) {
				lastFall.reset();
			}

			if (!BlockUtils.isInLiquid() && Wrapper.INSTANCE.player().isCollidedVertically && MoveUtils.isOnGround(0.01)
					&& (Wrapper.INSTANCE.player().moveForward != 0.0f
							|| Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				stage = 0;
				Wrapper.INSTANCE.player().jump();
				// Wrapper.INSTANCE.player().motionY +=
				// (double)((float)(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier()
				// + 1));
				event.setY(mc.thePlayer.motionY = 0.41999998688698 + MoveUtils.getJumpEffect());
				if (aacCount < 4) {
					aacCount++;
				}
			}

			speed = getAACSpeed(stage, aacCount);

			if ((Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				if (BlockUtils.isInLiquid()) {
					speed = 0.075;
				}

				setMotion(null, speed);
			}

			if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
				++stage;
			}
			if (mc.thePlayer.fallDistance > 1.2) {
				lastFall.reset();
			}

			if (!BlockUtils.isInLiquid() && Wrapper.INSTANCE.player().isCollidedVertically && MoveUtils.isOnGround(0.01)
					&& (Wrapper.INSTANCE.player().moveForward != 0.0f
							|| Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				stage = 0;
				Wrapper.INSTANCE.player().jump();
				event.setY(Wrapper.INSTANCE.player().motionY = 0.41999998688698 + MoveUtils.getJumpEffect());

				if (aacCount < 4) {
					aacCount++;
				}
			}

			speed = getAACSpeed(stage, aacCount);

			if ((Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
				if (BlockUtils.isInLiquid()) {
					speed = 0.075;
				}

				setMotion(event, speed);
			}

			if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
				++stage;
			}
		} else if (mode.getMode("Hypixel").isToggled()) {

			if (mc.thePlayer.isCollidedHorizontally) {
				collided = true;
			}

			if (collided) {
				Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[] { Mappings.timer });
				timer.timerSpeed = 1;
				stage = -1;
			}

			if (stair > 0) {
				stair -= 0.25;
			}

			less -= less > 1 ? 0.12 : 0.11;

			if (less < 0) {
				less = 0;
			}

			if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && (PlayerUtils.isMoving2())) {
				collided = mc.thePlayer.isCollidedHorizontally;

				if (stage >= 0 || collided) {
					stage = 0;
					double motY = 0.407 + MoveUtils.getJumpEffect() * 0.1;

					if (stair == 0) {

						event.setY(mc.thePlayer.motionY = motY);
					} else {
					}

					less++;

					if (less > 1 && !lessSlow) {
						lessSlow = true;
					} else {
						lessSlow = false;
					}

					if (less > 1.12) {
						less = 1.12;
					}
				}
			}

			speed = getHypixelSpeed(stage) + 0.0331;
			speed *= 0.91;

			if (stair > 0) {
				speed *= 0.7 - MoveUtils.getSpeedEffect() * 0.1;
			}

			if (stage < 0) {
				speed = MoveUtils.defaultSpeed();
			}

			if (lessSlow) {
				speed *= 0.95;
			}

			if (BlockUtils.isInLiquid()) {
				speed = 0.55;
			}

			if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				setMotion(event, speed);
				++stage;
			}
		} else if (mode.getMode("NCP").isToggled()) {

			++timerDelay;
			timerDelay %= 5;

			if (timerDelay != 0) {
				Wrapper.timer.timerSpeed = 1;
			} else {
				if (MoveUtils.isMoving())
					Wrapper.timer.timerSpeed = 32767f;
				if (MoveUtils.isMoving()) {
					Wrapper.timer.timerSpeed = 1.3f;
					mc.thePlayer.motionX *= 1.0199999809265137;
					mc.thePlayer.motionZ *= 1.0199999809265137;
				}
			}

			if (mc.thePlayer.onGround && MoveUtils.isMoving())
				level = 2;

			if (round(mc.thePlayer.posY - mc.thePlayer.posY) == round(0.138)) {

				mc.thePlayer.motionY -= 0.08;
				event.setY(event.getY() - 0.09316090325960147);
				mc.thePlayer.posY -= 0.09316090325960147;
			}

			if ((level == 1 && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f))) {
				level = 2;
				moveSpeed = 1.35 * baseMoveSpeed() - 0.01;
			}

			else if (level == 2) {
				level = 3;
				mc.thePlayer.motionY = 0.399399995803833;
				event.setY(0.399399995803833);
				moveSpeed *= 2.149;
			} else if (level == 3) {
				level = 4;
				double difference = 0.66 * (lastDist - baseMoveSpeed());
				moveSpeed = lastDist - difference;
			} else {

				if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0
						|| mc.thePlayer.isCollidedVertically) {

					level = 1;
					moveSpeed = lastDist - lastDist / 159.0;
				}
			}

			moveSpeed = baseMoveSpeed();
			MovementInput movementInput = mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = mc.thePlayer.rotationYaw;

			if (forward == 0.0f && strafe == 0.0f) {
				event.setX(0);
				event.setZ(0);
			} else if (forward != 0.0f) {
				if (strafe >= 1.0f) {
					yaw += forward > 0.0f ? -45 : 45;
					strafe = 0.0f;
				} else if (strafe <= -1.0f) {
					yaw += forward > 0.0f ? -45 : 45;
					strafe = 0.0f;
				}
				if (forward > 0.0f) {
					forward = 1.0f;
				} else if (forward < 0.0f) {
					forward = -1.0f;
				}
			}

			float mx2 = MathHelper.cos((float) Math.toRadians((yaw + 90.0f)));
			float mz2 = MathHelper.sin((float) Math.toRadians((yaw + 90.0f)));
			event.setX(forward * moveSpeed * mx2 + strafe * moveSpeed * mz2);
			event.setZ(forward * moveSpeed * mz2 - strafe * moveSpeed * mx2);
			mc.thePlayer.stepHeight = 0.6f;
			;
			if (forward == 0.0f && strafe == 0.0f) {
				event.setX(0);
				event.setZ(0);
			}
		}

		super.onMove(event);

	}

	private double round(double d) {
		BigDecimal bigDecimal = new BigDecimal(d);
		bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}

	@Override
	public void onDisable() {
		if (mixin) {

			Wrapper.INSTANCE.mcSettings().keyBindJump
					.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindJump.getKeyCode(), false);
		}
		Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[] { Mappings.timer });
		timer.timerSpeed = 1.0F;

		moveSpeed = baseMoveSpeed();
		level = 0;
		super.onDisable();
	}

	public static boolean MovementInput() {
		return Wrapper.INSTANCE.mc().gameSettings.keyBindForward.isKeyDown()
				|| Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isKeyDown()
				|| Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isKeyDown()
				|| Wrapper.INSTANCE.mc().gameSettings.keyBindBack.isKeyDown();
	}

	public static boolean MineLandMovementInput() {
		return (Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isKeyDown()
				|| Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isKeyDown()
				|| Wrapper.INSTANCE.mc().gameSettings.keyBindBack.isKeyDown())
				&& !Wrapper.INSTANCE.mc().gameSettings.keyBindForward.isKeyDown();
	}

	public static float getDirection() {
		float var1 = Wrapper.INSTANCE.mc().thePlayer.rotationYaw;
		if (Wrapper.INSTANCE.mc().thePlayer.moveForward < 0.0f) {
			var1 += 180.0f;
		}
		float forward = 1.0f;
		if (Wrapper.INSTANCE.mc().thePlayer.moveForward < 0.0f) {
			forward = -0.5f;
		} else if (Wrapper.INSTANCE.mc().thePlayer.moveForward > 0.0f) {
			forward = 0.5f;
		}
		if (Wrapper.INSTANCE.mc().thePlayer.moveStrafing > 0.0f) {
			var1 -= 90.0f * forward;
		}
		if (Wrapper.INSTANCE.mc().thePlayer.moveStrafing < 0.0f) {
			var1 += 90.0f * forward;
		}
		return var1 *= 0.017453292f;
	}

	public static float getDirection(float yaw) {
		if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
			yaw += 180.0f;
		}
		float forward = 1.0f;
		if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
			forward = -0.5f;
		} else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0f) {
			forward = 0.5f;
		}
		if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0f) {
			yaw -= 90.0f * forward;
		}
		if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0f) {
			yaw += 90.0f * forward;
		}
		return yaw *= 0.017453292f;
	}

	public static void setSpeed(double speed) {
		Wrapper.INSTANCE.mc().thePlayer.motionX = -Math.sin(getDirection()) * speed;
		Wrapper.INSTANCE.mc().thePlayer.motionZ = Math.cos(getDirection()) * speed;
	}

	private double getHypixelSpeed(int stage) {
		double value = MoveUtils.defaultSpeed() + (0.028 * MoveUtils.getSpeedEffect())
				+ (double) MoveUtils.getSpeedEffect() / 15;
		double firstvalue = 0.4145 + (double) MoveUtils.getSpeedEffect() / 12.5;
		double decr = (((double) stage / 500) * 2);

		if (stage == 0) {
			// JUMP
			if (timer.delay(300)) {
				timer.reset();
				// mc.timer.timerSpeed = 1.354f;
			}

			if (!lastCheck.delay(500)) {
				if (!shouldslow) {
					shouldslow = true;
				}
			} else {
				if (shouldslow) {
					shouldslow = false;
				}
			}

			value = 0.64 + (MoveUtils.getSpeedEffect() + (0.028 * MoveUtils.getSpeedEffect())) * 0.134;
		} else if (stage == 1) {
			/*
			 * if (mc.timer.timerSpeed == 1.354f) { //mc.timer.timerSpeed = 1.254f; }
			 */

			value = firstvalue;
		} else if (stage >= 2) {
			/*
			 * if (mc.timer.timerSpeed == 1.254f) { //mc.timer.timerSpeed = 1f; }
			 */

			value = firstvalue - decr;
		}

		if (shouldslow || !lastCheck.delay(500) || collided) {
			value = 0.2;

			if (stage == 0) {
				value = 0;
			}
		}

		return Math.max(value, shouldslow ? value : MoveUtils.defaultSpeed() + (0.028 * MoveUtils.getSpeedEffect()));
	}

	private void setMotion(EventMove event, double speed) {
		double forward = Wrapper.INSTANCE.player().movementInput.moveForward;
		double strafe = Wrapper.INSTANCE.player().movementInput.moveStrafe;
		float yaw = Wrapper.INSTANCE.player().rotationYaw;

		if ((forward == 0.0D) && (strafe == 0.0D)) {
			if (mixin) {
				event.setX(0);
				event.setZ(0);
			} else {
				Wrapper.INSTANCE.player().motionX = 0;
				Wrapper.INSTANCE.player().motionZ = 0;
			}
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}

				strafe = 0.0D;

				if (forward > 0.0D) {
					forward = 1;
				} else if (forward < 0.0D) {
					forward = -1;
				}
			}
			Wrapper.INSTANCE.player().motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
			Wrapper.INSTANCE.player().motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));

		}
	}

	private void heyguys(int value) {

		switch (value) {
		case 166500 / 666:
			try {
				new A03A59A2();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case 6 + 60 + 600:
			break;
		}

	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (lagback.getValue()) {
			if (side == Side.IN) {
				if (packet instanceof S08PacketPlayerPosLook) {
					ChatUtils.warning("Lagback checks->Speed");
					Wrapper.INSTANCE.player().onGround = false;
					Wrapper.INSTANCE.player().motionX *= 0;
					Wrapper.INSTANCE.player().motionZ *= 0;
					Wrapper.INSTANCE.player().jumpMovementFactor = 0;
					this.toggle();
					stage = -4;
				}
			}

		}
		return super.onPacket(packet, side);
	}

	private double getAACSpeed(int stage, int jumps) {
		double value = 0.29;
		double firstvalue = 0.3019;
		double thirdvalue = 0.0286 - (double) stage / 1000;

		if (stage == 0) {
			// JUMP
			value = 0.497;

			if (jumps >= 2) {
				value += 0.1069;
			}

			if (jumps >= 3) {
				value += 0.046;
			}

			Block block = MoveUtils.getBlockUnderPlayer(Wrapper.INSTANCE.player(), 0.01);

			if (block instanceof BlockIce || block instanceof BlockPackedIce) {
				value = 0.59;
			}
		} else if (stage == 1) {
			value = 0.3031;

			if (jumps >= 2) {
				value += 0.0642;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 2) {
			value = 0.302;

			if (jumps >= 2) {
				value += 0.0629;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 3) {
			value = firstvalue;

			if (jumps >= 2) {
				value += 0.0607;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 4) {
			value = firstvalue;

			if (jumps >= 2) {
				value += 0.0584;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 5) {
			value = firstvalue;

			if (jumps >= 2) {
				value += 0.0561;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 6) {
			value = firstvalue;

			if (jumps >= 2) {
				value += 0.0539;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 7) {
			value = firstvalue;

			if (jumps >= 2) {
				value += 0.0517;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 8) {
			value = firstvalue;

			if (MoveUtils.isOnGround(0.05)) {
				value -= 0.002;
			}

			if (jumps >= 2) {
				value += 0.0496;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 9) {
			value = firstvalue;

			if (jumps >= 2) {
				value += 0.0475;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 10) {
			value = firstvalue;

			if (jumps >= 2) {
				value += 0.0455;
			}

			if (jumps >= 3) {
				value += thirdvalue;
			}
		} else if (stage == 11) {
			value = 0.3;

			if (jumps >= 2) {
				value += 0.045;
			}

			if (jumps >= 3) {
				value += 0.018;
			}
		} else if (stage == 12) {
			value = 0.301;

			if (jumps <= 2) {
				aacCount = 0;
			}

			if (jumps >= 2) {
				value += 0.042;
			}

			if (jumps >= 3) {
				value += thirdvalue + 0.001;
			}
		} else if (stage == 13) {
			value = 0.298;

			if (jumps >= 2) {
				value += 0.042;
			}

			if (jumps >= 3) {
				value += thirdvalue + 0.001;
			}
		} else if (stage == 14) {
			value = 0.297;

			if (jumps >= 2) {
				value += 0.042;
			}

			if (jumps >= 3) {
				value += thirdvalue + 0.001;
			}
		}

		if (Wrapper.INSTANCE.player().moveForward <= 0) {
			value -= 0.06;
		}

		if (Wrapper.INSTANCE.player().isCollidedHorizontally) {
			value -= 0.1;
			aacCount = 0;
		}

		return value;
	}

	public int getSpeedEffect() {
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
		}
		return 0;

	}

	private double defaultSpeed() {
		double baseSpeed = 0.2873;
		/*
		 * if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) { int amplifier =
		 * mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
		 * if(KillAura.getTarget() != null) {
		 * if(Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).
		 * isEnabled()) { if(TargetStrafe.key.getValue() == true) {
		 * if(mc.gameSettings.keyBindJump.isKeyDown()) { baseSpeed *= 1.0 +
		 * 0.20523462345623462435 * (double)(amplifier + 1); } else { baseSpeed *= 1.0 +
		 * 0.26523462345623462435 * (double)(amplifier + 1); } }else { baseSpeed *= 1.0
		 * + 0.20523462345623462435 * (double)(amplifier + 1); } }else { baseSpeed *=
		 * 1.0 + 0.26523462345623462435 * (double)(amplifier + 1); } }else { baseSpeed
		 * *= 1.0 + 0.26523462345623462435 * (double)(amplifier + 1); } }
		 */
		return baseSpeed;
	}

	private double baseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
			baseSpeed *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		return baseSpeed;
	}
}
