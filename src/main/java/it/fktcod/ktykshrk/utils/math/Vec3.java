package it.fktcod.ktykshrk.utils.math;

import net.minecraft.util.MathHelper;

public class Vec3
{
    public final double x;
    public final double y;
    public final double z;
    public static Vec3 instace;

    public Vec3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public Vec3 addVector(double x, double y, double z)
    {
        return new Vec3(this.x + x, this.y + y, this.z + z);
    }

    public Vec3 floor()
    {
        return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(Vec3 v)
    {
        return Math.pow(v.x - this.x, 2) + Math.pow(v.y - this.y, 2) + Math.pow(v.z - this.z, 2);
    }

    public Vec3 add(Vec3 v)
    {
        return addVector(v.getX(), v.getY(), v.getZ());
    }

    public Vec3 mc()
    {
        return new Vec3(x, y, z);
    }
    public net.minecraft.util.Vec3 mc2() {
        return new net.minecraft.util.Vec3(x, y, z);
    }

    @Override
    public String toString()
    {
        return "[" + x + ";" + y + ";" + z + "]";
    }

    public Vec3 normalize()
    {
        double d0 = (double)MathHelper.sqrt_double(this.x * this.x + this.y * this.y + this.z * this.z);
        return d0 < 1.0E-4D ? new Vec3(0.0D, 0.0D, 0.0D) : new Vec3(this.x / d0, this.y / d0, this.z / d0);
    }
}
