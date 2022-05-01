package it.fktcod.ktykshrk.hanabi.addon.hacks;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import it.fktcod.ktykshrk.Core;
import org.apache.commons.lang3.ArrayUtils;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ChestStealer extends Module {
	// Hanabi
	public int ticks;
	public S30PacketWindowItems packet;
	public static boolean isChest;

	BooleanValue onlychest;
	BooleanValue trash;
	BooleanValue tools;

	NumberValue delayValue;

	ModeValue mode;

	TimerUtils timerUtils = new TimerUtils();

	private final int[] itemHelmet;
	private final int[] itemChestplate;
	private final int[] itemLeggings;
	private final int[] itemBoots;

	public ChestStealer() {
		super("ChestStealer", HackCategory.PLAYER);
		this.itemHelmet = new int[] { 298, 302, 306, 310, 314 };
		this.itemChestplate = new int[] { 299, 303, 307, 311, 315 };
		this.itemLeggings = new int[] { 300, 304, 308, 312, 316 };
		this.itemBoots = new int[] { 301, 305, 309, 313, 317 };

		this.onlychest = new BooleanValue("OnlyChest", true);
		this.trash = new BooleanValue("Trash", true);
		this.tools = new BooleanValue("Tools", true);

		this.delayValue = new NumberValue("Delay", 30D, 0D, 1000D);

		this.mode = new ModeValue("Mode", new Mode("Fast", false), new Mode("Basic", true));

		this.addValue(onlychest, trash, tools, delayValue, mode);
		//this.setChinese(Core.Translate_CN[104]);

	}

	@Override
	public void onEnable() {
		ticks = 0;
		super.onEnable();
	}

	// fast hanabi code
	@Override
	public void onClientTick(ClientTickEvent event) {
		if (mode.getMode("Fast").isToggled()) {

			if (!isChest && onlychest.getValue())
				return;
			if (mc.thePlayer.openContainer != null) {
				if (mc.thePlayer.openContainer instanceof ContainerChest) {
					ContainerChest c = (ContainerChest) mc.thePlayer.openContainer;

					if (isChestEmpty(c)) {
						mc.thePlayer.closeScreen();
					}

					for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
						if (c.getLowerChestInventory().getStackInSlot(i) != null) {
							if (timerUtils.isDelayComplete(delayValue.getValue() + new Random().nextInt(100))
									&& (itemIsUseful(c, i) || trash.getValue())) {
								if (new Random().nextInt(100) > 80)
									continue; // Random
								mc.playerController.windowClick(c.windowId, i, 0, 1, mc.thePlayer);
								this.timerUtils.reset();
							}
						}
					}
				}
			}
		} else if (mode.getMode("Basic").isToggled()) {
			if (event.phase != Phase.START)
				return;
			EntityPlayerSP player = Wrapper.INSTANCE.player();
			if ((!Wrapper.INSTANCE.mc().inGameHasFocus) && (this.packet != null)
					&& ((Wrapper.INSTANCE.mc().currentScreen instanceof GuiChest))) {
				if (!isContainerEmpty(player.openContainer)) {
					for (int i = 0; i < player.openContainer.inventorySlots.size() - 36; ++i) {
						Slot slot = player.openContainer.getSlot(i);
						if (slot.getHasStack() && slot.getStack() != null) {
							if (this.ticks >= this.delayValue.getValue().intValue()) {
								ContainerChest c = (ContainerChest) mc.thePlayer.openContainer;
								mc.playerController.windowClick(c.windowId, i, 0, 1, mc.thePlayer);
								this.ticks = 0;
							}
						}
					}
					this.ticks += 1;
				} else {
					player.closeScreen();
					this.packet = null;
				}
			}
		}

		super.onClientTick(event);
	}

	private boolean isChestEmpty(ContainerChest c) {
		for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
			if (c.getLowerChestInventory().getStackInSlot(i) != null) {
				if (itemIsUseful(c, i) || trash.getValue()) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean isPotionNegative(ItemStack itemStack) {
		ItemPotion potion = (ItemPotion) itemStack.getItem();

		List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

		return potionEffectList.stream().map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
				.anyMatch(Potion::isBadEffect);
	}

	private boolean itemIsUseful(ContainerChest c, int i) {
		ItemStack itemStack = c.getLowerChestInventory().getStackInSlot(i);
		Item item = itemStack.getItem();

		if ((item instanceof ItemAxe || item instanceof ItemPickaxe) && tools.getValue()) {
			return true;
		}
		if (item instanceof ItemFood)
			return true;
		if (item instanceof ItemPotion && !isPotionNegative(itemStack))
			return true;
		if (item instanceof ItemSword && isBestSword(c, itemStack))
			return true;
		if (item instanceof ItemArmor && isBestArmor(c, itemStack))
			return true;
		return item instanceof ItemBlock;
	}

	private boolean isBestSword(ContainerChest c, ItemStack item) {
		float itemdamage1 = getSwordDamage(item);
		float itemdamage2 = 0f;
		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				float tempdamage = getSwordDamage(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
				if (tempdamage >= itemdamage2)
					itemdamage2 = tempdamage;
			}
		}
		for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
			if (c.getLowerChestInventory().getStackInSlot(i) != null) {
				float tempdamage = getSwordDamage(c.getLowerChestInventory().getStackInSlot(i));
				if (tempdamage >= itemdamage2)
					itemdamage2 = tempdamage;
			}
		}
		return itemdamage1 == itemdamage2;
	}

	private float getSwordDamage(ItemStack itemStack) {
		float damage = 0f;
		Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
		if (attributeModifier.isPresent()) {
			damage = (float) ((AttributeModifier) attributeModifier.get()).getAmount();
		}
		return damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
	}

	private boolean isBestArmor(ContainerChest c, ItemStack item) {
		float itempro1 = ((ItemArmor) item.getItem()).damageReduceAmount;
		float itempro2 = 0f;
		if (isContain(itemHelmet, Item.getIdFromItem(item.getItem()))) { // ͷ��
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemHelmet,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemHelmet,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		if (isContain(itemChestplate, Item.getIdFromItem(item.getItem()))) { // �ؼ�
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemChestplate,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemChestplate,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		if (isContain(itemLeggings, Item.getIdFromItem(item.getItem()))) { // ����
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemLeggings,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemLeggings,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		if (isContain(itemBoots, Item.getIdFromItem(item.getItem()))) { // Ь��
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemBoots,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemBoots,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		return itempro1 == itempro2;
	}

	public static boolean isContain(int[] arr, int targetValue) {
		return ArrayUtils.contains(arr, targetValue);
	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (mode.getMode("Basic").isToggled()) {
			if (side == Side.IN && packet instanceof S30PacketWindowItems) {
				this.packet = (S30PacketWindowItems) packet;
			}
		}
		return true;
	}

	boolean isContainerEmpty(Container container) {
		boolean temp = true;
		int i = 0;
		for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 35; i < slotAmount; i++) {
			if (container.getSlot(i).getHasStack()) {
				temp = false;
			}
		}
		return temp;
	}

}
