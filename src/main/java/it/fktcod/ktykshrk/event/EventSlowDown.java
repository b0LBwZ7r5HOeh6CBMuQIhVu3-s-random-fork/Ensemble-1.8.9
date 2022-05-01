package it.fktcod.ktykshrk.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventSlowDown extends Event{
	private float strafe;
	private float forward;
	public EventSlowDown(float strafe,float forward ) {
		this.forward=forward;
		this.strafe=strafe;
	}
	
	public void setStrafe(float strafe) {
		this.strafe=strafe;
	}
	public void setForward(float forward) {
		this.forward=forward;
	}
	public float getStrafe() {
		return strafe;
	}
	public float getForward() {
		return forward;
	}
}
