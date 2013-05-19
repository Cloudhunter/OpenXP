package openxp.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import openxp.OpenXP;
import openxp.common.ccintegration.PeripheralRegistry;
import openxp.common.tileentity.TileEntityXPBottler;
import openxp.common.util.BlockSide;
import openxp.common.util.BlockUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockXPBottler extends BlockContainer {

	public static class icons {
		public static Icon back;
		public static Icon top;
		public static Icon sides;
		public static Icon front;
		public static Icon bottom;
	}
	
	public BlockXPBottler() {
		super(OpenXP.Config.bottlerID, Material.ground);
		setHardness(0.5F);
		setCreativeTab(OpenXP.tabOpenXP);
		GameRegistry.registerBlock(this, "xpbottler");
		GameRegistry.registerTileEntity(TileEntityXPBottler.class, "xpbottler");
		PeripheralRegistry.registerTileEntity(TileEntityXPBottler.class, "xpbottler");
		setUnlocalizedName("openxp.xpbottler");
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icons.bottom = iconRegister.registerIcon("openxp:xpbottler_bottom");
		icons.top = iconRegister.registerIcon("openxp:xpbottler_top");
		icons.sides = iconRegister.registerIcon("openxp:xpbottler_sides");
		icons.front = iconRegister.registerIcon("openxp:xpbottler_front");
		icons.back = iconRegister.registerIcon("openxp:xpbottler_back");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityXPBottler();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (player.isSneaking() || tileEntity == null) {
			return false;
		}
		player.openGui(OpenXP.instance, OpenXP.Gui.xpBottler.ordinal(), world, x, y, z);
		return true;
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int i, int j) {
    	if (j == 0 && i == 3)
			return icons.front;
    	if (i == BlockSide.TOP)
			return icons.top;
		else if (i == BlockSide.BOTTOM)
			return icons.bottom;
		else if (i == j)
			return icons.front;
		else if (j >= 0 && j < 6 && ForgeDirection.values()[j].getOpposite().ordinal() == i)
			return icons.back;
		else
			return icons.sides;
    }
    
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, i, j, k, entityliving, itemStack);
		ForgeDirection orientation = BlockUtils.get2dOrientation(entityliving.getPosition(1.0F), Vec3.createVectorHelper(i, j, k));
		world.setBlockMetadataWithNotify(i, j, k, orientation.getOpposite().ordinal(), 3);
	}
	
}
