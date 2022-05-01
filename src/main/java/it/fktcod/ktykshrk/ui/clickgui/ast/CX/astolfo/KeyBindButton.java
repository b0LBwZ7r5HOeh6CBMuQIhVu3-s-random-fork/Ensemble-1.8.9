package it.fktcod.ktykshrk.ui.clickgui.ast.CX.astolfo;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.font.FontLoaders;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class KeyBindButton
        extends ValueButton {
    public Module cheat;
    public double opacity = 0.0;
    public boolean bind;
    public HackCategory category;

    public KeyBindButton(HackCategory category, Module cheat, int x, int y) {
        super(category, null, x, y);
        this.category = category;
        this.custom = true;
        this.bind = false;
        this.cheat = cheat;
    }

    @Override
    public void render(int mouseX, int mouseY, Limitation limitation) {
        GL11.glEnable((int)3089);
        limitation.cut();
        Gui.drawRect((int)0, (int)0, (int)0, (int)0, (int)0);
        Gui.drawRect((int)(this.x - 10), (int)(this.y - 4), (int)(this.x + 80), (int)(this.y + 11), (int)new Color(39, 39, 39).getRGB());
        FontLoaders.kiona14.drawString("Bind", this.x - 7, this.y + 2, new Color(108, 108, 108).getRGB());
      //  FontLoaders.kiona14.drawString(String.valueOf(this.bind ? "" : "") + Keyboard.getKeyName((int)this.cheat.getKey()), this.x + 77 - FontLoaders.kiona14.getStringWidth(Keyboard.getKeyName((int)this.cheat.getKey())), this.y + 2, new Color(108, 108, 108).getRGB());
        GL11.glDisable((int)3089);
    }

    @Override
    public void key(char typedChar, int keyCode) {
        if (this.bind) {
            this.cheat.setKey(keyCode);
            if (keyCode == 1) {
                this.cheat.setKey(0);
            }
            ClickUi.binding = false;
            this.bind = false;
        }
        super.key(typedChar, keyCode);
    }

    @Override
    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.kiona18.getStringHeight(this.cheat.getName()) + 5 && button == 0) {
            this.bind = !this.bind;
            ClickUi.binding = this.bind;
        }
        super.click(mouseX, mouseY, button);
    }
}
