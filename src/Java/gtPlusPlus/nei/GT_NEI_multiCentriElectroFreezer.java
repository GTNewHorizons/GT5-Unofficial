package gtPlusPlus.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.*;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.util.*;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map_Internal;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_NEI_multiCentriElectroFreezer
extends TemplateRecipeHandler {
	public static final int sOffsetX = 5;
	public static final int sOffsetY = 11;

	static {
		GuiContainerManager.addInputHandler(new GT_RectHandler());
		GuiContainerManager.addTooltipHandler(new GT_RectHandler());
	}

	protected GTPP_Recipe_Map_Internal mRecipeMap;

	public GT_NEI_multiCentriElectroFreezer(GTPP_Recipe_Map_Internal aMap) {
		this.mRecipeMap = aMap;
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 13, 36, 18), this.getRecipeMapName(), new Object[0]));
		if (!NEI_GT_Config.sIsAdded) {
			FMLInterModComms.sendRuntimeMessage(GT_Values.GT, "NEIPlugins", "register-crafting-handler", "gregtechplusplus@" + this.getRecipeName() + "@" + this.getRecipeMapName());
			GuiCraftingRecipe.craftinghandlers.add(this);
			GuiUsageRecipe.usagehandlers.add(this);
		}
	}
	
	public static void logRecipeError(GT_Recipe aRecipe) {
		if (aRecipe == null) {
			Logger.INFO("Tried to handle null recipe. :(");
		}
		else {
			ItemStack[] aInputs = aRecipe.mInputs;
			ItemStack[] aOutputs = aRecipe.mOutputs;
			FluidStack[] aFluidInputs = aRecipe.mFluidInputs;
			FluidStack[] aFluidOutputs = aRecipe.mFluidOutputs;
			int aEU = aRecipe.mEUt;
			int aTime = aRecipe.mDuration;
			int aSpecialValue = aRecipe.mSpecialValue;
			String aInputitems = ItemUtils.getArrayStackNames(aInputs);
			String aOutputitems = ItemUtils.getArrayStackNames(aOutputs);
			String aInputFluids = ItemUtils.getArrayStackNames(aFluidInputs);
			String aOutputFluids = ItemUtils.getArrayStackNames(aFluidOutputs);
			Logger.INFO("Logging Broken Recipe Details:");
			Logger.INFO("Input Items - "+aInputitems);
			Logger.INFO("Output Items - "+aOutputitems);
			Logger.INFO("Input Fluids - "+aInputFluids);
			Logger.INFO("Output Fluids - "+aOutputFluids);
			Logger.INFO("EU/t - "+aEU);
			Logger.INFO("Duration - "+aTime);
			Logger.INFO("Special Value - "+aSpecialValue);
		}
	}

	public List<GTPP_Recipe> getSortedRecipes() {
		List<GTPP_Recipe> result = new ArrayList(this.mRecipeMap.mRecipeList);
		Collections.sort(result);
		return result;
	}
	
	public static void drawText(final int aX, final int aY, final String aString, final int aColor) {
		Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_multiCentriElectroFreezer(mRecipeMap);
	}

	@Override
	public void loadCraftingRecipes(final String outputId, final Object... results) {
		if (outputId.equals(getRecipeMapName())) {
			for (GTPP_Recipe tRecipe : getSortedRecipes()) {
				if (!tRecipe.mHidden) {
					CachedDefaultRecipe tNEIRecipe = getCachedRecipe(tRecipe);
					if (tNEIRecipe == null) {
						continue;
					}
					this.arecipes.add(tNEIRecipe);
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(final ItemStack aResult) {
		ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aResult);

		ArrayList<ItemStack> tResults = new ArrayList<ItemStack>();
		tResults.add(aResult);
		tResults.add(GT_OreDictUnificator.get(true, aResult));
		if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
			for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
				tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
			}
		}
		FluidStack tFluid = GT_Utility.getFluidForFilledItem(aResult, true);
		if (tFluid != null) {
			tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
			for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
				if (tData.fluid.isFluidEqual(tFluid)) {
					tResults.add(GT_Utility.copy(new Object[]{tData.filledContainer}));
				}
			}
		}
		for (GTPP_Recipe tRecipe : getSortedRecipes()) {
			if (!tRecipe.mHidden) {
				CachedDefaultRecipe tNEIRecipe = getCachedRecipe(tRecipe);
				if (tNEIRecipe == null) {
					continue;
				}
				for (ItemStack tStack : tResults) {
					if (tNEIRecipe.contains(tNEIRecipe.mOutputs, tStack)) {
						this.arecipes.add(tNEIRecipe);
						break;
					}
				}
			}
		}
		//CachedDefaultRecipe tNEIRecipe;
	}

	public void loadUsageRecipes(ItemStack aInput) {
		ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aInput);

		ArrayList<ItemStack> tInputs = new ArrayList<ItemStack>();
		tInputs.add(aInput);
		tInputs.add(GT_OreDictUnificator.get(false, aInput));
		if ((tPrefixMaterial != null) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
			for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
				tInputs.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
			}
		}
		FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInput, true);
		if (tFluid != null) {
			tInputs.add(GT_Utility.getFluidDisplayStack(tFluid, false));
			for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
				if (tData.fluid.isFluidEqual(tFluid)) {
					tInputs.add(GT_Utility.copy(new Object[]{tData.filledContainer}));
				}
			}
		}
		for (GTPP_Recipe tRecipe : getSortedRecipes()) {
			if (!tRecipe.mHidden) {
				CachedDefaultRecipe tNEIRecipe = getCachedRecipe(tRecipe);
				if (tNEIRecipe == null) {
					continue;
				}
				for (ItemStack tStack : tInputs) {
					if (tNEIRecipe.contains(tNEIRecipe.mInputs, tStack)) {
						this.arecipes.add(tNEIRecipe);
						break;
					}
				}
			}
		}
		//CachedDefaultRecipe tNEIRecipe;
	}

	public String getRecipeMapName() {
		return this.mRecipeMap.mNEIName;
	}

	@Override
	public String getOverlayIdentifier() {
		return this.mRecipeMap.mNEIName;
	}

	@Override
	public void drawBackground(final int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(this.getGuiTexture());
		GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 78);
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public String getRecipeName() {
		return GT_LanguageManager.getTranslation(this.mRecipeMap.mUnlocalizedName);
	}

	@Override
	public String getGuiTexture() {
		return this.mRecipeMap.mNEIGUIPath;
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
					currenttip.add("Chance: " + (((FixedPositionedStack) tStack).mChance / 100) + "." + ((((FixedPositionedStack) tStack).mChance % 100) < 10 ? "0" + (((FixedPositionedStack) tStack).mChance % 100) : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100)) + "%");
					break;
				}
			}
			for (final PositionedStack tStack : tRecipe.mInputs) {
				if (aStack == tStack.item) {
					if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) ||
							(tStack.item.stackSize != 0)) {
						break;
					}
					currenttip.add("Does not get consumed in the process");
					break;
				}
			}
		}
		return currenttip;
	}

	@Override
	public void drawExtras(final int aRecipeIndex) {
		final int tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
		final int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
		if (tEUt != 0) {
			drawText(10, 83, "Total: " + (tDuration * tEUt) + " EU", -16777216);
			drawText(10, 93, "Usage: " + tEUt + " EU/t", -16777216);
			if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
				drawText(10, 103, "Voltage: " + (tEUt / this.mRecipeMap.mAmperage) + " EU", -16777216);
				drawText(10, 113, "Amperage: " + this.mRecipeMap.mAmperage, -16777216);
			} else {
				drawText(10, 103, "Voltage: unspecified", -16777216);
				drawText(10, 113, "Amperage: unspecified", -16777216);
			}
		}
		if (tDuration > 0) {
			drawText(10, 123, "Time: " + (tDuration < 20 ? "< 1" : Integer.valueOf(tDuration / 20)) + " secs", -16777216);
		}
		if ((GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre)) || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost))) {
			drawText(10, 133, this.mRecipeMap.mNEISpecialValuePre + (((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue * this.mRecipeMap.mNEISpecialValueMultiplier) + this.mRecipeMap.mNEISpecialValuePost, -16777216);
		}
	}

	public static class GT_RectHandler
	implements IContainerInputHandler, IContainerTooltipHandler {
		@Override
		public boolean mouseClicked(final GuiContainer gui, final int mousex, final int mousey, final int button) {
			if (this.canHandle(gui)) {
				if (button == 0) {
					return this.transferRect(gui, false);
				}
				if (button == 1) {
					return this.transferRect(gui, true);
				}
			}
			return false;
		}

		@Override
		public boolean lastKeyTyped(final GuiContainer gui, final char keyChar, final int keyCode) {
			return false;
		}

		public boolean canHandle(final GuiContainer gui) {
			return (((gui instanceof GT_GUIContainer_BasicMachine)) && (GT_Utility.isStringValid(((GT_GUIContainer_BasicMachine) gui).mNEI)) /*|| ((gui instanceof GT_GUIContainer_FusionReactor)) && (GT_Utility.isStringValid(((GT_GUIContainer_FusionReactor) gui).mNEI))*/);
		}

		@Override
		public List<String> handleTooltip(final GuiContainer gui, final int mousex, final int mousey, final List<String> currenttip) {
			if ((this.canHandle(gui)) && (currenttip.isEmpty())) {
				if ((gui instanceof GT_GUIContainer_BasicMachine) && new Rectangle(65, 13, 36, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) {
					currenttip.add("Recipes");
				} /*else if (gui instanceof GT_GUIContainer_FusionReactor && new Rectangle(145, 0, 24, 24).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_FusionReactor) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_FusionReactor) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) {
                    currenttip.add("Recipes");
                }*/

			}
			return currenttip;
		}

		private boolean transferRect(final GuiContainer gui, final boolean usage) {
			if (gui instanceof GT_GUIContainer_BasicMachine) {
				return (this.canHandle(gui)) && (new Rectangle(65, 13, 36, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) && (usage ? GuiUsageRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]) : GuiCraftingRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]));
			} /*else if (gui instanceof GT_GUIContainer_FusionReactor) {
                return (canHandle(gui)) && (new Rectangle(145, 0, 24, 24).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_FusionReactor) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_FusionReactor) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) && (usage ? GuiUsageRecipe.openRecipeGui(((GT_GUIContainer_FusionReactor) gui).mNEI, new Object[0]) : GuiCraftingRecipe.openRecipeGui(((GT_GUIContainer_FusionReactor) gui).mNEI, new Object[0]));
            }*/
			return false;
		}

		@Override
		public List<String> handleItemDisplayName(final GuiContainer gui, final ItemStack itemstack, final List<String> currenttip) {
			return currenttip;
		}

		@Override
		public List<String> handleItemTooltip(final GuiContainer gui, final ItemStack itemstack, final int mousex, final int mousey, final List<String> currenttip) {
			return currenttip;
		}

		@Override
		public boolean keyTyped(final GuiContainer gui, final char keyChar, final int keyCode) {
			return false;
		}

		@Override
		public void onKeyTyped(final GuiContainer gui, final char keyChar, final int keyID) {
		}

		@Override
		public void onMouseClicked(final GuiContainer gui, final int mousex, final int mousey, final int button) {
		}

		@Override
		public void onMouseUp(final GuiContainer gui, final int mousex, final int mousey, final int button) {
		}

		@Override
		public boolean mouseScrolled(final GuiContainer gui, final int mousex, final int mousey, final int scrolled) {
			return false;
		}

		@Override
		public void onMouseScrolled(final GuiContainer gui, final int mousex, final int mousey, final int scrolled) {
		}

		@Override
		public void onMouseDragged(final GuiContainer gui, final int mousex, final int mousey, final int button, final long heldTime) {
		}
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
	private static final HashMap<Integer, Pair<Integer, Integer>> mInputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();
	private static final HashMap<Integer, Pair<Integer, Integer>> mOutputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();
	
	static {
		int aSlotX_1 = 12;
		int aSlotX_2 = 30;
		int aSlotX_3 = 48;
		int aSlotY_1 = 5;
		int aSlotY_2 = 23;
		int aSlotY_3 = 41;
		int aSlotY_10 = 65; // Only if 9 input items and a FLuid
		mInputSlotMap.put(0, new Pair<Integer, Integer>(aSlotX_1, aSlotY_1));
		mInputSlotMap.put(1, new Pair<Integer, Integer>(aSlotX_2, aSlotY_1));
		mInputSlotMap.put(2, new Pair<Integer, Integer>(aSlotX_3, aSlotY_1));
		mInputSlotMap.put(3, new Pair<Integer, Integer>(aSlotX_1, aSlotY_2));
		mInputSlotMap.put(4, new Pair<Integer, Integer>(aSlotX_2, aSlotY_2));
		mInputSlotMap.put(5, new Pair<Integer, Integer>(aSlotX_3, aSlotY_2));
		mInputSlotMap.put(6, new Pair<Integer, Integer>(aSlotX_1, aSlotY_3));
		mInputSlotMap.put(7, new Pair<Integer, Integer>(aSlotX_2, aSlotY_3));
		mInputSlotMap.put(8, new Pair<Integer, Integer>(aSlotX_3, aSlotY_3));
		mInputSlotMap.put(9, new Pair<Integer, Integer>(aSlotX_1, aSlotY_10));
		mInputSlotMap.put(10, new Pair<Integer, Integer>(aSlotX_2, aSlotY_10));
		mInputSlotMap.put(11, new Pair<Integer, Integer>(aSlotX_3, aSlotY_10));
		aSlotX_1 = 102;
		aSlotX_2 = 120;
		aSlotX_3 = 138;
		mOutputSlotMap.put(0, new Pair<Integer, Integer>(aSlotX_1, aSlotY_1));
		mOutputSlotMap.put(1, new Pair<Integer, Integer>(aSlotX_2, aSlotY_1));
		mOutputSlotMap.put(2, new Pair<Integer, Integer>(aSlotX_3, aSlotY_1));
		mOutputSlotMap.put(3, new Pair<Integer, Integer>(aSlotX_1, aSlotY_2));
		mOutputSlotMap.put(4, new Pair<Integer, Integer>(aSlotX_2, aSlotY_2));
		mOutputSlotMap.put(5, new Pair<Integer, Integer>(aSlotX_3, aSlotY_2));
		mOutputSlotMap.put(6, new Pair<Integer, Integer>(aSlotX_1, aSlotY_3));
		mOutputSlotMap.put(7, new Pair<Integer, Integer>(aSlotX_2, aSlotY_3));
		mOutputSlotMap.put(8, new Pair<Integer, Integer>(aSlotX_3, aSlotY_3));
		mOutputSlotMap.put(9, new Pair<Integer, Integer>(aSlotX_1, aSlotY_10));
		mOutputSlotMap.put(10, new Pair<Integer, Integer>(aSlotX_2, aSlotY_10));
		mOutputSlotMap.put(11, new Pair<Integer, Integer>(aSlotX_3, aSlotY_10));
	}
	
	private CachedDefaultRecipe getCachedRecipe(GT_Recipe aRecipe) {
		try {
			return new CachedDefaultRecipe(aRecipe);
		}
		catch(Throwable e) {
			logRecipeError(aRecipe);
		}
		return null;
	}

	public class CachedDefaultRecipe
	extends TemplateRecipeHandler.CachedRecipe {
		
		
		public final GT_Recipe mRecipe;
		public final List<PositionedStack> mOutputs = new ArrayList<PositionedStack>();
		public final List<PositionedStack> mInputs = new ArrayList<PositionedStack>();

		public CachedDefaultRecipe(final GT_Recipe aRecipe) {
			super();
			this.mRecipe = aRecipe;
			int aInputItemsCount = this.mRecipe.mInputs.length;
			int aInputFluidsCount = this.mRecipe.mFluidInputs.length;			
			int aOutputItemsCount = this.mRecipe.mOutputs.length;
			int aOutputFluidsCount = this.mRecipe.mFluidOutputs.length;
			int aInputSlotsUsed = 0;
			int aOutputSlotsUsed = 0;			
			int aSlotToCheck = 0;	

			// Special Slot
			if (aRecipe.mSpecialItems != null) {
				this.mInputs.add(new FixedPositionedStack(aRecipe.mSpecialItems, 120, 52));
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
					int x = mInputSlotMap.get(aSlotToCheck).getKey();
					int y = mInputSlotMap.get(aSlotToCheck).getValue();
					this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(aSlotToCheck), x, y));	
					aSlotToCheck++;
					aInputSlotsUsed++;
				}
			}
			aSlotToCheck = 0;	
			// Upto 9 Output Slots
			if (aOutputItemsCount > 0) {			
				if (aOutputItemsCount > 9) {
					aOutputItemsCount = 9;
				}		
				for (int i=0;i<aOutputItemsCount;i++) {
					int x = mOutputSlotMap.get(aSlotToCheck).getKey();
					int y = mOutputSlotMap.get(aSlotToCheck).getValue();
					this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(aSlotToCheck), x, y, aRecipe.getOutputChance(aSlotToCheck)));	
					aSlotToCheck++;
					aOutputSlotsUsed++;
				}
			}					
			
			/*
			 * Fluids
			 */

			// Upto 9 Fluid Inputs Slots
			aSlotToCheck = aInputSlotsUsed;	
			if (aInputFluidsCount > 0) {				
				for (int i=0;i<aInputFluidsCount;i++) {
					int x = mInputSlotMap.get(aSlotToCheck).getKey();
					int y = mInputSlotMap.get(aSlotToCheck).getValue();
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[i], true), x, y));
					aSlotToCheck++;
					aInputSlotsUsed++;
				}
			}
			// Upto 9 Fluid Outputs Slots
			aSlotToCheck = aOutputSlotsUsed;	
			if (aOutputFluidsCount > 0) {				
				for (int i=0;i<aOutputFluidsCount;i++) {
					int x = mOutputSlotMap.get(aSlotToCheck).getKey();
					int y = mOutputSlotMap.get(aSlotToCheck).getValue();
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[i], true), x, y));
					aSlotToCheck++;
					aOutputSlotsUsed++;
				}
			}
			
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(GT_NEI_multiCentriElectroFreezer.this.cycleticks / 10, this.mInputs);
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
