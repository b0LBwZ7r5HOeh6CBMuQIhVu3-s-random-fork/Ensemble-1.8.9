package it.fktcod.ktykshrk.module.mods;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Hitbox extends Module {

    public NumberValue width;
    public NumberValue height;
    public BooleanValue walls;

    public static NumberValue expand;
    public static BooleanValue extra;
    public static BooleanValue nofire;
    public static NumberValue extraV;

    public ModeValue mode;

    //raven
    private static MovingObjectPosition mv;
    NumberValue multiplier;
    BooleanValue showbox;
    int getInput = 2;

    public Hitbox() {
        super("HitBox", HackCategory.COMBAT);


        this.mode = new ModeValue("Mode", new Mode("Basic", true), new Mode("Box", false), new Mode("Raven", false));

        this.width = new NumberValue("Width", 1.0D, 0.6D, 5.0D);
        this.height = new NumberValue("Height", 2.2D, 1.8D, 5.0D);

        expand = new NumberValue("Expand", 0.1, 1.0, 2.0);
        extra = new BooleanValue("Extra", false);
        nofire = new BooleanValue("NoFire", false);
        extraV = new NumberValue("ExtraExpand", 0.0, 0.0, 15.0);

        multiplier = new NumberValue("Multiplier", 1.2D, 1D, 5D);
        showbox = new BooleanValue("ShowBox", true);

        this.walls = new BooleanValue("ThroughWalls", false);
        this.setChinese(Core.Translate_CN[50]);
        this.addValue(extra, width, height, expand, extraV, nofire, multiplier, showbox, walls, mode);
    }

    @Override
    public String getDescription() {
        return "Change size hit box of entity.";
    }

    @Override
    public void onClientTick(ClientTickEvent event) {

        if (mode.getMode("Basic").isToggled()) {

            for (Object object : Utils.getEntityList()) {
                if (!(object instanceof EntityLivingBase))
                    continue;
                EntityLivingBase entity = (EntityLivingBase) object;
                if (!check(entity)) {

                    continue;
                }


                if (nofire.getValue()) {
                    entity.setFire(0);
                }

                Utils.setEntityBoundingBoxSize(entity, (float) (width.getValue().floatValue()),
                        (float) (height.getValue().floatValue()));
            }
        } else if (mode.getMode("Box").isToggled()) {
            List loadedEntityList = Reach.mc.theWorld.loadedEntityList;
            for (int i = 0; i < loadedEntityList.size(); ++i) {
                Entity e = (Entity) loadedEntityList.get(i);
                // if (e == mc.thePlayer || !e.canBeCollidedWith())
                //    continue;

                if (!isValidEntity(e)) {
                    continue;
                }

                if (nofire.getValue()) {
                    e.setFire(0);
                }

                e.width = (float) (extra.getValue() ? 0.6 + expand.getValue() + extraV.getValue() : 0.6 + expand.getValue());
            }
        } else if (mode.getMode("Raven").isToggled()) {
            gmo(1.0F);
        }
        super.onClientTick(event);
    }

    @Override
    public void onDisable() {
        for (Object object : Utils.getEntityList()) {
            if (!(object instanceof EntityLivingBase))
                continue;
            EntityLivingBase entity = (EntityLivingBase) object;
            EntitySize entitySize = this.getEntitySize(entity);
            Utils.setEntityBoundingBoxSize(entity, entitySize.width, entitySize.height);
        }
        super.onDisable();
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (mode.getMode("Raven").isToggled()) {
            Utils.nullCheck();
            Module click = HackManager.getHack("AutoClicker");
            if (click != null && !click.isToggled()) return;

            if (click != null && click.isToggled() && Mouse.isButtonDown(0)) {
                if (mv != null) {
                    mc.objectMouseOver = mv;
                }
            }

        }
        super.onPlayerTick(event);
    }

    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mode.getMode("Raven").isToggled() && showbox.getValue()) {
            for (Entity en : mc.theWorld.loadedEntityList) {
                if (en != mc.thePlayer && en instanceof EntityLivingBase && ((EntityLivingBase) en).deathTime == 0 && !(en instanceof EntityArmorStand) && !en.isInvisible()) {
                    this.rh(en, Color.WHITE, event.partialTicks);
                }
            }

        }

        super.onRenderWorldLast(event);
    }

    private void rh(Entity e, Color c, float partialTicks) {
        if (e instanceof EntityLivingBase) {
            final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * partialTicks
                    - mc.getRenderManager().viewerPosX;
            final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * partialTicks
                    - mc.getRenderManager().viewerPosY;
            final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * partialTicks
                    - mc.getRenderManager().viewerPosZ;
            float ex = (float) ((double) e.getCollisionBorderSize() * this.getInput);
            AxisAlignedBB bbox = e.getEntityBoundingBox().expand(ex, ex, ex);
            AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(2.0F);
            GL11.glColor3d(c.getRed(), c.getGreen(), c.getBlue());
            RenderGlobal.drawSelectionBoundingBox(axis);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }

    @Override
    public void onMouse(MouseEvent event) {
        Utils.nullCheck();
        if (event.button == 0 && event.buttonstate && mv != null) {
            mc.objectMouseOver = mv;
        }
        super.onMouse(event);
    }

    // TODO This code is very bad. Shit!!!
    public EntitySize getEntitySize(Entity entity) {
        EntitySize entitySize = new EntitySize(0.6F, 1.8F);
        if (entity instanceof EntitySpider)
            entitySize = new EntitySize(1.4F, 0.9F);
        if (entity instanceof EntityBat)
            entitySize = new EntitySize(0.5F, 0.9F);
        if (entity instanceof EntityChicken)
            entitySize = new EntitySize(0.5F, 0.9F);
        if (entity instanceof EntityCow)
            entitySize = new EntitySize(0.9F, 1.4F);
        if (entity instanceof EntitySheep)
            entitySize = new EntitySize(0.9F, 1.4F);
        if (entity instanceof EntityEnderman)
            entitySize = new EntitySize(0.6F, 2.9F);
        if (entity instanceof EntityGhast)
            entitySize = new EntitySize(4.0F, 4.0F);
        if (entity instanceof EntityEndermite)
            entitySize = new EntitySize(0.4F, 0.3F);
        if (entity instanceof EntityGiantZombie)
            entitySize = new EntitySize(0.6F * 6.0F, 1.8F * 6.0F);
        if (entity instanceof EntityWolf)
            entitySize = new EntitySize(0.6F, 0.85F);
        if (entity instanceof EntityGuardian)
            entitySize = new EntitySize(0.85F, 0.85F);
        if (entity instanceof EntitySquid)
            entitySize = new EntitySize(0.8F, 0.8F);
        if (entity instanceof EntityDragon)
            entitySize = new EntitySize(16.0F, 8.0F);
        if (entity instanceof EntityRabbit)
            entitySize = new EntitySize(0.4F, 0.5F);
        return entitySize;
    }

    public Entity entity() {
        Entity e = null;
        if (mc.thePlayer.worldObj != null) {
            for (Object o : mc.theWorld.loadedEntityList) {
                e = (Entity) o;
            }
        }
        return e;
    }

    public boolean check(EntityLivingBase entity) {
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (ValidUtils.isValidEntity(entity)) {
            return false;
        }
        if (entity == Wrapper.INSTANCE.player()) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (!ValidUtils.isFriendEnemy(entity)) {
            return false;
        }
        if (!ValidUtils.isTeam(entity)) {
            return false;
        }
        if (!ValidUtils.isBot(entity)) {
            return false;
        }
        if (walls.getValue() && mc.thePlayer.canEntityBeSeen(entity)) {
            return false;
        }
        if (!entity.canBeCollidedWith()) {
            return false;
        }
        return true;

    }

    class EntitySize {
        public float width;
        public float height;

        public EntitySize(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }

    public double exp(Entity en) {
        Module hitBox = HackManager.getHack("HitBox");
        return (hitBox != null && isToggled() && !AntiBot.isBot(en)) ? this.getInput : 1.0D;
    }

    public void gmo(float partialTicks) {
        if (mc.getRenderViewEntity() != null && mc.theWorld != null) {
            mc.pointedEntity = null;
            Entity pE = null;
            double d0 = 3.0D;
            mv = mc.getRenderViewEntity().rayTrace(d0, partialTicks);
            double d2 = d0;
            Vec3 vec3 = mc.getRenderViewEntity().getPositionEyes(partialTicks);
            if (mv != null) {
                d2 = mv.hitVec.distanceTo(vec3);
            }

            Vec3 vec4 = mc.getRenderViewEntity().getLook(partialTicks);
            Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            Vec3 vec6 = null;
            float f1 = 1.0F;
            List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.getRenderViewEntity(), mc.getRenderViewEntity().getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(f1, f1, f1));
            double d3 = d2;

            for (Object o : list) {
                Entity entity = (Entity) o;
                if (entity.canBeCollidedWith()) {
                    float ex = (float) ((double) entity.getCollisionBorderSize() * exp(entity));
                    AxisAlignedBB ax = entity.getEntityBoundingBox().expand(ex, ex, ex);
                    MovingObjectPosition mop = ax.calculateIntercept(vec3, vec5);
                    if (ax.isVecInside(vec3)) {
                        if (0.0D < d3 || d3 == 0.0D) {
                            pE = entity;
                            vec6 = mop == null ? vec3 : mop.hitVec;
                            d3 = 0.0D;
                        }
                    } else if (mop != null) {
                        double d4 = vec3.distanceTo(mop.hitVec);
                        if (d4 < d3 || d3 == 0.0D) {
                            if (entity == mc.getRenderViewEntity().ridingEntity && !entity.canRiderInteract()) {
                                if (d3 == 0.0D) {
                                    pE = entity;
                                    vec6 = mop.hitVec;
                                }
                            } else {
                                pE = entity;
                                vec6 = mop.hitVec;
                                d3 = d4;
                            }
                        }
                    }
                }
            }

            if (pE != null && (d3 < d2 || mv == null)) {
                mv = new MovingObjectPosition(pE, vec6);
                if (pE instanceof EntityLivingBase || pE instanceof EntityItemFrame) {
                    mc.pointedEntity = pE;
                }
            }
        }

    }


    private boolean isValidEntity(Entity entity) {


        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase) entity).getHealth() <= 0f||!entity.canBeCollidedWith()) {


                return false;
            }


            Module targets = HackManager.getHack("Targets");
            if (entity != mc.thePlayer && !mc.thePlayer.isDead
                    && !(entity instanceof EntityArmorStand || entity instanceof EntitySnowman)) {


                if (targets.isToggled() && entity instanceof EntityPlayer && targets.isToggledValue("Players")) {

                    if (!mc.thePlayer.canEntityBeSeen(entity) && !walls.getValue()) {
                        return false;
                    }
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

        return false;
    }

}
