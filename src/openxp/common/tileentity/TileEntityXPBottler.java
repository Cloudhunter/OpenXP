package openxp.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import openxp.api.IHasSimpleGui;
import openxp.common.ccintegration.LuaMethod;
import openxp.common.core.BaseInventory;
import openxp.common.core.BaseTankContainer;
import openxp.common.core.BaseTileEntity;
import openxp.common.core.GuiValueHolder;
import openxp.common.core.IInventoryCallback;
import openxp.common.core.ITankCallback;
import openxp.common.core.SyncableInt;
import openxp.common.util.EnchantmentUtils;

public class TileEntityXPBottler extends BaseTileEntity implements IInventory,
ISidedInventory, ITankContainer, IHasSimpleGui, IInventoryCallback,
ITankCallback {

	public static final int MODE_FILL = 0;
	public static final int MODE_DRAIN = 1;

	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	
	/**
	 * These are the slot positions of the GUI. x,y, x,y
	 * Used in common/client proxy gui handler
	 */
	public static final int[] SLOTS = new int[] { 48, 34, 102, 34 };
	
	/**
	 * The progress bar in the GUI
	 */
	protected SyncableInt progress = new SyncableInt("progress");
	
	/**
	 * The percentage of liquid stored
	 */
	protected SyncableInt percentStored = new SyncableInt("percentStored");
	
	/**
	 * A wrapper around the tank
	 */
	protected BaseTankContainer tanks = new BaseTankContainer(
				new LiquidTank(
						EnchantmentUtils.XPToLiquidRatio(
								EnchantmentUtils.XP_PER_BOTTLE * 32
						)
				)
	);
	
	/**
	 * A wrapper around the inventory
	 */
	protected BaseInventory inventory = new BaseInventory("xpbottler", true, 2);
	
	/**
	 * Whenever the inventory or tank changes, this changes to true for a tick
	 */
	protected boolean hasChanged = false;
	
	/**
	 * A list of the SavableInts that we want auto-synced with the client
	 */
	protected GuiValueHolder guiValues = new GuiValueHolder(percentStored, progress);

	public TileEntityXPBottler() {
		
		// add callbacks for when the inventory or tank changes
		// (see onTankChanged/onInventoryChanged)
		inventory.addCallback(this);
		tanks.addCallback(this);
	}

	private boolean areSlotsValid(Item inputItem, Item outputItem) {

		ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);
		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

		if (!inventory.isItem(INPUT_SLOT, inputItem)) {
			return false;
		}

		return (
				outputStack == null ||
				(outputStack.getItem() == outputItem && outputStack.stackSize < outputStack.getMaxStackSize())
				);

	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side) {
		return slotID == OUTPUT_SLOT;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side) {
		return isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public void closeChest() {
		inventory.closeChest();
	}

	@Override
	public ItemStack decrStackSize(int stackIndex, int byAmount) {
		return inventory.decrStackSize(stackIndex, byAmount);
	}
	
	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tanks.drain(from, maxDrain, doDrain);
	}

	/*
	 * Property getters and setters
	 */
	
	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return tanks.drain(tankIndex, maxDrain, doDrain);
	}

	/**
	 * Drain the bottles in the input stack if they're XP bottles and if there's
	 * space in the tank
	 */
	public void drainBottles() {

		// validate that everything is OK regarding slots and the tank
		if (tanks.getTankAmount() + EnchantmentUtils.LIQUID_PER_XP_BOTTLE > tanks.getCapacity() ||
				!areSlotsValid(Item.expBottle, Item.glassBottle)) {
			progress.setValue(0);
			return;
		}

		// update the progress bar in the gui (this gets automatically synced)
		progress.add(1);

		// if the progress is complete, lets fill the tank, fix the slots and reset the progress
		if (progress.getValue() >= getSpeed()) {
			switchSlots(Item.glassBottle);
			tanks.fill(LiquidDictionary.getLiquid("liquidxp", EnchantmentUtils.LIQUID_PER_XP_BOTTLE), true);
			progress.setValue(0);
		}

	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return tanks.fill(from, resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return tanks.fill(tankIndex, resource, doFill);
	}
	
	/*
	 * Methods exposed to ComputerCraft Lua
	 */

	/**
	 * Fill empty bottles
	 */
	public void fillBottles() {

		// check we have enough fluid in the tank and the slots are correct
		if (tanks.getTankAmount() < EnchantmentUtils.LIQUID_PER_XP_BOTTLE ||
				!areSlotsValid(Item.glassBottle, Item.expBottle)) {

			progress.setValue(0);
			return;
		}

		// update the progress bar
		progress.add(1);

		// if the progress bar reaches the end, reset the progress, switch the slots and drain
		// the tank
		if (progress.getValue() >= getSpeed()) {
			switchSlots(Item.expBottle);
			tanks.drain(EnchantmentUtils.LIQUID_PER_XP_BOTTLE, true);
			progress.setValue(0);
		}

	}

	/*
	 * Reading and saving from disk
	 */
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	@Override
	public int getGuiValue(int index) {
		return guiValues.get(index).getValue();
	}

	@Override
	public int[] getGuiValues() {
		return guiValues.asIntArray();
	}

	@Override
	public int getInventoryStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	@Override
	public String getInvName() {
		return inventory.getInvName();
	}

	public double getPercentProgress() {
		return 100.0 / getSpeed() * getProgress();
	}

	public int getPercentStored() {
		return percentStored.getValue();
	}

	public int getProgress() {
		return progress.getValue();
	}

	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	/**
	 * The time it takes to empty/fill a bottle
	 */
	public int getSpeed() {
		return 20;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory.getStackInSlot(i);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return inventory.getStackInSlotOnClosing(i);
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tanks.getTank(direction, type);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return tanks.getTanks(direction);
	}

	@LuaMethod(onTick = true)
	public int getXPStored(dan200.computer.api.IComputerAccess computer) {
		return tanks.getTankAmount();
	}

	@Override
	public boolean isInvNameLocalized() {
		return inventory.isInvNameLocalized();
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return inventory.isStackValidForSlot(i, itemstack);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return inventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public void onClientButtonClicked(int button) {
	}

	@Override
	public void onInventoryChanged(BaseInventory inventory) {
		hasChanged = true;
		progress.setValue(0);
	}

	@Override
	public void onServerButtonClicked(EntityPlayer player, int button) {
		hasChanged = true;
	}

	@Override
	public void onTankChanged(BaseTankContainer tankContainer, int index) {
		hasChanged = true;
	}

	@Override
	public void openChest() {
		inventory.openChest();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inventory.readFromNBT(tag);
		tanks.readFromNBT(tag);
		percentStored.readFromNBT(tag);
		progress.readFromNBT(tag);
	}

	@Override
	public void setGuiValue(int i, int value) {
		guiValues.get(i).setValue(value);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory.setInventorySlotContents(i, itemstack);
	}

	public void setPercentStored(int value) {
		percentStored.setValue(value);
	}

	/**
	 * Decreases the size of the input slot and increases the size of the
	 * 
	 * @param inputItem
	 * @param outputItem
	 * @return success
	 */
	private boolean switchSlots(Item outputItem) {

		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

		if (outputStack == null) {
			inventory.setInventorySlotContents(OUTPUT_SLOT, new ItemStack(outputItem));
			inventory.decrStackSize(INPUT_SLOT, 1);
			return true;
		} else if (outputStack.getItem() == outputItem && outputStack.stackSize < outputStack.getMaxStackSize()) {
			outputStack.stackSize++;
			inventory.decrStackSize(INPUT_SLOT, 1);
			return true;
		}

		return false;
	}

	@Override
	public void updateEntity() {

		super.updateEntity();
		
		if (!worldObj.isRemote) {

			ItemStack stack = inventory.getStackInSlot(INPUT_SLOT);

			if (inventory.isItem(INPUT_SLOT, Item.expBottle)) {
				drainBottles();
			} else if (inventory.isItem(INPUT_SLOT, Item.glassBottle)) {
				fillBottles();
			}

			if (hasChanged) {
				percentStored.setValue((int) tanks.getPercentFull());
			}

			hasChanged = false;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		inventory.writeToNBT(tag);
		tanks.writeToNBT(tag);
		percentStored.writeToNBT(tag);
		progress.writeToNBT(tag);
	}


}
