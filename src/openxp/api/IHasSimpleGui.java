package openxp.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;

public interface IHasSimpleGui {
	public void onServerButtonClicked(EntityPlayer player, int button);
	public void onClientButtonClicked(int button);
	public int[] getGuiValues();
	public int getGuiValue(int index);
	public void setGuiValue(int i, int value);
}
