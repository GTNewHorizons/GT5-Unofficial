package gtPlusPlus.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GTPP_NEI_DefaultHandler extends TemplateRecipeHandler {
	
	public static final int sOffsetX = 5;
	public static final int sOffsetY = 11;
    private SoftReference<List<CachedDefaultRecipe>> mCachedRecipes = null;
	
	private static final HashMap<Integer, Pair<Integer, Integer>> mInputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();
	private static final HashMap<Integer, Pair<Integer, Integer>> mOutputSlotMap = new HashMap<Integer, Pair<Integer, Integer>>();

	static {
		GuiContainerManager.addInputHandler(new GT_RectHandler());
		GuiContainerManager.addTooltipHandler(new GT_RectHandler());
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

	protected final GT_Recipe_Map mRecipeMap;

	public GTPP_NEI_DefaultHandler(final GT_Recipe_Map tMap) {
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
		return new GTPP_NEI_DefaultHandler(this.mRecipeMap);
	}
	

    public List<GT_Recipe> getSortedRecipes() {
        List<GT_Recipe> result = new ArrayList<>(this.mRecipeMap.mRecipeList);
        Collections.sort(result);
        return result;
    }
    
    public List<CachedDefaultRecipe> getCache() {
        List<CachedDefaultRecipe> cache;
        if (mCachedRecipes == null || (cache = mCachedRecipes.get()) == null) {
            cache = mRecipeMap.mRecipeList.stream()  // do not use parallel stream. This is already parallelized by NEI
                    .filter(r -> !r.mHidden)
                    .sorted()
                    .map(temp -> {return createCachedRecipe(temp);})
                    .collect(Collectors.toList());
            // while the NEI parallelize handlers, for each individual handler it still uses sequential execution model
            // so we do not need any synchronization here
            mCachedRecipes = new SoftReference<>(cache);
        }
        return cache;
    }
    
    public CachedDefaultRecipe createCachedRecipe(GT_Recipe aRecipe) {
    	return new CachedDefaultRecipe(aRecipe);
    }
    
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            arecipes.addAll(getCache());
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadCraftingRecipes(ItemStack aResult) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aResult);

        ArrayList<ItemStack> tResults = new ArrayList<>();
        tResults.add(aResult);
        tResults.add(GT_OreDictUnificator.get(true, aResult));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aResult, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
        }
        else tFluidStack = GT_Utility.getFluidFromDisplayStack(aResult);
        if (tFluidStack != null) {
            tResults.addAll(GT_Utility.getContainersFromFluid(tFluidStack));
        }
        for (CachedDefaultRecipe recipe : getCache()) {
            if (tResults.stream().anyMatch(stack -> recipe.contains(recipe.mOutputs, stack)))
                arecipes.add(recipe);
        }
    }

    public void loadUsageRecipes(ItemStack aInput) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aInput);

        ArrayList<ItemStack> tInputs = new ArrayList<>();
        tInputs.add(aInput);
        tInputs.add(GT_OreDictUnificator.get(false, aInput));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tInputs.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInput, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tInputs.add(GT_Utility.getFluidDisplayStack(tFluid, false));
        }
        else tFluidStack = GT_Utility.getFluidFromDisplayStack(aInput);
        if (tFluidStack != null) {
            tInputs.addAll(GT_Utility.getContainersFromFluid(tFluidStack));
        }
        for (CachedDefaultRecipe recipe : getCache()) {
            if (tInputs.stream().anyMatch(stack -> recipe.contains(recipe.mInputs, stack)))
                arecipes.add(recipe);
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
		final long tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
		final long tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
		if (tEUt != 0) {
			drawText(10, 73, "Total: " + MathUtils.formatNumbers((long) (tDuration * tEUt)) + " EU", -16777216);
			drawText(10, 83, "Usage: " + MathUtils.formatNumbers(tEUt) + " EU/t", -16777216);
			if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
				drawText(10, 93, "Voltage: " + MathUtils.formatNumbers(tEUt / this.mRecipeMap.mAmperage) + " EU", -16777216);
				drawText(10, 103, "Amperage: " + this.mRecipeMap.mAmperage, -16777216);
			} else {
				drawText(10, 93, "Voltage: unspecified", -16777216);
				drawText(10, 103, "Amperage: unspecified", -16777216);
			}
		}
		if (tDuration > 0) {
			drawText(10, 113, "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(Long.valueOf(tDuration / 20))) + " secs", -16777216);
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
			return (((gui instanceof GT_GUIContainer_BasicMachine)) && (GT_Utility.isStringValid(((GT_GUIContainer_BasicMachine) gui).mNEI)));
		}

		@Override
		public List<String> handleTooltip(final GuiContainer gui, final int mousex, final int mousey, final List<String> currenttip) {
			if ((this.canHandle(gui)) && (currenttip.isEmpty())) {
				if ((gui instanceof GT_GUIContainer_BasicMachine) && new Rectangle(65, 13, 36, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) {
					currenttip.add("Recipes");
				}
			}
			return currenttip;
		}

		private boolean transferRect(final GuiContainer gui, final boolean usage) {
			if (gui instanceof GT_GUIContainer_BasicMachine) {
				return (this.canHandle(gui)) && (new Rectangle(65, 13, 36, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) && (usage ? GuiUsageRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]) : GuiCraftingRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]));
			}
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

	public class CachedDefaultRecipe extends TemplateRecipeHandler.CachedRecipe {
		public final GT_Recipe mRecipe;
		public final List<PositionedStack> mOutputs = new ArrayList<PositionedStack>();
		public final List<PositionedStack> mInputs = new ArrayList<PositionedStack>();

		public CachedDefaultRecipe(final GT_Recipe aRecipe) {
			super();
			this.mRecipe = aRecipe;
			handleSlots();						
		}
		
		public void handleSlots() {
			int tStartIndex = 0;
			switch (GTPP_NEI_DefaultHandler.this.mRecipeMap.mUsualInputCount) {
			case 0:
				break;
			case 1:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 14));
				}
				tStartIndex++;
				break;
			case 2:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 14));
				}
				tStartIndex++;
				break;
			case 3:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 14));
				}
				tStartIndex++;
				break;
			case 4:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 23));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 23));
				}
				tStartIndex++;
				break;
			case 5:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 23));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 23));
				}
				tStartIndex++;
				break;
			case 6:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 5));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 23));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 23));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 23));
				}
				tStartIndex++;
				break;
			case 7:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 32));
				}
				tStartIndex++;
				break;
			case 8:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 32));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 32));
				}
				tStartIndex++;
				break;
			default:
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, -4));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 14));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 12, 32));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 30, 32));
				}
				tStartIndex++;
				if (mRecipe.getRepresentativeInput(tStartIndex) != null) {
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(tStartIndex), 48, 32));
				}
				tStartIndex++;
			}
			if (mRecipe.mSpecialItems != null) {
				this.mInputs.add(new FixedPositionedStack(mRecipe.mSpecialItems, 120, 52));
			}
			tStartIndex = 0;
			switch (GTPP_NEI_DefaultHandler.this.mRecipeMap.mUsualOutputCount) {
			case 0:
				break;
			case 1:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			case 2:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			case 3:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			case 4:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 23, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 23, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			case 5:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 23, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 23, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			case 6:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 5, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 23, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 23, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 23, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			case 7:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 32, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			case 8:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 32, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 32, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				break;
			default:
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, -4, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 14, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 102, 32, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 120, 32, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (mRecipe.getOutput(tStartIndex) != null) {
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(tStartIndex), 138, 32, mRecipe.getOutputChance(tStartIndex)));
				}
				tStartIndex++;
			}


            if ((mRecipe.mFluidInputs.length > 0) && (mRecipe.mFluidInputs[0] != null) && (mRecipe.mFluidInputs[0].getFluid() != null)) {
                this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[0], true), 48, 52));
                if ((mRecipe.mFluidInputs.length > 1) && (mRecipe.mFluidInputs[1] != null) && (mRecipe.mFluidInputs[1].getFluid() != null)) {
                    this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[1], true), 30, 52));
                }
            }
            if (mRecipe.mFluidOutputs.length > 1) {
                if (mRecipe.mFluidOutputs[0] != null && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
                    this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 120, 5));
                }
                if (mRecipe.mFluidOutputs[1] != null && (mRecipe.mFluidOutputs[1].getFluid() != null)) {
                    this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[1], true), 138, 5));
                }
                if (mRecipe.mFluidOutputs.length > 2 && mRecipe.mFluidOutputs[2] != null && (mRecipe.mFluidOutputs[2].getFluid() != null)) {
                    this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[2], true), 102, 23));
                }
                if (mRecipe.mFluidOutputs.length > 3 && mRecipe.mFluidOutputs[3] != null && (mRecipe.mFluidOutputs[3].getFluid() != null)) {
                    this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[3], true), 120, 23));
                }
                if (mRecipe.mFluidOutputs.length > 4 && mRecipe.mFluidOutputs[4] != null && (mRecipe.mFluidOutputs[4].getFluid() != null)) {
                    this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[4], true), 138, 23));
                }
            } else if ((mRecipe.mFluidOutputs.length > 0) && (mRecipe.mFluidOutputs[0] != null) && (mRecipe.mFluidOutputs[0].getFluid() != null)) {
                this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[0], true), 102, 52));
            }	
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(GTPP_NEI_DefaultHandler.this.cycleticks / 10, this.mInputs);
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

	public class NoCellMultiDefaultRecipe extends CachedDefaultRecipe {

		public NoCellMultiDefaultRecipe(final GT_Recipe aRecipe) {
			super(aRecipe);
			
		}

		@Override
		public void handleSlots() {

			int aInputItemsCount = this.mRecipe.mInputs.length;
			int aInputFluidsCount = this.mRecipe.mFluidInputs.length;			
			int aOutputItemsCount = this.mRecipe.mOutputs.length;
			int aOutputFluidsCount = this.mRecipe.mFluidOutputs.length;
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
					int x = mInputSlotMap.get(aSlotToCheck).getKey();
					int y = mInputSlotMap.get(aSlotToCheck).getValue();
					this.mInputs.add(new FixedPositionedStack(mRecipe.getRepresentativeInput(aSlotToCheck), x, y));	
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
					this.mOutputs.add(new FixedPositionedStack(mRecipe.getOutput(aSlotToCheck), x, y, mRecipe.getOutputChance(aSlotToCheck)));	
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
					this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidInputs[i], true), x, y));
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
					this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mRecipe.mFluidOutputs[i], true), x, y));
					aSlotToCheck++;
					aOutputSlotsUsed++;
				}
			}
		}
	}
}
