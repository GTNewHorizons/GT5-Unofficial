package gtPlusPlus.core.handler.workbench;

import java.util.Comparator;

import net.minecraft.item.crafting.*;

public class Workbench_RecipeSorter implements Comparator<Object>
{
	final Workbench_CraftingHandler CraftingManagerCrafter;

	Workbench_RecipeSorter(final Workbench_CraftingHandler par1CraftingManager)
	{
		this.CraftingManagerCrafter = par1CraftingManager;
	}

	public int compareRecipes(final IRecipe par1IRecipe, final IRecipe par2IRecipe)
	{
		if ((par1IRecipe instanceof ShapelessRecipes) && (par2IRecipe instanceof ShapedRecipes))
		{
			return 1;
		}

		if ((par2IRecipe instanceof ShapelessRecipes) && (par1IRecipe instanceof ShapedRecipes))
		{
			return -1;
		}

		if (par2IRecipe.getRecipeSize() < par1IRecipe.getRecipeSize())
		{
			return -1;
		}

		return par2IRecipe.getRecipeSize() <= par1IRecipe.getRecipeSize() ? 0 : 1;
	}

	@Override
	public int compare(final Object par1Obj, final Object par2Obj)
	{
		return this.compareRecipes((IRecipe)par1Obj, (IRecipe)par2Obj);
	}

}