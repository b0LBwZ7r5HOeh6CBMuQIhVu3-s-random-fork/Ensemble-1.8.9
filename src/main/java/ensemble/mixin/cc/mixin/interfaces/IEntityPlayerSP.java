package ensemble.mixin.cc.mixin.interfaces;

import it.fktcod.ktykshrk.event.EventMove;

public interface IEntityPlayerSP {
	boolean moving();
	
	float getSpeed();
	
	void setSpeed(double speed);

	void setMoveSpeed(EventMove event, double speed);
	
	void setYaw(double yaw);
	
	void setPitch(double pitch);
	
	float getDirection();
	
	void setLastReportedPosY(double f);
}
