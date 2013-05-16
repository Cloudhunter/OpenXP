package openxp.common.tileentity;

import java.util.List;

import openxp.client.core.BaseTankContainer;
import openxp.client.core.BaseTileEntity;
import openxp.client.core.ITankCallback;
import openxp.client.core.SavableInt;
import openxp.common.util.EnchantmentUtils;

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

public class TileEntityXPSponge extends BaseTileEntity implements ITankContainer {

	protected BaseTankContainer tanks = new BaseTankContainer(new LiquidTank(EnchantmentUtils.LEVEL_30));
	
	private SavableInt lastFilled = new SavableInt("lastFilled");
	
	private AxisAlignedBB suckupBounds;
	private AxisAlignedBB destroyBounds;
	
	public TileEntityXPSponge() {
		
	}
	
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
					tanks.fill(LiquidDictionary.getLiquid("liquidxp", entity.getXpValue()), true);
					entity.setDead();
				}
			}
		
		}

		if (!worldObj.isRemote) { 
			int filled = (int)Math.round(tanks.getPercentFull());
			if (filled != lastFilled.getValue()) {
				lastFilled.setValue(filled);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}

	}
	
	public LiquidStack getLiquidStack() {
		return tanks.getLiquid();
	}
	
	public int getLastFilled() {
		return lastFilled.getValue();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		tanks.readFromNBT(tag);
		lastFilled.readFromNBT(tag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tanks.writeToNBT(tag);
		lastFilled.writeToNBT(tag);
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return 0;
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
	
}
