package openxp.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openxp.OpenXP;
import openxp.client.gui.GuiPeripheralEnchantment;
import openxp.client.gui.GuiXPIngester;
import openxp.client.renderer.RendererXPSponge;
import openxp.common.CommonProxy;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityPeripheralEnchantmentTable;
import openxp.common.tileentity.TileEntityXPIngester;
import openxp.common.tileentity.TileEntityXPSponge;

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
		      switch (ID) {
		      case 1:
		    	  return new GuiXPIngester(new ContainerGeneric(player.inventory, tile, TileEntityXPIngester.SLOTS), (TileEntityXPIngester)tile);
		      case 2:
		    	  return new GuiPeripheralEnchantment(new ContainerGeneric(player.inventory, tile, TileEntityPeripheralEnchantmentTable.SLOTS), (TileEntityPeripheralEnchantmentTable)tile);
		      
		      }
		}
		return null;
	}
}
