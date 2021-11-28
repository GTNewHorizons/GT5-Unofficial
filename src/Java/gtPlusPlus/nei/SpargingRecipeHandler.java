package gtPlusPlus.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GasSpargingRecipe;
import gregtech.api.util.GasSpargingRecipeMap;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.gui.machine.GUI_DecayablesChest;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class SpargingRecipeHandler extends TemplateRecipeHandler {

	public static final String mNEIName = GasSpargingRecipeMap.mNEIDisplayName;

	public String getRecipeName() {
		return mNEIName;
	}

	public String getGuiTexture() {
		return GasSpargingRecipeMap.mNEIGUIPath;
	}

	public Class<? extends GuiContainer> getGuiClass() {
		return GUI_DecayablesChest.class;
	}

	public String getOverlayIdentifier() {
		return "GTPP_Sparging";
	}

	public int recipiesPerPage() {
		return 1;
	}

	public void loadTransferRects() {
		this.transferRects.add(new RecipeTransferRect(new Rectangle(72, 14, 22, 16), getOverlayIdentifier(), new Object[0]));
	}

	public void loadCraftingRecipes(ItemStack result) {
		if (result == null) {
			return;
		}
		if (result != null) {
			//Logger.INFO("Looking up crafting recipes for "+ItemUtils.getItemName(result));
		}
		final List<GasSpargingRecipe> recipes = GasSpargingRecipeMap.mRecipes;
		for (final GasSpargingRecipe recipe : recipes) {
			if (recipe.isValid()) {				
				final GasSpargingRecipeNEI rec = new GasSpargingRecipeNEI(recipe);
				this.arecipes.add(rec);
				sort();
			}
		}
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals(getOverlayIdentifier()) && this.getClass() == SpargingRecipeHandler.class) {
			final List<GasSpargingRecipe> recipes = GasSpargingRecipeMap.mRecipes;
			for (final GasSpargingRecipe recipe : recipes) {
				if (recipe.isValid()) {
					final GasSpargingRecipeNEI rec = new GasSpargingRecipeNEI(recipe);
					this.arecipes.add(rec);
					sort();
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		final List<GasSpargingRecipe> recipes = GasSpargingRecipeMap.mRecipes;
		if (ingredient != null) {
			//Logger.INFO("Looking up Usage results for "+ItemUtils.getItemName(ingredient));
		}
		for (final GasSpargingRecipe recipe : recipes) {
			if (recipe.isValid()) {
				final GasSpargingRecipeNEI rec = new GasSpargingRecipeNEI(recipe);
				this.arecipes.add(rec);
				sort();
			}
		}
	}

	private final void sort() {
		List<GasSpargingRecipeNEI> g = new ArrayList<GasSpargingRecipeNEI>();
		for (CachedRecipe u : arecipes) {
			g.add((GasSpargingRecipeNEI) u);
		}
		if (g != null && !g.isEmpty()) {
			Collections.sort(g);
		}
	}

	public void drawExtras(int recipeIndex) {
		GasSpargingRecipeNEI recipe = (GasSpargingRecipeNEI) this.arecipes.get(recipeIndex);		
		//NeiTextureHandler.RECIPE_BUTTON.renderIcon(72.0D, 14.0D, 22.0D, 16.0D, 0.0D, true);
	}
	
	@Override
	public List<String> handleItemTooltip(final GuiRecipe gui, final ItemStack aStack, final List<String> currenttip, final int aRecipeIndex) {
		final TemplateRecipeHandler.CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
		if ((tObject instanceof GasSpargingRecipeNEI)) {
			final GasSpargingRecipeNEI tRecipe = (GasSpargingRecipeNEI) tObject;
			ItemStack aInput = tRecipe.mInputs.get(0).item;
			for (final PositionedStack tStack : tRecipe.mOutputs) {
				if (aStack == tStack.item) {
					if (ItemList.Display_Fluid.isStackEqual(tStack.item, true, true) && ((FixedPositionedStack) tStack).mChance <= 10000) {						
						if (GT_Utility.areStacksEqual(aStack, aInput, true)) {
							currenttip.add("The amount returned is the remainder after all other outputs.");
						}
						currenttip.add("Maximum Output: " + (((FixedPositionedStack) tStack).mChance / 100) + "." + ((((FixedPositionedStack) tStack).mChance % 100) < 10 ? "0" + (((FixedPositionedStack) tStack).mChance % 100) : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100)) + "L");
						break;
					}
					break;
				}
			}
			for (final PositionedStack tStack : tRecipe.mInputs) {
				if (GT_Utility.areStacksEqual(aStack, tStack.item)) {
					if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) ||
							(tStack.item.stackSize != 0)) {
						break;
					}
					if (ItemUtils.isControlCircuit(aStack)) {
						currenttip.add("Does not get consumed in the process");					
					}
					break;
				}
			}
		}
		return currenttip;
	}
	
	public class FixedPositionedStack
	extends PositionedStack {
		public final int mChance;
		public boolean permutated = false;

		public FixedPositionedStack(final Object object, final int x, final int y) {
			this(object, x, y, 0);
		}

		public FixedPositionedStack(final Object object, final int x, final int y, final int aChance) {
			super(object, x, y, true);
			this.mChance = aChance;
		}

		@Override
		public void generatePermutations() {
			if (this.permutated) {
				return;
			}
			final ArrayList<ItemStack> tDisplayStacks = new ArrayList<ItemStack>();
			for (final ItemStack tStack : this.items) {
				if (GT_Utility.isStackValid(tStack)) {
					if (tStack.getItemDamage() == 32767) {
						final List<ItemStack> permutations = codechicken.nei.ItemList.itemMap.get(tStack.getItem());
						if (!permutations.isEmpty()) {
							ItemStack stack;
							for (final Iterator<ItemStack> i$ = permutations.iterator(); i$.hasNext(); tDisplayStacks.add(GT_Utility.copyAmount(tStack.stackSize, new Object[]{stack}))) {
								stack = i$.next();
							}
						} else {
							final ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
							base.stackTagCompound = tStack.stackTagCompound;
							tDisplayStacks.add(base);
						}
					} else {
						tDisplayStacks.add(GT_Utility.copy(new Object[]{tStack}));
					}
				}
			}
			this.items = (tDisplayStacks.toArray(new ItemStack[0]));
			if (this.items.length == 0) {
				this.items = new ItemStack[]{new ItemStack(Blocks.fire)};
			}
			this.permutated = true;
			this.setPermutationToRender(0);
		}
	}

	public class GasSpargingRecipeNEI extends CachedRecipe implements Comparable<CachedRecipe> {

		public final GasSpargingRecipe mRecipe;
		public final List<PositionedStack> mOutputs = new ArrayList<PositionedStack>();
		public final List<PositionedStack> mInputs = new ArrayList<PositionedStack>();

		public GasSpargingRecipeNEI(GasSpargingRecipe tRecipe) {
			super();
			this.mRecipe = tRecipe;
			int tStartIndex = 0;
			if (tRecipe.mFluidInputs.length > 0) {
				if ((tRecipe.mFluidInputs[0] != null) && (tRecipe.mFluidInputs[0].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[0], true), 12, 5));
				}
				if ((tRecipe.mFluidInputs.length > 1) && (tRecipe.mFluidInputs[1] != null) && (tRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[1], true), 30, 5));
				}
				if ((tRecipe.mFluidInputs.length > 2) && (tRecipe.mFluidInputs[2] != null) && (tRecipe.mFluidInputs[2].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[2], true), 48, 5));
				}
				if ((tRecipe.mFluidInputs.length > 3) && (tRecipe.mFluidInputs[3] != null) && (tRecipe.mFluidInputs[3].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[3], true), 12, 23));
				}
				if ((tRecipe.mFluidInputs.length > 4) && (tRecipe.mFluidInputs[4] != null) && (tRecipe.mFluidInputs[4].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[4], true), 30, 23));
				}
				if ((tRecipe.mFluidInputs.length > 5) && (tRecipe.mFluidInputs[5] != null) && (tRecipe.mFluidInputs[5].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[5], true), 48, 23));
				}
				if ((tRecipe.mFluidInputs.length > 6) && (tRecipe.mFluidInputs[6] != null) && (tRecipe.mFluidInputs[6].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[6], true), 12, 41));
				}
				if ((tRecipe.mFluidInputs.length > 7) && (tRecipe.mFluidInputs[7] != null) && (tRecipe.mFluidInputs[7].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[7], true), 30, 41));
				}
				if ((tRecipe.mFluidInputs.length > 8) && (tRecipe.mFluidInputs[8] != null) && (tRecipe.mFluidInputs[8].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[8], true), 48, 41));
				}
			}

			tStartIndex = 0;
			if (tRecipe.mFluidOutputs.length > 0) {
				if ((tRecipe.mFluidOutputs[0] != null) && (tRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[0], false), 102, 5, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 1) && (tRecipe.mFluidOutputs[1] != null) && (tRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[1], false), 120, 5, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 2) && (tRecipe.mFluidOutputs[2] != null) && (tRecipe.mFluidOutputs[2].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[2], false), 138, 5, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 3) && (tRecipe.mFluidOutputs[3] != null) && (tRecipe.mFluidOutputs[3].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[3], false), 102, 23, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 4) && (tRecipe.mFluidOutputs[4] != null) && (tRecipe.mFluidOutputs[4].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[4], false), 120, 23, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 5) && (tRecipe.mFluidOutputs[5] != null) && (tRecipe.mFluidOutputs[5].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[5], false), 138, 23, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 6) && (tRecipe.mFluidOutputs[6] != null) && (tRecipe.mFluidOutputs[6].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[6], false), 102, 41, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 7) && (tRecipe.mFluidOutputs[7] != null) && (tRecipe.mFluidOutputs[7].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[7], false), 120, 41, tRecipe.getMaxOutput(tStartIndex++)));
				}
				if ((tRecipe.mFluidOutputs.length > 8) && (tRecipe.mFluidOutputs[8] != null) && (tRecipe.mFluidOutputs[8].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[8], false), 138, 41, tRecipe.getMaxOutput(tStartIndex++)));
				}
				Logger.INFO("Outputs: "+tRecipe.mFluidOutputs.length);
			}
		}		

		@Override
		public int compareTo(CachedRecipe o) {
			boolean b = GasSpargingRecipeNEI.class.isInstance(o);
			if (b) {
				GasSpargingRecipeNEI p = (GasSpargingRecipeNEI) o;
				if (p.mOutputs.size() > this.mOutputs.size()) {
					return 1;
				} else if (p.mOutputs.size() == this.mOutputs.size()) {
					return 0;
				} else {
					return -1;
				}
			}
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null) {
				if (GasSpargingRecipeNEI.class.isInstance(obj)) {
					GasSpargingRecipeNEI p = (GasSpargingRecipeNEI) obj;
					if (p != null) {
						if (GT_Utility.areStacksEqual(p.mInputs.get(0).item, this.mInputs.get(0).item, true)) {
							if (p.mOutputs.size() == this.mOutputs.size()) {
								return true;
							}
						}
					}					
				}
			}
			return false;
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(SpargingRecipeHandler.this.cycleticks / 10, this.mInputs);
		}

		@Override
		public PositionedStack getResult() {
			return null;
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			return this.mOutputs;
		}
		
		
	}
}