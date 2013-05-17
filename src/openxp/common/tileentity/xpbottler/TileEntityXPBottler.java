package openxp.common.tileentity.xpbottler;

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
import openxp.client.core.BaseInventory;
import openxp.client.core.BaseTankContainer;
import openxp.client.core.BaseTileEntity;
import openxp.client.core.GuiValueHolder;
import openxp.client.core.IInventoryCallback;
import openxp.client.core.ITankCallback;
import openxp.client.core.SavableInt;
import openxp.common.util.EnchantmentUtils;
import openxp.common.util.LuaMethod;
import openxp.common.util.PeripheralMethodRegistry;

public class TileEntityXPBottler extends BaseTileEntity implements IInventory,
ISidedInventory, ITankContainer, IHasSimpleGui, IInventoryCallback,
ITankCallback {

	public static final int MODE_FILL = 0;
	public static final int MODE_DRAIN = 1;

	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;

	public static final int[] SLOTS = new int[] { 48, 34, 102, 34 };

	protected PeripheralMethodRegistry methodRegistry;
	protected SavableInt progress = new SavableInt("progress");
	protected SavableInt percentStored = new SavableInt("percentStored");
	protected BaseTankContainer tanks = new BaseTankContainer(new LiquidTank(EnchantmentUtils.XP_PER_BOTTLE * 32));
	protected BaseInventory inventory = new BaseInventory("xpbottler", true, 2);
	protected boolean hasChanged = false;
	protected GuiValueHolder guiValues = new GuiValueHolder(percentStored, progress);

	public TileEntityXPBottler() {
		inventory.addCallback(this);
		tanks.addCallback(this);
		methodRegistry = new PeripheralMethodRegistry(this);
	}

	public int getSpeed() {
		return 20;
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
	protected void initialize() {
		if (worldObj != null) {
			methodRegistry.setWorld(worldObj);
		}
	}
	
	public void drainBottles() {

		if (tanks.getTankAmount() + EnchantmentUtils.XP_PER_BOTTLE > tanks.getCapacity() ||
				!areSlotsValid(Item.expBottle, Item.glassBottle)) {

			progress.setValue(0);
			return;
		}

		progress.add(1);

		if (progress.getValue() >= getSpeed()) {
			switchSlots(Item.expBottle, Item.glassBottle);
			tanks.fill(LiquidDictionary.getLiquid("liquidxp", EnchantmentUtils.XP_PER_BOTTLE), true);
			progress.setValue(0);
		}

	}

	public void fillBottles() {

		if (tanks.getTankAmount() < EnchantmentUtils.XP_PER_BOTTLE ||
			!areSlotsValid(Item.glassBottle, Item.expBottle)) {

			progress.setValue(0);
			return;
		}

		progress.add(1);

		if (progress.getValue() >= getSpeed()) {
			switchSlots(Item.glassBottle, Item.expBottle);
			tanks.drain(EnchantmentUtils.XP_PER_BOTTLE, true);
			progress.setValue(0);
		}

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

	private boolean switchSlots(Item inputItem, Item outputItem) {

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

	public double getPercentProgress() {
		return 100.0 / getSpeed() * getProgress();
	}

	public int getProgress() {
		return progress.getValue();
	}

	public int getPercentStored() {
		return percentStored.getValue();
	}

	public void setPercentStored(int value) {
		percentStored.setValue(value);
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
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		inventory.writeToNBT(tag);
		tanks.writeToNBT(tag);
		percentStored.writeToNBT(tag);
		progress.writeToNBT(tag);
	}

	@Override
	public void onInventoryChanged(BaseInventory inventory) {
		hasChanged = true;
		progress.setValue(0);
	}

	@Override
	public void onTankChanged(BaseTankContainer tankContainer, int index) {
		hasChanged = true;
	}

	@Override
	public void onServerButtonClicked(EntityPlayer player, int button) {
		hasChanged = true;
	}

	@Override
	public void onClientButtonClicked(int button) {
	}

	@Override
	public int[] getGuiValues() {
		return guiValues.asIntArray();
	}

	@Override
	public int getGuiValue(int index) {
		return guiValues.get(index).getValue();
	}

	@Override
	public void setGuiValue(int i, int value) {
		guiValues.get(i).setValue(value);
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return tanks.fill(from, resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return tanks.fill(tankIndex, resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tanks.drain(from, maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return tanks.drain(tankIndex, maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return tanks.getTanks(direction);
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tanks.getTank(direction, type);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side) {
		return isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side) {
		return slotID == OUTPUT_SLOT;
	}

	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int stackIndex, int byAmount) {
		return inventory.decrStackSize(stackIndex, byAmount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return inventory.getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory.setInventorySlotContents(i, itemstack);
	}

	@Override
	public String getInvName() {
		return inventory.getInvName();
	}

	@Override
	public boolean isInvNameLocalized() {
		return inventory.isInvNameLocalized();
	}

	@Override
	public int getInventoryStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return inventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public void openChest() {
		inventory.openChest();
	}

	@Override
	public void closeChest() {
		inventory.closeChest();
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return inventory.isStackValidForSlot(i, itemstack);
	}


	
	/***
	 * ComputerCraft Interfaces
	 */
	
	public String getType() {
		return "xpbottler";
	}

	public String[] getMethodNames() {
		return methodRegistry.getMethodNames();
	}

	public Object[] callMethod(dan200.computer.api.IComputerAccess computer, int methodId,
			Object[] arguments) throws Exception {
		return methodRegistry.callMethod(computer, methodId, arguments);
	}

	public boolean canAttachToSide(int side) {
		return true;
	}

	public void attach(dan200.computer.api.IComputerAccess computer) {
	}

	public void detach(dan200.computer.api.IComputerAccess computer) {
	}
	
	@LuaMethod(onTick = true)
	public int getXPStored(dan200.computer.api.IComputerAccess computer) {
		return tanks.getTankAmount();
	}
	
}
