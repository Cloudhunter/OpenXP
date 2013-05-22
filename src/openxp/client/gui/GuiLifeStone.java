package openxp.client.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import openxp.api.IHasSimpleGui;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityLifeStone;

public class GuiLifeStone extends SimpleGui {

	private TileEntityLifeStone lifeStone;
	
	private boolean isMouseDown = false;
	private int mouseXOffset = 0;
	
	
	public GuiLifeStone(ContainerGeneric container, IHasSimpleGui tileentity) {
		super(container, tileentity);
		this.lifeStone = (TileEntityLifeStone) tileentity;

		buttons = new SimpleGuiButton[] {
				new SimpleGuiButton(45, 38, 10, 10, "-", 0, 176, 79),
				new SimpleGuiButton(75, 38, 10, 10, "+", 1, 176, 79),
				new SimpleGuiButton(58, 66, 10, 10, "*", 2, 176, 79),
				new SimpleGuiButton(88, 66, 10, 10, "*", 3, 176, 79)
		};
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int mouseX,
			int mouseY) {
		super.drawGuiContainerBackgroundLayer(par1, mouseX, mouseY);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture();

		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);


    	buttons[0].render(this, false);
    	buttons[1].render(this, false);

    	buttons[2].render(this, lifeStone.isHealingPlayers());
    	buttons[3].render(this, lifeStone.isDamagingMobs());
    	
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	bindTexture();
    	double stored = ((double)lifeStone.getPercentStored()) / 100.0;
    	int tankHeight = (int)(67.0 * stored);
		this.drawTexturedModalRect(left + 146, top + 9 + (67-tankHeight), 176, 67-tankHeight , 24, tankHeight);

        String s = String.format("Range: %s", lifeStone.getRange().getValue());
        fontRenderer.drawString(s, left + xSize - 10 - fontRenderer.getStringWidth(s), top + 64, 4210752);
        
        
        fontRenderer.drawString(String.format("%s/t", lifeStone.getCostPerTick()), left + 10, top + 64, 4210752);

	}

	@Override
	public void bindTexture() {
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/lifestone.png");
	}
}
