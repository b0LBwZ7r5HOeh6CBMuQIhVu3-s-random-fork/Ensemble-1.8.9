package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Criticals extends Module {

	public ModeValue mode;
	TimerUtils timer;
	boolean cancelSomePackets;

	public static boolean disable;

	BooleanValue debug;

	int targetid;

	private NumberValue jumpHeightValue = new NumberValue("JumpHeight", 0.42D, 0.1D, 0.42D);
	private NumberValue downYValue = new NumberValue("DownY", 0D, 0D, 0.1D);
	private NumberValue hurtTimeValue = new NumberValue("HurtTime", 10D, 0D, 10D);

	private boolean readyCrits = false;
	private boolean canCrits = true;
	private int counter = 0;



	public Criticals() {
		super("Criticals", HackCategory.COMBAT);
		this.mode = new ModeValue("Mode", new Mode("Packet", true), new Mode("Jump", false), new Mode("PJump", false),
				new Mode("AAC5", false), new Mode("NoGround", false), new Mode("Hypixel", false),
				new Mode("Smart", false),new Mode("NewPacket", false),new Mode("NCPPacket", false),new Mode("AAC4", false),new Mode("HOP", false),
				new Mode("TPHOP", false),new Mode("MiniPhase", false),new Mode("NanoPacket", false),new Mode("Non-Calculable", false),new Mode("Invalid", false)
				,new Mode("VerusSmart", true));
		this.debug = new BooleanValue("Debug", true);

		this.addValue(mode, debug,jumpHeightValue,downYValue,hurtTimeValue);

		this.timer = new TimerUtils();
		this.setChinese(Core.Translate_CN[32]);
	}

	@Override
	public String getDescription() {
		return "Changes all your hits to critical hits.";
	}

	@Override
	public void onAttackEntity(AttackEntityEvent event) {

		if (event.target instanceof EntityLivingBase) {
			
			Entity entity = event.target;

			if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.ridingEntity != null || entity.hurtResistantTime > hurtTimeValue.getValue()) {return;}
			
			double x = mc.thePlayer.posX;
			double y = mc.thePlayer.posY;
			double z = mc.thePlayer.posZ;

			String name = mode.getSelectMode().getName();
			if ("NewPacket".equals(name)) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05250000001304, z, true));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00150000001304, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01400000001304, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00150000001304, z, false));
			} else if ("NCPPacket".equals(name)) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1100013579, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0000013579, z, false));
			} else if ("AAC4".equals(name)) {
				mc.thePlayer.motionZ *= 0;
				mc.thePlayer.motionX *= 0;
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3e-14, mc.thePlayer.posZ, true));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 8e-15, mc.thePlayer.posZ, true));
			} else if ("HOP".equals(name)) {
				mc.thePlayer.motionY = 0.1;
				mc.thePlayer.fallDistance = 0.1f;
				mc.thePlayer.onGround = false;
			} else if ("TPHOP".equals(name)) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.02, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01, z, false));
				mc.thePlayer.setPosition(x, y + 0.01, z);
			} else if ("MiniPhase".equals(name)) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.0125, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01275, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.00025, z, true));
			} else if ("NanoPacket".equals(name)) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00973333333333, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.001, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.01200000000007, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.0005, z, false));
			} else if ("Non-Calculable".equals(name)) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1E-5, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1E-7, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1E-6, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1E-4, z, false));
			} else if ("Invalid".equals(name)) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1E+27, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1E+68, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1E+41, z, false));
			} else if ("VerusSmart".equals(name)) {
				counter++;
				if (counter == 1) {
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.001, z, true));
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
				}
				if (counter >= 5)
					counter = 0;
			}
			mc.thePlayer.onCriticalHit(entity);
			readyCrits = true;
			timer.reset();
		}
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (mode.getMode("AAC5").isToggled()) {
			if (mc.thePlayer.onGround) {
				if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime <= 6) {
					mc.thePlayer.motionX *= 0.600151164;
					mc.thePlayer.motionZ *= 0.600151164;
				}
				if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime <= 4) {
					mc.thePlayer.motionX *= 0.800151164;
					mc.thePlayer.motionZ *= 0.800151164;
				}
			} else {
				if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime <= 9) {
					mc.thePlayer.motionX *= 0.8001421204;
					mc.thePlayer.motionZ *= 0.8001421204;
				}
			}

			
		}
		super.onClientTick(event);
	}

	@Override
	public void onPacketEvent(EventPacket event) {
		if (event.getType() == EventType.SEND && mode.getMode("Smart").isToggled()) {

			if (disable) {
				return;
			}

			if (event.getPacket() instanceof C02PacketUseEntity
					&& ((C02PacketUseEntity) event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK) {
				critical(((C02PacketUseEntity) event.getPacket()).getEntityFromWorld(mc.theWorld));
			}
		}
		super.onPacketEvent(event);
	}

	@Override
	public boolean onPacket(Object packet, Side side) {

		if (!mode.getMode("NoGround").isToggled()) {

			if (Wrapper.INSTANCE.player().onGround) {
				if (side == Side.OUT) {
					if (packet instanceof C02PacketUseEntity) {
						C02PacketUseEntity attack = (C02PacketUseEntity) packet;
						if (attack.getAction() == C02PacketUseEntity.Action.ATTACK) {

							Field crit = ReflectionHelper.findField(C02PacketUseEntity.class,
									new String[] { "entityId", "field_149567_a" });
							try {

								if (!crit.isAccessible()) {
									crit.setAccessible(true);
								}

								targetid = crit.getInt(attack);

							} catch (Exception e) {
								////System.out.println(e);
							}

							if (mode.getMode("Packet").isToggled()) {
								if (Wrapper.INSTANCE.player().isCollidedVertically && this.timer.isDelay(500)) {
									Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
											Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.0627,
											Wrapper.INSTANCE.player().posZ, false));
									Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
											Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY,
											Wrapper.INSTANCE.player().posZ, false));
									Entity entity = attack.getEntityFromWorld(Wrapper.INSTANCE.world());
									if (entity != null) {
										Wrapper.INSTANCE.player().onCriticalHit(entity);
									}
									this.timer.setLastMS();
									this.cancelSomePackets = true;
								}
							} else if (mode.getMode("Jump").isToggled()) {
								if (canJump()) {
									Wrapper.INSTANCE.player().jump();
								}
							} else if (mode.getMode("PJump").isToggled()) {
								if (canJump()) {
									Wrapper.INSTANCE.player().jump();
									Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX,
													Wrapper.INSTANCE.player().posY + 0.0031311231111,
													Wrapper.INSTANCE.player().posZ, false));

								}
							} else if (mode.getMode("Hypixel").isToggled()) {
								if (Wrapper.INSTANCE.player().isCollidedVertically && this.timer.isDelay(500)
										&& !HackManager.getHack("Speed").isToggled()) {
									hypixelCrit();

									this.timer.setLastMS();
									this.cancelSomePackets = true;
								}
							}
						}
					} else if (mode.getMode("Packet").isToggled() && packet instanceof C03PacketPlayer) {
						if (cancelSomePackets) {
							cancelSomePackets = false;
							return false;
						}
					}
				}
			}
		} else {
			if (packet instanceof C03PacketPlayer) {
				final C03PacketPlayer p = (C03PacketPlayer) packet;
				Field field = ReflectionHelper.findField(C03PacketPlayer.class,
						new String[] { "onGround", "field_149474_g" });

				try {

					if (!field.isAccessible()) {
						field.setAccessible(true);

					}

					field.setBoolean(p, false);
					// ChatUtils.message(p.isOnGround());
				} catch (Exception e) {
					////System.out.println(e);
				}

			}
		}

		if (debug.getValue() && side == Side.IN) {
			if (packet instanceof S0BPacketAnimation) {
				S0BPacketAnimation s08 = (S0BPacketAnimation) packet;

				if (s08.getAnimationType() == 4 && s08.getEntityID() == targetid) {
					ChatUtils.report("Crit!");
				}

			}
		}

		return true;
	}

	public static void hypixelCrit() {

		double[] arrd = new double[] { 0.0212622959183674, 0.0, 0.0521, 0.02474, 0.01, 0.001 };
		int n = arrd.length;
		int n2 = 0;
		while (n2 < n) {
			double offset = arrd[n2];
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
			++n2;
		}

	}

	boolean canJump() {
		if (Wrapper.INSTANCE.player().isOnLadder()) {
			return false;
		}
		if (Wrapper.INSTANCE.player().isInWater()) {
			return false;
		}
		if (Wrapper.INSTANCE.player().isInLava()) {
			return false;
		}
		if (Wrapper.INSTANCE.player().isSneaking()) {
			return false;
		}
		if (Wrapper.INSTANCE.player().isRiding()) {
			return false;
		}
		if (Wrapper.INSTANCE.player().isPotionActive(Potion.blindness)) {
			return false;
		}
		return true;
	}

	public static void critical(Entity en) {
		if (!mc.thePlayer.onGround) {
			return;
		}
		if ((HackManager.getHack("Flight").isToggled() &&( Fly.mode.getMode("Hypixel").isToggled()))
				&& !mc.thePlayer.isCollidedVertically) {
			return;
		}

		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY;
		double z = mc.thePlayer.posZ;
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05, z, false));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.015, z, false));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
	}

	public static void crit(double xx, double yy, double zz) {

		if (!HackManager.getHack("Criticals").isToggled()) {
			return;
		}
		if (!mc.thePlayer.onGround) {
			return;
		}
		double x = xx;
		double y = yy;
		double z = zz;

		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05, z, false));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.015, z, false));
		Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
	}
}
