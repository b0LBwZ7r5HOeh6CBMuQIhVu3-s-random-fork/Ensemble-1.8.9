package it.fktcod.ktykshrk.module.mods;

import java.util.LinkedList;
import java.util.Queue;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.system.Connection;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Ghost extends Module {

	double rot1, rot2;
	private int delay;
	boolean shouldSpeed = false;
	float yaw, pitch;
	private String PM = "MODE";
	private String DIST = "DIST";
	TimerUtils timer = new TimerUtils();

	Queue<C03PacketPlayer> packets = new LinkedList<>();
	boolean send = false;
	public BooleanValue noWalls;
	public ModeValue mode;
	public NumberValue verticalValue;
	public NumberValue horizontalValue;

	public Ghost() {
		super("Ghost", HackCategory.PLAYER);
		noWalls = new BooleanValue("NoWalls", true);
		mode = new ModeValue("Mode", new Mode("Basic", false), new Mode("Skip", true),new Mode("Vanilla", false),new Mode("Clip", false));
		verticalValue =new NumberValue("VerticalValue", 5D, -10D, 10D);
		horizontalValue=new NumberValue("HorizontalValue", 0D, -10D, 10D);
		this.addValue(noWalls,verticalValue,horizontalValue,mode);
		this.setChinese(Core.Translate_CN[47]);
	}

	@Override
	public String getDescription() {
		return "Allows you to pass through walls.";
	}

	@Override
	public void onEnable() {
		double yaw = Math.toRadians(Wrapper.INSTANCE.player().rotationYaw);
		        double x = -Math.sin(yaw) * horizontalValue.getValue();
		        double z = Math.cos(yaw) * horizontalValue.getValue();
		if (Wrapper.INSTANCE.world() == null) {
			return;
		}
		if(mode.getMode("Clip").isToggled()) {
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(  Wrapper.INSTANCE.player().posX,
                      Wrapper.INSTANCE.player().posY,   Wrapper.INSTANCE.player().posZ, true));
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0,
                    0.5, true));
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(  Wrapper.INSTANCE.player().posX,
                      Wrapper.INSTANCE.player().posY,   Wrapper.INSTANCE.player().posZ, true));
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(  Wrapper.INSTANCE.player().posX + x,
                      Wrapper.INSTANCE.player().posY + verticalValue.getValue(),   Wrapper.INSTANCE.player().posZ + z, true));
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5,
                    0.0, 0.5, true));
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(  Wrapper.INSTANCE.player().posX
                    + 0.5,   Wrapper.INSTANCE.player().posY,   Wrapper.INSTANCE.player().posZ + 0.5, true));

            Wrapper.INSTANCE.player().setPosition(  Wrapper.INSTANCE.player().posX + -Math.sin(yaw) * 0.04,   Wrapper.INSTANCE.player().posY,
                      Wrapper.INSTANCE.player().posZ + Math.cos(yaw) * 0.04);
		}
		shouldSpeed = isInsideBlock();

		if ((MoveUtils.isCollidedH(0.001) || Wrapper.INSTANCE.player().isCollidedHorizontally)) {
			Wrapper.INSTANCE.player().onGround = false;
			Wrapper.INSTANCE.player().noClip = true;
			Wrapper.INSTANCE.player().motionX *= 0;
			Wrapper.INSTANCE.player().motionZ *= 0;
			Wrapper.INSTANCE.player().jumpMovementFactor = 0;
			teleport(0.006000000238415);
			rot1 = 0;
			rot2 = 0;
		}
		super.onEnable();
	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (mode.getMode("Basic").isToggled()) {
			boolean skip = true;
			if (noWalls.getValue()) {
				if (side == Side.OUT && packet instanceof C03PacketPlayer) {

					ChatUtils.message("Packets:" + String.valueOf(packets.size()));

					send = false;
					packets.add((C03PacketPlayer) packet);
					return send;
				} else {
					send = true;
				}

			}

			return send;
		}
		return true;

	}

	@Override
	public void onDisable() {
		if (mode.getMode("Basic").isToggled()) {
			Wrapper.INSTANCE.player().noClip = false;

			if (noWalls.getValue()) {
				Wrapper.INSTANCE.sendPacket(packets.poll());
			}
		}
		super.onDisable();
	}

	@Override
	public void onLivingUpdate(LivingUpdateEvent event) {
		
		 if(isInsideBlock()) {
				Wrapper.INSTANCE.player().noClip = true;
				Wrapper.INSTANCE.player().motionY = 0;
				Wrapper.INSTANCE.player().onGround = true;
	        }
		
		if (mode.getMode("Basic").isToggled()) {
			Wrapper.INSTANCE.player().noClip = true;
			Wrapper.INSTANCE.player().fallDistance = 0;
			Wrapper.INSTANCE.player().onGround = true;

			Wrapper.INSTANCE.player().capabilities.isFlying = false;
			Wrapper.INSTANCE.player().motionX = 0;
			Wrapper.INSTANCE.player().motionY = 0;
			Wrapper.INSTANCE.player().motionZ = 0;

			float speed = 0.2f;
			Wrapper.INSTANCE.player().jumpMovementFactor = speed;
			if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
				Wrapper.INSTANCE.player().motionY += speed;
			}
			if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
				Wrapper.INSTANCE.player().motionY -= speed;
			}
		}else if(mode.getMode("Skip").isToggled()) {
			
			if (!Wrapper.INSTANCE.player().onGround || !timer.hasReached(2f) || !Wrapper.INSTANCE.player().isCollidedHorizontally|| !(!isInsideBlock() || Wrapper.INSTANCE.player().isSneaking()))
                return;

            final double direction = MoveUtils.getDirection();
            final double posX = -Math.sin(direction) * 0.3;
            final double posZ = Math.cos(direction) * 0.3;

            for (int i = 0; i < 3; ++i) {
               Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.06, Wrapper.INSTANCE.player().posZ, true));
               Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX + posX * i, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + posZ * i, true));
            }

            Wrapper.INSTANCE.player().setEntityBoundingBox(Wrapper.INSTANCE.player().getEntityBoundingBox().offset(posX, 0.0D, posZ));
            Wrapper.INSTANCE.player().setPositionAndUpdate(Wrapper.INSTANCE.player().posX + posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + posZ);
            timer.reset();
			
		}else if(mode.getMode("Vanilla").isToggled()) {
			if (!Wrapper.INSTANCE.player().onGround || !timer.hasReached(2f) || !Wrapper.INSTANCE.player().isCollidedHorizontally|| !(!isInsideBlock() || Wrapper.INSTANCE.player().isSneaking()))
                return;

			 Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition( Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, true));
			 Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5D, 0, 0.5D, true));
             	 Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition( Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, true));
             	 Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition( Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY + 0.2D, Wrapper.INSTANCE.player().posZ, true));
             	 Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5D, 0, 0.5D, true));
             	 Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition( Wrapper.INSTANCE.player().posX + 0.5D, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + 0.5D, true));
             final double yaw = Math.toRadians( Wrapper.INSTANCE.player().rotationYaw);
             final double x = -Math.sin(yaw) * 0.04D;
             final double z = Math.cos(yaw) * 0.04D;
             Wrapper.INSTANCE.player().setPosition( Wrapper.INSTANCE.player().posX + x, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ + z);
             timer.reset();
		}
		super.onLivingUpdate(event);
	}

	public static boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(Wrapper.INSTANCE.player().getEntityBoundingBox().minX); x < MathHelper
				.floor_double(Wrapper.INSTANCE.player().getEntityBoundingBox().maxX) + 1; x++) {
			for (int y = MathHelper.floor_double(Wrapper.INSTANCE.player().getEntityBoundingBox().minY); y < MathHelper
					.floor_double(Wrapper.INSTANCE.player().getEntityBoundingBox().maxY) + 1; y++) {
				for (int z = MathHelper
						.floor_double(Wrapper.INSTANCE.player().getEntityBoundingBox().minZ); z < MathHelper
								.floor_double(Wrapper.INSTANCE.player().getEntityBoundingBox().maxZ) + 1; z++) {
					Block block = Wrapper.INSTANCE.world().getBlockState(new BlockPos(x, y, z)).getBlock();

					if ((block != null) && (!(block instanceof BlockAir))) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Wrapper.INSTANCE.world(),
								new BlockPos(x, y, z), Wrapper.INSTANCE.world().getBlockState(new BlockPos(x, y, z)));

						if ((block instanceof BlockHopper)) {
							boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
						}

						if (boundingBox != null) {
							if (Wrapper.INSTANCE.player().getEntityBoundingBox().intersectsWith(boundingBox)) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	private void teleport(double dist) {
		double forward = Wrapper.INSTANCE.player().movementInput.moveForward;
		double strafe = Wrapper.INSTANCE.player().movementInput.moveStrafe;
		float yaw = Wrapper.INSTANCE.player().rotationYaw;

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

		double x = Wrapper.INSTANCE.player().posX;
		double y = Wrapper.INSTANCE.player().posY;
		double z = Wrapper.INSTANCE.player().posZ;
		double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0F))
				+ strafe * dist * Math.sin(Math.toRadians(yaw + 90.0F));
		double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0F))
				- strafe * dist * Math.cos(Math.toRadians(yaw + 90.0F));
		Wrapper.INSTANCE.player().setPosition(x + xspeed, y, z + zspeed);
	}
}
