package openxp.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityAutoAnvil;
import openxp.common.util.BlockUtils;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * The Block object for the Automatic Anvil
 * 
 * @author SinZ
 */
public class BlockAutoAnvil extends BlockContainer {	
	public BlockAutoAnvil() {
		super(OpenXP.Config.autoAnvilBlockID, Material.anvil);
		setHardness(5.0F);
		setStepSound(soundAnvilFootstep);
		setResistance(2000.0F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "autoAnvil");
		GameRegistry.registerTileEntity(TileEntityAutoAnvil.class, "autoAnvil");
		setUnlocalizedName("openxp.autoanvil");
	}
    
	public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("openxp:anvilblock");
    }
    
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		BlockUtils.dropInventoryItems(world.getBlockTileEntity(x, y, z));
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAutoAnvil();
	}
	
	@Override
	public int getRenderType() {
		return OpenXP.renderId;
	}
	
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
		player.openGui(OpenXP.instance, OpenXP.Gui.autoAnvil.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, i, j, k, entityliving, itemStack);
		ForgeDirection orientation = BlockUtils.get2dOrientation(Vec3.createVectorHelper(entityliving.posX, entityliving.posY, entityliving.posZ), Vec3.createVectorHelper(i, j, k));
		world.setBlockMetadataWithNotify(i, j, k, orientation.getOpposite().ordinal(), 3);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

}
