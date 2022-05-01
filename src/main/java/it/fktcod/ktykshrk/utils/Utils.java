package it.fktcod.ktykshrk.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import it.fktcod.ktykshrk.Core;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.utils.inject.IIIIIIIIII;
import it.fktcod.ktykshrk.utils.math.Vec3d;
import it.fktcod.ktykshrk.utils.system.Mapping;
import it.fktcod.ktykshrk.utils.system.WebUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import static it.fktcod.ktykshrk.module.mods.SelfDestruct.isDes;

public class Utils {

	public static boolean lookChanged;
	public static float[] rotationsToBlock = null;
	private static final Random RANDOM = new Random();

	public static boolean nullCheck() {
		if (isDes){return true;}
		return (Wrapper.INSTANCE.player() == null || Wrapper.INSTANCE.world() == null);
	}

	public static void hotkeyToSlot(int slot) { Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot; }

	public static int getCurrentPlayerSlot() {
		return Minecraft.getMinecraft().thePlayer.inventory.currentItem;
	}


	public static final ScaledResolution getScaledRes() {
		final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
		return scaledRes;
	}

	public static void copy(String content) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
	}

	public static int random(int min, int max) {
		return RANDOM.nextInt(max - min) + min;
	}

	public static Vec3 getRandomCenter(AxisAlignedBB bb) {
		return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.8 * Math.random(),
				bb.minY + (bb.maxY - bb.minY) * Math.random() + 0.1 * Math.random(),
				bb.minZ + (bb.maxZ - bb.minZ) * 0.8 * Math.random());
	}

	public static boolean isMoving(Entity e) {
		return e.motionX != 0.0 && e.motionZ != 0.0 && (e.motionY != 0.0 || e.motionY > 0.0);
	}

	public static boolean canBeClicked(final BlockPos pos) {
		return BlockUtils.getBlock(pos).canCollideCheck(BlockUtils.getState(pos), false);
	}

	public static Vec3 getEyesPos() {
		return new Vec3(Wrapper.INSTANCE.player().posX,
				Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(),
				Wrapper.INSTANCE.player().posZ);
	}

	public static void faceVectorPacketInstant(final Vec3 vec) {
		Utils.rotationsToBlock = getNeededRotations(vec);
	}

	public static List<Entity> getEntityList() {
		return Wrapper.INSTANCE.world().getLoadedEntityList();
	}

	/*
	 * public static boolean isNullOrEmptyStack(ItemStack stack) { return stack ==
	 * null || stack.isEmpty(); }
	 */

	/*
	 * public static void windowClick(int windowId, int slotId, int mouseButton,
	 * ClickType type) { Wrapper.INSTANCE.controller().windowClick(windowId, slotId,
	 * mouseButton, type, Wrapper.INSTANCE.player()); }
	 */

	public static void swingMainHand() {
		Wrapper.INSTANCE.player().swingItem();
	}

	public static void attack(Entity entity) {
		Wrapper.INSTANCE.controller().attackEntity(Wrapper.INSTANCE.player(), entity);
	}

	public static void drawRect(double left, double top, double right, double bottom, int color) {
		int j;
		if (left < right) {
			j = (int) left;
			left = right;
			right = j;
		}

		if (top < bottom) {
			j = (int) top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		net.minecraft.client.renderer.Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
		worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	/*
	 * public static void addEffect(int id, int duration, int amplifier) {
	 * Wrapper.INSTANCE.player().addPotionEffect(new PotionEffect(PotionEffect,
	 * duration, amplifier)); }
	 * 
	 * public static void removeEffect(int id) {
	 * Wrapper.INSTANCE.player().removePotionEffect(Potion.getPotionById(id)); }
	 * 
	 * public static void clearEffects() { for(PotionEffect effect :
	 * Wrapper.INSTANCE.player().getActivePotionEffects()) {
	 * Wrapper.INSTANCE.player().removePotionEffect(effect.getPotion()); }
	 */

	public static double[] teleportToPosition(double[] startPosition, double[] endPosition, double setOffset,
			double slack, boolean extendOffset, boolean onGround) {
		boolean wasSneaking = false;

		if (Wrapper.INSTANCE.player().isSneaking())
			wasSneaking = true;

		double startX = startPosition[0];
		double startY = startPosition[1];
		double startZ = startPosition[2];

		double endX = endPosition[0];
		double endY = endPosition[1];
		double endZ = endPosition[2];

		double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

		int count = 0;
		while (distance > slack) {
			distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

			if (count > 120) {
				break;
			}

			double offset = extendOffset && (count & 0x1) == 0 ? setOffset + 0.15D : setOffset;

			double diffX = startX - endX;
			double diffY = startY - endY;
			double diffZ = startZ - endZ;

			if (diffX < 0.0D) {
				if (Math.abs(diffX) > offset) {
					startX += offset;
				} else {
					startX += Math.abs(diffX);
				}
			}
			if (diffX > 0.0D) {
				if (Math.abs(diffX) > offset) {
					startX -= offset;
				} else {
					startX -= Math.abs(diffX);
				}
			}
			if (diffY < 0.0D) {
				if (Math.abs(diffY) > offset) {
					startY += offset;
				} else {
					startY += Math.abs(diffY);
				}
			}
			if (diffY > 0.0D) {
				if (Math.abs(diffY) > offset) {
					startY -= offset;
				} else {
					startY -= Math.abs(diffY);
				}
			}
			if (diffZ < 0.0D) {
				if (Math.abs(diffZ) > offset) {
					startZ += offset;
				} else {
					startZ += Math.abs(diffZ);
				}
			}
			if (diffZ > 0.0D) {
				if (Math.abs(diffZ) > offset) {
					startZ -= offset;
				} else {
					startZ -= Math.abs(diffZ);
				}
			}

			if (wasSneaking) {
				Wrapper.INSTANCE.sendPacket(new C0BPacketEntityAction(Wrapper.INSTANCE.player(),
						C0BPacketEntityAction.Action.STOP_SNEAKING));
			}
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(startX, startY, startZ, onGround));
			count++;
		}

		if (wasSneaking) {
			Wrapper.INSTANCE.sendPacket(
					new C0BPacketEntityAction(Wrapper.INSTANCE.player(), C0BPacketEntityAction.Action.START_SNEAKING));
		}

		return new double[] { startX, startY, startZ };
	}

	public static void selfDamage(double posY) {
		if (!Wrapper.INSTANCE.player().onGround)
			return;
		for (int i = 0; i <= 64.0D; i++) {
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX,
					Wrapper.INSTANCE.player().posY + posY, Wrapper.INSTANCE.player().posZ, false));
			Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.INSTANCE.player().posX,
					Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ, (i == 64.0D)));
		}
		Wrapper.INSTANCE.player().motionX *= 0.2;
		Wrapper.INSTANCE.player().motionZ *= 0.2;
		Utils.swingMainHand();
	}

	public static String getPlayerName(EntityPlayer player) {
		return player.getGameProfile() != null ? player.getGameProfile().getName() : player.getName();
	}

	public static boolean isPlayer(Entity entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			String entityName = getPlayerName(player);
			String playerName = getPlayerName(Wrapper.INSTANCE.player());
			if (entityName.equals(playerName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMurder(EntityLivingBase entity) {
		Utils.mysteryFind(entity, 0);
		if (!EnemyManager.murders.isEmpty()) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer murder = (EntityPlayer) entity;
				for (String name : EnemyManager.murders) {
					if (murder.getGameProfile().getName().equals(name)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isDetect(EntityLivingBase entity) {
		Utils.mysteryFind(entity, 1);
		if (!EnemyManager.detects.isEmpty()) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer murder = (EntityPlayer) entity;
				for (String name : EnemyManager.detects) {
					if (murder.getGameProfile().getName().equals(name)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void mysteryFind(EntityLivingBase entity, int target) {
		if (target == 0) {
			if (!EnemyManager.murders.isEmpty()) {
				for (int index = 0; index < EnemyManager.murders.size(); index++) {
					EntityLivingBase murder = Utils.getWorldEntityByName(EnemyManager.murders.get(index));
					if (murder == null) {
						EnemyManager.murders.remove(index);
					}
				}
			}
		} else if (target == 1) {
			if (!EnemyManager.detects.isEmpty()) {
				for (int index = 0; index < EnemyManager.detects.size(); index++) {
					EntityLivingBase detect = Utils.getWorldEntityByName(EnemyManager.detects.get(index));
					if (detect == null) {
						EnemyManager.detects.remove(index);
					}
				}
			}
		}
		if (entity instanceof EntityPlayerSP) {
			return;
		}

		if (!(entity instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entity;
		if (player.getGameProfile() == null) {
			return;
		}

		GameProfile profile = player.getGameProfile();

		if (profile.getName() == null) {
			return;
		}
		if (EnemyManager.murders.contains(profile.getName()) || EnemyManager.detects.contains(profile.getName())) {
			return;
		}

		if (player.inventory == null) {
			return;
		}

		for (int slot = 0; slot < 36; slot++) {

			ItemStack stack = player.inventory.getStackInSlot(slot);
			if (stack == null) {
				continue;
			}

			Item item = stack.getItem();

			if (item == null) {
				continue;
			}
			if (target == 0) {
				if ( // swords
				item == Items.iron_sword || item == Items.diamond_sword || item == Items.golden_sword
						|| item == Items.stone_sword || item == Items.wooden_sword
						// shovels
						|| item == Items.iron_shovel || item == Items.diamond_shovel || item == Items.golden_shovel
						|| item == Items.stone_shovel || item == Items.wooden_shovel
						// axes
						|| item == Items.iron_axe || item == Items.diamond_axe || item == Items.golden_axe
						|| item == Items.stone_axe || item == Items.wooden_axe
						// pickaxes
						|| item == Items.iron_pickaxe || item == Items.diamond_pickaxe || item == Items.golden_pickaxe
						|| item == Items.stone_pickaxe || item == Items.wooden_pickaxe
						// hoes
						|| item == Items.iron_hoe || item == Items.diamond_hoe || item == Items.golden_hoe
						|| item == Items.stone_hoe || item == Items.wooden_hoe
						// others
						|| item == Items.stick || item == Items.blaze_rod || item == Items.fishing_rod
						|| item == Items.carrot || item == Items.golden_carrot || item == Items.bone
						|| item == Items.cookie || item == Items.feather || item == Items.pumpkin_pie
						|| item == Items.cooked_fish || item == Items.fish || item == Items.shears
						|| item == Items.carrot_on_a_stick) {
					String name = player.getGameProfile().getName();
					EnemyManager.murders.add(name);
				}
			} else if (target == 1) {
				if (item == Items.bow) {
					String name = player.getGameProfile().getName();
					EnemyManager.detects.add(name);
				}
			}
		}
	}

	public static boolean checkEnemyNameColor(EntityLivingBase entity) {
		String name = entity.getDisplayName().getFormattedText();
		if (getEntityNameColor(Wrapper.INSTANCE.player()).equals(getEntityNameColor(entity))) {
			return false;
		}
		return true;
	}

	public static String getEntityNameColor(EntityLivingBase entity) {
		String name = entity.getDisplayName().getFormattedText();
		if (name.contains("\u00a7")) {
			if (name.contains("\u00a71")) {
				return "\u00a71";
			} else if (name.contains("\u00a72")) {
				return "\u00a72";
			} else if (name.contains("\u00a73")) {
				return "\u00a73";
			} else if (name.contains("\u00a74")) {
				return "\u00a74";
			} else if (name.contains("\u00a75")) {
				return "\u00a75";
			} else if (name.contains("\u00a76")) {
				return "\u00a76";
			} else if (name.contains("\u00a77")) {
				return "\u00a77";
			} else if (name.contains("\u00a78")) {
				return "\u00a78";
			} else if (name.contains("\u00a79")) {
				return "\u00a79";
			} else if (name.contains("\u00a70")) {
				return "\u00a70";
			} else if (name.contains("\u00a7e")) {
				return "\u00a7e";
			} else if (name.contains("\u00a7d")) {
				return "\u00a7d";
			} else if (name.contains("\u00a7a")) {
				return "\u00a7a";
			} else if (name.contains("\u00a7b")) {
				return "\u00a7b";
			} else if (name.contains("\u00a7c")) {
				return "\u00a7c";
			} else if (name.contains("\u00a7f")) {
				return "\u00a7f";
			}
			;
		}
		return "null";
	}

	public static int getPlayerArmorColor(EntityPlayer player, ItemStack stack) {
		if (player == null || stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemArmor))
			return -1;
		ItemArmor itemArmor = (ItemArmor) stack.getItem();
		if (itemArmor == null || itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER)
			return -1;
		return itemArmor.getColor(stack);
	}

	public static boolean checkEnemyColor(EntityPlayer enemy) {
		int colorEnemy0 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(0));
		int colorEnemy1 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(1));
		int colorEnemy2 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(2));
		int colorEnemy3 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(3));

		int colorPlayer0 = getPlayerArmorColor(Wrapper.INSTANCE.player(),
				Wrapper.INSTANCE.inventory().armorItemInSlot(0));
		int colorPlayer1 = getPlayerArmorColor(Wrapper.INSTANCE.player(),
				Wrapper.INSTANCE.inventory().armorItemInSlot(1));
		int colorPlayer2 = getPlayerArmorColor(Wrapper.INSTANCE.player(),
				Wrapper.INSTANCE.inventory().armorItemInSlot(2));
		int colorPlayer3 = getPlayerArmorColor(Wrapper.INSTANCE.player(),
				Wrapper.INSTANCE.inventory().armorItemInSlot(3));

		if (colorEnemy0 == colorPlayer0 && colorPlayer0 != -1 && colorEnemy0 != 1
				|| colorEnemy1 == colorPlayer1 && colorPlayer1 != -1 && colorEnemy1 != 1
				|| colorEnemy2 == colorPlayer2 && colorPlayer2 != -1 && colorEnemy2 != 1
				|| colorEnemy3 == colorPlayer3 && colorPlayer3 != -1 && colorEnemy3 != 1) {
			return false;
		}
		return true;
	}

	public static boolean screenCheck() {
		if (Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer
				|| Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat
				|| Wrapper.INSTANCE.mc().currentScreen instanceof GuiScreen)
			return false;
		return true;
	}

	public static double round(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static EntityLivingBase getWorldEntityByName(String name) {
		EntityLivingBase entity = null;
		for (Object object : Utils.getEntityList()) {
			if (object instanceof EntityLivingBase) {
				EntityLivingBase entityForCheck = (EntityLivingBase) object;
				if (entityForCheck.getName().contains(name)) {
					entity = entityForCheck;
				}
			}
		}
		return entity;
	}

	public static float[] getDirectionToBlock(int var0, int var1, int var2, EnumFacing var3) {
		EntityEgg var4 = new EntityEgg(Wrapper.INSTANCE.world());
		var4.posX = (double) var0 + 0.5D;
		var4.posY = (double) var1 + 0.5D;
		var4.posZ = (double) var2 + 0.5D;
		var4.posX += (double) var3.getDirectionVec().getX() * 0.25D;
		var4.posY += (double) var3.getDirectionVec().getY() * 0.25D;
		var4.posZ += (double) var3.getDirectionVec().getZ() * 0.25D;
		return getDirectionToEntity(var4);
	}

	private static float[] getDirectionToEntity(Entity var0) {
		return new float[] { getYaw(var0) + Wrapper.INSTANCE.player().rotationYaw,
				getPitch(var0) + Wrapper.INSTANCE.player().rotationPitch };
	}

	public static float getPitch(Entity entity) {
		double x = entity.posX - Wrapper.INSTANCE.player().posX;
		double y = entity.posY - Wrapper.INSTANCE.player().posY;
		double z = entity.posZ - Wrapper.INSTANCE.player().posZ;
		y /= Wrapper.INSTANCE.player().getDistanceToEntity(entity);
		double pitch = Math.asin(y) * 57.29577951308232;
		pitch = -pitch;
		return (float) pitch;
	}

	public static float getYaw(Entity entity) {
		double x = entity.posX - Wrapper.INSTANCE.player().posX;
		double y = entity.posY - Wrapper.INSTANCE.player().posY;
		double z = entity.posZ - Wrapper.INSTANCE.player().posZ;
		double yaw = Math.atan2(x, z) * 57.29577951308232;
		yaw = -yaw;
		return (float) yaw;
	}

	public static float[] getNeededRotations(Vec3 vec) {
		final Vec3 eyesPos = getEyesPos();
		final double diffX = vec.xCoord - eyesPos.xCoord;
		final double diffY = vec.yCoord - eyesPos.yCoord;
		final double diffZ = vec.zCoord - eyesPos.zCoord;
		final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
		final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
		return new float[] {
				Wrapper.INSTANCE.player().rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Wrapper.INSTANCE.player().rotationYaw),
				Wrapper.INSTANCE.player().rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Wrapper.INSTANCE.player().rotationPitch) };
	}

	public static float getDirection() {
		float var1 = Wrapper.INSTANCE.player().rotationYaw;
		if (Wrapper.INSTANCE.player().moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (Wrapper.INSTANCE.player().moveForward < 0.0F) {
			forward = -0.5F;
		} else if (Wrapper.INSTANCE.player().moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (Wrapper.INSTANCE.player().moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (Wrapper.INSTANCE.player().moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;
		return var1;
	}

	public static void faceVectorPacket(Vec3 vec) {
		float[] rotations = getNeededRotations(vec);
		EntityPlayerSP pl = Minecraft.getMinecraft().thePlayer;

		float preYaw = pl.rotationYaw;
		float prePitch = pl.rotationPitch;

		pl.rotationYaw = rotations[0];
		pl.rotationPitch = rotations[1];

		try {
			Method onUpdateWalkingPlayer = pl.getClass().getDeclaredMethod(Mapping.onUpdateWalkingPlayer);
			onUpdateWalkingPlayer.setAccessible(true);
			onUpdateWalkingPlayer.invoke(pl, new Object[0]);
		} catch (Exception ex) {
		}

		pl.rotationYaw = preYaw;
		pl.rotationPitch = prePitch;
	}

	public static void setEntityBoundingBoxSize(Entity entity, float width, float height) {
		if (entity.width == width && entity.height == height)
			return;
		entity.width = width;
		entity.height = height;
		double d0 = (double) width / 2.0D;
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0,
				entity.posY + (double) entity.height, entity.posZ + d0));

	}

	public static boolean placeBlockScaffold(final BlockPos pos) {
		final Vec3 eyesPos = new Vec3(Wrapper.INSTANCE.player().posX,
				Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(),
				Wrapper.INSTANCE.player().posZ);
		EnumFacing[] values;
		for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
			final EnumFacing side = values[i];

			final BlockPos neighbor = pos.offset(side);
			final EnumFacing side2 = side.getOpposite();
			// final Vec3d hitVec2 = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new
			// Vec3(side2.getDirectionVec()).scale(0.5));
			// final Vec3d hitVec2 = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new
			// Vec3(side2.getDirectionVec()).scale(0.5));

			if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos
					.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5)) && canBeClicked(neighbor)) {
				// final Vec3d hitVec2 = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new
				// Vec3(side2.getDirectionVec()).xCoord*0.5,);

				final Vec3 posVec = new Vec3((Vec3i) neighbor).addVector(0.5, 0.5, 0.5);
				final Vec3 dirVec = new Vec3(side2.getDirectionVec());
				final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));

				if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
					Utils.faceVectorPacketInstant(hitVec);
					Utils.swingMainHand();
					if (Wrapper.INSTANCE.controller().onPlayerRightClick(Wrapper.INSTANCE.player(),
							Wrapper.INSTANCE.world(), Wrapper.INSTANCE.player().getItemInUse(), neighbor, side2,
							hitVec)) {
						// Wrapper.INSTANCE.sendPacket(new C0APacketAnimation());
					}

					try {
						Field f = ReflectionHelper.findField(Minecraft.class,
								new String[] { "rightClickDelayTimer", "field_71467_ac" });
						f.setAccessible(true);
						f.set(Wrapper.INSTANCE.mc(), 4);
					} catch (Exception e) {
						e.printStackTrace();
					}

					return true;
				}
			}
		}
		return false;
	}
	/*
	 * public static boolean isInsideBlock(EntityLivingBase entity) { for (int x =
	 * MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper
	 * .floor_double(entity.getEntityBoundingBox().maxX) + 1; ++x) { for (int y =
	 * MathHelper.floor_double(entity.getEntityBoundingBox().minY); y < MathHelper
	 * .floor_double(entity.getEntityBoundingBox().maxY) + 1; ++y) { for (int z =
	 * MathHelper.floor_double(entity.getEntityBoundingBox().minZ); z < MathHelper
	 * .floor_double(entity.getEntityBoundingBox().maxZ) + 1; ++z) { final Block
	 * block = BlockUtils.getBlock(new BlockPos(x, y, z)); final AxisAlignedBB
	 * boundingBox; if (block != null && !(block instanceof BlockAir) &&
	 * (boundingBox = block.getCollisionBoundingBox(Wrapper.INSTANCE.world(),new
	 * BlockPos(x, y, z)) != null &&
	 * entity.getEntityBoundingBox().intersectsWith(boundingBox),BlockUtils.getState
	 * (new BlockPos(x, y, z)) ) { return true; } } } } return false; }
	 */

	public static boolean isBlockEdge(EntityLivingBase entity) {
		return (Wrapper.INSTANCE.world()
				.getCollidingBoundingBoxes(entity,
						entity.getEntityBoundingBox().offset(0.0D, -0.5D, 0.0D).expand(0.001D, 0.0D, 0.001D))
				.isEmpty() && entity.onGround);
	}

	public static void faceEntity(EntityLivingBase entity) {
		if (entity == null) {
			return;
		}

		double d0 = entity.posX - Wrapper.INSTANCE.player().posX;
		double d4 = entity.posY - Wrapper.INSTANCE.player().posY;
		double d1 = entity.posZ - Wrapper.INSTANCE.player().posZ;
		double d2 = Wrapper.INSTANCE.player().posY + (double) Wrapper.INSTANCE.player().getEyeHeight()
				- (entity.posY + (double) entity.getEyeHeight());
		double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d1 * d1);

		float f = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
		float f1 = (float) (-(Math.atan2(d2, d3) * 180.0D / Math.PI));

		Wrapper.INSTANCE.player().rotationYaw = f;
		Wrapper.INSTANCE.player().rotationPitch = f1;
	}

	public static void assistFaceEntity(Entity entity, float yaw, float pitch) {
		if (entity == null) {
			return;
		}

		double diffX = entity.posX - Wrapper.INSTANCE.player().posX;
		double diffZ = entity.posZ - Wrapper.INSTANCE.player().posZ;
		double yDifference;

		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			yDifference = entityLivingBase.posY + entityLivingBase.getEyeHeight()
					- (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
		} else {
			yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D
					- (Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight());
		}

		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float rotationYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float rotationPitch = (float) -(Math.atan2(yDifference, dist) * 180.0D / Math.PI);

		if (yaw > 0) {
			Wrapper.INSTANCE.player().rotationYaw = updateRotation(Wrapper.INSTANCE.player().rotationYaw, rotationYaw,
					yaw / 4);
		}
		if (pitch > 0) {
			Wrapper.INSTANCE.player().rotationPitch = updateRotation(Wrapper.INSTANCE.player().rotationPitch,
					rotationPitch, pitch / 4);
		}
	}

	public static float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
		if (var4 > p_70663_3_) {
			var4 = p_70663_3_;
		}
		if (var4 < -p_70663_3_) {
			var4 = -p_70663_3_;
		}
		return p_70663_1_ + var4;
	}

	public static int getDistanceFromMouse(final EntityLivingBase entity) {
		final float[] neededRotations = getRotationsNeeded(entity);
		if (neededRotations != null) {
			final float neededYaw = Wrapper.INSTANCE.player().rotationYaw - neededRotations[0];
			final float neededPitch = Wrapper.INSTANCE.player().rotationPitch - neededRotations[1];
			final float distanceFromMouse = MathHelper
					.sqrt_double(neededYaw * neededYaw + neededPitch * neededPitch * 2.0f);
			return (int) distanceFromMouse;
		}
		return -1;
	}

	public static float[] getSmoothNeededRotations(Vec3 vec, float yaw, float pitch) {
		final Vec3 eyesPos = getEyesPos();
		final double diffX = vec.xCoord - eyesPos.xCoord;
		final double diffY = vec.yCoord - eyesPos.yCoord;
		final double diffZ = vec.zCoord - eyesPos.zCoord;
		final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		final float rotationYaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
		final float rotationPitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

		return new float[] { updateRotation(Wrapper.INSTANCE.player().rotationYaw, rotationYaw, yaw / 4),
				updateRotation(Wrapper.INSTANCE.player().rotationPitch, rotationPitch, pitch / 4) };
	}

	public static float[] getRotationsNeeded(Entity entity) {
		if (entity == null) {
			return null;
		}

		double diffX = entity.posX - Wrapper.INSTANCE.mc().thePlayer.posX;
		double diffZ = entity.posZ - Wrapper.INSTANCE.mc().thePlayer.posZ;
		double diffY;

		if ((entity instanceof EntityLivingBase)) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight()
					- (Wrapper.INSTANCE.mc().thePlayer.posY + Wrapper.INSTANCE.mc().thePlayer.getEyeHeight());
		} else {
			diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D
					- (Wrapper.INSTANCE.mc().thePlayer.posY + Wrapper.INSTANCE.mc().thePlayer.getEyeHeight());
		}

		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

		return new float[] {
				Wrapper.INSTANCE.mc().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Wrapper.INSTANCE.mc().thePlayer.rotationYaw),
				Wrapper.INSTANCE.mc().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Wrapper.INSTANCE.mc().thePlayer.rotationPitch) };
	}

	private static class BlockData {
		public BlockPos position;
		public EnumFacing face;

		private BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}

	private static BlockData getBlockData(BlockPos pos) {
		if (isPosSolid(pos.add(0, -1, 0))) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}

		if (isPosSolid(pos.add(0, 1, 0))) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos1.add(0, 1, 0))) {
			return new BlockData(pos1.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos3.add(0, 1, 0))) {
			return new BlockData(pos3.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos4.add(0, 1, 0))) {
			return new BlockData(pos4.add(0, 0, 1), EnumFacing.DOWN);
		}

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
		if (isPosSolid(pos1.add(0, 1, 0))) {
			return new BlockData(pos1.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos2.add(0, 1, 0))) {
			return new BlockData(pos2.add(0, 0, 1), EnumFacing.DOWN);
		}
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

		if (isPosSolid(pos3.add(0, 1, 0))) {
			return new BlockData(pos3.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos4.add(0, 1, 0))) {
			return new BlockData(pos4.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos5.add(0, 1, 0))) {
			return new BlockData(pos5.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos6.add(0, 1, 0))) {
			return new BlockData(pos6.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos7.add(0, 1, 0))) {
			return new BlockData(pos7.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos8.add(0, 1, 0))) {
			return new BlockData(pos8.add(0, 0, 1), EnumFacing.DOWN);
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

		if (isPosSolid(pos9.add(0, 1, 0))) {
			return new BlockData(pos9.add(0, 0, 1), EnumFacing.DOWN);
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

	private static boolean isPosSolid(BlockPos pos) {
		Block block = Wrapper.INSTANCE.world().getBlockState(pos).getBlock();

		if ((block.getMaterial().isSolid() || !block.isTranslucent() || block.isFullCube()
				|| block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow
				|| block instanceof BlockSkull) && !block.getMaterial().isLiquid()
				&& !(block instanceof BlockContainer)) {
			return true;
		}

		return false;
	}

	public static Vec3 getVec3(BlockPos pos, EnumFacing face) {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		x += (double) face.getFrontOffsetX() / 2;
		z += (double) face.getFrontOffsetZ() / 2;
		y += (double) face.getFrontOffsetY() / 2;

		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += randomNumber(0.3, -0.3);
			z += randomNumber(0.3, -0.3);
		} else {
			y += randomNumber(0.3, -0.3);
		}

		if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
			z += randomNumber(0.3, -0.3);
		}

		if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
			x += randomNumber(0.3, -0.3);
		}

		return new Vec3(x, y, z);
	}

	public static double randomNumber(double max, double min) {
		return (Math.random() * (max - min)) + min;
	}

	public static BlockPos getHypixelBlockpos(String str) {
		int val = 89;

		if (str != null && str.length() > 1) {
			char[] chs = str.toCharArray();
			int lenght = chs.length;

			for (int i = 0; i < lenght; i++) {
				val += (int) chs[i] * str.length() * str.length() + (int) str.charAt(0) + (int) str.charAt(1);
			}

			val /= str.length();
		}

		return new BlockPos(val, -val % 255, val);
	}

	public static float[] getRotations(EntityLivingBase ent) {
		double x = ent.posX;
		double z = ent.posZ;
		double y = ent.posY + ent.getEyeHeight() / 2.0F;
		return getRotationFromPosition(x, z, y);
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
		return new float[] { yaw, pitch };
	}

	public static float getDistanceBetweenAngles(float angle1, float angle2) {
		float angle = Math.abs(angle1 - angle2) % 360.0F;

		if (angle > 180.0F) {
			angle = 360.0F - angle;
		}

		return angle;
	}

	public static float getPitchChange(float pitch, Entity entity, double posY) {
		double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F;
	}

	public static float getYawChange(float yaw, double posX, double posZ) {
		double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double yawToEntity = 0;

		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			if (deltaX != 0) {
				yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			}
		} else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
			if (deltaX != 0) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			}
		} else {
			if (deltaZ != 0) {
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
			}
		}

		return MathHelper.wrapAngleTo180_float(-(yaw - (float) yawToEntity));
	}

	public static boolean checkircuser() {
		try {
		if(WebUtils.get("https://gitee.com/VortexTeam/ensemble/raw/master/irc.txt").contains(IIIIIIIIII.getHWID())) {
			return true;
		}else {
			return false;
		}
	} catch (NoSuchAlgorithmException e) {
		ChatUtils.error("Network Error!");
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		ChatUtils.error("Error");
		e.printStackTrace();
	} catch (IOException e) {
		ChatUtils.error("Error");
		e.printStackTrace();
	}
		return true;
	}
	
	public static void rect(float x1, float y1, float x2, float y2, int fill) {
        GlStateManager.color(0, 0, 0);
        GL11.glColor4f(0, 0, 0, 0);

        float f = (fill >> 24 & 0xFF) / 255.0F;
        float f1 = (fill >> 16 & 0xFF) / 255.0F;
        float f2 = (fill >> 8 & 0xFF) / 255.0F;
        float f3 = (fill & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
	
	public static void rect(double x1, double y1, double x2, double y2, int fill) {
        GlStateManager.color(0, 0, 0);
        GL11.glColor4f(0, 0, 0, 0);

        float f = (fill >> 24 & 0xFF) / 255.0F;
        float f1 = (fill >> 16 & 0xFF) / 255.0F;
        float f2 = (fill >> 8 & 0xFF) / 255.0F;
        float f3 = (fill & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
}
