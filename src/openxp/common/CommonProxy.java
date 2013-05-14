package openxp.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import openxp.OpenXP;
import openxp.common.block.BlockPeripheralEnchantmentTable;
import openxp.common.block.BlockXPSponge;
import openxp.common.container.ContainerGeneric;
import openxp.common.item.ItemLiquidXP;
import openxp.common.tileentity.TileEntityPeripheralEnchantmentTable;
import openxp.common.tileentity.TileEntityXPIngester;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler
{   
	
	public void init()
	{
		initBlocks();
		initItems();

	}

	private void initBlocks()
	{
		OpenXP.Blocks.XPSponge = new BlockXPSponge();
		OpenXP.Blocks.enchantmentTable = new BlockPeripheralEnchantmentTable();
	}
	
	private void initItems()
	{
		OpenXP.Items.liquidXP = new ItemLiquidXP();
	}	
	
	public void registerRenderInformation() {

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch (ID) {
	    case 1:
	    	return new ContainerGeneric(player.inventory, tile, TileEntityXPIngester.SLOTS);
	    case 2:
	    	return new ContainerGeneric(player.inventory, tile, TileEntityPeripheralEnchantmentTable.SLOTS);
		
		}
		return null;
	}



	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}
}
