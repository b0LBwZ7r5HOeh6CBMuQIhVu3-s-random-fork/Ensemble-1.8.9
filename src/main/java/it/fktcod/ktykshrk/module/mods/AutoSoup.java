package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoSoup extends Module{
	
	NumberValue health;
	NumberValue delay;
	BooleanValue drop;
	private int oldSlot = -1;
	
	public AutoSoup() {
	super("AutoSoup", HackCategory.COMBAT);
	
	health=new NumberValue("Health", 6.5, 0.5, 9.5);
	drop=new BooleanValue("Drop", false);
	delay=new NumberValue("Delay", 350D, 50D, 1000D);
	addValue(health,delay,drop);
		this.setChinese(Core.Translate_CN[16]);
	
	}
	
	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		 int soupSlot = getSoupFromInventory();

         if (soupSlot != -1 && mc.thePlayer.getHealth() < health.getValue())
         {
         	 this.swap(getSoupFromInventory(), 6);
              mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
              mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
              mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
         }
     
		super.onPlayerTick(event);
	}
	
	private int findSoup(int startSlot, int endSlot)
	{
		for(int i = startSlot; i < endSlot; i++)
		{
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			
			if(stack != null && stack.getItem() instanceof ItemSoup)
				return i;
		}
		
		return -1;
	}
	private boolean shouldEatSoup()
	{
		// check health
		if(mc.thePlayer.getHealth() > health.getValue() * 2F)
			return false;
		
		return true;
	}
	private void stopIfEating()
	{
		// check if eating
		if(oldSlot == -1)
			return;
		
		// stop eating
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
		
		// reset slot
		mc.thePlayer.inventory.currentItem = oldSlot;
		oldSlot = -1;
	}
	
	
	 protected void swap(int slot, int hotbarNum)
	    {
	        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	    }

	    public static int getSoupFromInventory()
	    {
	        Minecraft mc = Minecraft.getMinecraft();
	        int soup = -1;
	        int counter = 0;

	        for (int i = 1; i < 45; ++i)
	        {
	            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
	            {
	                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	                Item item = is.getItem();

	                if (Item.getIdFromItem(item) == 282)
	                {
	                    ++counter;
	                    soup = i;
	                }
	            }
	        }

	        return soup;
	    }
}
