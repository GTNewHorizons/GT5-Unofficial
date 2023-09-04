package com.github.technus.tectech.recipe;

import static com.github.technus.tectech.util.CommonValues.EOH_TIER_FANCY_NAMES;
import static com.google.common.math.LongMath.pow;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static java.lang.Math.min;
import static net.minecraft.util.EnumChatFormatting.*;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.*;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import appeng.util.ReadableNumberConverter;
import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.NEIRecipeInfo;

@SuppressWarnings("SpellCheckingInspection")
public class TT_recipe extends GT_Recipe {

    public static final String E_RECIPE_ID = "eRecipeID";

    public TT_recipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
            FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        super(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecialItems,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue);
    }

    public static class TT_Recipe_Map<T extends GT_Recipe> {

        public static TT_Recipe_Map<TT_assLineRecipe> sCrafterRecipes = new TT_Recipe_Map<>();
        public static TT_Recipe_Map<TT_assLineRecipe> sMachineRecipes = new TT_Recipe_Map<>();

        private final HashMap<String, T> mRecipeMap;

        public TT_Recipe_Map() {
            mRecipeMap = new HashMap<>(16);
        }

        public T findRecipe(String identifier) {
            return mRecipeMap.get(identifier);
        }

        public T findRecipe(ItemStack dataHandler) {
            if (dataHandler == null || dataHandler.stackTagCompound == null) {
                return null;
            }
            return mRecipeMap.get(dataHandler.stackTagCompound.getString(E_RECIPE_ID));
        }

        public void add(T recipe) {
            GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(recipe.mOutputs[0].getItem());
            mRecipeMap.put(uid + ":" + recipe.mOutputs[0].getItemDamage(), recipe);
        }

        public Collection<T> recipeList() {
            return mRecipeMap.values();
        }
    }

    public static class GT_Recipe_MapTT extends GT_Recipe.GT_Recipe_Map {

        public static final GT_Recipe_Map sEyeofHarmonyRecipes = new Eye_Of_Harmony_Recipe_Map(
                new HashSet<>(250),
                "gt.recipe.eyeofharmony",
                "Eye of Harmony",
                null,
                "gregtech:textures/gui/basicmachines/Extractor",
                1,
                9 * 9,
                1,
                0,
                1,
                "",
                1,
                "",
                true,
                false) // Custom NEI handler means this must be false.
                        .setProgressBar(GT_UITextures.PROGRESSBAR_HAMMER, ProgressBar.Direction.DOWN)
                        .setProgressBarPos(78, 24 + 2).setUsualFluidOutputCount(18).setLogoPos(10, 10);

        public static GT_Recipe_MapTT sResearchableFakeRecipes = new GT_Recipe_MapTT(
                new HashSet<>(32),
                "gt.recipe.researchStation",
                "Research station",
                null,
                "gregtech:textures/gui/multimachines/ResearchFake",
                1,
                1,
                1,
                0,
                1,
                "",
                1,
                "",
                true,
                false); // nei to false - using custom handler
        public static GT_Recipe_MapTT sScannableFakeRecipes = new GT_Recipe_MapTT(
                new HashSet<>(32),
                "gt.recipe.em_scanner",
                "EM Scanner Research",
                null,
                "gregtech:textures/gui/multimachines/ResearchFake",
                1,
                1,
                1,
                0,
                1,
                "",
                1,
                "",
                true,
                false);
        public static ArrayList<GT_Recipe_AssemblyLine> sAssemblylineRecipes = new ArrayList<>();

        public GT_Recipe_MapTT(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }
    }

    public static class TT_assLineRecipe extends GT_Recipe {

        public final ItemStack mResearchItem;

        public TT_assLineRecipe(boolean aOptimize, ItemStack researchItem, ItemStack[] aInputs, ItemStack[] aOutputs,
                Object aSpecialItems, FluidStack[] aFluidInputs, int aDuration, int aEUt, int aSpecialValue) {
            super(
                    aOptimize,
                    aInputs,
                    aOutputs,
                    aSpecialItems,
                    null,
                    aFluidInputs,
                    null,
                    aDuration,
                    aEUt,
                    aSpecialValue);
            mResearchItem = researchItem;
        }

    }

    public static class Eye_Of_Harmony_Recipe_Map extends GT_Recipe_Map {

        private static final int xDirMaxCount = 9;
        private static final int yOrigin = 8;
        private static final long TRILLION = pow(10, 12);

        public Eye_Of_Harmony_Recipe_Map(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
            useModularUI(true);
            setLogoPos(8, yOrigin);
            setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                EyeOfHarmonyRecipe recipe = (EyeOfHarmonyRecipe) recipeInfo.recipe.mSpecialItems;
                List<String> result = new ArrayList<>();

                result.add(
                        GT_LanguageManager.addStringLocalization("EOH.Recipe.Hydrogen.In", "Hydrogen") + ": "
                                + formatNumbers(recipe.getHydrogenRequirement())
                                + " L");
                result.add(
                        GT_LanguageManager.addStringLocalization("EOH.Recipe.Helium.In", "Helium") + ": "
                                + formatNumbers(recipe.getHydrogenRequirement())
                                + " L");
                result.add(
                        GT_LanguageManager.addStringLocalization("EOH.Recipe.SpacetimeTier", "Spacetime Tier") + ": "
                                + EOH_TIER_FANCY_NAMES[(int) recipe.getSpacetimeCasingTierRequired()]);

                if (recipe.getEUOutput() < TRILLION) {
                    result.add(
                            GT_LanguageManager.addStringLocalization("EOH.Recipe.EU.Out", "EU Output") + ": "
                                    + formatNumbers(recipe.getEUOutput())
                                    + " EU");
                } else {
                    result.add(
                            GT_LanguageManager.addStringLocalization("EOH.Recipe.EU.Out", "EU Output") + ": "
                                    + ReadableNumberConverter.INSTANCE.toWideReadableForm(recipe.getEUOutput())
                                    + " EU");
                }

                if (recipe.getEUOutput() < TRILLION) {
                    result.add(
                            GT_LanguageManager.addStringLocalization("EOH.Recipe.EU.In", "EU Input") + ": "
                                    + formatNumbers(recipe.getEUStartCost())
                                    + " EU");
                } else {
                    result.add(
                            GT_LanguageManager.addStringLocalization("EOH.Recipe.EU.In", "EU Input") + ": "
                                    + ReadableNumberConverter.INSTANCE.toWideReadableForm(recipe.getEUStartCost())
                                    + " EU");
                }

                result.add(
                        GT_LanguageManager.addStringLocalization("EOH.Recipe.BaseRecipeChance", "Base Recipe Chance")
                                + ": "
                                + formatNumbers(100 * recipe.getBaseRecipeSuccessChance())
                                + "%");
                result.add(
                        GT_LanguageManager
                                .addStringLocalization("EOH.Recipe.RecipeEnergyEfficiency", "Recipe Energy Efficiency")
                                + ": "
                                + formatNumbers(100 * recipe.getRecipeEnergyEfficiency())
                                + "%");

                if (recipe.getOutputItems().size() > maxItemsToRender) {
                    result.add(
                            "" + DARK_RED
                                    + BOLD
                                    + GT_LanguageManager.addStringLocalization("EOH.Recipe.Warning.0", "Warning")
                                    + RESET
                                    + ": "
                                    + GT_LanguageManager
                                            .addStringLocalization("EOH.Recipe.Warning.1", "Not all items displayed."));
                }

                return result;
            });
        }

        @Override
        public boolean usesSpecialSlot() {
            return false;
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getGridPositions(itemInputCount, 79, yOrigin, 1, 1);
        }

        public static final int maxItemsToRender = 80;

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return UIHelper
                    .getGridPositions(min(itemOutputCount, maxItemsToRender + 1), 7, yOrigin + 36, xDirMaxCount, 12);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 0, 0, 0, 0);
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return UIHelper.getGridPositions(fluidOutputCount, 7, yOrigin + 13 * 17 - 7 - 16, xDirMaxCount, 3);
        }

        @Override
        public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
                IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
                IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
                Supplier<Float> progressSupplier, Pos2d windowOffset) {
            // Delay setter so that calls to #setUsualFluidInputCount and #setUsualFluidOutputCount are considered
            setNEIBackgroundSize(172, 117 + (Math.max(getItemRowCount() + getFluidRowCount() - 4, 0)) * 18);
            return super.createNEITemplate(
                    itemInputsInventory,
                    itemOutputsInventory,
                    specialSlotInventory,
                    fluidInputsInventory,
                    fluidOutputsInventory,
                    progressSupplier,
                    windowOffset);
        }

        private int getItemRowCount() {
            return (Math.max(mUsualInputCount, mUsualOutputCount) - 1) / xDirMaxCount + 1;
        }

        private int getFluidRowCount() {
            return (Math.max(getUsualFluidInputCount(), getUsualFluidOutputCount()) - 1) / xDirMaxCount + 1;
        }

        @Override
        protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int yShift) {
            drawNEIText(recipeInfo, text, 7, yShift);
        }

        @Override
        public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
                GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
            super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
            if (stack == null) return currentTip;

            EyeOfHarmonyRecipe currentRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;

            // Draw tooltip on planet item.
            if (stack.isItemEqual(currentRecipe.getRecipeTriggerItem())) {
                currentTip.add(
                        EnumChatFormatting.GRAY + translateToLocal("Total Items: ")
                                + formatNumbers(currentRecipe.getSumOfItems()));
                return currentTip;
            }

            // Draw tooltip on other items.
            double percentage = currentRecipe.getItemStackToProbabilityMap().getOrDefault(stack, -1.0);

            if (percentage != -1.0) {
                currentTip.add(
                        EnumChatFormatting.GRAY + translateToLocal("Percentage of Solid Mass: ") + percentage + "%");
                currentTip.add(
                        EnumChatFormatting.GRAY + translateToLocal("Item Count: ")
                                + formatNumbers(currentRecipe.getItemStackToTrueStackSizeMap().get(stack)));
            }

            return currentTip;
        }

        @Override
        public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
            EyeOfHarmonyRecipe EOHRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;
            for (PositionedStack stack : neiCachedRecipe.mInputs) {
                if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    if (stack.item.isItemEqual(EOHRecipe.getRecipeTriggerItem())) {
                        drawNEIOverlayText(translateToLocal("NC"), stack);
                    }
                }
            }
            for (PositionedStack stack : neiCachedRecipe.mOutputs) {
                if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    if (EOHRecipe.getItemStackToTrueStackSizeMap().containsKey(stack.item)) {
                        long stackSize = EOHRecipe.getItemStackToTrueStackSizeMap().get(stack.item);
                        String displayString;
                        if (stackSize > 9999) {
                            displayString = ReadableNumberConverter.INSTANCE.toWideReadableForm(stackSize);
                        } else {
                            displayString = String.valueOf(stackSize);
                        }

                        drawNEIOverlayText(displayString, stack, 0xffffff, 0.5f, true, Alignment.BottomRight);
                    }
                }
            }
        }
    }
}
