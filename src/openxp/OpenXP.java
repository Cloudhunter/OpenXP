package openxp;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.liquids.LiquidStack;
import openxp.common.CommonProxy;
import openxp.common.block.BlockAutoAnvil;
import openxp.common.block.BlockAutomatedEnchantmentTable;
import openxp.common.block.BlockXPBottler;
import openxp.common.block.BlockXPSponge;
import openxp.common.item.ItemLiquidXP;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod( modid = "OpenXP", name = "OpenXP", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class OpenXP
{
	public static class Config
	{
		public static int spongeBlockID = 500;
		public static int enchantmentTableID = 501;
		public static int bottlerID = 502;
		public static int autoAnvilBlockID = 503;
		
		public static int liquidXpItemID = 3000;
	}
	
	public static enum Gui
	{
		enchantmentTable,
		xpBottler,
		autoAnvil
	}
	
	public static class Blocks
	{
		public static BlockXPSponge XPSponge;
		public static BlockXPBottler XPBottler;
		public static BlockAutomatedEnchantmentTable enchantmentTable;
		public static BlockAutoAnvil autoAnvil;
	}
	
	public static class Items
	{
		public static ItemLiquidXP liquidXP;
	}
	

	public static LiquidStack liquidStack;
	
	public static int renderId;

	public static CreativeTabs tabOpenXP = new CreativeTabs("tabOpenXP") {
        public ItemStack getIconItemStack() {
                return new ItemStack(Blocks.XPSponge);
        }
	};
	
	@Instance( value = "OpenXP" )
	public static OpenXP instance;

	@SidedProxy( clientSide = "openxp.client.ClientProxy", serverSide = "openxp.common.CommonProxy" )
	public static CommonProxy proxy;

	@Mod.PreInit
	public void preInit( FMLPreInitializationEvent evt )
	{
		Configuration configFile = new Configuration(evt.getSuggestedConfigurationFile());
/*
		Property prop = configFile.getBlock("XPLiquidFlowingID", 702);
		prop.comment = "The block ID for the flowing XP liquid";
		Config.XPLiquidFlowingID = prop.getInt();

		prop = configFile.getBlock("XPLiquidStillID", 701);
		prop.comment = "The block ID for the static XP liquid";
		Config.XPLiquidStillID = prop.getInt();
		
		prop = configFile.getBlock("itemBucketXPID", 2101);
		prop.comment = "The item ID for the XP bucket";
		Config.itemBucketXPID = prop.getInt();
*/
		configFile.save();		
	}

	@Mod.Init
	public void init( FMLInitializationEvent evt )
	{
		proxy.init();
		NetworkRegistry.instance().registerGuiHandler(OpenXP.instance, proxy);
		proxy.registerRenderInformation();
		
		//OCSLog.info( "OpenXP version %s starting", FMLCommonHandler.instance().findContainerFor(instance).getVersion() );
	}


}
