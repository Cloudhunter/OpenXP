package openxp.common.tileentity;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import openxp.OpenXP;
import openxp.api.IHasSimpleGui;
import openxp.client.core.BaseInventory;
import openxp.client.core.BaseTankContainer;
import openxp.client.core.BaseTileEntity;
import openxp.client.core.GuiValueHolder;
import openxp.client.core.IInventoryCallback;
import openxp.client.core.ITankCallback;
import openxp.client.core.SavableInt;
import openxp.common.util.EnchantmentUtils;
/**
 * The TileEntity object for the Automatic Anvil
 *
 * @author SinZ
 */
public class TileEntityAutoAnvil extends BaseTileEntity implements IInventory,
ISidedInventory, ITankContainer, IHasSimpleGui, IInventoryCallback,
ITankCallback  {
	
    /** The maximum cost of repairing/renaming in the anvil. */
    public int maximumCost = 0;

    /** determined by damage of input item and stackSize of repair materials */
    private int stackSizeToBeUsedInRepair = 0;
    private String repairedItemName;
    
	public final static int[] SLOTS = new int[] { 10, 33, 10, 54, 150, 33 };
	
	public final static int BUTTON_LOWEST = 0;
	public final static int BUTTON_HIGHEST = 1;

	public final static int MODE_LOWEST = 0;
	public final static int MODE_HIGHEST = 1;

	public final static int INPUT_STACK = 0;
	public final static int MODIFIER_STACK = 1;
	public final static int OUTPUT_STACK = 2;

	protected SavableInt mode = new SavableInt("mode");
	protected SavableInt levelsAvailable = new SavableInt("levelsAvailable");
	protected BaseTankContainer tanks = new BaseTankContainer(new LiquidTank(EnchantmentUtils.LEVEL_30));
	protected BaseInventory inventory = new BaseInventory("autoAnvil", true, 3);
	protected boolean hasChanged = false;
	protected GuiValueHolder guiValues = new GuiValueHolder(mode, levelsAvailable);
	
	public TileEntityAutoAnvil() {
		inventory.addCallback(this);
		tanks.addCallback(this);
	}

	@Override
	public void updateEntity() {

		super.updateEntity();

		if (!worldObj.isRemote){

			if (hasChanged) {
				if (inventory.getStackInSlot(INPUT_STACK) != null && inventory.getStackInSlot(MODIFIER_STACK) != null && inventory.getStackInSlot(OUTPUT_STACK) == null) {
					updateRepairOutput();
				}
			}

			hasChanged = false;
		}

	}
	
    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
	public void updateRepairOutput()
    {
        ItemStack itemstack = inventory.getStackInSlot(INPUT_STACK);
        this.maximumCost = 0;
        int i = 0;
        byte b0 = 0;
        int j = 0;
        
        ItemStack itemstack1 = itemstack.copy();
        ItemStack itemstack2 = inventory.getStackInSlot(MODIFIER_STACK);
        Map map = EnchantmentHelper.getEnchantments(itemstack1);
        boolean flag = false;
        int k = b0 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
        this.stackSizeToBeUsedInRepair = 0;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        Iterator iterator;
        Enchantment enchantment;

        if (itemstack2 != null)
        {
            flag = itemstack2.itemID == Item.enchantedBook.itemID && Item.enchantedBook.func_92110_g(itemstack2).tagCount() > 0;

            if (itemstack1.isItemStackDamageable() && Item.itemsList[itemstack1.itemID].getIsRepairable(itemstack, itemstack2))
            {
                l = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);

                if (l <= 0)
                {
                    this.maximumCost = 0;
                    return;
                }

                for (i1 = 0; l > 0 && i1 < itemstack2.stackSize; ++i1)
                {
                    j1 = itemstack1.getItemDamageForDisplay() - l;
                    itemstack1.setItemDamage(j1);
                    i += Math.max(1, l / 100) + map.size();
                    l = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);
                }

                this.stackSizeToBeUsedInRepair = i1;
            }
            else
            {
                if (!flag && (itemstack1.itemID != itemstack2.itemID || !itemstack1.isItemStackDamageable()))
                {
                    this.maximumCost = 0;
                    return;
                }

                if (itemstack1.isItemStackDamageable() && !flag)
                {
                    l = itemstack.getMaxDamage() - itemstack.getItemDamageForDisplay();
                    i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamageForDisplay();
                    j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                    int i2 = l + j1;
                    k1 = itemstack1.getMaxDamage() - i2;

                    if (k1 < 0)
                    {
                        k1 = 0;
                    }

                    if (k1 < itemstack1.getItemDamage())
                    {
                        itemstack1.setItemDamage(k1);
                        i += Math.max(1, j1 / 100);
                    }
                }

                Map map1 = EnchantmentHelper.getEnchantments(itemstack2);
                iterator = map1.keySet().iterator();

                while (iterator.hasNext())
                {
                    j1 = ((Integer)iterator.next()).intValue();
                    enchantment = Enchantment.enchantmentsList[j1];
                    k1 = map.containsKey(Integer.valueOf(j1)) ? ((Integer)map.get(Integer.valueOf(j1))).intValue() : 0;
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
                    boolean flag1 = enchantment.canApply(itemstack);

                    Iterator iterator1 = map.keySet().iterator();

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

                        map.put(Integer.valueOf(j1), Integer.valueOf(l1));
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

            for (iterator = map.keySet().iterator(); iterator.hasNext(); k += l + k1 * l1)
            {
                j1 = ((Integer)iterator.next()).intValue();
                enchantment = Enchantment.enchantmentsList[j1];
                k1 = ((Integer)map.get(Integer.valueOf(j1))).intValue();
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

            if (flag && itemstack1!=null && !Item.itemsList[itemstack1.itemID].isBookEnchantable(itemstack1,itemstack2))
            {
                itemstack1 = null;
            }

            this.maximumCost = k + i;

            if (i <= 0)
            {
                itemstack1 = null;
            }

            if (itemstack1 != null)
            {
                i1 = itemstack1.getRepairCost();

                if (itemstack2 != null && i1 < itemstack2.getRepairCost())
                {
                    i1 = itemstack2.getRepairCost();
                }

                if (itemstack1.hasDisplayName())
                {
                    i1 -= 9;
                }

                if (i1 < 0)
                {
                    i1 = 0;
                }

                i1 += 2;
                itemstack1.setRepairCost(i1);
                EnchantmentHelper.setEnchantments(map, itemstack1);
                
                if (tanks.getTankAmount() >= i1) {
                	tanks.drain(i1, true);
		            inventory.setInventorySlotContents(INPUT_STACK, (ItemStack)null);
		            inventory.setInventorySlotContents(MODIFIER_STACK, (ItemStack)null);
		            inventory.setInventorySlotContents(OUTPUT_STACK, itemstack1);
                }
            }
        }
    }

	public int getLevelsAvailable() {
		return levelsAvailable.getValue();
	}

	public int getMode() {
		return mode.getValue();
	}

	public int getExperience() {
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
		return new int[0];
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
