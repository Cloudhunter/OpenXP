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

        double required = ((double)tileentity.getPercentRequired()) / 100.0;
		
        boolean tooExpensive = false;
        if (required > 1.0) {
        	tooExpensive = true;
        	required = 1.0;
        }
        
        int tankHeightRequired = (int)(61.0 * required);
		
        this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoanvil.png");
		
        this.drawTexturedModalRect(left + 149, top + 12 + (61-tankHeightRequired), tooExpensive ? 192 : 189, 61-tankHeightRequired , 3, tankHeightRequired);	
        
        
		double stored = ((double)tileentity.getPercentStored()) / 100.0;
		int storedHeight = (int)(61.0 * stored);

        this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoanvil.png");
		this.drawTexturedModalRect(left + 154, top + 12 + (61-storedHeight), 176, 61-storedHeight , 13, storedHeight);

		double progress = tileentity.getPercentProgress() / 100.0;
		int progressWidth = (int)(29.0 * progress);

		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoanvil.png");
		this.drawTexturedModalRect(left + 66, top + 36, 176, 67, progressWidth, 12);
    }
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2, "openxp.gui.autoanvil");
	}

	private void drawPanel(SimpleGuiButton button, int mouseX, int mouseY) {
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoanvil.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);        
	}
}
