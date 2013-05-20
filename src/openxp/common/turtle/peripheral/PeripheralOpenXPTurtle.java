package openxp.common.turtle.peripheral;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import openxp.common.ccintegration.BaseTurtlePeripheral;
import openxp.common.ccintegration.ExposedMethod;
import openxp.common.ccintegration.LuaMethod;
import openxp.common.ccintegration.TickHandler;
import openxp.common.core.BaseTankContainer;
import openxp.common.util.EnchantmentUtils;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidTank;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;

public class PeripheralOpenXPTurtle extends BaseTurtlePeripheral implements IHostedPeripheral {
	
	protected BaseTankContainer tanks = new BaseTankContainer(new LiquidTank(
				EnchantmentUtils.XPToLiquidRatio(
						EnchantmentUtils.getExperienceForLevel(30)
				)
	));
	
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
	
	@LuaMethod(onTick = true)
	public int getLiquidVolume(IComputerAccess computer) {
		return tanks.getTankAmount();
	}

	@LuaMethod
	public int getLiquidCapacity(IComputerAccess computer) {
		return tanks.getCapacity();
	}
	
	@LuaMethod(onTick = true)
	public int collect(IComputerAccess computer) {
		int xpCollected = 0;
		Vec3 position = turtle.getPosition();
		AxisAlignedBB suckupBounds = AxisAlignedBB.getBoundingBox(
				position.xCoord - 3,
				position.yCoord - 3,
				position.zCoord - 3,
				position.xCoord + 4,
				position.yCoord + 4,
				position.zCoord + 4
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
}
