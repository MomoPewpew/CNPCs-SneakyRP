package noppes.npcs.controllers.data;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.controllers.RecipeController;

public class RecipesDefault {
	public static void addRecipe(String name, Object ob, boolean isGlobal, Object... recipe) {
		ItemStack item;
		if (ob instanceof Item) {
			item = new ItemStack((Item) ob);
		} else if (ob instanceof Block) {
			item = new ItemStack((Block) ob);
		} else {
			item = (ItemStack) ob;
		}

		RecipeCarpentry recipeAnvil = new RecipeCarpentry(name);
		recipeAnvil.isGlobal = isGlobal;
		recipeAnvil = RecipeCarpentry.createRecipe(recipeAnvil, item, recipe);

		try {
			RecipeController.instance.saveRecipe(recipeAnvil);
		} catch (IOException var7) {
		}

	}

	public static void loadDefaultRecipes(int i) {
		if (i < 0) {
			addRecipe("Npc Wand", CustomItems.wand, true, "XX", " Y", " Y", 'X', Items.BREAD, 'Y', Items.STICK);
			addRecipe("Mob Cloner", CustomItems.cloner, true, "XX", "XY", " Y", 'X', Items.BREAD, 'Y', Items.STICK);
		}

	}
}
