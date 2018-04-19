package gtPlusPlus.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.util.GregtechOreDictUnificator;

import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.util.*;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class GT_NEI_MultiBlockHandler
extends TemplateRecipeHandler {
	public static final int sOffsetX = 5;
	public static final int sOffsetY = 11;

	static {
		GuiContainerManager.addInputHandler(new GT_RectHandler());
		GuiContainerManager.addTooltipHandler(new GT_RectHandler());
	}

	protected final Recipe_GT.Gregtech_Recipe_Map mRecipeMap;

	public GT_NEI_MultiBlockHandler(Gregtech_Recipe_Map aRecipeMap) {
		this.mRecipeMap = aRecipeMap;
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 13, 36, 18), getOverlayIdentifier(), new Object[0]));
		if (!NEI_GT_Config.sIsAdded) {
			FMLInterModComms.sendRuntimeMessage(GT_Values.GT, "NEIPlugins", "register-crafting-handler", "gregtech@" + getRecipeName() + "@" + getOverlayIdentifier());
			GuiCraftingRecipe.craftinghandlers.add(this);
			GuiUsageRecipe.usagehandlers.add(this);
		}
	}

	public List<Recipe_GT> getSortedRecipes() {
		List<Recipe_GT> result = new ArrayList<>(this.mRecipeMap.mRecipeList);	        
		//List<Recipe_GT> resultReal = new ArrayList<Recipe_GT>();	        
		/*for (Recipe_GT g : this.mRecipeMap.mRecipeList) {  

	        }	        	     
	        this.mRecipeMap.findRecipe(null, aRecipe, aNotUnificated, aVoltage, aFluids, fluidStacks)*/
		//result.sort(new recipeCompare());
		Collections.sort(result);
		return result;
	}

	public static void drawText(int aX, int aY, String aString, int aColor) {
		Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
	}

	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_MultiBlockHandler(this.mRecipeMap);
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals(getOverlayIdentifier())) {
			for (Recipe_GT tRecipe : getSortedRecipes()) {
				if (!tRecipe.mHidden) {
					this.arecipes.add(new CachedDefaultRecipe(tRecipe));
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack aResult) {
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
		for (Recipe_GT tRecipe : getSortedRecipes()) {
			if (!tRecipe.mHidden) {
				CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
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
		for (Recipe_GT tRecipe : getSortedRecipes()) {
			if (!tRecipe.mHidden) {
				CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
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

	public String getOverlayIdentifier() {
		return this.mRecipeMap.mNEIName;
	}

	public void drawBackground(int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 78);
	}

	public int recipiesPerPage() {
		return 1;
	}

	public String getRecipeName() {
		return GT_LanguageManager.getTranslation(this.mRecipeMap.mUnlocalizedName);
	}

	public String getGuiTexture() {
		//    return "gregtech:textures/gui/" + this.mRecipeMap.mUnlocalizedName + ".png";
		return this.mRecipeMap.mNEIGUIPath;
	}

	public List<String> handleItemTooltip(GuiRecipe gui, ItemStack aStack, List<String> currenttip, int aRecipeIndex) {
		TemplateRecipeHandler.CachedRecipe tObject = (TemplateRecipeHandler.CachedRecipe) this.arecipes.get(aRecipeIndex);
		if ((tObject instanceof CachedDefaultRecipe)) {
			CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
			for (PositionedStack tStack : tRecipe.mOutputs) {
				if (aStack == tStack.item) {
					if ((!(tStack instanceof FixedPositionedStack)) || (((FixedPositionedStack) tStack).mChance <= 0) || (((FixedPositionedStack) tStack).mChance == 10000)) {
						break;
					}
					currenttip.add(trans("150","Chance: ") + ((FixedPositionedStack) tStack).mChance / 100 + "." + (((FixedPositionedStack) tStack).mChance % 100 < 10 ? "0" + ((FixedPositionedStack) tStack).mChance % 100 : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100)) + "%");
					break;
				}
			}
			for (PositionedStack tStack : tRecipe.mInputs) {
				if (aStack == tStack.item) {
					if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) ||
							(tStack.item.stackSize != 0)) {
						break;
					}
					currenttip.add(trans("151","Does not get consumed in the process"));
					break;
				}
			}
		}
		return currenttip;
	}

	public void drawExtras(int aRecipeIndex) {
		int tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
		int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
		//String[] recipeDesc = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.getNeiDesc();
		String[] recipeDesc = null;
		if (recipeDesc == null) {
			if (tEUt != 0) {
				drawText(10, 73, trans("152","Total: ") + ((long)tDuration * tEUt) + " EU", -16777216);
				drawText(10, 83, trans("153","Usage: ") + tEUt + " EU/t", -16777216);
				if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
					drawText(10, 93, trans("154","Voltage: ") + tEUt / this.mRecipeMap.mAmperage + " EU", -16777216);
					drawText(10, 103, trans("155","Amperage: ") + this.mRecipeMap.mAmperage, -16777216);
				} else {
					drawText(10, 93, trans("156","Voltage: unspecified"), -16777216);
					drawText(10, 103, trans("157","Amperage: unspecified"), -16777216);
				}
			}
			if (tDuration > 0) {
				//				drawText(10, 113, trans("158","Time: ") + (tDuration < 20 ? "< 1" : Integer.valueOf(tDuration / 20)) + trans("161"," secs"), -16777216);
				drawText(10, 113, String.format("%s%.2f%s", trans("158","Time: "), 0.05 * tDuration, trans("161"," secs")), -16777216);
			}
			int tSpecial = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
			if ((GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre)) || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost))) {
				drawText(10, 123, this.mRecipeMap.mNEISpecialValuePre + tSpecial * this.mRecipeMap.mNEISpecialValueMultiplier + this.mRecipeMap.mNEISpecialValuePost, -16777216);
			}
		} else {
			int i = 0;
			for (String descLine : recipeDesc) {
				drawText(10, 73 + 10 * i, descLine, -16777216);
				i++;
			}
		}
	}

	public static class GT_RectHandler
	implements IContainerInputHandler, IContainerTooltipHandler {
		public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
			if (canHandle(gui)) {
				if (button == 0) {
					return transferRect(gui, false);
				}
				if (button == 1) {
					return transferRect(gui, true);
				}
			}
			return false;
		}

		public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyCode) {
			return false;
		}

		public boolean canHandle(GuiContainer gui) {
			return (gui instanceof GT_GUIContainer_BasicMachine && GT_Utility.isStringValid(((GT_GUIContainer_BasicMachine) gui).mNEI));
		}

		public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
			if ((canHandle(gui)) && (currenttip.isEmpty())) {
				if (gui instanceof GT_GUIContainer_BasicMachine && new Rectangle(65, 13, 36, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) {
					currenttip.add("Recipes");
				}
			}
			return currenttip;
		}

		private boolean transferRect(GuiContainer gui, boolean usage) {
			if (gui instanceof GT_GUIContainer_BasicMachine) {
				return (canHandle(gui)) && (new Rectangle(65, 13, 36, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) && (usage ? GuiUsageRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]) : GuiCraftingRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]));
			}
			return false;
		}

		public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
			return currenttip;
		}

		public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {
			return currenttip;
		}

		public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
			return false;
		}

		public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
		}

		public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
		}

		public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {
		}

		public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
			return false;
		}

		public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
		}

		public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {
		}
	}

	public static class FixedPositionedStack extends PositionedStack {
		public final int mChance;
		public boolean permutated = false;

		public FixedPositionedStack(Object object, int x, int y) {
			this(object, x, y, 0);
		}

		public FixedPositionedStack(Object object, int x, int y, int aChance) {
			super(getNonUnifiedStacks(object), x, y, true);
			this.mChance = aChance;
		}

		public void generatePermutations() {
			if (this.permutated) {
				return;
			}
			ArrayList<ItemStack> tDisplayStacks = new ArrayList<ItemStack>();
			for (ItemStack tStack : this.items) {
				if (GT_Utility.isStackValid(tStack)) {
					if (tStack.getItemDamage() == 32767) {
						List<ItemStack> permutations = codechicken.nei.ItemList.itemMap.get(tStack.getItem());
						if (!permutations.isEmpty()) {
							ItemStack stack;
							for (Iterator<ItemStack> i$ = permutations.iterator(); i$.hasNext(); tDisplayStacks.add(GT_Utility.copyAmount(tStack.stackSize, new Object[]{stack}))) {
								stack = (ItemStack) i$.next();
							}
						} else {
							ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
							base.stackTagCompound = tStack.stackTagCompound;
							tDisplayStacks.add(base);
						}
					} else {
						tDisplayStacks.add(GT_Utility.copy(new Object[]{tStack}));
					}
				}
			}
			this.items = ((ItemStack[]) tDisplayStacks.toArray(new ItemStack[0]));
			if (this.items.length == 0) {
				this.items = new ItemStack[]{new ItemStack(Blocks.fire)};
			}
			this.permutated = true;
			setPermutationToRender(0);
		}
	}

	public class CachedDefaultRecipe extends TemplateRecipeHandler.CachedRecipe {
		public final Recipe_GT mRecipe;
		public final List<PositionedStack> mOutputs;
		public final List<PositionedStack> mInputs;

		public CachedDefaultRecipe(Recipe_GT aRecipe) {
			super();
			this.mRecipe = aRecipe;

			if (aRecipe.getInputPositionedStacks() != null && aRecipe.getOutputPositionedStacks() != null) {
				mInputs = aRecipe.getInputPositionedStacks();
				mOutputs = aRecipe.getOutputPositionedStacks();
				return;
			}

			mOutputs = new ArrayList<PositionedStack>();
			mInputs = new ArrayList<PositionedStack>();

			int tStartIndex = 0;
			switch (GT_NEI_MultiBlockHandler.this.mRecipeMap.mUsualInputCount) {
				case 0:
					break;
				case 1:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
					}
					tStartIndex++;
					break;
				case 2:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
					}
					tStartIndex++;
					break;
				case 3:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
					}
					tStartIndex++;
					break;
				case 4:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 23));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 23));
					}
					tStartIndex++;
					break;
				case 5:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 23));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 23));
					}
					tStartIndex++;
					break;
				case 6:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 5));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 23));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 23));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 23));
					}
					tStartIndex++;
					break;
				case 7:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 32));
					}
					tStartIndex++;
					break;
				case 8:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 32));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 32));
					}
					tStartIndex++;
					break;
				default:
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, -4));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 32));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 32));
					}
					tStartIndex++;
					if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
						this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 32));
					}
					tStartIndex++;
			}
			if (aRecipe.mSpecialItems != null) {
				this.mInputs.add(new FixedPositionedStack(aRecipe.mSpecialItems, 120, 52));
			}
			tStartIndex = 0;
			switch (GT_NEI_MultiBlockHandler.this.mRecipeMap.mUsualOutputCount) {
				case 0:
					break;
				case 1:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				case 2:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				case 3:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				case 4:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 23, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 23, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				case 5:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 23, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 23, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				case 6:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 5, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 23, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 23, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 23, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				case 7:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 32, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				case 8:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 32, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 32, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					break;
				default:
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, -4, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 32, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 32, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
					if (aRecipe.getOutput(tStartIndex) != null) {
						this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 32, aRecipe.getOutputChance(tStartIndex)));
					}
					tStartIndex++;
			}
			if ((aRecipe.mFluidInputs.length > 0) && (aRecipe.mFluidInputs[0] != null) && (aRecipe.mFluidInputs[0].getFluid() != null)) {
				this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[0], true), 48, 52));
				if ((aRecipe.mFluidInputs.length > 1) && (aRecipe.mFluidInputs[1] != null) && (aRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[1], true), 30, 52));
				}
			}
			if (aRecipe.mFluidOutputs.length > 1) {
				if (aRecipe.mFluidOutputs[0] != null && (aRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[0], true), 120, 5));
				}
				if (aRecipe.mFluidOutputs[1] != null && (aRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[1], true), 138, 5));
				}
				if (aRecipe.mFluidOutputs.length > 2 && aRecipe.mFluidOutputs[2] != null && (aRecipe.mFluidOutputs[2].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[2], true), 102, 23));
				}
				if (aRecipe.mFluidOutputs.length > 3 && aRecipe.mFluidOutputs[3] != null && (aRecipe.mFluidOutputs[3].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[3], true), 120, 23));
				}
				if (aRecipe.mFluidOutputs.length > 4 && aRecipe.mFluidOutputs[4] != null && (aRecipe.mFluidOutputs[4].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[4], true), 138, 23));
				}
			} else if ((aRecipe.mFluidOutputs.length > 0) && (aRecipe.mFluidOutputs[0] != null) && (aRecipe.mFluidOutputs[0].getFluid() != null)) {
				this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[0], true), 102, 52));
			}
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(GT_NEI_MultiBlockHandler.this.cycleticks / 10, this.mInputs);
		}

		public PositionedStack getResult() {
			return null;
		}

		public List<PositionedStack> getOtherStacks() {
			return this.mOutputs;
		}
	} 

	public String trans(String aKey, String aEnglish){
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_"+aKey, aEnglish, false);
	}

	public static Map<String, ItemStack> sUnifTable;
	public static Map<GT_ItemStack, ItemData> sNametoStackTable;
	public static Map<GT_ItemStack, List<ItemStack>> sItemToDataTable;
	public static Field sFields[] = new Field[3];
	public static Method sMethods[] = new Method[2];

	@SuppressWarnings("unchecked")
	private static final void setVars() {
		try {			
			//Set Fields
			for (int u=0;u<3;u++) {
				if (sFields[u] == null) {
					if (u==0) {
						sFields[0] = ReflectionUtils.getField(GT_OreDictUnificator.class, "sUnificationTable");
						if (sFields[0] == null) {
							ReflectionUtils.getField(GregtechOreDictUnificator.class, "sUnificationTable");
						}
					}
					else if (u==1) {
						sFields[1] = ReflectionUtils.getField(GT_OreDictUnificator.class, "sName2StackMap");
					}
					else if (u==2) {
						sFields[2] = ReflectionUtils.getField(GT_OreDictUnificator.class, "sItemStack2DataMap");
					}
				}
			}

			//Set Method
			if (sMethods[0] == null) {
				sMethods[0] = GT_OreDictUnificator.class.getMethod("get", boolean.class, ItemStack.class, boolean.class);
			}

			//Set Local Fields
			sUnifTable = (Map<String, ItemStack>) sFields[0].get(null);
			sNametoStackTable = (Map<GT_ItemStack, ItemData>) sFields[1].get(null);
			sItemToDataTable = (Map<GT_ItemStack, List<ItemStack>>) sFields[2].get(null);

		}
		catch (Throwable T) {

		}
	}

	public static List<ItemStack> getNonUnifiedStacks(Object obj) {   
		setVars();
		if (sUnifTable != null)
			synchronized (sUnifTable) {
				if (sUnifTable.isEmpty() && !sItemToDataTable.isEmpty()) {
					for (GT_ItemStack tGTStack0 : sItemToDataTable.keySet()) {
						ItemStack tStack0 = tGTStack0.toStack();
						ItemStack tStack1 = null;					
						try {
							tStack1 = (ItemStack) sMethods[0].invoke(null, false, tStack0, true);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							GregtechOreDictUnificator.get(false, tStack0, true);
						}

						if (tStack0 != null && tStack1 != null && !GT_Utility.areStacksEqual(tStack0, tStack1)) {
							GT_ItemStack tGTStack1 = new GT_ItemStack(tStack1);
							List<ItemStack> list = sItemToDataTable.get(tGTStack1);
							if (list == null) sItemToDataTable.put(tGTStack1, list = new ArrayList<ItemStack>());
							if (!list.contains(tStack0)) list.add(tStack0);
						}
					}
				}
			}
		ItemStack[] aStacks = {};
		if (obj instanceof ItemStack) aStacks = new ItemStack[]{(ItemStack) obj};
		else if (obj instanceof ItemStack[]) aStacks = (ItemStack[]) obj;
		else if (obj instanceof List) aStacks = (ItemStack[]) ((List)obj).toArray(new ItemStack[0]);
		List<ItemStack> rList = new ArrayList<ItemStack>();
		for (ItemStack aStack : aStacks) {
			rList.add(aStack);
			if (sItemToDataTable != null) {
				List<ItemStack> tList = sItemToDataTable.get(new GT_ItemStack(aStack));
				if (tList != null) {
					for (ItemStack tStack : tList) {
						ItemStack tStack1 = GT_Utility.copyAmount(aStack.stackSize, tStack);
						tStack1.setTagCompound(aStack.getTagCompound());
						rList.add(tStack1);
					}
				}
			}
		}
		return rList;

		//List<ItemStack> x = new ArrayList<ItemStack>();
		//return x;
	}


	public class recipeCompare implements Comparator<GT_Recipe> {
		public int compare(GT_Recipe a, GT_Recipe b) {
			if (a.mEUt != b.mEUt) {
				return a.mEUt - b.mEUt;
			} else if (a.mDuration != b.mDuration) {
				return a.mDuration - b.mDuration;
			} else if (a.mSpecialValue != b.mSpecialValue) {
				return a.mSpecialValue - b.mSpecialValue;
			} else if (a.mFluidInputs.length != b.mFluidInputs.length) {
				return a.mFluidInputs.length - b.mFluidInputs.length;
			} else if (a.mInputs.length != b.mInputs.length) {
				return a.mInputs.length - b.mInputs.length;
			}
			return 0;
		}
	}



}
