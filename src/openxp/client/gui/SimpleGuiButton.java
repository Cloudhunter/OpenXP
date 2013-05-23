package openxp.client.gui;

import org.lwjgl.opengl.GL11;

public class SimpleGuiButton {
	
	public int x;
	public int y;
	public int width;
	public int height;
	public String text;
	public int index = 0;
	
	private int mouseX;
	private int mouseY;
	private int guiWidth;
	private int guiHeight;
	private int xSize;
	private int ySize;
	
	private int left;
	private int top;
	
	private int U;
	private int V;
	
	public SimpleGuiButton (int x, int y, int width, int height, String text, int index, int u, int v) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.index = index;
		this.U = u;
		this.V = v;
	}
	
	
	public void updateState(int mouseX, int mouseY, int guiWidth, int guiHeight, int xSize, int ySize) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.guiWidth = guiWidth;
		this.guiHeight = guiHeight;
		this.xSize = xSize;
		this.ySize = ySize;
		left = (guiWidth - xSize) / 2;
        top = (guiHeight - ySize) / 2;
	}
	
	public boolean isMouseOver() {
		return mouseX > left + x && mouseX < left + x + width && mouseY > top + y && mouseY < top + y + height;
    }
	
	public void render(SimpleGui gui, boolean selected) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);    
		gui.bindTexture("/mods/openxp/textures/gui/common.png");
		int textureOffset = 0;
		
		if (isMouseOver() && selected) {
			textureOffset = height * 3;
		} else if (isMouseOver()) {
			textureOffset += height;
		} else if (selected) {
			textureOffset += height * 2;
		}
		gui.drawTexturedModalRect(left + x, top + y, U, V + textureOffset, width, height);	
		
		if (text != null) {
	        int offset = selected ? 1 : 0;
	        gui.getFontRenderer().drawStringWithShadow(text, left + x + offset + ((width - gui.getFontRenderer().getStringWidth(text))/2), top + offset + y + 4, 8453920);
		}
	}

}
