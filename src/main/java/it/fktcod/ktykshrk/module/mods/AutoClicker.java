/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.MouseEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$RenderTickEvent
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package it.fktcod.ktykshrk.module.mods;


import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AutoClicker
        extends Module {

    //M1

    public static boolean isClicking = false;

    public boolean isDone = true;

    public int timer;

    TimerUtils left = new TimerUtils();
    TimerUtils right = new TimerUtils();

    //
    private Random random = new Random();

    private long lastClick;
    private long hold;


    private double speed;
    private double holdLength;
    private double min;
    private double max;
    private boolean hasSelectedBlock;

    private static Field FmouseButton;
    private static Field FmouseButtons;
    private static Field FmouseButtonState;

    private NumberValue maxcps;
    private NumberValue mincps;

    private NumberValue jitteryaw;
    private NumberValue jitterpitch;
    //private BooleanValue reflect = new BooleanValue("Reflect", false);
    private BooleanValue jitter;
    private BooleanValue weapon;
    private BooleanValue BlockHit;
    private BooleanValue BreakBlocks;
    private BooleanValue cpsdBypass;

    private ModeValue modeValue;


    public AutoClicker() {
        super("AutoClicker", HackCategory.COMBAT);

        hasSelectedBlock = false;

        modeValue = new ModeValue("Mode", new Mode("Basic", false), new Mode("M1", true));
        maxcps = new NumberValue("MaxCPS", 12.0D, 1.0D, 20.0D);
        mincps = new NumberValue("MinCPS", 6.0D, 1.0D, 20.0D);

        jitteryaw = new NumberValue("Jitter Yaw", 0.5D, 0.1D, 3.0D);
        jitterpitch = new NumberValue("Jitter Pitch", 0.5D, 0.1D, 3.0D);

        jitter = new BooleanValue("Jitter", false);
        weapon = new BooleanValue("Weapon", false);

        BlockHit = new BooleanValue("Block Hit", false);
        BreakBlocks = new BooleanValue("Break Blocks", false);
        cpsdBypass = new BooleanValue("Cps Bypass", false);

        this.addValue(this.maxcps);
        this.addValue(this.mincps);
        this.addValue(this.jitteryaw);
        this.addValue(this.jitterpitch);
        this.addValue(this.jitter);
        this.addValue(this.weapon);
        this.addValue(this.BlockHit);
        this.addValue(this.BreakBlocks);
        this.addValue(this.cpsdBypass);
        this.addValue(modeValue);
        this.setChinese(Core.Translate_CN[11]);
    }

    @Override
    public String getDescription() {
        return "Automatically clicks for you while holding down the attack button.";
    }

    public boolean check(EntityPlayerSP playerSP) {
        return (!this.weapon.getValue() || playerSP.getCurrentEquippedItem() != null) && (playerSP.getCurrentEquippedItem().getItem() instanceof ItemSword || playerSP.getCurrentEquippedItem().getItem() instanceof ItemAxe)&&Wrapper.INSTANCE.mc().currentScreen==null;
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (modeValue.getMode("Basic").isToggled()) {
            click();
        } else if (modeValue.getMode("M1").isToggled()) {
            m1click();
        }
        super.onClientTick(event);
    }

    @Override
    public void onDisable() {
        isDone=true;
        super.onDisable();
    }

    @Override
    public void onMouse(MouseEvent event) {
        if(modeValue.getMode("M1").isToggled()){
            ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
            if (stack != null && this.BlockHit.getValue()) {
                if (stack.getItem() instanceof ItemSword && !mc.thePlayer.isUsingItem()) {
                    if (!isDone || this.timer > 0)
                        return;
                    isDone = false;
                }
            }

        }

        super.onMouse(event);
    }

    public void m1click() {
        isClicking = false;
        if(!check(Wrapper.INSTANCE.player())){
            return;
        }

        if (Mouse.isButtonDown(0) && !mc.thePlayer.isUsingItem()) {
            if (this.left.hasReached(1000 / (float) this.getDelay())) {

                if (this.jitter.getValue()) {
                    jitter(this.random);
                }



                clickMouse();

                isClicking = true;
                left.reset();
            }
        }

        if (!isDone) {
            switch (this.timer) {
                case 0: {
                    KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(),false);
                    break;
                }
                case 1:
                case 2: {
                    KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(),true);
                    break;
                }
                case 3: {
                    KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(),false);
                    isDone = true;
                    this.timer = -1;
                }
            }
            ++this.timer;
        }
    }

    public void jitter(Random rand) {
        if (rand.nextBoolean()) {
            if (rand.nextBoolean()) {
                mc.thePlayer.rotationPitch -= (float) (rand.nextFloat() * 0.6);
            } else {
                mc.thePlayer.rotationPitch += (float) (rand.nextFloat() * 0.6);
            }
        } else if (rand.nextBoolean()) {
            mc.thePlayer.rotationYaw -= (float) (rand.nextFloat() * 0.6);
        } else {
            mc.thePlayer.rotationYaw += (float) (rand.nextFloat() * 0.6);
        }
    }

    private long getDelay() {
        return (long) (this.maxcps.getValue().intValue() + this.random.nextDouble()
                * (this.mincps.getValue().intValue() - this.maxcps.getValue().intValue()));
    }

    public void click() {
        if (this.mc.currentScreen == null && this.check(this.mc.thePlayer)) {
            if (BreakBlocks.getValue() && getIfSelectingBlock(hasSelectedBlock)) {
                return;
            }
            if (Mouse.isButtonDown(0)) {
                if (this.jitter.getValue() && this.random.nextDouble() > 0.65D) {

                    float jittery = (float) (this.jitteryaw.getValue() * 0.5D);
                    float jitterp = (float) (this.jitterpitch.getValue() * 0.5D);

                    EntityPlayerSP var10000;
                    if (this.random.nextBoolean()) {
                        var10000 = this.mc.thePlayer;
                        var10000.rotationYaw += this.random.nextFloat() * jittery;
                    } else {
                        var10000 = this.mc.thePlayer;
                        var10000.rotationYaw -= this.random.nextFloat() * jittery;
                    }

                    if (this.random.nextBoolean()) {
                        var10000 = this.mc.thePlayer;
                        var10000.rotationPitch += (float) ((double) this.random.nextFloat() * (double) jitterp * 0.75D);
                    } else {
                        var10000 = this.mc.thePlayer;
                        var10000.rotationPitch -= (float) ((double) this.random.nextFloat() * (double) jitterp * 0.75D);
                    }
                }
                if (System.currentTimeMillis() - lastClick > speed * 500) {
                    lastClick = System.currentTimeMillis();
                    if (hold < lastClick) {
                        hold = lastClick;
                    }

                    //bhit
                    int ucode = mc.gameSettings.keyBindUseItem.getKeyCode();

                    if (BlockHit.getValue() && Mouse.isButtonDown(1)) {
                        KeyBinding.setKeyBindState(ucode, false);
                    }

                    int key = mc.gameSettings.keyBindAttack.getKeyCode();
                    if (canByp()) {
                        setMouseButtonState(0, true);
                    }
                    KeyBinding.setKeyBindState(key, true);
                    KeyBinding.onTick(key);
                    this.updateVals();


                } else if (System.currentTimeMillis() - hold > holdLength * 500) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);

                    if (canByp()) {
                        setMouseButtonState(0, false);
                    }

                    this.updateVals();

                    int ucode = mc.gameSettings.keyBindUseItem.getKeyCode();
                    if (BlockHit.getValue() && Mouse.isButtonDown(1)) {
                        KeyBinding.setKeyBindState(ucode, true);
                    }

                }
            }
        }
    }

    private boolean canByp() {
        if (FmouseButtonState == null) {
            return false;
        }
        if (FmouseButton == null) {
            return false;
        }
        if (FmouseButtons == null) {
            return false;
        }
        return cpsdBypass.getValue();
    }


    public static void setMouseButtonState(int mouseButton, boolean state) {
        MouseEvent e = new MouseEvent();
        FmouseButton.setAccessible(true);
        try {
            FmouseButton.set(e, mouseButton);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        FmouseButton.setAccessible(false);
        FmouseButtonState.setAccessible(true);
        try {
            FmouseButtonState.set(e, state);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        FmouseButtonState.setAccessible(false);
        MinecraftForge.EVENT_BUS.post((Event) e);
        try {
            FmouseButtons.setAccessible(true);
            ByteBuffer buffer = (ByteBuffer) FmouseButtons.get(null);
            FmouseButtons.setAccessible(false);
            buffer.put(mouseButton, (byte) (state ? 1 : 0));
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }


    private boolean getIfSelectingBlock(boolean state) {
        BlockPos bpos;
        World w;
        MovingObjectPosition objectPosition;
        EntityPlayerSP playerSP;
        if (Minecraft.getMinecraft().getRenderViewEntity() != null && (playerSP = mc.thePlayer) != null && playerSP instanceof EntityPlayerSP && (objectPosition = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(4.8, 1.0f)) != null && (w = mc.thePlayer.worldObj) != null && objectPosition.hitVec != null && (bpos = new BlockPos(objectPosition.hitVec.xCoord, objectPosition.hitVec.yCoord, objectPosition.hitVec.zCoord)) != null) {
            Material material = w.getBlockState(bpos).getBlock().getMaterial();
            if (w.getBlockState(bpos).getBlock() != null && material != null && objectPosition.typeOfHit != null && mc.objectMouseOver != null) {
                if (mc.objectMouseOver.entityHit != null) {
                    state = false;
                } else if (objectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                    state = true;
                } else if (objectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                    state = false;
                }
            }
        }
        return state;
    }


    @Override
    public void onEnable() {
        this.updateVals();
    }

    private void updateVals() {

        try {
            Field LF = Minecraft.class.getDeclaredField("leftClickCounter");
            LF.setAccessible(true);
            LF.set(Minecraft.getMinecraft(), 0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        min = mincps.getValue();
        max = maxcps.getValue();

        if (min >= max) {
            max = min + 1;
        }

        speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
        holdLength = speed / ThreadLocalRandom.current().nextDouble(min, max);

        //M1
        isDone=true;
        timer=0;
    }

    static {

        try {
            FmouseButton = MouseEvent.class.getDeclaredField("button");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            FmouseButtonState = MouseEvent.class.getDeclaredField("buttonstate");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            FmouseButtons = Mouse.class.getDeclaredField("buttons");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void clickMouse() {

        mc.thePlayer.swingItem();

        if (mc.objectMouseOver != null) {
            switch (mc.objectMouseOver.typeOfHit) {
                case ENTITY:
                    mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                    break;
                case BLOCK:
                    BlockPos blockpos = mc.objectMouseOver.getBlockPos();

                    if (mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                        mc.playerController.clickBlock(blockpos, mc.objectMouseOver.sideHit);
                        break;
                    }

                case MISS:
                default:

            }
        }

    }

}

