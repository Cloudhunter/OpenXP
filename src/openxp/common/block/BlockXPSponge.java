package openxp.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityXPIngester;
import openxp.common.tileentity.TileEntityXPSponge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockXPSponge extends BlockContainer {

	public Icon iconLiquid;
	
	public BlockXPSponge() {
		super(OpenXP.Config.spongeBlockID, Material.ground);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "xpSponge");
		GameRegistry.registerTileEntity(TileEntityXPSponge.class, "xpSponge");
		setUnlocalizedName("openxp.XPIngester");
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("openxp:xpIngester");
		iconLiquid = blockIcon;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityXPSponge();
	}
	
	/*
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (player.isSneaking() || tileEntity == null) {
			return false;
		}
		player.openGui(OpenXP.instance, 1, world, x, y, z);
		return true;
	}
	*/

	@Override
	public int getRenderType() {
		return OpenXP.renderId;
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}
}
