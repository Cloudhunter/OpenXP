package openxp.common.tileentity;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import openxp.api.IHasSimpleGui;
import openxp.common.core.BaseInventory;
import openxp.common.core.BaseTankContainer;
import openxp.common.core.BaseTileEntity;
import openxp.common.core.GuiValueHolder;
import openxp.common.core.IInventoryCallback;
import openxp.common.core.ITankCallback;
import openxp.common.core.SyncableInt;
import openxp.common.util.BlockSide;
import openxp.common.util.EnchantmentUtils;
/**
 * The TileEntity object for the Automatic Anvil
 *
 * @author SinZ
 */
public class TileEntityAutoAnvil extends BaseTileEntity implements IInventory,
ISidedInventory, ITankContainer, IHasSimpleGui, IInventoryCallback,
ITankCallback  {

	public final static int[] SLOTS = new int[] { 48, 24, 48, 47, 102, 34 };
	
	public final static int INPUT_STACK = 0;
	public final static int MODIFIER_STACK = 1;
	public final static int OUTPUT_STACK = 2;

	protected BaseTankContainer tanks = new BaseTankContainer(
			new LiquidTank(
					EnchantmentUtils.XPToLiquidRatio(
							EnchantmentUtils.getExperienceForLevel(39)
					)
			)
	);
	protected BaseInventory inventory = new BaseInventory("autoAnvil", true, 3);
	protected boolean hasChanged = false;

	/**
	 * The progress bar in the GUI
	 */
	protected SyncableInt progress = new SyncableInt("progress");
	private SyncableInt percentStored = new SyncableInt("percentStored");
	private SyncableInt percentRequired = new SyncableInt("percentRequired");
	protected GuiValueHolder guiValues = new GuiValueHolder(percentStored, percentRequired, progress);
	private int liquidRequired = 0;
	private int stackSizeToBeUsedInRepair;
	
	public TileEntityAutoAnvil() {
		inventory.addCallback(this);
		tanks.addCallback(this);
	}

	
	@Override
	public void updateEntity() {

		super.updateEntity();

		if (!worldObj.isRemote){
			if (hasChanged) {
				liquidRequired = updateRepairOutput(false);	
			}
			if (liquidRequired == 0) {
				progress.setValue(0);
			}
			if (liquidRequired > 0 && tanks.getTankAmount() >= liquidRequired) {
				progress.add(1);
				if (progress.getValue() >= getSpeed()) {
					liquidRequired = updateRepairOutput(true);
					progress.setValue(0);
				}
			}

			percentStored.setValue((int) tanks.getPercentFull());
			percentRequired.setValue((int)(100.0 / tanks.getCapacity() * liquidRequired));
			
			hasChanged = false;
		}
	}
	
	public int updateRepairOutput(boolean doIt)
    {
        ItemStack inputStack = inventory.getStackInSlot(INPUT_STACK);
        int maximumCost = 0;
        int i = 0;
        byte b0 = 0;
        int j = 0;
        
        if (inputStack == null) {
        	return 0;
        }
        
        ItemStack inputStackCopy = inputStack.copy();
        ItemStack modifierStack = inventory.getStackInSlot(MODIFIER_STACK);
        Map inputStackEnchantments = EnchantmentHelper.getEnchantments(inputStackCopy);
        boolean flag = false;
        int k = b0 + inputStack.getRepairCost() + (modifierStack == null ? 0 : modifierStack.getRepairCost());
        this.stackSizeToBeUsedInRepair = 0;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        Iterator iterator;
        Enchantment enchantment;

        if (modifierStack != null)
        {
            flag = modifierStack.itemID == Item.enchantedBook.itemID && Item.enchantedBook.func_92110_g(modifierStack).tagCount() > 0;

            if (inputStackCopy.isItemStackDamageable() && Item.itemsList[inputStackCopy.itemID].getIsRepairable(inputStack, modifierStack))
            {
                l = Math.min(inputStackCopy.getItemDamageForDisplay(), inputStackCopy.getMaxDamage() / 4);

                if (l <= 0)
                {
                    return 0;
                }

                for (i1 = 0; l > 0 && i1 < modifierStack.stackSize; ++i1)
                {
                    j1 = inputStackCopy.getItemDamageForDisplay() - l;
                    inputStackCopy.setItemDamage(j1);
                    i += Math.max(1, l / 100) + inputStackEnchantments.size();
                    l = Math.min(inputStackCopy.getItemDamageForDisplay(), inputStackCopy.getMaxDamage() / 4);
                }

                this.stackSizeToBeUsedInRepair = i1;
            }
            else
            {
                if (!flag && (inputStackCopy.itemID != modifierStack.itemID || !inputStackCopy.isItemStackDamageable()))
                {
                    return 0;
                }

                if (inputStackCopy.isItemStackDamageable() && !flag)
                {
                    l = inputStack.getMaxDamage() - inputStack.getItemDamageForDisplay();
                    i1 = modifierStack.getMaxDamage() - modifierStack.getItemDamageForDisplay();
                    j1 = i1 + inputStackCopy.getMaxDamage() * 12 / 100;
                    int i2 = l + j1;
                    k1 = inputStackCopy.getMaxDamage() - i2;

                    if (k1 < 0)
                    {
                        k1 = 0;
                    }

                    if (k1 < inputStackCopy.getItemDamage())
                    {
                        inputStackCopy.setItemDamage(k1);
                        i += Math.max(1, j1 / 100);
                    }
                }

                Map map1 = EnchantmentHelper.getEnchantments(modifierStack);
                iterator = map1.keySet().iterator();

                while (iterator.hasNext())
                {
                    j1 = ((Integer)iterator.next()).intValue();
                    enchantment = Enchantment.enchantmentsList[j1];
                    k1 = inputStackEnchantments.containsKey(Integer.valueOf(j1)) ? ((Integer)inputStackEnchantments.get(Integer.valueOf(j1))).intValue() : 0;
                    l1 = ((Integer)map1.get(Integer.valueOf(j1))).intValue();
                    int j2;

                    if (k1 == l1)
                    {
                        ++l1;
                        j2 = l1;
                    }
                    else
                    {
                        j2 = Math.max(l1, k1);
                    }

                    l1 = j2;
                    int k2 = l1 - k1;
                    boolean flag1 = enchantment.canApply(inputStack);

                    Iterator iterator1 = inputStackEnchantments.keySet().iterator();

                    while (iterator1.hasNext())
                    {
                        int l2 = ((Integer)iterator1.next()).intValue();

                        if (l2 != j1 && !enchantment.canApplyTogether(Enchantment.enchantmentsList[l2]))
                        {
                            flag1 = false;
                            i += k2;
                        }
                    }

                    if (flag1)
                    {
                        if (l1 > enchantment.getMaxLevel())
                        {
                            l1 = enchantment.getMaxLevel();
                        }

                        inputStackEnchantments.put(Integer.valueOf(j1), Integer.valueOf(l1));
                        int i3 = 0;

                        switch (enchantment.getWeight())
                        {
                            case 1:
                                i3 = 8;
                                break;
                            case 2:
                                i3 = 4;
                            case 3:
                            case 4:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            default:
                                break;
                            case 5:
                                i3 = 2;
                                break;
                            case 10:
                                i3 = 1;
                        }

                        if (flag)
                        {
                            i3 = Math.max(1, i3 / 2);
                        }

                        i += i3 * k2;
                    }
                }
            }

            l = 0;

            for (iterator = inputStackEnchantments.keySet().iterator(); iterator.hasNext(); k += l + k1 * l1)
            {
                j1 = ((Integer)iterator.next()).intValue();
                enchantment = Enchantment.enchantmentsList[j1];
                k1 = ((Integer)inputStackEnchantments.get(Integer.valueOf(j1))).intValue();
                l1 = 0;
                ++l;

                switch (enchantment.getWeight())
                {
                    case 1:
                        l1 = 8;
                        break;
                    case 2:
                        l1 = 4;
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;
                    case 5:
                        l1 = 2;
                        break;
                    case 10:
                        l1 = 1;
                }

                if (flag)
                {
                    l1 = Math.max(1, l1 / 2);
                }
            }

            if (flag)
            {
                k = Math.max(1, k / 2);
            }

            if (flag && inputStackCopy!=null && !Item.itemsList[inputStackCopy.itemID].isBookEnchantable(inputStackCopy,modifierStack))
            {
                inputStackCopy = null;
            }

            maximumCost = k + i;

            if (i <= 0)
            {
                inputStackCopy = null;
            }

            if (inputStackCopy != null)
            {
                i1 = inputStackCopy.getRepairCost();

                if (modifierStack != null && i1 < modifierStack.getRepairCost())
                {
                    i1 = modifierStack.getRepairCost();
                }

                if (inputStackCopy.hasDisplayName())
                {
                    i1 -= 9;
                }

                if (i1 < 0)
                {
                    i1 = 0;
                }

                i1 += 2;
                inputStackCopy.setRepairCost(i1);
                EnchantmentHelper.setEnchantments(inputStackEnchantments, inputStackCopy);
                
                int requiredXP = EnchantmentUtils.getExperienceForLevel(maximumCost);
                int requiredLiquid = EnchantmentUtils.XPToLiquidRatio(requiredXP);
                if (tanks.getTankAmount() >= requiredLiquid && doIt) {
                	tanks.drain(requiredLiquid, true);
		            inventory.setInventorySlotContents(INPUT_STACK, null);
		            if (flag) {
		            	stackSizeToBeUsedInRepair = 1;
		            }
		            inventory.decrStackSize(MODIFIER_STACK, stackSizeToBeUsedInRepair);
		            inventory.setInventorySlotContents(OUTPUT_STACK, inputStackCopy);
		            progress.setValue(0);
		            return 0;
                }
                return requiredLiquid;
            }
        }
        return 0;
        
    }

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inventory.readFromNBT(tag);
		tanks.readFromNBT(tag);
		percentStored.readFromNBT(tag);
		percentRequired.readFromNBT(tag);
		progress.readFromNBT(tag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		inventory.writeToNBT(tag);
		tanks.writeToNBT(tag);
		percentStored.writeToNBT(tag);
		percentRequired.writeToNBT(tag);
		progress.writeToNBT(tag);
	}
	
	public double getPercentProgress() {
		return 100.0 / getSpeed() * getProgress();
	}

	public int getPercentRequired() {
		return percentRequired.getValue();
	}

	public int getPercentStored() {
		return percentStored.getValue();
	}

	public int getProgress() {
		return progress.getValue();
	}

	public int getRotation() {
		if (worldObj == null) {
			return 0;
		}
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	public int getSpeed() {
		return 20;
	}

	/* IInventory implementation */
	
	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side) {
		return true;
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
	public int[] getAccessibleSlotsFromSide(int side) {
		if (side == BlockSide.TOP) {
			return new int[] { INPUT_STACK };
		}else if (side == BlockSide.BOTTOM) {
			return new int[] { MODIFIER_STACK };
		}
		return new int[] { OUTPUT_STACK };
	}

	@Override
	public int getInventoryStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	@Override
	public String getInvName() {
		return inventory.getInvName();
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
	public ItemStack getStackInSlotOnClosing(int i) {
		return inventory.getStackInSlotOnClosing(i);
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
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory.setInventorySlotContents(i, itemstack);
	}
	
	@Override
	public void openChest() {
		inventory.openChest();
	}
	
	/* ITankContainer implementation */

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tanks.getTank(direction, type);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return tanks.getTanks(direction);
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
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return tanks.fill(from, resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return tanks.fill(tankIndex, resource, doFill);
	}

	/* ITankCallback implementation */
	
	@Override
	public void onTankChanged(BaseTankContainer tankContainer, int index) {
		hasChanged = true;
	}


	/* IHasSimpleGui Implementation */

	@Override
	public void onClientButtonClicked(int button) {

	}
	
	@Override
	public void setGuiValue(int i, int value) {
		guiValues.get(i).setValue(value);
	}

	@Override
	public void onServerButtonClicked(EntityPlayer player, int button) {
		onClientButtonClicked(button);
	}

	@Override
	public int getGuiValue(int index) {
		return guiValues.get(index).getValue();
	}

	@Override
	public int[] getGuiValues() {
		return guiValues.asIntArray();
	}
	
	/* IInventoryCallback implementation */
	
	@Override
	public void onInventoryChanged(BaseInventory inventory) {
		hasChanged = true;
	}
}
