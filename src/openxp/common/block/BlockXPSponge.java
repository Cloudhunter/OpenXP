package openxp.common.block;

import java.util.ArrayList;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openxp.OpenXP;
import openxp.common.item.ItemXPSponge;
import openxp.common.tileentity.TileEntityXPSponge;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockXPSponge extends BlockContainer {
	
	public BlockXPSponge() {
		super(OpenXP.Config.spongeBlockID, Material.ground);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, ItemXPSponge.class, "xpsponge");
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

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
        if (tile != null && tile instanceof TileEntityXPSponge) {
        	((TileEntityXPSponge)tile).setTankAmount((double)par1World.getBlockMetadata(par2, par3, par4)/10);
        }
    }
	
	@Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
    	ArrayList<ItemStack> stacks = super.getBlockDropped(world, x, y, z, metadata, fortune);
        for(int i = 0; i < stacks.size(); i++)
        {
            stacks.get(i).setItemDamage(metadata);
        }
        return stacks;
    }

}
