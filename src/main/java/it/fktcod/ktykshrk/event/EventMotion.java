package it.fktcod.ktykshrk.event;


import it.fktcod.ktykshrk.eventapi.events.Cancellable;
import it.fktcod.ktykshrk.eventapi.events.Event;
import it.fktcod.ktykshrk.eventapi.types.EventType;

/**
 *  warnning: you cant't obfuscate this class of fields
 */
public class EventMotion implements Event, Cancellable
{
    public static double y;
    public static float yaw;
    public static float pitch;
    public static boolean onGround;
    public boolean cancel;
    public static EventType type;

    public boolean isCancel() {
        return cancel;
    }
    public EventMotion(double y, float yaw, float pitch) {
        EventMotion.y = y;
        EventMotion.yaw = yaw;
        EventMotion.pitch = pitch;
        this.type = EventType.PRE;
    }
    public EventMotion(double y, float yaw, float pitch, boolean onGround) {
        EventMotion.y = y;
        EventMotion.yaw = yaw;
        EventMotion.pitch = pitch;
        EventMotion.onGround = onGround;
        this.type = EventType.PRE;
    }


    public EventType getEventType() {
        return this.type;
    }

    public EventMotion(EventType type) {
        this.type = type;
    }

    public static boolean isPre() {
        return EventMotion.type == EventType.PRE;
    }


    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public double getY() {
        return EventMotion.y;
    }

    public void setY(double y) {
        EventMotion.y = y;
    }

    public float getYaw() {
        return EventMotion.yaw;
    }

    public void setYaw(float yaw) {
        EventMotion.yaw = yaw;
    }

    public float getPitch() {
        return EventMotion.pitch;
    }

    public void setPitch(float pitch) {
        EventMotion.pitch = pitch;
    }

    public boolean isOnGround() {
        return EventMotion.onGround;
    }

    public void setOnGround(boolean onGround) {
        EventMotion.onGround = onGround;
    }

}


