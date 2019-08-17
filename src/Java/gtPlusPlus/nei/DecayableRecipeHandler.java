package gtPlusPlus.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import crazypants.enderio.machine.enchanter.GuiEnchanter;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.handler.Recipes.DecayableRecipe;
import gtPlusPlus.core.item.base.dusts.BaseItemDustUnique;
import gtPlusPlus.core.item.materials.DustDecayable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.VanillaColours;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.nei.handlers.NeiTextureHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class DecayableRecipeHandler extends TemplateRecipeHandler {

	public String getRecipeName() {
		return StatCollector.translateToLocal("GTPP.container.decaychest.name");
	}

	public String getGuiTexture() {
		return CORE.MODID+":textures/gui/nei/decayables.png";
	}

	public Class<? extends GuiContainer> getGuiClass() {
		return GuiEnchanter.class;
	}

	public String getOverlayIdentifier() {
		return "GTPP_Decayables";
	}
    
    public int recipiesPerPage() {
        return 1;
    }

	public void loadTransferRects() {
		this.transferRects.add(new RecipeTransferRect(new Rectangle(6, 3, 16, 16), "GTPP_Decayables", new Object[0]));
	}

	public void loadCraftingRecipes(ItemStack result) {
		if (result == null || (!DustDecayable.class.isInstance(result.getItem()) && !BaseItemDustUnique.class.isInstance(result.getItem()))) {
			return;
		}
		if (result != null) {
			Logger.INFO("Looking up crafting recipes for "+ItemUtils.getItemName(result));
		}
		final List<DecayableRecipe> recipes = DecayableRecipe.mRecipes;
		for (final DecayableRecipe recipe : recipes) {
			if (recipe.isValid()) {
				final ItemStack input = recipe.mInput.copy();
				final ItemStack output = recipe.mOutput.copy();
				if (!GT_Utility.areStacksEqual(result, output, true)) {
					continue;
				}				
				Logger.INFO("Showing Usage result for "+ItemUtils.getItemName(result));
				final DecayableRecipeNEI rec = new DecayableRecipeNEI(input, output, recipe.mTime);
				this.arecipes.add(rec);
				sort();
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
					sort();
				}
			}
		}
		else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		final List<DecayableRecipe> recipes = DecayableRecipe.mRecipes;
		if (ingredient != null) {
			Logger.INFO("Looking up Usage results for "+ItemUtils.getItemName(ingredient));
		}
		for (final DecayableRecipe recipe : recipes) {
			if (recipe.isValid()) {
				final ItemStack input = recipe.mInput.copy();
				final ItemStack output = recipe.mOutput.copy();
				if (!GT_Utility.areStacksEqual(ingredient, input, true)) {
					continue;
				}				
				Logger.INFO("Showing up Usage results for "+ItemUtils.getItemName(ingredient));
				final DecayableRecipeNEI rec = new DecayableRecipeNEI(input, output, recipe.mTime);				
				//rec.setIngredientPermutation((Collection) rec.input, ingredient);
				this.arecipes.add(rec);
				sort();
			}
		}}
	
	private final void sort() {
		List<DecayableRecipeNEI> g = new ArrayList<DecayableRecipeNEI>();
		for (CachedRecipe u : arecipes) {
			g.add((DecayableRecipeNEI) u);
		}
		if (g != null && !g.isEmpty()) {
			Collections.sort(g);			
		}
	}

	public void drawExtras(int recipeIndex) {
		DecayableRecipeNEI recipe = (DecayableRecipeNEI) this.arecipes.get(recipeIndex);
		
		//GuiDraw.drawStringC(I18n.format("GTPP.container.decaychest.result", new Object[]{}), 43, 10, 8421504, false);

		int cost = recipe.time;
		if (cost > 0){
			
			// NEI Strings
			String s = I18n.format("GTPP.nei.info", new Object[]{cost});
			String s0 = I18n.format("GTPP.nei.timetaken", new Object[]{cost});
			
			// Time Strings
			String s1 = I18n.format("GTPP.time.ticks", new Object[]{cost});
			String s2 = I18n.format("GTPP.time.seconds", new Object[]{cost});
			String s3 = I18n.format("GTPP.time.minutes", new Object[]{cost});
			String s4 = I18n.format("GTPP.time.hours", new Object[]{cost});
			String s5 = I18n.format("GTPP.time.days", new Object[]{cost});
			String s6 = I18n.format("GTPP.time.months", new Object[]{cost});
			int y = 20;
			
			int secs = cost / 20;
			int mins = secs / 60;
			int hours = mins / 60;
			int days = hours / 24;
			int months = days / 30;
			
			
			String suffix;
			int formattedTime;
			if (cost <= 20) {
				suffix = s1;
				formattedTime = cost;
			}
			else if (cost <= (20 * 60)) {
				suffix = s2;
				formattedTime = secs;
			}
			else if (cost <= (20 * 60 * 60)) {
				suffix = s3;
				formattedTime = mins;
			}
			else if (cost <= (20 * 60 * 60 * 24)) {
				suffix = s4;
				formattedTime = hours;
			}
			else if (cost < (20 * 60 * 60 * 24 * 30)) {
				suffix = s5;
				formattedTime = days;
			}
			else if (cost <= (20 * 60 * 60 * 24 * 30)) {
				suffix = s6;
				formattedTime = months;
			}
			else {
				suffix = s1;
				formattedTime = cost;				
			}
			
			

			int x = 5;
			GuiDraw.drawString(s, x, 25, VanillaColours.DYE_BLACK.getAsInt(), false); 
			GuiDraw.drawString(s0, x, 40, VanillaColours.DYE_BLACK.getAsInt(), false); 
			
			
			
			
			
			GuiDraw.drawString(suffix, x + 16, y+30, VanillaColours.DYE_BLACK.getAsInt(), false); 
			
			//Values	
			GuiDraw.drawString((""+formattedTime), x, y+30, VanillaColours.DYE_GREEN.getAsInt(), false); 
			
			
			if (hours > 1) {
				int aLeftoverMinutes = (cost - (hours * (20 * 60 * 60)));
				if (aLeftoverMinutes > 0) {
					int secs2 = aLeftoverMinutes / 20;
					int mins2 = secs2 / 60;
					GuiDraw.drawString(s3, x + 16, y+42, VanillaColours.DYE_BLACK.getAsInt(), false); 
					GuiDraw.drawString((""+mins2), x, y+42, VanillaColours.DYE_GREEN.getAsInt(), false); 
					
				}
				
			}
			
			
			
			
		}
		
		NeiTextureHandler.RECIPE_BUTTON.renderIcon(6.0D, 3.0D, 16.0D, 16.0D, 0.0D, true);
	}

	public class DecayableRecipeNEI extends TemplateRecipeHandler.CachedRecipe implements Comparable<CachedRecipe>
	{
		private PositionedStack input;
		private PositionedStack output; 
		public int time;

		@Override
		public PositionedStack getIngredient() {
			return this.input;
		}

		public PositionedStack getResult() {
			return this.output;
		}

		public DecayableRecipeNEI(final ItemStack input, final ItemStack result, final int time) {
			super();
			this.input = new PositionedStack(input, 93, 24);
			this.output = new PositionedStack(result, 142, 42);
			this.time = time;
		}

		@Override
		public int compareTo(CachedRecipe o) {
			if (o instanceof DecayableRecipeNEI) {
				DecayableRecipeNEI p = (DecayableRecipeNEI) o;
				if (p.time > this.time) {
					return 1;
				}
				else if (p.time == this.time) {
					return 0;
				}
				else {
					return -1;
				}
			}
			return 0;
		}
	}
}