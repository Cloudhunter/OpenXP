package openxp.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.IPeripheralHandler;

public class PeripheralHandler implements IPeripheralHandler {
	
	@Override
	public IHostedPeripheral getPeripheral(TileEntity tile) {
		if (tile instanceof IIsPeripheral) {
			return ((IIsPeripheral) tile).getMethodRegistry();
		}
		return null;
	}

}
