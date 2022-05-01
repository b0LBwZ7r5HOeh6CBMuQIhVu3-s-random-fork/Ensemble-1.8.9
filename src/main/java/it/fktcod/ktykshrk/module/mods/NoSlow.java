package it.fktcod.ktykshrk.module.mods;

import ensemble.mixin.cc.mixin.interfaces.IS08Packet;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.event.EventPlayerPost;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.event.EventSlowDown;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.NoSlowDownUtils;
import it.fktcod.ktykshrk.utils.PlayerControllerUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import static it.fktcod.ktykshrk.utils.PlayerUtils.isMoving;

public class NoSlow extends Module {
	TimerUtils timer = new TimerUtils();
	ModeValue mode;

	BooleanValue sendpacket;

	NumberValue blockForwardValue;
	NumberValue blockStrafeValue;

	BooleanValue teleport;
	BooleanValue sneak;
	BooleanValue teleportNoApplyValue;
	private NumberValue percent =new NumberValue("Percent", 0.0D, 0.0D, 100.0D);
	ModeValue tmode;
	MovementInput origmi;
	boolean pendingFlagApplyPacket = false;
	double lastMotionX = 0.0;
	double lastMotionY = 0.0;
	double lastMotionZ = 0.0;

	public NoSlow() {
		super("NoSlow", HackCategory.MOVEMENT);
		sendpacket = new BooleanValue("SendPacket", true);
		blockForwardValue = new NumberValue("BlockForward", 1D, 0.2D, 1D);
		blockStrafeValue = new NumberValue("BlockStrafe", 1D, 0.2D, 1D);
		teleport = new BooleanValue("Teleport", false);
		sneak = new BooleanValue("Sneak", false);
		tmode = new ModeValue("TeleMode", new Mode("Vanilla", true), new Mode("VanillaNSB", false),
				new Mode("Custom", false), new Mode("Decrease", false));
		teleportNoApplyValue = new BooleanValue("TNAV", false);

		this.mode = new ModeValue("Mode", new Mode("AAC", false), new Mode("Hypixel", true), new Mode("Custom", true), new Mode("NCP", true), new Mode("Basic", false),
				new Mode("AAC5", false));

		this.addValue(sendpacket, mode, blockForwardValue, blockStrafeValue, teleport,teleportNoApplyValue,tmode,percent);
		this.setChinese(Core.Translate_CN[67]);
	}


	@Override
	public void onEnable(){
		//vanilla
		origmi=mc.thePlayer.movementInput;
		if(!(mc.thePlayer.movementInput instanceof NoSlowDownUtils)) {
			mc.thePlayer.movementInput = new NoSlowDownUtils(mc.gameSettings);
		}
	}

	@Override
	public void onPlayerEventPre(EventPlayerPre event) {
		if (mode.getMode("AAC").isToggled()) {
			if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding()
					&& MoveUtils.isMoving()) {
				Wrapper.INSTANCE.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
						new BlockPos(0, 0, 0), EnumFacing.DOWN));
			}
		} else if (mode.getMode("Hypixel").isToggled()) {

			if (mc.thePlayer.isBlocking() && PlayerControllerUtils.isMoving() && MoveUtils.isOnGround(0.42)
					&& KillAura.blockstate == false) {

				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

			}
		}
		super.onPlayerEventPre(event);
	}

	@Override
	public void onPlayerEventPost(EventPlayerPost event) {
		if (mode.getMode("AAC").isToggled()) {
			if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding()
					&& MoveUtils.isMoving()) {
				Wrapper.INSTANCE.sendPacket(
						new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().inventory.getCurrentItem()));
			}
		} else if (mode.getMode("Hypixel").isToggled()) {
			if (mc.thePlayer.isBlocking() && PlayerControllerUtils.isMoving() && MoveUtils.isOnGround(0.42)
					&& KillAura.blockstate == false) {

				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
						PlayerControllerUtils.getHypixelBlockpos(mc.getSession().getUsername()), 255,
						mc.thePlayer.inventory.getCurrentItem(), 0, 0, 0));
			}

		} else if (mode.getMode("AAC5").isToggled()) {

			if (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking() || KillAura.blockstate) {
				BlockPos pos = new BlockPos(-1, -1, -1);
				Wrapper.INSTANCE.sendPacket(new C08PacketPlayerBlockPlacement(pos, 255,
						mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
			}
		}
		super.onPlayerEventPost(event);
	}

	@Override
	public void onSlowDownEvent(EventSlowDown event) {
		event.setForward(blockForwardValue.getValue().floatValue());
		event.setStrafe(blockStrafeValue.getValue().floatValue());
		super.onSlowDownEvent(event);
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {


		if(!(mc.thePlayer.movementInput instanceof NoSlowDownUtils)) {
			origmi=mc.thePlayer.movementInput;
			mc.thePlayer.movementInput = new NoSlowDownUtils(mc.gameSettings);
		}

		//vanilla
		if(mc.thePlayer.onGround&&!mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()&&mc.gameSettings.keyBindUseItem.isKeyDown()) {
			NoSlowDownUtils move = (NoSlowDownUtils) mc.thePlayer.movementInput;
			move.setNSD(true);
		}

		if(mc.thePlayer.isUsingItem() && isMoving() && MoveUtils.isOnGround(0.42) && KillAura.target == null && mode.getSelectMode().getName() == "NCP"){

			double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}

		if (mode.getSelectMode().getName() == "Custom") {
			try{

				if(mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) {
					mc.thePlayer.motionX *= 0.5f;
					mc.thePlayer.motionZ *= 0.5f;
				}
				if (mc.thePlayer.isUsingItem()) {
					mc.thePlayer.motionX *= percent.getValue() / 100;
					mc.thePlayer.motionZ *= percent.getValue()/ 100;
				}
			}  catch (NullPointerException ignore){}
		}

		//post
		if(mc.thePlayer.isUsingItem() && isMoving() && MoveUtils.isOnGround(0.42) && KillAura.target == null && mode.getSelectMode().getName() == "NCP"){
			double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
			mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
		}



		if (mode.getMode("Basic").isToggled()) {
			if (mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown()
					|| mc.gameSettings.keyBindSneak.isKeyDown() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
				if (!(mc.thePlayer.movementInput instanceof NoSlowDownUtils)) {
					mc.thePlayer.movementInput = new NoSlowDownUtils(mc.gameSettings);
				}
				NoSlowDownUtils move = (NoSlowDownUtils) mc.thePlayer.movementInput;
				move.setNSD(true);

				if (event.phase == Phase.START) {
					if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding()
							&& MoveUtils.isMoving()) {
						Wrapper.INSTANCE
								.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
										new BlockPos(0, 0, 0), EnumFacing.DOWN));
					}
				} else if (event.phase == Phase.END) {
					if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding()
							&& MoveUtils.isMoving()) {
						Wrapper.INSTANCE.sendPacket(new C08PacketPlayerBlockPlacement(
								Wrapper.INSTANCE.player().inventory.getCurrentItem()));
					}
				}
			}

			if (!sendpacket.getValue()) {
				return;
			}
			if (mode.getMode("AAC").isToggled()) {

				if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding()
						&& MoveUtils.isMoving()) {

					if (event.phase == Phase.START) {
						if (Wrapper.INSTANCE.player().onGround || MoveUtils.isOnGround(0.5)) {
							Wrapper.INSTANCE.sendPacket(
									new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
											new BlockPos(0, 0, 0), EnumFacing.DOWN));
						}
					} else if (event.phase == Phase.END) {
						if (timer.delay(65)) {

							Wrapper.INSTANCE.sendPacket(new C08PacketPlayerBlockPlacement(
									Wrapper.INSTANCE.player().inventory.getCurrentItem()));
							timer.reset();
						}
					}
				}
			}
		}
	}

	@Override
	public void onDisable() {
		if (mode.getMode("Basic").isToggled()) {
			if (!(mc.thePlayer.movementInput instanceof NoSlowDownUtils)) {
				mc.thePlayer.movementInput = new NoSlowDownUtils(mc.gameSettings);
			}
			NoSlowDownUtils move = (NoSlowDownUtils) mc.thePlayer.movementInput;
			move.setNSD(false);

			pendingFlagApplyPacket = false;
		}
		mc.thePlayer.movementInput=origmi;
	}

	@Override
	public void onPacketEvent(EventPacket event) {

		Packet packet = event.getPacket();
		if (teleport.getValue() && packet instanceof S08PacketPlayerPosLook) {

			pendingFlagApplyPacket = true;
			lastMotionX = mc.thePlayer.motionX;
			lastMotionY = mc.thePlayer.motionY;
			lastMotionZ = mc.thePlayer.motionZ;

			if (tmode.getMode("VanillaNSB").isToggled()) {

				IS08Packet s08 = (IS08Packet) packet;
				double x = s08.getX() - mc.thePlayer.posX;
				double y = s08.getY() - mc.thePlayer.posY;
				double z = s08.getZ() - mc.thePlayer.posZ;
				double diff = Math.sqrt(x * x + y * y + z * z);
				if (diff <= 8) {
					event.setCancelled(true);
					pendingFlagApplyPacket = false;
					Wrapper.INSTANCE.sendPacket(new C06PacketPlayerPosLook(s08.getX(), s08.getY(), s08.getZ(),
							s08.getYaw(), s08.getPitch(), true));
				}
			}
		} else if (pendingFlagApplyPacket && packet instanceof C06PacketPlayerPosLook) {

			pendingFlagApplyPacket = false;
			if (teleportNoApplyValue.getValue()) {
				event.setCancelled(true);
			}

			if (tmode.getMode("Vanilla").isToggled() || tmode.getMode("VanillaNBS").isToggled()) {

				mc.thePlayer.motionX = lastMotionX;
				mc.thePlayer.motionY = lastMotionY;
				mc.thePlayer.motionZ = lastMotionZ;

			}

		}

		super.onPacketEvent(event);
	}
}
