package it.fktcod.ktykshrk.event;

import it.fktcod.ktykshrk.eventapi.events.callables.EventCancellable;

public class EventPlayerPost extends EventCancellable {
	private float yaw;
	private float pitch;

	public EventPlayerPost(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
}
