package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class BlockOverlay extends Module{

	public BlockOverlay() {

		super("BlockOverlay", HackCategory.VISUAL);
		this.setChinese(Core.Translate_CN[23]);
	}
	
	@Override
    public String getDescription() {
        return "Show of selected block.";
    }
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if(Wrapper.INSTANCE.mc().objectMouseOver == null) {
			return;
		}
		if (Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit == MovingObjectType.BLOCK)
        {
            Block block = BlockUtils.getBlock(Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos());
            BlockPos blockPos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();

            if (Block.getIdFromBlock(block) == 0) {
                return;
            }
            RenderUtils.drawBlockESP(blockPos, 1F, 1F, 1F);
        }
		
		super.onRenderWorldLast(event);
	}

}