package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.input.Mouse;

import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.PlayerControllerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Teleport extends Module{
	
	public ModeValue mode;
	public NumberValue reach;
	public BooleanValue math;
	public boolean passPacket = false;
    private BlockPos teleportPosition = null;
    private boolean canDraw;
    private int delay;
    
	public Teleport() {
		super("Teleport", HackCategory.MOVEMENT);
		
		this.mode = new ModeValue("Mode", new Mode("Command", false),new Mode("Reach", true), new Mode("Flight", false));
		this.math = new BooleanValue("Math", false);
		this.reach = new NumberValue("Reach", 10.0D, 1.0D, 100D);
		this.addValue(mode, math,reach);
		this.setChinese(Core.Translate_CN[94]);
	}
	
	@Override
	public String getDescription() {
		return "Teleports you on selected block.";
	}
	
	@Override
	public void onEnable() {
		if(mode.getMode("Reach").isToggled()) {
			
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(mode.getMode("Flight").isToggled()) {
			Wrapper.INSTANCE.player().noClip = false;
			passPacket = false;
			teleportPosition = null;
			return;
		}
		canDraw = false;
		PlayerControllerUtils.setReach(Wrapper.INSTANCE.player(), reach.getValue());
		super.onDisable();
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if(side == Side.OUT && mode.getMode("Flight").isToggled()) {
			if(packet instanceof C03PacketPlayer
                || packet instanceof C03PacketPlayer
                || packet instanceof C03PacketPlayer.C05PacketPlayerLook
                || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
				return passPacket;
			}
		}
		return true;
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(mode.getMode("Flight").isToggled()) {
			MovingObjectPosition object = Wrapper.INSTANCE.mc().objectMouseOver;
			if(object == null) {
				return;
			}
			EntityPlayerSP player = Wrapper.INSTANCE.player();
			GameSettings settings = Wrapper.INSTANCE.mcSettings();
			if(!passPacket) {
		 		if(settings.keyBindAttack.isKeyDown() && object.typeOfHit == MovingObjectType.BLOCK) {
		 			if(BlockUtils.isBlockMaterial(object.getBlockPos(), Blocks.air)) {
		 				return;
		 			}
		 			teleportPosition = object.getBlockPos();
					passPacket = true;
				}
				return;
			}
			player.noClip = false;
			if(settings.keyBindSneak.isKeyDown() && player.onGround) {
				if(math.getValue()) {
		            double[] playerPosition = new double[]{Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ};
		            double[] blockPosition = new double[]{teleportPosition.getX() + 0.5F, teleportPosition.getY() + getOffset(BlockUtils.getBlock(teleportPosition), teleportPosition) + 1.0F, teleportPosition.getZ() + 0.5F};

		            Utils.teleportToPosition(playerPosition, blockPosition, 0.25D, 0.0D, true, true);
		            Wrapper.INSTANCE.player().setPosition(blockPosition[0], blockPosition[1], blockPosition[2]);

		            teleportPosition = null;
				} else {
		            double x = teleportPosition.getX();
		            double y = teleportPosition.getY() + 1;
		            double z = teleportPosition.getZ();
		            
		            Wrapper.INSTANCE.player().setPosition(x, y, z);
		            for(int i = 0; i < 1; i++) {
		            	Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, Wrapper.INSTANCE.player().onGround));
		            }
				}
	            
			}
			return;
		}
		 if ((!Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().inGameHasFocus || !Wrapper.INSTANCE.mc().inGameHasFocus) && Wrapper.INSTANCE.player().getItemInUseCount() == 0) {
			 PlayerControllerUtils.setReach(Wrapper.INSTANCE.player(), 100.0);
             canDraw = true;
         } else {
             canDraw = false;
             PlayerControllerUtils.setReach(Wrapper.INSTANCE.player(), reach.getValue());
         }
		if (teleportPosition != null && delay == 0 && Mouse.isButtonDown(1)) {
			if(math.getValue()) {
	            double[] playerPosition = new double[]{Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ};
	            double[] blockPosition = new double[]{teleportPosition.getX() + 0.5F, teleportPosition.getY() + getOffset(BlockUtils.getBlock(teleportPosition), teleportPosition) + 1.0F, teleportPosition.getZ() + 0.5F};

	            Utils.teleportToPosition(playerPosition, blockPosition, 0.25D, 0.0D, true, true);
	            Wrapper.INSTANCE.player().setPosition(blockPosition[0], blockPosition[1], blockPosition[2]);

	            teleportPosition = null;
			} else {
	            double x = teleportPosition.getX();
	            double y = teleportPosition.getY() + 1;
	            double z = teleportPosition.getZ();
	            
	            Wrapper.INSTANCE.player().setPosition(x, y, z);
	            for(int i = 0; i < 1; i++) {
	            	Wrapper.INSTANCE.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, Wrapper.INSTANCE.player().onGround));
	            }
			}
            delay = 5;
        }

        if (delay > 0) {
            delay--;
        }
		super.onClientTick(event);
	}
	
	@Override
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(!mode.getMode("Flight").isToggled()) {
			return;
		}
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		GameSettings settings = Wrapper.INSTANCE.mcSettings();
		if(!passPacket) {
			player.noClip = true;
			player.fallDistance = 0;
			player.onGround = true;
			player.capabilities.isFlying = false;
			player.motionX = 0.0F;
			player.motionY = 0.0F;
			player.motionZ = 0.0F;
			float speed = 0.5f;
			if(settings.keyBindJump.isKeyDown()) {
				player.motionY += speed;
			}
			if(settings.keyBindSneak.isKeyDown()) {
				player.motionY -= speed;
			}
			double d5 = player.rotationPitch + 90F;
	 		double d7 = player.rotationYaw + 90F;
	 		boolean flag4 = settings.keyBindForward.isKeyDown();
	 		boolean flag6 = settings.keyBindBack.isKeyDown();
	 		boolean flag8 = settings.keyBindLeft.isKeyDown();
	 		boolean flag10 = settings.keyBindRight.isKeyDown();
	 		if (flag4) {
	 			if (flag8) {
	 				d7 -= 45D;
	 			} else if (flag10) {
	 				d7 += 45D;
	 			}
	 		} else if (flag6) {
	 			d7 += 180D;
	 			if (flag8) {
	 				d7 += 45D;
	 			} else if (flag10) {
	 				d7 -= 45D;
	 			}
	 		} else if (flag8) {
	 			d7 -= 90D;
	 		} else if (flag10) {
	 			d7 += 90D;
	 		}
	 		if (flag4 || flag8 || flag6 || flag10) {
	 			player.motionX = Math.cos(Math.toRadians(d7));
	 			player.motionZ = Math.sin(Math.toRadians(d7));
	 		}
		}
		super.onLivingUpdate(event);
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if(mode.getMode("Flight").isToggled()) {
			if(teleportPosition == null) {
				return;
			}
			if(teleportPosition.getY() == new BlockPos(Wrapper.INSTANCE.player()).down().getY()) {
				RenderUtils.drawBlockESP(teleportPosition, 1F, 0F, 1F);
				return;
			}
			RenderUtils.drawBlockESP(teleportPosition, 1F, 0F, 0F);
			return;
		}
		MovingObjectPosition object = Wrapper.INSTANCE.mc().objectMouseOver;
		if(object == null) {
			return;
		}
		if (object.getBlockPos() != null && canDraw) {
            for (float offset = -2.0F; offset < 18.0F; offset++) {
                double[] mouseOverPos = new double[]{object.getBlockPos().getX(), object.getBlockPos().getY() + offset, object.getBlockPos().getZ()};

                BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
                Block blockBelow = BlockUtils.getBlock(blockBelowPos);

                if (canRenderBox(mouseOverPos)) {
                	RenderUtils.drawBlockESP(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]), 1F, 0F, 1F);
                    //RenderUtils.beginGl();
                    //drawBox(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                    //drawNametags(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                    //RenderUtils.endGl();

                    if (Wrapper.INSTANCE.mc().inGameHasFocus) {
                        teleportPosition = blockBelowPos;
                        break;
                    } else {
                        teleportPosition = null;
                    }
                }
            }
        } else if (object.entityHit != null) {
            for (float offset = -2.0F; offset < 18.0F; offset++) {
                double[] mouseOverPos = new double[]{object.entityHit.posX, object.entityHit.posY + offset,object.entityHit.posZ};

                BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
                Block blockBelow = BlockUtils.getBlock(blockBelowPos);

                if (canRenderBox(mouseOverPos)) {
                	RenderUtils.drawBlockESP(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]), 1F, 0F, 1F);
                    //RenderUtils.beginGl();
                    //drawBox(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                    //drawNametags(blockBelow, new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
                    //RenderUtils.endGl();

                	if (Wrapper.INSTANCE.mc().inGameHasFocus) {
                        teleportPosition = blockBelowPos;
                        break;
                    } else {
                        teleportPosition = null;
                    }
                }
            }
        } else {
            teleportPosition = null;
        }
		super.onRenderWorldLast(event);
	}
	
	public boolean canRenderBox(double[] mouseOverPos) {
        boolean canTeleport = false;

        Block blockBelowPos = BlockUtils.getBlock(new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2]));
        Block blockPos = BlockUtils.getBlock(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]));
        Block blockAbovePos = BlockUtils.getBlock(new BlockPos(mouseOverPos[0], mouseOverPos[1] + 1.0F, mouseOverPos[2]));

        boolean validBlockBelow = blockBelowPos.getCollisionBoundingBox(Wrapper.INSTANCE.world(),new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2]),
        		 BlockUtils.getState(
						new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0F, mouseOverPos[2]))) != null;

        
        boolean validBlock = isValidBlock(blockPos);
        boolean validBlockAbove = isValidBlock(blockAbovePos);

        if ((validBlockBelow && validBlock && validBlockAbove)) {
            canTeleport = true;
        }

        return canTeleport;
    }
	
	public double getOffset(Block block, BlockPos pos) {
        IBlockState state = BlockUtils.getState(pos);

        double offset = 0;

        if (block instanceof BlockSlab && !((BlockSlab) block).isDouble()) {
            offset -= 0.5F;
        } else if (block instanceof BlockEndPortalFrame) {
            offset -= 0.2F;
        } else if (block instanceof BlockBed) {
            offset -= 0.44F;
        } else if (block instanceof BlockCake) {
            offset -= 0.5F;
        } else if (block instanceof BlockDaylightDetector) {
            offset -= 0.625F;
        } else if (block instanceof BlockRedstoneComparator || block instanceof BlockRedstoneRepeater) {
            offset -= 0.875F;
        } else if (block instanceof BlockChest || block == Blocks.ender_chest) {
            offset -= 0.125F;
        } else if (block instanceof BlockLilyPad) {
            offset -= 0.95F;
        } else if (block == Blocks.snow_layer) {
            offset -= 0.875F;
            offset += 0.125F * ((Integer) state.getValue(BlockSnow.LAYERS) - 1);
        } else if (isValidBlock(block)) {
            offset -= 1.0F;
        }

        return offset;
    }
	
	public boolean isValidBlock(Block block) {
        return block == Blocks.portal 
        		|| block == Blocks.snow_layer 
        		|| block instanceof BlockTripWireHook 
        		|| block instanceof BlockTripWire 
        		|| block instanceof BlockDaylightDetector 
        		|| block instanceof BlockRedstoneComparator 
        		|| block instanceof BlockRedstoneRepeater 
        		|| block instanceof BlockSign 
        		|| block instanceof BlockAir
        		|| block instanceof BlockPressurePlate 
        		|| block instanceof BlockTallGrass 
        		|| block instanceof BlockFlower 
        		|| block instanceof BlockMushroom 
        		|| block instanceof BlockDoublePlant 
        		|| block instanceof BlockReed 
        		|| block instanceof BlockSapling 
        		|| block == Blocks.carrots 
        		|| block == Blocks.wheat
        		|| block == Blocks.nether_wart
        		|| block == Blocks.potatoes
        		|| block == Blocks.pumpkin_stem
        		|| block == Blocks.melon_stem
        		|| block == Blocks.heavy_weighted_pressure_plate
        		|| block == Blocks.light_weighted_pressure_plate
        		|| block == Blocks.redstone_wire
        		|| block instanceof BlockTorch 
        		|| block instanceof BlockRedstoneTorch 
        		|| block == Blocks.lever 
        		|| block instanceof BlockButton;
    }
}
