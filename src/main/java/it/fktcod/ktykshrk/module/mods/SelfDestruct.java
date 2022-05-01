package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Nan0EventRegister;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.opengl.Display;


public class SelfDestruct extends Module {

    public static boolean isDes;

    public SelfDestruct() {
        super("SelfDestruct",  HackCategory.ANOTHER);
        //this.setChinese(Drift.Translate_CN[35]);
    }

    @Override
    public void onEnable() {
        ChatUtils.message("Destructed");
        mc.displayGuiScreen(null);
        Display.setTitle(Core.TrueTitle);
        for(Module m : Core.hackManager.getHacks()) {
            m.setToggled(false);
        }
        isDes = true;
        super.onEnable();
    }
}
