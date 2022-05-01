package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.value.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;

import it.fktcod.ktykshrk.event.EventBlockBB;
import it.fktcod.ktykshrk.event.EventJump;
import it.fktcod.ktykshrk.event.EventMotion;
import it.fktcod.ktykshrk.event.EventMove;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import scala.collection.mutable.MutableList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Fly extends Module {

	NumberValue speedValue;
	public static ModeValue mode;
	int ticks = 0;

	int state = 0;
	int aactick;

	double lastPosX;
	double lastPosZ;
	int sameCount;
	int sameCountReach;

	Packet p = null;
	ArrayList<Packet> packets = new ArrayList<Packet>();
	double launchY=0;

	float yaw;
	float pitch;
	
	//hyp2
	int wait = 6;
	double MACvelY = 0.02;
	double startingHeight;
	double fallSpeed = 0.05;
	double maxY;
	boolean damaging = false;
	public double flyHeight;
	private boolean aac;
	private double aad;
	boolean Up = false;
	boolean Start = false;
	private TimerUtils hypixelTimer = new TimerUtils();
	private TimerUtils timerutils = new TimerUtils();
	boolean simulateFall = false;
	
	//verus
	NumberValue airspeed;
	NumberValue groundSpeed;
	NumberValue hopDelay;
	private int waitTicks = 0;

	//Hypixel
	private boolean doFly;
	private double x, y, z;
	private float stage;
	private boolean hasClipped;
	net.minecraft.util.Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});

	//AAC5
	private boolean nextFlag = false;
	private boolean flyClip = false;
	private boolean flyStart = false;

	//AAC5Value
	private BooleanValue smoothValue;
	private ModeValue packetModeValue;
	private NumberValue purseValue;
	private BooleanValue useC04Value;


	public Fly() {
		super("Flight", HackCategory.MOVEMENT);

		this.mode = new ModeValue("Mode", new Mode("Simple", true), new Mode("Dynamic", false),
				new Mode("Hypixel", false), new Mode("AAC", false),new Mode("Motion", false),new Mode("Verus", false), new Mode("AirWalk",false), new Mode("AAC5",false), new Mode("AAC5Vanilla",false));

		this.speedValue = new NumberValue("Speed", 1D, 1D, 5D);
		
		airspeed=new NumberValue("AirSpeed", 0.5D, 0D, 1D);
		groundSpeed=new NumberValue("GroundSpeed", 0.42D, 0D, 1D);
		hopDelay=new NumberValue("HopDelay", 3D, 0D, 10D);
		this.packetModeValue = new ModeValue("PacketMode", new Mode("Old", true), new Mode("Rise", false));
		purseValue=new NumberValue("Purse", 7D, 3D, 20D);
		smoothValue=new BooleanValue("Smooth", false);
		useC04Value=new BooleanValue("UseC04", false);
		this.addValue(mode, speedValue,airspeed,groundSpeed,hopDelay,packetModeValue,purseValue,smoothValue,useC04Value);
		this.setChinese(Core.Translate_CN[44]);

	}

	@Override
	public String getDescription() {
		return "Allows you to you fly.";
	}

	@Override
	public void onEnable() {
		yaw = mc.thePlayer.rotationYaw;
		pitch = mc.thePlayer.rotationPitch;

		lastPosX = 0;
		lastPosZ = 0;
		sameCount = 0;
		aactick = 0;
		sameCountReach = 5;
		ticks = 0;
		//verus
		waitTicks=0;
		launchY=Wrapper.INSTANCE.player().posY;


		//AAC5
		if (mode.getMode("AAC5").isToggled() || mode.getMode("AAC5Vanilla").isToggled()) {
			if (mc.isSingleplayer()) {
				ChatUtils.error("Use AAC5.2.0 Fly will crash single player");
				this.setToggled(false);
				return;
			}

			packets.clear();
			nextFlag = false;
			flyClip = false;
			flyStart = false;
			timerutils.reset();

			if (smoothValue.getValue()) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
			}
		}

		super.onEnable();
	}

	@Override
	public void onDisable() {

		if (mode.getMode("Simple").isToggled()) {
			Wrapper.INSTANCE.player().capabilities.isFlying = false;
		}

		if (mode.getMode("AAC").isToggled()) {
			mc.thePlayer.motionY = 0;
			timer.timerSpeed = (float) 1.0;

			Wrapper.INSTANCE.mcSettings().keyBindForward
					.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode(), false);
			// mc.gameSettings.keyBindForward.pressed=false

			if (p != null)
				sendPacket(p);

			packets.clear();
		}
		//AAC5
		if (mode.getMode("AAC5Vanilla").isToggled()) {
			sendPackets();
			packets.clear();
			mc.thePlayer.noClip = false;
		}
		if (mode.getMode("Hypixel").isToggled()){
			timer.timerSpeed = (float) 1.0;
		}
		super.onDisable();
	}
	private void sendPackets() {
		float yaw = mc.thePlayer.rotationYaw;
		float pitch = mc.thePlayer.rotationPitch;
		if (packetModeValue.getSelectMode().getName() == "Old") {
			for (int i=0; i<packets.size(); i++) {
				if (packets.get(i) instanceof C03PacketPlayer ){
					C03PacketPlayer packet = (C03PacketPlayer)packets.get(i);

					if (packet.isMoving()) {
						sendPacket(packet);
						if (packet.getRotating()) {
							yaw = packet.getYaw();
							pitch = packet.getPitch();
						}
						if (useC04Value.getValue()) {
							sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(packet.getPositionX(), 1e+308, packet.getPositionZ(), true));
							sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(packet.getPositionX(),packet.getPositionY(), packet.getPositionZ(), true));
						} else {
							sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getPositionX(), 1e+308, packet.getPositionZ(), yaw, pitch, true));
							sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ(), yaw, pitch, true));
						}
					}
				}
			}

		} else {
			for (int i=0; i<packets.size(); i++) {
				if (packets.get(i) instanceof C03PacketPlayer ){
					C03PacketPlayer packet = (C03PacketPlayer)packets.get(i);
					if (packet.isMoving()) {
						sendPacket(packet);
						if (packet.getRotating()) {
							yaw = packet.getYaw();
							pitch = packet.getPitch();
						}
						if (useC04Value.getValue()) {
							sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(packet.getPositionX(), -1e+159, packet.getPositionZ() + 10, true));
							sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ(), true));
						} else {
							sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getPositionX(), -1e+159, packet.getPositionZ() + 10, yaw, pitch, true));
							sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ(), yaw, pitch, true));
						}
					}
				}
			}
		}
		packets.clear();
	}
	@Override
	public boolean onPacket(Object packet, Side side) {
		if (mode.getMode("AAC5").isToggled()) {
			if (side == Side.IN) {

				if (packet instanceof S08PacketPlayerPosLook) {

					mc.thePlayer.setPosition(((S08PacketPlayerPosLook) packet).getX(), ((S08PacketPlayerPosLook) packet).getY(), ((S08PacketPlayerPosLook) packet).getZ());
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, ((S08PacketPlayerPosLook) packet).getYaw(), ((S08PacketPlayerPosLook) packet).getPitch(), false));
					double dist = 0.14;
					double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
					mc.thePlayer.setPosition(mc.thePlayer.posX + -sin(yaw) * dist, mc.thePlayer.posY, mc.thePlayer.posZ + cos(yaw) * dist);
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true));
					return false;
				}

			}else if (side == Side.OUT){
				if (packet instanceof C03PacketPlayer){
					return false;
				}
			}
		}

		if (mode.getMode("AAC5Vanilla").isToggled()) {
			if (side == Side.IN) {

				if (packet instanceof S08PacketPlayerPosLook) {
					flyStart = true;
					if (timerutils.hasReached(2000)) {
						flyClip = true;
						timer.timerSpeed = 1.3F;
					}
					nextFlag = true;
				}

			}else if (side == Side.OUT){
				if (packet instanceof C03PacketPlayer){
					double f = mc.thePlayer.width / 2.0;
					// need to no collide else will flag
					if(((C03PacketPlayer) packet).getPositionY() < 1145.14001919810) {
						if (mc.theWorld.checkBlockCollision(new AxisAlignedBB(((C03PacketPlayer) packet).getPositionX() - f, ((C03PacketPlayer) packet).getPositionY(), ((C03PacketPlayer) packet).getPositionZ() - f, ((C03PacketPlayer) packet).getPositionX() + f, ((C03PacketPlayer) packet).getPositionY() + mc.thePlayer.height, ((C03PacketPlayer) packet).getPositionZ() + f))) {
							return true;
						}
						packets.add((Packet) packet);
						if (!(smoothValue.getValue() && !timerutils.hasReached(1000)) && packets.size() > purseValue.getValue()) {
							sendPackets();
						}
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		if (mode.getMode("AAC5").isToggled()) {
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionY = 0.003;
			mc.thePlayer.motionZ = 0.0;
		}else if (mode.getMode("AAC5").isToggled()) {
			mc.thePlayer.noClip = !MoveUtils.isMoving();
			if (smoothValue.getValue()) {
				if (!timerutils.hasReached(1000) || !flyStart) {
					mc.thePlayer.motionY = 0.0;
					mc.thePlayer.motionX = 0.0;
					mc.thePlayer.motionZ = 0.0;
					mc.thePlayer.jumpMovementFactor = 0.00f;
					timer.timerSpeed = 0.32F;
					return;
				} else {
					if (!flyClip) {
						timer.timerSpeed = 0.19F;
					} else {
						flyClip = false;
						timer.timerSpeed = 1.2F;
					}
				}
			}

			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionY = 0.0;
			mc.thePlayer.motionZ = 0.0;
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.thePlayer.motionY += speedValue.getValue() * 0.5;
			}
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY -= speedValue.getValue() * 0.5;
			}
			MoveUtils.strafe(speedValue.getValue());
		}else if (mode.getMode("Hypixel").isToggled()) {
			mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0.05f;
			mc.thePlayer.posY = y;
			if (mc.thePlayer.onGround && stage == 0) {
				mc.thePlayer.motionY = 0.09;
			}
			stage++;
			if (mc.thePlayer.onGround && stage > 2 && !hasClipped) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.15, mc.thePlayer.posZ, false));
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.15, mc.thePlayer.posZ, true));
				hasClipped = true;
			}

			if (doFly) {
				mc.thePlayer.motionY = 0;
				mc.thePlayer.onGround = true;
				timer.timerSpeed = 2;
			} else {
				MoveUtils.setSpeed(0);
				timer.timerSpeed = 5;
			}
		} else if (mode.getMode("Simple").isToggled()) {
			player.capabilities.isFlying = true;
		} else if (mode.getMode("Dynamic").isToggled()) {
			float flyspeed = speedValue.getValue().floatValue();
			player.jumpMovementFactor = 0.4f;
			player.motionX = 0.0;
			player.motionY = 0.0;
			player.motionZ = 0.0;
			player.jumpMovementFactor *= (float) flyspeed * 3f;
			if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
				player.motionY += flyspeed;
			}
			if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
				player.motionY -= flyspeed;
			}
		} else if (mode.getMode("AAC").isToggled()) {

			if (mc.thePlayer.onGround) {
				this.setToggled(false);
				return;
			}

			mc.gameSettings.keyBindForward.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), (state != 1));
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.rotationYaw = yaw;

			if (state == 1) {
				if (p != null) {
					sendPacket(p);
					double dist = 0.13;
					double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
					double x = -sin(yaw) * dist;
					double z = cos(yaw) * dist;
					mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY,
							mc.thePlayer.posZ, false));
					ChatUtils.message("CLIP " + mc.thePlayer.posX + " " + mc.thePlayer.posY + " " + mc.thePlayer.posZ);
				}
				sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308,
						mc.thePlayer.posZ, true));
				p = new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
						mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false);
				ChatUtils.warning("MAKEFLAG " + mc.thePlayer.posX + " " + mc.thePlayer.posY + " " + mc.thePlayer.posZ);
			} else if (state == 2) {
				aactick++;
				if (aactick >= 2) {
					state = 1;
				}
			}

		}else if(mode.getMode("AirWalk").isToggled()){
			mc.thePlayer.motionY = 0;
			mc.thePlayer.onGround = true;
		}

		super.onClientTick(event);
	}

	@Override
	public void onPlayerEventPre(EventPlayerPre event) {
		if(mode.getMode("Motion").isToggled()) {
		 double var25 = mc.thePlayer.posX;
         double Y = mc.thePlayer.posY;
         double Z = mc.thePlayer.posZ;
         String var12;
         float var30;
         double var32;
         double var37;
         
		 Wrapper.INSTANCE.player().onGround = false;
         event.setOnGround(MoveUtils.isOnGround(0.001D) || HackManager.getHack("NoFall").isToggled());
         double var21 = Math.max(speedValue.getValue(), getBaseMoveSpeed());
         if (mc.thePlayer.movementInput.jump)
         {
             mc.thePlayer.motionY = var21 * 0.6D;
         }
         else if (mc.thePlayer.movementInput.sneak)
         {
             mc.thePlayer.motionY = -var21 * 0.6D;
         }
         else
         {
             mc.thePlayer.motionY = 0.0D;
         }
         
         
		}
		super.onPlayerEventPre(event);
	}

	@Override
	public void onMove(EventMove event) {
		if(mode.getMode("Motion").isToggled()) {
			
			 double var24 = speedValue.getValue();
            float var26 = mc.thePlayer.movementInput.moveForward;
            float strafe = mc.thePlayer.movementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;

           // ChatUtils.message("ok");
            if (var26 == 0.0D && strafe == 0.0D)
            {
                event.setX(0.0D);
                event.setZ(0.0D);
            }
            else
            {
                if (var26 != 0.0D)
                {
                    if (strafe > 0.0D)
                    {
                        yaw += (float)(var26 > 0.0D ? -45 : 45);
                    }
                    else if (strafe < 0.0D)
                    {
                        yaw += (float)(var26 > 0.0D ? 45 : -45);
                    }

                    strafe = 0;

                    if (var26 > 0.0D)
                    {
                        var26 = 1;
                    }
                    else if (var26 < 0.0D)
                    {
                        var26 = -1;
                    }
                }

                event.setX(var26 * var24 * cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * var24 * sin(Math.toRadians((double)(yaw + 90.0F))));
                event.setZ(var26 * var24 * sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * var24 * cos(Math.toRadians((double)(yaw + 90.0F))));
            }
        }else if(mode.getMode("Verus").isToggled()) {
        	if (MoveUtils.isMoving()) {
                if (mc.thePlayer.onGround) {
                	MoveUtils.strafe(groundSpeed.getValue());
                    waitTicks++;
                    if (waitTicks >= hopDelay.getValue()) {
                        waitTicks = 0;
                        mc.thePlayer.triggerAchievement(StatList.jumpStat);
                        mc.thePlayer.motionY = 0.0;
                        event.setY(0.41999998688698); 
                    }
                } else {
                    MoveUtils.strafe(airspeed.getValue());
                }
            }
        	
        }
    
		
		super.onMove(event);
	}
	@Override
	public void onJump(EventJump event) {
		event.setCancelled(true);
		super.onJump(event);
	}


	@Override
	public void onBlockBB(EventBlockBB event) {
		if (event.getBlock() instanceof BlockAir && event.getPos().getY() <=launchY) {
			event.boundingBox = AxisAlignedBB.fromBounds(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getPos().getX() + 1.0, launchY,event.getPos().getZ() + 1.0);
		}
		super.onBlockBB(event);
	}
	@Override
	public void onPacketEvent(EventPacket event) {
		if (mode.getMode("AAC").isToggled()) {
			Packet packet = event.getPacket();
			if (event.getType() == EventType.RECIEVE) {

				if (event.getPacket() instanceof S08PacketPlayerPosLook) {
					event.setCancelled(true);
					if (state == 0) {
						mc.thePlayer.setPosition( ((S08PacketPlayerPosLook) packet).getX(),
								((S08PacketPlayerPosLook) packet).getY(), ((S08PacketPlayerPosLook) packet).getZ());
						sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
								mc.thePlayer.posZ, ((S08PacketPlayerPosLook) packet).getYaw(),
								((C03PacketPlayer) packet).getPitch(), false));
						if (mc.thePlayer.posX == lastPosX && mc.thePlayer.posZ == lastPosZ) {
							sameCount++;
							if (sameCount >= 5) {
								state = 1;
								timer.timerSpeed = (float) 0.1;
								sameCount = 0;
								return;
							}
						}
						double dist = 0.13;
						double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
						double x = -sin(yaw) * dist;
						double z = cos(yaw) * dist;
						mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
						sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY,
								mc.thePlayer.posZ, false));
						lastPosX = mc.thePlayer.posX;
						lastPosZ = mc.thePlayer.posZ;
						sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
								1.7976931348623157E+308, mc.thePlayer.posZ, true));
					} else {
						if (timer.timerSpeed <= 1.2) {
							sameCount++;
							if (sameCount >= sameCountReach) {
								sameCount = 0;
								sameCountReach += 13;
								timer.timerSpeed += 0.4;
							}
						}

					}
					if (packet instanceof S40PacketDisconnect) {
						this.setToggled(false);
					}
					
				

				}

			} else if (event.getType() == EventType.SEND) {
				if (packet instanceof C03PacketPlayer && packets.indexOf(packet) == -1) {
					event.setCancelled(true);
				}

			}

		}

		super.onPacketEvent(event);
	}

	/*
	 * @Override public boolean onPacket(Object packet, Side side) { if
	 * (mode.getMode("AAC").isToggled()) {
	 * 
	 * if (side == Side.IN) { if (packet instanceof S08PacketPlayerPosLook) { if
	 * (state == 0) { mc.thePlayer.setPosition(((S08PacketPlayerPosLook)
	 * packet).getX(), ((S08PacketPlayerPosLook) packet).getY(),
	 * ((S08PacketPlayerPosLook) packet).getZ()); sendPacket(new
	 * C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
	 * mc.thePlayer.posZ, ((C03PacketPlayer) packet).getYaw(), ((C03PacketPlayer)
	 * packet).getPitch(), false)); if (mc.thePlayer.posX == lastPosX &&
	 * mc.thePlayer.posZ == lastPosZ) { sameCount++; if (sameCount >= 5) { state =
	 * 1; timer.timerSpeed = (float) 0.1; sameCount = 0; return false; } } double
	 * dist = 0.13; double yaw = Math.toRadians(mc.thePlayer.rotationYaw); double x
	 * = -Math.sin(yaw) * dist; double z = Math.cos(yaw) * dist;
	 * mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY,
	 * mc.thePlayer.posZ + z); sendPacket(new
	 * C03PacketPlayer(mc.thePlayer.posX, mc.thePlayer.posY,
	 * mc.thePlayer.posZ, false)); lastPosX = mc.thePlayer.posX; lastPosZ =
	 * mc.thePlayer.posZ; sendPacket(new
	 * C03PacketPlayer(mc.thePlayer.posX,
	 * 1.7976931348623157E+308, mc.thePlayer.posZ, true)); } else { if
	 * (timer.timerSpeed <= 1.2) { sameCount++; if (sameCount >= sameCountReach) {
	 * sameCount = 0; sameCountReach += 13; timer.timerSpeed += 0.4; } }
	 * 
	 * } if (packet instanceof S40PacketDisconnect) { this.setToggled(false); }
	 * 
	 * } } else if (side == Side.OUT) { if (packet instanceof C03PacketPlayer &&
	 * (((LinkedList<Packet>) packets).indexOf(packet) == -1)) { // return false; }
	 * }
	 * 
	 * } return false;
	 * 
	 * }
	 */
	public void sendPacket(Packet p) {
		packets.add(p);
		Wrapper.INSTANCE.sendPacket(p);
	}
	
    public static double getBaseMoveSpeed()
    {
        double baseSpeed = 0.2873D;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
        {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
        }

        return baseSpeed;
    }
}
