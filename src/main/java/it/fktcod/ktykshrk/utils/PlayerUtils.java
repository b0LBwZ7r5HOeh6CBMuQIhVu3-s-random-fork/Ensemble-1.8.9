package it.fktcod.ktykshrk.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Multimap;

import it.fktcod.ktykshrk.event.EventMove;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Map.Entry;

public class PlayerUtils {
	private static Minecraft mc;

	static {
		PlayerUtils.mc = Minecraft.getMinecraft();
	}

	private static final Minecraft MC = Minecraft.getMinecraft();

	public static boolean isAirUnder(Entity ent) {
		return mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1, ent.posZ)).getBlock() == Blocks.air;
	}

	public static boolean isHoldingSword() {
		return mc.thePlayer.getCurrentEquippedItem() != null
				&& mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
	}
	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	public static float getDirection() {
		float yaw = PlayerUtils.mc.thePlayer.rotationYaw;
		if (PlayerUtils.mc.thePlayer.moveForward < 0.0f) {
			yaw += 180.0f;
		}
		float forward = 1.0f;
		if (PlayerUtils.mc.thePlayer.moveForward < 0.0f) {
			forward = -0.5f;
		} else if (PlayerUtils.mc.thePlayer.moveForward > 0.0f) {
			forward = 0.5f;
		}
		if (PlayerUtils.mc.thePlayer.moveStrafing > 0.0f) {
			yaw -= 90.0f * forward;
		}
		if (PlayerUtils.mc.thePlayer.moveStrafing < 0.0f) {
			yaw += 90.0f * forward;
		}
		yaw *= 0.017453292f;
		return yaw;
	}

	public static boolean isInWater() {
		return PlayerUtils.mc.theWorld.getBlockState(
				new BlockPos(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY, PlayerUtils.mc.thePlayer.posZ))
				.getBlock().getMaterial() == Material.water;
	}

	public static boolean isInLiquid() {
		if (mc.thePlayer.isInWater()) {
			return true;
		}
		boolean inLiquid = false;
		final int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
		for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper
				.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper
					.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
				final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && block.getMaterial() != Material.air) {
					if (!(block instanceof BlockLiquid))
						return false;
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static void toFwd(final double speed) {
		final float yaw = PlayerUtils.mc.thePlayer.rotationYaw * 0.017453292f;
		final EntityPlayerSP thePlayer = PlayerUtils.mc.thePlayer;
		thePlayer.motionX -= MathHelper.sin(yaw) * speed;
		final EntityPlayerSP thePlayer2 = PlayerUtils.mc.thePlayer;
		thePlayer2.motionZ += MathHelper.cos(yaw) * speed;
	}

	public static void setSpeed(final double speed) {
		PlayerUtils.mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
		PlayerUtils.mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
	}

	public static double getSpeed() {
		return Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX
				+ Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
	}

	public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
		return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
	}

	public static Block getBlock(final BlockPos pos) {
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	}

	public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
		return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
	}

	public static ArrayList<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ,
			final double speed) {
		final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		final Minecraft mc = Minecraft.getMinecraft();
		final double posX = tpX - mc.thePlayer.posX;
		final double posY = tpY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 1.1);
		final double posZ = tpZ - mc.thePlayer.posZ;
		final float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
		final float pitch = (float) (-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0
				/ 3.141592653589793);
		double tmpX = mc.thePlayer.posX;
		double tmpY = mc.thePlayer.posY;
		double tmpZ = mc.thePlayer.posZ;
		double steps = 1.0;
		for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
				tpZ); d += speed) {
			++steps;
		}
		for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
				tpZ); d += speed) {
			tmpX = mc.thePlayer.posX - Math.sin(getDirection(yaw)) * d;
			tmpZ = mc.thePlayer.posZ + Math.cos(getDirection(yaw)) * d;
			tmpY -= (mc.thePlayer.posY - tpY) / steps;
			positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
		}
		positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));
		return positions;
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
		yaw *= 0.017453292f;
		return yaw;
	}

	public static double getDistance(final double x1, final double y1, final double z1, final double x2,
			final double y2, final double z2) {
		final double d0 = x1 - x2;
		final double d2 = y1 - y2;
		final double d3 = z1 - z2;
		return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
	}

	public static boolean MovementInput() {
		return (mc.gameSettings.keyBindForward).isKeyDown()
				||  (mc.gameSettings.keyBindLeft).isKeyDown()
				|| ( mc.gameSettings.keyBindRight).isKeyDown()
				|| ( mc.gameSettings.keyBindBack).isKeyDown();
	}

	public static void blockHit(Entity en, boolean value) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = mc.thePlayer.getCurrentEquippedItem();

		if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value) {
			if (stack.getItem() instanceof ItemSword && mc.thePlayer.swingProgress > 0.2) {
				mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
			}
		}
	}

	public static float getItemAtkDamage(ItemStack itemStack) {
		final Multimap multimap = itemStack.getAttributeModifiers();
		if (!multimap.isEmpty()) {
			final Iterator iterator = multimap.entries().iterator();
			if (iterator.hasNext()) {
				final Map.Entry entry = (Entry) iterator.next();
				final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
				double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2
						? attributeModifier.getAmount()
						: attributeModifier.getAmount() * 100.0;

				if (attributeModifier.getAmount() > 1.0) {
					return 1.0f + (float) damage;
				}
				return 1.0f;
			}
		}
		return 1.0f;
	}

	public static int bestWeapon(Entity target) {
		Minecraft mc = Minecraft.getMinecraft();
		int firstSlot = mc.thePlayer.inventory.currentItem = 0;
		int bestWeapon = -1;
		int j = 1;

		for (byte i = 0; i < 9; i++) {
			mc.thePlayer.inventory.currentItem = i;
			ItemStack itemStack = mc.thePlayer.getHeldItem();

			if (itemStack != null) {
				int itemAtkDamage = (int) getItemAtkDamage(itemStack);
				itemAtkDamage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

				if (itemAtkDamage > j) {
					j = itemAtkDamage;
					bestWeapon = i;
				}
			}
		}

		if (bestWeapon != -1) {
			return bestWeapon;
		} else {
			return firstSlot;
		}
	}
	public static List<EntityLivingBase> getLivingEntities() {
		return Arrays.asList(
				Minecraft.getMinecraft().theWorld.loadedEntityList.stream()
						.filter(entity -> entity instanceof EntityLivingBase)
						.filter(entity -> entity != Minecraft.getMinecraft().thePlayer)
						.map(entity -> (EntityLivingBase) entity)
						.toArray(EntityLivingBase[]::new)
		);
	}

	public static void shiftClick(Item i) {
		for (int i1 = 9; i1 < 37; ++i1) {
			ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();

			if (itemstack != null && itemstack.getItem() == i) {
				mc.playerController.windowClick(0, i1, 0, 1, mc.thePlayer);
				break;
			}
		}
	}

	public static boolean hotbarIsFull() {
		for (int i = 0; i <= 36; ++i) {
			ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

			if (itemstack == null) {
				return false;
			}
		}

		return true;
	}



	public static boolean isMoving() {
		if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
			return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
		}
		return false;
	}

	public static boolean isMoving2() {
		return ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F));
	}

	public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
		double curX = startPos[0];
		double curY = startPos[1];
		double curZ = startPos[2];
		double endX = (double) endPos.getX() + 0.5D;
		double endY = (double) endPos.getY() + 1.0D;
		double endZ = (double) endPos.getZ() + 0.5D;
		double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);

		for (int count = 0; distance > slack; ++count) {
			distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
			if (count > 120) {
				break;
			}

			boolean next = false;
			double diffX = curX - endX;
			double diffY = curY - endY;
			double diffZ = curZ - endZ;
			double offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
			if (diffX < 0.0D) {
				if (Math.abs(diffX) > offset) {
					curX += offset;
				} else {
					curX += Math.abs(diffX);
				}
			}

			if (diffX > 0.0D) {
				if (Math.abs(diffX) > offset) {
					curX -= offset;
				} else {
					curX -= Math.abs(diffX);
				}
			}

			if (diffY < 0.0D) {
				if (Math.abs(diffY) > 0.25D) {
					curY += 0.25D;
				} else {
					curY += Math.abs(diffY);
				}
			}

			if (diffY > 0.0D) {
				if (Math.abs(diffY) > 0.25D) {
					curY -= 0.25D;
				} else {
					curY -= Math.abs(diffY);
				}
			}

			if (diffZ < 0.0D) {
				if (Math.abs(diffZ) > offset) {
					curZ += offset;
				} else {
					curZ += Math.abs(diffZ);
				}
			}

			if (diffZ > 0.0D) {
				if (Math.abs(diffZ) > offset) {
					curZ -= offset;
				} else {
					curZ -= Math.abs(diffZ);
				}
			}

			Minecraft.getMinecraft().getNetHandler()
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
		}

	}

	public static void damage(int damage) {
		for (int index = 0; index <= 67 + (23 * (damage - 1)); ++index) {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, mc.thePlayer.posY + 2.535E-9D, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, mc.thePlayer.posY + 1.05E-10D, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, mc.thePlayer.posY + 0.0448865D, mc.thePlayer.posZ, false));
		}
	}

	public static List<net.minecraft.util.AxisAlignedBB> getCollidingBoundingList(EntityPlayerSP thePlayer, float f) {
		return mc.theWorld.getCollidingBoundingBoxes(thePlayer,
				thePlayer.getEntityBoundingBox().offset(0.0D, -f, 0.0D));

	}

	public static final Block getBlockBelowEntity(Entity entity, double offset) {
		final Vec3 below = entity.getPositionVector();
		return MC.theWorld.getBlockState(new BlockPos(below).add(0, -offset, 0)).getBlock();
	}

	public static final Block getBlockBelowEntity(Entity entity) {
		return getBlockBelowEntity(entity, 1);
	}

	public static final Block getBlockBelowPlayer() {
		return getBlockBelowEntity(MC.thePlayer);
	}

	public void portMove(float yaw, float multiplyer, float up) {
		double moveX = -Math.sin(Math.toRadians(yaw)) * (double) multiplyer;
		double moveZ = Math.cos(Math.toRadians(yaw)) * (double) multiplyer;
		double moveY = up;
		mc.thePlayer.setPosition(moveX + mc.thePlayer.posX, moveY + mc.thePlayer.posY,
				moveZ + mc.thePlayer.posZ);
	}

	public final Block getBlockBelowPlayer(double offset) {
		return getBlockBelowEntity(MC.thePlayer, offset);
	}

	public final boolean isTeamMate(EntityLivingBase entity) {
		if (!(entity instanceof EntityPlayer))
			return false;
		if (MC.thePlayer.getTeam() != null && entity.getTeam() != null) {
			if (MC.thePlayer.isOnSameTeam(entity)) {
				return true;
			}
		}

		if (MC.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
			final String playerName = MC.thePlayer.getDisplayName().getFormattedText().replace("顪�", "");
			final String entityName = entity.getDisplayName().getFormattedText().replace("顪�", "");
			if (playerName.isEmpty() || entityName.isEmpty())
				return false;
			return playerName.charAt(1) == entityName.charAt(1);
		}

		return false;
	}

	public static void setSpeed(double speed, float yaw, double strafe, double forward) {
		if (forward == 0 && strafe == 0) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		} else {
			if (forward != 0) {
				if (strafe > 0) {
					yaw += (forward > 0 ? -45 : 45);
				} else if (strafe < 0) {
					yaw += (forward > 0 ? 45 : -45);
				}
				strafe = 0;
				if (forward > 0) {
					forward = 1;
				} else {
					forward = -1;
				}
			}
			mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90));
			mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90));
		}
	}


	public static void setSpeed(EventMove event, double speed) {
		float yaw = mc.thePlayer.rotationYaw;
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		if (forward == 0 && strafe == 0) {
			event.setX(0);
			event.setZ(0);
		} else {
			if (forward != 0) {
				if (strafe > 0) {
					yaw += (forward > 0 ? -45 : 45);
				} else if (strafe < 0) {
					yaw += (forward > 0 ? 45 : -45);
				}
				strafe = 0;
				if (forward > 0) {
					forward = 1;
				} else {
					forward = -1;
				}
			}
			event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90)));
			event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90)));
		}
	}


	public static double getLastDist() {
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		return Math.sqrt(xDist * xDist + zDist * zDist);
	}
	
	public static ArrayList<BlockPos> getBlockPosesEntityIsStandingOn(Entity en) {
		BlockPos pos1 = new BlockPos(en.getCollisionBoundingBox().minX, en.getCollisionBoundingBox().minY - 0.01, en.getCollisionBoundingBox().minZ);
		
		BlockPos pos2 = new BlockPos(en.getCollisionBoundingBox().maxX, en.getCollisionBoundingBox().minY - 0.01, en.getCollisionBoundingBox().maxZ);
		
		Iterable<BlockPos> collisionBlocks = BlockPos.getAllInBox(pos1, pos2);
		ArrayList<BlockPos> returnList = new ArrayList<BlockPos>();
		for(BlockPos pos : collisionBlocks) {
			returnList.add(pos);
		}
		return returnList;
	}
	
	public static boolean isEntityOnGround(Entity en) {
		ArrayList<BlockPos> poses = getBlockPosesEntityIsStandingOn(en);
		
		
		for(BlockPos pos : poses) {
			IBlockState blockState = getBlockState(pos);
			Block block = blockState.getBlock();
			if(!(blockState.getBlock().getMaterial() instanceof MaterialTransparent) && blockState.getBlock().getMaterial() != Material.air
					&& !(block instanceof BlockLiquid) && blockState.getBlock().isFullCube()) {
				return true;
			}
		}
		
		return false;
	}
	public static IBlockState getBlockState(BlockPos blockPos) {
		return mc.theWorld.getBlockState(blockPos);
	}
}
