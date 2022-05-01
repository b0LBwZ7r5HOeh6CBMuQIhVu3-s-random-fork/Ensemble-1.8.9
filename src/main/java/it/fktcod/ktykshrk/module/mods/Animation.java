package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;

public class Animation extends Module {
    public static ModeValue modeValue;

    public static BooleanValue all;
    public static BooleanValue chat;

    public static NumberValue posx;
    public static NumberValue posz;
    public static NumberValue posy;
    public static NumberValue scalevalue;


    //Author Zenwix
    public Animation() {
        super("Animation", HackCategory.VISUAL);
        setToggled(true);
        setShow(false);
        all = new BooleanValue("All", true);
        chat = new BooleanValue("Chat", false);
        posx = new NumberValue("ItemPosX", 0.56D, -1.0D, 1.0D);
		posy = new NumberValue("ItemPosY", -0.52D, -1.0D, 1.0D);
        posz = new NumberValue("ItemPosZ", -0.7D, -1.0D, 1.0D);
        scalevalue = new NumberValue("ItemScale", 0.4D, 0.0D, 2.0D);
        modeValue = new ModeValue("Mode", new Mode("Vanilla", true), new Mode("Swang", false), new Mode("1.7", false),
                new Mode("Swank", false), new Mode("Sigma", false), new Mode("Jello", false), new Mode("Slide", false),
                new Mode("Ohare", false), new Mode("Wizzard", false), new Mode("Lennox", false),
                new Mode("Push", false), new Mode("Chill", false), new Mode("Butter", false), new Mode("Hanabi", false), new Mode("Tap", false),new Mode("NONE",false));

        this.addValue(modeValue, all, chat, posx, posy,posz, scalevalue);
        this.setChinese(Core.Translate_CN[1]);
        this.setMixin();
    }
}
