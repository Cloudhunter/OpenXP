package openxp.client;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openxp.OpenXP;
import openxp.client.gui.GuiAutomatedEnchantment;
import openxp.client.gui.GuiXPBottler;
import openxp.client.renderer.RendererXPSponge;
import openxp.common.CommonProxy;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityAutomatedEnchantmentTable;
import openxp.common.tileentity.TileEntityXPSponge;
import openxp.common.tileentity.xpbottler.TileEntityXPBottler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation() {
		OpenXP.renderId = RenderingRegistry.getNextAvailableRenderId();

		RenderingRegistry.registerBlockHandler(new GenericRenderingHandler());

		ClientRegistry.bindTileEntitySpecialRenderer(
				TileEntityXPSponge.class,
				new RendererXPSponge()
		);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if ((world instanceof WorldClient)) {
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if (ID == OpenXP.Gui.enchantmentTable.ordinal()) {
				  return new GuiAutomatedEnchantment(new ContainerGeneric(player.inventory, tile, TileEntityAutomatedEnchantmentTable.SLOTS), (TileEntityAutomatedEnchantmentTable)tile);
			}else if (ID == OpenXP.Gui.xpBottler.ordinal()) {
				return new GuiXPBottler(new ContainerGeneric(player.inventory, tile, TileEntityXPBottler.SLOTS), (TileEntityXPBottler)tile);
			}
		}
		return null;
	}
}
