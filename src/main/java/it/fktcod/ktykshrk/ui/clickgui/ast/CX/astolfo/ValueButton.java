package it.fktcod.ktykshrk.ui.clickgui.ast.CX.astolfo;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.utils.visual.font.FontLoaders;
import it.fktcod.ktykshrk.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class ValueButton {
    public Value value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public int y;
    public double opacity;
    public HackCategory category;
    public ModeValue priority;
    public ModeValue mode;
    public ModeValue rotations;


    public ValueButton(HackCategory category, Value value, int x, int y) {
        this.category = category;
        this.custom = false;
        this.opacity = 0.0;
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value instanceof BooleanValue) {
            this.change = (Boolean)((BooleanValue)this.value).getValue();
        } else if (this.value instanceof ModeValue) {
            this.name = "" + ((ModeValue)this.value).getValue();
        } else if (value instanceof NumberValue) {
            NumberValue v = (NumberValue)value;
            this.name = String.valueOf(this.name + (double)v.getValue().intValue());
        }
        this.opacity = 0.0;
    }

    public void render(int mouseX, int mouseY, Limitation limitation) {
        if (!this.custom) {
            this.opacity = mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.kiona18.getStringHeight(this.value.getName()) + 6 ? (this.opacity + 10.0 < 200.0 ? (this.opacity += 10.0) : 200.0) : (this.opacity - 6.0 > 0.0 ? (this.opacity -= 6.0) : 0.0);
            if (this.value instanceof BooleanValue) {
                this.change = (Boolean)((BooleanValue)this.value).getValue();
            } else if (this.value instanceof ModeValue) {
                this.name = "" + ((ModeValue)this.value).getSelectMode().getName();
            } else if (this.value instanceof NumberValue) {
                NumberValue v = (NumberValue)this.value;
                this.name = "" + (double)((Number)v.getValue()).intValue();
                if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) - 10 && mouseY < this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) + 2 && Mouse.isButtonDown((int)0)) {
                    double min = ((Number)v.getMin()).doubleValue();
                    double max = ((Number)v.getMax()).doubleValue();
                    double inc = ((Number)v.getValue()).doubleValue();
                    double valAbs = (double)mouseX - ((double)this.x + 1.0);
                    double perc = valAbs / 68.0;
                    perc = Math.min(Math.max(0.0, perc), 1.0);
                    double valRel = (max - min) * perc;
                    double val = min + valRel;
                    val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                    v.setValue(val);
                }
            }
            int staticColor = this.category.name().equals("COMBAT") ? new Color(231, 76, 60).getRGB() : (this.category.name().equals("VISUAL") ? new Color(54, 1, 205).getRGB() : (this.category.name().equals("MOVEMENTF") ? new Color(45, 203, 113).getRGB() : (this.category.name().equals("PLAYER") ? new Color(141, 68, 173).getRGB() : (this.category.name().equals("ANOTHER") ? new Color(38, 154, 255).getRGB() : new Color(38, 154, 255).getRGB()))));
            GL11.glEnable((int)3089);
            limitation.cut();
            Gui.drawRect((int)(this.x - 10), (int)(this.y - 4), (int)(this.x + 80), (int)(this.y + 11), (int)new Color(39, 39, 39).getRGB());
            if (this.value instanceof BooleanValue) {
                FontLoaders.kiona14.drawString(this.value.getName(), this.x - 7, this.y + 2, (Boolean)((BooleanValue)this.value).getValue() != false ? new Color(255, 255, 255).getRGB() : new Color(108, 108, 108).getRGB());
            }
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            if (this.value instanceof ModeValue) {
                FontLoaders.kiona14.drawString(this.value.getName(), this.x - 7, this.y + 3, new Color(255, 255, 255).getRGB());
                FontLoaders.kiona14.drawString(this.name, this.x + 77 - FontLoaders.kiona14.getStringWidth(this.name), this.y + 3, new Color(182, 182, 182).getRGB());
            }
            if (this.value instanceof NumberValue) {
                NumberValue v = (NumberValue)this.value;
                double render = 82.0f * (((Number)v.getValue()).floatValue() - ((Number)v.getMin()).floatValue()) / (((Number)v.getMax()).floatValue() - ((Number)v.getMin()).floatValue());
                Gui.drawRect((int)(this.x - 8), (int)(this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) + 2), (int)(this.x + 78), (int)(this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) - 9), (int)new Color(50, 50, 50, 180).getRGB());
                Gui.drawRect((int)(this.x - 8), (int)(this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) + 2), (int)((int)((double)(this.x - 4) + render)), (int)(this.y + FontLoaders.kiona14.getStringHeight(this.value.getName()) - 9), (int)staticColor);
            }
            if (this.value instanceof NumberValue) {
                FontLoaders.kiona14.drawString(this.value.getName(), this.x - 7, this.y, new Color(255, 255, 255).getRGB());
                FontLoaders.kiona14.drawString(this.name, this.x + FontLoaders.kiona14.getStringWidth(this.value.getName()), this.y, -1);
            }
            GL11.glDisable((int)3089);
        }
    }

    public void key(char typedChar, int keyCode) {
    }

    private boolean isHovering(int n, int n2) {
        boolean b = n >= this.x && n <= this.x - 7 && n2 >= this.y && n2 <= this.y + FontLoaders.kiona18.getStringHeight(this.value.getName());
        return b;
    }

    int press = 0;
    public void click(int mouseX, int mouseY, int button) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontLoaders.kiona18.getStringHeight(this.value.getName())) {
            if (this.value instanceof BooleanValue) {
                BooleanValue v = (BooleanValue)this.value;
                v.setValue((Boolean)(v = (BooleanValue)this.value).getValue() == false);
                return;
            }
            if (this.value instanceof ModeValue) {
                press++;

                ModeValue m = (ModeValue)this.value;

                ArrayList<Mode> modes = new ArrayList<Mode>();
                for (Mode mode : m.getModes()) {
                    modes.add(mode);

                }
                String t1 = null;
                if (press <= modes.size()) {
                    ////System.out.println(press);
                    modes.get(press - 1).setToggled(true);
                    t1 = modes.get(press - 1).getName();
                } else {
                    press = 0;
                }

                for (Mode mode : m.getModes()) {
                    if (mode.getName() != t1) {
                        mode.setToggled(false);

                    }

                }
            }
        }
    }

    private class mode {
    }
}

