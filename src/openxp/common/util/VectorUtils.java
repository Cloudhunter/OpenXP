package openxp.common.util;

import net.minecraft.util.Vec3;

public class VectorUtils {

	public static Vec3 subtract(Vec3 vec1, Vec3 vec2) {
		return Vec3.createVectorHelper(vec2.xCoord - vec1.xCoord, vec2.yCoord - vec1.yCoord, vec2.zCoord - vec1.zCoord);
	}
}
