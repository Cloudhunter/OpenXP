package openxp.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import openxp.OpenXP;
import openxp.client.core.BaseTileEntity;
import openxp.common.block.BlockAutomatedEnchantmentTable;
import openxp.common.block.BlockXPBottler;
import openxp.common.block.BlockXPSponge;
import openxp.common.container.ContainerGeneric;
import openxp.common.item.ItemLiquidXP;
import openxp.common.tileentity.TileEntityAutomatedEnchantmentTable;
import openxp.common.tileentity.xpbottler.TileEntityXPBottler;
import openxp.common.util.PeripheralHandler;
import openxp.common.util.TickHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.ComputerCraftAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class CommonProxy implements IGuiHandler
{   
	
	public void init()
	{
		initBlocks();
		initItems();
		
		if (ModLoader.isModLoaded("ComputerCraft")) {
			TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
			ComputerCraftAPI.registerExternalPeripheral(BaseTileEntity.class, new PeripheralHandler());
		}
	}

	private void initBlocks()
	{
		OpenXP.Blocks.XPSponge = new BlockXPSponge();
		OpenXP.Blocks.enchantmentTable = new BlockAutomatedEnchantmentTable();
		OpenXP.Blocks.XPBottler = new BlockXPBottler();
	}
	
	private void initItems()
	{
		OpenXP.Items.liquidXP = new ItemLiquidXP();
		
		LiquidDictionary.getOrCreateLiquid("liquidxp", new LiquidStack(OpenXP.Items.liquidXP, 1));
		
		OpenXP.liquidStack = LiquidDictionary.getCanonicalLiquid("liquidxp");
	}	
	
	public void registerRenderInformation() {

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if (ID == OpenXP.Gui.enchantmentTable.ordinal()) {
			return new ContainerGeneric(player.inventory, tile, TileEntityAutomatedEnchantmentTable.SLOTS);
		}else if (ID == OpenXP.Gui.xpBottler.ordinal()) {
			return new ContainerGeneric(player.inventory, tile, TileEntityXPBottler.SLOTS);	
		}
		
		return null;
	}



	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}
}
