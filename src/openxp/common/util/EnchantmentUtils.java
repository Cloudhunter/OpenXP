package openxp.common.util;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EnchantmentUtils {

	public static final int XP_PER_BOTTLE = 8;
	
	public static int[] levels = new int[] { 0, 17, 34, 51, 68, 85, 102, 119,
		136, 153, 170, 187, 204, 221, 238, 255, 272, 292, 315, 341, 370,
		402, 437, 475, 516, 560, 607, 657, 710, 766, 825 };

	public static int LEVEL_30 = 825;
	
	public static int getLevelForExperience(int experience) {
		int level = 0;
		for (int i = 0; i < levels.length; i++) {
			if (experience >= levels[i])
				level = i;
		}
		return level;
	}
	
	public static int getExperienceForLevel(int level) {
		return levels[level];
	}
	
	public static double getPower(World worldObj, int xCoord, int yCoord, int zCoord) {

		int i = 0;
		int j;
		float power = 0;

		for (j = -1; j <= 1; ++j) {
			for (int k = -1; k <= 1; ++k) {
				if ((j != 0 || k != 0)
					&& worldObj.isAirBlock(xCoord + k, yCoord, zCoord + j)
					&& worldObj.isAirBlock(xCoord + k, yCoord + 1, zCoord + j)) {
					
					power += ForgeHooks.getEnchantPower(worldObj, xCoord + k * 2, yCoord, zCoord + j * 2);
					power += ForgeHooks.getEnchantPower(worldObj, xCoord + k * 2, yCoord + 1, zCoord + j * 2);

					if (k != 0 && j != 0) {
						power += ForgeHooks.getEnchantPower(worldObj, xCoord + k * 2, yCoord, zCoord + j);
						power += ForgeHooks.getEnchantPower(worldObj, xCoord + k * 2, yCoord + 1, zCoord + j);
						power += ForgeHooks.getEnchantPower(worldObj, xCoord + k, yCoord, zCoord + j * 2);
						power += ForgeHooks.getEnchantPower(worldObj, xCoord + k, yCoord + 1, zCoord + j * 2);
					}
				}
			}
		}
		return power;
	}
	
    public static int calcEnchantability(ItemStack itemStack, int power, boolean max)
    {
        Item item = itemStack.getItem();
        int k = item.getItemEnchantability();
        if (k <= 0)
        {
            return 0;
        }
        else
        {
            if (power > 15)
            {
            	power = 15;
            }

            int l = (max ? 7 : 0) + 1 + (power >> 1) + (max ? power : 0);
            return max ? Math.max(l, power * 2) : Math.max(l / 3, 1);
        }
    }
    

	public static boolean enchantItem(ItemStack itemstack, int level, Random rand) {
		
		if (itemstack != null) {
				
				List list = EnchantmentHelper.buildEnchantmentList(rand, itemstack, level);
				boolean flag = itemstack.itemID == Item.book.itemID;
				if (list != null) {
					
					if (flag) {
						itemstack.itemID = Item.enchantedBook.itemID;
					}

					int j = flag ? rand.nextInt(list.size()) : -1;

					for (int k = 0; k < list.size(); ++k) {
						EnchantmentData enchantmentdata = (EnchantmentData) list.get(k);

						if (!flag || k == j) {
							if (flag) {
								Item.enchantedBook.func_92115_a(itemstack, enchantmentdata);
							} else {
								itemstack.addEnchantment(
										enchantmentdata.enchantmentobj,
										enchantmentdata.enchantmentLevel
								);
							}
						}
					}
				}

			return true;
		} else {
			return false;
		}
	}
}
