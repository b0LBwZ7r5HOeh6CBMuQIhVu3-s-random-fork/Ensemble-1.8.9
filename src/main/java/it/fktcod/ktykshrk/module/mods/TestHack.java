package it.fktcod.ktykshrk.module.mods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import com.google.common.collect.Ordering;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.PacketBuffer;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import scala.reflect.runtime.ReflectionUtils;

public class TestHack extends Module{

	public TestHack() {

		super("TestHack", HackCategory.ANOTHER);
		this.setChinese(Core.Translate_CN[95]);
	}
	
	@Override
	public String getDescription() {
		return "I do not recommend enabling this!";
	}
	
	@Override
	public void onEnable() {


		ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
		int noThreads = currentGroup.activeCount();
		Thread[] lstThreads = new Thread[noThreads];
		currentGroup.enumerate(lstThreads);
		for (int i = 0; i < noThreads; i++){
			ChatUtils.message("Thread: " + i + " [ " + lstThreads[i].getName()+" ]");
		}

		super.onEnable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {



		try {
			Class MAC = Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");

			Field DEBUG = MAC.getDeclaredField("DEBUG");
			DEBUG.setAccessible(true);
			DEBUG.set(MAC,true);

			Field isCheating =MAC.getDeclaredField("isCheating");
			isCheating.setAccessible(true);
			isCheating.set(MAC,false);

			Field Minecraft =MAC.getDeclaredField("mc");
			Minecraft.setAccessible(true);
			Minecraft.set(MAC,null);

			Field cheatingVl =MAC.getDeclaredField("cheatingVl");
			cheatingVl.setAccessible(true);
			cheatingVl.set(MAC,0);

		} catch (NoSuchFieldException e) {
		} catch (ClassNotFoundException e) {
		} catch (IllegalAccessException e) {
		}

	}
}
