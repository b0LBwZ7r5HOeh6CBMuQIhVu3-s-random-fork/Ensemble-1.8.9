package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.TranslationManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;

public class ChineseMode extends Module {

    public static boolean isCN = false;

    public ChineseMode() {
        super("ChineseMode", HackCategory.ANOTHER);
        this.setChinese(Core.Translate_CN[27]);
    }

    @Override
    public void onEnable(){

        isCN = true;
        TranslationManager.setValuesTranslation();
    }
    @Override
    public void onDisable(){
        isCN = false;
    }
}
