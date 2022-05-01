package it.fktcod.ktykshrk.hanabi.addon.hacks;

import java.util.Optional;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.hanabi.addon.utils.InvUtils;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class InvCleaner extends Module {

	private double handitemAttackValue;
	private int currentSlot = 9;

	TimerUtils timerUtils = new TimerUtils();

	// settings
	NumberValue delayValue;

	static BooleanValue toggleValue;
	static BooleanValue keepToolsValue;
	static BooleanValue keepArmorValue;
	static BooleanValue keepBowValue;
	static BooleanValue keepBuckValue;
	static BooleanValue keepArrowValue;
	static BooleanValue inInvValue;

	public InvCleaner() {
		super("InvCleaner", HackCategory.PLAYER);
		this.delayValue = new NumberValue("Delay", 80D, 0D, 1000D);

		this.toggleValue = new BooleanValue("AutoToggle", false);
		this.keepToolsValue = new BooleanValue("KeepTools", false);
		this.keepArmorValue = new BooleanValue("KeepArmor", false);
		this.keepArrowValue = new BooleanValue("KeepArrow", false);
		this.keepBowValue = new BooleanValue("KeepBow", false);
		this.keepBuckValue = new BooleanValue("KeepBuck", false);
		this.inInvValue = new BooleanValue("InInv", false);

		this.addValue(toggleValue, keepArmorValue, keepArrowValue, keepBowValue, keepBuckValue, keepToolsValue,
				inInvValue, delayValue);
		//this.setChinese(Core.Translate_CN[105]);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		Utils.nullCheck();
		if (mc.thePlayer == null || mc.theWorld == null) {
			return;
		}
		if (!this.isToggled() || mc.currentScreen instanceof GuiChest)
			return;

		if (currentSlot >= 45) {
			currentSlot = 9;
			if (mc.thePlayer.ticksExisted % 40 == 0 || toggleValue.getValue()) {
				InvUtils.getBestAxe();
				InvUtils.getBestPickaxe();
				InvUtils.getBestShovel();
			}
			if (toggleValue.getValue()) {
				this.setToggled(false);
				return;
			}
		}

		if (!inInvValue.getValue() || mc.currentScreen instanceof GuiInventory) {
			handitemAttackValue = getSwordAttackDamage(mc.thePlayer.getHeldItem());
			final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(currentSlot).getStack();
			if (timerUtils.hasReached(delayValue.getValue().floatValue())) {
				if (isShit(currentSlot) && getSwordAttackDamage(itemStack) <= handitemAttackValue
						&& itemStack != mc.thePlayer.getHeldItem()) {
					mc.playerController.windowClick(0, currentSlot, 1, 4, mc.thePlayer);
					timerUtils.reset();
				}
				currentSlot++;
			}
		}
		super.onClientTick(event);
	}

	@Override
	public void onEnable() {
		currentSlot = 9;
		handitemAttackValue = getSwordAttackDamage(mc.thePlayer.getHeldItem());
		super.onEnable();
	}

	public static boolean isShit(int slot) {
		ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();

		if (itemStack == null)
			return false;

		if (itemStack.getItem() == Items.stick)
			return true;

		if (itemStack.getItem() == Items.egg)
			return true;

		if (itemStack.getItem() == Items.bone)
			return true;

		if (itemStack.getItem() == Items.bowl)
			return true;

		if (itemStack.getItem() == Items.glass_bottle)
			return true;

		if (itemStack.getItem() == Items.string)
			return true;

		if (itemStack.getItem() == Items.flint && getItemAmount(Items.flint) > 1)
			return true;

		if (itemStack.getItem() == Items.compass && getItemAmount(Items.compass) > 1)
			return true;

		if (itemStack.getItem() == Items.feather)
			return true;

		if (itemStack.getItem() == Items.fishing_rod)
			return true;

		// buckets
		if (itemStack.getItem() == Items.bucket && !keepBuckValue.getValue())
			return true;

		if (itemStack.getItem() == Items.lava_bucket && !keepBuckValue.getValue())
			return true;

		if (itemStack.getItem() == Items.water_bucket && !keepBuckValue.getValue())
			return true;

		if (itemStack.getItem() == Items.milk_bucket && !keepBuckValue.getValue())
			return true;

		// arrow
		if (itemStack.getItem() == Items.arrow && !keepArrowValue.getValue())
			return true;

		if (itemStack.getItem() == Items.snowball)
			return true;

		if (itemStack.getItem() == Items.fish)
			return true;

		if (itemStack.getItem() == Items.experience_bottle)
			return true;

		// tools
		if (itemStack.getItem() instanceof ItemTool && (!keepToolsValue.getValue() || !isBestTool(itemStack)))
			return true;

		// sword
		if (itemStack.getItem() instanceof ItemSword && (!keepToolsValue.getValue() || !isBestSword(itemStack)))
			return true;

		// armour
		if (itemStack.getItem() instanceof ItemArmor && (!keepArmorValue.getValue() || !isBestArmor(itemStack)))
			return true;

		// bow
		if (itemStack.getItem() instanceof ItemBow && (!keepBowValue.getValue() || !isBestBow(itemStack)))
			return true;

		if (itemStack.getItem().getUnlocalizedName().contains("potion")) {
			return isBadPotion(itemStack);
		}

		return false;
	}

	private static int getItemAmount(Item shit) {
		int result = 0;

		for (Object item : mc.thePlayer.inventoryContainer.inventorySlots) {
			Slot slot = (Slot) item;

			if (slot.getHasStack() && slot.getStack().getItem() == shit)
				result++;
		}
		return result;
	}

	private static boolean isBestTool(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemTool))
				continue;

			if (itemStack == input)
				continue;

			if (itemStack.getItem() instanceof ItemPickaxe && !(input.getItem() instanceof ItemPickaxe))
				continue;

			if (itemStack.getItem() instanceof ItemAxe && !(input.getItem() instanceof ItemAxe))
				continue;

			if (itemStack.getItem() instanceof ItemSpade && !(input.getItem() instanceof ItemSpade))
				continue;

			if (getToolEffencly(itemStack) >= getToolEffencly(input))
				return false;
		}
		return true;
	}

	private static boolean isBestSword(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemSword))
				continue;

			if (itemStack == input)
				continue;

			if (getSwordAttackDamage(itemStack) >= getSwordAttackDamage(input))
				return false;
		}
		return true;
	}

	private static boolean isBestBow(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemBow))
				continue;

			if (itemStack == input)
				continue;

			if (getBowAttackDamage(itemStack) >= getBowAttackDamage(input))
				return false;
		}
		return true;
	}

	private static boolean isBestArmor(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemArmor))
				continue;

			if (itemStack == input)
				continue;

			if (((ItemArmor) itemStack.getItem()).armorType != ((ItemArmor) input.getItem()).armorType)
				continue;

			if (InvUtils.getArmorScore(itemStack) >= InvUtils.getArmorScore(input))
				return false;
		}
		return true;
	}

	private static boolean isBadPotion(final ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			final ItemPotion potion = (ItemPotion) stack.getItem();
			for (final Object o : potion.getEffects(stack)) {
				final PotionEffect effect = (PotionEffect) o;
				if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.moveSlowdown.getId()
						|| effect.getPotionID() == Potion.harm.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	private static double getSwordAttackDamage(ItemStack itemStack) {
		float damage = 0f;
		if (itemStack != null) {

			Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

			if (attributeModifier.isPresent()) {
				damage = (float) ((AttributeModifier) attributeModifier.get()).getAmount();
			}
		}
		return damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
	}

	private static double getBowAttackDamage(ItemStack itemStack) {
		if (itemStack == null || !(itemStack.getItem() instanceof ItemBow))
			return 0;

		return EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack)
				+ (EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack) * 0.1)
				+ (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) * 0.1);
	}

	private static double getToolEffencly(ItemStack itemStack) {
		if (itemStack == null || !(itemStack.getItem() instanceof ItemTool))
			return 0;

		ItemTool sword = (ItemTool) itemStack.getItem();

		return EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
	}
}
