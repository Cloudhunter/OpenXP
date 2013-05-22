package openxp.common.ccintegration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.IPeripheralHandler;

public class PeripheralRegistry implements IPeripheralHandler {
	
	private static HashMap<Class, ArrayList<ExposedMethod>> peripherals = new HashMap<Class, ArrayList<ExposedMethod>>();
	private static HashMap<Class, String> names = new HashMap<Class, String>();
	
	public static ArrayList<ExposedMethod> getLuaMethods(Class <? extends TileEntity> clazz) {
		return peripherals.get(clazz);
	}
	
	public static String getName(Class klazz) {
		return names.get(klazz);
	}
	
	public static void registerTileEntity(Class <? extends TileEntity> clazz, String name) {
			
			Method[] methods = clazz.getMethods();
			
			ArrayList<ExposedMethod> exposedMethods = new ArrayList<ExposedMethod>();
			
			for (Method method : methods) {
				if (method.isAnnotationPresent(LuaMethod.class)) {
					exposedMethods.add(new ExposedMethod(method));
				}
			}
			
			names.put(clazz, name);
			peripherals.put(clazz, exposedMethods);
	}

	@Override
	public IHostedPeripheral getPeripheral(TileEntity tile) {
		Class clazz = tile.getClass();
		if (peripherals.containsKey(clazz)) {
			return new HostedPeripheral(tile, peripherals.get(clazz));
		}
		return null;
	}

}
