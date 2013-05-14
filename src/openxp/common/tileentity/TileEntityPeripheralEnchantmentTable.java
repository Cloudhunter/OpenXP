package openxp.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityEnchantmentTable;

public class TileEntityPeripheralEnchantmentTable extends TileEntityEnchantmentTable implements IInventory {

	public final static int[] SLOTS = new int[] { 0, 0 };
	
	protected ItemStack[] itemStacks = new ItemStack[1];
	
	public TileEntityPeripheralEnchantmentTable() {
	}
	
	@Override
	public int getSizeInventory() {
		return this.itemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.itemStacks[i];
	}

	@Override
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

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (itemStacks[slot] == null)
			return null;
		ItemStack toReturn = itemStacks[slot];
		itemStacks[slot] = null;
		return toReturn;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		itemStacks[i] = itemstack;
	}

	@Override
	public String getInvName() {
		return "openxp.machines.enchantment";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this;
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

}
