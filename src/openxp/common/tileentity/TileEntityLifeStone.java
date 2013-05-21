package openxp.common.tileentity;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import openxp.OpenXP;
import openxp.api.IHasSimpleGui;
import openxp.common.core.BaseTankContainer;
import openxp.common.core.BaseTileEntity;
import openxp.common.core.GuiValueHolder;
import openxp.common.core.SyncableInt;
import openxp.common.util.EnchantmentUtils;

public class TileEntityLifeStone extends BaseTileEntity implements ITankContainer, IHasSimpleGui {

	protected BaseTankContainer tanks = new BaseTankContainer(
		new LiquidTank(EnchantmentUtils.LIQUID_PER_XP_BOTTLE * 2)
	);
	
	private AxisAlignedBB bounds;
	private Vec3 tilePosition;
	
	private int counter = 0;
	private int targetId = -1;
	private SyncableInt range = new SyncableInt("range", 16);
	
	private Vec3 healingLocation;
	private AxisAlignedBB searchBounds;
	
	protected GuiValueHolder guiValues = new GuiValueHolder(range);

	@Override
	public void initialize() {
		healingLocation = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
		searchBounds = AxisAlignedBB.getBoundingBox(
				xCoord - range.getValue(),
				yCoord - range.getValue(),
				zCoord - range.getValue(),
				xCoord + range.getValue()+1,
				yCoord + range.getValue()+1,
				zCoord + range.getValue()+1
		);
		tilePosition = Vec3.createVectorHelper(xCoord + 0.5, yCoord, zCoord + 0.5);
	}
	

	@Override
	public void updateEntity() {
		super.updateEntity();
		
		EntityLiving target = null;
		if (!worldObj.isRemote && (++counter % 6 == 0)) {
			if (targetId == -1) {
				targetId = findNewTarget();
			}
		}
			
		if (targetId != -1) {
			target = (EntityLiving)worldObj.getEntityByID(targetId);
		}
		
		Vec3 targetPosition = null;
		if (target != null) {
			targetPosition = target.getPosition(0);
		}
		
		if (targetPosition == null || targetPosition.distanceTo(tilePosition) > range.getValue()) {
			target = null;
			targetId = -1;
		}
		
		if (target instanceof EntityPlayer) {
			if (target.getHealth() >= target.getMaxHealth()) {
				target= null;
				targetId = -1;
			}
		}
		
		String particle = "spell";
		
		Vec3 targetLocation = null;
		
		if (target == null) {
			targetLocation = tilePosition;
		}else {
			targetLocation = ((EntityLiving)target).getPosition(0);
		}
		
		double distance = targetLocation.distanceTo(healingLocation);
		
		Vec3 v = healingLocation.subtract(targetLocation);
		Vec3 moveDir = v.normalize();
		
		healingLocation.xCoord += moveDir.xCoord * 0.2;
		healingLocation.zCoord += moveDir.zCoord * 0.2;
		healingLocation.yCoord += moveDir.yCoord * 0.2;
		
		if (target != null && distance < 1.5) {
			
			if (target instanceof EntityPlayer) {
				
				if (!worldObj.isRemote) {
					((EntityPlayer) target).heal(1);
				}else {
					particle = "heart";
				}
				
			}else if (target instanceof IMob) {
				
				if (!worldObj.isRemote) {
					((EntityLiving)target).attackEntityFrom(DamageSource.magic, 1);
					if (target.isDead) {
						target = null;
					}
				}else {
					particle = "largesmoke";
				}
				
			}
		}

		if (!worldObj.isRemote && counter % 30 == 0) {
			markForUpdate();
		}
		
		if (worldObj.isRemote){
				
			  double distanceFromTile = tilePosition.distanceTo(healingLocation);
			  distanceFromTile = Math.min(distanceFromTile, 6);
			  distanceFromTile /= 6;
			  
			  worldObj.spawnParticle(
				particle,
				healingLocation.xCoord + ((worldObj.rand.nextDouble() * 2 - 1) * distanceFromTile),
				healingLocation.yCoord + ((worldObj.rand.nextDouble() * 2 - 1) * distanceFromTile),
				healingLocation.zCoord + ((worldObj.rand.nextDouble() * 2 - 1) * distanceFromTile),
				0.0D, 0.0D, 0.0D);
		}
		
	}

	private int findNewTarget() {
		List<EntityPlayer> players = (List<EntityPlayer>) worldObj.getEntitiesWithinAABB(EntityPlayer.class, searchBounds);
		for (EntityPlayer player : players) {
			if (player.getPosition(0).distanceTo(tilePosition) < range.getValue() && player.getHealth() < player.getMaxHealth()) {
				return player.entityId;
			}
		}
		List<EntityLiving> mobs = (List<EntityLiving>) worldObj.getEntitiesWithinAABB(EntityLiving.class, searchBounds);
		for (EntityLiving mob : mobs) {
			if (mob instanceof IMob && mob.getPosition(0).distanceTo(tilePosition) < range.getValue()) {
				return mob.entityId;
			}
		}
		return -1;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		range.readFromNBT(tag);
		tanks.writeToNBT(tag);
	}

	@Override
	public void readFromNetwork(NBTTagCompound tag) {
		
		if (healingLocation == null) {
			healingLocation = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
		}
		
		healingLocation.xCoord = tag.getDouble("hX");
		healingLocation.yCoord = tag.getDouble("hY");
		healingLocation.zCoord = tag.getDouble("hZ");
		targetId = tag.getInteger("tid");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		range.writeToNBT(tag);
		tanks.writeToNBT(tag);
	}

	@Override
	public void writeToNetwork(NBTTagCompound tag) {
		tag.setDouble("hX", healingLocation.xCoord);
		tag.setDouble("hY", healingLocation.yCoord);
		tag.setDouble("hZ", healingLocation.zCoord);
		tag.setInteger("tid", targetId);
	}
	
	/* ITankContainer implementation */

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}
	
	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return null;
	}
	
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		if (resource != null && resource.itemID == OpenXP.liquidStack.itemID) {
			tanks.fill(resource, doFill);
		}
		return 0;
	}
	
	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return fill(ForgeDirection.UNKNOWN, resource, doFill);
	}
	
	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tanks.getTank(direction, type);
	}
	
	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return tanks.getTanks(direction);
	}
	
	/* IHasSimpleGui implemenation */

	@Override
	public void onClientButtonClicked(int button) {
	}

	@Override
	public void onServerButtonClicked(EntityPlayer player, int button) {
	}

	@Override
	public void setGuiValue(int i, int value) {
		guiValues.get(i).setValue(value);
		if (guiValues.get(i) == range) {
			searchBounds = AxisAlignedBB.getBoundingBox(
					xCoord - range.getValue(),
					yCoord - range.getValue(),
					zCoord - range.getValue(),
					xCoord + range.getValue()+1,
					yCoord + range.getValue()+1,
					zCoord + range.getValue()+1
			);
		}
	}

	@Override
	public int getGuiValue(int index) {
		return guiValues.get(index).getValue();
	}

	@Override
	public int[] getGuiValues() {
		return guiValues.asIntArray();
	}
}
