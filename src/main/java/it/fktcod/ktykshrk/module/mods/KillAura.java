package it.fktcod.ktykshrk.module.mods;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import ensemble.mixin.cc.mixin.interfaces.IC03Packet;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.event.EventPlayerPost;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.event.EventWorld;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.utils.Colors;
import it.fktcod.ktykshrk.utils.Location;
import it.fktcod.ktykshrk.utils.TranslateUtil;
import it.fktcod.ktykshrk.managers.FontManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.addon.Rotation;
import it.fktcod.ktykshrk.utils.EntityUtils;
import it.fktcod.ktykshrk.utils.MoveUtils;
import it.fktcod.ktykshrk.utils.PlayerControllerUtils;
import it.fktcod.ktykshrk.utils.RayCastUtils;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.system.A03A59A2;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.utils.visual.RotationUtil;
import it.fktcod.ktykshrk.utils.visual.font.render.TTFFontRenderer;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import static net.minecraft.util.MovingObjectPosition.MovingObjectType.ENTITY;

public class KillAura extends Module {

    public ModeValue priority;
    public ModeValue mode;
    public ModeValue rotations;
    public static BooleanValue walls;
    public BooleanValue autoDelay;
    public BooleanValue packetReach;
    public NumberValue minCPS;
    public NumberValue maxCPS;
    public NumberValue packetRange;
    public static NumberValue range;
    public static NumberValue FOV;
    public BooleanValue rotate;
    public static BooleanValue autoblockValue;
    public NumberValue blockrateValue;
    public NumberValue switchsize;
    TimerUtils switchTimer = new TimerUtils();
    public NumberValue switchDelay;
    public static BooleanValue randoms;
    public static BooleanValue randoms2;
    public static NumberValue rotationValue;
    public static NumberValue rotationValue2;
    public ModeValue blockmode;
    public BooleanValue hud;
    public ModeValue hudmode;
    public NumberValue minTurnSpeed;
    public NumberValue maxTurnSpeed;
    BooleanValue interact;
    BooleanValue autosword;
    BooleanValue packet;

    public int index;

    final TranslateUtil healthTranslate = new TranslateUtil(0, 0);
    final TranslateUtil armorTranslate = new TranslateUtil(0, 0);
    final TranslateUtil htTranslate = new TranslateUtil(0, 0);
    final TranslateUtil dmgTranslate = new TranslateUtil(0, 0);
    float lastHealth = 0;
    public float damageDelt = 0;
    public float lastPlayerHealth = -1;
    public float damageDeltToPlayer = 0;
    public int sendtime = 0;
    public double turn;

    public Rotation serverrotation = new Rotation(0, 0);
    public static Rotation targetRotation;

//Multi

    boolean send = false;
    static Random random = new Random();

    public TimerUtils timer;
    public static EntityLivingBase target;
    public static float[] facingCam = null;
    public float[] facing = null;
    Vec3 randomCenter = null;
    boolean phaseOne = false;
    boolean phaseTwo = false;
    boolean phaseThree = false;
    boolean shouldrotate = false;
    public static boolean blockstate;

    public static ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    public static ArrayList<EntityLivingBase> attacked = new ArrayList<EntityLivingBase>();

    // public static FontManager fontManager = new FontManager();

    public static float[] lastRotations;

    // render
    double delay = 0;

    // new mode is hanabi code
    public KillAura() {
        super("KillAura", HackCategory.COMBAT);

        this.priority = new ModeValue("Priority", new Mode("Closest", true), new Mode("Health", false),
                new Mode("Armor", false), new Mode("Fov", false), new Mode("Angle", false));
        this.mode = new ModeValue("Mode", new Mode("Simple", true), new Mode("AAC", false),
                new Mode("Multi", false), new Mode("Basic", false), new Mode("New", false), new Mode("ENSEB", false),new Mode("Legit",false));
        walls = new BooleanValue("ThroughWalls", false);
        autoDelay = new BooleanValue("AutoDelay", false);
        rotate = new BooleanValue("Rotate", false);
        packetReach = new BooleanValue("PacketReach", false);
        packetRange = new NumberValue("PacketRange", 10.0D, 1.0D, 100D);
        minCPS = new NumberValue("MinCPS", 4.0D, 1.0D, 30.0D);
        maxCPS = new NumberValue("MaxCPS", 8.0D, 1.0D, 30.0D);
        range = new NumberValue("Range", 3.4D, 1.0D, 7.0D);
        FOV = new NumberValue("FOV", 180D, 1.0D, 360D);
        autoblockValue = new BooleanValue("AutoBlock", false);
        blockrateValue = new NumberValue("BlockRate", 100D, 1D, 100D);
        switchsize = new NumberValue("SwitchSize", 1D, 1D, 5D);
        rotations = new ModeValue("RotaMode", new Mode("ROBasic", true), new Mode("RONull", false),
                new Mode("RONew", false), new Mode("Custom", false), new Mode("ROENSEB", false),
                new Mode("ROAAC", false), new Mode("Auto", false), new Mode("ROLegit", false));
        randoms = new BooleanValue("YawRandom", true);
        randoms2 = new BooleanValue("PitchRandom", true);
        rotationValue2 = new NumberValue("YawOffset", 200D, 0D, 400D);
        rotationValue = new NumberValue("PitchOffset", 200D, 0D, 400D);

        minTurnSpeed = new NumberValue("MinTurnSpeed", 15D, 5D, 360D);
        maxTurnSpeed = new NumberValue("MaxTurnSpeed", 18D, 5D, 360D);

        switchDelay = new NumberValue("SwitchDelay", 50D, 0D, 2000D);
        blockmode = new ModeValue("BlockMode", new Mode("BlockBasic", false), new Mode("BlockBasic2", true),
                new Mode("Hypixel", false), new Mode("NoPacket", false));
        hud = new BooleanValue("HUD", true);
        hudmode = new ModeValue("HudMode", new Mode("HUDBasic", false), new Mode("HUDNew", true),
                new Mode("Fancy", false), new Mode("HUD2", false), new Mode("HUD3", false), new Mode("HUD4", false), new Mode("HUD5", false));

        interact = new BooleanValue("Interact", true);
        autosword = new BooleanValue("AutoSword", true);
        packet = new BooleanValue("Packet", false);

        this.addValue(mode, priority, walls, autoDelay, packetReach, minCPS, maxCPS, packetRange, range, FOV, rotate,
                autoblockValue, blockrateValue, switchsize, switchDelay, rotations, randoms, randoms2, rotationValue,
                rotationValue2, blockmode, hud, hudmode, minTurnSpeed, maxTurnSpeed, interact, packet, autosword);

        this.timer = new TimerUtils();
        this.setChinese(Core.Translate_CN[58]);
    }

    @Override
    public String getDescription() {
        return "Attacks the entities around you.";
    }

    @Override
    public void onEnable() {

        nmsl(Core.iloveu);
        facingCam = null;
        if (HackManager.getHack("Scaffold").isToggled()) {
            HackManager.getHack("Scaffold").setToggled(false);
            ChatUtils.warning("Disable Scaffold!");
        }

        if (mode.getMode("AAC").isToggled()) {
            facing = null;
            randomCenter = null;
            phaseOne = false;
            phaseTwo = false;
            phaseThree = false;
        } else if (mode.getMode("ENSEB").isToggled()) {
            facing = null;
            randomCenter = null;
            phaseOne = false;
            phaseTwo = false;
            phaseThree = false;

            if (!rotations.getMode("ROENSEB").isToggled()) {
                for (Mode mode : rotations.getModes()) {
                    if (mode.getName().equals("ROENSEB")) {
                        mode.setToggled(true);

                    } else {
                        mode.setToggled(false);
                    }
                }

                ChatUtils.report("Matching mode has been automatically selected->ROENSEB");
            }

        }

        lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        attacked = new ArrayList<EntityLivingBase>();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.facingCam = null;
        this.target = null;
        if (blockstate) {
            unBlock();
        }

        this.blockstate = false;
        targets.clear();

        super.onDisable();
    }

    /*
     *
     * coded by zenwix 2020/11/7 rotation
     *
     *
     *
     */

    @Override
    public void onPlayerEventPre(EventPlayerPre event) {
        Utils.nullCheck();
        if (mode.getMode("New").isToggled()) {
            Hanabiupdate();
            targetRotation.setServerPitch(lastRotations[1]);
            if (rotate.getValue()) {
                if (target != null) {

                    event.setYaw(lastRotations[0]);
                    event.setPitch(lastRotations[1]);
                    Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
                    Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();

                }
                if (target != null) {
                    unBlock();
                    killAuraAttack(target);
                }

            }
        } else if (mode.getMode("ENSEB").isToggled()) {

            if (target == null) {
                event.setYaw(Wrapper.INSTANCE.player().rotationYaw);
                event.setPitch(Wrapper.INSTANCE.player().rotationPitch);
            }
            Hanabiupdate();
            BphaseOne();
            phaseTwo();
            BphaseThree(event);
            BphaseFour();

        }
        super.onPlayerEventPre(event);
    }

    @Override
    public void onWorld(EventWorld event) {
        this.setToggled(false);
        super.onWorld(event);
    }

    @Override
    public void onPlayerEventPost(EventPlayerPost event) {
        if (mode.getMode("New").isToggled() || mode.getMode("ENSEB").isToggled()) {

            if (target != null && autoblockValue.getValue()) {
                doBlock();
            }
        }
        super.onPlayerEventPost(event);
    }

    @Override
    public void onRenderGameOverlay(Text event) {
        if (Wrapper.INSTANCE.mc() == null && !Utils.nullCheck()) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        if (target != null && hud.getValue()) {
            if (hudmode.getMode("HUDBasic").isToggled()) {

                float health = target.getHealth();
                if (health > 20) {
                    health = 20;
                }
                int red = (int) Math.abs((((health * 5) * 0.01f) * 0) + ((1 - (((health * 5) * 0.01f))) * 255));
                int green = (int) Math.abs((((health * 5) * 0.01f) * 255) + ((1 - (((health * 5) * 0.01f))) * 0));
                Color customColor = new Color(red, green, 0).brighter();

                Wrapper.INSTANCE.fontRenderer().drawString("\2474\u2764\247f",
                        sr.getScaledWidth() / 2 - Wrapper.INSTANCE.fontRenderer().getStringWidth("\2474\u2764\247f"),
                        sr.getScaledHeight() / 2 - 15, customColor.getRGB());
                Core.fontManager.getFont("SFM 7").drawString("" + (int) target.getHealth(), sr.getScaledWidth() / 2,
                        sr.getScaledHeight() / 2 - 15, customColor.getRGB());
                Wrapper.INSTANCE.fontRenderer().drawString(target.getName(),
                        sr.getScaledWidth() / 2 - Wrapper.INSTANCE.fontRenderer().getStringWidth("\2474\u2764\247f"),
                        sr.getScaledHeight() / 2 - 24, 0xFFFFFFFF);
            } else if (hudmode.getMode("HUDNew").isToggled()) {
                newhud(sr);
            } else if (hudmode.getMode("Fancy").isToggled()) {
                fancyhud(sr);
            } else if (hudmode.getMode("HUD2").isToggled()) {
                hud2(sr);
            } else if (hudmode.getMode("HUD3").isToggled()) {
                hud3(sr);
            } else if (hudmode.getMode("HUD4").isToggled()) {
                hud4(sr);
            }
        }

        super.onRenderGameOverlay(event);
    }

    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (target == null) {
            return;
        }
        if (hudmode.getMode("HUD5").isToggled()) {
            this.renderTargetBox(target, event.partialTicks);
        } else {

            if (target.hurtTime > 0) {
                RenderUtils.renderTarget(event, target, 1.0f, 0.0f, 0.0f, 1, 1, target.height / 2);
                return;
            }
            RenderUtils.renderTarget(event, target, 1, 1, 1, 1, 1, target.height / 2);

        }

        super.onRenderWorldLast(event);


    }

    /*
     * @Override public void onPacketEvent(EventPacket event) { if (event.getType()
     * == EventType.SEND) { if (event.getPacket() instanceof C03PacketPlayer &&
     * mode.getMode("New").isToggled() && rotations.getMode("ROAAC").isToggled() &&
     * rotate.getValue()) { final C03PacketPlayer packetPlayer = (C03PacketPlayer)
     * event.getPacket();
     *
     * if (targetRotation != null && (targetRotation.getYaw() !=
     * serverrotation.getYaw() || targetRotation.getPitch() !=
     * serverrotation.getPitch())) {
     *
     *
     * Field field = ReflectionHelper.findField(C03PacketPlayer.class, new String[]
     * { "pitch", "field_149473_f" }); Field field2 =
     * ReflectionHelper.findField(C03PacketPlayer.class, new String[] { "yaw",
     * "field_149476_e" }); Field field3 =
     * ReflectionHelper.findField(C03PacketPlayer.class, new String[] { "rotating",
     * "field_149481_i" });
     *
     * try {
     *
     * if (!field.isAccessible()) { field.setAccessible(true); } if
     * (!field2.isAccessible()) { field2.setAccessible(true); } if
     * (!field3.isAccessible()) { field3.setAccessible(true); }
     *
     * field2.setFloat(packetPlayer, targetRotation.getYaw());
     * field.setFloat(packetPlayer, targetRotation.getPitch());
     * field3.setBoolean(packetPlayer, true);
     *
     * } catch (Exception e) { // TODO: handle exception } }
     *
     * if (packetPlayer.getRotating()) serverrotation = new
     * Rotation(packetPlayer.getYaw(), packetPlayer.getPitch());
     *
     * }
     *
     * } super.onPacketEvent(event); }
     */
    /*
     * @Override public boolean onPacket(Object packet, Side side) { if
     * (!(mode.getMode("Basic").isToggled() || mode.getMode("New").isToggled() ||
     * mode.getMode("ENSEB").isToggled())) {
     *
     * if (side == Side.OUT) {
     *
     * if (packet instanceof C03PacketPlayer) { final C03PacketPlayer p =
     * (C03PacketPlayer) packet;
     *
     * if (target != null) {
     *
     * Field field = ReflectionHelper.findField(C03PacketPlayer.class, new String[]
     * { "pitch", "field_149473_f" }); Field field2 =
     * ReflectionHelper.findField(C03PacketPlayer.class, new String[] { "yaw",
     * "field_149476_e" }); Field field3 =
     * ReflectionHelper.findField(C03PacketPlayer.class, new String[] { "rotating",
     * "field_149481_i" }); Field lastyaw =
     * ReflectionHelper.findField(EntityPlayerSP.class, new String[] {
     * "lastReportedYaw", "field_175164_bL" }); Field lastpitch =
     * ReflectionHelper.findField(EntityPlayerSP.class, new String[] {
     * "lastReportedPitch", "field_175165_bM" }); try {
     *
     * if (!field.isAccessible()) { field.setAccessible(true); } if
     * (!field2.isAccessible()) { field2.setAccessible(true); } if
     * (!field3.isAccessible()) { field3.setAccessible(true); } if
     * (!lastyaw.isAccessible()) { lastyaw.setAccessible(true); } if
     * (!lastpitch.isAccessible()) { lastpitch.setAccessible(true); } if
     * (rotate.getValue()) {
     *
     * if (this.getTarget() != null) {
     *
     * if (packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
     *
     * C05PacketPlayerLook p5 = (C05PacketPlayerLook) packet; PacketBuffer buf = new
     * PacketBuffer(Unpooled.buffer());
     * buf.writeFloat(Utils.getRotationsNeeded(target)[0]);
     * buf.writeFloat(Utils.getRotationsNeeded(target)[1]); p5.writePacketData(buf);
     * p5.readPacketData(buf); field.setFloat(p5,
     * Utils.getRotationsNeeded(target)[1]); field2.setFloat(p5,
     * Utils.getRotationsNeeded(target)[0]); field3.setBoolean(p5, true); } if
     * (shouldrotate) {
     *
     * field.setFloat(p, Utils.getRotationsNeeded(target)[1]); field2.setFloat(p,
     * Utils.getRotationsNeeded(target)[0]);
     *
     * lastyaw.setFloat(Wrapper.INSTANCE.player(),
     * Wrapper.INSTANCE.player().rotationYaw);
     * lastpitch.setFloat(Wrapper.INSTANCE.player(),
     * Wrapper.INSTANCE.player().rotationPitch); field3.setBoolean(p, true);
     * Wrapper.INSTANCE.player()
     * .setRotationYawHead(Utils.getRotationsNeeded(target)[0]);
     *
     * Wrapper.INSTANCE.player().renderYawOffset =
     * Utils.getRotationsNeeded(target)[0];
     *
     * shouldrotate = false; }
     *
     * } else { shouldrotate = false; } } ;
     *
     * } catch (Exception e) { ////System.out.println(e); } }
     *
     * } } } return true; }
     */
    public static double fovFromEntity(Entity en) {
        return ((double) (mc.thePlayer.rotationYaw - fovToEntity(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
    }

    public static float fovToEntity(Entity ent) {
        double x = ent.posX - mc.thePlayer.posX;
        double z = ent.posZ - mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 57.2957795D;
        return (float) (yaw * -1.0D);
    }

    @Override
    public void onAttackEntity(AttackEntityEvent event) {
        if (target != null && autosword.getValue()) {
            setSword();
        }

        super.onAttackEntity(event);
    }

    @Override
    public void onClientTick(ClientTickEvent event) {

        Utils.nullCheck();
        if (this.minCPS.getValue() > this.maxCPS.getValue()) {
            this.minCPS.setValue(this.maxCPS.getValue());
        }
        if (mode.getMode("Basic").isToggled()) {
            if(target==null){
              //  ChatUtils.message("ok");
                unBlock();
            }

            if (event.phase == Phase.START) {
                Hanabiupdate();
                if (target != null) {


                    if (rotate.getValue() && rotations.getMode("ROLegit").isToggled()) {
                        turn = RandomFloat(minTurnSpeed.getValue().floatValue(), maxTurnSpeed.getValue().floatValue());
                        mc.thePlayer.rotationYaw = AimUtil.getRotation(mc.thePlayer.rotationYaw, Utils.getRotationsNeeded(target)[0],
                                (float) (turn * 0.3f));
                        mc.thePlayer.rotationPitch = AimUtil.getRotation(mc.thePlayer.rotationPitch, Utils.getRotationsNeeded(target)[1],
                                (float) (turn * 0.3f));
                    }
                    unBlock();
                    killAuraAttack(target);
                }
            } else if (event.phase == Phase.END) {
                if (target != null && autoblockValue.getValue()) {
                    doBlock();
                }
            }


        } else if (mode.getMode("AAC").isToggled()) {
            phaseOne();
            phaseTwo();
            phaseFour();
        } else if (mode.getMode("Simple").isToggled()) {
            killAuraUpdate();
            // ChatUtils.message(Wrapper.INSTANCE.controller().getBlockReachDistance());
            if (target == null) {
                unBlock();
            }
            AutoShield.block(true);
            simpleattack(target);
            if (event.phase == Phase.END) {
                if (target != null && autoblockValue.getValue()) {
                    doBlock();
                }
            }
        } else if (mode.getMode("Multi").isToggled()) {
            MultiUpdate();
        }  else if (mode.getMode("New").isToggled()) {
            if (target == null) {
                unBlock();
            }

        } else if (mode.getMode("ENSEB").isToggled()) {
            if (target == null) {
                unBlock();
            }
        }else if (mode.getMode("Legit").isToggled()) {
            if (event.phase == Phase.START) {
                Hanabiupdate();
            } else if (event.phase == Phase.END) {
                if (target != null) {
                    turn = RandomFloat(minTurnSpeed.getValue().floatValue(), maxTurnSpeed.getValue().floatValue());
                    mc.thePlayer.rotationYaw = AimUtil.getRotation(mc.thePlayer.rotationYaw, Utils.getRotationsNeeded(target)[0],
                            (float) (turn * 0.3f));
                    mc.thePlayer.rotationPitch = AimUtil.getRotation(mc.thePlayer.rotationPitch, Utils.getRotationsNeeded(target)[1],
                            (float) (turn * 0.3f));
                    int CPS = Utils.random((int) (minCPS.getValue().intValue()), (int) (maxCPS.getValue().intValue()));
                    int r1 = Utils.random(1, 50), r2 = Utils.random(1, 60), r3 = Utils.random(1, 70);

                    if (timer.isDelay((1000 + ((r1 - r2) + r3)) / CPS)) {

                        mc.thePlayer.swingItem();

                        if (mc.objectMouseOver != null) {
                            if (mc.objectMouseOver.typeOfHit == ENTITY) {
                                unBlock();
                                mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                            }
                        }

                        timer.setLastMS();
                       // target = null;
                    }
                }
                if (target != null && autoblockValue.getValue()) {
                    doBlock();
                }

            }

        }
        super.onClientTick(event);
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {

        if (mode.getMode("AAC").isToggled()) {
            phaseThree(event);
        } else if (mode.getMode("Simple").isToggled()) {

        }
        super.onPlayerTick(event);
    }

    void phaseOne() {
        if (target != null) {
            randomCenter = Utils.getRandomCenter(target.getEntityBoundingBox());
            facing = Utils.getSmoothNeededRotations(randomCenter, 100.0F, 100.0F);
        }
        killAuraUpdate();
        if (target != null)
            phaseOne = true;
    }

    void BphaseOne() {
        if (target != null) {
            randomCenter = Utils.getRandomCenter(target.getEntityBoundingBox());
            facing = Utils.getSmoothNeededRotations(randomCenter, 100.0F, 100.0F);
        }
        HanabigetTarget();
        if (target != null)
            phaseOne = true;
    }

    void phaseTwo() {
        if (target == null || randomCenter == null || !phaseOne)
            return;
        if (facing[0] == Utils.getNeededRotations(randomCenter)[0]) {
            phaseOne = false;
            phaseTwo = true;
        }
    }

    void phaseThree(PlayerTickEvent event) {
        if (target == null || facing == null || event.player != Wrapper.INSTANCE.player())
            return;

        if (target.hurtTime <= target.maxHurtTime) {
            event.player.rotationYaw = facing[0];
            event.player.rotationPitch = facing[1];
            Wrapper.INSTANCE.player().rotationYawHead = facing[0];
        }
        if (!phaseTwo)
            return;

        event.player.rotationYaw = facing[0];
        event.player.rotationPitch = facing[1];
        Wrapper.INSTANCE.player().rotationYawHead = facing[0];
        phaseTwo = false;
        phaseThree = true;
    }

    void BphaseThree(EventPlayerPre event) {
        if (target == null || facing == null)
            return;

        if (target.hurtTime <= target.maxHurtTime) {
            event.setYaw(facing[0]);
            event.setPitch(facing[1]);
            Wrapper.INSTANCE.player().rotationYawHead = facing[0];
        }
        if (!phaseTwo)
            return;
        event.setYaw(facing[0]);
        event.setPitch(facing[1]);
        Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
        Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
        Wrapper.INSTANCE.player().rotationYawHead = facing[0];
        phaseTwo = false;
        phaseThree = true;
    }

    void phaseFour() {
        if (target == null || randomCenter == null || !phaseThree
                || facing[0] != Utils.getNeededRotations(randomCenter)[0]) {
            facingCam = null;
            return;
        }
        Entity rayCastEntity = RayCastUtils.rayCast((float) (range.getValue().floatValue() + 1.0F), facing[0],
                facing[1]);
        killAuraAttack2(rayCastEntity == null ? target : (EntityLivingBase) rayCastEntity);
    }

    void BphaseFour() {
        if (target == null || randomCenter == null || !phaseThree
                || facing[0] != Utils.getNeededRotations(randomCenter)[0]) {
            facingCam = null;
            return;
        }
        Entity rayCastEntity = RayCastUtils.rayCast((float) (range.getValue().floatValue() + 1.0F), facing[0],
                facing[1]);
        unBlock();

        // rayCastEntity == null ? target : (EntityLivingBase) rayCastEntity
        killAuraAttack(rayCastEntity == null ? target : (EntityLivingBase) rayCastEntity);
    }

    void killAuraUpdate() {

        for (Object object : Utils.getEntityList()) {
            if (!(object instanceof EntityLivingBase))
                continue;
            EntityLivingBase entity = (EntityLivingBase) object;
            if (!check(entity))
                continue;
            target = entity;
            shouldrotate = true;
        }

    }

    void MultiUpdate() {
        EntityPlayerSP player = Wrapper.INSTANCE.player();
        WorldClient world = Wrapper.INSTANCE.world();
        double rangeSq = Math.pow(((Double) this.range.getValue()).doubleValue(), 2.0D);
        Stream stream = world.loadedEntityList.parallelStream().filter((e) -> {
            return e instanceof EntityLivingBase;
        }).map((e) -> {
            return (EntityLivingBase) e;
        }).filter((e) -> {
            return !e.isDead && e.getHealth() > 0.0F;
        }).filter((e) -> {
            return EntityUtils.getDistanceSq(player, e) <= rangeSq;
        }).filter((e) -> {
            return e != player;
        }).filter((e) -> {
            return !(HackManager.getHack("AntiBot").isToggled() && ValidUtils.isBot(e));
        }).filter((e) -> {
            return !(HackManager.getHack("Teams").isToggled() && ValidUtils.isTeam(e));
        });

        Module h = HackManager.getHack("Targets");
        if (!h.isToggledValue("Players")) {
            stream = stream.filter((e) -> {
                return !(e instanceof EntityPlayer);
            });
        }

        if (!h.isToggledValue("Mobs")) {

            stream = stream.filter((e) -> {
                return !(e instanceof IMob) && !(e instanceof EntityAnimal);
            });
        }

        ArrayList entities = (ArrayList) stream.collect(Collectors.toCollection(() -> {
            return new ArrayList();
        }));
        if (!entities.isEmpty()) {
            int CPS = Utils.random(((Double) this.minCPS.getValue()).intValue(),
                    ((Double) this.maxCPS.getValue()).intValue());
            int r1 = Utils.random(1, 50);
            int r2 = Utils.random(1, 60);
            int r3 = Utils.random(1, 70);
            if (this.timer.hasReached((float) ((1000 + r1 - r2 + r3) / CPS))) {
                Iterator var12 = entities.iterator();

                while (var12.hasNext()) {
                    Entity entity = (Entity) var12.next();
                    Wrapper.INSTANCE.controller().attackEntity(player, entity);
                }

                player.swingItem();
                this.timer.reset();
            }

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

    public void killAuraAttack(EntityLivingBase entity) {
        if (entity == null) {
            AutoShield.block(false);
            blockstate = false;
            return;
        }
        if (target != null) {
            if (!attacked.contains(target) && target instanceof EntityPlayer) {
                attacked.add(target);
            }
        }
        int CPS = Utils.random((int) (minCPS.getValue().intValue()), (int) (maxCPS.getValue().intValue()));
        int r1 = Utils.random(1, 50), r2 = Utils.random(1, 60), r3 = Utils.random(1, 70);
        if (timer.isDelay((1000 + ((r1 - r2) + r3)) / CPS)) {
            processAttack(entity);
            timer.setLastMS();
            facingCam = null;
            // target = null;
            phaseThree = false;
        }

    }

    public void killAuraAttack2(EntityLivingBase entity) {
        if (entity == null) {
            AutoShield.block(false);
            blockstate = false;
            return;
        }
        if (target != null) {
            if (!attacked.contains(target) && target instanceof EntityPlayer) {
                attacked.add(target);
            }
        }
        int CPS = Utils.random((int) (minCPS.getValue().intValue()), (int) (maxCPS.getValue().intValue()));
        int r1 = Utils.random(1, 50), r2 = Utils.random(1, 60), r3 = Utils.random(1, 70);
        if (timer.isDelay((1000 + ((r1 - r2) + r3)) / CPS)) {
            processAttack(entity);
            timer.setLastMS();
            facingCam = null;
            target = null;
            phaseThree = false;
        }

    }

    public void simpleattack(EntityLivingBase entity) {
        if (entity == null) {
            AutoShield.block(false);
            blockstate = false;
            return;
        }
        if (target != null) {
            if (!attacked.contains(target) && target instanceof EntityPlayer) {
                attacked.add(target);
            }
        }
        int CPS = Utils.random((int) (minCPS.getValue().intValue()), (int) (maxCPS.getValue().intValue()));
        int r1 = Utils.random(1, 50), r2 = Utils.random(1, 60), r3 = Utils.random(1, 70);
        if (timer.isDelay((1000 + ((r1 - r2) + r3)) / CPS)) {
            processAttack(entity);
            timer.setLastMS();
            facingCam = null;
            target = null;
            phaseThree = false;
        }

    }

    public void BMultiAttack(EntityLivingBase entity) {
        if (entity == null) {
            AutoShield.block(false);
            blockstate = false;
            return;
        }

        int CPS = Utils.random((int) (minCPS.getValue().intValue()), (int) (maxCPS.getValue().intValue()));
        int r1 = Utils.random(1, 50), r2 = Utils.random(1, 60), r3 = Utils.random(1, 70);
        if (timer.isDelay((1000 + ((r1 - r2) + r3)) / CPS)) {
            processAttack(entity);
            timer.setLastMS();
            facingCam = null;
            phaseThree = false;
        }

    }

    private boolean hasSword() {
        return Wrapper.INSTANCE.player().inventory.getCurrentItem() != null
                && Wrapper.INSTANCE.player().inventory.getCurrentItem().getItem() instanceof ItemSword;
    }

    public void processAttack(EntityLivingBase entity) {
        AutoShield.block(false);

        if (this.hasSword()) {
            this.unBlock();
        }

        if (!isInAttackRange(entity) || !ValidUtils.isInAttackFOV(entity, (int) (FOV.getValue().intValue())))
            return;
        EntityPlayerSP player = Wrapper.INSTANCE.player();
        float sharpLevel = EnchantmentHelper.getModifierForCreature(player.getHeldItem(),
                entity.getCreatureAttribute());
        if (this.packetReach.getValue()) {
            double posX = entity.posX - 3.5 * Math.cos(Math.toRadians(Utils.getYaw(entity) + 90.0f));
            double posZ = entity.posZ - 3.5 * Math.sin(Math.toRadians(Utils.getYaw(entity) + 90.0f));
            Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(posX, entity.posY, posZ,
                    Utils.getYaw(entity), Utils.getPitch(entity), player.onGround));
            Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(entity, Action.ATTACK));
            Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY,
                    player.posZ, player.onGround));
        } else {
            if (autoDelay.getValue() || mode.getMode("Simple").isToggled())
                Utils.attack(entity);
            else
                Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(entity, Action.ATTACK));
        }
        Utils.swingMainHand();
        if (sharpLevel > 0.0f)
            player.onEnchantmentCritical(entity);
        AutoShield.block(true);

    }

    public void MultiprocessAttack(EntityLivingBase entity) {
        AutoShield.block(false);
        blockstate = false;
        if (!isInAttackRange(entity) || !ValidUtils.isInAttackFOV(entity, (int) (FOV.getValue().intValue())))
            return;
        EntityPlayerSP player = Wrapper.INSTANCE.player();
        float sharpLevel = EnchantmentHelper.getModifierForCreature(player.getHeldItem(),
                entity.getCreatureAttribute());
        if (this.packetReach.getValue()) {
            double posX = entity.posX - 3.5 * Math.cos(Math.toRadians(Utils.getYaw(entity) + 90.0f));
            double posZ = entity.posZ - 3.5 * Math.sin(Math.toRadians(Utils.getYaw(entity) + 90.0f));
            Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(posX, entity.posY, posZ,
                    Utils.getYaw(entity), Utils.getPitch(entity), player.onGround));
            Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(entity, Action.ATTACK));
            Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY,
                    player.posZ, player.onGround));
        } else {
            if (autoDelay.getValue() || mode.getMode("Multi").isToggled())
                Utils.attack(entity);
            else
                Wrapper.INSTANCE.sendPacket(new C02PacketUseEntity(entity, Action.ATTACK));
        }
        Utils.swingMainHand();
        if (sharpLevel > 0.0f)
            player.onEnchantmentCritical(entity);

    }

    boolean isPriority(EntityLivingBase entity) {
        return (priority.getMode("Closest").isToggled() && ValidUtils.isClosest(entity, target))
                || (priority.getMode("Health").isToggled() && ValidUtils.isLowHealth(entity, target))
                || (priority.getMode("Fov").isToggled() && ValidUtils.isFov(entity, target))
                || (priority.getMode("Armor").isToggled() && ValidUtils.isArmor(entity, target))

                ;
    }

    boolean isInAttackRange(EntityLivingBase entity) {
        return packetReach.getValue()
                ? entity.getDistanceToEntity(Wrapper.INSTANCE.player()) <= (float) (packetRange.getValue().floatValue())
                : entity.getDistanceToEntity(Wrapper.INSTANCE.player()) <= (float) (range.getValue().floatValue());
    }

    private static boolean HanabiisValidEntity(Entity entity) {

        // Fov check
        if (!AimUtil.isVisibleFOV(entity, FOV.getValue().floatValue()))
            return false;

        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0f) {


                return false;
            }

            if (mc.thePlayer.getDistanceToEntity(entity) < (range.getValue())) {
                Module targets = HackManager.getHack("Targets");
                if (entity != mc.thePlayer && !mc.thePlayer.isDead
                        && !(entity instanceof EntityArmorStand || entity instanceof EntitySnowman)) {


                    if (targets.isToggled() && entity instanceof EntityPlayer && targets.isToggledValue("Players")) {

                        if (!mc.thePlayer.canEntityBeSeen(entity) && !walls.getValue())
                            return false;

                        if (targets.isToggled() && entity.isInvisible() && !targets.isToggledValue("Invisibles"))
                            return false;

                        return !AntiBot.isBot(entity) && ValidUtils.isFriendEnemy((EntityLivingBase) entity);
                    }

                    if ((entity instanceof EntityMob || entity instanceof EntitySlime) && targets.isToggledValue("Mobs") && targets.isToggled()) {
                        if (!mc.thePlayer.canEntityBeSeen(entity) && !walls.getValue())
                            return false;

                        return !AntiBot.isBot(entity);
                    }

                    if ((entity instanceof EntityAnimal || entity instanceof EntityVillager)
                            && targets.isToggledValue("Mobs") && targets.isToggled()) {
                        if (!mc.thePlayer.canEntityBeSeen(entity) && !walls.getValue())
                            return false;

                        return !AntiBot.isBot(entity);
                    }
                }
            }
        }
        return false;
    }


    public boolean check(EntityLivingBase entity) {
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (ValidUtils.isValidEntity(entity)) {
            return false;
        }
        if (!ValidUtils.isNoScreen()) {
            return false;
        }
        if (entity == Wrapper.INSTANCE.player()) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity.deathTime > 0) {
            return false;
        }
        if (ValidUtils.isBot(entity)) {
            return false;
        }
        if (!ValidUtils.isFriendEnemy(entity)) {
            return false;
        }
        if (!ValidUtils.isInvisible(entity)) {
            return false;
        }
        if (!ValidUtils.isInAttackFOV(entity, FOV.getValue().intValue())) {
            return false;
        }
        if (!isInAttackRange(entity)) {
            return false;
        }
        if (!ValidUtils.isTeam(entity)) {
            return false;
        }
        if (!ValidUtils.pingCheck(entity)) {
            return false;
        }
        if (!this.walls.getValue()) {
            // IEntity ent=(IEntity) entity;
            if (!entity.canEntityBeSeen(Wrapper.INSTANCE.player())) {
                return false;
            }
        }
        if (!isPriority(entity)) {
            return false;
        }
        return true;
    }

    public void doBlock() {

        if (!(blockrateValue.getValue() > 0 && random.nextInt(100) <= blockrateValue.getValue()))
            return;

        if (Wrapper.INSTANCE.player().getHeldItem() == null)
            return;
        if (!(Wrapper.INSTANCE.player().getHeldItem().getItem() instanceof ItemSword))
            return;
        if (KillAura.getTarget() == null) {
            KillAura.blockstate = false;
            return;
        }

        if (interact.getValue()) {
            mc.thePlayer.sendQueue
                    .addToSendQueue(new C02PacketUseEntity(target, new Vec3((double) randomNumber(-50, 50) / 100.0D,
                            (double) randomNumber(0, 200) / 100.0D, (double) randomNumber(-50, 50) / 100.0D)));
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.INTERACT));
        }

        if (blockmode.getMode("BlockBasic").isToggled()) {
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);

            if (Wrapper.INSTANCE.controller().sendUseItem(Wrapper.INSTANCE.player(), Wrapper.INSTANCE.world(),
                    Wrapper.INSTANCE.player().inventory.getCurrentItem())) {
                Wrapper.INSTANCE.mc().getItemRenderer().resetEquippedProgress2();
            }

        } else if (blockmode.getMode("BlockBasic2").isToggled()) {

            Wrapper.INSTANCE.sendPacket(
                    new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().inventory.getCurrentItem()));

        } else if (blockmode.getMode("Hypixel").isToggled()) {

            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
                    PlayerControllerUtils.getHypixelBlockpos(mc.getSession().getUsername()), 255,
                    mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));

        } else if (blockmode.getMode("NoPacket").isToggled()) {
            // Wrapper.INSTANCE.mcSettings().keyBindUseItem.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(),
            // true);
            //IKeyBind key = (IKeyBind) Wrapper.INSTANCE.mcSettings().keyBindUseItem;
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);
        }
        KillAura.blockstate = true;

    }

    public static boolean getBlockState() {
        return blockstate;
    }

    public static int randomNumber(int max, int min) {
        return Math.round((float) min + (float) Math.random() * (float) (max - min));
    }

    private void unBlock() {
        if (blockmode.getMode("BlockBasic").isToggled()) {
            if (blockstate) {
                KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), false);
                Wrapper.INSTANCE.controller().onStoppedUsingItem(Wrapper.INSTANCE.player());
                Wrapper.INSTANCE.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        } else if (blockmode.getMode("BlockBasic2").isToggled()) {
            if (blockstate) {
                Wrapper.INSTANCE.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        MoveUtils.isMoving() ? new BlockPos(-1, -1, -1) : BlockPos.ORIGIN, EnumFacing.DOWN));

            }

        } else if (blockmode.getMode("NoPacket").isToggled()) {
            if (blockstate) {
                // Wrapper.INSTANCE.controller().onPlayerRightClick(Wrapper.INSTANCE.player(),
                // Wrapper.INSTANCE.world(), Wrapper.INSTANCE.inventory().getCurrentItem(),
                // MoveUtils.isMoving() ? new BlockPos(-1, -1, -1) : BlockPos.ORIGIN,
                // Wrapper.INSTANCE.mc().objectMouseOver.sideHit,
                // Wrapper.INSTANCE.mc().objectMouseOver.hitVec);
                // Wrapper.INSTANCE.controller().onStoppedUsingItem(Wrapper.INSTANCE.player());
                KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), false);
            }
        }
        this.blockstate = false;
    }

    public static EntityLivingBase getTarget() {
        return target;
    }

    private void nmsl(int value) {

        switch (value) {
            case 166500 / 666:
                try {
                    new A03A59A2();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case 6 + 60 + 600:
                break;
        }

    }

    // hanabi code
    private void HanabigetTarget() {
        int maxSize = switchsize.getValue().intValue();

        /*
         * for (Object object : Utils.getEntityList()) { if (!(object instanceof
         * EntityLivingBase)) continue; EntityLivingBase entity = (EntityLivingBase)
         * object; if (!check(entity)&& !targets.contains(entity)) continue;
         * targets.add(entity);
         *
         * if (targets.size() >= maxSize) break; }
         */

        for (Entity o3 : mc.theWorld.loadedEntityList) { // 闂備緡鍓欑粔鏉戭啅閼姐倧鎷烽崷顓炰粧缂傚稄鎷?
            EntityLivingBase curEnt;

            if (o3 instanceof EntityLivingBase && HanabiisValidEntity(curEnt = (EntityLivingBase) o3) && !targets.contains(curEnt))
                targets.add(curEnt);

            if (targets.size() >= maxSize)
                break;
        }

        if (priority.getMode("Armor").isToggled()) {
            targets.sort(Comparator.comparingInt((o) -> {
                return o instanceof EntityPlayer ? ((EntityPlayer) o).inventory.getTotalArmorValue()
                        : (int) o.getHealth();
            }));
        }

        if (priority.getMode("Closest").isToggled()) {
            targets.sort(
                    (o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
        }
        if (priority.getMode("Fov").isToggled()) {
            // ChatUtils.message("ok");
            targets.sort(Comparator.comparingDouble(o -> RotationUtil
                    .getDistanceBetweenAngles(mc.thePlayer.rotationPitch, RotationUtil.getRotations(o)[0])));
        }
        if (priority.getMode("Angle").isToggled()) {
            targets.sort((o1, o2) -> {
                float[] rot1 = RotationUtil.getRotations(o1);
                float[] rot2 = RotationUtil.getRotations(o2);
                return (int) (mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
            });
        }
        if (priority.getMode("Health").isToggled()) {
            targets.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        }
    }

    // hanabi update

    private void Hanabiupdate() {
        sendtime = 0;

        if (!targets.isEmpty() && index >= targets.size())
            index = 0;

        if (target != null && !targets.isEmpty()) {

            for (Object object : Utils.getEntityList()) {
                if (!(object instanceof EntityLivingBase))
                    continue;
                EntityLivingBase entity = (EntityLivingBase) object;
                if (HanabiisValidEntity(entity))
                    continue;
                targets.remove(entity);
            }
        }

        HanabigetTarget();

        if (targets.size() == 0) {
            target = null;
        } else {
            target = targets.get(index);
            if (mc.thePlayer.getDistanceToEntity(target) > range.getValue()) {
                target = targets.get(0);
            }
        }

        if ((target != null && targets != null && !targets.isEmpty()) || !target.isDead) {

            if (targets.isEmpty()) {
                return;
            }

            if (target.hurtTime == 10 && switchTimer.isDelayComplete(switchDelay.getValue() * 1000)
                    && targets.size() > 1) {
                switchTimer.reset();
                ++index;
            }

            if (mc.thePlayer.getDistanceToEntity(target) <= 0.39 && !rotations.getMode("ROAAC").isToggled()) {
                lastRotations[1] = 90f;
            } else {
                if (rotations.getMode("RONull").isToggled()) {
                    lastRotations = AimUtil.getRotations(targets.get(index));
                } else if (rotations.getMode("RONew").isToggled()) {
                    Vec3 playerVec = new Vec3(mc.thePlayer.posX,
                            mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
                    Vec3 targetVec = target.getPositionVector();
                    if (targetVec.yCoord - 0.7D > playerVec.yCoord) {
                        targetVec = targetVec.addVector(0.0D, (target.getEyeHeight() / 2.0F + RandomFloat(-0.5F, 0.5F)),
                                0.0D);
                    } else if (targetVec.yCoord > playerVec.yCoord) {
                        targetVec = targetVec.addVector(0.0D, (target.getEyeHeight() / 1.2F + RandomFloat(-0.3F, 0.3F)),
                                0.0D);
                    } else {
                        targetVec = targetVec.addVector(0.0D, target.getEyeHeight() / 0.9 + RandomFloat(-0.3F, 0.3F),
                                0.0D);
                    }
                    targetVec = targetVec.addVector(target.posX - target.lastTickPosX, 0.0,
                            target.posZ - target.lastTickPosZ);
                    lastRotations = RotationUtil.getNeededFacing(targetVec, playerVec);
                } else if (rotations.getMode("Custom").isToggled()) {
                    lastRotations = RotationUtil
                            .getNeededRotations(AimUtil.getCenter(targets.get(index).getEntityBoundingBox()));
                } else if (rotations.getMode("ROAAC").isToggled()) {


                    double randomYaw = 0.05D;
                    double randomPitch = 0.05D;
                    float targetYaw = RotationUtil.getYawChange(lastRotations[0],
                            target.posX + (double) randomNumber(1, -1) * randomYaw,
                            target.posZ + (double) randomNumber(1, -1) * randomYaw);
                    float yawFactor = targetYaw / 1.7F;
                    lastRotations[0] = lastRotations[0] + yawFactor;
                    // em.setYaw(sYaw + yawFactor);
                    lastRotations[0] += yawFactor;
                    float targetPitch = RotationUtil.getPitchChange(lastRotations[1], target,
                            target.posY + (double) randomNumber(1, -1) * randomPitch);
                    float pitchFactor = targetPitch / 1.7F;
                    // em.setPitch(sPitch + pitchFactor);
                    lastRotations[1] = lastRotations[1] + pitchFactor;
                    lastRotations[1] += pitchFactor;

                } else if (rotations.getMode("ROBasic").isToggled()) {
                    lastRotations[0] = Utils.getRotationsNeeded(target)[0];
                    lastRotations[1] = Utils.getRotationsNeeded(target)[1];
                } else if (rotations.getMode("Auto").isToggled()) {
                    turn = RandomFloat(minTurnSpeed.getValue().floatValue(), maxTurnSpeed.getValue().floatValue());
                    lastRotations[0] = AimUtil.getRotation(mc.thePlayer.rotationYaw, Utils.getRotationsNeeded(target)[0],
                            (float) (turn * 1));
                    lastRotations[1] = AimUtil.getRotation(mc.thePlayer.rotationPitch, Utils.getRotationsNeeded(target)[1],
                            (float) (turn * 1));

                }

            }

        } else {
            targets.clear();
            lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        }
    }
    // hanabi

    public static class AimUtil {
        protected static float getRotation(float currentRotation, float targetRotation, float maxIncrement) {
            float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
            if (deltaAngle > maxIncrement) {
                deltaAngle = maxIncrement;
            }
            if (deltaAngle < -maxIncrement) {
                deltaAngle = -maxIncrement;
            }
            return currentRotation + deltaAngle / 2.0f;
        }

        public static float normalizeAngle(float angle) {
            return MathHelper.wrapAngleTo180_float((angle + 180.0F) % 360.0F - 180.0F);
        }

        public static float[] getSmoothRotations(Vec3 vec, float currentYaw, float currentPitch) {
            double diffX = vec.xCoord + 0.5D - Minecraft.getMinecraft().thePlayer.posX;
            double diffY = vec.yCoord + 0.5D - (Minecraft.getMinecraft().thePlayer.posY
                    + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());
            double diffZ = vec.zCoord + 0.5D - Minecraft.getMinecraft().thePlayer.posZ;
            double dist = (double) MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
            float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
            boolean aim = false;
            float max = 5.0F;
            if (MathHelper.wrapAngleTo180_float(yaw - currentYaw) > max * 2.0F) {
                aim = true;
            } else if (MathHelper.wrapAngleTo180_float(yaw - currentYaw) < -max * 2.0F) {
                aim = true;
            }

            if (MathHelper.wrapAngleTo180_float(pitch - currentPitch) > max * 4.0F) {
                aim = true;
            } else if (MathHelper.wrapAngleTo180_float(pitch - currentPitch) < -max * 4.0F) {
                aim = true;
            }

            float[] rotations = new float[]{currentYaw, currentPitch};
            if (aim) {
                rotations[0] = (float) ((double) currentYaw + (double) MathHelper.wrapAngleTo180_float(yaw - currentYaw)
                        / (1.5D * (random.nextDouble() * 2.0D + 1.0D)));
                rotations[1] = (float) ((double) currentPitch
                        + (double) MathHelper.wrapAngleTo180_float(pitch - currentPitch)
                        / (1.5D * (random.nextDouble() * 2.0D + 1.0D)));
            }

            return rotations;
        }

        public static float set(float f1, float f2, float f3) {
            float f = MathHelper.wrapAngleTo180_float(f2 - f1);
            if (f > f3) {
                f = f3;
            }
            if (f < -f3) {
                f = -f3;
            }
            return f1 + f;
        }

        private static float[] getRotations(EntityLivingBase ent) {
            final double x = ent.posX - mc.thePlayer.posX;
            double y = ent.posY - mc.thePlayer.posY;
            final double z = ent.posZ - mc.thePlayer.posZ;
            y /= mc.thePlayer.getDistanceToEntity(ent);
            final float yaw = (float) (-(Math.atan2(x, z) * 57.29577951308232));
            final float pitch = (float) (-(Math.asin(y) * 57.29577951308232));
            return new float[]{yaw, pitch};
        }

        public static Vec3 getCenter(AxisAlignedBB bb) {
            double value = Math.random();
            return new Vec3(
                    bb.minX + (bb.maxX - bb.minX) * (randoms.getValue() ? value : (rotationValue.getValue() / 400)),
                    bb.minY + (bb.maxY - bb.minY) * (randoms2.getValue() ? value : (rotationValue.getValue() / 400)),
                    bb.minZ + (bb.maxZ - bb.minZ) * (randoms.getValue() ? value : (rotationValue.getValue() / 400)));
        }

        public static boolean isVisibleFOV(final Entity e, final float fov) {
            return ((Math.abs(AimUtil.getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw)
                    % 360.0f > 180.0f) ? (360.0f
                    - Math.abs(AimUtil.getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw)
                    % 360.0f)
                    : (Math.abs(AimUtil.getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw)
                    % 360.0f)) <= fov;
        }

        // NCP Simulated
        public static boolean isValidRangeForNCP(final Entity entity, double range) {
            if (entity == null || Minecraft.getMinecraft().thePlayer == null)
                return false;

            final Location dRef = new Location(entity.posX, entity.posY, entity.posZ);
            final Location pLoc = new Location(Minecraft.getMinecraft().thePlayer.posX,
                    Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);

            final double height = entity instanceof EntityLivingBase ? entity.getEyeHeight() : 1.75;

            // Refine y position.

            final double pY = pLoc.getY() + Minecraft.getMinecraft().thePlayer.getEyeHeight();
            final double dY = dRef.getY();
            if (pY <= dY)
                ; // Keep the foot level y.
            else if (pY >= dY + height)
                dRef.setY(dY + height); // Highest ref y.
            else
                dRef.setY(pY); // Level with damaged.

            Vec3 temp = new Vec3(pLoc.getX(), pY, pLoc.getZ());
            final Vec3 pRel = dRef.toVector().subtract(temp); //

            // Distance is calculated from eye location to center of targeted. If the player
            // is further away from their target
            // than allowed, the difference will be assigned to "distance".
            final double lenpRel = pRel.lengthVector();

            double violation = lenpRel - range;

            if (violation > 0) {
                return false;
            }

            return true;
        }

        // Skidded from Minecraft
        public static float[] getRotations(final Entity entity) {
            if (entity == null) {
                return null;
            }
            final double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
            final double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
            double diffY;
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase elb = (EntityLivingBase) entity;
                diffY = elb.posY + (elb.getEyeHeight())
                        - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
            } else {
                diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0
                        - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
            }
            final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
            final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
            return new float[]{yaw, pitch};
        }

    }

    public static double RandomFloat(float minFloat, float maxFloat) {
        return minFloat >= maxFloat ? minFloat : new Random().nextFloat() * (maxFloat - minFloat) + minFloat;
    }

    private static boolean isValidEntity(Entity entity) {

        // Fov check
        if (!AimUtil.isVisibleFOV(entity, FOV.getValue().floatValue()))
            return false;

        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0f) {
                return false;
            }

            if (mc.thePlayer.getDistanceToEntity(entity) < range.getValue()) {
                if (entity != mc.thePlayer && !mc.thePlayer.isDead
                        && !(entity instanceof EntityArmorStand || entity instanceof EntitySnowman)) {

                    Module targets = HackManager.getHack("Targets");

                    if (entity instanceof EntityPlayer && targets.isToggled() && targets.isToggledValue("Players")) {

                        if (!mc.thePlayer.canEntityBeSeen(entity) && !walls.getValue())
                            return false;

                        /*
                         * if (entity.isInvisible() && !invisible.getValueState()) return false;
                         */

                        return !AntiBot.isBot((EntityPlayer) entity) && !ValidUtils.isTeam((EntityLivingBase) entity);
                    }

                    if ((entity instanceof EntityMob || entity instanceof EntitySlime) && targets.isToggled()
                            && targets.isToggledValue("Mobs")) {
                        return !AntiBot.isBot((EntityPlayer) entity);
                    }

                    if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && targets.isToggled()
                            && targets.isToggledValue("Mobs")) {
                        return !AntiBot.isBot((EntityPlayer) entity);
                    }
                }
            }
        }
        return false;
    }

    // hanabi code
    public void newhud(ScaledResolution sr) {
        int w = sr.getScaledWidth() / 2;
        int h = sr.getScaledHeight() / 2;
        // EntityLivingBase ent = null;

        if (target != null) {
            String name = target.getName();
            float rectWidth = Math.max(100 + Core.fontManager.getFont("SFB 6").getWidth(name), 140);
            RenderUtils.drawRoundRect2(w + 37, h + 39, w + rectWidth + 1, h + 71, 14, new Color(50, 50, 50).getRGB());
            RenderUtils.drawRoundRect2(w + 38, h + 40, w + rectWidth, h + 70, 14, new Color(40, 40, 40).getRGB());
            RenderUtils.circle(w + 50, h + 50, 20, new Color(50, 50, 50).getRGB());
            int health = Math.round(target.getHealth());
            GlStateManager.color(1, 1, 1);
            GuiInventory.drawEntityOnScreen(w + 50, h + 65, 15, target.rotationYaw % 180 + 45,
                    target.rotationPitch % 45, target);
            mc.fontRendererObj.drawString("\247l" + name, w + 80, h + 44, -1);

            if (health > 20) {
                health = 20;
            }
            float[] fractions = new float[]{0f, 0.5f, 1f};
            Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
            float progress = (health * 5) * 0.01f;
            Color customColor = Colors.blendColors(fractions, colors, progress).brighter();
            float healthWidth = (float) (360 * MathHelper
                    .clamp_double(target.getHealth() / Math.max(target.getHealth(), target.getMaxHealth()), 0, 1));
            healthTranslate.interpolate(healthWidth, 0, 0.1F);
            RenderUtils.drawArc(w + 50, h + 50, 20, customColor.getRGB(), 180, 180 + healthTranslate.getX(), 4);

            float armorWidth = 3.6f * (target.getTotalArmorValue() * 5);
            armorTranslate.interpolate(armorWidth, 0, 0.1F);
            RenderUtils.drawArc(w + 85, h + 60, 7, Colors.getDarker(new Color(60, 160, 255), 100, 255).getRGB(), 180,
                    180 * 3, 3);
            RenderUtils.drawArc(w + 85, h + 60, 7, new Color(60, 160, 255).getRGB(), 180, 180 + armorTranslate.getX(),
                    3);
            Core.fontManager.getFont("SFB 6").drawCenteredString("" + target.getTotalArmorValue(), w + 86, h + 59,
                    new Color(255, 255, 255).getRGB());

            float htWidth = (float) (360
                    * MathHelper.clamp_double(target.hurtTime / Math.max(10, target.maxHurtTime), 0, 1));
            if (htWidth < htTranslate.getX()) {
                htTranslate.interpolate(htWidth, 0, 0.1F);
            } else {
                htTranslate.setX(360);
            }
            RenderUtils.drawArc(w + 105, h + 60, 7, Colors.getDarker(new Color(255, 60, 60), 100, 255).getRGB(), 180,
                    180 * 3, 3);
            RenderUtils.drawArc(w + 105, h + 60, 7, new Color(255, 60, 60).getRGB(), 180, 180 + htTranslate.getX(), 3);
            Core.fontManager.getFont("SFB 6").drawCenteredString("" + target.hurtTime, w + 106, h + 59,
                    new Color(255, 255, 255).getRGB());

            if (target != this.target) {
                this.target = target;
                this.lastHealth = target.getHealth();
                this.damageDelt = 0.0f;
            }
            if (this.lastHealth != target.getHealth() && target.getHealth() - this.lastHealth < 1.0f) {
                this.damageDelt = target.getHealth() - this.lastHealth;
                this.lastHealth = target.getHealth();
            }
            float dmgWidth = (float) (360 * MathHelper.clamp_double(Math.min(Math.abs(damageDelt), 6) / 6, 0, 1));
            dmgTranslate.interpolate(dmgWidth, 0, 0.1F);
            RenderUtils.drawArc(w + 125, h + 60, 7, Colors.getDarker(new Color(160, 60, 255), 100, 255).getRGB(), 180,
                    180 * 3, 3);
            RenderUtils.drawArc(w + 125, h + 60, 7, new Color(160, 60, 255).getRGB(), 180, 180 + dmgTranslate.getX(),
                    3);
            Core.fontManager.getFont("SFB 6").drawCenteredString(" " + String.format("%.1f", Math.abs(damageDelt)),
                    w + 126, h + 59, new Color(255, 255, 255).getRGB());
        } else {
            healthTranslate.setX(0);
            armorTranslate.setX(0);
            htTranslate.setX(360);
            dmgTranslate.setX(0);
        }
    }

    private void fancyhud(ScaledResolution sr) {
        FontManager font1 = Core.fontManager;
        if (target != null) {
            int width = (sr.getScaledWidth() / 2) + 100;
            int height = sr.getScaledHeight() / 2;

            EntityLivingBase player = target;

            Gui.drawRect(width - 70, height + 30, width + 80, height + 105, new Color(255, 255, 255, 100).getRGB());
            font1.getFont("SFB 8").drawString("Name: " + player.getName(), width - 65, height + 35, -1);
            font1.getFont("SFB 8").drawString("Distance: " + (int) mc.thePlayer.getDistanceToEntity(player), width - 65,
                    height + 60, -1);
            // font1.getFont("SFB 8").drawString("Hurttime: " + player.hurtTime, width - 65,
            // height + 70, -1);

            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            Wrapper.INSTANCE.mc().getRenderItem().renderItemAndEffectIntoGUI(player.getHeldItem(), width + 50,
                    height + 80);
            GL11.glPopMatrix();

            float health = player.getHealth();
            float healthPercentage = (health / player.getMaxHealth());
            float targetHealthPercentage = 0;
            if (healthPercentage != lastHealth) {
                float diff = healthPercentage - this.lastHealth;
                targetHealthPercentage = this.lastHealth;
                this.lastHealth += diff / 8;
            }
            Color healthcolor = Color.WHITE;
            if (healthPercentage * 100 > 75) {
                healthcolor = Color.GREEN;
            } else if (healthPercentage * 100 > 50 && healthPercentage * 100 < 75) {
                healthcolor = Color.YELLOW;
            } else if (healthPercentage * 100 < 50 && healthPercentage * 100 > 25) {
                healthcolor = Color.ORANGE;
            } else if (healthPercentage * 100 < 25) {
                healthcolor = Color.RED;
            }
            Gui.drawRect(width - 70, height + 104, (int) (width - 70 + (149 * targetHealthPercentage)), height + 106,
                    healthcolor.getRGB());
            Gui.drawRect(width - 70, height + 104, (int) (width - 70 + (149 * healthPercentage)), height + 106,
                    Color.GREEN.getRGB());
            GL11.glColor4f(1, 1, 1, 1);
            GuiInventory.drawEntityOnScreen(width + 60, height + 75, 20, Mouse.getX(), Mouse.getY(), player);
        }
    }

    public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
        return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
    }

    // hanabi circle
    private void drawCircle(Entity entity, float partialTicks, double rad, double height) {

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0f);

        GL11.glBegin(GL11.GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
                - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
                - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
                - mc.getRenderManager().viewerPosZ;

        final float r = ((float) 1 / 255) * Color.WHITE.getRed();
        final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
        final float b = ((float) 1 / 255) * Color.WHITE.getBlue();
        GL11.glColor3f(r, g, b);
        final double pix2 = Math.PI * 2.0D;

        for (int i = 0; i <= 90; ++i) {

            GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45), y + height, z + rad * Math.sin(i * pix2 / 45));
        }

        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private void hud2(ScaledResolution sr) {

        FontManager font1 = Core.fontManager;
        if (target != null) {
            int width = (sr.getScaledWidth() / 2);
            int height = sr.getScaledHeight() / 2;

            EntityLivingBase player = target;

            Gui.drawRect(width - 70, height + 20, width + 80, height + 40, new Color(255, 255, 255, 100).getRGB());
            font1.getFont("SFB 8").drawString("Name: " + player.getName(), width - 65, height + 28, -1);
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();

            float health = player.getHealth();
            float healthPercentage = (health / player.getMaxHealth());
            float targetHealthPercentage = 0;
            if (healthPercentage != lastHealth) {
                float diff = healthPercentage - this.lastHealth;
                targetHealthPercentage = this.lastHealth;
                this.lastHealth += diff / 8;
            }
            Color healthcolor = Color.WHITE;
            if (healthPercentage * 100 > 75) {
                healthcolor = Color.GREEN;
            } else if (healthPercentage * 100 > 50 && healthPercentage * 100 < 75) {
                healthcolor = Color.YELLOW;
            } else if (healthPercentage * 100 < 50 && healthPercentage * 100 > 25) {
                healthcolor = Color.ORANGE;
            } else if (healthPercentage * 100 < 25) {
                healthcolor = Color.RED;
            }
            Gui.drawRect(width - 70, height + 39, (int) (width - 70 + (149 * targetHealthPercentage)), height + 41,
                    healthcolor.getRGB());
            Gui.drawRect(width - 70, height + 39, (int) (width - 70 + (149 * healthPercentage)), height + 41,
                    Color.GREEN.getRGB());
            GL11.glColor4f(1, 1, 1, 1);
        }
    }

    private void hud3(ScaledResolution sr) {
        TTFFontRenderer font = Core.fontManager.getFont("SFB 6");
        if (target != null) {
            int width = (sr.getScaledWidth() / 2);
            int height = sr.getScaledHeight() / 2;

            GlStateManager.pushMatrix();
            RenderUtils.drawUnfilledRect(sr.getScaledWidth() / 2 + 21, sr.getScaledHeight() / 2 + 21, 80, 25,
                    Colors.getColor(50, 50, 50, 255), 1);

            Wrapper.INSTANCE.mc().getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItem(),
                    sr.getScaledWidth() / 2 + 21 + 24 + 34,
                    (int) (sr.getScaledHeight() / 2 + 18 + font.getHeight(target.getName()) + 4));
            GlStateManager.popMatrix();
            ;

            float health = target.getHealth();
            float healthPercentage = (health / target.getMaxHealth());
            float targetHealthPercentage = 0;
            if (healthPercentage != lastHealth) {
                float diff = healthPercentage - this.lastHealth;
                targetHealthPercentage = this.lastHealth;
                this.lastHealth += diff / 8;
            }
            Color healthcolor = Color.WHITE;
            if (healthPercentage * 100 > 75) {
                healthcolor = Color.GREEN;
            } else if (healthPercentage * 100 > 50 && healthPercentage * 100 < 75) {
                healthcolor = Color.YELLOW;
            } else if (healthPercentage * 100 < 50 && healthPercentage * 100 > 25) {
                healthcolor = Color.ORANGE;
            } else if (healthPercentage * 100 < 25) {
                healthcolor = Color.RED;
            }

            Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 22,
                    sr.getScaledWidth() / 2 + 21 + 24 + 56, sr.getScaledHeight() / 2 + 21 + 25,
                    Colors.getColor(80, 80, 80, 200));
            Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 21 + 24,
                    (int) (sr.getScaledWidth() / 2 + 45 + (56 * targetHealthPercentage)),
                    sr.getScaledHeight() / 2 + 21 + 24 + 1, healthcolor.getRGB());

            Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 21 + 24,
                    (int) (sr.getScaledWidth() / 2 + 45 + (56 * healthPercentage)),
                    sr.getScaledHeight() / 2 + 21 + 24 + 1, Color.GREEN.getRGB());

            font.drawString(target.getName(), sr.getScaledWidth() / 2 + 21 + 24 + 2,
                    sr.getScaledHeight() / 2 + 21 + font.getHeight(target.getName()), Color.white.getRGB());

            font.drawString("HP:" + String.format("%.2f", target.getHealth()), sr.getScaledWidth() / 2 + 21 + 26,
                    (sr.getScaledHeight() / 2 + 21 + font.getHeight(target.getName())) + 7, Color.white.getRGB());
            font.drawString("Armor:" + target.getTotalArmorValue(), sr.getScaledWidth() / 2 + 21 + 24 + 2,
                    (sr.getScaledHeight() / 2 + 21 + font.getHeight(target.getName())) + 14,
                    (target.getTotalArmorValue() <= mc.thePlayer.getTotalArmorValue() ? Color.green.getRGB()
                            : Color.red.getRGB()));

            GL11.glColor4f(1, 1, 1, 1);

            drawFace(target, sr.getScaledWidth() / 2 + 20, sr.getScaledHeight() / 2 + 20);

        }

    }

    private void drawFace(EntityLivingBase target, int x, int y) {
        if (target instanceof EntityPlayer) {
            NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            Gui.drawScaledCustomSizeModalRect((int) x + 2, (int) y + 2, 8.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.bindTexture(0);
        }

    }

    @Override
    public void onPacketEvent(EventPacket event) {
        if (event.getType() == EventType.SEND && event.getPacket() instanceof C03PacketPlayer && packet.getValue()) {
            if (rotate.getValue() && target != null) {
                IC03Packet IC03 = (IC03Packet) event.getPacket();
                IC03.setPitch(lastRotations[1]);
                IC03.setYaw(lastRotations[0]);

            }

        }
        super.onPacketEvent(event);
    }

    private void hud4(ScaledResolution sr) {
        TTFFontRenderer font = Core.fontManager.getFont("SFB 6");
        if (target != null) {
            int width = (sr.getScaledWidth() / 2);
            int height = sr.getScaledHeight() / 2;

            GlStateManager.pushMatrix();
            RenderUtils.drawUnfilledRect(sr.getScaledWidth() / 2 + 21, sr.getScaledHeight() / 2 + 21, 80, 25,
                    Colors.getColor(50, 50, 50, 255), 1);
            GlStateManager.popMatrix();
            float health = target.getHealth();
            float healthPercentage = (health / target.getMaxHealth());
            float targetHealthPercentage = 0;
            if (healthPercentage != lastHealth) {
                float diff = healthPercentage - this.lastHealth;
                targetHealthPercentage = this.lastHealth;
                this.lastHealth += diff / 8;
            }
            Color healthcolor = Color.WHITE;
            if (healthPercentage * 100 > 75) {
                healthcolor = Color.GREEN;
            } else if (healthPercentage * 100 >= 50 && healthPercentage * 100 <= 75) {
                healthcolor = Color.YELLOW;
            } else if (healthPercentage * 100 < 50 && healthPercentage * 100 > 25) {
                healthcolor = Color.ORANGE;
            } else if (healthPercentage * 100 <= 25) {
                healthcolor = Color.RED;
            }

            Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 22,
                    sr.getScaledWidth() / 2 + 21 + 24 + 56, sr.getScaledHeight() / 2 + 21 + 25,
                    Colors.getColor(80, 80, 80, 200));
            Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 21 + 18,
                    (int) (sr.getScaledWidth() / 2 + 45 + (56 * targetHealthPercentage)),
                    sr.getScaledHeight() / 2 + 21 + 24 + 1, healthcolor.getRGB());

            font.drawStringWithShadow(String.format("%.1f", healthPercentage * 100) + "%",
                    sr.getScaledWidth() / 2 + 21 + 26, sr.getScaledHeight() / 2 + 22 + 18,
                    Colors.getColor(Color.white));

            /*
             * Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 21
             * + 24 , (int) (sr.getScaledWidth() / 2 + 45 + (56 * healthPercentage)),
             * sr.getScaledHeight() / 2 + 21 + 24 + 1, Color.GREEN.getRGB());
             */

            font.drawString(target.getName(), sr.getScaledWidth() / 2 + 21 + 24 + 2,
                    sr.getScaledHeight() / 2 + 21 + font.getHeight(target.getName()), Color.white.getRGB());

            // GL11.glColor4f(1, 1, 1, 1);

            drawFace(target, sr.getScaledWidth() / 2 + 20, sr.getScaledHeight() / 2 + 20);

        }

    }

    public void renderTargetBox(EntityLivingBase e, float partialTicks) {
        if (e instanceof EntityLivingBase) {
            final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * partialTicks
                    - Wrapper.INSTANCE.mc().getRenderManager().viewerPosX;
            final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * partialTicks
                    - Wrapper.INSTANCE.mc().getRenderManager().viewerPosY;
            final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * partialTicks
                    - Wrapper.INSTANCE.mc().getRenderManager().viewerPosZ;
            float ex = (float) ((double) e.getCollisionBorderSize());

            float p = (e.getMaxHealth() - e.getHealth())
                    / e.getMaxHealth();
            float red = p * 2F;
            float green = 2 - red;

            net.minecraft.util.AxisAlignedBB bbox = e.getEntityBoundingBox().expand(ex, ex, ex);
            net.minecraft.util.AxisAlignedBB axis = new net.minecraft.util.AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(2.0F);
            GL11.glColor3d(red, green, 0);
            RenderGlobal.drawSelectionBoundingBox(axis);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }

    void hud5() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glPushMatrix();
        GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX,
                -TileEntityRendererDispatcher.staticPlayerY,
                -TileEntityRendererDispatcher.staticPlayerZ);

        //AxisAlignedBB box = new AxisAlignedBB(BlockPos.ORIGIN,BlockPos.ORIGIN);
        float p = (target.getMaxHealth() - target.getHealth())
                / target.getMaxHealth();
        float red = p * 2F;
        float green = 2 - red;

        GL11.glTranslated(target.posX, target.posY, target.posZ);
        GL11.glTranslated(0, 0.05, 0);
        GL11.glScaled(target.width, target.height, target.width);
        GL11.glTranslated(-0.5, 0, -0.5);

        if (p < 1) {
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glScaled(p, p, p);
            GL11.glTranslated(-0.5, -0.5, -0.5);
        }

        GL11.glColor3f(red, green, 0);
        GL11.glBegin(GL11.GL_QUADS);
        RenderUtils.drawSolidBox2(RenderUtils.DEFAULT_AABB);
        GL11.glEnd();

        GL11.glColor3f(red, green, 0);
        GL11.glBegin(GL11.GL_LINES);
        RenderUtils.drawOutlinedBox2(RenderUtils.DEFAULT_AABB);
        GL11.glEnd();

        GL11.glPopMatrix();

        // GL resets
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    void setSword() {
        Utils.nullCheck();
        int best = -1;

        for (int i = 0; i < 9; ++i) {
            if (mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].getItem() != null && mc.thePlayer.inventory.mainInventory[i].getItem() instanceof ItemSword) {
                final ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                if (is.getItem() instanceof ItemSword) {
                    best = i;
                }
            }
        }

        if (best != -1 && Wrapper.INSTANCE.player().inventory.getCurrentItem() == null) {
            Utils.hotkeyToSlot(best);
        }
        if (best != -1 && !(Wrapper.INSTANCE.player().inventory.getCurrentItem().getItem() instanceof ItemSword) && Wrapper.INSTANCE.player().inventory.getCurrentItem() != null) {
            Utils.hotkeyToSlot(best);
        }
    }

}
