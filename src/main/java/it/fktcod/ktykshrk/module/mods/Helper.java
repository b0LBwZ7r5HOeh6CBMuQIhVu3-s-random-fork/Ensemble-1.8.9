package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.CommandManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;

import static net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK;
import static net.minecraft.util.MovingObjectPosition.MovingObjectType.ENTITY;

public class Helper extends Module {

	public static BlockPos TeamBed;
	public Helper() {
		super("Helper", HackCategory.ANOTHER);
		setToggled(true);
		setShow(false);
		//this.setChinese(Core.Translate_CN[31]);
	}

	@Override
	public void onClientTick(TickEvent.ClientTickEvent event) {

		/*
		if (Mouse.isButtonDown(1) && Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown() && mc.thePlayer.onGround) {

			//自己床设置
			if (mc.objectMouseOver.typeOfHit == BLOCK) {
				Block block = BlockUtils.getBlock(Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos());
				BlockPos blockPos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();
				if (block instanceof BlockBed){

					if (TeamBed != null){
						ChatUtils.message("Team BED Changed");
					}else{
						ChatUtils.message("Team BED Has Been Set");
					}

					TeamBed = blockPos;

				}
			}
		}

		 */
	}

}
