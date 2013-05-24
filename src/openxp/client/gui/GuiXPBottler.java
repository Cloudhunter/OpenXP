package openxp.client.gui;

import openxp.api.IHasSimpleGui;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityXPBottler;

import org.lwjgl.opengl.GL11;

public class GuiXPBottler extends SimpleGui {

	private TileEntityXPBottler tile;
	private GuiProgressBar progressBar = new GuiProgressBar();
	private GuiTank tank = new GuiTank();
	
	public GuiXPBottler(ContainerGeneric container, TileEntityXPBottler tile) {
		super(container, (IHasSimpleGui) tile);
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int left = (this.width - this.xSize) / 2;
		int top = (this.height - this.ySize) / 2;
		
		bindTexture();
		this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
		
		tank.render(this, left + 146, top + 8, tile.getPercentStored());
		progressBar.render(this, left + 66, top + 36, tile.getPercentProgress());
		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2, "openxp.gui.xpbottler");
	}
	
	@Override
	public void bindTexture() {
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/xpbottler.png");
	}
}
