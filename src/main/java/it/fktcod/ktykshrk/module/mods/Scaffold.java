package it.fktcod.ktykshrk.module.mods;

import java.io.IOException;
import java.util.*;

import ensemble.mixin.cc.mixin.interfaces.IC03Packet;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventLoop;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.event.EventPlayerPost;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.addon.Rotation;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.PlayerUtils;
import it.fktcod.ktykshrk.utils.RobotUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.A03A59A2;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.utils.visual.RotationUtil;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Scaffold extends Module {
	public final static List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace,
			Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air,
			Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer,
			Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
			Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
			Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab,
			Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower,
			Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder,
			Blocks.web);
	private final List<Block> validBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
			Blocks.flowing_lava);
	private final BlockPos[] blockPositions = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0),
			new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
	private final EnumFacing[] facings = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH,
			EnumFacing.NORTH };
	private final TimerUtils towerStopwatch = new TimerUtils();
	private final Random rng = new Random();
	private float[] angles = new float[2];
	private boolean rotating;
	private int slot;
	private ItemStack currentblock;
	public ModeValue mode;

	public TimerUtils timer;
	public BlockData blockData;
	boolean isBridging = false;
	BlockPos blockDown = null;
	public static float[] facingCam = null;
	float startYaw = 0;
	float startPitch = 0;

	BooleanValue tower;
	BooleanValue towerboost;
	private int towerTick;
	public int godBridgeTimer;

	boolean sneaking;
	double y;

	private static final Rotation rotation = new Rotation(999.0f, 999.0f);

	public static BooleanValue sprint;
	public static NumberValue legit;
	public NumberValue expand;
	public BooleanValue samey;

	public BooleanValue timervalue;
	public NumberValue timerspeed;

	public Scaffold() {
		super("Scaffold", HackCategory.PLAYER);
		this.mode = new ModeValue("Mode", new Mode("AAC", false), new Mode("Simple", false), new Mode("Hypixel", true),
				new Mode("Hanabi", false), new Mode("GodBridge", false), new Mode("Legit", false),
				new Mode("Mineland", false));

		this.tower = new BooleanValue("Tower", false);
		this.towerboost = new BooleanValue("TowerBoost", false);
		this.sprint = new BooleanValue("Sprint", false);
		this.legit = new NumberValue("Speed", 1D, 0.1D, 3D);
		this.expand = new NumberValue("Expand", 0.4, 0D, 5D);
		this.samey = new BooleanValue("Samey", false);

		timervalue=new BooleanValue("Timer",false);
		timerspeed=new NumberValue("TimerSpeed",1.4D,1D,2D);


		this.addValue(mode, tower, towerboost, sprint, legit, expand, samey,timervalue,timerspeed);

		this.timer = new TimerUtils();
		this.setChinese(Core.Translate_CN[80]);
	}

	@Override
	public String getDescription() {
		return "Automatically places blocks below your feet.";
	}

	@Override
	public void onDisable() {

		rotation.setYaw(999.0f);
		rotation.setPitch(999.0f);
		Wrapper.timer.timerSpeed = 1.0f;

		// HackManager.getHack("SafeWalk").setToggled(false);
		if (mode.getMode("AAC").isToggled()) {

		}
		facingCam = null;

		if (sneaking && !mc.thePlayer.isSneaking()) {
			C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING);
			mc.thePlayer.sendQueue.addToSendQueue(p);
		}

		super.onDisable();
	}

	@Override
	public void onEnable() {
		a03c01(Core.iloveu);
		blockDown = null;
		facingCam = null;
		isBridging = false;
		startYaw = 0;
		startPitch = 0;
		y = mc.thePlayer.posY;
		if (HackManager.getHack("KillAura").isToggled()) {
			HackManager.getHack("KillAura").setToggled(false);
			ChatUtils.warning("Disabled KillAura");
		}

		this.blockData = null;
		this.slot = -1;
		rotation.setYaw(999.0f);
		rotation.setPitch(999.0f);
		this.towerTick = 0;
		if (mode.getMode("AAC").isToggled() && Wrapper.INSTANCE.mcSettings().keyBindBack.isKeyDown()) {
			KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindBack.getKeyCode(), false);
		}
		Wrapper.INSTANCE.player().setSprinting(false);
		sneaking = true;

		if(timervalue.getValue()){
			Wrapper.timer.timerSpeed=this.timerspeed.getValue().floatValue();
		}

		super.onEnable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {

		if (mode.getMode("AAC").isToggled()) {
			AAC();
			godBridgeTimer = 0;
		} else if (mode.getMode("Simple").isToggled()) {
			Simple();
			godBridgeTimer = 0;
		} else if (mode.getMode("Hypixel").isToggled()) {
			speed();
			godBridgeTimer = 0;
		} else if (mode.getMode("GodBridge").isToggled()) {
			GodBridge();
		} else if (mode.getMode("Mineland").isToggled()) {
			// Simple();
			godBridgeTimer = 0;
		}
		super.onClientTick(event);
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		if (mode.getMode("Simple").isToggled()) {
			EntityPlayerSP player = Wrapper.INSTANCE.player();
			WorldClient world = mc.theWorld;
			if (this.getBlockCount() <= 0) {
				int spoofSlot = this.getBestSpoofSlot();
				this.getBlock(spoofSlot);
			}
			double yDif = 1.0;
			BlockData data = null;
			for (double posY = player.posY - 1.0; posY > 0.0; posY -= 1.0) {
				BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
				if (newData == null || !((yDif = player.posY - posY) <= 3.0))
					continue;
				data = newData;
				break;
			}
			int slot = -1;
			int blockCount = 0;
			for (int i = 0; i < 9; ++i) {
				ItemStack itemStack = player.inventory.getStackInSlot(i);
				if (itemStack == null)
					continue;
				int stackSize = itemStack.stackSize;
				if (!this.isValidItem(itemStack.getItem()) || stackSize <= blockCount)
					continue;
				blockCount = stackSize;
				slot = i;
				currentblock = itemStack;
			}
			if (slot == -1) {
				// empty if block
			}
			if (data != null && slot != -1) {
				BlockPos pos = data.pos;
				Block block = world.getBlockState(pos.offset(data.face)).getBlock();
				Vec3 hitVec = this.getVec3(data);
				if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
					return;
				}
				int last = player.inventory.currentItem;
				player.inventory.currentItem = slot;

				if (mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), pos,
						data.face, hitVec)) {
					Wrapper.INSTANCE.player().sendQueue.addToSendQueue(new C0APacketAnimation());
//                    	player.swingItem();
				}
				player.inventory.currentItem = last;
			}
			if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
				this.setToggled(false);
			}
		}
		super.onPlayerTick(event);
	}

	private int getBlockSlot() {
		for (int i = 0; i < 9; ++i) {
			if (!mc.thePlayer.inventoryContainer.getSlot((int) (i + 36)).getHasStack()
					|| !isScaffoldBlock((mc.thePlayer.inventoryContainer.getSlot((int) (i + 36)).getStack())))
				continue;
			return i;
		}
		return -1;
	}

	public static boolean isScaffoldBlock(ItemStack itemStack) {
		if (itemStack == null)
			return false;

		if (itemStack.stackSize <= 0)
			return false;

		if (!(itemStack.getItem() instanceof ItemBlock))
			return false;

		ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

		// whitelist
		if (itemBlock.getBlock() == Blocks.glass)
			return true;

		if (invalidBlocks.contains(Block.getBlockFromItem(itemStack.getItem())))
			return false;

		// only fullblock
		if (!itemBlock.getBlock().isFullBlock())
			return false;

		return true;
	}

	@Override
	public void onPlayerEventPre(EventPlayerPre event) {
		if (mode.getMode("AAC").isToggled()) {
			return;
		} else if (mode.getMode("Hypixel").isToggled()) {

			EntityPlayerSP player = Wrapper.INSTANCE.player();
			WorldClient world = mc.theWorld;
			if (this.getBlockCount() <= 0) {
				int spoofSlot = this.getBestSpoofSlot();
				this.getBlock(spoofSlot);
			}
			double yDif = 1.0;
			BlockData data = null;
			for (double posY = player.posY - 1.0; posY > 0.0; posY -= 1.0) {
				BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
				if (newData == null || !((yDif = player.posY - posY) <= 3.0))
					continue;
				data = newData;
				break;
			}
			int slot = -1;
			int blockCount = 0;
			for (int i = 0; i < 9; ++i) {
				ItemStack itemStack = player.inventory.getStackInSlot(i);
				if (itemStack == null)
					continue;
				int stackSize = itemStack.stackSize;
				if (!this.isValidItem(itemStack.getItem()) || stackSize <= blockCount)
					continue;
				blockCount = stackSize;
				slot = i;
				currentblock = itemStack;
			}
			if (slot == -1) {
				// empty if block
			}
			if (data != null && slot != -1) {
				BlockPos pos = data.pos;
				Block block = world.getBlockState(pos.offset(data.face)).getBlock();
				Vec3 hitVec = this.getVec3(data);
				if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
					return;
				}
				int last = player.inventory.currentItem;
				player.inventory.currentItem = slot;

				if (data != null) {
					float rot = 0.0f;
					if (mc.thePlayer.movementInput.moveForward > 0.0f) {
						rot = 180.0f;
						if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
							rot = -120.0f;
						} else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
							rot = 120.0f;
						}
					} else if (mc.thePlayer.movementInput.moveForward == 0.0f) {
						rot = 180.0f;
						if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
							rot = -90.0f;
						} else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
							rot = 90.0f;
						}
					} else if (mc.thePlayer.movementInput.moveForward < 0.0f) {
						if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
							rot = -45.0f;
						} else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
							rot = 45.0f;
						}
					}
					if (PlayerUtils.isAirUnder((Entity) mc.thePlayer) && mc.gameSettings.keyBindJump.isKeyDown()
							&& !PlayerUtils.MovementInput() && ((Boolean) this.tower.getValue()).booleanValue()) {
						rot = 180.0f;
					}

					float gcd = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
					float a2 = gcd * gcd * gcd * 1.2F;
					rot -= rot % a2;

					float yaw = MathHelper.wrapAngleTo180_float((float) mc.thePlayer.rotationYaw) - rot;
					float pitch = getRotationsHypixel(data.pos, data.face)[1];
					if (data != null) {
						rotation.setYaw((float) (yaw + KillAura.getRandomDoubleInRange(-1, 1)));
						rotation.setPitch((pitch));
					}
				}
				if (rotation.getYaw() != 999.0f) {
					mc.thePlayer.rotationYawHead = rotation.getYaw();
					mc.thePlayer.renderYawOffset = rotation.getYaw();
					event.setYaw(rotation.getYaw());
					Wrapper.INSTANCE.player().setRotationYawHead(rotation.getYaw());
				}
				if (rotation.getPitch() != 999.0f) {
					event.setPitch(rotation.getPitch());
					// Wrapper.INSTANCE.player().render
				}
				if (mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), pos,
						data.face, hitVec)) {

					Wrapper.INSTANCE.sendPacket(new C0APacketAnimation());
//              
				}
				player.inventory.currentItem = last;
			}
			if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
				this.setToggled(false);
			}

			Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
			Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
		}

		else if (mode.getMode("Hanabi").isToggled()) {
			if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
				this.setToggled(false);
			}

			if (this.getBlockCount() <= 0) {
				int spoofSlot = this.getBestSpoofSlot();
				this.getBlock(spoofSlot);
			}
			this.blockData = this.getBlockDataHanabi(new BlockPos(Wrapper.INSTANCE.player().posX,
					Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ)) == null
							? this.getBlockDataHanabi(new BlockPos(Wrapper.INSTANCE.player().posX,
									Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ).down(1))
							: this.getBlockDataHanabi(new BlockPos(Wrapper.INSTANCE.player().posX,
									Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ));
			this.slot = this.getBlockSlot();
			currentblock = mc.thePlayer.inventoryContainer.getSlot(slot + 36).getStack();
			if (this.blockData == null || this.slot == -1 || this.getBlockCount() <= 0
					|| !(MoveUtils.isMoving() || mc.gameSettings.keyBindJump.isKeyDown())) {
				return;
			}
			if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ))
					.getBlock() == Blocks.air) {
				float rot = 0.0f;
				if (mc.thePlayer.movementInput.moveForward > 0.0f) {
					rot = 180.0f;
					if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
						rot = -120.0f;
					} else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
						rot = 120.0f;
					}
				} else if (mc.thePlayer.movementInput.moveForward == 0.0f) {
					rot = 180.0f;
					if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
						rot = -90.0f;
					} else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
						rot = 90.0f;
					}
				} else if (mc.thePlayer.movementInput.moveForward < 0.0f) {
					if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
						rot = -45.0f;
					} else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
						rot = 45.0f;
					}
				}
				if (PlayerUtils.isAirUnder((Entity) mc.thePlayer) && mc.gameSettings.keyBindJump.isKeyDown()
						&& !PlayerUtils.MovementInput() && ((Boolean) this.tower.getValue()).booleanValue()) {
					rot = 180.0f;
				}

				float gcd = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
				float a2 = gcd * gcd * gcd * 1.2F;
				rot -= rot % a2;

				float yaw = MathHelper.wrapAngleTo180_float((float) mc.thePlayer.rotationYaw) - rot;
				float pitch = getRotationsHypixel(blockData.pos, blockData.face)[1];
				// + KillAura.getRandomDoubleInRange(-1, 1)
				rotation.setYaw((float) (yaw));
				rotation.setPitch((pitch));
			}
			if (rotation.getYaw() != 999.0f) {
				mc.thePlayer.rotationYawHead = rotation.getYaw();
				mc.thePlayer.renderYawOffset = rotation.getYaw();
				event.setYaw(rotation.getYaw());
				Wrapper.INSTANCE.player().setRotationYawHead(rotation.getYaw());

			}
			if (rotation.getPitch() != 999.0f) {
				event.setPitch(rotation.getPitch());
				rotation.setServerPitch(rotation.getPitch());
				// Wrapper.INSTANCE.player().render
			}
			if (PlayerUtils.isAirUnder((Entity) mc.thePlayer) && MoveUtils.isOnGround((double) 1.15)
					&& mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && (tower.getValue())) {
				if (towerboost.getValue())
					Wrapper.timer.timerSpeed = 2.1078f;
				mc.thePlayer.motionX = 0.0;
				mc.thePlayer.motionZ = 0.0;
				mc.thePlayer.movementInput.moveForward = 0.0f;
				mc.thePlayer.movementInput.moveStrafe = 0.0f;
				if (++this.towerTick < 10) {
					mc.thePlayer.jump();
				} else {
					this.towerTick = 0;
				}
			}
			if (MoveUtils.isOnGround((double) 1.15) && mc.gameSettings.keyBindJump.isKeyDown()
					&& !PlayerUtils.MovementInput() && ((Boolean) this.tower.getValue()).booleanValue()) {

			} else if (Wrapper.timer.timerSpeed == 2.1078f) {
				Wrapper.timer.timerSpeed = 1.0f;
			}
		} else if (mode.getMode("Legit").isToggled()) {

			if (this.getBlockCount() <= 0) {
				int spoofSlot = this.getBestSpoofSlot();
				this.getBlock(spoofSlot);

			}
			this.slot = this.getBlockSlot();
			if (!hotbarContainBlock()) {

				blockData = null;
				return;
			}
			double x = mc.thePlayer.posX;
			double z = mc.thePlayer.posZ;
			double forward = mc.thePlayer.movementInput.moveForward;
			double strafe = mc.thePlayer.movementInput.moveStrafe;
			float YAW = mc.thePlayer.rotationYaw;

			if (!mc.thePlayer.isCollidedHorizontally) {
				double[] coords = getExpandCoords(x, z, forward, strafe, YAW);
				x = coords[0];
				z = coords[1];
			}

			if (isAirBlock(
					mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ))
							.getBlock())) {
				x = mc.thePlayer.posX;
				z = mc.thePlayer.posZ;

			}

			if (samey.getValue()) {
				if (mc.thePlayer.fallDistance > 1.2 + 1 * MoveUtils.getJumpEffect()
						|| (!PlayerUtils.isMoving2() && mc.gameSettings.keyBindJump.isKeyDown())) {
					y = mc.thePlayer.posY;
				}
			} else {
				y = mc.thePlayer.posY;
			}

			if (blockData != null && rotation.getPitch() != 999) {
				rotation.setPitch(Utils.updateRotation(Wrapper.INSTANCE.player().rotationPitch,
						(float) (82.0f - randomNumber(0, 1)), 15.0f));
				event.setPitch(Utils.updateRotation(Wrapper.INSTANCE.player().rotationPitch,
						(float) (82.0f - randomNumber(0, 1)), 15.0f));
				rotation.setServerPitch(Utils.updateRotation(Wrapper.INSTANCE.player().rotationPitch,
						(float) (82.0f - randomNumber(0, 1)), 15.0f));
			}

			/*
			 * if (rotation.getPitch() != 999) { //ChatUtils.message("ok");
			 * //rotation.setPitch(getRotationsHypixel(blockData.pos, blockData.face)[1]);
			 * //event.setYaw(rotation.getYaw() + (float) randomNumber(1, -1));
			 * event.setPitch(getRotationsHypixel(blockData.pos, blockData.face)[1]); }
			 */
			setSpeed();
			if (getBlockCount() > 0) {

				if (!mc.gameSettings.keyBindJump.isKeyDown()) {
					Wrapper.timer.timerSpeed = 1;
				}

			}
			this.blockData = this.getBlockDataHanabi(new BlockPos(Wrapper.INSTANCE.player().posX,
					Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ)) == null
							? this.getBlockDataHanabi(new BlockPos(Wrapper.INSTANCE.player().posX,
									Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ).down(1))
							: this.getBlockDataHanabi(new BlockPos(Wrapper.INSTANCE.player().posX,
									Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ));

			float[] rot = getRotations(blockData.pos, blockData.face);

			int last = Wrapper.INSTANCE.player().inventory.currentItem;
			Wrapper.INSTANCE.player().inventory.currentItem = this.slot;
			if (Wrapper.INSTANCE.player().getCurrentEquippedItem() == null || blockData == null) {
				return;
			}
			if (Wrapper.INSTANCE.controller().onPlayerRightClick(mc.thePlayer, mc.theWorld,
					Wrapper.INSTANCE.player().getCurrentEquippedItem(), blockData.pos, blockData.face,
					getVec3(blockData.pos, blockData.face))) {

				Wrapper.INSTANCE.player().sendQueue.addToSendQueue(new C0APacketAnimation());
			}
			Wrapper.INSTANCE.player().inventory.currentItem = last;
		}

		else if (mode.getMode("Mineland").isToggled()) {
			if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
				this.setToggled(false);
			}

			if (this.getBlockCount() <= 0) {
				int spoofSlot = this.getBestSpoofSlot();
				this.getBlock(spoofSlot);
			}
			BlockPos underPos = new BlockPos(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY - 1,
					Wrapper.INSTANCE.player().posZ);
			Block underBlock = Wrapper.INSTANCE.world().getBlockState(underPos).getBlock();
			BlockData data = getBlockDataHanabi(underPos);

			this.blockData = data;
			this.slot = this.getBlockSlot();
			currentblock = mc.thePlayer.inventoryContainer.getSlot(slot + 36).getStack();
			if (this.blockData == null || this.slot == -1 || this.getBlockCount() <= 0
					|| !(MoveUtils.isMoving() || mc.gameSettings.keyBindJump.isKeyDown())) {
				return;
			}
			if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ))
					.getBlock() == Blocks.air) {

				if (data != null) {
					float[] rot = getRotations(data.pos, data.face);

					if (BlockUtils.isBlockMaterial(underPos, Blocks.air) && data != null && rot != null) {
						event.setYaw(rot[0]);
						event.setPitch(rot[1]);
						rotation.setServerPitch(rot[1]);
					}
				}
				Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
				// Wrapper.INSTANCE.player().rotationPitch=event.getPitch();
				Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
			}
			if (PlayerUtils.isAirUnder((Entity) mc.thePlayer) && MoveUtils.isOnGround((double) 1.15)
					&& mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && (tower.getValue())) {
				if (towerboost.getValue())
					Wrapper.timer.timerSpeed = 2.1078f;
				mc.thePlayer.motionX = 0.0;
				mc.thePlayer.motionZ = 0.0;
				mc.thePlayer.movementInput.moveForward = 0.0f;
				mc.thePlayer.movementInput.moveStrafe = 0.0f;
				if (++this.towerTick < 10) {
					mc.thePlayer.jump();
				} else {
					this.towerTick = 0;
				}
			}
			if (MoveUtils.isOnGround((double) 1.15) && mc.gameSettings.keyBindJump.isKeyDown()
					&& !PlayerUtils.MovementInput() && ((Boolean) this.tower.getValue()).booleanValue()) {

			} else if (Wrapper.timer.timerSpeed == 2.1078f) {
				Wrapper.timer.timerSpeed = 1.0f;
			}
		}
		super.onPlayerEventPre(event);
	}

	public boolean isAirBlock(Block block) {
		if (block.getMaterial().isReplaceable()) {
			if (block instanceof BlockSnow && block.getBlockBoundsMaxY() > 0.125) {
				return false;
			}

			return true;
		}

		return false;
	}

	/*
	 * @Override public void onPacketEvent(EventPacket event) { if
	 * ((mode.getMode("Hanabi").isToggled()||mode.getMode("Legit").isToggled()) &&
	 * event.getType() == EventType.SEND) { if (event.getPacket() instanceof
	 * C03PacketPlayer) { IC03Packet c03 = (IC03Packet) event.getPacket();
	 * c03.setYaw(rotation.getYaw()); c03.setPitch(rotation.getPitch());
	 * rotation.setServerPitch(rotation.getPitch()); } } super.onPacketEvent(event);
	 * }
	 */

	@Override
	public void onPlayerEventPost(EventPlayerPost event) {
		if (mode.getMode("Hanabi").isToggled() || mode.getMode("Mineland").isToggled()) {
			int last = Wrapper.INSTANCE.player().inventory.currentItem;
			Wrapper.INSTANCE.player().inventory.currentItem = this.slot;
			if (Wrapper.INSTANCE.player().getCurrentEquippedItem() == null || blockData == null) {
				return;
			}
			if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
					Wrapper.INSTANCE.player().getCurrentEquippedItem(), this.blockData.pos, this.blockData.face,
					getVec3(this.blockData.pos, this.blockData.face))) {
				Wrapper.INSTANCE.player().sendQueue.addToSendQueue(new C0APacketAnimation());
			}

			Wrapper.INSTANCE.player().inventory.currentItem = last;
		}
		super.onPlayerEventPost(event);
	}

	public float[] getRotationsHypixel(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
		paramBlockPos = paramBlockPos.offset(paramEnumFacing.getOpposite());
		return RotationUtil.getRotationFromPosition(paramBlockPos.getX() + 0.5, paramBlockPos.getZ() + 0.5,
				paramBlockPos.getY());
	}

	  @Override
	public void onLoop(EventLoop event) {
		  for (int i = 0; i < 8; i++) {
	            if (mc.thePlayer.inventory.mainInventory[i] != null
	                    && mc.thePlayer.inventory.mainInventory[i].stackSize <= 0)
	                mc.thePlayer.inventory.mainInventory[i] = null;
	        }
		super.onLoop(event);
	}

	void AAC() {

		EntityPlayerSP player = Wrapper.INSTANCE.player();
		int oldSlot = -1;
		if (!check()) {
			if (isBridging) {
				KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindSneak.getKeyCode(),
						BlockUtils.isBlockMaterial(new BlockPos(player).down(), Blocks.air));
				isBridging = false;
				if (oldSlot != -1) {
					player.inventory.currentItem = oldSlot;
				}
			}
			startYaw = 0;
			startPitch = 0;
			facingCam = null;
			blockDown = null;
			return;
		}
		startYaw = Wrapper.INSTANCE.player().rotationYaw;
		startPitch = Wrapper.INSTANCE.player().rotationPitch;
		KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindRight.getKeyCode(), false);
		KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindLeft.getKeyCode(), false);
		blockDown = new BlockPos(player).down();
		float r1 = new Random().nextFloat();
		if (r1 == 1.0f)
			r1--;
		int newSlot = findSlotWithBlock();
		if (newSlot == -1)
			return;
		oldSlot = player.inventory.currentItem;
		player.inventory.currentItem = newSlot;
		player.rotationPitch = Utils.updateRotation(player.rotationPitch, (82.0f - r1), 15.0f);
		int currentCPS = Utils.random(3, 4);
		if (timer.isDelay(1000 / currentCPS)) {
			RobotUtils.clickMouse(1);
			Utils.swingMainHand();
			timer.setLastMS();
		}
		isBridging = true;
		KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindSneak.getKeyCode(),
				BlockUtils.isBlockMaterial(new BlockPos(player).down(), Blocks.air));
	}

	void Simple() {
		final Vec3 below = Wrapper.INSTANCE.player().getPositionVector();
		blockDown = new BlockPos(below).add(0, -1, 0);
		if (!BlockUtils.getBlock(blockDown).getMaterial().isReplaceable())
			return;
		int newSlot = findSlotWithBlock();
		if (newSlot == -1)
			return;
		final int oldSlot = Wrapper.INSTANCE.inventory().currentItem;
		Wrapper.INSTANCE.inventory().currentItem = newSlot;

		Wrapper.INSTANCE.inventory().currentItem = oldSlot;
	}

	void GodBridge() {
		if (godBridgeTimer > 0) {
			/*
			 * ReflectionHelper.setPrivateValue(Minecraft.class, mc, new Integer(0), new
			 * String[] { "rightClickDelayTimer", "field_71467_ac" });
			 */
			godBridgeTimer--;
		}

		if (mc.theWorld == null || mc.thePlayer == null)
			return;
		WorldClient world = mc.theWorld;
		EntityPlayerSP player = mc.thePlayer;
		MovingObjectPosition movingObjectPosition = player.rayTrace(mc.playerController.getBlockReachDistance(), 1);
		boolean isKeyUseDown = false;
		int keyCode = mc.gameSettings.keyBindUseItem.getKeyCode();
		if (keyCode >= 0) {
			isKeyUseDown = Keyboard.isKeyDown(keyCode);
		} else {
			isKeyUseDown = Mouse.isButtonDown(keyCode + 100);
		}
		if (movingObjectPosition != null
				&& movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
				&& movingObjectPosition.sideHit == EnumFacing.UP && isKeyUseDown) {

			ItemStack itemstack = player.inventory.getCurrentItem();
			int i = itemstack != null ? itemstack.stackSize : 0;

			if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
				ItemBlock itemblock = (ItemBlock) itemstack.getItem();

				if (!itemblock.canPlaceBlockOnSide((net.minecraft.world.World) world,
						movingObjectPosition.getBlockPos(), movingObjectPosition.sideHit, (EntityPlayer) player,
						itemstack)) {
					BlockPos blockPos = movingObjectPosition.getBlockPos();
					IBlockState blockState = world.getBlockState(blockPos);
					AxisAlignedBB axisalignedbb = blockState.getBlock().getSelectedBoundingBox(world, blockPos);

					if (axisalignedbb == null || world.isAirBlock(blockPos))
						return;

					double x1, x2, y1, y2, z1, z2;
					Vec3 targetVec3 = null;
					Vec3 eyeVec3 = player.getPositionEyes((float) 1);
					x1 = axisalignedbb.minX;
					x2 = axisalignedbb.maxX;
					y1 = axisalignedbb.minY;
					y2 = axisalignedbb.maxY;
					z1 = axisalignedbb.minZ;
					z2 = axisalignedbb.maxZ;

					class Data implements Comparable<Data> {
						public BlockPos blockPos;
						public EnumFacing enumFacing;
						public double cost;

						public Data(BlockPos blockPos, EnumFacing enumFacing, double cost) {
							this.blockPos = blockPos;
							this.enumFacing = enumFacing;
							this.cost = cost;
						}

						@Override
						public int compareTo(Data data) {
							return (this.cost - data.cost) > 0 ? -1 : (this.cost - data.cost) < 0 ? 1 : 0;
						}

					}

					List<Data> list = new ArrayList<Data>();

					if (x1 <= eyeVec3.xCoord && eyeVec3.xCoord <= x2 && y1 <= eyeVec3.yCoord && eyeVec3.yCoord <= y2
							&& z1 <= eyeVec3.zCoord && eyeVec3.zCoord <= z2) {
						// targetVec3 = new Vec3(0.5*(axisalignedbb.minX+axisalignedbb.maxX),
						// 0.5*(axisalignedbb.minY+axisalignedbb.maxY),
						// 0.5*(axisalignedbb.minZ+axisalignedbb.maxZ));
					} else {
						double xCost = Math.abs(eyeVec3.xCoord - 0.5 * (axisalignedbb.minX + axisalignedbb.maxX));
						double yCost = Math.abs(eyeVec3.yCoord - 0.5 * (axisalignedbb.minY + axisalignedbb.maxY));
						double zCost = Math.abs(eyeVec3.zCoord - 0.5 * (axisalignedbb.minZ + axisalignedbb.maxZ));
						double sumCost = xCost + yCost + zCost;
						if (eyeVec3.xCoord < x1) {
							list.add(new Data(blockPos.west(), EnumFacing.WEST, xCost));
						} else if (eyeVec3.xCoord > x2) {
							list.add(new Data(blockPos.east(), EnumFacing.EAST, xCost));
						}

//						if ( eyeVec3.yCoord < y1 ) {
//							list.add(new Data( blockPos.down(), EnumFacing.UP, yCost));
//						} else if ( eyeVec3.yCoord > y2 ) {
//							list.add(new Data( blockPos.up(), EnumFacing.DOWN, yCost ));
//						}

						if (eyeVec3.zCoord < z1) {
							list.add(new Data(blockPos.north(), EnumFacing.NORTH, zCost));
						} else if (eyeVec3.zCoord > z2) {
							list.add(new Data(blockPos.south(), EnumFacing.SOUTH, zCost));
						}

						Collections.sort(list);
						double border = 0.05;
						double x = MathHelper.clamp_double(eyeVec3.xCoord, x1 + border, x2 - border);
						double y = MathHelper.clamp_double(eyeVec3.yCoord, y1 + border, y2 - border);
						double z = MathHelper.clamp_double(eyeVec3.zCoord, z1 + border, z2 - border);
						for (Data data : list) {
							if (!world.isAirBlock(data.blockPos))
								continue;
							if (data.enumFacing == EnumFacing.WEST || data.enumFacing == EnumFacing.EAST) {
								x = MathHelper.clamp_double(eyeVec3.xCoord, x1, x2);
							} else if (data.enumFacing == EnumFacing.UP || data.enumFacing == EnumFacing.DOWN) {
								y = MathHelper.clamp_double(eyeVec3.yCoord, y1, y2);
							} else {
								z = MathHelper.clamp_double(eyeVec3.zCoord, z1, z2);
							}
							targetVec3 = new Vec3(x, y, z);
							break;
						}

						if (targetVec3 != null) {
//							log("eee");

							double d0 = targetVec3.xCoord - eyeVec3.xCoord;
							double d1 = targetVec3.yCoord - eyeVec3.yCoord;
							double d2 = targetVec3.zCoord - eyeVec3.zCoord;

							// targetVec3 = new Vec3(x,y,z);

							double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
							float f = (float) (MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
							float f1 = (float) (-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI));
							// player.setPositionAndRotation(player.posX, player.posY, player.posZ, f, f1);
							float f2, f3;
							f2 = player.rotationYaw;
							f3 = player.rotationPitch;

							player.rotationYaw = f;
							player.rotationPitch = f1;

//							Vec3 vec31 = player.getLook(1);
//							Vec3 vec32 = vec31.addVector(vec31.xCoord * mc.playerController.getBlockReachDistance(), vec31.yCoord * mc.playerController.getBlockReachDistance(), vec31.zCoord * mc.playerController.getBlockReachDistance());
							MovingObjectPosition movingObjectPosition1 = player
									.rayTrace(mc.playerController.getBlockReachDistance(), 1);
//

//							Vec3 lookVec3= player.getLook((float) partialTicks);
//							double reach = 6.0d;
//							Vec3 rayVec3 = eyeVec3.addVector(lookVec3.xCoord * reach, lookVec3.yCoord * reach, lookVec3.zCoord * reach);
//							MovingObjectPosition movingObjectPosition = axisalignedbb.calculateIntercept(eyeVec3, rayVec3);
							// if (movingObjectPosition != null)
							// log(movingObjectPosition.toString());

//							log(movingObjectPosition1.toString());
//							log(blockPos.toString());
							if (movingObjectPosition1.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
									&& movingObjectPosition1.getBlockPos().getX() == blockPos.getX()
									&& movingObjectPosition1.getBlockPos().getY() == blockPos.getY()
									&& movingObjectPosition1.getBlockPos().getZ() == blockPos.getZ()) {
//							if (movingObjectPosition1.typeOfHit == MovingObjectType.BLOCK ) {
//								log("eee");
//								mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(player.rotationYaw, player.rotationPitch, player.onGround));
//								mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(player.rotationYaw, player.rotationPitch, player.onGround));
								if (mc.playerController.onPlayerRightClick(player, mc.theWorld, itemstack, blockPos,
										movingObjectPosition1.sideHit, movingObjectPosition1.hitVec)) {
									player.swingItem();
								}
								if (itemstack == null) {
									;
								} else if (itemstack.stackSize == 0) {
									player.inventory.mainInventory[player.inventory.currentItem] = null;
								} else if (itemstack.stackSize != i || mc.playerController.isInCreativeMode()) {
									mc.entityRenderer.itemRenderer.resetEquippedProgress();
								}
							}
							//
							player.rotationYaw = f2;
							player.rotationPitch = f3;
							double pitchDelta = 2.5;
							double targetPitch = 75.5;
							if (targetPitch - pitchDelta < player.rotationPitch
									&& player.rotationPitch < targetPitch + pitchDelta) {
								double mod = player.rotationYaw % 45.0;
								if (mod < 0) {
									mod += 45.0;
								}
//	                	        log(String.format("yaw: %s", mod));
//	                	        log(String.format("Pitch: %s", player.rotationPitch));
								double delta = 5.0;

								if (mod < delta) {
									player.rotationYaw -= mod;
									player.rotationPitch = (float) targetPitch;
//	    							player.prevRotationYaw = player.rotationYaw;
//	    							player.prevRotationPitch = player.rotationPitch;
								} else if (45.0 - mod < delta) {
									player.rotationYaw += 45.0 - mod;
									player.rotationPitch = (float) targetPitch;
//	    							player.prevRotationYaw = player.rotationYaw;
//	    							player.prevRotationPitch = player.rotationPitch;
								}

								// if (Math.abs(rawMod))
							}

							ReflectionHelper.setPrivateValue(Minecraft.class, mc, new Integer(1),
									new String[] { "rightClickDelayTimer", "field_71467_ac" });
							godBridgeTimer = 10;

						}

//						log(String.format("x: %s, y: %s, z: %s", x,y,z));

					}
				}
			}

		}
	}

	public int findSlotWithBlock() {
		for (int i = 0; i < 9; ++i) {
			final ItemStack stack = Wrapper.INSTANCE.inventory().getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof ItemBlock) {
				Block block = Block.getBlockFromItem(stack.getItem()).getDefaultState().getBlock();
				if (block.isFullBlock() && block != Blocks.sand && block != Blocks.gravel) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (blockData != null) {
			blockDown = blockData.pos;
			RenderUtils.drawBlockESP(blockData.pos, 1F, 1F, 1F);
			if (mode.getMode("AAC").isToggled()) {
				BlockPos blockDown2 = new BlockPos(Wrapper.INSTANCE.player()).down();
				BlockPos blockDown3 = new BlockPos(Wrapper.INSTANCE.player()).down();
				if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.EAST) {
					blockDown2 = new BlockPos(Wrapper.INSTANCE.player()).down().west();
					blockDown3 = new BlockPos(Wrapper.INSTANCE.player()).down().west(2);
				} else if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.NORTH) {
					blockDown2 = new BlockPos(Wrapper.INSTANCE.player()).down().south();
					blockDown3 = new BlockPos(Wrapper.INSTANCE.player()).down().south(2);
				} else if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.SOUTH) {
					blockDown2 = new BlockPos(Wrapper.INSTANCE.player()).down().north();
					blockDown3 = new BlockPos(Wrapper.INSTANCE.player()).down().north(2);
				} else if (Wrapper.INSTANCE.player().getHorizontalFacing() == EnumFacing.WEST) {
					blockDown2 = new BlockPos(Wrapper.INSTANCE.player()).down().east();
					blockDown3 = new BlockPos(Wrapper.INSTANCE.player()).down().east(2);
				}
				RenderUtils.drawBlockESP(blockDown2, 1F, 0F, 0F);
				RenderUtils.drawBlockESP(blockDown3, 1F, 0F, 0F);
			}
		}
		super.onRenderWorldLast(event);
	}

	boolean check() {
		MovingObjectPosition object = Wrapper.INSTANCE.mc().objectMouseOver;
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		ItemStack stack = player.inventory.getCurrentItem();
		if (object == null || stack == null) {
			return false;
		}
		if (object.typeOfHit != MovingObjectType.BLOCK) {
			return false;
		}
		if (player.rotationPitch <= 70 || !player.onGround || player.isOnLadder() || player.isInLava()
				|| player.isInWater()) {
			return false;
		}
		if (!Wrapper.INSTANCE.mcSettings().keyBindBack.isKeyDown()) {
			return false;
		}
		return true;
	}

	private boolean isPosSolid(BlockPos pos) {
		Block block = mc.theWorld.getBlockState(pos).getBlock();
		return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isVisuallyOpaque()
				|| block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow
				|| block instanceof BlockSkull) && !block.getMaterial().isLiquid()
				&& !(block instanceof BlockContainer);
	}

	private BlockData getBlockData1(BlockPos pos) {
		if (isPosSolid(pos.add(0, -1, 0))) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos.add(-1, 0, 0))) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos.add(1, 0, 0))) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos.add(0, 0, 1))) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos.add(0, 0, -1))) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos1 = pos.add(-1, 0, 0);

		if (isPosSolid(pos1.add(0, -1, 0))) {
			return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos1.add(-1, 0, 0))) {
			return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos1.add(1, 0, 0))) {
			return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos1.add(0, 0, 1))) {
			return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos1.add(0, 0, -1))) {
			return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos2 = pos.add(1, 0, 0);

		if (isPosSolid(pos2.add(0, -1, 0))) {
			return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos2.add(-1, 0, 0))) {
			return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos2.add(1, 0, 0))) {
			return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos2.add(0, 0, 1))) {
			return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos2.add(0, 0, -1))) {
			return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos3 = pos.add(0, 0, 1);

		if (isPosSolid(pos3.add(0, -1, 0))) {
			return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos3.add(-1, 0, 0))) {
			return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos3.add(1, 0, 0))) {
			return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos3.add(0, 0, 1))) {
			return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos3.add(0, 0, -1))) {
			return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos4 = pos.add(0, 0, -1);

		if (isPosSolid(pos4.add(0, -1, 0))) {
			return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos4.add(-1, 0, 0))) {
			return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos4.add(1, 0, 0))) {
			return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos4.add(0, 0, 1))) {
			return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos4.add(0, 0, -1))) {
			return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos19 = pos.add(-2, 0, 0);

		if (isPosSolid(pos1.add(0, -1, 0))) {
			return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos1.add(-1, 0, 0))) {
			return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos1.add(1, 0, 0))) {
			return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos1.add(0, 0, 1))) {
			return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos1.add(0, 0, -1))) {
			return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos29 = pos.add(2, 0, 0);

		if (isPosSolid(pos2.add(0, -1, 0))) {
			return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos2.add(-1, 0, 0))) {
			return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos2.add(1, 0, 0))) {
			return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos2.add(0, 0, 1))) {
			return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos2.add(0, 0, -1))) {
			return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos39 = pos.add(0, 0, 2);

		if (isPosSolid(pos3.add(0, -1, 0))) {
			return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos3.add(-1, 0, 0))) {
			return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos3.add(1, 0, 0))) {
			return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos3.add(0, 0, 1))) {
			return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos3.add(0, 0, -1))) {
			return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos49 = pos.add(0, 0, -2);

		if (isPosSolid(pos4.add(0, -1, 0))) {
			return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos4.add(-1, 0, 0))) {
			return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos4.add(1, 0, 0))) {
			return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos4.add(0, 0, 1))) {
			return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos4.add(0, 0, -1))) {
			return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos5 = pos.add(0, -1, 0);

		if (isPosSolid(pos5.add(0, -1, 0))) {
			return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos5.add(-1, 0, 0))) {
			return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos5.add(1, 0, 0))) {
			return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos5.add(0, 0, 1))) {
			return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos5.add(0, 0, -1))) {
			return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos6 = pos5.add(1, 0, 0);

		if (isPosSolid(pos6.add(0, -1, 0))) {
			return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos6.add(-1, 0, 0))) {
			return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos6.add(1, 0, 0))) {
			return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos6.add(0, 0, 1))) {
			return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos6.add(0, 0, -1))) {
			return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos7 = pos5.add(-1, 0, 0);

		if (isPosSolid(pos7.add(0, -1, 0))) {
			return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos7.add(-1, 0, 0))) {
			return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos7.add(1, 0, 0))) {
			return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos7.add(0, 0, 1))) {
			return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos7.add(0, 0, -1))) {
			return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos8 = pos5.add(0, 0, 1);

		if (isPosSolid(pos8.add(0, -1, 0))) {
			return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos8.add(-1, 0, 0))) {
			return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos8.add(1, 0, 0))) {
			return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos8.add(0, 0, 1))) {
			return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos8.add(0, 0, -1))) {
			return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
		}

		BlockPos pos9 = pos5.add(0, 0, -1);

		if (isPosSolid(pos9.add(0, -1, 0))) {
			return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos9.add(-1, 0, 0))) {
			return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
		}

		if (isPosSolid(pos9.add(1, 0, 0))) {
			return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
		}

		if (isPosSolid(pos9.add(0, 0, 1))) {
			return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
		}

		if (isPosSolid(pos9.add(0, 0, -1))) {
			return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
		}

		return null;
	}

	private boolean isValidItem(Item item) {
		if (item instanceof ItemBlock) {
			ItemBlock iBlock = (ItemBlock) item;
			Block block = iBlock.getBlock();
			return !this.invalidBlocks.contains(block);
		}
		return false;
	}

	public int getBlockCount() {
		int n = 0;
		int i = 36;
		while (i < 45) {
			if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack stack = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
				final Item item = stack.getItem();
				if (stack.getItem() instanceof ItemBlock && this.isValid(item)) {
					n += stack.stackSize;
				}
			}
			++i;
		}
		return n;
	}

	void getBlock(int hotbarSlot) {
		for (int i = 9; i < 45; ++i) {
			Minecraft var10000 = mc;
			if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()
					&& (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
				var10000 = mc;
				ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
				if (is.getItem() instanceof ItemBlock) {
					ItemBlock block = (ItemBlock) is.getItem();
					if (isValidItem(block)) {
						if (36 + hotbarSlot != i) {
							this.swap(i, hotbarSlot);
						}
						break;
					}
				}
			}
		}

	}

	int getBestSpoofSlot() {
		int spoofSlot = 5;

		for (int i = 36; i < 45; ++i) {
			if (!Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
				spoofSlot = i - 36;
				break;
			}
		}

		return spoofSlot;
	}

	private Vec3 getVec3(BlockData data) {
		BlockPos pos = data.pos;
		EnumFacing face = data.face;
		double x = (double) pos.getX() + 0.5;
		double y = (double) pos.getY() + 0.5;
		double z = (double) pos.getZ() + 0.5;
		x += (double) face.getFrontOffsetX() / 2.0;
		z += (double) face.getFrontOffsetZ() / 2.0;
		y += (double) face.getFrontOffsetY() / 2.0;
		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += this.randomNumber(0.3, -0.3);
			z += this.randomNumber(0.3, -0.3);
		} else {
			y += this.randomNumber(0.49, 0.5);
		}
		if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
			z += this.randomNumber(0.3, -0.3);
		}
		if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
			x += this.randomNumber(0.3, -0.3);
		}
		return new Vec3(x, y, z);
	}

	void speed() {
		Wrapper.INSTANCE.player().stepHeight = 0.5f;
		Aura.target = null;
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		WorldClient world = mc.theWorld;
		if (this.getBlockCount() <= 0) {
			int spoofSlot = this.getBestSpoofSlot();
			this.getBlock(spoofSlot);
		}
		double yDif = 1.0;
		BlockData data = null;
		for (double posY = player.posY - 1.0; posY > 0.0; posY -= 1.0) {
			BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
			if (newData == null || !((yDif = player.posY - posY) <= 3.0))
				continue;
			data = newData;
			break;
		}
		int slot = -1;
		int blockCount = 0;
		for (int i = 0; i < 9; ++i) {
			ItemStack itemStack = player.inventory.getStackInSlot(i);
			if (itemStack == null)
				continue;
			int stackSize = itemStack.stackSize;
			if (!this.isValidItem(itemStack.getItem()) || stackSize <= blockCount)
				continue;
			blockCount = stackSize;
			slot = i;
			currentblock = itemStack;
		}
		if (slot == -1) {
			// empty if block
		}
		if (data != null && slot != -1) {
			BlockPos pos = data.pos;
			Block block = world.getBlockState(pos.offset(data.face)).getBlock();
			Vec3 hitVec = this.getVec3(data);
			if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
				return;
			}

			if (mc.gameSettings.keyBindJump.isKeyDown()) {

				player.motionX = 0.0;
				player.motionY = 0.41982;
				player.motionZ = 0.0;
				if (this.towerStopwatch.hasReached(1500)) {
					player.motionY = -0.28;
					this.towerStopwatch.reset();
				}
			} else {
				this.towerStopwatch.reset();
			}
		}

		if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
			this.setToggled(false);
		}
	}

	private double randomNumber(double max, double min) {
		return Math.random() * (max - min) + min;
	}

	private BlockData getBlockData(BlockPos pos) {
		BlockPos[] blockPositions = this.blockPositions;
		EnumFacing[] facings = this.facings;
		List<Block> validBlocks = this.validBlocks;
		WorldClient world = mc.theWorld;
		BlockPos posBelow = new BlockPos(0, -1, 0);
		if (!validBlocks.contains(world.getBlockState(pos.add(posBelow)).getBlock())) {
			return new BlockData(pos.add(posBelow), EnumFacing.UP);
		}
		int blockPositionsLength = blockPositions.length;
		for (int i = 0; i < blockPositionsLength; ++i) {
			BlockPos blockPos = pos.add(blockPositions[i]);
			if (!validBlocks.contains(world.getBlockState(blockPos).getBlock())) {
				return new BlockData(blockPos, facings[i]);
			}
			for (int i1 = 0; i1 < blockPositionsLength; ++i1) {
				BlockPos blockPos1 = pos.add(blockPositions[i1]);
				BlockPos blockPos2 = blockPos.add(blockPositions[i1]);
				if (!validBlocks.contains(world.getBlockState(blockPos1).getBlock())) {
					return new BlockData(blockPos1, facings[i1]);
				}
				if (validBlocks.contains(world.getBlockState(blockPos2).getBlock()))
					continue;
				return new BlockData(blockPos2, facings[i1]);
			}
		}
		return null;
	}

	public int getallBlockCount() {
		int n = 0;
		int i = 0;
		while (i < 36) {
			if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack stack = Wrapper.INSTANCE.player().inventoryContainer.getSlot(i).getStack();
				final Item item = stack.getItem();
				if (stack.getItem() instanceof ItemBlock && this.isValid(item)) {
					n += stack.stackSize;
				}
			}
			++i;
		}
		return n;
	}

	private boolean isValid(final Item item) {
		return item instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) item).getBlock());
	}

	private boolean isBlockUnder(double yOffset) {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		return !this.validBlocks.contains(
				mc.theWorld.getBlockState(new BlockPos(player.posX, player.posY - yOffset, player.posZ)).getBlock());
	}

	public void swap(int slot1, int hotbarSlot) {
		mc.playerController.windowClick(Wrapper.INSTANCE.player().inventoryContainer.windowId, slot1, hotbarSlot, 2,
				Wrapper.INSTANCE.player());
	}

	private static class BlockData {
		public final BlockPos pos;
		public final EnumFacing face;

		private BlockData(BlockPos pos, EnumFacing face) {
			this.pos = pos;
			this.face = face;
		}
	}

	public float[] getRotations(BlockPos block, EnumFacing face) {
		double x = block.getX() + 0.5 - Wrapper.INSTANCE.player().posX + (double) face.getFrontOffsetX() / 2;
		double z = block.getZ() + 0.5 - Wrapper.INSTANCE.player().posZ + (double) face.getFrontOffsetZ() / 2;
		double y = (block.getY() + 0.5);

		if (mode.getMode("Legit").isToggled()) {
			double dist = mc.thePlayer.getDistance(block.getX() + 0.5 + (double) face.getFrontOffsetX() / 2,
					block.getY(), block.getZ() + 0.5 + (double) face.getFrontOffsetZ() / 2);

			y += 0.5;
		}

		double d1 = Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight() - y;
		double d3 = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);

		if (yaw < 0.0F) {
			yaw += 360f;
		}

		return new float[] { yaw, pitch };
	}

	private void a03c01(int value) {

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

	public static Vec3 getVec3(BlockPos pos, EnumFacing face) {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += KillAura.getRandomDoubleInRange(0.3, -0.3);
			z += KillAura.getRandomDoubleInRange(0.3, -0.3);
		} else {
			y += KillAura.getRandomDoubleInRange(0.3, -0.3);
		}
		if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
			z += KillAura.getRandomDoubleInRange(0.3, -0.3);
		}
		if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
			x += KillAura.getRandomDoubleInRange(0.3, -0.3);
		}
		return new Vec3(x, y, z);
	}

	private BlockData getBlockDataHanabi(BlockPos pos) {
		if (this.isPosSolid(pos.add(0, -1, 0))) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos.add(-1, 0, 0))) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos.add(1, 0, 0))) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos.add(0, 0, 1))) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos.add(0, 0, -1))) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos1 = pos.add(-1, 0, 0);
		if (this.isPosSolid(pos1.add(0, -1, 0))) {
			return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos1.add(-1, 0, 0))) {
			return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos1.add(1, 0, 0))) {
			return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos1.add(0, 0, 1))) {
			return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos1.add(0, 0, -1))) {
			return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos2 = pos.add(1, 0, 0);
		if (this.isPosSolid(pos2.add(0, -1, 0))) {
			return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos2.add(-1, 0, 0))) {
			return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos2.add(1, 0, 0))) {
			return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos2.add(0, 0, 1))) {
			return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos2.add(0, 0, -1))) {
			return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos3 = pos.add(0, 0, 1);
		if (this.isPosSolid(pos3.add(0, -1, 0))) {
			return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos3.add(-1, 0, 0))) {
			return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos3.add(1, 0, 0))) {
			return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos3.add(0, 0, 1))) {
			return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos3.add(0, 0, -1))) {
			return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos4 = pos.add(0, 0, -1);
		if (this.isPosSolid(pos4.add(0, -1, 0))) {
			return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos4.add(-1, 0, 0))) {
			return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos4.add(1, 0, 0))) {
			return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos4.add(0, 0, 1))) {
			return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos4.add(0, 0, -1))) {
			return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos19 = pos.add(-2, 0, 0);
		if (this.isPosSolid(pos1.add(0, -1, 0))) {
			return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos1.add(-1, 0, 0))) {
			return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos1.add(1, 0, 0))) {
			return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos1.add(0, 0, 1))) {
			return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos1.add(0, 0, -1))) {
			return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos29 = pos.add(2, 0, 0);
		if (this.isPosSolid(pos2.add(0, -1, 0))) {
			return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos2.add(-1, 0, 0))) {
			return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos2.add(1, 0, 0))) {
			return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos2.add(0, 0, 1))) {
			return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos2.add(0, 0, -1))) {
			return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos39 = pos.add(0, 0, 2);
		if (this.isPosSolid(pos3.add(0, -1, 0))) {
			return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos3.add(-1, 0, 0))) {
			return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos3.add(1, 0, 0))) {
			return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos3.add(0, 0, 1))) {
			return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos3.add(0, 0, -1))) {
			return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos49 = pos.add(0, 0, -2);
		if (this.isPosSolid(pos4.add(0, -1, 0))) {
			return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos4.add(-1, 0, 0))) {
			return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos4.add(1, 0, 0))) {
			return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos4.add(0, 0, 1))) {
			return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos4.add(0, 0, -1))) {
			return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos5 = pos.add(0, -1, 0);
		if (this.isPosSolid(pos5.add(0, -1, 0))) {
			return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos5.add(-1, 0, 0))) {
			return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos5.add(1, 0, 0))) {
			return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos5.add(0, 0, 1))) {
			return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos5.add(0, 0, -1))) {
			return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos6 = pos5.add(1, 0, 0);
		if (this.isPosSolid(pos6.add(0, -1, 0))) {
			return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos6.add(-1, 0, 0))) {
			return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos6.add(1, 0, 0))) {
			return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos6.add(0, 0, 1))) {
			return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos6.add(0, 0, -1))) {
			return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos7 = pos5.add(-1, 0, 0);
		if (this.isPosSolid(pos7.add(0, -1, 0))) {
			return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos7.add(-1, 0, 0))) {
			return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos7.add(1, 0, 0))) {
			return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos7.add(0, 0, 1))) {
			return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos7.add(0, 0, -1))) {
			return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos8 = pos5.add(0, 0, 1);
		if (this.isPosSolid(pos8.add(0, -1, 0))) {
			return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos8.add(-1, 0, 0))) {
			return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos8.add(1, 0, 0))) {
			return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos8.add(0, 0, 1))) {
			return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos8.add(0, 0, -1))) {
			return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
		}
		BlockPos pos9 = pos5.add(0, 0, -1);
		if (this.isPosSolid(pos9.add(0, -1, 0))) {
			return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
		}
		if (this.isPosSolid(pos9.add(-1, 0, 0))) {
			return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (this.isPosSolid(pos9.add(1, 0, 0))) {
			return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
		}
		if (this.isPosSolid(pos9.add(0, 0, 1))) {
			return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
		}
		if (this.isPosSolid(pos9.add(0, 0, -1))) {
			return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
		}
		return null;
	}

	public void setSpeed() {
		double motionx = mc.thePlayer.motionX;
		double motionz = mc.thePlayer.motionZ;

		if (mode.getMode("Legit").isToggled()) {

			if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)) {
				mc.thePlayer.setSprinting(false);

				if (timer.delay(50)) {
					if (timer.delay(250)) {
						if (!sneaking && !mc.gameSettings.keyBindJump.isKeyDown()) {
							C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.START_SNEAKING);
							mc.thePlayer.sendQueue.addToSendQueue(p);
							sneaking = !sneaking;
						} else if (mc.gameSettings.keyBindJump.isKeyDown()) {
							C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING);
							mc.thePlayer.sendQueue.addToSendQueue(p);
							sneaking = !sneaking;
						}
					} else {
						if (sneaking) {
							C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING);
							mc.thePlayer.sendQueue.addToSendQueue(p);
							sneaking = !sneaking;
						}
					}
				}

				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.thePlayer.jump();
				}

				mc.thePlayer.onGround = false;
				mc.thePlayer.jumpMovementFactor = 0;
				double speed = sneaking ? 0.09 : 0.13;
				double x = mc.thePlayer.posX;
				double z = mc.thePlayer.posZ;
				double forward = mc.thePlayer.movementInput.moveForward;
				double strafe = mc.thePlayer.movementInput.moveStrafe;
				float YAW = mc.thePlayer.rotationYaw;
				double a = (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))
						+ strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)));
				double b = (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))
						- strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)));
				double c = Math.abs((a * b));
				double slow = 1 - c * 5;
				speed *= slow;

				if (speed < 0.05) {
					speed = 0.05;
				}

				speed += randomNumber(0.001, -0.001);
				double more = legit.getValue();
				speed *= more;
				MoveUtils.setMotion(speed);
			} else {
				// mc.thePlayer.jumpMovementFactor = 0;
				// mc.thePlayer.onGround = false;
				// double speed = ((Number) settings.get(CUBE).getValue()).doubleValue()*1;
				// MoveUtils.setMotion(speed);
			}

		}

	}

	private boolean hotbarContainBlock() {
		int i = 36;

		while (i < 45) {
			try {
				ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if ((stack == null) || (stack.getItem() == null) || !(stack.getItem() instanceof ItemBlock)
						|| !isValid(stack.getItem())) {
					i++;
					continue;
				}

				return true;
			} catch (Exception e) {
			}
		}

		return false;
	}

	public double[] getExpandCoords(double x, double z, double forward, double strafe, float YAW) {
		BlockPos underPos = new BlockPos(x, mc.thePlayer.posY - 1, z);
		Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
		double xCalc = -999, zCalc = -999;
		double dist = 0;
		double expandDist = expand.getValue() * 2;

		while (!isAirBlock(underBlock)) {
			xCalc = x;
			zCalc = z;
			dist++;

			if (dist > expandDist) {
				dist = expandDist;
			}

			xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))
					+ strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
			zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))
					- strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;

			if (dist == expandDist) {
				break;
			}

			underPos = new BlockPos(xCalc, mc.thePlayer.posY - 1, zCalc);
			underBlock = mc.theWorld.getBlockState(underPos).getBlock();
		}

		return new double[] { xCalc, zCalc };
	}
}
