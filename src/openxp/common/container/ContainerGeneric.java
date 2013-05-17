package openxp.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import openxp.api.IHasSimpleGui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerGeneric extends Container {
	
	private int inventorySize;
	private TileEntity tileentity;
	protected IInventory playerInventory;

	private int[] craftingProgress = new int[16];
	
	public ContainerGeneric(IInventory playerInventory, TileEntity tileentity, int[] slots) {
		this.inventorySize = slots.length / 2;
		this.playerInventory = playerInventory;
		this.tileentity = tileentity;
		
		for (int i = 0, slotId = 0; i < slots.length; i += 2, slotId++) {
			addSlotToContainer(new Slot((IInventory)tileentity, slotId, slots[i], slots[i+1]));
		}

		for (int l = 0; l < 3; l++) {
			for (int k1 = 0; k1 < 9; k1++) {
				addSlotToContainer(new Slot(playerInventory, k1 + l * 9 + 9, 8 + k1 * 18, 84 + l * 18));
			}
		}

		for (int i1 = 0; i1 < 9; i1++) {
			addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
		}

	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < inventorySize) {
				if (!mergeItemStack(itemstack1, inventorySize, inventorySlots.size(), true))
					return null;
			} else if (!mergeItemStack(itemstack1, 0, inventorySize, false))
				return null;
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	public TileEntity getTileEntity() {
		return this.tileentity;
	}

	public int getInventorySize() {
		return inventorySize;
	}

	public boolean enchantItem(EntityPlayer player, int button) {
		if (tileentity instanceof IHasSimpleGui) {
			((IHasSimpleGui)tileentity).onServerButtonClicked(player, button);
		}
		return false;
	}
	
	@Override
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
        if (tileentity instanceof IHasSimpleGui) {
	        int[] craftingValues = ((IHasSimpleGui)tileentity).getGuiValues();
	        for (int i = 0; i < craftingValues.length; i++) {
		        craftingProgress[i] = craftingValues[i];
	            crafting.sendProgressBarUpdate(this, i, craftingValues[i]);
	        }
        }
    }
	
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (tileentity instanceof IHasSimpleGui) {
        	
	        int[] newValues = ((IHasSimpleGui)tileentity).getGuiValues();
	        

	        for (int i = 0; i < this.crafters.size(); ++i)
	        {
	            ICrafting icrafting = (ICrafting)this.crafters.get(i);
	            
	        	for (int j = 0; j < newValues.length; j++ ) {

	        		if (craftingProgress[j] != newValues[j]) {

	                    icrafting.sendProgressBarUpdate(this, j, newValues[j]);
	        		
	        		}
	        	}
	        }
	        
	        for (int i  = 0; i < newValues.length; i++) {
	        	craftingProgress[i] = newValues[i];
	        }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (tileentity instanceof IHasSimpleGui) {
        	
        	((IHasSimpleGui)tileentity).setGuiValue(par1, par2);
        }
    }
}
