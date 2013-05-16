package openxp.client.core;

import net.minecraft.nbt.NBTTagCompound;

public class SavableInt {

	protected String name;
	protected int value;
	
	public SavableInt(String name) {
		this.name = name;
	}
	
	public SavableInt(String name, int value) {
		this(name);
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(name)) {
			value = tag.getInteger(name);
		}
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger(name, value);
	}
	
	public void add(int val) {
		value += val;
	}
}
