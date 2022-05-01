package ensemble.mixin.cc.mixin.client;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ensemble.mixin.cc.mixin.interfaces.IEntity;
import it.fktcod.ktykshrk.event.EventMove;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.mods.Scaffold;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mixin(Entity.class)
@SideOnly(Side.CLIENT)
public abstract class MixinEntity implements IEntity {

	@Shadow
	public float height;

	@Shadow
	public double posX;

	@Shadow
	public double posY;

	@Shadow
	public double posZ;

	@Shadow
	public abstract boolean isSprinting();

	@Shadow
	public float rotationPitch;

	@Shadow
	public float rotationYaw;

	@Shadow
	public abstract AxisAlignedBB getEntityBoundingBox();

	@Shadow
	public Entity ridingEntity;

	@Shadow
	public double motionX;

	@Shadow
	public double motionY;

	@Shadow
	public double motionZ;

	@Shadow
	public boolean onGround;

	@Shadow
	public boolean isAirBorne;

	@Shadow
	public boolean noClip;

	@Shadow
	public World worldObj;

	/*
	 * @Shadow public void moveEntity(double x, double y, double z) { }
	 */

	@Shadow
	public boolean isInWeb;

	@Shadow
	public float stepHeight;

	@Shadow
	public boolean isCollidedHorizontally;

	@Shadow
	public boolean isCollidedVertically;

	@Shadow
	public boolean isCollided;

	@Shadow
	public float distanceWalkedModified;

	@Shadow
	public float distanceWalkedOnStepModified;

	@Shadow
	public abstract boolean isInWater();

	@Shadow
	protected Random rand;

	@Shadow
	public int fireResistance;

	@Shadow
	protected boolean inPortal;

	@Shadow
	public int timeUntilPortal;

	@Shadow
	public float width;

	@Shadow
	public abstract boolean isRiding();

	@Shadow
	protected abstract void dealFireDamage(int amount);

	@Shadow
	public abstract boolean isWet();

	@Shadow
	public abstract void addEntityCrashInfo(CrashReportCategory category);

	@Shadow
	protected abstract void doBlockCollisions();

	@Shadow
	protected abstract void playStepSound(BlockPos pos, Block blockIn);

	@Shadow
	public abstract void setEntityBoundingBox(AxisAlignedBB bb);

	@Shadow
	public float prevRotationPitch;

	@Shadow
	public float prevRotationYaw;

	@Shadow
	public abstract UUID getUniqueID();

	@Shadow
	public abstract boolean isSneaking();

	@Shadow
	public abstract boolean isInsideOfMaterial(Material materialIn);

	@Shadow
	public abstract void resetPositionToBB();

	@Shadow
	protected abstract void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos);

	@Shadow
	protected abstract boolean canTriggerWalking();

	@Shadow
	public abstract void playSound(String name, float volume, float pitch);

	@Shadow
	protected abstract String getSwimSound();

	@Shadow
	private int nextStepDistance;

	@Override
	public int getNextStepDistance() {
		return nextStepDistance;
	}

	@Shadow
	public abstract Vec3 getVectorForRotation(float pitch, float yaw);

	@Shadow
	private int fire;

	@Override
	public void setNextStepDistance(int distance) {
		nextStepDistance = distance;
	}

	@Shadow
	private AxisAlignedBB boundingBox;

	@Override
	public int getFire() {
		return fire;
	}

	@Override
	public void setFire(int i) {
		fire = i;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public boolean canEntityBeSeenFixed(Entity entityIn) {
		return this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + (double) height * 0.85, this.posZ),
				new Vec3(entityIn.posX, entityIn.posY + (double) entityIn.getEyeHeight(), entityIn.posZ)) == null
				|| this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + (double) height * 0.85, this.posZ),
						new Vec3(entityIn.posX, entityIn.posY, entityIn.posZ)) == null;
	}

}