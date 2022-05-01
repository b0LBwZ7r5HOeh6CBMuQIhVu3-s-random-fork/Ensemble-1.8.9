package it.fktcod.ktykshrk.value;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.ChineseMode;

public class Value<T> {

    public T value;

    private String name;
    private String Chinese = null;
    private T defaultValue;

    public Value(String name, T defaultValue) {

        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;

    }
    public String getEName() {
        return name;
    }
    public String getName() {
        return ChineseMode.isCN? Chinese != null?Chinese:name:name;
    }
    public T getDefaultValue() {

        return defaultValue;
    }

    public T getValue() {

        return value;
    }

    public void setValue(T value) {

        this.value = value;
    }

    public void setChinese(String CNS) {
        this.Chinese = CNS;
    }
}
