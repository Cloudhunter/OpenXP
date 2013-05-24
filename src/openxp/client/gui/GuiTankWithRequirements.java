package openxp.client.gui;

public class GuiTankWithRequirements {
	
	public void render(SimpleGui gui, int left, int top, double d, double r) {
		
		double progressD = d / 100.0;
		int progressWidth = (int)(29.0 * d);

		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		gui.drawTexturedModalRect(left, top, 0, 0, 24, 67);
		
		double stored = d / 100.0;
		int tankHeight = (int)(61.0 * stored);
		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		gui.drawTexturedModalRect(left + 8, top + 3 + (61 - tankHeight), 24, 61 - tankHeight, 13, tankHeight);

		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		double required = r / 100.0;
		int requiredHeight = (int)(61.0 * required);
		requiredHeight = Math.min(61, requiredHeight);
		int xOffset = (r >= 1.0) ? 3 : 0;
		gui.drawTexturedModalRect(left + 3, top + 3 + (61 - requiredHeight), 37+xOffset, 61 - requiredHeight, 3, requiredHeight);

	}
}
