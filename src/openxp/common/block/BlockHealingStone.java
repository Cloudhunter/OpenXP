package openxp.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityHealingStone;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockHealingStone extends BlockContainer {

	public BlockHealingStone() {
		super(OpenXP.Config.healingStoneID, Material.ground);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "healingstone");
		GameRegistry.registerTileEntity(TileEntityHealingStone.class, "healingstone");
		setUnlocalizedName("openxp.healingstone");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityHealingStone();
	}

}
