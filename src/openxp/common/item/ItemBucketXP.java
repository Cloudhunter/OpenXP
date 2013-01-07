package openxp.common.item;

import openxp.common.lib.Strings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;

public class ItemBucketXP extends ItemBucket {

	public ItemBucketXP(int i) {
		super(i, 700);
		setItemName(Strings.XP_LIQUID_ITEM_BUCKET_NAME);
		setContainerItem(Item.bucketEmpty);
		iconIndex = 0 * 16 + 1;
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return "XPbucket";
	}

	@Override
	public String getTextureFile() {
		return "/openxp/resources/gfx/items.png";
	}

}