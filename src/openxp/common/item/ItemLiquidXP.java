package openxp.common.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import openxp.OpenXP;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class ItemLiquidXP extends Item {
	
	public ItemLiquidXP() {
		super(OpenXP.Config.liquidXpItemID);
		setUnlocalizedName("liquidxp");
		LanguageRegistry.addName(this, "Liquid XP");
		LiquidDictionary.getOrCreateLiquid("liquidxp", new LiquidStack(this, 1));
	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("openxp:xpjuice");

		OpenXP.liquidStack.setRenderingIcon(itemIcon);
		OpenXP.liquidStack.setTextureSheet("/mods/openxp/textures/items/xpjuice.png");
	}

}
