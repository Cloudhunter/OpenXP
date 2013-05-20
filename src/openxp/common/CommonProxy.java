package openxp.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import openxp.OpenXP;
import openxp.common.block.BlockAutoAnvil;
import openxp.common.block.BlockAutomatedEnchantmentTable;
import openxp.common.block.BlockXPBottler;
import openxp.common.block.BlockXPSponge;
import openxp.common.ccintegration.PeripheralRegistry;
import openxp.common.ccintegration.TickHandler;
import openxp.common.container.ContainerGeneric;
import openxp.common.core.BaseTileEntity;
import openxp.common.item.ItemLiquidXP;
import openxp.common.tileentity.TileEntityAutoAnvil;
import openxp.common.tileentity.TileEntityAutomatedEnchantmentTable;
import openxp.common.tileentity.TileEntityXPBottler;
import openxp.common.turtle.TurtleOpenXP;
import openxp.common.util.LanguageUtils;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.ComputerCraftAPI;
import dan200.turtle.api.TurtleAPI;

public class CommonProxy implements IGuiHandler
{   
	
	public void init()
	{
		initBlocks();
		initItems();
		setupLanguages();
		
		if (ModLoader.isModLoaded("ComputerCraft")) {
			TurtleAPI.registerUpgrade(new TurtleOpenXP());
			TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
			ComputerCraftAPI.registerExternalPeripheral(BaseTileEntity.class, new PeripheralRegistry());
		}
	}

	private void setupLanguages() {
		LanguageUtils.setupLanguages();
	}

	private void initBlocks()
	{
		OpenXP.Blocks.XPSponge = new BlockXPSponge();
		OpenXP.Blocks.enchantmentTable = new BlockAutomatedEnchantmentTable();
		OpenXP.Blocks.XPBottler = new BlockXPBottler();
		OpenXP.Blocks.autoAnvil = new BlockAutoAnvil();
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
		}else if (ID == OpenXP.Gui.autoAnvil.ordinal()) {
			return new ContainerGeneric(player.inventory, tile, TileEntityAutoAnvil.SLOTS);
		}
		
		return null;
	}



	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}
}
