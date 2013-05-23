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
				new SimpleGuiButton(34, 26, 108, 15, "Lowest Level",
									TileEntityAutomatedEnchantmentTable.MODE_LOWEST,
									0, 107),
				new SimpleGuiButton(34, 42, 108, 15, "Highest Level",
									TileEntityAutomatedEnchantmentTable.MODE_HIGHEST,
									0, 107)
		};
	}
	
	@Override
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY)
    {
		super.drawGuiContainerBackgroundLayer(par1, mouseX, mouseY);
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();

		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);

        for (int i = 0; i < buttons.length; i++) {
        	buttons[i].render(this, tileentity.getMode() == buttons[i].index);
        }
        
        String s = String.format("Stored Levels: %s", tileentity.getLevelsAvailable());
        fontRenderer.drawString(s, left + xSize - 10 - fontRenderer.getStringWidth(s), top + 64, 4210752);
        
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2, "openxp.gui.autoenchanter");
	}

	@Override
	public void bindTexture() {
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/autoenchant.png");
	}
}
