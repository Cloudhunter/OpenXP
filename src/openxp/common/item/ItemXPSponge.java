package openxp.common.item;

import net.minecraft.item.ItemBlock;

public class ItemXPSponge extends ItemBlock {

	public ItemXPSponge(int par1) {
		super(par1);
		setUnlocalizedName("openxp.xpsponge");
		setHasSubtypes(true);
	}
	
	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
