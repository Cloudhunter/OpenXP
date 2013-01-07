package openxp.common.block;

import net.minecraft.block.BlockFlowing;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.liquids.ILiquid;

public class BlockXPLiquidFlowing extends BlockFlowing implements ILiquid {

	public BlockXPLiquidFlowing(int par1) {
		super(par1, Material.water);
        this.blockHardness = 100F;
        this.setLightOpacity(3);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setBlockName("OXP.flowing");
	}

	@Override
	public int stillLiquidId() {
		// TODO Auto-generated method stub
		return 701;
	}

	@Override
	public boolean isMetaSensitive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int stillLiquidMeta() {
		// TODO Auto-generated method stub
		return 0;
	}

}
