package openxp.common.block;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;

public class BlockXPLiquidStill extends BlockStationary implements ILiquid {

	public BlockXPLiquidStill(int id) {

        super(id, Material.water);
        this.blockHardness = 100F;
        this.setLightOpacity(3);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.disableStats();
        setBlockName("OXP.still");
        this.setRequiresSelfNotify();
    }

	@Override
	public int stillLiquidId() {
		return 701;
	}

	@Override
	public boolean isMetaSensitive() {
		return false;
	}

	@Override
	public int stillLiquidMeta() {
		return 0;
	}
}
