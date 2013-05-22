package openxp.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityLifeStone;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLifeStone extends BlockContainer {

	public BlockLifeStone() {
		super(OpenXP.Config.lifeStoneID, Material.circuits);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "healingstone");
		GameRegistry.registerTileEntity(TileEntityLifeStone.class, "healingstone");
		this.setBlockBounds(0.35f, 0f, 0.35f, 0.65f, 0.5f, 0.65f);
		setUnlocalizedName("openxp.healingstone");
	}
    
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityLifeStone();
	}

	@Override
	public int getRenderType() {
		return OpenXP.renderId;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (player.isSneaking() || tileEntity == null) {
			return false;
		}
		player.openGui(OpenXP.instance, OpenXP.Gui.lifeStone.ordinal(), world, x, y, z);
		return true;
	}
}
