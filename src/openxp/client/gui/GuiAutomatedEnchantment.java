package openxp.client.gui;

import openxp.api.IHasSimpleGui;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityAutomatedEnchantmentTable;

import org.lwjgl.opengl.GL11;

public class GuiAutomatedEnchantment extends SimpleGui {
	
    
    private TileEntityAutomatedEnchantmentTable tileentity;
    
	public GuiAutomatedEnchantment(ContainerGeneric container, TileEntityAutomatedEnchantmentTable tile) {
		super(container, (IHasSimpleGui) tile);
		this.tileentity = tile;
		buttons = new SimpleGuiButton[] {
				new SimpleGuiButton(34, 26, 107, 15, "Lowest Level", TileEntityAutomatedEnchantmentTable.MODE_LOWEST),
				new SimpleGuiButton(34, 42, 107, 15, "Highest Level", TileEntityAutomatedEnchantmentTable.MODE_HIGHEST)
		};
	}
	
	@Override
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoenchant.png");

		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);

        for (int i = 0; i < buttons.length; i++) {
            drawPanel(buttons[i], mouseX, mouseY);
        }
        String s = String.format("Stored Levels: %s", tileentity.getLevelsAvailable());
        fontRenderer.drawString(s, left + xSize - 10 - fontRenderer.getStringWidth(s), top + 64, 4210752);
        
    }
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2, "openxp.gui.autoenchanter");
	}

	private void drawPanel(SimpleGuiButton button, int mouseX, int mouseY) {
		boolean selected = tileentity.getMode() == button.index;
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoenchant.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);        
        this.drawTexturedModalRect(left + button.x, top + button.y, 0, selected ? 196 : button.isMouseOver(mouseX, mouseY, width, height, xSize, ySize) ? 181 : 166, 107, 15);
        if (selected) {
        	left += 1;
        	top += 1;
        }
        this.mc.fontRenderer.drawStringWithShadow(button.text, left + button.x + ((107 - this.mc.fontRenderer.getStringWidth(button.text))/2), top + button.y + 4, 8453920);
	}
}
