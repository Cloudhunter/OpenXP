package openxp.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import openxp.api.IHasSimpleGui;
import openxp.common.core.BaseInventory;
import openxp.common.core.BaseTankContainer;
import openxp.common.core.GuiValueHolder;
import openxp.common.core.IInventoryCallback;
import openxp.common.core.ITankCallback;
import openxp.common.core.SavableInt;
import openxp.common.util.BlockSide;
import openxp.common.util.EnchantmentUtils;

public class TileEntityAutomatedEnchantmentTable extends
TileEntityEnchantmentTable implements IInventory, IHasSimpleGui,
ITankContainer, ISidedInventory, ITankCallback, IInventoryCallback {

	public final static int[] SLOTS = new int[] { 10, 33, 150, 33 };

	public final static int BUTTON_LOWEST = 0;
	public final static int BUTTON_HIGHEST = 1;

	public final static int MODE_LOWEST = 0;
	public final static int MODE_HIGHEST = 1;

	public final static int INPUT_STACK = 0;
	public final static int OUTPUT_STACK = 1;

	protected SavableInt mode = new SavableInt("mode");
	protected SavableInt levelsAvailable = new SavableInt("levelsAvailable");
	protected BaseTankContainer tanks = new BaseTankContainer(
			new LiquidTank(
					EnchantmentUtils.XPToLiquidRatio(
							EnchantmentUtils.getExperienceForLevel(30)
					)
			)
	);
	protected BaseInventory inventory = new BaseInventory("enchantmenttable", true, 2);
	protected boolean hasChanged = false;
	protected GuiValueHolder guiValues = new GuiValueHolder(mode, levelsAvailable);

	public TileEntityAutomatedEnchantmentTable() {
		inventory.addCallback(this);
		tanks.addCallback(this);
	}


	@Override
	public void updateEntity() {

		super.updateEntity();

		if (!worldObj.isRemote){

			if (hasChanged) {

				// set the levels available to the amount of experience stored
				levelsAvailable.setValue(getXPLevel());

				ItemStack inputStack = inventory.getStackInSlot(INPUT_STACK);
				ItemStack outputStack = inventory.getStackInSlot(OUTPUT_STACK);

				if (inputStack != null &&
						inputStack.isItemEnchantable() &&
						outputStack == null) {

					double power = EnchantmentUtils.getPower(worldObj, xCoord, yCoord, zCoord);

					boolean getMaxEnchantability = mode.getValue() == MODE_HIGHEST;

					int enchantability = EnchantmentUtils.calcEnchantability(inputStack, (int)power, getMaxEnchantability);

					// the amount of actual liquid XP required
					int xpRequired = EnchantmentUtils.XPToLiquidRatio(
							EnchantmentUtils.getExperienceForLevel(enchantability)
					);

					// if we've got enough liquid
					if (getExperienceLiquid() >= xpRequired) {

						// drain it
						tanks.drain(xpRequired, true);

						inventory.setInventorySlotContents(OUTPUT_STACK, inputStack);
						inventory.setInventorySlotContents(INPUT_STACK, null);
						EnchantmentUtils.enchantItem(inputStack, enchantability, worldObj.rand);

						levelsAvailable.setValue(getXPLevel());

					}
				}
			}

			hasChanged = false;
		}

	}

	public int getLevelsAvailable() {
		return levelsAvailable.getValue();
	}

	public int getMode() {
		return mode.getValue();
	}

	public int getXPLevel() {
		return EnchantmentUtils.getLevelForExperience(getXP());
	}
	
	public int getXP() {
		return EnchantmentUtils.LiquidToXPRatio(getExperienceLiquid());
	}
	
	public int getExperienceLiquid() {
		return tanks.getTankAmount();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inventory.readFromNBT(tag);
		tanks.readFromNBT(tag);
		mode.readFromNBT(tag);
		levelsAvailable.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		inventory.writeToNBT(tag);
		tanks.writeToNBT(tag);
		mode.writeToNBT(tag);
		levelsAvailable.writeToNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		Packet132TileEntityData packet = new Packet132TileEntityData();
		packet.actionType = 0;
		packet.xPosition = xCoord;
		packet.yPosition = yCoord;
		packet.zPosition = zCoord;
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		packet.customParam1 = nbt;
		return packet;
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.customParam1);
	}

	@Override
	public void onInventoryChanged(BaseInventory inventory) {
		hasChanged = true;
	}

	@Override
	public void onTankChanged(BaseTankContainer tankContainer, int index) {
		hasChanged = true;
	}

	@Override
	public void onServerButtonClicked(EntityPlayer player, int button) {
		onClientButtonClicked(button);
	}

	@Override
	public void onClientButtonClicked(int button) {
		switch(button) {
		case BUTTON_LOWEST:
			mode.setValue(MODE_LOWEST);
			break;
		case BUTTON_HIGHEST:
			mode.setValue(MODE_HIGHEST);
			break;
		}
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
		if (side == BlockSide.BOTTOM) {
			return new int[] { OUTPUT_STACK };
		}
		return new int[] { INPUT_STACK };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side) {
		return isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side) {
		return true;
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
}
