package it.fktcod.ktykshrk.module.mods;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.fktcod.ktykshrk.Core;
import org.lwjgl.opengl.GL11;

import it.fktcod.ktykshrk.managers.EnemyManager;
import it.fktcod.ktykshrk.managers.FriendManager;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.EntityUtils;
import it.fktcod.ktykshrk.utils.HUDUtils;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.ValidUtils;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.value.BooleanValue;
import it.fktcod.ktykshrk.value.Mode;
import it.fktcod.ktykshrk.value.ModeValue;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EntityESP extends Module {

	public ModeValue mode;
	private int playerBox;
	private final ArrayList<EntityPlayer> players = new ArrayList<>();

	public EntityESP() {
		super("EntityESP", HackCategory.VISUAL);
		mode = new ModeValue("Mode", new Mode("Basic", false), new Mode("2D", true), new Mode("Arrow", false), new Mode("Box", false), new Mode("Health", false), new Mode("Ring", false), new Mode("Shaded", false));
		addValue(mode);
		this.setChinese(Core.Translate_CN[39]);
	}

	@Override
	public String getDescription() {
		return "Allows you to see all of the entities around you.";
	}

	@Override
	public void onEnable() {
		playerBox = GL11.glGenLists(1);
		GL11.glNewList(playerBox, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_LINES);
		AxisAlignedBB bb = new AxisAlignedBB(-0.5, 0, -0.5, 0.5, 1, 0.5);
		RenderUtils.drawOutlinedBoxWurst(bb);
		GL11.glEnd();
		GL11.glEndList();
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		GL11.glDeleteLists(playerBox, 1);
		playerBox = 0;
		super.onDisable();
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {

		if (mode.getMode("Basic").isToggled()) {
			for (Object object : Utils.getEntityList()) {
				if (object instanceof EntityLivingBase && !(object instanceof EntityArmorStand)) {
					EntityLivingBase en = (EntityLivingBase) object;
					this.render(en, event.partialTicks);
				}
			}
		}else{
			for (Object object : Utils.getEntityList()) {
				if (object instanceof EntityLivingBase && !(object instanceof EntityArmorStand)) {
					EntityLivingBase en = (EntityLivingBase) object;

					if (mode.getMode("2D").isToggled()) {
						HUDUtils.ee(en, 3,0.5D, 1D, 0, true);
					} else if (mode.getMode("Arrow").isToggled()) {
						HUDUtils.ee(en, 5,0.5D, 1D, 0, true);
					} else if (mode.getMode("Box").isToggled()) {
						HUDUtils.ee(en, 1,0.5D, 1D, 0, true);
					} else if (mode.getMode("Health").isToggled()) {
						HUDUtils.ee(en, 4,0.5D, 1D, 0, true);
					} else if (mode.getMode("Ring").isToggled()) {
						HUDUtils.ee(en, 6,0.5D, 1D, 0, true);
					} else if (mode.getMode("Shaded").isToggled()) {
						HUDUtils.ee(en, 2,0.5D, 1D, 0, true);
					}
				}
			}

		}





		super.onRenderWorldLast(event);
	}

	@Override
	public void onPlayerTick(PlayerTickEvent event) {
		
		Stream<EntityPlayer> stream = Wrapper.INSTANCE.world().playerEntities.parallelStream()
				.filter(e -> !e.isDead && e.getHealth() > 0).filter(e -> e != Wrapper.INSTANCE.player())
				.filter(e -> Math.abs(e.posY - Wrapper.INSTANCE.player().posY) <= 1e6);

		players.addAll(stream.collect(Collectors.toList()));

		super.onPlayerTick(event);
	}

	void render(EntityLivingBase entity, float ticks) {
		if (ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player()) {
			return;
		}
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			String ID = Utils.getPlayerName(player);
			if (EnemyManager.enemysList.contains(ID)) {
				RenderUtils.drawESP(entity, 0.8f, 0.3f, 0.0f, 1.0f, ticks);
				return;
			}
			if (FriendManager.friendsList.contains(ID)) {
				RenderUtils.drawESP(entity, 0.0f, 0.7f, 1.0f, 1.0f, ticks);
				return;
			}
		}
		if (HackManager.getHack("Targets").isToggledValue("Murder")) {
			if (Utils.isMurder(entity)) {
				RenderUtils.drawESP(entity, 1.0f, 0.0f, 0.8f, 1.0f, ticks);
				return;
			}
			if (Utils.isDetect(entity)) {
				RenderUtils.drawESP(entity, 0.0f, 0.0f, 1.0f, 1.0f, ticks);
				return;
			}
		}
		if (entity.isInvisible()) {
			RenderUtils.drawESP(entity, 0.0f, 0.0f, 0.0f, 1.0f, ticks);
			return;
		}
		if (entity.hurtTime > 0) {
			RenderUtils.drawESP(entity, 1.0f, 0.0f, 0.0f, 1.0f, ticks);
			return;
		}
		RenderUtils.drawESP(entity, 1.0f, 1.0f, 1.0f, 1.0f, ticks);
	}

	private void renderBoxes(double partialTicks) {
		for (EntityPlayer e : players) {
			GL11.glPushMatrix();
			GL11.glTranslated(e.prevPosX + (e.posX - e.prevPosX) * partialTicks,
					e.prevPosY + (e.posY - e.prevPosY) * partialTicks,
					e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks);
			GL11.glScaled(e.width + 0.1, e.height + 0.1, e.width + 0.1);

			float f = Wrapper.INSTANCE.player().getDistanceToEntity(e) / 20F;
			GL11.glColor4f(2 - f, f, 0, 0.5F);

			GL11.glCallList(playerBox);

			GL11.glPopMatrix();
		}
	}

	public void renderwurst(RenderWorldLastEvent event) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		GL11.glPushMatrix();
		GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY,
				-TileEntityRendererDispatcher.staticPlayerZ);

		double partialTicks = event.partialTicks;

		renderBoxes(partialTicks);

		GL11.glPopMatrix();

		// GL resets
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
}