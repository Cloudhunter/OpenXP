package openxp.common.util;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;

public class BlockUtils {
	public static ForgeDirection get2dOrientation(Vec3 pos1, Vec3 pos2) {
		double Dx = pos1.xCoord - pos2.xCoord;
		double Dz = pos1.zCoord - pos2.zCoord;
		double angle = Math.atan2(Dz, Dx) / Math.PI * 180 + 180;

		if (angle < 45 || angle > 315)
			return ForgeDirection.EAST;
		else if (angle < 135)
			return ForgeDirection.SOUTH;
		else if (angle < 225)
			return ForgeDirection.WEST;
		else
			return ForgeDirection.NORTH;
	}
}
