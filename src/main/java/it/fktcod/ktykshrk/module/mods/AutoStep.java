package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventStep;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import java.util.Arrays;
import java.util.List;

public class AutoStep extends Module{


	public ModeValue mode;
	public NumberValue height;
	public NumberValue delay;
	public float tempHeight;
	public int ticks = 0;
	public TimerUtils timer=new TimerUtils();

	public static boolean state;
	public static boolean reset;

	public AutoStep() {
		super("AutoStep", HackCategory.PLAYER);

		this.mode = new ModeValue("Mode", new Mode("Simple", true), new Mode("AAC", false),new Mode("NCP", false),new Mode("AAC2",false));
		delay=new NumberValue("Delay", 0D, 0D, 1000D);
		height = new NumberValue("Height", 0.5D, 0D, 10D);

		this.addValue(mode, height,delay);
		this.setChinese(Core.Translate_CN[18]);
	}

	@Override
    public String getDescription() {
        return "Allows you to walk on value blocks height.";
    }

	@Override
	public void onEnable() {
		ticks = 0;
		reset=false;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		Wrapper.INSTANCE.player().stepHeight = 0.5f;
		Wrapper.INSTANCE.timer.timerSpeed=1;
		super.onDisable();

	}

	@Override
	public void onEventStep(EventStep event) {

		if (event.getEventType() == EventType.PRE) {
			if (this.reset) {
				boolean resetTimer;
				if (!this.reset) {
					resetTimer = true;
				} else {
					resetTimer = false;
				}
				this.reset = resetTimer;
				Wrapper.timer.timerSpeed = 1.0f;
			}
			if (!this.mc.thePlayer.onGround || !timer.isDelay(this.delay.getValue().longValue())) {
				event.setHeight(this.mc.thePlayer.stepHeight = 0.5f);
				return;
			}
			this.mc.thePlayer.stepHeight = this.height.getValue().floatValue();
			event.setHeight(this.height.getValue().floatValue());
		}
		if (event.getEventType() == EventType.POST) {
			if (event.getHeight() > 0.5) {
				final double n = this.mc.thePlayer.getEntityBoundingBox().minY - this.mc.thePlayer.posY;
				if (n >= 0.625) {
					final float n2 = 0.6f;
					float n3;
					if (n >= 1.0) {
						n3 = Math.abs(1.0f - (float) n) * 0.33f;
					} else {
						n3 = 0.0f;
					}
					Wrapper.timer.timerSpeed = n2 - n3;
					if (Wrapper.timer.timerSpeed <= 0.05f) {
						Wrapper.timer.timerSpeed = 0.05f;
					}
					this.reset = true;
					if(mode.getMode("NCP").isToggled()) {
						this.ncpStep(n);
					}else if(mode.getMode("AAC2").isToggled()){
						this.aacStep(n);
					}
					this.timer.reset();
				}
			}

	}


		super.onEventStep(event);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Wrapper.timer.timerSpeed<1&&Wrapper.INSTANCE.player().onGround) {
			Wrapper.timer.timerSpeed=1;
		}

		if(mode.getMode("AAC").isToggled()) {
			EntityPlayerSP player = Wrapper.INSTANCE.player();
			if(player.isCollidedHorizontally) {
				switch(ticks) {
					case 0:
					if(player.onGround)
						player.jump();
						break;
					case 7:
						player.motionY = 0;
						break;
					case 8:
					if(!player.onGround)
						player.setPosition(player.posX, player.posY + 1, player.posZ);
						break;
				}
				ticks++;
			} else {
				ticks = 0;
			}
		} else if(mode.getMode("Simple").isToggled()) {
			Wrapper.INSTANCE.player().stepHeight = height.getValue().floatValue();
		}

		super.onClientTick(event);
	}

	 void ncpStep(final double n) {
	        final double posX = this.mc.thePlayer.posX;
	        final double posZ = this.mc.thePlayer.posZ;
	        double posY = this.mc.thePlayer.posY;
	        if (n <= 1.0D) {
	            double offset = height.getValue();

	            if (n < 1.1) {
	                final double[] array = {0.42D * offset, 0.7532D * offset};
	                final int length = array.length;
	                int j = 0;
	                while (j < length) {
	                    this.mc.thePlayer.sendQueue.addToSendQueue(
	                            new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
	                    ++j;
	                }
	            } else if (n < 1.1) {
	                final double[] array = {0.42D, 0.7532D, 1.001D};
	                final int length = array.length;
	                int j = 0;
	                while (j < length) {
	                    this.mc.thePlayer.sendQueue.addToSendQueue(
	                            new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
	                    ++j;
	                }
	            } else if (n < 1.6) {
	                final double[] array = {0.42D, 0.7532D, 1.001D, 1.084D, 1.006D};
	                final int length = array.length;
	                int j = 0;
	                while (j < length) {
	                    this.mc.thePlayer.sendQueue.addToSendQueue(
	                            new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
	                    ++j;
	                }
	            } else if (n < 2.1) {
	                final double[] array = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
	                final int length = array.length;
	                int j = 0;
	                while (j < length) {
	                    this.mc.thePlayer.sendQueue.addToSendQueue(
	                            new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
	                    ++j;
	                }
	            } else {
	                final double[] array2 = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
	                final int length2 = array2.length;
	                int k = 0;
	                while (k < length2) {
	                    this.mc.thePlayer.sendQueue.addToSendQueue(
	                            new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array2[k], posZ, false));
	                    ++k;
	                }
	            }
	        }
	    }

	void aacStep(double height)
	{
		double posX = mc.thePlayer.posX;
		double posY = mc.thePlayer.posY;
		double posZ = mc.thePlayer.posZ;

		if (height < 1.1)
		{
			double first = 0.42;
			double second = 0.75;

			if (height > 1)
			{
				first *= height;
				second *= height;

				if (first > 0.4349)
				{
					first = 0.4349;
				}
				else if (first < 0.405)
				{
					first = 0.405;
				}
			}

			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + first, posZ, false));

			if (posY + second < posY + height)
			{
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + second, posZ, false));
			}

			return;
		}

		List<Double> offset = Arrays.asList(0.434999999999998, 0.360899999999992, 0.290241999999991, 0.220997159999987, 0.13786084000003104, 0.055);
		double y = mc.thePlayer.posY;

		for (int i = 0; i < offset.size(); i++)
		{
			double off = offset.get(i);
			y += off;

			if (y > mc.thePlayer.posY + height)
			{
				double x = mc.thePlayer.posX;
				double z = mc.thePlayer.posZ;
				double forward = mc.thePlayer.movementInput.moveForward;
				double strafe = mc.thePlayer.movementInput.moveStrafe;
				float YAW = mc.thePlayer.rotationYaw;
				double speed = 0.3;

				if (forward != 0 && strafe != 0)
				{
					speed -= 0.09;
				}

				x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) * 1;
				z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) * 1;
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						x, y, z, false));
				break;
			}

			if (i == offset.size() - 1)
			{
				double x = mc.thePlayer.posX;
				double z = mc.thePlayer.posZ;
				double forward = mc.thePlayer.movementInput.moveForward;
				double strafe = mc.thePlayer.movementInput.moveStrafe;
				float YAW = mc.thePlayer.rotationYaw;
				double speed = 0.3;

				if (forward != 0 && strafe != 0)
				{
					speed -= 0.09;
				}

				x += (forward * speed * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(YAW + 90.0f))) * 1;
				z += (forward * speed * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(YAW + 90.0f))) * 1;
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						x, y, z, false));
			}
			else
			{
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
			}
		}
	}

}
