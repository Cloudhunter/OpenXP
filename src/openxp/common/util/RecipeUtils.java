package openxp.common.util;

import openxp.OpenXP;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeUtils {

	public static void addAllRecipes() {

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
				new ItemStack(OpenXP.Blocks.XPBottler),
				new Object[] {
					"iii",
					"igi",
					"iii",
					Character.valueOf('i'), new ItemStack(Item.ingotIron),
					Character.valueOf('g'), new ItemStack(Block.thinGlass),			
				}
			));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
				new ItemStack(OpenXP.Blocks.autoAnvil),
				new Object[] {
					" b ",
					"rar",
					Character.valueOf('b'), new ItemStack(Item.expBottle),
					Character.valueOf('r'), new ItemStack(Item.redstone),
					Character.valueOf('a'), new ItemStack(Block.anvil),
				}
			));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
				new ItemStack(OpenXP.Blocks.enchantmentTable),
				new Object[] {
					" b ",
					"rer",
					Character.valueOf('b'), new ItemStack(Item.expBottle),
					Character.valueOf('r'), new ItemStack(Item.redstone),
					Character.valueOf('e'), new ItemStack(Block.enchantmentTable),
				}
			));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
				new ItemStack(OpenXP.Blocks.lifeStone),
				new Object[] {
					" b ",
					"rcr",
					Character.valueOf('b'), new ItemStack(Item.expBottle),
					Character.valueOf('r'), new ItemStack(Item.redstone),
					Character.valueOf('c'), new ItemStack(Block.cobblestoneMossy),
				}
			));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
				new ItemStack(OpenXP.Blocks.XPSponge),
				new Object[] {
					"ooo",
					"pgp",
					"ooo",
					Character.valueOf('o'), new ItemStack(Block.obsidian),
					Character.valueOf('p'), new ItemStack(Item.enderPearl),
					Character.valueOf('g'), new ItemStack(Block.thinGlass),
				}
			));
	}

}
