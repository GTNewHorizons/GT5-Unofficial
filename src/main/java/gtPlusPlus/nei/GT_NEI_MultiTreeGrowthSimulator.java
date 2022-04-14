package gtPlusPlus.nei;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.item.ModItems;
import net.minecraft.item.ItemStack;

public class GT_NEI_MultiTreeGrowthSimulator extends GTPP_NEI_DefaultHandler {

	private static final HashMap<Integer, Pair<Integer, Integer>> mInputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();
	private static final HashMap<Integer, Pair<Integer, Integer>> mOutputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();

	static {
		int[] aSlotX = new int[] {12, 30, 48};
		int[] aSlotY = new int[] {5, 23, 41, 64};	
		// Input slots
		int aIndex = 0;
		for (int y=0; y<aSlotY.length;y++) {
			for (int x=0; x<aSlotX.length;x++) {
				mInputSlotMap.put(aIndex++, new Pair<Integer, Integer>(aSlotX[x], aSlotY[y]));				
			}
		}
		// Output slots
		aSlotX = new int[] {102, 120, 138};
		aIndex = 0;
		for (int y=0; y<aSlotY.length;y++) {
			for (int x=0; x<aSlotX.length;x++) {
				mOutputSlotMap.put(aIndex++, new Pair<Integer, Integer>(aSlotX[x], aSlotY[y]));				
			}
		}
	}
	
	public GT_NEI_MultiTreeGrowthSimulator() {
		super(GTPP_Recipe_Map.sTreeSimFakeRecipes);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_MultiTreeGrowthSimulator();
	}	

	@Override
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new TreeSimDefaultRecipe(aRecipe);
    }
	
	@Override
	public void drawBackground(final int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 89);
	}
	
	@Override
	public String getGuiTexture() {
		return RES_PATH_GUI + "basicmachines/FissionFuel.png";
	}

	@Override
	public void drawExtras(final int aRecipeIndex) {
		if (ModItems.fluidFertBasic != null) {		
			drawText(5, 90, "Chance of Sapling output if", -16777216);
			drawText(5, 100, ""+ModItems.fluidFertBasic.getLocalizedName()+" is provided.", -16777216);
			drawText(5, 110, "This is optional.", -16777216);	
		}
	}
	
	@Override
	public List<String> handleItemTooltip(final GuiRecipe gui, final ItemStack aStack, final List<String> currenttip, final int aRecipeIndex) {
		final TemplateRecipeHandler.CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
		if ((tObject instanceof CachedDefaultRecipe)) {
			final CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
			for (final PositionedStack tStack : tRecipe.mOutputs) {
				if (aStack == tStack.item) {
					if ((!(tStack instanceof FixedPositionedStack)) || (((FixedPositionedStack) tStack).mChance <= 0) || (((FixedPositionedStack) tStack).mChance == 10000)) {
						break;
					}
					if (ModItems.fluidFertBasic != null) {
						currenttip.add("Chance output if "+ModItems.fluidFertBasic.getLocalizedName()+" is provided.");						
					}
					break;
				}
			}
		}
		return currenttip;
	}
	
	public class TreeSimDefaultRecipe extends CachedDefaultRecipe {

		public TreeSimDefaultRecipe(final GT_Recipe aRecipe) {
			super(aRecipe);
			
		}

		@Override
		public void handleSlots() {

			int aInputItemsCount = this.mRecipe.mInputs.length;
			int aInputFluidsCount = this.mRecipe.mFluidInputs.length;			
			int aOutputItemsCount = this.mRecipe.mOutputs.length;
			int aInputSlotsUsed = 0;
			int aOutputSlotsUsed = 0;			
			int aSlotToCheck = 0;	

			// Special Slot
			if (mRecipe.mSpecialItems != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.mSpecialItems, 120, 52));
			}
			
			/*
			 * Items
			 */
			
			// Upto 9 Inputs Slots
			if (aInputItemsCount > 0) {				
				if (aInputItemsCount > 9) {
					aInputItemsCount = 9;
				}				
				for (int i=0;i<aInputItemsCount;i++) {
					int x = mInputSlotMap.get(aInputSlotsUsed).getKey();
					int y = mInputSlotMap.get(aInputSlotsUsed).getValue();
					ItemStack aRepStack = mRecipe.getRepresentativeInput(aSlotToCheck++);
					if (aRepStack != null) {
						this.mInputs.add(new FixedPositionedStack(aRepStack, x, y));
						aInputSlotsUsed++;
					}
				}
			}
			aSlotToCheck = 0;	
			// Upto 9 Output Slots
			if (aOutputItemsCount > 0) {			
				if (aOutputItemsCount > 9) {
					aOutputItemsCount = 9;
				}
				boolean tUnificate = mRecipeMap.mNEIUnificateOutput;
				for (int i=0;i<aOutputItemsCount;i++) {
					int x = mOutputSlotMap.get(aOutputSlotsUsed).getKey();
					int y = mOutputSlotMap.get(aOutputSlotsUsed).getValue();
					ItemStack aRepStack = mRecipe.getOutput(aSlotToCheck);
					if (aRepStack != null) {
						this.mOutputs.add(new FixedPositionedStack(aRepStack, x, y, mRecipe.getOutputChance(aSlotToCheck), tUnificate));
						aOutputSlotsUsed++;
					}
					aSlotToCheck++;
				}
			}					
			
			/*
			 * Fluids
			 */
			aInputSlotsUsed = 9;
			aSlotToCheck = aInputSlotsUsed;	
			if (aInputFluidsCount > 0) {				
				for (int i=0;i<aInputFluidsCount;i++) {
					int x = mInputSlotMap.get(aSlotToCheck).getKey();
					int y = mInputSlotMap.get(aSlotToCheck).getValue();
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[i], true), x, y));
					aSlotToCheck++;
					aInputSlotsUsed++;
				}
			}
		}
	}
	
}
