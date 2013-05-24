package openxp.client.gui;

public class GuiProgressBar {
	
	public void render(SimpleGui gui, int left, int top, double d) {
		
		double progressD = d / 100.0;
		int progressWidth = (int)(29.0 * progressD);

		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		gui.drawTexturedModalRect(left, top, 0, 67, 29, 12);
		
		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		gui.drawTexturedModalRect(left, top, 0, 79, progressWidth, 12);
	}
}
