package openxp.common.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;

public interface IMethodCallback {
	public String getMethodName();
	public Object execute(IComputerAccess item, Object[] args) throws Exception;
}