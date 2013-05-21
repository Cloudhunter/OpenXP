package openxp.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityLifeStone;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLifeStone extends BlockContainer {

	public BlockLifeStone() {
		super(OpenXP.Config.lifeStoneID, Material.ground);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "healingstone");
		GameRegistry.registerTileEntity(TileEntityLifeStone.class, "healingstone");
		setUnlocalizedName("openxp.healingstone");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityLifeStone();
	}
	
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventType, int eventParam)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityLifeStone) {
        	((TileEntityLifeStone)te).onBlockEventReceived(eventType, eventParam);
        }
        return true;
    }
}
