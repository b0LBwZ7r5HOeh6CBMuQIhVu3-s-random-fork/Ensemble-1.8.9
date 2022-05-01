package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.hook.ProfilerHook;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Reach extends Module{

	BooleanValue throughBlocks;
	static NumberValue reachMinVal;
	static NumberValue reachMaxVal;
	static BooleanValue bypassMAC;
	  public  Random r;
	public Reach() {
		super("Reach", HackCategory.COMBAT);
		reachMinVal=new NumberValue("ReachMin", 3D, 3D, 6D);
		reachMaxVal=new NumberValue("ReachMax", 3D, 3D, 6D);
		throughBlocks=new BooleanValue("ThroughBlocks", false);
		bypassMAC=new BooleanValue("BypassMAC", false);
		  r = new Random();
		addValue(throughBlocks,reachMinVal,reachMaxVal);
		this.setChinese(Core.Translate_CN[77]);
	}
		@Override
		public void onMouse(MouseEvent event) {
			  if (!throughBlocks.getValue()&& Reach.mc.objectMouseOver != null && Reach.mc.objectMouseOver.typeOfHit != null && Reach.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
		            return;
		        }
		        double range = reachMinVal.getValue() + r.nextDouble() * (reachMaxVal.getValue() - reachMinVal.getValue());
		        Object[] mouseOver = Reach.getMouseOver(range, 0.0, 0.0f);
		        if (mouseOver == null) {
		            return;
		        }
		        Vec3 lookVec = Reach.mc.thePlayer.getLookVec();
		        Reach.mc.objectMouseOver = new MovingObjectPosition((Entity)mouseOver[0], (Vec3)mouseOver[1]);
		        Reach.mc.pointedEntity = (Entity)mouseOver[0];
			super.onMouse(event);
		}
	
	
	  public static Object[] getMouseOver(double Range, double bbExpand, float f) {
	        Entity renderViewEntity = mc.getRenderViewEntity();
	        Entity entity = null;
	        if (renderViewEntity == null || Reach.mc.theWorld == null) {
	            return null;
	        }
	        Reach.mc.mcProfiler.startSection("pick");
	        Vec3 positionEyes = renderViewEntity.getPositionEyes(0.0f);
	        Vec3 renderViewEntityLook = renderViewEntity.getLook(0.0f);
	        Vec3 vector = positionEyes.addVector(renderViewEntityLook.xCoord * Range, renderViewEntityLook.yCoord * Range, renderViewEntityLook.zCoord * Range);
	        Vec3 ve = null;
	        List entitiesWithinAABB = Reach.mc.theWorld.getEntitiesWithinAABBExcludingEntity(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(renderViewEntityLook.xCoord * Range, renderViewEntityLook.yCoord * Range, renderViewEntityLook.zCoord * Range).expand(1.0, 1.0, 1.0));
	        double range = Range;
	        for (int i = 0; i < entitiesWithinAABB.size(); ++i) {
	            double v;
	            Entity e = (Entity)entitiesWithinAABB.get(i);
	            if (!e.canBeCollidedWith()) continue;
	            float size = e.getCollisionBorderSize();
	            AxisAlignedBB bb = e.getEntityBoundingBox().expand((double)size, (double)size, (double)size);
	            bb = bb.expand(bbExpand, bbExpand, bbExpand);
	            MovingObjectPosition objectPosition = bb.calculateIntercept(positionEyes, vector);
	            if (bb.isVecInside(positionEyes)) {
	                if (!(0.0 < range) && range != 0.0) continue;
	                entity = e;
	                ve = objectPosition == null ? positionEyes : objectPosition.hitVec;
	                range = 0.0;
	                continue;
	            }
	            if (objectPosition == null || !((v = positionEyes.distanceTo(objectPosition.hitVec)) < range) && range != 0.0) continue;
	            boolean b = false;
	            if (e == renderViewEntity.ridingEntity) {
	                if (range != 0.0) continue;
	                entity = e;
	                ve = objectPosition.hitVec;
	                continue;
	            }
	            entity = e;
	            ve = objectPosition.hitVec;
	            range = v;
	        }
	        if (range < Range && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
	            entity = null;
	        }
	        Reach.mc.mcProfiler.endSection();
	        if (entity == null || ve == null) {
	            return null;
	        }
	        return new Object[]{entity, ve};
	    }

	
	
//	@Override
//	public void onClientTick(ClientTickEvent event) {
//		 if(mc.mcProfiler!=null){
//
//	            try {
//	                Field field = ReflectionHelper.findField(Minecraft.class, new String[]{"mcProfiler", "field_71424_I"});
//	                if (!field.isAccessible()) {
//	                    field.setAccessible(true);
//	                }
//	              net.minecraft.profiler.Profiler  profiler = (   net.minecraft.profiler.Profiler) field.get(mc);
//	                if(!(profiler instanceof ProfilerHook)){
//	                    field.set(mc,(net.minecraft.profiler.Profiler)new ProfilerHook(profiler));
//	                }
//
//	            }catch (Exception e){
//	                e.printStackTrace();
//
//	            }
//	        }
//		super.onClientTick(event);
//	}
//	
//	public static void onStartSection(String name) {
//		 if(name.equals("gameMode")){
//	       
//	            getMouseOver(1.0f);
//	        }
//		
//	}
//	public static void onEndSection(String lastSection) {
//		// TODO �Զ����ɵķ������
//		
//	}
//	public static void onEndStartSection(String lastSection) {
//		// TODO �Զ����ɵķ������
//		
//	}
//	 public static void getMouseOver(float partialTicks)
//	    {
//	        Entity entity = mc.getRenderViewEntity();
//	        Entity pointedEntity=null;
//
//	        if (entity != null)
//	        {
//	            if (mc.theWorld != null)
//	            {
//	               mc.mcProfiler.startSection("pick");
//	                mc.pointedEntity = null;
//
//	                //(double)this.mc.playerController.getBlockReachDistance();
//	                double d0 = reachValue.getValue();
//	               mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
//	                double d1 = d0;
//	                Vec3 vec3 = entity.getPositionEyes(partialTicks);
//	                boolean flag = false;
//	                int i = 3;
//
//	                if (mc.playerController.extendedReach())
//	                {
//	                    d0 = 6.0D;
//	                    d1 = 6.0D;
//	                }
//	                else
//	                {
//	                    if (d0 > reachValue.getValue())
//	                    {
//	                        flag = true;
//	                    }
//	                }
//
//	                if (mc.objectMouseOver != null)
//	                {
//	                    d1 = mc.objectMouseOver.hitVec.distanceTo(vec3);
//	                }
//
//	                Vec3 vec31 = entity.getLook(partialTicks);
//	                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
//	                pointedEntity = null;
//	                Vec3 vec33 = null;
//	                float f = 1.0F;
//	                List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
//	                {
//	                    public boolean apply(Entity p_apply_1_)
//	                    {
//	                        return p_apply_1_.canBeCollidedWith();
//	                    }
//	                }));
//	                double d2 = d1;
//
//	                for (int j = 0; j < list.size(); ++j)
//	                {
//	                    Entity entity1 = (Entity)list.get(j);
//	                    float f1 = entity1.getCollisionBorderSize();
//	                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
//	                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
//
//	                    if (axisalignedbb.isVecInside(vec3))
//	                    {
//	                        if (d2 >= 0.0D)
//	                        {
//	                           pointedEntity = entity1;
//	                            vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
//	                            d2 = 0.0D;
//	                        }
//	                    }
//	                    else if (movingobjectposition != null)
//	                    {
//	                        double d3 = vec3.distanceTo(movingobjectposition.hitVec);
//
//	                        if (d3 < d2 || d2 == 0.0D)
//	                        {
//	                            if (entity1 == entity.ridingEntity && !entity.canRiderInteract())
//	                            {
//	                                if (d2 == 0.0D)
//	                                {
//	                                   pointedEntity = entity1;
//	                                    vec33 = movingobjectposition.hitVec;
//	                                }
//	                            }
//	                            else
//	                            {
//	                                pointedEntity = entity1;
//	                                vec33 = movingobjectposition.hitVec;
//	                                d2 = d3;
//	                            }
//	                        }
//	                    }
//	                }
//
//	                if (pointedEntity != null && flag && vec3.distanceTo(vec33) > reachValue.getValue())
//	                {
//	                    pointedEntity = null;
//	                    mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
//	                }
//
//	                if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
//	                {
//	                    mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
//
//	                    if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame)
//	                    {
//	                        mc.pointedEntity = pointedEntity;
//	                    }
//	                }
//	                ReflectionHelper.setPrivateValue(EntityRenderer.class,mc.entityRenderer,pointedEntity,new String[]{"pointedEntity","field_78528_u"});
//	               // this.mc.mcProfiler.endSection();
//	            }
//	        }
//	    }
//	
	
}
