package openxp.client.core;

import java.util.ArrayList;
import java.util.List;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class BaseInventory implements IInventory, ISidedInventory {

	protected List<IInventoryCallback> callbacks;
	protected String inventoryTitle;
	protected int slotsCount;
	protected ItemStack[] inventoryContents;
	protected boolean isInvNameLocalized;
    
	public BaseInventory(String name, boolean isInvNameLocalized, int size) {
		callbacks = new ArrayList<IInventoryCallback>();
		this.isInvNameLocalized = isInvNameLocalized;
		this.slotsCount = size;
		this.inventoryTitle = name;
		this.inventoryContents = new ItemStack[size];
	}
	
	public void addCallback(IInventoryCallback callback) {
		callbacks.add(callback);
	}
	
    public void onInventoryChanged() {
        for (int i = 0; i < callbacks.size(); ++i)
        {
            callbacks.get(i).onInventoryChanged(this);
        }
    }


	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList nbttaglist = tag.getTagList("Items");
		inventoryContents = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound stacktag = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = stacktag.getByte("Slot") & 0xff;
			if (j >= 0 && j < inventoryContents.length) {
				inventoryContents[j] = ItemStack.loadItemStackFromNBT(stacktag);
			}
		}
	}


	public void writeToNBT(NBTTagCompound tag) {

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventoryContents.length; i++) {
			if (inventoryContents[i] != null) {
				NBTTagCompound stacktag = new NBTTagCompound();
				stacktag.setByte("Slot", (byte) i);
				inventoryContents[i].writeToNBT(stacktag);
				nbttaglist.appendTag(stacktag);
			}
		}

		tag.setTag("Items", nbttaglist);
	}

	@Override
	public int getSizeInventory() {
		return slotsCount;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
        return this.inventoryContents[i];
	}

	@Override
	public ItemStack decrStackSize(int stackIndex, int byAmount) {
        if (this.inventoryContents[stackIndex] != null)
        {
            ItemStack itemstack;

            if (this.inventoryContents[stackIndex].stackSize <= byAmount)
            {
                itemstack = this.inventoryContents[stackIndex];
                this.inventoryContents[stackIndex] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.inventoryContents[stackIndex].splitStack(byAmount);

                if (this.inventoryContents[stackIndex].stackSize == 0)
                {
                    this.inventoryContents[stackIndex] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}
	
	public boolean isItem(int slot, Item item) {
		return inventoryContents[slot] != null && inventoryContents[slot].getItem() == item;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
        if (this.inventoryContents[i] != null)
        {
            ItemStack itemstack = this.inventoryContents[i];
            this.inventoryContents[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.inventoryContents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
        	itemstack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
        return this.inventoryTitle;
	}

	@Override
	public boolean isInvNameLocalized() {
		return isInvNameLocalized;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		// TODO Auto-generated method stub
		return false;
	}
    
}
