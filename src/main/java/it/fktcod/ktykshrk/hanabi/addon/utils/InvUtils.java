package it.fktcod.ktykshrk.hanabi.addon.utils;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class InvUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int findEmptySlot() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] == null)
                return i;
        }

        return mc.thePlayer.inventory.currentItem + (mc.thePlayer.inventory.getCurrentItem() == null ? 0 : ((mc.thePlayer.inventory.currentItem < 8) ? 3 : -1));
    }

    // TODO: AutoPot refill always put potions on slot 1, bugs here?
    public static int findEmptySlot(int priority) {
        if (mc.thePlayer.inventory.mainInventory[priority] == null)
            return priority;

        return findEmptySlot();
    }

	private static final int hotBarSize = 9;
	private static final int inventoryContainerSize = 36;
	private static final int allInventorySize = (hotBarSize + inventoryContainerSize);

	public static int getHotBarSize() {
		return hotBarSize;
	}

	public static int getInventoryContainerSize() {
		return inventoryContainerSize;
	}

	public static int getAllInventorySize() {
		return allInventorySize;
	}

	public static Slot getSlot(int id) {
		return mc.thePlayer.inventoryContainer.getSlot(id);
	}




    public static void swapShift(int slot) {
        mc.playerController.windowClick(
                mc.thePlayer.inventoryContainer.windowId, slot, 0, 1,
                mc.thePlayer);
    }

    public static void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(
                mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
                mc.thePlayer);
    }

    public static boolean isFull() {
        return !Arrays.asList(mc.thePlayer.inventory.mainInventory).contains(null);
    }

    public static int armorSlotToNormalSlot(int armorSlot) {
        return 8 - armorSlot;
    }

    public static void block() {
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
    }

    public static ItemStack getCurrentItem() {
        return mc.thePlayer.getCurrentEquippedItem() == null ? new ItemStack(Blocks.air) : mc.thePlayer.getCurrentEquippedItem();
    }

    public static ItemStack getItemBySlot(int slot) {
        return mc.thePlayer.inventory.mainInventory[slot] == null ? new ItemStack(Blocks.air) : mc.thePlayer.inventory.mainInventory[slot];
    }

    public static List<ItemStack> getHotbarContent() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            result.add(mc.thePlayer.inventory.mainInventory[i]);
        }
        return result;
    }

    public static List<ItemStack> getAllInventoryContent() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            result.add(mc.thePlayer.inventory.mainInventory[i]);
        }
        return result;
    }

    public static List<ItemStack> getInventoryContent() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 9; i < 35; i++) {
            result.add(mc.thePlayer.inventory.mainInventory[i]);
        }
        return result;
    }

    public static int getEmptySlotInHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] == null)
                return i;
        }
        return -1;
    }

    public static float getArmorScore(ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor))
            return -1;

        ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
        float score = 0;

        //basic reduce amount
        score += itemArmor.damageReduceAmount;

        if (EnchantmentHelper.getEnchantments(itemStack).size() <= 0)
            score -= 0.1;

        int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);

        score += protection * 0.2;

        return score;
    }

    public static boolean hasWeapon() {
        if (mc.thePlayer.inventory.getCurrentItem() != null)
            return false;

        return (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe) || (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword);
    }

    public static boolean isHeldingSword() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }
    public static int pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
    
    public static void getBestPickaxe() {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestPickaxe(is) && pickaxeSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
							swap(i, pickaxeSlot - 36);
						} else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
							swap(i, pickaxeSlot - 36);
						}

				}
			}
		}
	}

    public static void getBestShovel() {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestShovel(is) && shovelSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
							swap(i, shovelSlot - 36);
						} else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) {
							swap(i, shovelSlot - 36);
						}

				}
			}
		}
	}

    public static void getBestAxe() {

		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestAxe(is) && axeSlot != i) {
					if (!isBestWeapon(is)) {
						if (!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
							swap(i, axeSlot - 36);
						} else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) {
							swap(i, axeSlot - 36);
						}
					}
				}
			}
		}
	}

    public static boolean isBestPickaxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemPickaxe))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
					return false;
				}

			}
		}
		return true;
	}

    public static boolean isBestShovel(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemSpade))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
					return false;
				}

			}
		}
		return true;
	}

    public static boolean isBestAxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemAxe))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
					return false;
				}

			}
		}
		return true;
	}

    public static float getToolEffect(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemTool))
			return 0;
		String name = item.getUnlocalizedName();
		ItemTool tool = (ItemTool) item;
		float value = 1;
		if (item instanceof ItemPickaxe) {
			value = tool.getStrVsBlock(stack, Blocks.stone);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else if (item instanceof ItemSpade) {
			value = tool.getStrVsBlock(stack, Blocks.dirt);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else if (item instanceof ItemAxe) {
			value = tool.getStrVsBlock(stack, Blocks.log);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else
			return 1f;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;
		return value;
	}
	
	public static boolean isBestWeapon(ItemStack stack) {
		float damage = getDamage(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getDamage(is) > damage && (is.getItem() instanceof ItemSword))
					return false;
			}
		}
		return stack.getItem() instanceof ItemSword;

	}
	
	public static float getDamage(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			ItemTool tool = (ItemTool) item;
			damage += tool.getDamage(stack);
		}
		if (item instanceof ItemSword) {
			ItemSword sword = (ItemSword) item;
			damage += sword.getDamageVsEntity();
		}
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f
				+ EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
		return damage;
	}
}
