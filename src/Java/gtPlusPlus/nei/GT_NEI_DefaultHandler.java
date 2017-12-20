package gtPlusPlus.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.*;
import codechicken.nei.recipe.*;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_NEI_DefaultHandler
extends TemplateRecipeHandler {
	public static final int sOffsetX = 5;
	public static final int sOffsetY = 11;

	static {
		GuiContainerManager.addInputHandler(new GT_RectHandler());
		GuiContainerManager.addTooltipHandler(new GT_RectHandler());
	}

	protected final CustomRecipeMap mRecipeMap;

	public GT_NEI_DefaultHandler(final CustomRecipeMap tMap) {
		this.mRecipeMap = tMap;
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 13, 36, 18), this.getOverlayIdentifier(), new Object[0]));
		if (!NEI_GT_Config.sIsAdded) {
			FMLInterModComms.sendRuntimeMessage(GT_Values.GT, "NEIPlugins", "register-crafting-handler", "gregtechplusplus@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
			GuiCraftingRecipe.craftinghandlers.add(this);
			GuiUsageRecipe.usagehandlers.add(this);
		}
	}

	public static void drawText(final int aX, final int aY, final String aString, final int aColor) {
		Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new GT_NEI_DefaultHandler(this.mRecipeMap);
	}

	@Override
	public void loadCraftingRecipes(final String outputId, final Object... results) {
		if (outputId.equals(this.getOverlayIdentifier())) {
			for (final GT_Recipe tRecipe : this.mRecipeMap.mRecipeList) {
				if (!tRecipe.mHidden) {
					this.arecipes.add(new CachedDefaultRecipe(tRecipe));
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(final ItemStack aResult) {
		final ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aResult);

		final ArrayList<ItemStack> tResults = new ArrayList<ItemStack>();
		tResults.add(aResult);
		tResults.add(GT_OreDictUnificator.get(true, aResult));
		if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
			for (final OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
				tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
			}
		}
		final FluidStack tFluid = GT_Utility.getFluidForFilledItem(aResult, true);
		if (tFluid != null) {
			tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
			for (final FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
				if (tData.fluid.isFluidEqual(tFluid)) {
					tResults.add(GT_Utility.copy(new Object[]{tData.filledContainer}));
				}
			}
		}
		for (final GT_Recipe tRecipe : this.mRecipeMap.mRecipeList) {
			if (!tRecipe.mHidden) {
				final CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
				for (final ItemStack tStack : tResults) {
					if (tNEIRecipe.contains(tNEIRecipe.mOutputs, tStack)) {
						this.arecipes.add(tNEIRecipe);
						break;
					}
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(final ItemStack aInput) {
		final ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aInput);

		final ArrayList<ItemStack> tInputs = new ArrayList<ItemStack>();
		tInputs.add(aInput);
		tInputs.add(GT_OreDictUnificator.get(false, aInput));
		if ((tPrefixMaterial != null) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
			for (final OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
				tInputs.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
			}
		}
		final FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInput, true);
		if (tFluid != null) {
			tInputs.add(GT_Utility.getFluidDisplayStack(tFluid, false));
			for (final FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
				if (tData.fluid.isFluidEqual(tFluid)) {
					tInputs.add(GT_Utility.copy(new Object[]{tData.filledContainer}));
				}
			}
		}
		for (final GT_Recipe tRecipe : this.mRecipeMap.mRecipeList) {
			if (!tRecipe.mHidden) {
				final CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
				for (final ItemStack tStack : tInputs) {
					if (tNEIRecipe.contains(tNEIRecipe.mInputs, tStack)) {
						this.arecipes.add(tNEIRecipe);
						break;
					}
				}
			}
		}
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
		//    return "gregtech:textures/gui/" + this.mRecipeMap.mUnlocalizedName + ".png";
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
			drawText(10, 73, "Total: " + (tDuration * tEUt) + " EU", -16777216);
			drawText(10, 83, "Usage: " + tEUt + " EU/t", -16777216);
			if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
				drawText(10, 93, "Voltage: " + (tEUt / this.mRecipeMap.mAmperage) + " EU", -16777216);
				drawText(10, 103, "Amperage: " + this.mRecipeMap.mAmperage, -16777216);
			} else {
				drawText(10, 93, "Voltage: unspecified", -16777216);
				drawText(10, 103, "Amperage: unspecified", -16777216);
			}
		}
		if (tDuration > 0) {
			drawText(10, 113, "Time: " + (tDuration < 20 ? "< 1" : Integer.valueOf(tDuration / 20)) + " secs", -16777216);
		}
		if ((GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre)) || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost))) {
			drawText(10, 123, this.mRecipeMap.mNEISpecialValuePre + (((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue * this.mRecipeMap.mNEISpecialValueMultiplier) + this.mRecipeMap.mNEISpecialValuePost, -16777216);
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

	public class CachedDefaultRecipe
	extends TemplateRecipeHandler.CachedRecipe {
		public final GT_Recipe mRecipe;
		public final List<PositionedStack> mOutputs = new ArrayList<PositionedStack>();
		public final List<PositionedStack> mInputs = new ArrayList<PositionedStack>();

		public CachedDefaultRecipe(final GT_Recipe aRecipe) {
			super();
			this.mRecipe = aRecipe;

			int tStartIndex = 0;
			switch (GT_NEI_DefaultHandler.this.mRecipeMap.mUsualInputCount) {
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
			switch (GT_NEI_DefaultHandler.this.mRecipeMap.mUsualOutputCount) {
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


			//New fluid display behaviour when 3 fluid inputs are detected. (Basically a mix of the code below for outputs an the code above for 9 input slots.)
			if (aRecipe.mFluidInputs.length > 2) {
				if ((aRecipe.mFluidInputs[0] != null) && (aRecipe.mFluidInputs[0].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[0], true), 12, 5));
				}
				if ((aRecipe.mFluidInputs[1] != null) && (aRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[1], true), 30, 5));
				}
				if ((aRecipe.mFluidInputs.length > 2) && (aRecipe.mFluidInputs[2] != null) && (aRecipe.mFluidInputs[2].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[2], true), 48, 5));
				}
				if ((aRecipe.mFluidInputs.length > 3) && (aRecipe.mFluidInputs[3] != null) && (aRecipe.mFluidInputs[3].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[3], true), 12, 23));
				}
				if ((aRecipe.mFluidInputs.length > 4) && (aRecipe.mFluidInputs[4] != null) && (aRecipe.mFluidInputs[4].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[4], true), 30, 23));
				}
				if ((aRecipe.mFluidInputs.length > 5) && (aRecipe.mFluidInputs[5] != null) && (aRecipe.mFluidInputs[5].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[5], true), 48, 23));
				}
				if ((aRecipe.mFluidInputs.length > 6) && (aRecipe.mFluidInputs[6] != null) && (aRecipe.mFluidInputs[6].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[6], true), 12, 41));
				}
				if ((aRecipe.mFluidInputs.length > 7) && (aRecipe.mFluidInputs[7] != null) && (aRecipe.mFluidInputs[7].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[7], true), 30, 41));
				}
				if ((aRecipe.mFluidInputs.length > 8) && (aRecipe.mFluidInputs[8] != null) && (aRecipe.mFluidInputs[8].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[8], true), 48, 41));
				}
			}
			//Returns to old behaviour if fluid inputs < 3
			else if ((aRecipe.mFluidInputs.length > 0) && (aRecipe.mFluidInputs[0] != null) && (aRecipe.mFluidInputs[0].getFluid() != null)) {
				this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[0], true), 48, 52));
				if ((aRecipe.mFluidInputs.length == 2) && (aRecipe.mFluidInputs[1] != null) && (aRecipe.mFluidInputs[1].getFluid() != null)) {
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[1], true), 30, 52));
				}
			}

			if (aRecipe.mFluidOutputs.length > 1) {
				if ((aRecipe.mFluidOutputs[0] != null) && (aRecipe.mFluidOutputs[0].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[0], true), 102, 5));
				}
				if ((aRecipe.mFluidOutputs[1] != null) && (aRecipe.mFluidOutputs[1].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[1], true), 120, 5));
				}
				if ((aRecipe.mFluidOutputs.length > 2) && (aRecipe.mFluidOutputs[2] != null) && (aRecipe.mFluidOutputs[2].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[2], true), 138, 5));
				}
				if ((aRecipe.mFluidOutputs.length > 3) && (aRecipe.mFluidOutputs[3] != null) && (aRecipe.mFluidOutputs[3].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[3], true), 102, 23));
				}
				if ((aRecipe.mFluidOutputs.length > 4) && (aRecipe.mFluidOutputs[4] != null) && (aRecipe.mFluidOutputs[4].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[4], true), 120, 23));
				}
				if ((aRecipe.mFluidOutputs.length > 5) && (aRecipe.mFluidOutputs[5] != null) && (aRecipe.mFluidOutputs[5].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[5], true), 138, 23));
				}
				if ((aRecipe.mFluidOutputs.length > 6) && (aRecipe.mFluidOutputs[6] != null) && (aRecipe.mFluidOutputs[6].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[6], true), 102, 41));
				}
				if ((aRecipe.mFluidOutputs.length > 7) && (aRecipe.mFluidOutputs[7] != null) && (aRecipe.mFluidOutputs[7].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[7], true), 120, 41));
				}
				if ((aRecipe.mFluidOutputs.length > 8) && (aRecipe.mFluidOutputs[8] != null) && (aRecipe.mFluidOutputs[8].getFluid() != null)) {
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[8], true), 138, 41));
				}
			} else if ((aRecipe.mFluidOutputs.length > 0) && (aRecipe.mFluidOutputs[0] != null) && (aRecipe.mFluidOutputs[0].getFluid() != null)) {
				this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[0], true), 102, 5));
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(GT_NEI_DefaultHandler.this.cycleticks / 10, this.mInputs);
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
