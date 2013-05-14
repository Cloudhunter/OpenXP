package openxp.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class BaseInventoryTileEntity extends TileEntity implements IInventory {


	protected ItemStack[] itemStacks = new ItemStack[4];

	public int getSizeInventory() {
		return this.itemStacks.length;
	}

	public ItemStack getStackInSlot(int i) {
		return this.itemStacks[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		ItemStack result;
		if (itemStacks[i] == null) {
			result = null;
		} else if (itemStacks[i].stackSize > j) {
			result = itemStacks[i].splitStack(j);
		} else {
			ItemStack tmp = itemStacks[i];
			itemStacks[i] = null;
			result = tmp;
		}
		return result;
	}

	public ItemStack getStackInSlotOnClosing(int slot) {
		if (itemStacks[slot] == null)
			return null;
		ItemStack toReturn = itemStacks[slot];
		itemStacks[slot] = null;
		return toReturn;
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		itemStacks[i] = itemstack;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this;
	}

	public void openChest() {
	}

	public void closeChest() {
	}


	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		itemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < itemStacks.length) {
				itemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}


	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				itemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public String getInvName() {
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}	
}
