package openxp.common.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityXPIngester extends BaseInventoryTileEntity implements
		IInventory {

	public static final int[] SLOTS = { 0, 0, 40, 40 };
	
	public static final int MAX_STORAGE = 1000;
	
	private int internalStorage = 0;
	
	private int previousInternalStorage = 0;
	
	public TileEntityXPIngester () {
		itemStacks = new ItemStack[2];
	}
	
	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) { 
			
			if (internalStorage >= 10) {
				
				ItemStack emptyBottles = itemStacks[0];
				ItemStack outputStack = itemStacks[1];
				
				if (emptyBottles != null) {
					
					if (outputStack != null) {
						if (outputStack.getItem() != Item.expBottle) {
							return;
						}
						if (outputStack.stackSize >= outputStack.getMaxStackSize()) {
							return;
						}
					}
					
					Item bottleItem = emptyBottles.getItem();
					if (bottleItem == Item.glassBottle) {
						internalStorage -= 10;
						if (itemStacks[1] != null) {
							itemStacks[1].stackSize++;
						}else {
							itemStacks[1] = new ItemStack(Item.expBottle);
						}
						decrStackSize(0, 1);
					}
				
				}
			
			}
			if (internalStorage != previousInternalStorage) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			previousInternalStorage = internalStorage;
		}
	}
	
	public int getInternalStorage() {
		return internalStorage;
	}

	@Override
	public String getInvName() {
		return "dnacraft.machines.electroporator";
	}

	public boolean hasInputStack() {
		return itemStacks[0] != null;
	}
	
	public boolean hasOutputStack() {
		return itemStacks[1] != null;
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
	
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		internalStorage = nbttagcompound.getInteger("internalStorage");
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("internalStorage", internalStorage);
		super.writeToNBT(nbttagcompound);
	}
}
