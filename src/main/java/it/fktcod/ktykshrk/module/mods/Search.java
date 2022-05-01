/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C13PacketPlayerAbilities
 *  net.minecraft.potion.Potion
 */
package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Nan0EventRegister;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.stream.Collectors;

public class Search extends Module {
    
    public static List<BlockPos> toRender = new ArrayList<>();

    public EBlockPos pos =new EBlockPos();

    public BooleanValue dia = new BooleanValue("Diamond", true);
    public BooleanValue gold = new BooleanValue("Gold",true);
    public BooleanValue iron = new BooleanValue("Iron",true);
    public BooleanValue lapis = new BooleanValue("Lapis",true);
    public BooleanValue emerald = new BooleanValue("Emerald",true);
    public BooleanValue coal = new BooleanValue("Coal",true);
    public BooleanValue redstone = new BooleanValue("Redstone",true);
    public BooleanValue bypass = new BooleanValue("Bypass",true);
    public BooleanValue limitEnabled = new BooleanValue("RenderLimitEnabled",true);
    public BooleanValue radiusOn = new BooleanValue("RadiusEnabled",true);
    
    public NumberValue depth = new NumberValue("Depth",2d, 1d, 5d);
    public NumberValue limit = new NumberValue("RenderLimit",10d, 5d, 100d);
    public NumberValue refresh_timer = new NumberValue("RefreshDelay",5d, 0d, 50d);
    public NumberValue alpha = new NumberValue("Alpha",0.25d, 0d, 1d);
    public NumberValue width = new NumberValue("LineWidth",2.5d, 1d, 10d);
    public NumberValue radius = new NumberValue("Radius",10d, 5d, 100d);




    private final Minecraft mc = Minecraft.getMinecraft();
    private final TimerUtils refresh = new TimerUtils();
    
    public Search() {
        super("Search", HackCategory.VISUAL);
        addValue(dia);
        addValue(gold);
        addValue(iron);
        addValue(lapis);
        addValue(emerald);
        addValue(coal);
        addValue(redstone);
        addValue(bypass);
        addValue(depth);
        addValue(radiusOn);
        addValue(radius);
        addValue(limitEnabled);
        addValue(limit);
        addValue(refresh_timer);
        addValue(alpha);
        addValue(width);
        //this.setChinese(Drift.Translate_CN[22]);
    }


    @Override
    public void onEnable() {
        toRender.clear();
        refresh.reset();
        mc.renderGlobal.loadRenderers();

    }

   // int i=0;
   @Override
    public void onRenderWorldLast(RenderWorldLastEvent event)  {

       for (BlockPos blockPos : toRender) {
           renderBlock(blockPos);
       }

        if (refresh.isDelayComplete(refresh_timer.getValue())) {
            final WorldClient world = mc.theWorld;
            final EntityPlayerSP player = mc.thePlayer;

            if (world != null && player != null) {
               // this.blockList.clear();

                final int sx = (int) player.posX - this.radius.getValue().intValue();
                final int sz = (int) player.posZ - this.radius.getValue().intValue();
                final int endX = (int) player.posX + this.radius.getValue().intValue();
                final int endZ = (int) player.posZ + this.radius.getValue().intValue();

                Chunk chunk;
                IBlockState blockState;
                Block block;
                //int blockId;
               // byte damage;

                for (int x = sx; x <= endX; x++) {
                    this.pos.setX(x);
                    for (int z = sz; z <= endZ; z++) {

                        chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);

                        if(!chunk.isLoaded()) {
                            continue;
                        }

                        this.pos.setZ(z);

                        for (int y = 0; y <= 255; y++) {
                            this.pos.setY(y);
                            blockState = chunk.getBlockState(this.pos);
                            block = blockState.getBlock();

                            if (block != Blocks.air) {
                                //blockId = this.blockRegistery.getId(block);
                              //  damage = (byte) block.getMetaFromState(blockState);
                              //  xrayBlock = XrayBlock.find(blockId, damage);

                               // if (xrayBlock != null) {
                                   // if(this.antiAntiXrayLevel == 0 || antiAntiXray(x, y, z, world)) {
                                BlockPos poss = new BlockPos(x, y, z);

                                if (!toRender.contains(poss)){
                                    if (test(poss)){
                                        if (!(toRender.size() > limit.getValue()) || !limitEnabled.getValue()){
                                            toRender.add(poss);
                                        }
                                        //else
                                       // {
                                         //   chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);

                                        //    if(!chunk.isLoaded()&&toRender.size() > limit.getValue()) {
                                        //        toRender.clear();
                                        //    }
                                      //  }
                                    }

                                }

                                    //}
                                //}
                            }
                        }
                    }
                }

            
            List<BlockPos> list = toRender;
            list = list.stream().filter(this::test).collect(Collectors.toList());
            toRender = list;
            refresh.reset();
               // i++;
        }
      }
    }

    @Override
    public void onRender3D(RenderBlockOverlayEvent event) {

       // BlockPos pos =event.blockPos; //new BlockPos(event.x, event.y, event.z);

        if (!toRender.contains(pos))
            if (test(pos))
                if (!(toRender.size() > limit.getValue()) || !limitEnabled.getValue())
                    toRender.add(pos);

    }
    public boolean isTarget(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return dia.getValue();
        } else if (Blocks.lapis_ore.equals(block)) {
            return lapis.getValue();
        } else if (Blocks.iron_ore.equals(block)) {
            return iron.getValue();
        } else if (Blocks.gold_ore.equals(block)) {
            return gold.getValue();
        } else if (Blocks.coal_ore.equals(block)) {
            return coal.getValue();
        } else if (Blocks.emerald_ore.equals(block)) {
            return emerald.getValue();
        } else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return redstone.getValue();
        }
        return false;
    }

    private Boolean oreTest(BlockPos origPos, Double depth) {
        Collection<BlockPos> posesNew = new ArrayList<>();
        Collection<BlockPos> posesLast = new ArrayList<>(Collections.singletonList(origPos));
        Collection<BlockPos> finalList = new ArrayList<>();
        for (int i = 0; i < depth; i++) {
            for (BlockPos blockPos : posesLast) {
                posesNew.add(blockPos.up());
                posesNew.add(blockPos.down());
                posesNew.add(blockPos.north());
                posesNew.add(blockPos.south());
                posesNew.add(blockPos.west());
                posesNew.add(blockPos.east());
            }
            for (BlockPos pos : posesNew) {
                if (posesLast.contains(pos)) {
                    posesNew.remove(pos);
                }
            }
            posesLast = posesNew;
            finalList.addAll(posesNew);
            posesNew = new ArrayList<>();
        }

        List<Block> legitBlocks = Arrays.asList(Blocks.water, Blocks.lava, Blocks.flowing_lava, Blocks.air,
                Blocks.flowing_water , Blocks.fire );

        return finalList.stream()
                .anyMatch(blockPos -> legitBlocks.contains(mc.theWorld.getBlockState(blockPos).getBlock()));
    }

    public boolean test(BlockPos pos1) {
        if (!isTarget(pos1)) {
            return false;
        }
        if (bypass.getValue()) {
            if (!oreTest(pos1, (double)depth.getValue())) {
                return false;
            }
        }
        if (radiusOn.getValue()) {
            return !(mc.thePlayer.getDistance(pos1.getX(), pos1.getY(), pos1.getZ()) >= radius.getValue());
        }
        return true;
    }

    private void renderBlock(BlockPos pos) {
        
        //double x = (double) pos.getX() - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
       // double y = (double) pos.getY() - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
       // double z = (double) pos.getZ() - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
        
        double x = (double)  pos.getX() - Wrapper.getRenderPosX();;
        double y = (double)  pos.getY() - Wrapper.getRenderPosY();
        double z = (double)  pos.getZ() - Wrapper.getRenderPosZ();
        
        
        final float[] color = getColor(pos);
        drawOutlinedBlockESP(x, y, z, color[0], color[1], color[2],alpha.getValue().floatValue(), width.getValue().floatValue());
    }


    public static float[] getColor(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return new float[]{0, 1, 1};
        } else if (Blocks.lapis_ore.equals(block)) {
            return new float[]{0, 0, 1};
        } else if (Blocks.iron_ore.equals(block)) {
            return new float[]{1, 1, 1};
        } else if (Blocks.gold_ore.equals(block)) {
            return new float[]{1, 1, 0};
        } else if (Blocks.coal_ore.equals(block)) {
            return new float[]{0, 0, 0};
        } else if (Blocks.emerald_ore.equals(block)) {
            return new float[]{0, 1, 0};
        } else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return new float[]{1, 0, 0};
        }
        return new float[]{0, 0, 0};
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue,
                                            float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
/*
    private boolean antiAntiXray(final int x, final int y, final int z, final WorldClient world) {
        return showBlock(world, x, y, z) ||
                showBlock(world, x + 1, y, z) ||
                showBlock(world, x - 1, y, z) ||
                showBlock(world, x, y + 1, z) ||
                showBlock(world, x, y - 1, z) ||
                showBlock(world, x, y, z + 1) ||
                showBlock(world, x, y, z - 1);
    }

    private boolean showBlock(final WorldClient world, final int x, final int y, final int z) {
        final Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if(!chunk.isLoaded()) {
            return false;
        }

      //  this.pos2.set(x, y, z);
       // final Block block = chunk.getBlockState(this.pos2).getBlock();

      //  return !block.isNormalCube();
    }*/

}

class EBlockPos extends BlockPos {
    private int x;
    private int y;
    private int z;

    public EBlockPos() {
        super(0, 0, 0);
    }

    public void set(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public void setZ(final int z) {
        this.z = z;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }
}

