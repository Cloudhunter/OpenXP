package openxp.common.core;

import java.util.ArrayList;

public class GuiValueHolder extends ArrayList<SavableInt> {
	
	public GuiValueHolder(SavableInt ... values) {
		super(values.length);
		for (int i = 0; i < values.length; i++) {
			add(values[i]);
		}
	}
	
	public int[] asIntArray() {
		int[] values = new int[size()];
		for (int i = 0; i < size(); i++) {
			values[i] = get(i).getValue();
		}
		return values;
	}
	
}
