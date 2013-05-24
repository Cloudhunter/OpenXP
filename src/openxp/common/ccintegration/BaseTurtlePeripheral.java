package openxp.common.ccintegration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class BaseTurtlePeripheral {

	protected ITurtleAccess turtle;
	protected ArrayList<ExposedMethod> exposedMethods = new ArrayList<ExposedMethod>();
	protected String[] methodNames;
	private BaseTurtlePeripheral self;
	
	public BaseTurtlePeripheral(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		self = this;
		Method[] methods = self.getClass().getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(LuaMethod.class)) {
				ExposedMethod exposedMethod = new ExposedMethod(method);
				exposedMethods.add(exposedMethod);
			}
		}
		int i = 0;
		methodNames = new String[exposedMethods.size()];
		for (ExposedMethod exposedMethod : exposedMethods) {
			methodNames[i++] = exposedMethod.getName();
		}
	}
	
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
			try {
				Future callback = TickHandler.addTickCallback(
					turtle.getWorld(),
					new Callable() {
						@Override
						public Object call() throws Exception {
							return exposedMethod.getMethod().invoke(self, args);
						}
					}
				);
				return new Object[] { callback.get() };
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			return new Object[] { exposedMethod.getMethod().invoke(self, args) };
		}
		return null;
	}
	
}
