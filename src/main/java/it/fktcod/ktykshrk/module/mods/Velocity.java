package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;
import java.util.Random;

import io.netty.buffer.Unpooled;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {

	public ModeValue mode;

	public NumberValue chance;
	public NumberValue hori;
	public NumberValue verti;

	public Velocity() {
		super("Velocity", HackCategory.COMBAT);

		this.mode = new ModeValue("Mode", new Mode("AAC", false), new Mode("Legit", false),new Mode("Simple", true), new Mode("Ghost", false),new Mode("Hypixel", false));
		hori = new NumberValue("Horizon",90D,0D,100D);
		chance = new NumberValue("Chance",90D,0D,100D);
		verti = new NumberValue("Verti",90D,0D,100D);
		this.addValue(hori);
		this.addValue(chance);
		this.addValue(verti);
		this.addValue(mode);
		this.setChinese(Core.Translate_CN[101]);
	}

	@Override
	public String getDescription() {
		return "Prevents you from getting pushed by players, mobs and flowing water.";
	}

	@Override
	public void onPacketEvent(EventPacket event) {
		if (mode.getMode("Hypixel").isToggled()) {

			if (event.getPacket() instanceof S12PacketEntityVelocity
					|| event.getPacket() instanceof S27PacketExplosion) {
				event.setCancelled(true);
			}
		}
		super.onPacketEvent(event);
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if (mode.getMode("AAC").isToggled()) {
			EntityPlayerSP player = Wrapper.INSTANCE.player();
			if (player.hurtTime > 0 && player.hurtTime <= 7) {
				player.motionX *= 0.5;
				player.motionZ *= 0.5;
			}
			if (player.hurtTime > 0 && player.hurtTime < 6) {
				player.motionX = 0.0;
				player.motionZ = 0.0;
			}
		}else if(mode.getMode("Legit").isToggled()){
			if (mc.thePlayer.maxHurtResistantTime != mc.thePlayer.hurtResistantTime || mc.thePlayer.maxHurtResistantTime == 0) {
				return;
			}

			Double random = Math.random();
			random *= 100.0;

			if(random < this.chance.getValue().intValue()) {
				float hori = this.hori.getValue().floatValue();
				hori /= 100.0f;
				float verti = this.verti.getValue().floatValue();
				verti /= 100.0f;
				mc.thePlayer.motionX *= hori;
				mc.thePlayer.motionZ *= hori;
				mc.thePlayer.motionY *= verti;
			} else {
				mc.thePlayer.motionX *= 1.0f;
				mc.thePlayer.motionY *= 1.0f;
				mc.thePlayer.motionZ *= 1.0f;
			}
		}
		super.onClientTick(event);
	}


	@Override
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent ev) {
		if (mode.getMode("Ghost").isToggled()) {
			if (mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {

				if (chance.getValue() != 100.0D) {
					double ch = Math.random();
					if (ch >= chance.getValue() / 100.0D) {
						return;
					}
				}

				if (hori.getValue() != 100.0D) {
					mc.thePlayer.motionX *= hori.getValue() / 100.0D;
					mc.thePlayer.motionZ *= hori.getValue() / 100.0D;
				}

				if (verti.getValue() != 100.0D) {
					mc.thePlayer.motionY *= verti.getValue() / 100.0D;
				}
			}
		}

	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if ((packet instanceof S12PacketEntityVelocity) && mode.getMode("Simple").isToggled()
				&& Wrapper.INSTANCE.player().hurtTime >= 0) {
			S12PacketEntityVelocity p = (S12PacketEntityVelocity) packet;
			if (p.getEntityID() == Wrapper.INSTANCE.player().getEntityId()) {
				return false;
			}
		}
		return true;
	}
}
