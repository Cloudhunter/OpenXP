package openxp.client.gui;

import openxp.api.IHasSimpleGui;
import openxp.common.container.ContainerGeneric;
import openxp.common.tileentity.TileEntityLifeStone;

import org.lwjgl.opengl.GL11;

public class GuiLifeStone extends SimpleGui {

	private TileEntityLifeStone lifeStone;
	
	private boolean isMouseDown = false;
	private int mouseXOffset = 0;

	private GuiTank tank = new GuiTank();
	
	public GuiLifeStone(ContainerGeneric container, IHasSimpleGui tileentity) {
		super(container, tileentity);
		this.lifeStone = (TileEntityLifeStone) tileentity;

		buttons = new SimpleGuiButton[] {
				new SimpleGuiButton(10, 48, 7, 4, null, TileEntityLifeStone.Button.INCREASE_RANGE.ordinal(), 39, 67),
				new SimpleGuiButton(10, 53, 7, 4, null, TileEntityLifeStone.Button.DECREASE_RANGE.ordinal(), 46, 67),
				new SimpleGuiButton(9, 21, 10, 10, null, TileEntityLifeStone.Button.HEAL_PLAYERS.ordinal(), 29, 67),
				new SimpleGuiButton(9, 35, 10, 10, null, TileEntityLifeStone.Button.DAMAGE_MOBS.ordinal(), 29, 67)
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
        tank.render(this, left + 147, top + 9, lifeStone.getPercentStored());
		
        fontRenderer.drawString("Heal players", left + 24, top + 21, 4210752);
        fontRenderer.drawString("Damage mobs", left + 24, top + 35, 4210752);
        String s = String.format("Range: %s", lifeStone.getRange().getValue());
        fontRenderer.drawString(s, left + 24, top + 49, 4210752);
        fontRenderer.drawString(String.format("%s/t", lifeStone.getCostPerTick()), left + 114, top + 72, 4210752);

	}

	@Override
	public void bindTexture() {
		this.mc.renderEngine.bindTexture("/mods/openxp/textures/gui/base.png");
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2, "openxp.gui.lifestone");
	}

}
