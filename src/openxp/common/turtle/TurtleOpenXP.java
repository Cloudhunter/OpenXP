package openxp.common.turtle;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import openxp.OpenXP;
import openxp.common.turtle.peripheral.PeripheralOpenXPTurtle;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class TurtleOpenXP implements ITurtleUpgrade {

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle,
			TurtleSide side) {
		return new PeripheralOpenXPTurtle(turtle, side);
	}

	@Override
	public String getAdjective() {
		String translation = LanguageRegistry.instance().getStringLocalization(
				"turtle.openxp.sensor.adjective"
		);
		return translation == "" ? LanguageRegistry.instance().getStringLocalization(
						"turtle.openxp.sensor.adjective", "en_US"
		) : translation;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Item.expBottle);
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return OpenXP.Blocks.XPBottler.getIcon(3, 0);
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public int getUpgradeID() {
		return 99;
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side,
			TurtleVerb verb, int direction) {
		return false;
	}
}
