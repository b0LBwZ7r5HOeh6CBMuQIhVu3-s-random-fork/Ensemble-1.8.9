package it.fktcod.ktykshrk.script.api;

import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.*;
import it.fktcod.ktykshrk.utils.*;

import java.util.ArrayList;

public class Values {
    private Module mod;

    public Values(Module mod) {
        this.mod = mod;
    }

    public NumberValue addNumberValue(String name, double value, double min, double max) {
        NumberValue num = new NumberValue(name, value, min, max);
        try {
            mod.addValue(num);
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtils.error("添加参数失败");
        }
        return num;
    }

    public BooleanValue addBooleanValue(String name, boolean state) {
        BooleanValue bool = new BooleanValue(name, state);
        try {
            mod.addValue(bool);
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtils.error("添加参数失败");
        }
        return bool;
    }

    public ModeValue addModeValue(String name, String[] values, String value) {

        ArrayList<Mode> modes = null;
        Mode[] modes2 = new Mode[0];

        for (int i = 0; i <values.length ; i++) {
            modes.add(new Mode(values[i],false));
            modes.toArray(modes2);
        }
        ModeValue mode = new ModeValue(name, modes2);

        try {
            mod.addValue(mode);
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtils.error("添加参数失败");
        }
        return mode;
    }
}
