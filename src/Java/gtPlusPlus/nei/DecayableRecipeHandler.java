package gtPlusPlus.nei;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import crazypants.enderio.gui.IconEIO;
import crazypants.enderio.machine.enchanter.GuiEnchanter;
import gtPlusPlus.core.handler.Recipes.DecayableRecipe;
import gtPlusPlus.core.item.materials.DustDecayable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class DecayableRecipeHandler extends TemplateRecipeHandler {

	public String getRecipeName() {
		return StatCollector.translateToLocal("gtpp.nei.decayables");
	}

	public String getGuiTexture() {
		return "enderio:textures/gui/enchanter.png";
	}

	public Class<? extends GuiContainer> getGuiClass() {
		return GuiEnchanter.class;
	}

	public String getOverlayIdentifier() {
		return "GTPP_Decayables";
	}

	public void loadTransferRects() {
		this.transferRects.add(new RecipeTransferRect(new Rectangle(149, -3, 16, 16), "GTPP_Decayables", new Object[0]));
	}

	public void loadCraftingRecipes(ItemStack result) {
		if (result == null || !DustDecayable.class.isInstance(result.getItem())) {
			return;
		}
		final List<DecayableRecipe> recipes = DecayableRecipe.mRecipes;
		for (final DecayableRecipe recipe : recipes) {
			if (recipe.isValid()) {
				final ItemStack input = recipe.mInput.copy();
				final ItemStack output = recipe.mOutput.copy();
				final DecayableRecipeNEI rec = new DecayableRecipeNEI(input, output, recipe.mTime);
				this.arecipes.add(rec);
			}
		}
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals(getOverlayIdentifier()) && this.getClass() == DecayableRecipeHandler.class) {
			final List<DecayableRecipe> recipes = DecayableRecipe.mRecipes;
			for (final DecayableRecipe recipe : recipes) {
				if (recipe.isValid()) {
					final ItemStack input = recipe.mInput.copy();
					final ItemStack output = recipe.mOutput.copy();
					final DecayableRecipeNEI rec = new DecayableRecipeNEI(input, output, recipe.mTime);
					this.arecipes.add(rec);
				}
			}
		}
		else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		final List<DecayableRecipe> recipes = DecayableRecipe.mRecipes;
		for (final DecayableRecipe recipe : recipes) {
			if (recipe.isValid()) {
				final ItemStack input = recipe.mInput.copy();
				final ItemStack output = recipe.mOutput.copy();
				final DecayableRecipeNEI rec = new DecayableRecipeNEI(input, output, recipe.mTime);
				if (!rec.contains((Collection)rec.input, ingredient)) {
					continue;
				}
				rec.setIngredientPermutation((Collection) rec.input, ingredient);
				this.arecipes.add(rec);
			}
		}}

	public void drawExtras(int recipeIndex) {
		DecayableRecipeNEI recipe = (DecayableRecipeNEI) this.arecipes.get(recipeIndex);
		//GuiDraw.drawStringC(recipe.getEnchantName(), 83, 10, 8421504, false);
		/*
		 * int cost = TileEnchanter.getEnchantmentCost(recipe.recipe, 1); if (cost > 0)
		 * { String s = I18n.format("container.repair.cost", new Object[]{cost});
		 * GuiDraw.drawStringC(s, 83, 46, 8453920); }
		 */

		IconEIO.RECIPE_BUTTON.renderIcon(149.0D, -3.0D, 16.0D, 16.0D, 0.0D, true);
	}

	public class DecayableRecipeNEI extends TemplateRecipeHandler.CachedRecipe
	{
		private PositionedStack input;
		private PositionedStack output;        

		public PositionedStack getResult() {
			return this.output;
		}

		public DecayableRecipeNEI(final ItemStack input, final ItemStack result, final int time) {
			super();
			this.input = new PositionedStack(input, 22, 24);
			this.output = new PositionedStack(result, 129, 24);
		}
	}
}