package openxp.common.block;

import openxp.OpenXP;
import openxp.common.tileentity.TileEntityPeripheralEnchantmentTable;
import openxp.common.tileentity.TileEntityXPSponge;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPeripheralEnchantmentTable extends BlockEnchantmentTable {

	public BlockPeripheralEnchantmentTable() {
		super(OpenXP.Config.enchantmentTableID);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "enchantmentTable");
		GameRegistry.registerTileEntity(TileEntityPeripheralEnchantmentTable.class, "enchantmentTable");
		setUnlocalizedName("openxp.enchantmentTable");
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (player.isSneaking() || tileEntity == null) {
			return false;
		}
		player.openGui(OpenXP.instance, 2, world, x, y, z);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityPeripheralEnchantmentTable();
	}

}
