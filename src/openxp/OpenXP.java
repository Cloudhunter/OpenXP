package openxp;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.liquids.LiquidStack;
import openxp.common.CommonProxy;
import openxp.common.block.BlockAutoAnvil;
import openxp.common.block.BlockAutomatedEnchantmentTable;
import openxp.common.block.BlockLifeStone;
import openxp.common.block.BlockXPBottler;
import openxp.common.block.BlockXPSponge;
import openxp.common.item.ItemLiquidXP;
import openxp.common.turtle.TurtleOpenXP;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod( modid = "OpenXP", name = "OpenXP", version = "0.0.6")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class OpenXP
{
	
	public static class Blocks
	{
		public static BlockXPSponge XPSponge;
		public static BlockXPBottler XPBottler;
		public static BlockAutomatedEnchantmentTable enchantmentTable;
		public static BlockAutoAnvil autoAnvil;
		public static BlockLifeStone lifeStone;
	}
	public static class Config
	{
		public static int spongeBlockID;
		public static int enchantmentTableID;
		public static int bottlerID;
		public static int autoAnvilBlockID;
		public static int lifeStoneID;
		public static int liquidXpItemID;
		
		public static int spectraclSwordID = 3001;
	}
	
	public static enum Gui
	{
		enchantmentTable,
		xpBottler,
		autoAnvil,
		lifeStone
	}
	
	public static class Items
	{
		public static ItemLiquidXP liquidXP;
	}
	
	public static TurtleOpenXP xpTurtle;
	
	public static String RESOURCE_PATH;
	
	public static String LANGUAGE_PATH;
	

	public static LiquidStack liquidStack;
	public static EnumToolMaterial spectralMaterial;
	
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

	@Mod.Init
	public void init( FMLInitializationEvent evt )
	{
		proxy.init();
		NetworkRegistry.instance().registerGuiHandler(OpenXP.instance, proxy);
		proxy.registerRenderInformation();
		
		//OCSLog.info( "OpenXP version %s starting", FMLCommonHandler.instance().findContainerFor(instance).getVersion() );
	}

	@Mod.PreInit
	public void preInit( FMLPreInitializationEvent evt )
	{

		RESOURCE_PATH = "/mods/openxp";
		LANGUAGE_PATH = String.format("%s/languages", RESOURCE_PATH);
		
		Configuration configFile = new Configuration(evt.getSuggestedConfigurationFile());

		Property prop = configFile.getBlock("spongeBlockID", 500);
		prop.comment = "The block ID for the sponge block";
		Config.spongeBlockID = prop.getInt();

		prop = configFile.getBlock("enchantmentTableID", 501);
		prop.comment = "The block ID for the enchantment table block";
		Config.enchantmentTableID = prop.getInt();
		
		prop = configFile.getBlock("bottlerID", 502);
		prop.comment = "The block ID for the xp bottler block";
		Config.bottlerID = prop.getInt();
		
		prop = configFile.getBlock("autoAnvilBlockID", 503);
		prop.comment = "The block ID for the auto anvil block";
		Config.autoAnvilBlockID = prop.getInt();
		
		prop = configFile.getBlock("lifeStoneID", 504);
		prop.comment = "The block ID for the life stone block";
		Config.lifeStoneID = prop.getInt();

		prop = configFile.getItem("liquidXpItemID", 3000);
		prop.comment = "The item ID for liquid XP";
		Config.liquidXpItemID = prop.getInt();
		
		configFile.save();		
	}


}
