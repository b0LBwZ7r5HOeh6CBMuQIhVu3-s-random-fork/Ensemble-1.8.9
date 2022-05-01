package it.fktcod.ktykshrk.utils.visual.font;

public interface IBFFontRenderer
{
    StringCache getStringCache();

    void setStringCache(StringCache value);

    boolean isDropShadowEnabled();

    void setDropShadowEnabled(boolean value);

    boolean isEnabled();

    void setEnabled(boolean value);
}
