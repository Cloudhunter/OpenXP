package openxp.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityXPSponge;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockXPSponge extends BlockContainer {
	
	public BlockXPSponge() {
		super(OpenXP.Config.spongeBlockID, Material.ground);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "xpsponge");
		GameRegistry.registerTileEntity(TileEntityXPSponge.class, "xpsponge");
		setUnlocalizedName("openxp.xpsponge");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityXPSponge();
	}

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
