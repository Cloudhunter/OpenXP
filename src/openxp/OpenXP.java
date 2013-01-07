package openxp;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import openxp.common.CommonProxy;
import openxp.common.block.BlockXPLiquidFlowing;
import openxp.common.block.BlockXPLiquidStill;
import openxp.common.item.ItemBucketXP;
import openxp.common.lib.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod( modid = "OpenXP", name = "OpenXP", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class OpenXP
{
	public static class Config
	{
		public static int XPLiquidFlowingID;
		public static int XPLiquidStillID;
		
		public static int itemBucketXPID;
	}
	
	public static class Blocks
	{
		public static Block XPLiquidFlowing;
		public static Block XPLiquidStill;
	}
	
	public static class Items
	{
		public static Item itemBucketXP;
	}

	@Instance( value = "OpenXP" )
	public static OpenXP instance;

	@SidedProxy( clientSide = "openxp.client.ClientProxy", serverSide = "openxp.common.CommonProxy" )
	public static CommonProxy proxy;

	@Mod.PreInit
	public void preInit( FMLPreInitializationEvent evt )
	{
		Configuration configFile = new Configuration(evt.getSuggestedConfigurationFile());

		Property prop = configFile.getBlock("XPLiquidFlowingID", 702);
		prop.comment = "The block ID for the flowing XP liquid";
		Config.XPLiquidFlowingID = prop.getInt();

		prop = configFile.getBlock("XPLiquidStillID", 701);
		prop.comment = "The block ID for the static XP liquid";
		Config.XPLiquidStillID = prop.getInt();
		
		prop = configFile.getBlock("itemBucketXPID", 2101);
		prop.comment = "The item ID for the XP bucket";
		Config.itemBucketXPID = prop.getInt();

		configFile.save();		
	}

	@Mod.Init
	public void init( FMLInitializationEvent evt )
	{
		proxy.init();

		registerBlocks();
		registerItems();
		
		//OCSLog.info( "OpenXP version %s starting", FMLCommonHandler.instance().findContainerFor(instance).getVersion() );
	}

	
	private void registerBlocks()
	{
		Blocks.XPLiquidStill = new BlockXPLiquidStill(Config.XPLiquidStillID);
		Blocks.XPLiquidFlowing = new BlockXPLiquidFlowing(Config.XPLiquidFlowingID);

		GameRegistry.registerBlock(Blocks.XPLiquidStill, Strings.XP_LIQUID_STILL_NAME);
		GameRegistry.registerBlock(Blocks.XPLiquidFlowing, Strings.XP_LIQUID_FLOWING_NAME);
		
	}
	
	private void registerItems()
	{
		Items.itemBucketXP = new ItemBucketXP(Config.itemBucketXPID);
		LanguageRegistry.addName(Items.itemBucketXP, Strings.XP_LIQUID_ITEM_BUCKET_NAME);	
	}	

}
