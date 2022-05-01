package it.fktcod.ktykshrk.managers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import it.fktcod.ktykshrk.module.mods.AutoL;
import it.fktcod.ktykshrk.module.mods.ChestStealer;
import it.fktcod.ktykshrk.module.mods.InvCleaner;
import it.fktcod.ktykshrk.irc.IRCChat;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.*;
import it.fktcod.ktykshrk.module.mods.addon.BlocksmcAddon;
import it.fktcod.ktykshrk.module.mods.addon.MemoryFix;
import it.fktcod.ktykshrk.module.mods.addon.MinelandAddon;
import it.fktcod.ktykshrk.module.mods.addon.RedeskyAddon;
import it.fktcod.ktykshrk.ui.clickgui.click.ClickGuiScreen;
import it.fktcod.ktykshrk.ui.clickgui.click.theme.dark.DarkTheme;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.Value;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.*;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HackManager {

    private static Module toggleHack = null;
    public static ArrayList<Module> enabled_hacks;
    public static ArrayList<Module> modules;
    private GuiManager guiManager;
    private it.fktcod.ktykshrk.ui.clickgui.click.ClickGuiScreen guiScreen;
    public static Map<Module, Object> pluginModsList = new HashMap<>();
    public static Map<Module, Object> disabledPluginList = new HashMap<>();


    public void sortModules() {
        modules.sort((m1, m2) -> {
            if (m1.getName().toCharArray()[0] > m2.getName().toCharArray()[0]) {
                return 1;
            }
            return -1;
        });

    }

    public void addPluginModule(Module mod, Object plugin) {
        pluginModsList.put(mod, plugin);
        addHack(mod);
    }

    public HackManager() {
        modules = new ArrayList<Module>();
        addHack(new Targets());
        addHack(new Enemys());
        addHack(new Teams());
        addHack(new NoGuiEvents());
        addHack(new Trajectories());
        addHack(new EntityESP());
        addHack(new ItemESP());
        addHack(new AutoTool());
        addHack(new WTAP());
        addHack(new Disablers());
        addHack(new Tracers());
        addHack(new WallHack());
        addHack(new Fly());
        addHack(new NightVision());
        addHack(new Profiler());
        addHack(new AntiBot());
        addHack(new AimBot());
        addHack(new BowAimBot());
        addHack(new AutoClicker());
        addHack(new Criticals());
        addHack(new KillAura());
        addHack(new Velocity());
        addHack(new AutoSprint());
        addHack(new PingSpoof());
        addHack(new InteractClick());
        addHack(new Glide());
        addHack(new Nuker());
        addHack(new SlyPort());
        addHack(new NoFall());
        addHack(new Ghost());
        addHack(new Blink());
        addHack(new Scaffold());
        addHack(new FastLadder());
        addHack(new Speed());
        addHack(new AutoStep());
        addHack(new AntiSneak());
        addHack(new FastPlace());
        addHack(new SpeedMine());
        addHack(new BlockOverlay());
        addHack(new PluginsGetter());
        addHack(new Helper());
        addHack(new Derp());
        addHack(new TpBed());
        addHack(new Teleport());
        addHack(new FireballReturn());
        addHack(new SkinStealer());
        addHack(new InvMove());
        addHack(new PlayerRadar());
        addHack(new SkinChanger());
        addHack(new Parkour());
        addHack(new AntiRain());
        addHack(new AntiWeb());
        addHack(new Spider());
        addHack(new FastBow());
        addHack(new AutoWalk());
        addHack(new AutoSwim());
        addHack(new AutoShield());
        addHack(new Rage());
        addHack(new Suicide());
        addHack(new SelfDamage());
        addHack(new AntiAfk());
        addHack(new TestHack());
        addHack(new Disconnect());
        addHack(new ComboMode());
        addHack(new SelfKick());
        addHack(new PacketFilter());
        addHack(new HUD());
        addHack(new ClickGui());
        addHack(new CommandFrame());
        addHack(new CommandGetter());
        addHack(new TpAura());
        addHack(new FuckBed());
        addHack(new HighJump());
        addHack(new LongJump());
        addHack(new NoSlow());
        addHack(new NoSwing());
        addHack(new NoHurtCam());
        addHack(new SafeWalk());
        addHack(new ItemPhysic());
        addHack(new AntiVoid());
        addHack(new Animation());
        addHack(new AutoArmor());
        addHack(new FastUse());
        addHack(new Search());
        addHack(new KillerMark());
        addHack(new IRCChat());
        addHack(new Reach());
        addHack(new ChestStealer());
        addHack(new Hitbox());
        addHack(new Phase());
        addHack(new EnseChat());
        addHack(new AutoGA());
        addHack(new RedeskyAddon());
        addHack(new AutoL());
        // addHack(new InvCleaner());
        addHack(new AutoSay());
        addHack(new Timer());
        addHack(new Regen());
        addHack(new AntiPacketKick());
        addHack(new ChestESP());
        addHack(new FPS());
        addHack(new TargetStrafe());
        addHack(new ChatRect());
        //addHack(new HUDFIX());
        addHack(new InvCleaner());
        addHack(new KillEffect());
        addHack(new MegaKnockback());
        addHack(new ChineseMode());
        //addHack(new DDOS());
        addHack(new Spammer());
        //addHack(new JigFly());
        //addHack(new Diana());
        addHack(new AutoSoup());
        addHack(new AutoSword());
        addHack(new MemoryFix());
        addHack(new MinelandAddon());
        addHack(new BlocksmcAddon());
        addHack(new KeepSprint());
        addHack(new Hotbar());
        addHack(new AutoReport());
        addHack(new ServerCrasher());
        addHack(new SuperKB());
        addHack(new SelfDestruct());
        addHack(new Trigger());
    }

    public void setGuiManager(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    public ClickGuiScreen getGui() {
        if (this.guiManager == null) {
            this.guiManager = new GuiManager();
            this.guiScreen = new ClickGuiScreen();
            ClickGuiScreen.clickGui = this.guiManager;
            this.guiManager.Init();
            this.guiManager.setTheme(new DarkTheme());
        }
        return this.guiManager;
    }

    public static Module getHack(String name) {
        Module module = null;
        for (Module h : getHacks()) {
            if (h.getEName().equalsIgnoreCase(name)) {
                module = h;
            }
        }
        return module;
    }

    public static List<Module> getToggledHacks() {
        final List<Module> list = new ArrayList<Module>();
        for (final Module module : getHacks()) {
            if (module.isToggled()) {
                if (!module.isShow()) {
                    continue;
                }
                list.add(module);
            }
        }
        return list;
    }

    public static List<Module> getSortedHacks() {
        final List<Module> list = new ArrayList<Module>();
        for (final Module module : getHacks()) {
            if (module.isToggled()) {
                if (!module.isShow()) {
                    continue;
                }
                list.add(module);
            }
        }
        list.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module h1, final Module h2) {
                String s1 = h1.getName();
                String s2 = h2.getName();
                for (Value value : h1.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (!modeValue.getModeName().equals("Priority")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    s1 = s1 + " " + mode.getName();
                                }
                            }
                        }
                    }
                }
                for (Value value : h2.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (!modeValue.getModeName().equals("Priority")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    s2 = s2 + " " + mode.getName();
                                }
                            }
                        }
                    }
                }
                final int cmp = (int) (Core.fontManager.getFont("SFB 6").getWidth(s2)
                        - Core.fontManager.getFont("SFB 6").getWidth(s1));
                return (cmp != 0) ? cmp : s2.compareTo(s1);
            }
        });
        return list;
    }

    public static List<Module> getSortedHacks2() {
        final List<Module> list = new ArrayList<Module>();
        for (final Module module : getHacks()) {
            if (module.isToggled()) {
                if (!module.isShow()) {
                    continue;
                }
                list.add(module);
            }
        }
        list.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module h1, final Module h2) {
                String s1 = h1.getName();
                String s2 = h2.getName();
                for (Value value : h1.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (modeValue.getModeName().equals("Mode")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    s1 = s1 + " " + mode.getName();
                                }
                            }
                        }
                    }
                }
                for (Value value : h2.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (modeValue.getModeName().equals("Mode")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    s2 = s2 + " " + mode.getName();
                                }
                            }
                        }
                    }
                }
                if (HUD.font.getValue()) {
                    final int cmp = (int) (HUD.sigmaFont.getWidth(s2) - HUD.sigmaFont.getWidth(s1));
                    return (cmp != 0) ? cmp : s2.compareTo(s1);
                } else {

                    final int cmp = (int) (HUD.vanillaFont.getStringWidth(s2) - HUD.vanillaFont.getStringWidth(s1));
                    return (cmp != 0) ? cmp : s2.compareTo(s1);
                }
            }
        });
        return list;
    }


    public static List<Module> getSortedHacks3() {
        final List<Module> list = new ArrayList<Module>();
        for (final Module module : getHacks()) {
            if (module.isToggled()) {
                if (!module.isShow()) {
                    continue;
                }
                list.add(module);
            }
        }
        list.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module h1, final Module h2) {
                String s1 = h1.getName();
                String s2 = h2.getName();
                for (Value value : h1.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (modeValue.getModeName().equals("Mode")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    s1 = s1 + " " + mode.getName();
                                }
                            }
                        }
                    }
                }
                for (Value value : h2.getValues()) {
                    if (value instanceof ModeValue) {
                        ModeValue modeValue = (ModeValue) value;
                        if (modeValue.getModeName().equals("Mode")) {
                            for (Mode mode : modeValue.getModes()) {
                                if (mode.isToggled()) {
                                    s2 = s2 + " " + mode.getName();
                                }
                            }
                        }
                    }
                }

                final int cmp = (int) (HUD.rfont.getWidth(s2) - HUD.rfont.getWidth(s1));
                return (cmp != 0) ? cmp : s2.compareTo(s1);
            }
        });
        return list;
    }


    public static void addHack(Module module) {
        modules.add(module);
    }

    public static ArrayList<Module> getHacks() {
        return modules;
    }

    public static Module getToggleHack() {
        return toggleHack;
    }

    public static void onKeyPressed(int key) {
        if (Wrapper.INSTANCE.mc().currentScreen != null) {
            return;
        }
        for (Module module : getHacks()) {
            if (module.getKey() == key) {
                module.toggle();
                /*
                 * Wrapper.INSTANCE.player().playSound(SoundSourceEvent., 0.15F,
                 * hack.isToggled() ? 0.7F : 0.6F);
                 */
                toggleHack = module;

            }
        }
    }

    public static void onGuiContainer(GuiContainer event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onGuiContainer(event);
            }
        }
    }

    public static void onGuiOpen(GuiOpenEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onGuiOpen(event);
            }
        }
    }

    public static void onMouse(MouseEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onMouse(event);
            }
        }
    }

    public static void onLeftClickBlock(BlockEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onLeftClickBlock(event);
            }
        }
    }

    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onCameraSetup(event);
            }
        }
    }

    public static void onAttackEntity(AttackEntityEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onAttackEntity(event);
            }
        }
    }

    /*
     * public static void onProjectileImpact(ProjectileImpactEvent event) { for(Hack
     * hack : getHacks()) { if(hack.isToggled()) { hack.onProjectileImpact(event); }
     * } }
     */

    public static void onItemPickup(EntityItemPickupEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onItemPickup(event);
            }
        }
    }

    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onPlayerTick(event);
            }
        }
    }

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onClientTick(event);
            }
        }
    }

    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onRenderTick(event);
            }
        }
    }

    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onLivingUpdate(event);
            }
        }
    }

    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onRenderWorldLast(event);
            }
        }
    }

    public static void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onRenderGameOverlay(event);
            }
        }
    }

    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onRenderGameOverlay(event);
            }
        }
    }

    public static void onInputUpdate(MovementInput event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onInputUpdate(event);
            }
        }
    }

    public static void onPlayerEventPre(EventPlayerPre event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onPlayerEventPre(event);
            }
        }
    }

    public static void onPlayerEventPost(EventPlayerPost event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onPlayerEventPost(event);
            }
        }
    }

    public static void onSlowDownEvent(EventSlowDown event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onSlowDownEvent(event);
            }
        }
    }


    public static List<Module> getModulesInType(HackCategory t) {
        ArrayList<Module> output = new ArrayList<Module>();
        ArrayList<Module> module = new ArrayList<>();
        module.addAll(modules);
        for (Module m : modules) {
            if (m.getCategory() != t)
                continue;
            output.add(m);
        }
        return output;
    }

    public static void onMotionUpdate(EventMotion event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onMotionUpdate(event);
            }
        }
    }


    public static void onPacketEvent(EventPacket event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onPacketEvent(event);
            }
        }
    }

    /*
     * public static void onRender2D(EventRender2D event) { for (Hack hack :
     * getHacks()) { if (hack.isToggled()) { hack.onRender2D(event); } } }
     *
     * public static void onRender3D(EventRender3D event) { for (Hack hack :
     * getHacks()) { if (hack.isToggled()) { hack.onRender3D(event); } } }
     */

    public static void onMove(EventMove event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onMove(event);
            }
        }
    }

    public static void onRender3D(RenderBlockOverlayEvent event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onRender3D(event);
            }
        }
    }

    public static void onLoop(EventLoop event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onLoop(event);
            }
        }
    }

    public static void onEventStep(EventStep event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onEventStep(event);
            }
        }
    }

    public static void onWorld(EventWorld event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onWorld(event);
            }
        }
    }

    public static void onJump(EventJump event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onJump(event);
            }
        }
    }

    public static void onBlockBB(EventBlockBB event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onBlockBB(event);
            }
        }
    }

    public static void onRenderBlock(EventRenderBlock event) {
        for (Module module : getHacks()) {
            if (module.isToggled()) {
                module.onBlockRender(event);
            }
        }
    }
}
