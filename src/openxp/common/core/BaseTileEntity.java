package openxp.common.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class BaseTileEntity extends TileEntity {
	
	protected boolean initialized = false;
	
	public void addBlockEvent(int eventId, int eventParam) {
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, eventId, eventParam);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		Packet132TileEntityData packet = new Packet132TileEntityData();
		packet.actionType = 0;
		packet.xPosition = xCoord;
		packet.yPosition = yCoord;
		packet.zPosition = zCoord;
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNetwork(nbt);
		packet.customParam1 = nbt;
		return packet;
	}
	
	protected void initialize() {
		
	}
	
	public void markForUpdate() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void onBlockEventReceived(int eventId, int eventParam) {
		
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNetwork(pkt.customParam1);
	}
	
	public void readFromNetwork(NBTTagCompound tag) {
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!initialized) {
			initialize();
			initialized = true;
		}
	}
	
	public void writeToNetwork(NBTTagCompound tag) {
	}
	
}
