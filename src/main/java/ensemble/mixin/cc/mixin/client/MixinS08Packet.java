package ensemble.mixin.cc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import ensemble.mixin.cc.mixin.interfaces.IS08Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@Mixin(S08PacketPlayerPosLook.class)
public class MixinS08Packet implements IS08Packet {
	@Shadow
	private double x;
	@Shadow
    private double y;
	@Shadow
    private double z;
	@Shadow
    private float yaw;
	@Shadow
    private float pitch;

	@Override
	public double getX() {
		// TODO 自动生成的方法存根
		return x;
	}

	@Override
	public double getY() {
		// TODO 自动生成的方法存根
		return y;
	}

	@Override
	public double getZ() {
		// TODO 自动生成的方法存根
		return z;
	}

	@Override
	public void setX(float x_) {
		this.x = x_;

	}

	@Override
	public void setY(float y_) {
		this.y = y_;

	}

	@Override
	public void setZ(float z_) {
		this.z = z_;

	}

	@Override
	public float getYaw() {
		// TODO 自动生成的方法存根
		return yaw;
	}

	@Override
	public float getPitch() {
		// TODO 自动生成的方法存根
		return pitch;
	}

}
