package openxp.common.ccintegration;

import java.lang.reflect.Method;

public class ExposedMethod {
	
	private String name;
	private boolean onTick = false;
	private Method javaMethod;
	Class[] requiredParameters;
	
	public ExposedMethod(Method method) {
		javaMethod = method;
		LuaMethod luaMethod = method.getAnnotation(LuaMethod.class);
		
		requiredParameters = method.getParameterTypes();

		name = luaMethod.name();
		if (name.equals("[unassigned]")) {
			name = method.getName();
		}
		onTick = luaMethod.onTick();
	}
	
	public Method getMethod() {
		return javaMethod;
	}
	
	public String getName() {
		return name;
	}
	
	public Class[] getRequiredParameters() {
		return requiredParameters;
	}
	
	public boolean onTick() {
		return onTick;
	}
}