package it.fktcod.ktykshrk.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityUtils {
	public static float getDistance(Entity e1, Entity e2)
	{
		return e1.getDistanceToEntity(e2);
	}

	public static double getDistanceSq(Entity e1, Entity e2) {
		// TODO Auto-generated method stub
		return e1.getDistanceSqToEntity(e2);
	}
}
