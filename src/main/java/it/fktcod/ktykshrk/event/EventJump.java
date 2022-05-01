package it.fktcod.ktykshrk.event;

import it.fktcod.ktykshrk.eventapi.events.callables.EventCancellable;

public class EventJump extends EventCancellable {
    public float yaw;

    public EventJump(float yaw) {
        this.yaw = yaw;
    }
}
