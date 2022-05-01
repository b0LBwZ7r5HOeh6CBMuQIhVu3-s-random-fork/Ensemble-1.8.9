package it.fktcod.ktykshrk.module.mods.addon;

import java.lang.reflect.Field;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.Criticals;
import it.fktcod.ktykshrk.module.mods.KillAura;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class RedeskyAddon extends Module {
	BooleanValue nofall;
	BooleanValue criticals;
	BooleanValue debug;
	int crize;

	boolean shoudcri;

	public RedeskyAddon() {
		super("RedeskyAddon", HackCategory.ANOTHER);
		nofall = new BooleanValue("NoFall", true);
		criticals = new BooleanValue("Criticals", true);
		debug = new BooleanValue("Debug", true);
		this.addValue(nofall, criticals, debug);

	}

	@Override
	public void onEnable() {
		crize = 0;
		shoudcri = false;
		ChatUtils.report("Welcome to use this addon suggested by FunkNight!Redesky bypass!");
		super.onEnable();
	}

	@Override
	public void onDisable() {
		crize = 0;
		shoudcri = false;
		super.onDisable();
	}

	/*
	 * @Override public void onPlayerEventPre(EventPlayerPre event) { if
	 * (criticals.getValue() && KillAura.getTarget() != null && shoudcri) {
	 * super.onPlayerEventPre(event); } }
	 */

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (side == Side.OUT) {
			if (packet instanceof C03PacketPlayer) {
				final C03PacketPlayer p = (C03PacketPlayer) packet;

				if (nofall.getValue() && Wrapper.INSTANCE.player().fallDistance > 2) {
					Field field = ReflectionHelper.findField(C03PacketPlayer.class,
							new String[] { "onGround", "field_149474_g" });
					try {

						if (!field.isAccessible()) {
							field.setAccessible(true);
						}

						field.setBoolean(p, true);

					} catch (Exception e) {
						////System.out.println(e);
					}
				}
			}

			if (criticals.getValue()) {
				if (packet instanceof C02PacketUseEntity) {
					C02PacketUseEntity attack = (C02PacketUseEntity) packet;
					if (attack.getAction() == C02PacketUseEntity.Action.ATTACK) {
						shoudcri=true;
				}

			}else {
				shoudcri=false;
			}
				
			if (packet instanceof S0BPacketAnimation && debug.getValue()) {
				final S0BPacketAnimation p = (S0BPacketAnimation) packet;

			}
			if ((packet instanceof C03PacketPlayer||packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)&&KillAura.getTarget()!=null) {
			
			//	(packet instanceof C03PacketPlayer||packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)

				final C03PacketPlayer p = (C03PacketPlayer) packet;
				Field field = ReflectionHelper.findField(C03PacketPlayer.class,
						new String[] { "onGround", "field_149474_g" });
				
				try {

					if (!field.isAccessible()) {
						field.setAccessible(true);
					}

					
					field.setBoolean(p, true);

				} catch (Exception e) {
					////System.out.println(e);
				}

				crize++;

				if (crize == 3) {
					crize = 0;

				}

			} 
		}
			return super.onPacket(packet, side);
		
		}
		return true;
	}
}
