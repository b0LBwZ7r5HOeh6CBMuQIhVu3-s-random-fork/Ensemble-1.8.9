package it.fktcod.ktykshrk.event;

import it.fktcod.ktykshrk.eventapi.events.Event;
import it.fktcod.ktykshrk.eventapi.types.EventType;

public class EventStep implements Event {
   
    private float height;
    private final EventType eventType;

    public EventStep(EventType eventType, float height) {
        this.eventType = eventType;
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public EventType getEventType() {
        return eventType;
    }
}
