package openxp.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import openxp.api.IHasSimpleGui;
import openxp.common.container.ContainerGeneric;

public class SimpleGui extends GuiContainer {

	protected SimpleGuiButton[] buttons;
	private IHasSimpleGui tileentity;
	
	public SimpleGui(ContainerGeneric container, IHasSimpleGui tileentity) {
		super(container);
		this.tileentity = tileentity;
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].updateState(mouseX, mouseY, width, height, xSize, ySize);
		}
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY, String name) {

		String machineName = StatCollector.translateToLocal(name);
		fontRenderer.drawString(
			machineName,
			this.xSize / 2 - (fontRenderer.getStringWidth(machineName) / 2),
			6,
			4210752
		);
		fontRenderer.drawString(
			StatCollector.translateToLocal("container.inventory"),
			8,
			this.ySize - 96 + 2,
			4210752
		);
	}
	
	public FontRenderer getFontRenderer() {
		return this.mc.fontRenderer;
	}
	
	@Override
    protected void mouseClicked(int x, int y, int par3)
    {
	    super.mouseClicked(x, y, par3);
	    if (buttons == null) {
			return;
		}
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isMouseOver()) {
				tileentity.onClientButtonClicked(buttons[i].index);
				this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, buttons[i].index);
			}
		}
    }
	
	public void bindTexture() {
		
	}
}
