package it.fktcod.ktykshrk.module.mods;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JSpinner.NumberEditor;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import it.fktcod.ktykshrk.event.EventWorld;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.addon.Infinite;
import it.fktcod.ktykshrk.module.mods.addon.TeleportResult;
import it.fktcod.ktykshrk.utils.AStarCustomPathFinder;
import it.fktcod.ktykshrk.utils.NodeProcessor;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.NodeProcessor.Node;
import it.fktcod.ktykshrk.utils.math.Vec3;
import it.fktcod.ktykshrk.utils.visual.Colors;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.BlockPos;
import net.minecraft.world.pathfinder.WalkNodeProcessor;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TpAura extends Module {

	private List<Vec3>[] test = new ArrayList[50];
	private List<Vec3>[] test2 = new ArrayList[50];
	public int ticks;
	// settings
	public NumberValue MaxTargets;
	public NumberValue Range;
	public BooleanValue AttackInvisible;
	public BooleanValue AttackPlayer;
	public BooleanValue AttackMobs;
	public BooleanValue ESP;
	public ModeValue mode;
	public NumberValue scantime;
	public NumberValue maxXZTP;
	public NumberValue maxYTP;

	public static boolean isActive = false;
	private int say = 0;
	private double EachDistance = 5;
	private NumberValue CPS;
	private ArrayList<Vec3> path = new ArrayList<>();
	private TimerUtils cps = new TimerUtils();
	private TimerUtils timer1 = new TimerUtils();
	private TimerUtils ScanTimer=new TimerUtils();
	private int delaying = 0;

	private List<EntityLivingBase> targets = new CopyOnWriteArrayList<>();

	private NumberValue dashDistance;

	// M2
	private PathFinder m2path = new PathFinder(new WalkNodeProcessor());
	private NodeProcessor processor = new NodeProcessor();
	public ArrayList<Vec3> back = Lists.newArrayList();

	// god
	public boolean attack = false;
	double x;
	double y;
	double z;
	double xPreEn;
	double yPreEn;
	double zPreEn;
	double xPre;
	double yPre;
	double zPre;
	int stage = 0;

	public static EntityLivingBase T;

	ArrayList<net.minecraft.util.Vec3> positions = new ArrayList<net.minecraft.util.Vec3>();
	ArrayList<net.minecraft.util.Vec3> positionsBack = new ArrayList<net.minecraft.util.Vec3>();
	ArrayList<Node> triedPaths = new ArrayList<Node>();


	public TpAura() {
		super("TpAura", HackCategory.COMBAT);
		MaxTargets = new NumberValue("MaxTargets", 3.0D, 1.0D, 5.0D);
		Range = new NumberValue("Range", 35.0D, 10.0D, 100.0);
		AttackInvisible = new BooleanValue("AttackInvisible", true);
		AttackPlayer = new BooleanValue("AttackPlayer", true);
		AttackMobs = new BooleanValue("AttackMobs", false);
		CPS = new NumberValue("CPS", 1D, 1D, 20D);
		ESP = new BooleanValue("Path", false);
		dashDistance=new NumberValue("Dash", 5D, 1D, 20D);
		mode = new ModeValue("Mode", new Mode("M1", true), new Mode("M2", false), new Mode("M3", false),
				new Mode("God", false), new Mode("Improved", false));
		scantime=new NumberValue("ScanTime", 500D, 500D, 2000D);
		maxXZTP=new NumberValue("MaxXZTP", 9.5D, 1D, 20D);
		maxYTP=new NumberValue("MaxYTP", 9D, 1D, 20D);
		this.setChinese(Core.Translate_CN[97]);
		this.addValue(MaxTargets, Range, AttackInvisible, AttackPlayer, AttackMobs, CPS, ESP, mode,scantime,dashDistance,maxXZTP,maxYTP);
	}

	@Override
	public void onEnable() {
		// God
		this.stage = 0;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.xPreEn = 0;
		this.yPreEn = 0;
		this.zPreEn = 0;
		this.attack = false;
		super.onEnable();
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {

		// if(Wrapper.INSTANCE.mc().gameSettings.keyBindAttack.isKeyDown()) {

		targets = getTargets();
		int delayValue = (20 / (CPS.getValue()).intValue()) * 50;
		int maxtTargets = MaxTargets.getValue().intValue();

			if(ScanTimer.hasReached(scantime.getValue().floatValue()))
			if (targets.size() > 0) {

				if (mode.getMode("M1").isToggled()) {

					test = new ArrayList[50];

					for (int i = 0; i < (targets.size() > maxtTargets ? maxtTargets : targets.size()); i++) {
						T = targets.get(i);
						Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
						Vec3 to = new Vec3(T.posX, T.posY, T.posZ);
						path = computePath(topFrom, to);
						test[i] = path;

						for (Vec3 pathElm : path) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
						}
						if (cps.hasReached(delayValue)) {
						mc.thePlayer.swingItem();
						Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(T, Action.ATTACK));
						}
						Collections.reverse(path);
						
						
						
						for (Vec3 pathElm : path) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
						}
						
						cps.reset();
					}

				} else if (mode.getMode("M2").isToggled()) {
					test2 = new ArrayList[50];

					for (int i = 0; i < (targets.size() > maxtTargets ? maxtTargets : targets.size()); i++) {
						T = targets.get(i);
						processor.getPath(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
								new BlockPos(T.posX, T.posY, T.posZ));
						if (back != null && !back.isEmpty()) {
							test2[i] = back;
						}

						for (Node node : processor.path) {
							BlockPos pos = node.getBlockpos();
							mc.thePlayer.sendQueue.addToSendQueue(
									new C03PacketPlayer.C04PacketPlayerPosition(node.getBlockpos().getX() + 0.5,
											node.getBlockpos().getY(), node.getBlockpos().getZ() + 0.5, true));
							back.add(new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));

						}
						if (cps.hasReached(delayValue)) {
						mc.thePlayer.swingItem();
						Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(T, Action.ATTACK));
						}
						for (int i2 = back.size() - 1; i2 > -1; i2--) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									back.get(i).x, back.get(i).y, back.get(i).z, true));
						}

						back.clear();
						cps.reset();
					}

				} else if (mode.getMode("M3").isToggled()) {

					for (int i = 0; i < (targets.size() > maxtTargets ? maxtTargets : targets.size()); i++) {
						T = targets.get(i);
						processor.getPath(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
								new BlockPos(T.posX, T.posY, T.posZ));
						if (back != null && !back.isEmpty()) {
							test2[i] = back;
						}

						for (Node node : processor.path) {
							BlockPos pos = node.getBlockpos();
							mc.thePlayer.sendQueue.addToSendQueue(
									new C03PacketPlayer.C04PacketPlayerPosition(node.getBlockpos().getX() + 0.5,
											node.getBlockpos().getY(), node.getBlockpos().getZ() + 0.5, true));
							back.add(new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));

						}
						if (cps.hasReached(delayValue)) {
						mc.thePlayer.swingItem();
						Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(T, Action.ATTACK));
						
						}
						Collections.reverse(back);
						for (Vec3 path3 : back) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(path3.x, path3.y, path3.z, true));
						}

						back.clear();
						cps.reset();
					}

				} else if (mode.getMode("God").isToggled()) {

					positions.clear();
					positionsBack.clear();
					for (int i = 0; i < (targets.size() > maxtTargets ? maxtTargets : targets.size()); i++) {
						T = targets.get(i);
						boolean up = false;
						positions.clear();
						positionsBack.clear();
						if (cps.hasReached(delayValue)) {
						doReach(mc.thePlayer.getDistanceToEntity(T), up, T);
						}
						stage = 0;
						cps.reset();
					}

				} else if (mode.getMode("Improved").isToggled()) {

					for (int i = 0; i < (targets.size() > maxtTargets ? maxtTargets : targets.size()); i++) {
						T = targets.get(i);

						positions.clear();
						positionsBack.clear();
						triedPaths.clear();

						TeleportResult result = Infinite.pathFinderTeleportTo(
								new net.minecraft.util.Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
								new net.minecraft.util.Vec3(T.posX, T.posY, T.posZ));
						
						triedPaths = result.triedPaths;
						if(!result.foundPath) {
							continue;
						}
						
						net.minecraft.util.Vec3 lastPos = result.lastPos;
						
						if (cps.hasReached(delayValue)) {
						mc.thePlayer.swingItem();
						Criticals.disable = true;
						Criticals.crit(lastPos.xCoord, lastPos.yCoord, lastPos.zCoord);
						Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(T, C02PacketUseEntity.Action.ATTACK));
						Criticals.disable = false;
						}
						
						positions = result.positions;
						
						TeleportResult resultBack =Infinite.pathFinderTeleportBack(positions);
						
						positionsBack = resultBack.positionsBack;
						cps.reset();
					}
					

				}
				ScanTimer.reset();
			}

		super.onPlayerTick(event);
	}

	private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
		if (!canPassThrow(new BlockPos(topFrom.mc2()))) {
			topFrom = topFrom.addVector(0, 1, 0);
		}

		AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
		pathfinder.compute();
		int i = 0;
		Vec3 lastLoc = null;
		Vec3 lastDashLoc = null;
		ArrayList<Vec3> path = new ArrayList<Vec3>();
		ArrayList<Vec3> pathFinderPath = pathfinder.getPath();

		for (Vec3 pathElm : pathFinderPath) {
			if (i == 0 || i == pathFinderPath.size() - 1) {
				if (lastLoc != null) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
				}

				path.add(pathElm.addVector(0.5, 0, 0.5));
				lastDashLoc = pathElm;
			} else {
				boolean canContinue = true;

				if (pathElm.squareDistanceTo(lastDashLoc) > dashDistance.getValue() * dashDistance.getValue()) {
					canContinue = false;
				} else {
					double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
					double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
					double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
					double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
					double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
					double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
					cordsLoop:

					for (int x = (int) smallX; x <= bigX; x++) {
						for (int y = (int) smallY; y <= bigY; y++) {
							for (int z = (int) smallZ; z <= bigZ; z++) {
								if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
									canContinue = false;
									break cordsLoop;
								}
							}
						}
					}
				}

				if (!canContinue) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
					lastDashLoc = lastLoc;
				}
			}

			lastLoc = pathElm;
			i++;
		}

		return path;
	}

	public List<EntityLivingBase> getTargets() {
		List<EntityLivingBase> targets = new ArrayList<>();

		for (Object o : Wrapper.INSTANCE.mc().theWorld.getLoadedEntityList()) {
			if (o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) o;
				if (entity != null && !entity.isDead && !Wrapper.INSTANCE.mc().thePlayer.isDead
						&& (entity instanceof EntityLivingBase) && !(entity == Wrapper.INSTANCE.mc().thePlayer)
						&& Wrapper.INSTANCE.mc().thePlayer.getDistanceToEntity(entity) <= Range.getValue()
						&& !entity.isPlayerSleeping()) {
					if (entity instanceof EntityPlayer && AttackPlayer.getValue() && !entity.isInvisible()) {
						targets.add(entity);
					} else if (entity instanceof EntityPlayer && AttackPlayer.getValue() && entity.isInvisible()
							&& !AttackInvisible.getValue()) {
						targets.remove(entity);
					} else if (entity instanceof EntityPlayer && AttackPlayer.getValue() && entity.isInvisible()
							&& AttackInvisible.getValue()) {

						targets.add(entity);
					}

					if (!(entity instanceof EntityPlayer) && AttackMobs.getValue() && !entity.isInvisible()) {
						targets.add(entity);
					} else if (!(entity instanceof EntityPlayer) && AttackMobs.getValue() && entity.isInvisible()
							&& !AttackInvisible.getValue()) {
						targets.remove(entity);
					} else if (!(entity instanceof EntityPlayer) && AttackMobs.getValue() && entity.isInvisible()
							&& AttackInvisible.getValue()) {
						targets.add(entity);
					}

					if (HackManager.getHack("Teams").isToggled() && !ValidUtils.isTeam(entity)) {
						targets.remove(entity);
					}

					if (!ValidUtils.isFriendEnemy(entity)) {
						targets.remove(entity);
					}

				}
			}
		}

		targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(Wrapper.INSTANCE.mc().thePlayer) * 1000
				- o2.getDistanceToEntity(Wrapper.INSTANCE.mc().thePlayer) * 1000));
		return targets;
	}

	private boolean canPassThrow(BlockPos pos) {
		Block block = Minecraft.getMinecraft().theWorld
				.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
		return block.getMaterial() == Material.air || block.getMaterial() == Material.plants
				|| block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water
				|| block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
	}

	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (mode.getMode("M1").isToggled()) {
			if (ESP.getValue() && !path.isEmpty()) {
				for (int i = 0; i < targets.size(); i++) {
					try {
						if (test != null)
							for (Vec3 pos : test[i]) {
								if (pos != null) {
									drawPath(pos);
								}
							}
					} catch (Exception e) {
					}
				}

				if (cps.check(1000)) {
					test = new ArrayList[50];
					path.clear();
				}
			} else if (mode.getMode("M2").isToggled()) {
				if (ESP.getValue() && !back.isEmpty()) {
					for (int i = 0; i < targets.size(); i++) {
						try {
							if (back != null)
								for (Vec3 pos : test[i]) {
									if (pos != null) {
										drawPath(pos);
									}
								}
						} catch (Exception e) {
						}
					}
					/*
					 * if (cps.check(1000)) { test2 = new ArrayList[50]; back.clear(); }
					 */
				}
			}
		}

		super.onRenderWorldLast(event);
	}

	public void drawPath(Vec3 vec) {

		try {
			Field renderPosX = ReflectionHelper.findField(RenderManager.class,
					new String[] { "renderPosX", "field_78725_b" });
			if (!renderPosX.isAccessible())
				renderPosX.setAccessible(true);

			Field renderPosY = ReflectionHelper.findField(RenderManager.class,
					new String[] { "renderPosY", "field_78726_c" });
			if (!renderPosY.isAccessible())
				renderPosY.setAccessible(true);

			Field renderPosZ = ReflectionHelper.findField(RenderManager.class,
					new String[] { "renderPosZ", "field_78723_d" });
			if (!renderPosZ.isAccessible())
				renderPosZ.setAccessible(true);

			double x = vec.getX() - renderPosX.getDouble(mc.getRenderManager());
			double y = vec.getY() - renderPosY.getDouble(mc.getRenderManager());
			double z = vec.getZ() - renderPosZ.getDouble(mc.getRenderManager());

			double width = 0.3;
			double height = mc.thePlayer.getEyeHeight();
			RenderUtils.pre3D();
			GL11.glLoadIdentity();

			Method method = ReflectionHelper.findMethod(EntityRenderer.class, mc.entityRenderer,
					new String[] { "setupCameraTransform", "func_78479_a" }, float.class, int.class);
			method.invoke(mc.entityRenderer, new Object[] { new Float(timer.renderPartialTicks), new Integer(2) });

			int colors[] = { Colors.getColor(Color.black), Colors.getColor(Color.white) };

			for (int i = 0; i < 2; i++) {
				RenderUtils.glColor(colors[i]);
				GL11.glLineWidth(3 - i * 2);
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex3d(x - width, y, z - width);
				GL11.glVertex3d(x - width, y, z - width);
				GL11.glVertex3d(x - width, y + height, z - width);
				GL11.glVertex3d(x + width, y + height, z - width);
				GL11.glVertex3d(x + width, y, z - width);
				GL11.glVertex3d(x - width, y, z - width);
				GL11.glVertex3d(x - width, y, z + width);
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex3d(x + width, y, z + width);
				GL11.glVertex3d(x + width, y + height, z + width);
				GL11.glVertex3d(x - width, y + height, z + width);
				GL11.glVertex3d(x - width, y, z + width);
				GL11.glVertex3d(x + width, y, z + width);
				GL11.glVertex3d(x + width, y, z - width);
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex3d(x + width, y + height, z + width);
				GL11.glVertex3d(x + width, y + height, z - width);
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex3d(x - width, y + height, z + width);
				GL11.glVertex3d(x - width, y + height, z - width);
				GL11.glEnd();
			}

		} catch (Exception e) {

		}
		RenderUtils.post3D();
	}

	public void doReach(double range, boolean up, EntityLivingBase target) {
		if (mc.thePlayer.getDistanceToEntity(target) <= KillAura.range.getValue()) {
			mc.thePlayer.swingItem();
			mc.playerController.attackEntity(mc.thePlayer, target);
			return;
		}
		attack = Infinite.infiniteReach(range, maxXZTP.getValue(), (double) maxYTP.getValue(), positionsBack, positions, target);
	}
	
	@Override
	public void onWorld(EventWorld event) {
		this.setToggled(false);
		super.onWorld(event);
	}

}
