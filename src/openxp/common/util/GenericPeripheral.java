package openxp.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public class GenericPeripheral implements IHostedPeripheral {

	
	public GenericPeripheral(TileEntity tile) {
		
		
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMethodNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method,
			Object[] arguments) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canAttachToSide(int side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void attach(IComputerAccess computer) {
	}

	@Override
	public void detach(IComputerAccess computer) {
	}

	@Override
	public void update() {
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
	}
	
}