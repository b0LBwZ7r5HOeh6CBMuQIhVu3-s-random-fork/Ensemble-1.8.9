package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoSword extends Module{
	TimerUtils timer=new TimerUtils();
	TimerUtils time=new TimerUtils();
	public AutoSword() {
		super("AutoSword", HackCategory.COMBAT);
		this.setChinese(Core.Translate_CN[20]);
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
if (!timer.isDelay(300)) return;
		
		if (!time.isDelay(1000L) || (mc.currentScreen != null && !(mc.currentScreen instanceof GuiInventory)))
			return;
		
		int best = -1;
		float swordDamage = 0;
		for (int i = 9; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (is.getItem() instanceof ItemSword) {
					float swordD = getSharpnessLevel(is);
					if (swordD > swordDamage) {
						swordDamage = swordD;
						best = i;
					}
				}
			}
		}
		final ItemStack current = mc.thePlayer.inventoryContainer.getSlot(1 + 35).getStack();
		if (best != -1 && (current == null || !(current.getItem() instanceof ItemSword) || swordDamage > getSharpnessLevel(current))) {
			
			/*
			 * try { if
			 * (!Hanabi.AES_UTILS.decrypt(Hanabi.HWID_VERIFY).contains(Wrapper.getHWID())) {
			 * FMLCommonHandler.instance().exitJava(0, true); Client.sleep = true; } } catch
			 * (Exception e) { FMLCommonHandler.instance().exitJava(0, true); Client.sleep =
			 * true; }
			 * 
			 */
			timer.reset();
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, best, 1 - 1, 2, mc.thePlayer);
			time.reset();
		
	}
		super.onClientTick(event);
	}
	
	private float getSharpnessLevel(ItemStack stack) {
		float damage = ((ItemSword) stack.getItem()).getDamageVsEntity();
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,stack) * 0.01f;
		return damage;
	}
}
