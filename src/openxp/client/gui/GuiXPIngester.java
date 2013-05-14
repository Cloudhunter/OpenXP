package openxp.client.gui;

import org.lwjgl.opengl.GL11;

import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityXPIngester;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;

public class GuiXPIngester extends GuiContainer {

	public GuiXPIngester(ContainerGeneric container, TileEntityXPIngester tile) {
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/xpingester.png");
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String machineName = StatCollector
				.translateToLocal("openxp.gui.xpingester");
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
}
