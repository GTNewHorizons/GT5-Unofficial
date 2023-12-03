package com.github.technus.tectech.recipe;

import static com.github.technus.tectech.util.CommonValues.EOH_TIER_FANCY_NAMES;
import static com.google.common.math.LongMath.pow;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static java.lang.Math.min;
import static net.minecraft.util.EnumChatFormatting.BOLD;
import static net.minecraft.util.EnumChatFormatting.DARK_RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import appeng.util.ReadableNumberConverter;
import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EyeOfHarmonyFrontend extends RecipeMapFrontend {

    private static final int xDirMaxCount = 9;
    private static final int itemRows = 9, fluidRows = 2;
    public static final int maxItemInputs = 1, maxItemOutputs = xDirMaxCount * itemRows, maxFluidInputs = 0,
            maxFluidOutputs = xDirMaxCount * fluidRows;
    private static final int yOrigin = 8;
    private static final long TRILLION = pow(10, 12);

    public EyeOfHarmonyFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
                uiPropertiesBuilder.logoPos(new Pos2d(8, yOrigin)),
                neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 117 + (itemRows + fluidRows - 4) * 18))
                        .neiSpecialInfoFormatter(new EyeOfHarmonySpecialValueFormatter()));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 79, yOrigin, 1, 1);
    }

    public static final int maxItemsToRender = 80;

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(min(itemOutputCount, maxItemsToRender + 1), 7, yOrigin + 36, xDirMaxCount, 12);
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
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
            GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
        EyeOfHarmonyRecipe currentRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;

        // Draw tooltip on planet item.
        if (stack.isItemEqual(currentRecipe.getRecipeTriggerItem())) {
            currentTip.add(
                    EnumChatFormatting.GRAY + translateToLocalFormatted(
                            "tt.nei.eoh.total_items",
                            formatNumbers(currentRecipe.getSumOfItems())));
            return currentTip;
        }

        // Draw tooltip on other items.
        double percentage = currentRecipe.getItemStackToProbabilityMap().getOrDefault(stack, -1.0);

        if (percentage != -1.0) {
            currentTip.add(EnumChatFormatting.GRAY + translateToLocalFormatted("tt.nei.eoh.solid_mass", percentage));
            currentTip.add(
                    EnumChatFormatting.GRAY + translateToLocalFormatted(
                            "tt.nei.eoh.item_count",
                            formatNumbers(currentRecipe.getItemStackToTrueStackSizeMap().get(stack))));
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

    private static class EyeOfHarmonySpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
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
                    GT_LanguageManager.addStringLocalization("EOH.Recipe.BaseRecipeChance", "Base Recipe Chance") + ": "
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
        }
    }
}
