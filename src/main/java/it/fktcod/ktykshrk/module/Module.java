package it.fktcod.ktykshrk.module;

import java.util.ArrayList;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventBlockBB;
import it.fktcod.ktykshrk.event.EventJump;
import it.fktcod.ktykshrk.event.EventLoop;
import it.fktcod.ktykshrk.event.EventMotion;
import it.fktcod.ktykshrk.event.EventMove;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.event.EventPlayerPost;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.event.EventRenderBlock;
import it.fktcod.ktykshrk.event.EventSlowDown;
import it.fktcod.ktykshrk.event.EventStep;
import it.fktcod.ktykshrk.event.EventWorld;
import it.fktcod.ktykshrk.eventapi.EventManager;
import it.fktcod.ktykshrk.module.mods.ChineseMode;
import it.fktcod.ktykshrk.module.mods.SelfDestruct;
import it.fktcod.ktykshrk.utils.Mappings;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.Translate;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.*;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Module {

    private String name;
    private String Chinese;
    private HackCategory category;
    private boolean toggled;
    private boolean isMixin;
    private boolean show;
    public static Minecraft mc = Minecraft.getMinecraft();
    private int key;
    public static boolean doAnimate1 = false;
    public static boolean doAnimate2 = false;
    private ArrayList<Value> values = new ArrayList<Value>();
    int enable = 0;
    public String rendername;
    FontRenderer fontRenderer;
    private int slide = 0;

    Translate translate;

    public Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});


    public Module(String name, HackCategory category) {
        this.name = name;
        this.category = category;
        this.toggled = false;
        this.show = true;
        this.key = -1;
        this.rendername = "";
        this.translate = new Translate(0, 0);
    }

    public void setChinese(String CN) {
        ////System.out.println(CN);
        this.Chinese = CN;
    }

    public void setMixin() {
        this.isMixin = true;
    }

    public void addValue(Value... values) {
        for (Value value : values) {
            this.getValues().add(value);
        }
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public Value getValueByName(String Name) {
        for (int x1 = 0; x1 < this.getValues().size(); x1++) {
            Value value = this.getValues().get(x1);
            //System.out.println(value.getName());
            if (value.getEName().contains(Name)) {
                //System.out.println("Found");
                return value;
            }
        }
        return null;
    }

    public boolean isToggledMode(String modeName) {
        for (Value value : this.values) {
            if (value instanceof ModeValue) {
                ModeValue modeValue = (ModeValue) value;
                for (Mode mode : modeValue.getModes()) {
                    if (mode.getName().equalsIgnoreCase(modeName)) {
                        if (mode.isToggled()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isToggledValue(String valueName) {
        for (Value value : this.values) {
            if (value instanceof BooleanValue) {
                BooleanValue booleanValue = (BooleanValue) value;
                if (booleanValue.getName().equalsIgnoreCase(valueName)) {
                    if (booleanValue.getValue()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setValues(ArrayList<Value> values) {
        for (Value value : values) {
            for (Value value1 : this.values) {
                if (value.getName().equalsIgnoreCase(value1.getName())) {
                    value1.setValue(value.getValue());
                }
            }
        }
    }

    public void toggle() {

        if (SelfDestruct.isDes){
            return;
        }

        this.toggled = !this.toggled;
        //this.doAnimate=true;
        if (this.toggled) {
            this.doAnimate1 = true;
            this.onEnable();
            EventManager.register(this);
        } else {
            this.doAnimate2 = true;
            this.onDisable();
            EventManager.unregister(this);
        }
        //RenderUtils.splashTickPos = 0;
        /*
         * NotificationManager.splashTickPos = 0; if (!NotificationManager.isDrawing &&
         * !(Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen)) {
         * NotificationManager.isDrawing = true; } if (!RenderUtils.isSplash &&
         * !(Wrapper.INSTANCE.mc().currentScreen instanceof ClientClickGui2)) {
         * RenderUtils.isSplash = true; }
         */
    }

    //"\u00a7a"
    public void onEnable() {
        if (SelfDestruct.isDes){
            return;
        }
        Core.notificationManager.add(new Notification(this.getEName() + "\u00a7a" + " - Enabled", Notification.Type.Info));
        mc.thePlayer.playSound("random.click", 0.2F, 0.6F);
        //Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 1.0F);
        // Main.notificationManager.addInfo( this.getName()+"\u00a7a"+" - Enabled");
    }

    public void onDisable() {
        Core.notificationManager.add(new Notification(this.getEName() + "\u00a7c" + " - Disabled", Notification.Type.Info));
        mc.thePlayer.playSound("random.click", 0.2F, 0.5F);
        //Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 0.5F);
        // Main.notificationManager.addInfo( this.getName()+"\u00a7c"+" - Disabled");
    }

    public void onGuiContainer(GuiContainer event) {
    }

    public void onGuiOpen(GuiOpenEvent event) {
    }

    public void onMouse(MouseEvent event) {
    }

    public boolean onPacket(Object packet, Side side) {
        return true;
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    }

    public void onClientTick(TickEvent.ClientTickEvent event) {
    }

    public void onRenderTick(TickEvent.RenderTickEvent event) {
    }

    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
    }

    public void onAttackEntity(AttackEntityEvent event) {
    }

    public void onItemPickup(EntityItemPickupEvent event) {
    }

    // public void onProjectileImpact(ProjectileImpactEvent event) {}
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
    }

    public void onRenderWorldLast(RenderWorldLastEvent event) {
    }

    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
    }

    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
    }

    public void onLeftClickBlock(BlockEvent event) {
    }

    public void onInputUpdate(MovementInput event) {
    }

    public void onPlayerEventPre(EventPlayerPre event) {
    }

    public void onPlayerEventPost(EventPlayerPost event) {
    }

    public void onSlowDownEvent(EventSlowDown event) {
    }

    /*
     * public void onEventUpdate(EventUpdate event) { }
     */

    public void onEventStep(EventStep event) {

    }

    public void onBlockRender(EventRenderBlock event) {

    }

    public void d(Object o) {
        String str = "[DEBUG]: " + o;
        ChatUtils.warning(str);
        ////System.out.println(str);
    }

    public void d() {
        String str = "[DEBUG]: !";
        ChatUtils.warning(str);
        ////System.out.println(str);
    }

    public String getDescription() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getEName() {
        return name;
    }

    public String getCName() {
        return ChineseMode.isCN ? Chinese != null ? Chinese : getEName() : getEName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public HackCategory getCategory() {
        return category;
    }

    public void setCategory(HackCategory category) {
        this.category = category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getEnableTime() {
        return enable;
    }

    public void setEnableTime(int x) {
        this.enable = x;
    }

    public void setRenderName(String s) {
        this.rendername = s;
    }

    public String getRenderName() {
        return this.rendername;
    }

    public void onMotionUpdate(EventMotion event) {

    }

    /*
     * public void onUpdate(EventUpdate event) {
     *
     * }
     */

    public void onPacketEvent(EventPacket event) {

    }

    public void onLoop(EventLoop event) {

    }

    public void onWorld(EventWorld event) {

    }

    public void onJump(EventJump event) {

    }

    public void onBlockBB(EventBlockBB event) {

    }

    /*
     * public void onRender2D(EventRender2D event) {
     *
     * }
     *
     * public void onRender3D(EventRender3D event) {
     *
     * }
     */

    public void onMove(EventMove event) {

    }

    public void onRender3D(RenderBlockOverlayEvent event) {

    }

    public int getSlide() {
        return slide;
    }

    public void setSlide(int slide) {
        this.slide = slide;
    }

    public Translate getTranslate() {
        return this.translate;
    }

    public void setTranslate(int x, int y, int speed) {
        this.translate.interpolate(x, y, speed);
    }


}
