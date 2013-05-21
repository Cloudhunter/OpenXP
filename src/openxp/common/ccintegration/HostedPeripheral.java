package openxp.common.ccintegration;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public class HostedPeripheral implements IHostedPeripheral {

	private TileEntity tile;
	private ArrayList<ExposedMethod> exposedMethods;
	private String[] methodNames;
	
	public HostedPeripheral(TileEntity tile, ArrayList<ExposedMethod> exposedMethods) {
		this.tile = tile;
		this.exposedMethods = exposedMethods;
		
		methodNames = new String[exposedMethods.size()];
		int index = 0;
		for (ExposedMethod method : exposedMethods) {
			methodNames[index++] = method.getName();
		}
	}
	
	@Override
	public void attach(IComputerAccess computer) {
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int methodIndex,
			Object[] arguments) throws Exception {
		
			final ExposedMethod exposedMethod = exposedMethods.get(methodIndex);

			final Object[] args = new Object[arguments.length + 1];
			System.arraycopy(arguments, 0, args, 1, arguments.length);
			args[0] = computer;

			Class[] requiredParameters = exposedMethod.getRequiredParameters();

			if (args.length != requiredParameters.length) {
				throw new Exception("Invalid number of parameters.");
			}

			int offset = 0;
			for (Class requiredParameter : requiredParameters) {
				if (!requiredParameter.isAssignableFrom(args[offset].getClass())) {
					throw new Exception("Invalid parameter types");
				}
				offset++;
			}
			
			if (exposedMethod.onTick()) {
				Future callback = TickHandler.addTickCallback(
						tile.worldObj, new Callable() {
							@Override
							public Object call() throws Exception {
								return exposedMethod.getMethod().invoke(tile, args);
							}
						});

				return new Object[] { callback.get() };
			} else {
				return new Object[] { exposedMethod.getMethod().invoke(tile, args) };
			}
		}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void detach(IComputerAccess computer) {
	}

	@Override
	public String[] getMethodNames() {
		return methodNames;
	}

	@Override
	public String getType() {
		return PeripheralRegistry.getName(tile.getClass());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void update() {
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
	}

}
