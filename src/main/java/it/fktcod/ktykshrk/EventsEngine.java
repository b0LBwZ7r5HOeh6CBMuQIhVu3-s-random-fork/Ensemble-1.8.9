package it.fktcod.ktykshrk;

import it.fktcod.ktykshrk.irc.IRCChat;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.mods.Hitbox;
import it.fktcod.ktykshrk.ui.click.elements.Slider;
import net.minecraftforge.client.event.*;
import org.lwjgl.input.Keyboard;

import net.minecraft.util.MovementInput;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import it.fktcod.ktykshrk.event.EventBlockBB;
import it.fktcod.ktykshrk.event.EventJump;
import it.fktcod.ktykshrk.event.EventLoop;
import it.fktcod.ktykshrk.event.EventMotion;
import it.fktcod.ktykshrk.event.EventMove;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.event.EventPlayerPost;
import it.fktcod.ktykshrk.event.EventPlayerPre;
import it.fktcod.ktykshrk.event.EventRenderBlock;
import it.fktcod.ktykshrk.event.EventSlowDown;
import it.fktcod.ktykshrk.event.EventStep;
import it.fktcod.ktykshrk.event.EventWorld;
import it.fktcod.ktykshrk.eventapi.EventManager;
import it.fktcod.ktykshrk.eventapi.EventTarget;
import it.fktcod.ktykshrk.managers.HackManager;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.mods.AntiBot;
import it.fktcod.ktykshrk.module.mods.ClickGui;
import it.fktcod.ktykshrk.module.mods.ComboMode;
import it.fktcod.ktykshrk.ui.clickgui.click.ClickGuiScreen;
import it.fktcod.ktykshrk.utils.Utils;
import it.fktcod.ktykshrk.utils.system.Connection;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.wrappers.Wrapper;

import static it.fktcod.ktykshrk.module.mods.Hitbox.extra;

public class EventsEngine {
    private boolean initialized = false;

    public boolean onPacket(Object packet, Connection.Side side) {
        boolean suc = true;
        for (Module module : HackManager.getHacks()) {
            if (!module.isToggled() || Wrapper.INSTANCE.world() == null) {
                continue;
            }
            suc &= module.onPacket(packet, side);

        }
        return suc;
    }

    public EventsEngine() {
        EventManager.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (Utils.nullCheck())
            return;
        try {
            HackManager.onGuiOpen(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onGuiOpen");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (Utils.nullCheck())
            return;
        try {
            HackManager.onMouse(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onMouse");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onRenderTick(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onRenderTick");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Utils.nullCheck())
            return;
        try {
            int key = Keyboard.getEventKey();

            if (key == Keyboard.KEY_NONE){
                return;
            }

            if (Keyboard.getEventKeyState()) {
                HackManager.onKeyPressed(key);
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onKeyInput");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
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
            HackManager.onItemPickup(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onItemPickup");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
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

            if (HackManager.getHack("HitBox").isToggled()){
                event.target.width = (float) (extra.getValue() ? 0.6 + Hitbox.expand.getValue() + Hitbox.extraV.getValue() : 0.6 + Hitbox.expand.getValue());
            }

            HackManager.onAttackEntity(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onAttackEntity");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {

            HackManager.onPlayerTick(event);
        } catch (RuntimeException ex) {
            /*
             * ex.printStackTrace(); ChatUtils.error("RuntimeException: onPlayerTick");
             * ChatUtils.error(ex.toString()); Utils.copy(ex.toString());
             */
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (Utils.nullCheck()) {
            AntiBot.bots.clear();
            initialized = false;
            return;
        }
        try {

            if (HackManager.getHack("IRCChat").isToggled() == false||IRCChat.thread==null) {
                // ChatUtils.message("ok");
                HackManager.getHack("IRCChat").setToggled(true);
                HackManager.getHack("IRCChat").onEnable();
            }


            if (!initialized) {
                new Connection(this);
                ClickGui.setColor();
                initialized = true;
            }
            if (!(Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen))
                HackManager.getHack("ClickGui").setToggled(false);
            if (!ComboMode.enabled) {
                HackManager.onClientTick(event);

            } else if (ComboMode.enabled) {
            }

        } catch (RuntimeException ex) {
            /*
             * ex.printStackTrace(); ChatUtils.error("RuntimeException: onClientTick");
             * ChatUtils.error(ex.toString()); Utils.copy(ex.toString());
             */
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onLivingUpdate(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onLivingUpdate");
            ChatUtils.error(ex.toString());
            //Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (Utils.nullCheck() || ComboMode.enabled || Wrapper.INSTANCE.mcSettings().hideGUI)
            return;
        try {
            HackManager.onRenderWorldLast(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onRenderWorldLast");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderBlockOverlayEvent event) {
        if (Utils.nullCheck() || Wrapper.INSTANCE.mcSettings().hideGUI)
            return;
        try {
            HackManager.onRender3D(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onRenderWorldLast");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (Utils.nullCheck() || ComboMode.enabled) {
            return;
        }

        try {
            HackManager.onRenderGameOverlay(event);
        } catch (RuntimeException ex) {
            /*
             * ex.printStackTrace();
             * ChatUtils.error("RuntimeException: onRenderGameOverlay");
             * ChatUtils.error(ex.toString()); Utils.copy(ex.toString());
             */
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (Utils.nullCheck() || ComboMode.enabled) {
            return;
        }

        try {
            HackManager.onRenderGameOverlay(event);
        } catch (RuntimeException ex) {

        }
    }

    /*
     * @SubscribeEvent public void
     * onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
     * if(Utils.nullCheck() || ComboMode.enabled) return; try {
     * HackManager.onLeftClickBlock(event); } catch (RuntimeException ex) {
     * ex.printStackTrace();
     * ChatUtils.error("RuntimeException: onPlayerDamageBlock");
     * ChatUtils.error(ex.toString()); Utils.copy(ex.toString()); } }
     */

    public void onInputUpdateEvent(MovementInput event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onInputUpdate(event);
            //System.out.println(ComboMode.enabled);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPlayerDamageBlock");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @EventTarget
    public void onPlayerEventPre(EventPlayerPre event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onPlayerEventPre(event);
        } catch (RuntimeException ex) {
            /*
             * ex.printStackTrace(); ChatUtils.error("RuntimeException: onPlayerEventPre");
             * ChatUtils.error(ex.toString()); Utils.copy(ex.toString());
             */
        }
    }

    @EventTarget
    public void onPlayerEventPost(EventPlayerPost event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onPlayerEventPost(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPlayerEventPost");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @SubscribeEvent
    public void onSlowDownEvent(EventSlowDown event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onSlowDownEvent(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPlayerEventSlowDown");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    // ensemble ghost

    @EventTarget
    public void onEventMotion(EventMotion event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onMotionUpdate(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onMotionUpdate");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    /*
     * @SubscribeEvent public void onUpdate(EventUpdate event) { if
     * (Utils.nullCheck() || ComboMode.enabled) return; try {
     * HackManager.onUpdate(event); } catch (RuntimeException ex) {
     * ex.printStackTrace(); ChatUtils.error("RuntimeException: onUpdate");
     * ChatUtils.error(ex.toString()); Utils.copy(ex.toString()); } }
     */

    @EventTarget
    public void onPacketEvent(EventPacket event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onPacketEvent(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPacketEvent");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @EventTarget
    public void onLoop(EventLoop event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onLoop(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPacketEvent");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    /*
     * @EventTarget public void onRender2D(EventRender2D event) { if
     * (Utils.nullCheck() || ComboMode.enabled) return; try {
     * HackManager.onRender2D(event); } catch (RuntimeException ex) {
     * ex.printStackTrace(); ChatUtils.error("RuntimeException: onRender2D");
     * ChatUtils.error(ex.toString()); Utils.copy(ex.toString()); } }
     *
     * @EventTarget public void onRender3D(EventRender3D event) { if
     * (Utils.nullCheck() || ComboMode.enabled) return; try {
     * HackManager.onRender3D(event); } catch (RuntimeException ex) {
     * ex.printStackTrace(); ChatUtils.error("RuntimeException: onRender3D");
     * ChatUtils.error(ex.toString()); Utils.copy(ex.toString()); } }
     */

    @EventTarget
    public void onMove(EventMove event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onMove(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPlayerEventSlowDown");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @EventTarget
    public void onEventStep(EventStep event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onEventStep(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPacketEvent");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @EventTarget
    public void onWorld(EventWorld event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onWorld(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPacketEvent");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @EventTarget
    public void onJump(EventJump event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onJump(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPacketEvent");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @EventTarget
    public void onBlockBB(EventBlockBB event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onBlockBB(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPacketEvent");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }

    @EventTarget
    public void onBlockRender(EventRenderBlock event) {
        if (Utils.nullCheck() || ComboMode.enabled)
            return;
        try {
            HackManager.onRenderBlock(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            ChatUtils.error("RuntimeException: onPacketEvent");
            ChatUtils.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
}
