package openxp.client.gui;

import openxp.api.IHasSimpleGui;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityXPBottler;

import org.lwjgl.opengl.GL11;

public class GuiXPBottler extends SimpleGui {

	private TileEntityXPBottler tile;
	public GuiXPBottler(ContainerGeneric container, TileEntityXPBottler tile) {
		super(container, (IHasSimpleGui) tile);
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/xpbottler.png");
		int left = (this.width - this.xSize) / 2;
		int top = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
		double stored = ((double)tile.getPercentStored()) / 100.0;
		int tankHeight = (int)(67.0 * stored);
		double progress = tile.getPercentProgress() / 100.0;
		int progressWidth = (int)(29.0 * progress);
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/xpbottler.png");
		this.drawTexturedModalRect(left + 146, top + 9 + (67-tankHeight), 176, 67-tankHeight , 24, tankHeight);

		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/xpbottler.png");
		this.drawTexturedModalRect(left + 66, top + 36, 176, 67, progressWidth, 12);
		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2, "openxp.gui.xpbottler");
	}
}
