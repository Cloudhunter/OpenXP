package openxp.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public class PeripheralMethodRegistry implements IHostedPeripheral {

	private World world;

	private ArrayList<IMethodCallback> methods = new ArrayList<IMethodCallback>();

	public PeripheralMethodRegistry(final Object target) {
	
		Method[] methods = target.getClass().getMethods();
		
		for (final Method method : methods) {
			
			if (method.isAnnotationPresent(LuaMethod.class)) {
				
				final LuaMethod luaMethod = method.getAnnotation(LuaMethod.class);
				this.methods.add(new IMethodCallback() {

					@Override
					public String getMethodName() {
						return method.getName();
					}

					@Override
					public Object execute(IComputerAccess item, final Object[] params) throws Exception {
						
						final Object[] args = new Object[params.length + 1];
						System.arraycopy(params, 0, args, 1, params.length);
						args[0] = item;

						Class[] requiredParameters = method.getParameterTypes();

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

						if (luaMethod.onTick()) {
							Future callback = TickHandler.addTickCallback(
									world, new Callable() {
										@Override
										public Object call() throws Exception {
											return method.invoke(target, args);
										}
									});
							
							return callback.get();
						} else {
							return method.invoke(target, args);
						}
					}
				});
			}
		}
	}
	
	public void setWorld(World world) {
		this.world = world;
	}

	public String[] getMethodNames() {
		String[] names = new String[methods.size()];
		int index = 0;
		for (IMethodCallback method : methods) {
			names[index++] = method.getMethodName();
		}
		return names;
	}

	public Object[] callMethod(IComputerAccess computer, int methodId,
			Object[] arguments) throws Exception {
		return new Object[] { methods.get(methodId).execute(computer, arguments) };
	}

	public int Int(Object o) {
		return (int) (double) (Double) o;
	}

	@Override
	public String getType() {
		return "xpbottler";
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
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