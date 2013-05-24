package openxp.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import openxp.OpenXP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemXPSponge extends ItemBlock {

	public ItemXPSponge(int par1) {
		super(par1);
		setUnlocalizedName("openxp.xpsponge");
		setHasSubtypes(true);
		setCreativeTab(OpenXP.tabOpenXP);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs tab, List subItems) {
		OpenXP.xpTurtle.addTurtlesToCreative(subItems);
		subItems.add(new ItemStack(OpenXP.Blocks.XPSponge));
	}
}
