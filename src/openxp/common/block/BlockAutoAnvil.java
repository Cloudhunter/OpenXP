package openxp.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityAutoAnvil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
/**
 * The Block object for the Automatic Anvil
 * 
 * @author SinZ
 */
public class BlockAutoAnvil extends BlockContainer {	
	public BlockAutoAnvil() {
		super(OpenXP.Config.autoAnvilBlockID, Material.anvil);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "autoAnvil");
		GameRegistry.registerTileEntity(TileEntityAutoAnvil.class, "autoAnvil");
		setUnlocalizedName("openxp.autoAnvil");
		LanguageRegistry.addName(this, "Automatic Anvil");
	}
	
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAutoAnvil();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (player.isSneaking() || tileEntity == null) {
			return false;
		}
		player.openGui(OpenXP.instance, OpenXP.Gui.autoAnvil.ordinal(), world, x, y, z);
		return true;
	}

}
