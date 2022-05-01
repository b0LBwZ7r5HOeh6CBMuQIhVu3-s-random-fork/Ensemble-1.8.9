package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.opengl.GL11;

import it.fktcod.ktykshrk.managers.ColorManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ChestESP extends Module {
	private int maxChests = 50;
	public boolean shouldInform = true;
	private TileEntityChest openChest;
	private ArrayDeque<TileEntityChest> emptyChests = new ArrayDeque<TileEntityChest>();
	private ArrayDeque<TileEntityChest> nonEmptyChests = new ArrayDeque<TileEntityChest>();
	private String[] chestClasses = new String[] {
			"TileEntityIronChest",
			"TileEntityGoldChest",
			"TileEntityDiamondChest",
			"TileEntityCopperChest",
			"TileEntitySilverChest",
			"TileEntityCrystalChest",
			"TileEntityObsidianChest",
			"TileEntityDirtChest"
			};
	private boolean shouldRenderIronChest = true;
	
	
	public ChestESP() {

	    super("ChestESP", HackCategory.VISUAL);
        this.setChinese(Core.Translate_CN[26]);
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		 for (final Object o : mc.theWorld.loadedTileEntityList)
	        {
	            if (o instanceof TileEntityChest)
	            {
	                final TileEntityLockable storage = (TileEntityLockable) o;
	                this.drawESPOnStorage(storage, storage.getPos().getX(), storage.getPos().getY(), storage.getPos().getZ());
	            }
	        }
		super.onRenderWorldLast(event);
	}
	
	@Override
    public String getDescription() {
        return "Allows you to see all of the chests around you.";
    }
	
	@Override
	public void onEnable() {
		shouldInform = true;
		emptyChests.clear();
		nonEmptyChests.clear();
		super.onEnable();
	}
	
	
	public void drawESPOnStorage(TileEntityLockable storage, double x, double y, double z)
    {
        assert !storage.isLocked();
        //20 years of java experience service really helped me, I'm glad we bonded :)
        TileEntityChest chest = (TileEntityChest) storage;
        Vec3 vec = new Vec3(0, 0, 0);
        Vec3 vec2 = new Vec3(0, 0, 0);

        if (chest.adjacentChestZNeg != null)
        {
            vec = new Vec3(x + 0.0625, y, z - 0.9375);
            vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
        }
        else if (chest.adjacentChestXNeg != null)
        {
            vec = new Vec3(x + 0.9375, y, z + 0.0625);
            vec2 = new Vec3(x - 0.9375, y + 0.875, z + 0.9375);
        }
        else if (chest.adjacentChestXPos == null && chest.adjacentChestZPos == null)
        {
            vec = new Vec3(x + 0.0625, y, z + 0.0625);
            vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
        }
        else
        {
            return;
        }

        GL11.glPushMatrix();
        RenderUtils.pre3D();
      //  mc.entityRenderer.setupCameraTransform(timer.renderPartialTicks, 2);
        
        
        try {
			Method method = ReflectionHelper.findMethod(EntityRenderer.class, mc.entityRenderer, new String[] { "setupCameraTransform", "func_78479_a" }, float.class, int.class);
			method.invoke(mc.entityRenderer, new Object[] { new Float(timer.renderPartialTicks), new Integer(2) });
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (chest.getChestType() == 1)
        {
            GL11.glColor4d(0.7, 0.1, 0.1, 0.5);
        }
        else
        {
            GL11.glColor4d(((double)ColorManager.esp.getRed()) / 255,
                           ((double)ColorManager.esp.getGreen()) / 255,
                           ((double)ColorManager.esp.getBlue()) / 255, ((double)ColorManager.esp.getAlpha()) / 255);
        }

    
        
        try {
            Field renderPosX = ReflectionHelper.findField(RenderManager.class,
    				new String[] { "renderPosX", "field_78725_b" });
            if (!renderPosX.isAccessible()) 
            renderPosX.setAccessible(true);
    		
    		Field renderPosY = ReflectionHelper.findField(RenderManager.class,
    				new String[] { "renderPosY", "field_78726_c" });
    		 if (!renderPosY.isAccessible()) 
    		 renderPosY.setAccessible(true);
    		
    		Field renderPosZ = ReflectionHelper.findField(RenderManager.class,
    				new String[] { "renderPosZ", "field_78723_d" });
    		 if (!renderPosZ.isAccessible()) 
    		 renderPosZ.setAccessible(true);
        	
			drawBoundingBox(new AxisAlignedBB(vec.xCoord - renderPosX.getDouble(mc.getRenderManager()), vec.yCoord -renderPosY.getDouble(mc.getRenderManager()), vec.zCoord - renderPosZ.getDouble(mc.getRenderManager()), vec2.xCoord - renderPosX.getDouble(mc.getRenderManager()), vec2.yCoord - renderPosY.getDouble(mc.getRenderManager()), vec2.zCoord -renderPosZ.getDouble(mc.getRenderManager())));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        RenderUtils.post3D();
        GL11.glPopMatrix();
    }
	
	public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }
}
