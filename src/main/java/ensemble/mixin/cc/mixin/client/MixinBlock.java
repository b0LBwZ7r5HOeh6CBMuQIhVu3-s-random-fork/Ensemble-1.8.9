package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;





	import net.minecraft.block.Block;
	import net.minecraft.block.material.Material;
	import net.minecraft.block.state.BlockState;
	import net.minecraft.block.state.IBlockState;
	import net.minecraft.enchantment.EnchantmentHelper;
	import net.minecraft.entity.Entity;
	import net.minecraft.entity.EntityLivingBase;
	import net.minecraft.entity.player.EntityPlayer;
	import net.minecraft.util.AxisAlignedBB;
	import net.minecraft.util.BlockPos;
	import net.minecraft.util.EnumFacing;
	import net.minecraft.world.World;
	import org.spongepowered.asm.mixin.Final;
	import org.spongepowered.asm.mixin.Mixin;
	import org.spongepowered.asm.mixin.Overwrite;
	import org.spongepowered.asm.mixin.Shadow;
	import org.spongepowered.asm.mixin.injection.At;
	import org.spongepowered.asm.mixin.injection.Inject;
	import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.fktcod.ktykshrk.event.EventBlockBB;
import it.fktcod.ktykshrk.eventapi.EventManager;
import it.fktcod.ktykshrk.eventapi.events.Event;

import java.util.List;

	@Mixin(Block.class)
	public abstract class MixinBlock {

	    @Shadow
	    public abstract AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state);

	    @Shadow
	    @Final
	    protected BlockState blockState;

	    @Shadow
	    public abstract void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ);

	    // Has to be implemented since a non-virtual call on an abstract method is illegal
	    @Shadow
	    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
	        return null;
	    }

	    /**
	     * @author CCBlueX
	     */
	    @Overwrite
	    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
	        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);
	        EventBlockBB blockBBEvent=new EventBlockBB(blockState.getBlock(), pos, axisalignedbb);
	        EventManager.call(blockBBEvent);
	        
	        axisalignedbb = blockBBEvent.getBoundingBox();
	        if(axisalignedbb != null && mask.intersectsWith(axisalignedbb))
	            list.add(axisalignedbb);
	    }
	    
	    
}
