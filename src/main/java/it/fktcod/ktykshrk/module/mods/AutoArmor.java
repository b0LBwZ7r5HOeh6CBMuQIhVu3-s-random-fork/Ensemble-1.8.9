package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoArmor extends Module{
	
	BooleanValue fakeinv,onlyINV;
	NumberValue DELAY;
	private TimerUtils timer = new TimerUtils();
	public AutoArmor() {
		super("AutoArmor",HackCategory.PLAYER);
		this.DELAY=new NumberValue("Delay", 1d, 0d, 10d);
		this.fakeinv=new BooleanValue("FakeInv", false);
		this.onlyINV=new BooleanValue("OnlyINV", false);
		this.addValue(DELAY,fakeinv);
		this.setChinese(Core.Translate_CN[10]);
	}
	

	@Override
	public void onClientTick(ClientTickEvent event) {
		long delay = (long) (DELAY.getValue() * 50L);

		if(this.timer.hasReached((float) delay)){
			if (onlyINV.getValue()){
				if (Wrapper.INSTANCE.mc().currentScreen instanceof GuiInventory ) {
					this.getBestArmor();
				}
			}else{
				this.getBestArmor();
			}
			timer.reset();
		}



		super.onClientTick(event);
	}
	public void getBestArmor() {
		for (int type = 1; type < 5; ++type) {
			if (Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
				ItemStack i = Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(4 + type).getStack();

				if (isBestArmor(i, type)) {
					continue;
				}

				
				if(fakeinv.getValue()) {
					C16PacketClientStatus is = new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT);
					Wrapper.INSTANCE.mc().thePlayer.sendQueue.addToSendQueue(is);
				}

				this.drop(4 + type);
			}

			for (int var4 = 9; var4 < 45; ++var4) {
				if (Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(var4).getHasStack()) {
					ItemStack var5 = Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(var4).getStack();

					if (isBestArmor(var5, type) && getProtection(var5) > 0.0F) {
						this.shiftClick(var4);
						this.timer.reset();

						if ((long)DELAY.getValue().longValue() > 0L) {
							return;
						}
					}
				}
			}
		}
	}
	public static boolean isBestArmor(ItemStack stack, int type) {
		float prot = getProtection(stack);
		String strType = "";

		if (type == 1) {
			strType = "helmet";
		} else if (type == 2) {
			strType = "chestplate";
		} else if (type == 3) {
			strType = "leggings";
		} else if (type == 4) {
			strType = "boots";
		}

		if (!stack.getUnlocalizedName().contains(strType)) {
			return false;
		} else {
			for (int i = 5; i < 45; ++i) {
				if (Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					ItemStack is = Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(i).getStack();

					if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) {
						return false;
					}
				}
			}

			return true;
		}
	}
	
	public void shiftClick(int slot) {
		Wrapper.INSTANCE.mc().playerController.windowClick(Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.windowId, slot, 0, 1, Wrapper.INSTANCE.mc().thePlayer);
	}

	public void drop(int slot) {
		Wrapper.INSTANCE.mc().playerController.windowClick(Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.windowId, slot, 1, 4, Wrapper.INSTANCE.mc().thePlayer);
	}

	public static float getProtection(ItemStack stack) {
		float prot = 0.0F;

		if (stack.getItem() instanceof ItemArmor) {
			ItemArmor armor = (ItemArmor) stack.getItem();
			prot = (float) ((double) prot + (double) armor.damageReduceAmount
					+ (double) ((100 - armor.damageReduceAmount)
							* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)
							/ 100.0D);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)
							/ 100.0D);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0D);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0D);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)
							/ 100.0D);
		}
		
		return prot;
	}


}
