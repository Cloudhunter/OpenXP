package openxp.client.renderer;

import org.lwjgl.opengl.GL11;

import openxp.client.model.ModelAnvil;
import openxp.common.tileentity.TileEntityAutoAnvil;
import openxp.common.util.BlockSide;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class RendererAutoAnvil extends TileEntitySpecialRenderer {

	protected ModelAnvil model = new ModelAnvil();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		TileEntityAutoAnvil anvil = (TileEntityAutoAnvil) tileentity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.0f, (float) z + 0.5F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		bindTextureByName("/mods/openxp/textures/blocks/anvil.png");
		double halfPI = Math.PI / 2;
		double rotation = 0.0f;
		int blockRotation = anvil.getRotation();
		if (blockRotation == ForgeDirection.EAST.ordinal()) {
			rotation = halfPI;
		}else if (blockRotation == ForgeDirection.WEST.ordinal()) {
			rotation = halfPI * 3;
		}else if (blockRotation == ForgeDirection.SOUTH.ordinal()) {
			rotation = halfPI * 2;
		}
		model.render((float)rotation);
		GL11.glPopMatrix();
	}

}
