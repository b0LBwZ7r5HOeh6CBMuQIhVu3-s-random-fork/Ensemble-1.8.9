package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import ensemble.mixin.cc.mixin.interfaces.IGuiPlayerTabOverlay;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.BlockUtils;
import it.fktcod.ktykshrk.utils.EntityBot;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection.Side;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.value.NumberValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import scala.reflect.internal.Trees.This;

public class AntiBot extends Module {

	public static ArrayList<EntityBot> bots = new ArrayList<EntityBot>();

	public NumberValue level;
	public NumberValue tick;

	public BooleanValue ifInAir;
	public BooleanValue ifGround;
	public BooleanValue ifZeroHealth;
	public BooleanValue ifInvisible;
	public BooleanValue ifEntityId;
	public BooleanValue ifTabName;
	public BooleanValue ifPing;

	public BooleanValue remove;
	public BooleanValue gwen;
	public BooleanValue mineland;

	public static ModeValue mode;

	private static List invalid = new ArrayList();

	public AntiBot() {
		super("AntiBot", HackCategory.COMBAT);

		level = new NumberValue("AILevel", 0.0D, 0.0D, 6.0D);
		tick = new NumberValue("TicksExisted", 0.0D, 0.0D, 999.0D);

		ifInvisible = new BooleanValue("Invisible", false);
		ifInAir = new BooleanValue("InAir", false);
		ifGround = new BooleanValue("OnGround", false);
		ifZeroHealth = new BooleanValue("ZeroHealth", false);
		ifEntityId = new BooleanValue("EntityId", false);
		ifTabName = new BooleanValue("OutTabName", false);
		ifPing = new BooleanValue("PingCheck", false);

		remove = new BooleanValue("RemoveBots", false);
		gwen = new BooleanValue("Gwen", false);
		mineland = new BooleanValue("Mineland", false);

		mode = new ModeValue("Mode", new Mode("Basic", false), new Mode("Mineplex", true), new Mode("Hypixel", false));

		this.addValue(level, tick, remove, gwen, ifInvisible, ifInAir, ifGround, ifZeroHealth, ifEntityId, ifTabName,
				ifPing, mode);
		this.setChinese(Core.Translate_CN[3]);
	}

	@Override
	public String getDescription() {
		return "Ignore/Remove anti cheat bots.";
	}

	@Override
	public void onEnable() {
		bots.clear();
		invalid.clear();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		bots.clear();
		invalid.clear();
		super.onDisable();
	}

	@Override
	public boolean onPacket(Object packet, Side side) {
		if (gwen.getValue()) {
			for (Object entity : Utils.getEntityList()) {
				if (packet instanceof S0CPacketSpawnPlayer) {
					S0CPacketSpawnPlayer spawn = (S0CPacketSpawnPlayer) packet;
					double posX = spawn.getX() / 32.0D;
					double posY = spawn.getY() / 32.0D;
					double posZ = spawn.getZ() / 32.0D;

					double difX = Wrapper.INSTANCE.player().posX - posX;
					double difY = Wrapper.INSTANCE.player().posY - posY;
					double difZ = Wrapper.INSTANCE.player().posZ - posZ;

					double dist = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
					if ((dist <= 17.0D) && (posX != Wrapper.INSTANCE.player().posX)
							&& (posY != Wrapper.INSTANCE.player().posY) && (posZ != Wrapper.INSTANCE.player().posZ)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		EntityPlayer ent;
		Object o1;
		Iterator entX1;
		String name;
		if (mode.getMode("Basic").isToggled()) {
			if (tick.getValue().intValue() > 0.0) {
				bots.clear();
			}
			for (Object object : Utils.getEntityList()) {
				if (object instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) object;
					if (!(entity instanceof EntityPlayerSP) && entity instanceof EntityPlayer
							&& !(entity instanceof EntityArmorStand) && entity != Wrapper.INSTANCE.player()) {
						EntityPlayer bot = (EntityPlayer) entity;
						if (!isBotBase(bot)) {
							int ailevel = level.getValue().intValue();
							boolean isAi = ailevel > 0.0;
							if (isAi && botPercentage(bot) > ailevel) {
								addBot(bot);
							} else if (!isAi && botCondition(bot)) {
								addBot(bot);
							}
						} else {
							addBot(bot);
							if (remove.getValue()) {
								Wrapper.INSTANCE.world().removeEntity(bot);
							}
						}
					}
				}
			}
		} else if (mode.getMode("Mineplex").isToggled() && Wrapper.INSTANCE.player().ticksExisted > 40) {
			entX1 = Wrapper.INSTANCE.world().loadedEntityList.iterator();
			while (entX1.hasNext()) {
				o1 = entX1.next();
				Entity ent1 = (Entity) o1;

				if (ent1 instanceof EntityPlayer && !(ent1 instanceof EntityPlayerSP)) {
					int ticks1 = ent1.ticksExisted;
					double formated = Math.abs(mc.thePlayer.posY - ent1.posY);
					name = ent1.getName();
					String diffX1 = ent1.getCustomNameTag();

					if (diffX1 == "" && !invalid.contains((EntityPlayer) ent1)) {
						invalid.add(ent1);
						Wrapper.INSTANCE.world().removeEntity(ent1);
					}
				}

			}

		} else if (mode.getMode("Hypixel").isToggled()) {
			// Loop through entity list
			for (Object o : mc.theWorld.getLoadedEntityList()) {
				if (o instanceof EntityPlayer) {
					EntityPlayer ent1 = (EntityPlayer) o;
					// Make sure it's not the local player + they are in a worrying distance. Ignore
					// them if they're already invalid.
					if (ent1 != mc.thePlayer && !invalid.contains(ent1)) {
						// Handle current mode

						String formated = ent1.getDisplayName().getFormattedText();
						String custom = ent1.getCustomNameTag();
						String name1 = ent1.getName();

						if (ent1.isInvisible() && !formated.startsWith("��c") && formated.endsWith("��r")
								&& custom.equals(name1)) {
							double diffX = Math.abs(ent1.posX - mc.thePlayer.posX);
							double diffY = Math.abs(ent1.posY - mc.thePlayer.posY);
							double diffZ = Math.abs(ent1.posZ - mc.thePlayer.posZ);
							double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
							if (diffY < 13 && diffY > 10 && diffH < 3) {
								List<EntityPlayer> list = getTabPlayerList();
								if (!list.contains(ent1)) {
									invalid.add(ent1);
									Wrapper.INSTANCE.world().removeEntity(ent1);
								}

							}

						}
						// SHOP BEDWARS
						if (!formated.startsWith("��") && formated.endsWith("��r")) {
							invalid.add(ent1);
						}
						if (ent1.isInvisible()) {
							// BOT INVISIBLES IN GAME
							if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("��c��c")
									&& name1.contains("��c")) {

								invalid.add(ent1);
							}
						}
						// WATCHDOG BOT
						if (!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("��c")
								&& custom.toLowerCase().contains("��r")) {

							invalid.add(ent1);
						}

						// BOT LOBBY
						if (formated.contains("��8[NPC]")) {
							invalid.add(ent1);
						}
						if (!formated.contains("��c") && !custom.equalsIgnoreCase("")) {

							invalid.add(ent1);
						}
						break;

					}
				}
			}

		}
		super.onClientTick(event);
	}

	void addBot(EntityPlayer player) {
		if (!isBot(player)) {
			bots.add(new EntityBot(player));
		}
	}

	public static boolean isBot(Entity player) {
		if (mode.getMode("Basic").isToggled()) {
			for (EntityBot bot : bots) {
				if (bot.getName().equals((player.getName()))) {
					if (player.isInvisible() != bot.isInvisible()) {
						return player.isInvisible();
					}
					return true;
				} else {
					EntityPlayer X = (EntityPlayer)player;
					if (bot.getId() == player.getEntityId() || bot.getUuid().equals(X.getGameProfile().getId())) {
						return true;
					}
				}
			}
		} else if (mode.getMode("Mineplex").isToggled()) {
			for (Entity ent : Wrapper.INSTANCE.world().loadedEntityList) {
				if (invalid.contains(ent)) {
					return true;
				}

			}

		} else if (mode.getMode("Hypixel").isToggled()) {
			for (Entity ent : Wrapper.INSTANCE.world().loadedEntityList) {
				if (invalid.contains(ent)) {
					return true;
				}

			}

		}

		return false;
	}

	public static boolean isHypixelNPC(Entity entity) {
		String formattedName = entity.getDisplayName().getFormattedText();
		String customName = entity.getCustomNameTag();

		if (!formattedName.startsWith("\247") && formattedName.endsWith("\247r")) {
			return true;
		}

		return formattedName.contains("[NPC]");
	}

	public static boolean isEntityBot(EntityPlayer player) {
		if (player.getGameProfile() == null) {
			return true;
		}
		NetworkPlayerInfo npi = mc.getNetHandler().getPlayerInfo(player.getGameProfile().getId());
		return (npi == null || npi.getGameProfile() == null || npi.getResponseTime() != 1);
	}

	boolean botCondition(EntityPlayer bot) {
		Iterator entX1;
		int percentage = 0;
		if (tick.getValue().intValue() > 0.0 && bot.ticksExisted < tick.getValue().intValue()) {
			return true;
		}
		if (ifInAir.getValue() && bot.isInvisible() && bot.motionY == 0.0
				&& bot.posY > Wrapper.INSTANCE.player().posY + 1.0
				&& BlockUtils.isBlockMaterial(new BlockPos(bot).down(), Blocks.air)) {
			return true;
		}
		if (ifGround.getValue() && bot.motionY == 0.0 && !bot.isCollidedVertically && bot.onGround
				&& bot.posY % 1.0 != 0.0 && bot.posY % 0.5 != 0.0) {
			return true;
		}
		if (ifZeroHealth.getValue() && bot.getHealth() <= 0) {
			return true;
		}
		if (ifInvisible.getValue() && bot.isInvisible()) {
			return true;
		}
		if (ifEntityId.getValue() && bot.getEntityId() >= 1000000000) {
			return true;
		}
		if (ifTabName.getValue()) {
			boolean isTabName = false;
			for (NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getNetHandler().getPlayerInfoMap()) {
				if (npi.getGameProfile() != null) {
					if (npi.getGameProfile().getName().contains(bot.getName())) {
						isTabName = true;
					}
				}
			}
			if (!isTabName) {
				return true;
			}
		}

		return false;
	}

	int botPercentage(EntityPlayer bot) {
		int percentage = 0;
		if (tick.getValue().intValue() > 0.0 && bot.ticksExisted < tick.getValue().intValue()) {
			percentage++;
		}
		if (ifInAir.getValue() && bot.isInvisible() && bot.posY > Wrapper.INSTANCE.player().posY + 1.0
				&& BlockUtils.isBlockMaterial(new BlockPos(bot).down(), Blocks.air)) {
			percentage++;
		}
		if (ifGround.getValue() && bot.motionY == 0.0 && !bot.isCollidedVertically && bot.onGround
				&& bot.posY % 1.0 != 0.0 && bot.posY % 0.5 != 0.0) {
			percentage++;
		}
		if (ifZeroHealth.getValue() && bot.getHealth() <= 0) {
			percentage++;
		}
		if (ifInvisible.getValue() && bot.isInvisible()) {
			percentage++;
		}
		if (ifEntityId.getValue() && bot.getEntityId() >= 1000000000) {
			percentage++;
		}
		if (ifTabName.getValue()) {
			boolean isTabName = false;
			for (NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getNetHandler().getPlayerInfoMap()) {
				if (npi.getGameProfile() != null) {
					if (npi.getGameProfile().getName().contains(bot.getName())) {
						isTabName = true;
					}
				}
			}
			if (!isTabName) {
				percentage++;
			}
		}
		return percentage;
	}

	boolean isBotBase(EntityPlayer bot) {
		if (isBot(bot)) {
			return true;
		}
		if (bot.getGameProfile() == null) {
			return true;
		}
		GameProfile botProfile = bot.getGameProfile();
		if (bot.getUniqueID() == null) {
			return true;
		}
		UUID botUUID = bot.getUniqueID();
		if (botProfile.getName() == null) {
			return true;
		}
		String botName = botProfile.getName();
		if (botName.contains("Body #") || botName.contains("NPC")
				|| botName.equalsIgnoreCase(Utils.getEntityNameColor(bot))) {
			return true;
		}
		return false;
	}

	public List getInvalid() {
		return invalid;
	}

	// hanabi code
	public static List<EntityPlayer> getTabPlayerList() {
		NetHandlerPlayClient nhpc = Minecraft.getMinecraft().thePlayer.sendQueue;
		List<EntityPlayer> list = new ArrayList<>();
		List players = ((IGuiPlayerTabOverlay) new GuiPlayerTabOverlay(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().ingameGUI)).getField().sortedCopy(nhpc.getPlayerInfoMap());
		for (final Object o : players) {
			final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
			if (info == null) {
				continue;
			}
			list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
		}
		return list;
	}
}
