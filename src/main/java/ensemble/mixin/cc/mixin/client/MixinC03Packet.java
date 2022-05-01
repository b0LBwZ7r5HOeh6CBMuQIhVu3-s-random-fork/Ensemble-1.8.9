package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import ensemble.mixin.cc.mixin.interfaces.IC03Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

@Mixin(C03PacketPlayer.class)
public class MixinC03Packet implements IC03Packet {

	@Shadow
	protected double x;
	@Shadow
	protected double y;
	@Shadow
	protected double z;
	@Shadow
	protected float yaw;
	@Shadow
	protected float pitch;
	@Shadow
	protected boolean onGround;
	@Shadow
	protected boolean moving;
	@Shadow
	protected boolean rotating;

	@Override
	public void setY(float y_) {
		y = y_;
	}

	@Override
	public void setOnGround(boolean g) {
		onGround = g;
	}

	@Override
	public void setPitch(float pitch_) {
		pitch = pitch_;

	}

	@Override
	public void setrotating(boolean r) {
		rotating = r;

	}

	@Override
	public void setYaw(float yaw_) {
		yaw = yaw_;

	}

	@Override
	public void setX(float x_) {
		x = x_;
	}

	@Override
	public void setZ(float z_) {
		z = z_;

	}

	@Override
	public double getY() {
		return y;

	}

}
