package ensemble.mixin.cc.mixin.interfaces;

public interface IC03Packet {
	
	public void setY(float y_);
	
	void setX(float x_);
	
	void setZ(float z_);
	
	void setOnGround(boolean g);
	
	void setrotating(boolean r);
	
	void setPitch(float pitch_);
	
	void setYaw(float yaw_);
	
	double getY();

}
