package openxp.client.gui;

public class SimpleGuiButton {
	public int x;
	public int y;
	public int width;
	public int height;
	public String text;
	public int index = 0;
	public SimpleGuiButton (int x, int y, int width, int height, String text, int index) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.index = index;
	}
	public boolean isMouseOver(int mouseX, int mouseY, int guiWidth, int guiHeight, int xSize, int ySize) {
		int left = (guiWidth - xSize) / 2;
        int top = (guiHeight - ySize) / 2;
		return mouseX > left + x && mouseX < left + x + width && mouseY > top + y && mouseY < top + y + height;
    }
}
