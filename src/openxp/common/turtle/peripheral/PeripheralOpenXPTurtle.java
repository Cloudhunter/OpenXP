package openxp.common.turtle.peripheral;

import java.util.List;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import openxp.OpenXP;
import openxp.common.ccintegration.BaseTurtlePeripheral;
import openxp.common.ccintegration.LuaMethod;
import openxp.common.core.BaseTankContainer;
import openxp.common.util.EnchantmentUtils;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class PeripheralOpenXPTurtle extends BaseTurtlePeripheral implements IHostedPeripheral {

	protected BaseTankContainer tanks = new BaseTankContainer(new LiquidTank(
			EnchantmentUtils.XPToLiquidRatio(
						EnchantmentUtils.getExperienceForLevel(50)
					)
			)
	);

	public PeripheralOpenXPTurtle(ITurtleAccess turtle, TurtleSide side) {
		super(turtle, side);
	}

	@Override
	public String getType() {
		return "openxpturtle";
	}

	@Override
	public String[] getMethodNames() {
		return methodNames;
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {

	}

	@Override
	public void detach(IComputerAccess computer) {

	}

	@Override
	public void update() {

	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		tanks.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tanks.writeToNBT(tag);
	}

	@LuaMethod
	public int getXPStored(IComputerAccess computer) {
		return EnchantmentUtils.LiquidToXPRatio(tanks.getTankAmount());
	}

	@LuaMethod
	public int getLevelsStored(IComputerAccess computer) {
		return EnchantmentUtils.getLevelForExperience(getXPStored(computer));
	}

	@LuaMethod
	public int getLiquidVolume(IComputerAccess computer) {
		return tanks.getTankAmount();
	}

	@LuaMethod
	public int getLiquidCapacity(IComputerAccess computer) {
		return tanks.getCapacity();
	}

	@LuaMethod(onTick = true)
	public int dropUp(IComputerAccess computer, Double amount) {
		return dropIntoTank(0, amount, ForgeDirection.UP);
	}

	@LuaMethod(onTick = true)
	public int dropDown(IComputerAccess computer, Double amount) {
		return dropIntoTank(0, amount, ForgeDirection.DOWN);
	}

	@LuaMethod(onTick = true)
	public int drop(IComputerAccess computer, Double amount) {
		return dropIntoTank(0, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int dropBack(IComputerAccess computer, Double amount) {
		return dropIntoTank(2, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int dropLeft(IComputerAccess computer, Double amount) {
		return dropIntoTank(3, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int dropRight(IComputerAccess computer, Double amount) {
		return dropIntoTank(1, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int suck(IComputerAccess computer, Double amount) {
		return suckFromTank(0, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int suckDown(IComputerAccess computer, Double amount) {
		return suckFromTank(0, amount, ForgeDirection.DOWN);
	}

	@LuaMethod(onTick = true)
	public int suckUp(IComputerAccess computer, Double amount) {
		return suckFromTank(0, amount, ForgeDirection.UP);
	}

	@LuaMethod(onTick = true)
	public int suckBack(IComputerAccess computer, Double amount) {
		return suckFromTank(2, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int suckLeft(IComputerAccess computer, Double amount) {
		return suckFromTank(3, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int suckRight(IComputerAccess computer, Double amount) {
		return suckFromTank(1, amount, ForgeDirection.UNKNOWN);
	}

	@LuaMethod(onTick = true)
	public int collect(IComputerAccess computer) {
		int xpCollected = 0;
		Vec3 position = turtle.getPosition();
		AxisAlignedBB suckupBounds = AxisAlignedBB.getBoundingBox(
				position.xCoord - 2,
				position.yCoord - 2,
				position.zCoord - 2,
				position.xCoord + 3,
				position.yCoord + 3,
				position.zCoord + 3
				);
		List<EntityXPOrb> orbs = (List<EntityXPOrb>) turtle.getWorld().getEntitiesWithinAABB(EntityXPOrb.class, suckupBounds);
		for (EntityXPOrb orb : orbs) {
			xpCollected += orb.getXpValue();
			tanks.fill(LiquidDictionary.getLiquid("liquidxp", EnchantmentUtils.XPToLiquidRatio(orb.getXpValue())
					), true);
			orb.setDead();
		}
		return xpCollected;
	}


	private int dropIntoTank(int rotation, Double dAmount, ForgeDirection facingDirection) {

		int amount = (int)(double)dAmount;

		TileEntity target = getTileEntity(rotation, facingDirection);

		if (target != null && target instanceof ITankContainer) {

			int amountAvailable = Math.min(amount, tanks.getTankAmount());
			int amountFilled = ((ITankContainer) target).fill(facingDirection, createLiquid(amountAvailable), true);
			tanks.drain(amountFilled, true);

			return amountFilled;
		}
		return 0;
	}

	private int suckFromTank(int rotation, Double dAmount, ForgeDirection facingDirection) {

		int amount = (int)(double)dAmount;

		TileEntity target = getTileEntity(rotation, facingDirection);

		if (target != null && target instanceof ITankContainer) {

			int spaceAvailable = Math.min(tanks.getFreeSpace(), amount);

			LiquidStack drained = ((ITankContainer) target).drain(facingDirection, spaceAvailable, false);
			if (drained != null && drained.itemID == OpenXP.liquidStack.itemID) {
				drained = ((ITankContainer) target).drain(facingDirection, spaceAvailable, true);
				tanks.fill(createLiquid(drained.amount), true);
				return drained.amount;
			}

		}
		return 0;
	}

	private TileEntity getTileEntity(int rotation, ForgeDirection facingDirection) {
		if (facingDirection == ForgeDirection.UNKNOWN) {
			facingDirection = ForgeDirection.getOrientation(turtle.getFacingDir());
			for (int i = 0; i < rotation; i++) {
				facingDirection = facingDirection.getRotation(ForgeDirection.UP);
			}
		}

		Vec3 position = turtle.getPosition();

		return turtle.getWorld().getBlockTileEntity(
				(int)position.xCoord + facingDirection.offsetX,
				(int)position.yCoord + facingDirection.offsetY,
				(int)position.zCoord + facingDirection.offsetZ);
	}
	
	private LiquidStack createLiquid(int amount) {
		return LiquidDictionary.getLiquid("liquidxp", amount);
	}
}
