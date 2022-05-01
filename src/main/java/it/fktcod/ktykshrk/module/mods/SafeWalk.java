package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.BooleanValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class SafeWalk extends Module {
	BooleanValue sneak;
	
	public SafeWalk() {
		super("SafeWalk", HackCategory.MOVEMENT);
		sneak=new BooleanValue("Eagle", false);
		addValue(sneak);
        this.setChinese(Core.Translate_CN[79]);
	}
	
	@Override
	public void onPlayerTick(PlayerTickEvent event) {

	    if(sneak.getValue()){
            if(getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir) {
                if(mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
            } else {
                if(mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                }
            }
	    }
		super.onPlayerTick(event);
	}
	
	public Block getBlock(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    public Block getBlockUnderPlayer(EntityPlayer player) {
        return getBlock(new BlockPos(player.posX , player.posY - 1.0d, player.posZ));
    }
}
