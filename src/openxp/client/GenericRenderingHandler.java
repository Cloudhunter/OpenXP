package openxp.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;
import openxp.OpenXP;
import openxp.common.tileentity.TileEntityAutoAnvil;
import openxp.common.tileentity.TileEntityLifeStone;
import openxp.common.tileentity.TileEntityXPSponge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class GenericRenderingHandler implements ISimpleBlockRenderingHandler {

	@Override
	public int getRenderId() {
		return OpenXP.renderId;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {

		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		if (block == OpenXP.Blocks.autoAnvil) {
			TileEntityRenderer.instance.renderTileEntityAt(new TileEntityAutoAnvil(), 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == OpenXP.Blocks.XPSponge) {
			TileEntityXPSponge sponge = new TileEntityXPSponge();
			sponge.setInventoryRenderAmount(metadata);
			TileEntityRenderer.instance.renderTileEntityAt(sponge, 0.0D, 0.0D, 0.0D, 0.0F);
		}else if (block == OpenXP.Blocks.lifeStone) {
			GL11.glTranslatef(0F, 0.4F, 0F);
			TileEntityRenderer.instance.renderTileEntityAt(new TileEntityLifeStone(), 0.0D, 0.0D, 0.0D, 0.0F);
		}
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

}
