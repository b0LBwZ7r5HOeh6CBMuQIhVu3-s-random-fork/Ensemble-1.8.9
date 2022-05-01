package it.fktcod.ktykshrk.module.mods;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.text.DefaultEditorKit.CutAction;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import it.fktcod.ktykshrk.managers.FontManager;
import it.fktcod.ktykshrk.managers.FriendManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.EntityUtils;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.PlayerControllerUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.RotationUtil;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Aura extends Module {

	public static EntityLivingBase target, vip, blockTarget;
	private ModeValue Priority;
	private ModeValue rotatios;
	private ModeValue blockmode;
	private ModeValue mode;
	private ModeValue type;
	public static NumberValue MaxCPS;
	public static NumberValue MinCPS;
	public static NumberValue reach;
	public static NumberValue blockrange;
	public static NumberValue Yaw;
	private NumberValue switchdelay;
	public static BooleanValue players;
	public static BooleanValue interact;
	public static BooleanValue walls;
	public static BooleanValue animals;
	public static BooleanValue invis;
	public static BooleanValue blocking;
	public static BooleanValue death;
	public static BooleanValue esp;
	// private final String PITCH = "PITCH";
	private TimerUtils switchTimer = new TimerUtils();
	
	public static float sYaw, sPitch, aacB;
	private double fall;
	int[] randoms = { 0, 1, 0 };
	public static  boolean isBlocking = false;
	public static boolean isSetup = false;
	private TimerUtils newTarget = new TimerUtils();
	private TimerUtils lastStep = new TimerUtils();
	private TimerUtils rtimer = new TimerUtils();
	private Vector2f lastAngles = new Vector2f(0.0F, 0.0F);

	private List<EntityLivingBase> loaded = new CopyOnWriteArrayList<>();
	private int index, timer, crits, groundTicks;

	Minecraft mc = Wrapper.INSTANCE.mc();

	public Aura() {
		super("Aura", HackCategory.COMBAT);
		this.Priority = new ModeValue("Priority", new Mode("Angle", false), new Mode("Range", true),
				new Mode("Fov", false), new Mode("Armor", false), new Mode("Health", false));
		this.rotatios = new ModeValue("RotaMode", new Mode("Basic", true), new Mode("Smooth", false),
				new Mode("Legit", false), new Mode("Predict", false));
		this.blockmode = new ModeValue("BlockMode", new Mode("NCP", true), new Mode("Hypixel", false),
				new Mode("Basic1", false), new Mode("Basic2", false));
		this.mode = new ModeValue("AuraMode", new Mode("Switch", true), new Mode("Single", false),
				new Mode("Multi", false), new Mode("Multi2", false));
		this.type = new ModeValue("TypeMode", new Mode("Pre", true), new Mode("Post", false));

		this.MaxCPS = new NumberValue("MaxCPS", 12.0, 1.0, 20.0);
		this.MinCPS = new NumberValue("MinCPS", 12.0, 1.0, 20.0);
		this.reach = new NumberValue("Range", 4.5, 1.0, 8.0);
		this.blockrange = new NumberValue("BlockRange", 4.5, 1.0, 12.0);
		this.Yaw = new NumberValue("Yaw", 50.0, 1.0, 180.0);
		this.switchdelay = new NumberValue("SwitchDelay", 11.0, 1.0, 50.0);

		this.players = new BooleanValue("Players", true);
		this.interact = new BooleanValue("Interact", true);
		this.walls = new BooleanValue("Walls", true);
		this.animals = new BooleanValue("Animals", false);
		this.invis = new BooleanValue("Invisibles", false);
		this.blocking = new BooleanValue("Autoblock", true);
		this.death = new BooleanValue("Death", true);
		this.esp = new BooleanValue("ESP", true);

		this.addValue(this.Priority, this.rotatios, this.blockmode, this.mode, this.type, this.MaxCPS, this.MinCPS,
				this.reach, this.blockrange, this.Yaw, this.switchdelay, this.players, this.interact, this.walls,
				this.animals, this.invis, this.blocking, this.death, this.esp);
		this.setChinese(Core.Translate_CN[9]);
	
		
	}

	public static boolean isSetupTick() {
		return isSetup;
	}

	@Override
	public void onEnable() {
		if (mc.thePlayer != null) {
			sYaw = mc.thePlayer.rotationYaw;
			sPitch = mc.thePlayer.rotationPitch;
			lastAngles.x = mc.thePlayer.rotationYaw;
			loaded.clear();
			isBlocking = mc.thePlayer.isBlocking() ? true : false;
		}
		newTarget.reset();
		timer = 20;
		groundTicks = MoveUtils.isOnGround(0.01) ? 1 : 0;
		aacB = 0;
	}

	@Override
	public void onDisable() {
		loaded.clear();
		if (mc.thePlayer == null)
			return;
		if (isBlocking && hasSword() && mc.thePlayer.getItemInUseCount() == 999)
			unBlock();

		target = null;
		blockTarget = null;
		if (mc.thePlayer != null) {
			lastAngles.x = mc.thePlayer.rotationYaw;
		}
	}
	@Override
	public void onRenderGameOverlay(Text event) {
	
		ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
		if(target!=null) {
			float health =target.getHealth();
			if (health > 20) {
                health = 20;
            }
			int red = (int) Math.abs((((health * 5) * 0.01f) * 0) + ((1 - (((health * 5) * 0.01f))) * 255));
            int green = (int) Math.abs((((health * 5) * 0.01f) * 255) + ((1 - (((health * 5) * 0.01f))) * 0));
            Color customColor = new Color(red, green, 0).brighter();
			
			Wrapper.INSTANCE.fontRenderer().drawString("\2474\u2764\247f", sr.getScaledWidth()/2-Wrapper.INSTANCE.fontRenderer().getStringWidth("\2474\u2764\247f"), sr.getScaledHeight()/2-15, customColor.getRGB());
			//Wrapper.INSTANCE.fontRenderer().drawString("\2474\u2764\247f"+(int)target.getHealth(), sr.getScaledWidth()/2, sr.getScaledHeight()/2+5, customColor.getRGB());
		it.fktcod.ktykshrk.Core.fontManager.getFont("SFM 7").drawString(""+(int)target.getHealth(), sr.getScaledWidth()/2, sr.getScaledHeight()/2-15, customColor.getRGB());
		Wrapper.INSTANCE.fontRenderer().drawString(target.getName(), sr.getScaledWidth()/2-Wrapper.INSTANCE.fontRenderer().getStringWidth("\2474\u2764\247f"), sr.getScaledHeight()/2-24,0xFFFFFFFF);
		
		}
		
		super.onRenderGameOverlay(event);
	}
	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		final float cps = randomfloat(this.MinCPS.getValue(), this.MaxCPS.getValue());
			boolean shouldMiss = randomfloat2(0, 100) > 100;
			EntityLivingBase newT = getOptimalTarget(this.reach.getValue());

			timer++;
			if ((Boolean) this.death.getValue()) {
				if ((!mc.thePlayer.isEntityAlive()
						|| (mc.currentScreen != null && mc.currentScreen instanceof GuiGameOver))) {
					this.setToggled(false);
					ChatUtils.warning("Aura Death");
					return;
				}
				if (mc.thePlayer.ticksExisted <= 1) {
					this.setToggled(false);
					ChatUtils.warning("Aura Death");
					return;
				}
			}

			blockTarget = getOptimalTarget(this.blockrange.getValue());

			if (this.mode.getMode("Multi").isToggled()) {
				loaded = getTargets(this.reach.getValue());
				if (loaded.size() > 0) {
					target = loaded.get(0);
					final float[] rot =Utils.getRotationsNeeded(target);
					
					sYaw = rot[0];
					sPitch = rot[1];
					if (this.blocking.getValue()) {
						if (isBlocking && hasSword())
							unBlock();
					}
					// final float cps = randomfloat(this.MinCPS.getValue(),
					// this.MaxCPS.getValue());
					if (timer >= 20 / cps) {
						timer = 0;
						if (isBlocking) {
							if (hasSword())
								unBlock();
						}
						if (loaded.size() >= 1 && !shouldMiss) {
					
							mc.thePlayer.swingItem();
						}

						for (EntityLivingBase targ : loaded) {
							if (!shouldMiss) {
								mc.thePlayer.sendQueue
										.addToSendQueue(new C02PacketUseEntity(targ, C02PacketUseEntity.Action.ATTACK));
								mc.thePlayer.attackTargetEntityWithCurrentItem(targ);

							}
						}
						if (this.blocking.getValue()) {
							if (this.blockmode.getMode("Basic1").isToggled()) {
								if (hasSword())
									block(target);
							}
						}
					} else if (this.blocking.getValue()) {
						if (this.blockmode.getMode("NCP").isToggled()
								|| this.blockmode.getMode("Hypixel").isToggled()) {
							if (hasSword() && isBlocking)
								unBlock();
						}
					}

				}
			} else if (this.mode.getMode("Switch").isToggled()) {

				if (switchTimer.delay(this.switchdelay.getValue().floatValue())) {
					loaded = getTargets(this.reach.getValue());
				}
				if (index >= loaded.size()) {
					index = 0;
				}

				if (loaded.size() > 0) {
					if (this.switchTimer.delay(this.switchdelay.getValue().floatValue())) {
						this.incrementIndex();
						this.switchTimer.reset();
					}

				} else {
					// lastAngles.x = mc.thePlayer.rotationYaw;
				}

			} else if (this.mode.getMode("Multi2").isToggled()) {
				loaded = getTargets(this.reach.getValue());
				if (index >= loaded.size())
					index = 0;

				if (timer >= 20 / cps && loaded.size() > 0) {
					index += 1;
					if (index >= loaded.size()) {
						index = 0;
					}
				} else {
				}
				if (loaded.size() > 0) {
					newT = loaded.get(index);
				} else {
					newT = null;
				}
			}
			if (target != newT) {
				newTarget.reset();
				if (!(this.mode.getMode("Multi2").isToggled()))
					shouldMiss = true;
				target = newT;
				if (target == null) {
					sYaw = mc.thePlayer.rotationYaw;
					sPitch = mc.thePlayer.rotationPitch;
				}
			}
			if (target != null) {
				if ((!validEntity(target, this.reach.getValue()) || !mc.theWorld.loadedEntityList.contains(target))
						&& this.mode.getMode("Switch").isToggled()) {
					loaded = getTargets(this.reach.getValue());
					index += 1;
					if (index >= loaded.size())
						index = 0;
					return;
				}
				if (!validEntity(target, this.reach.getValue()) && this.mode.getMode("Multi2").isToggled() ) {
					loaded = getTargets(this.reach.getValue());

					return;
				}

				float[] rot = Utils.getRotationsNeeded(target);
				if (this.rotatios.getMode("Basic").isToggled()) {
					Random rand = new Random();
					double range1 = ((Number) this.reach.getValue()).floatValue();
					float[] NeedRotation = Utils.getRotationsNeeded(target);
					if (NeedRotation == null) {
						return;
					}
				/*
				 * float targetYaw = MathHelper.clamp_float(
				 * RotationUtil.getYawChangeGiven(target.posX, target.posZ, lastAngles.x),
				 * -180.0F, 180.0F);
				 */

				/*
				 * int yawspeed = (int) ((Number) (this.Yaw.getValue()).intValue()); if
				 * (targetYaw > (float) yawspeed) { targetYaw = (float) yawspeed; } else if
				 * (targetYaw < (float) -yawspeed) { targetYaw = (float) (-yawspeed); }
				 * em.setYaw(lastAngles.x += targetYaw / 1.1F); em.setPitch(NeedRotation[1]);
				 * sYaw = lastAngles.x; sPitch = NeedRotation[1]; // ChatUtil.printChat("");
				 */					// ChatUtil.printChat("pitch"+(NeedRotation[1]+rand.nextFloat()-rand.nextFloat()));
				}
			

				if (timer >= 20 / cps && (this.type.getMode("Pre").isToggled())) {
					timer = 0;
					int XR = (int) randomfloat2(1, -1);
					int YR = (int) randomfloat2(1, -1);
					int ZR = (int) randomfloat2(1, -1);
					randoms[0] = XR;
					randoms[1] = YR;
					randoms[2] = ZR;
					float neededYaw = RotationUtil.getYawChange(sYaw, target.posX, target.posZ);
					if (this.rotatios.getMode("Legit").isToggled())
						neededYaw = getCustomRotsChange(sYaw, sPitch, target.posX, target.posY, target.posZ)[0];
					float interval = 60 - mc.thePlayer.getDistanceToEntity(target) * 10;
					if (this.rotatios.getMode("Legit").isToggled())
						interval = 50 - mc.thePlayer.getDistanceToEntity(target) * 10;
					if (neededYaw > interval || neededYaw < -interval || !newTarget.delay((float) 70))
						shouldMiss = true;
					if (!shouldMiss || this.mode.getMode("Multi2").isToggled())
						hitEntity(target, this.blocking.getValue());
					else
						mc.thePlayer.swingItem();
				}
			}

			if (blockTarget != null) {
				if (this.blocking.getValue()) {
					if (hasSword()) {
						if (this.blockmode.getMode("NCP").isToggled()
								|| this.blockmode.getMode("Hypixel").isToggled()) {
							if (hasSword() && isBlocking)
								unBlock();
						} else if (mc.thePlayer.getItemInUseCount() == 0) {
							block(blockTarget);
						}
					} 
				 else if (mc.thePlayer.getItemInUseCount() == 999) {
					if (isBlocking && hasSword())
						unBlock();
				}

			} else {
				if (mc.thePlayer.getItemInUseCount() == 999) {
					if (isBlocking && hasSword()) {
						unBlock();
					}
				}
			}
		} else {
			if (this.type.getMode("Post").isToggled() && target != null) {
				timer = 0;
				int XR = (int) randomfloat2(1, -1);
				int YR = (int) randomfloat2(1, -1);
				int ZR = (int) randomfloat2(1, -1);
				randoms[0] = XR;
				randoms[1] = YR;
				randoms[2] = ZR;
				boolean shouldMiss1 = randomfloat2(0, 100) > 100;
				float neededYaw = RotationUtil.getYawChange(sYaw, target.posX, target.posZ);
				if (this.rotatios.getMode("Legit").isToggled())
					neededYaw = getCustomRotsChange(sYaw, sPitch, target.posX, target.posY, target.posZ)[0];
				float interval = 60 - mc.thePlayer.getDistanceToEntity(target) * 10;
				if (this.rotatios.getMode("Legit").isToggled())
					interval = 50 - mc.thePlayer.getDistanceToEntity(target) * 10;
				if (neededYaw > interval || neededYaw < -interval || !newTarget.delay((float) 70))
					shouldMiss1 = true;
				if (!shouldMiss1 || this.mode.getMode("Multi2").isToggled())
					hitEntity(target, this.blocking.getValue());
				else
					mc.thePlayer.swingItem();
			}
			if (blockTarget != null) {
				if (!isBlocking && this.blocking.getValue()) {
					if (hasSword()) {
						if (this.blockmode.getMode("Hypixel").isToggled())
							blockHypixel(blockTarget);
						else if (this.blockmode.getMode("NCP").isToggled())
							block(blockTarget);
						else if (this.blockmode.getMode("Basic2").isToggled()&& timer == 0)
							block(blockTarget);
					}
				}
			}

		}
		super.onPlayerTick(event);
	}

	private void incrementIndex() {
		++this.index;
		if (this.index >= loaded.size()) {
			this.index = 0;
		}

	}

	private EntityLivingBase getOptimalTarget(double range) {
		List<EntityLivingBase> load = new ArrayList<>();
		// List entities =
		// mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer,
		// mc.thePlayer.boundingBox.expand(range, range, range));
		// for (Object o : entities) {
		for (Object o : mc.theWorld.loadedEntityList) {
			if (o instanceof EntityLivingBase) {

				EntityLivingBase ent = (EntityLivingBase) o;
				if (validEntity(ent, range)) {
					if (ent == vip) {
						return ent;
					}
					load.add(ent);
				}
			}
		}
		if (load.isEmpty()) {
			return null;
		}
		return getTarget(load);
	}

	boolean validEntity(EntityLivingBase entity, double range) {
		if ((mc.thePlayer.isEntityAlive()) && !(entity instanceof EntityPlayerSP)) {
			if (mc.thePlayer.getDistanceToEntity(entity) <= range) {
				if (!RotationUtil.canEntityBeSeen(entity) && !(Boolean) this.walls.getValue())
					return false;
				if (ValidUtils.isBot(entity) || entity.isPlayerSleeping())
					return false;
				if (!ValidUtils.isFriendEnemy(entity))
					return false;
				if (entity instanceof EntityPlayer) {
					if (this.players.getValue()) {
						EntityPlayer player = (EntityPlayer) entity;
						if (!player.isEntityAlive() && player.getHealth() == 0.0) {
							return false;
						} else if (!ValidUtils.isTeam(entity))
							return false;
						else if (player.isInvisible() && !(Boolean) this.invis.getValue()) {
							return false;
						}
						} else
							return true;
					}
				} else {
					if (!entity.isEntityAlive())
						return false;
				}
				if (this.animals.getValue()) {
					if (entity instanceof EntityMob || entity instanceof EntityIronGolem
							|| entity instanceof EntityAnimal || entity instanceof EntityVillager) {
						if (entity.getName().equals("Villager") && entity instanceof EntityVillager) {
							return false;
						}
						return true;
					}
				}
			
}
		return false;
	}

	private EntityLivingBase getTarget(List<EntityLivingBase> list) {
		sortList(list);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	private void sortList(List<EntityLivingBase> weed) {

		if (this.Priority.getMode("Range").isToggled()) {
			weed.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) * 1000
					- o2.getDistanceToEntity(mc.thePlayer) * 1000));

		}
		if (this.Priority.getMode("Fov").isToggled()) {
			weed.sort(Comparator.comparingDouble(o -> RotationUtil
					.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, RotationUtil.getRotations(o)[0])));
		}
		if (this.Priority.getMode("Angle").isToggled()) {
			weed.sort((o1, o2) -> {
				float[] rot1 = RotationUtil.getRotations(o1);
				float[] rot2 = RotationUtil.getRotations(o2);
				return (int) ((mc.thePlayer.rotationYaw - rot1[0]) - (mc.thePlayer.rotationYaw - rot2[0]));
			});
		}
		if (this.Priority.getMode("Health").isToggled()) {
			weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
		}
		if (this.Priority.getMode("Armor").isToggled()) {
			weed.sort(Comparator
					.comparingInt(o -> (o instanceof EntityPlayer ? ((EntityPlayer) o).inventory.getTotalArmorValue()
							: (int) o.getHealth())));

		}
	}

	public static float randomfloat(Double double1, Double double2) {
		return (float) (double2 + (float) Math.random() * ((double1 - double2)));
	}

	public static float randomfloat2(int i, int j) {
		return (float) (j + (float) Math.random() * ((i - j)));
	}

	/*
	 * private void smoothAim(EventPreUpdate em) { double randomYaw = 0.05; double
	 * randomPitch = 0.05; float targetYaw = RotationUtil.getYawChange(sYaw,
	 * target.posX + randomfloat2(1, -1) * randomYaw, target.posZ + randomfloat2(1,
	 * -1) * randomYaw); float yawFactor = targetYaw / 1.7F; em.setYaw(sYaw +
	 * yawFactor); sYaw += yawFactor; float targetPitch =
	 * RotationUtil.getPitchChange(sPitch, target, target.posY + randomfloat2(1, -1)
	 * * randomPitch); float pitchFactor = targetPitch / 1.7F; em.setPitch(sPitch +
	 * pitchFactor); sPitch += pitchFactor; }
	 */

	private void block(EntityLivingBase ent) {
		isBlocking = true;
		if ((Boolean) this.interact.getValue()) {
			mc.thePlayer.sendQueue
					.addToSendQueue(new C02PacketUseEntity(ent, new Vec3((double) randomNumber(-50, 50) / 100,
							(double) randomNumber(0, 200) / 100, (double) randomNumber(-50, 50) / 100)));
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, Action.INTERACT));
		}

		mc.thePlayer.sendQueue
				.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
	}

	private void blockHypixel(EntityLivingBase ent) {
		isBlocking = true;
		if ((Boolean) this.interact.getValue()) {
			mc.thePlayer.sendQueue
					.addToSendQueue(new C02PacketUseEntity(ent, new Vec3((double) randomNumber(-50, 50) / 100,
							(double) randomNumber(0, 200) / 100, (double) randomNumber(-50, 50) / 100)));
			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, Action.INTERACT));
		}
		mc.thePlayer.sendQueue.addToSendQueue(
				new C08PacketPlayerBlockPlacement(Utils.getHypixelBlockpos(mc.getSession().getUsername()), 255,
						mc.thePlayer.inventory.getCurrentItem(), 0, 0, 0));
	}

	public static int randomNumber(int max, int min) {
		return (int) (Math.random() * (double) (max - min)) + min;
	}

	private void unBlock() {
		isBlocking = false;
		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
				BlockPos.ORIGIN, EnumFacing.DOWN));
	}

	private boolean hasSword() {
		return mc.thePlayer.inventory.getCurrentItem() != null
				&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
	}

	private void hitEntity(EntityLivingBase ent, boolean shouldBlock) {
		if (isBlocking) {
			if (hasSword())
				unBlock();
		}
		C0BPacketEntityAction act = new C0BPacketEntityAction(mc.thePlayer,
				net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SPRINTING);
		// mc.thePlayer.sendQueue.addToSendQueue(act);
		mc.thePlayer.swingItem();
		mc.playerController.attackEntity(mc.thePlayer, target);
		if (shouldBlock) {
			if (this.blockmode.getMode("Basic1").isToggled()) {
				if (hasSword())
					block(ent);
			}
		}

		
	}

	private List<EntityLivingBase> getTargets(double range) {
		List<EntityLivingBase> targets = new ArrayList<>();
		Iterator var2 = mc.theWorld.getLoadedEntityList().iterator();
		while (var2.hasNext()) {
			Object o = var2.next();
			if (o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) o;
				if (this.validEntity(entity, range)) {
					targets.add(entity);
				}
			}
		}
		sortList(targets);
		return targets;
	}


	/*
	 * public void customRots(EventPreUpdate em, EntityLivingBase ent) { double
	 * randomYaw = 0.05; double randomPitch = 0.05; float[] rotsN =
	 * getCustomRotsChange(sYaw, sPitch, target.posX + randomfloat2(1, -1) *
	 * randomYaw, target.posY + randomfloat2(1, -1) * randomPitch, target.posZ +
	 * randomfloat2(1, -1) * randomYaw); float targetYaw = rotsN[0]; float yawFactor
	 * = targetYaw * targetYaw / (4.7f * targetYaw); if (targetYaw < 5) { yawFactor
	 * = targetYaw * targetYaw / (3.7f * targetYaw); } if (Math.abs(yawFactor) > 7)
	 * { aacB = yawFactor * 7; yawFactor = targetYaw * targetYaw / (3.7f *
	 * targetYaw); } else { yawFactor = targetYaw * targetYaw / (6.7f * targetYaw) +
	 * aacB; }
	 * 
	 * em.setYaw(sYaw + yawFactor); sYaw += yawFactor; float targetPitch = rotsN[1];
	 * float pitchFactor = targetPitch / 3.7F; em.setPitch(sPitch + pitchFactor);
	 * sPitch += pitchFactor; }
	 */

	public float[] getCustomRotsChange(float yaw, float pitch, double x, double y, double z) {

		double xDiff = x - mc.thePlayer.posX;
		double zDiff = z - mc.thePlayer.posZ;
		double yDiff = y - mc.thePlayer.posY;

		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		double mult = (1 / (dist + 0.0001)) * 2;
		if (mult > 0.2)
			mult = 0.2;
		if (!mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox())
				.contains(target)) {
			x += 0.3 * randoms[0];
			y -= 0.4 + mult * randoms[1];
			z += 0.3 * randoms[2];
		}
		xDiff = x - mc.thePlayer.posX;
		zDiff = z - mc.thePlayer.posZ;
		yDiff = y - mc.thePlayer.posY;
		float yawToEntity = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitchToEntity = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity)),
				-MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F };
	}

	static enum priority {
		Angle, Range, Fov, Armor, Health
	}

	static enum RotaMode {
		Basic, Smooth, Legit, Predict
	}

	static enum BlockMode {
		NCP, Hypixel, Basicl, Basic1, Basic2
	}

	static enum Multi2 {
		Switch, Single, Multi, Multi2
	}

	static enum TypeMode {
		Pre, Post
	}
}
