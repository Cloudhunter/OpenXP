package openxp.client.gui;

import openxp.api.IHasSimpleGui;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityAutoAnvil;

import org.lwjgl.opengl.GL11;
/**
 * The Gui object for the Automatic Anvil
 *
 * @author SinZ
 */
public class GuiAutoAnvil extends SimpleGui {

    private TileEntityAutoAnvil tileentity;
    
	public GuiAutoAnvil(ContainerGeneric container, TileEntityAutoAnvil tile) {
		super(container, (IHasSimpleGui) tile);
		this.tileentity = tile;
	}
	
	@Override
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoanvil.png");

		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);

        String s = String.format("Stored Levels: %s", tileentity.getLevelsAvailable());
        fontRenderer.drawString(s, left + xSize - 10 - fontRenderer.getStringWidth(s), top + 64, 4210752);
        
    }
	
	
	private void drawPanel(SimpleGuiButton button, int mouseX, int mouseY) {
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoanvil.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);        
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2, "openxp.gui.autoanvil");
	}
}
