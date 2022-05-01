package it.fktcod.ktykshrk.script;

import it.fktcod.ktykshrk.event.*;
import it.fktcod.ktykshrk.eventapi.EventManager;
import it.fktcod.ktykshrk.eventapi.EventTarget;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.ComboMode;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.wrappers.Wrapper;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.lang.reflect.Method;

public class ScriptModule extends Module {
	private String moduleName;
	private HackCategory category;
	private Invocable invoke;

	public ScriptModule(String moduleName, HackCategory category, Invocable invocable) {
		super(moduleName, category);
		this.moduleName = moduleName;
		this.category = category;
		this.invoke = invocable;
	}

	@Override
	public void onEnable() {
		try {
			invoke.invokeFunction("onEnable");
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		super.onDisable();
	}

	@Override
	public void onDisable() {
		try {
			invoke.invokeFunction("onDisable");
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		super.onDisable();
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (Utils.nullCheck())
			return;
		try {
			invoke.invokeFunction("onGuiOpen", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onMouse(MouseEvent event) {
		if (Utils.nullCheck())
			return;
		try {
			invoke.invokeFunction("onMouse", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Utils.nullCheck())
			return;
		try {
			invoke.invokeFunction("onKeyInput", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	/*
	 * @SubscribeEvent public void onCameraSetup(EntityViewRenderEvent.CameraSetup
	 * event) { if (Utils.nullCheck() || ComboMode.enabled) return; try {
	 * HackManager.onCameraSetup(event); } catch (RuntimeException ex) {
	 * ex.printStackTrace(); ChatUtils.error("RuntimeException: onCameraSetup");
	 * ChatUtils.error(ex.toString()); Utils.copy(ex.toString()); } }
	 */
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onItemPickup", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	/*
	 * @SubscribeEvent public void onProjectileImpact(ProjectileImpactEvent event) {
	 * if(Utils.nullCheck() || ComboMode.enabled) return; try {
	 * HackManager.onProjectileImpact(event); } catch (RuntimeException ex) {
	 * ex.printStackTrace(); ChatUtils.error("RuntimeException: ProjectileImpact");
	 * ChatUtils.error(ex.toString()); Utils.copy(ex.toString()); } }
	 */

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onAttackEntity", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onPlayerTick", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (Utils.nullCheck()) {
			return;
		}

		try {
			invoke.invokeFunction("onClientTick", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onLivingUpdate", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (Utils.nullCheck() || ComboMode.enabled || Wrapper.INSTANCE.mcSettings().hideGUI)
			return;
		try {
			invoke.invokeFunction("onRenderWorldLast", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
		if (Utils.nullCheck() || ComboMode.enabled) {
			return;
		}

		try {
			invoke.invokeFunction("onRenderGameOverlay", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (Utils.nullCheck() || ComboMode.enabled) {
			return;
		}
		try {
			invoke.invokeFunction("onRenderGameOverlay", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	public void onInputUpdateEvent(MovementInput event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onInputUpdateEvent", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onPlayerEventPre(EventPlayerPre event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onPlayerEventPre", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onPlayerEventPost(EventPlayerPost event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;

		try {
			invoke.invokeFunction("onPlayerEventPost", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@SubscribeEvent
	public void onSlowDownEvent(EventSlowDown event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onSlowDownEvent", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	// ensemble ghost

	@EventTarget
	public void onEventMotion(EventMotion event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onEventMotion", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onPacketEvent(EventPacket event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onPacketEvent", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onLoop(EventLoop event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onLoop", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onMove(EventMove event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onMove", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onEventStep(EventStep event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onEventStep", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onWorld(EventWorld event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onWorld", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onJump(EventJump event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onJump", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onBlockBB(EventBlockBB event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onBlockBB", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	@EventTarget
	public void onBlockRender(EventRenderBlock event) {
		if (Utils.nullCheck() || ComboMode.enabled)
			return;
		try {
			invoke.invokeFunction("onBlockRender", event);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			this.unregisterMe();
		}
	}

	private void unregisterMe() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			String invokedMethodName = stackTrace[2].getMethodName();
			Class<?> eventClazz = null;
			try {
				eventClazz = Class.forName(invokedMethodName);
			} catch (ClassNotFoundException ig) {
				// 一般来说 找不到的Method就是onDisable和onEnabled
				// 而这两个Method不在EventManager中被注册
				// 所以直接return掉
				return;
			}
			Method invokedMethod = getClass().getDeclaredMethod(invokedMethodName, eventClazz);
			EventManager.unregister(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
