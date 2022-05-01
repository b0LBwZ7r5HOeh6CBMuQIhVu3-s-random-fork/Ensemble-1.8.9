package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class AutoTool extends Module {
    private final BooleanValue hotkeyBack;
    private BooleanValue doDelay;
    private NumberValue minDelay;
    private NumberValue maxDelay;
    public static int previousSlot;
    public static boolean justFinishedMining, mining;

    public AutoTool() {

        super("AutoTool", HackCategory.PLAYER);
        //this.setChinese(Drift.Translate_CN[28]);
        this.addValue(hotkeyBack = new BooleanValue("Hotkey back", true));
        this.addValue(doDelay = new BooleanValue("Random delay", true));
        this.addValue(minDelay = new NumberValue("Min delay", 25D, 0D, 600D));
        this.addValue(maxDelay = new NumberValue("Max delay", 100D, 0D, 600D));
    }

    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        //////////System.out.println(mc.currentScreen);

        if(Mouse.isButtonDown(0)) {

            BlockPos lookingAtBlock = mc.objectMouseOver.getBlockPos();
            if (lookingAtBlock != null) {
                Block stateBlock = mc.theWorld.getBlockState(lookingAtBlock).getBlock();
                if (stateBlock != Blocks.air && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block) {
                    if(!mining) {
                        previousSlot = Utils.getCurrentPlayerSlot();
                        mining = true;
                    }
                    int index = -1;
                    double speed = 1;


                    for (int slot = 0; slot <= 8; slot++) {
                        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
                        if(itemInSlot != null && itemInSlot.getItem() instanceof ItemTool) {
                            BlockPos p = mc.objectMouseOver.getBlockPos();
                            Block bl = mc.theWorld.getBlockState(p).getBlock();

                            if(itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed) {
                                speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                                index = slot;
                            }
                        }
                        else if(itemInSlot != null && itemInSlot.getItem() instanceof ItemShears) {
                            BlockPos p = mc.objectMouseOver.getBlockPos();
                            Block bl = mc.theWorld.getBlockState(p).getBlock();

                            if(itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed) {
                                speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                                index = slot;
                            }
                        }
                    }

                    if(index == -1 || speed <= 1.1 || speed == 0) {
                    } else {
                        Utils.hotkeyToSlot(index);
                    }
                }
                else{
                }
            }
            else {
            }


        }
        else {
            if(mining)
                finishMining();
        }


    }

    public static void hotkeyToPickAxe() {
        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
            if(itemInSlot != null && itemInSlot.getItem() instanceof ItemPickaxe) {
                BlockPos p = mc.objectMouseOver.getBlockPos();
                Block bl = mc.theWorld.getBlockState(p).getBlock();
            }
        }
    }

    public void finishMining(){
        if(hotkeyBack.getValue()) {
            Utils.hotkeyToSlot(previousSlot);
        }
        justFinishedMining = false;
        mining = false;
    }

}
