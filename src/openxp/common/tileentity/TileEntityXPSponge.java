package openxp.common.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class TileEntityXPSponge extends TileEntity implements ITankContainer {

	public final LiquidTank tank = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 5);

	private int lastFilled = 0;
	
	AxisAlignedBB suckupBounds;
	AxisAlignedBB destroyBounds;
	
	private void createBounds() {
		if (suckupBounds == null) {
			suckupBounds = AxisAlignedBB.getBoundingBox(
					xCoord - 16,
					yCoord - 16,
					zCoord - 16,
					xCoord + 17,
					yCoord + 17,
					zCoord + 17
			);
			destroyBounds = AxisAlignedBB.getBoundingBox(
					xCoord - 1,
					yCoord - 1,
					zCoord - 1,
					xCoord + 2,
					yCoord + 2,
					zCoord + 2
			);
		}
	}
	
	@Override
	public void updateEntity() {

		createBounds();
		
		List<EntityXPOrb> orbs = (List<EntityXPOrb>) worldObj.getEntitiesWithinAABB(EntityXPOrb.class, suckupBounds);
		
		for (EntityXPOrb entity : orbs) {

			double x = (xCoord + 0.5D - entity.posX) / 15.0D;
			double y = (yCoord + 0.5D - entity.posY) / 15.0D;
			double z = (zCoord + 0.5D - entity.posZ) / 15.0D;
			
			double distance = Math.sqrt(x * x + y * y + z * z);
			double var11 = 1.0D - distance;
			
			if (var11 > 0.0D) {
				var11 *= var11;
				entity.motionX += x / distance * var11 * 0.05;
				entity.motionY += y / distance * var11 * 0.2;
				entity.motionZ += z / distance * var11 * 0.05;
			}
			
			if (!worldObj.isRemote) {
				if (destroyBounds.isVecInside(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ))) {
					if (getAmountFilled() == 0) {
						worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
					}
					tank.fill(LiquidDictionary.getLiquid("liquidxp", entity.getXpValue()), true);
					entity.setDead();
				}
			}
		
		}

		if (!worldObj.isRemote) { 

			int filled = getAmountFilled()/100;
			if (filled != lastFilled) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			lastFilled = filled;
			
		}

	}
	
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return new ILiquidTank[]{tank};
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tank;
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
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		LiquidStack liquid = LiquidStack.loadLiquidStackFromNBT(data.getCompoundTag("tank"));
		if (liquid != null) {
			tank.setLiquid(liquid);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		if (tank.containsValidLiquid()) {
			data.setTag("tank", tank.getLiquid().writeToNBT(new NBTTagCompound()));
		}
	}
	
	public LiquidStack getLiquidStack() {
		return tank.getLiquid();
	}
	
	public int getAmountFilled() {
		LiquidStack liquid = tank.getLiquid();
		return liquid == null ? 0 : liquid.amount;
	}

	public double getPercentFull() {
		return 1.0/tank.getCapacity() * getAmountFilled();
	}
}
