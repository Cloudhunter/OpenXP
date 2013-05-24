package openxp.common.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class BaseTankContainer implements ITankContainer {

	private XPTank[] tanks;
	protected List<ITankCallback> callbacks;
	
	public BaseTankContainer(XPTank ... tanks) {
		this.tanks = tanks;
		this.callbacks = new ArrayList<ITankCallback>();
	}
	
	public void addCallback(ITankCallback callback) {
		callbacks.add(callback);
	}
	
	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		LiquidStack drained = tanks[0].drain(maxDrain, doDrain);
		if (doDrain && drained.amount > 0) {
			onTankChanged(0);
		}
		return drained;
	}
	
	public LiquidStack drain(int maxDrain, boolean doDrain) {
		return drain(0, maxDrain, doDrain);
	}
	
	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		LiquidStack drained = tanks[tankIndex].drain(maxDrain, doDrain);
		if (drained != null && doDrain && drained.amount > 0) {
			onTankChanged(tankIndex);
		}
		return drained;
	}
	
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		int fillAmount = tanks[0].fill(resource, doFill);
		if (doFill && fillAmount > 0) {
			onTankChanged(0);
		}
		return fillAmount;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		int fillAmount = tanks[tankIndex].fill(resource, doFill);
		if (doFill && fillAmount > 0) {
			onTankChanged(tankIndex);
		}
		return fillAmount;
	}
	
	
	public int fill(LiquidStack resource, boolean doFill) {
		return fill(0, resource, doFill);
	}
	
	public int fill(int amount, boolean doFill) {
		return fill(LiquidDictionary.getLiquid("liquidxp", amount), doFill);
	}
	
	public int getCapacity() {
		return getCapacity(0);
	}
	
	public int getCapacity(int i) {
		return tanks[i].getCapacity();
	}
	
	public int getFreeSpace() {
		return getCapacity() - getTankAmount();
	}
	
	public LiquidStack getLiquid() {
		return getLiquid(0);
	}
	
	public LiquidStack getLiquid(int index) {
		return tanks[index].getLiquid();
	}

	public double getPercentFull() {
		return getPercentFull(0);
	}
	
	public double getPercentFull(int i) {
		return 100.0 / getCapacity(i) * getTankAmount(i);
	}
	
	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tanks[0];
	}

	public int getTankAmount() {
		return getTankAmount(0);
	}

	public int getTankAmount(int index) {
		LiquidStack liquid = getLiquid(index);
		if (liquid != null) {
			return liquid.amount;
		}
		return 0;
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return tanks;
	}

	public void onTankChanged(int index) {
        for (int i = 0; i < callbacks.size(); ++i) {
            callbacks.get(i).onTankChanged(this, index);
        }
	}

	public void readFromNBT(NBTTagCompound tag) {
		for (int i = 0; i < tanks.length; i++) {
			LiquidStack liquid = LiquidStack.loadLiquidStackFromNBT(tag.getCompoundTag("tank_" + i));
			if (liquid != null) {
				tanks[i].setLiquid(liquid);
			}
		}
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i].containsValidLiquid()) {
				tag.setTag("tank_" + i, tanks[i].getLiquid().writeToNBT(new NBTTagCompound()));
			}
		}
	}
}
