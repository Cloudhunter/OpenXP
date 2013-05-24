package openxp.common.tileentity;

import java.util.List;

import net.minecraft.entity.Entity;
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
import openxp.api.IHasSimpleGui;
import openxp.common.core.BaseTankContainer;
import openxp.common.core.BaseTileEntity;
import openxp.common.core.GuiValueHolder;
import openxp.common.core.SyncableInt;
import openxp.common.core.XPTank;
import openxp.common.util.EnchantmentUtils;
import openxp.common.util.VectorUtils;

public class TileEntityLifeStone extends BaseTileEntity implements ITankContainer, IHasSimpleGui {

	protected BaseTankContainer tanks = new BaseTankContainer(
		new XPTank(EnchantmentUtils.LIQUID_PER_XP_BOTTLE * 4)
	);

	public enum Button {
		HEAL_PLAYERS {
	        @Override
	        public void onClick(TileEntityLifeStone lifeStone) {
	        	lifeStone.getMode().toggle(MODE_HEAL_PLAYERS);
	        }
	    },
		DAMAGE_MOBS {
	        @Override
	        public void onClick(TileEntityLifeStone lifeStone) {
				lifeStone.getMode().toggle(MODE_DAMAGE_MOBS);
	        }
	    },
		INCREASE_RANGE {
	        @Override
	        public void onClick(TileEntityLifeStone lifeStone) {
				lifeStone.setRange(1);
	        }
	    },
		DECREASE_RANGE {
	        @Override
	        public void onClick(TileEntityLifeStone lifeStone) {
	        	lifeStone.setRange(-1);
	        }
	    };

		public void onClick(TileEntityLifeStone lifeStone) {
		}
	}

	public static final int MODE_HEAL_PLAYERS = 0x1;
	public static final int MODE_DAMAGE_MOBS = 0x2;

	private AxisAlignedBB bounds;
	private Vec3 tilePosition;

	private int counter = 0;
	private int targetId = -1;
	private SyncableInt range = new SyncableInt("range", 16);
	private SyncableInt percentStored = new SyncableInt("ps");
	private SyncableInt mode = new SyncableInt("mode", MODE_HEAL_PLAYERS  | MODE_DAMAGE_MOBS);

	private Vec3 healingLocation;
	private AxisAlignedBB searchBounds;

	protected GuiValueHolder guiValues = new GuiValueHolder(range, percentStored);

	protected double actualTankAmount = 0;

	@Override
	public void initialize() {
		searchBounds = AxisAlignedBB.getBoundingBox(
				xCoord - range.getValue(),
				yCoord - range.getValue(),
				zCoord - range.getValue(),
				xCoord + range.getValue()+1,
				yCoord + range.getValue()+1,
				zCoord + range.getValue()+1
			);
		healingLocation = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
		tilePosition = Vec3.createVectorHelper(xCoord + 0.5, yCoord, zCoord + 0.5);
	}

	public double getCostPerTick() {
		double base = (double) range.getValue() / 100;
		int multiplier = 0;
		if (isDamagingMobs()) multiplier++;
		if (isHealingPlayers()) multiplier++;
		return base * multiplier;
	}

	public boolean isHealingPlayers() {
		return mode.is(MODE_HEAL_PLAYERS);
	}

	public boolean isDamagingMobs() {
		return mode.is(MODE_DAMAGE_MOBS);
	}

	@Override
	public void updateEntity() {

		super.updateEntity();

		EntityLiving target = null;

		if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
			percentStored.setValue((int)tanks.getPercentFull());
			targetId = -1;
			return;
		}

		if (!worldObj.isRemote) {

			actualTankAmount -= getCostPerTick();

			actualTankAmount = Math.max(actualTankAmount, 0);

			if (tanks.getLiquid() != null) {
				tanks.getLiquid().amount = (int) actualTankAmount;
			}

			percentStored.setValue((int)tanks.getPercentFull());

		}


		if (!worldObj.isRemote && actualTankAmount > 10) {
			if (targetId == -1) {
				targetId = findNewTarget();
			}
		}

		if (targetId != -1) {
			target = (EntityLiving)worldObj.getEntityByID(targetId);
		}

		Vec3 targetPosition = null;
		if (target != null) {
			targetPosition = getEntityPosition(target);
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
			targetLocation = getEntityPosition(target);
		}

		double distance = targetLocation.distanceTo(healingLocation);

		Vec3 v = VectorUtils.subtract(healingLocation, targetLocation);

		Vec3 moveDir = v.normalize();

		healingLocation.xCoord += moveDir.xCoord * 0.2;
		healingLocation.zCoord += moveDir.zCoord * 0.2;
		healingLocation.yCoord += moveDir.yCoord * 0.2;

		if (target != null && distance < 1.5) {

			if (target instanceof EntityPlayer) {

				if (!worldObj.isRemote) {
					if (tanks.getTankAmount() >= 10) {
						tanks.drain(3, true);
						actualTankAmount -= 3;
						((EntityPlayer) target).heal(1);
					}else {
						targetId = -1;
					}
				}else {
					particle = "heart";
				}

			}else if (target instanceof IMob) {

				if (!worldObj.isRemote) {
					if (tanks.getTankAmount() >= 10) {
						tanks.drain(3, true);
						actualTankAmount -= 3;
						((EntityLiving)target).attackEntityFrom(DamageSource.magic, 1);
					}else {
						targetId = -1;
					}
					if (target.isDead) {
						target = null;
					}
				}else {
					particle = "largesmoke";
				}

			}
		}

		if (!worldObj.isRemote && counter % 12 == 0) {
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

	private Vec3 getEntityPosition(Entity e) {
		return Vec3.createVectorHelper(e.posX, e.posY, e.posZ);
	}

	private int findNewTarget() {
		if (isHealingPlayers()) {
			List<EntityPlayer> players = (List<EntityPlayer>) worldObj.getEntitiesWithinAABB(EntityPlayer.class, searchBounds);
			for (EntityPlayer player : players) {
				if (getEntityPosition(player).distanceTo(tilePosition) < range.getValue() && player.getHealth() < player.getMaxHealth()) {
					return player.entityId;
				}
			}
		}
		if (isDamagingMobs()) {
			List<EntityLiving> mobs = (List<EntityLiving>) worldObj.getEntitiesWithinAABB(EntityLiving.class, searchBounds);
			for (EntityLiving mob : mobs) {
				if (mob instanceof IMob && getEntityPosition(mob).distanceTo(tilePosition) < range.getValue()) {
					return mob.entityId;
				}
			}
		}
		return -1;
	}

	public SyncableInt getMode() {
		return mode;
	}

	public int getPercentStored() {
		return percentStored.getValue();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		range.readFromNBT(tag);
		setRange(0);
		tanks.readFromNBT(tag);
		actualTankAmount = tanks.getTankAmount();
		mode.readFromNBT(tag);
	}

	@Override
	public void readFromNetwork(NBTTagCompound tag) {

		if (healingLocation == null) {
			healingLocation = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
		}
		if (tag.hasKey("hX")) {
			healingLocation.xCoord = tag.getDouble("hX");
			healingLocation.yCoord = tag.getDouble("hY");
			healingLocation.zCoord = tag.getDouble("hZ");
		}
		targetId = tag.getInteger("tid");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		range.writeToNBT(tag);
		tanks.writeToNBT(tag);
		mode.writeToNBT(tag);
	}

	@Override
	public void writeToNetwork(NBTTagCompound tag) {
		if (healingLocation != null) {
			tag.setDouble("hX", healingLocation.xCoord);
			tag.setDouble("hY", healingLocation.yCoord);
			tag.setDouble("hZ", healingLocation.zCoord);
			tag.setInteger("tid", targetId);
		}
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
		int amount = tanks.fill(resource, doFill);
		actualTankAmount += amount;
		return amount;
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
		Button.values()[button].onClick(this);
	}

	public void setRange(int v) {
		if ((range.getValue() > 0 && v < 0) || (range.getValue() < 30 && v > 0)) {
			range.add(v);
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
	public void onServerButtonClicked(EntityPlayer player, int button) {
		onClientButtonClicked(button);
	}

	@Override
	public void setGuiValue(int i, int value) {
		guiValues.get(i).setValue(value);
	}

	@Override
	public int getGuiValue(int index) {
		return guiValues.get(index).getValue();
	}

	@Override
	public int[] getGuiValues() {
		return guiValues.asIntArray();
	}

	public SyncableInt getRange() {
		return range;
	}
}
