package openxp.client.gui;

public class GuiTank {
	public void render(SimpleGui gui, int left, int top, double d) {
		
		double progressD = d / 100.0;
		int progressWidth = (int)(29.0 * d);

		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		gui.drawTexturedModalRect(left, top, 43, 0, 24, 67);
		
		double stored = d / 100.0;
		
		int tankHeight = (int)(61.0 * stored);
		
		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		gui.drawTexturedModalRect(left + 3, top + 3 + (61 - tankHeight), 67, 61 - tankHeight, 24, tankHeight);

	}
}
