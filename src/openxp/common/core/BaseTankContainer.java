package openxp.common.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class BaseTankContainer implements ITankContainer {

	private LiquidTank[] tanks;
	protected List<ITankCallback> callbacks;
	
	public BaseTankContainer(LiquidTank ... tanks) {
		this.tanks = tanks;
		this.callbacks = new ArrayList<ITankCallback>();
	}
	
	public void addCallback(ITankCallback callback) {
		callbacks.add(callback);
	}
	
	public void onTankChanged(int index) {
        for (int i = 0; i < callbacks.size(); ++i) {
            callbacks.get(i).onTankChanged(this, index);
        }
	}
	
	public LiquidStack getLiquid() {
		return getLiquid(0);
	}
	
	public LiquidStack getLiquid(int index) {
		return tanks[index].getLiquid();
	}
	
	public int getTankAmount(int index) {
		LiquidStack liquid = getLiquid(index);
		if (liquid != null) {
			return liquid.amount;
		}
		return 0;
	}

	public int getTankAmount() {
		return getTankAmount(0);
	}
	
	
	public int getCapacity() {
		return getCapacity(0);
	}
	
	public int getCapacity(int i) {
		return tanks[i].getCapacity();
	}
	
	public double getPercentFull() {
		return getPercentFull(0);
	}
	
	public double getPercentFull(int i) {
		return 100.0 / getCapacity(i) * getTankAmount(i);
	}
	
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		int fillAmount = tanks[0].fill(resource, doFill);
		if (doFill && fillAmount > 0) {
			onTankChanged(0);
		}
		return fillAmount;
	}

	public int fill(LiquidStack resource, boolean doFill) {
		return fill(0, resource, doFill);
	}
	
	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		int fillAmount = tanks[tankIndex].fill(resource, doFill);
		if (doFill && fillAmount > 0) {
			onTankChanged(tankIndex);
		}
		return fillAmount;
	}
	
	public LiquidStack drain(int maxDrain, boolean doDrain) {
		return drain(0, maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		LiquidStack drained = tanks[0].drain(maxDrain, doDrain);
		if (doDrain && drained.amount > 0) {
			onTankChanged(0);
		}
		return drained;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		LiquidStack drained = tanks[tankIndex].drain(maxDrain, doDrain);
		if (doDrain && drained.amount > 0) {
			onTankChanged(tankIndex);
		}
		return drained;
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return tanks;
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tanks[0];
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
