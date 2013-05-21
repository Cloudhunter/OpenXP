package openxp.client.renderer;

import org.lwjgl.opengl.GL11;

import openxp.client.model.ModelAnvil;
import openxp.client.model.ModelLifeStone;
import openxp.common.tileentity.TileEntityAutoAnvil;
import openxp.common.tileentity.TileEntityLifeStone;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RendererLifeStone extends TileEntitySpecialRenderer {

	protected ModelLifeStone model = new ModelLifeStone();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		TileEntityLifeStone anvil = (TileEntityLifeStone) tileentity;
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.0f, (float) z + 0.5F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		bindTextureByName("/mods/openxp/textures/blocks/lifestone.png");
		model.render();
		GL11.glPopMatrix();
	}

}
