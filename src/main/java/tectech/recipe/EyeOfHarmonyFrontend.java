package tectech.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.util.TTUtility.toExponentForm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Alignment;

import appeng.util.ReadableNumberConverter;
import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.ScrollableOutputFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.NEIScrollArea;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;
import tectech.loader.ConfigHandler;
import tectech.util.CommonValues;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EyeOfHarmonyFrontend extends ScrollableOutputFrontend {

    private static final int itemRows = 8, fluidRows = 3;
    public static final int maxItemInputs = 1, maxItemOutputs = ITEM_COLUMNS * itemRows, maxFluidInputs = 0,
        maxFluidOutputs = FLUID_COLUMNS * fluidRows;

    public EyeOfHarmonyFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder,
            neiPropertiesBuilder.neiSpecialInfoFormatter(new EyeOfHarmonySpecialValueFormatter()),
            itemRows,
            fluidRows);
    }

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
        EyeOfHarmonyRecipe currentRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;

        // Draw tooltip on planet item.
        if (stack.isItemEqual(currentRecipe.getRecipeTriggerItem())) {
            currentTip.add(
                EnumChatFormatting.GRAY
                    + translateToLocalFormatted("tt.nei.eoh.total_items", formatNumber(currentRecipe.getSumOfItems())));
            return currentTip;
        }

        // Draw tooltip on other items.
        double percentage = currentRecipe.getItemStackToProbabilityMap()
            .getOrDefault(stack, -1.0);

        if (percentage != -1.0) {
            currentTip.add(EnumChatFormatting.GRAY + translateToLocalFormatted("tt.nei.eoh.solid_mass", percentage));
            currentTip.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "tt.nei.eoh.item_count",
                    formatNumber(
                        currentRecipe.getItemStackToTrueStackSizeMap()
                            .get(stack))));
        }

        return currentTip;
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.prepareRecipe(neiCachedRecipe);
        final EyeOfHarmonyRecipe EOHRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;

        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed
                && stack.item.isItemEqual(EOHRecipe.getRecipeTriggerItem())) {
                fixed.setMaxSize(0);
            }
        }
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        final EyeOfHarmonyRecipe EOHRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;

        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                if (!NEIScrollArea.isShown(stack)) continue;
                if (EOHRecipe.getItemStackToTrueStackSizeMap()
                    .containsKey(stack.item)) {
                    long stackSize = EOHRecipe.getItemStackToTrueStackSizeMap()
                        .get(stack.item);
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
                StatCollector.translateToLocalFormatted(
                    "EOH.Recipe.Hydrogen.In",
                    formatNumber(recipe.getHydrogenRequirement())));
            result.add(
                StatCollector
                    .translateToLocalFormatted("EOH.Recipe.Helium.In", formatNumber(recipe.getHydrogenRequirement())));
            result.add(
                StatCollector.translateToLocalFormatted(
                    "EOH.Recipe.SpacetimeTier",
                    CommonValues.getLocalizedEohTierFancyNames((int) recipe.getSpacetimeCasingTierRequired())));

            // Energy Output
            switch (ConfigHandler.visual.EOH_NOTATION) {
                case Numerical -> result
                    .add(StatCollector.translateToLocalFormatted("EOH.Recipe.EU.Out", recipe.getEUOutput()));
                case Scientific -> result.add(
                    StatCollector.translateToLocalFormatted("EOH.Recipe.EU.Out", toExponentForm(recipe.getEUOutput())));
                case SI -> result.add(
                    StatCollector.translateToLocalFormatted(
                        "EOH.Recipe.EU.Out",
                        ReadableNumberConverter.INSTANCE.toWideReadableForm(recipe.getEUOutput())));
            }

            // Energy Input
            switch (ConfigHandler.visual.EOH_NOTATION) {
                case Numerical -> result
                    .add(StatCollector.translateToLocalFormatted("EOH.Recipe.EU.In", recipe.getEUStartCost()));
                case Scientific -> result.add(
                    StatCollector
                        .translateToLocalFormatted("EOH.Recipe.EU.In", toExponentForm(recipe.getEUStartCost())));
                case SI -> result.add(
                    StatCollector.translateToLocalFormatted(
                        "EOH.Recipe.EU.In",
                        ReadableNumberConverter.INSTANCE.toWideReadableForm(recipe.getEUStartCost())));
            }

            result.add(
                StatCollector.translateToLocalFormatted(
                    "EOH.Recipe.BaseRecipeChance",
                    formatNumber(100 * recipe.getBaseRecipeSuccessChance())));
            result.add(
                StatCollector.translateToLocalFormatted(
                    "EOH.Recipe.RecipeEnergyEfficiency",
                    formatNumber(100 * recipe.getRecipeEnergyEfficiency())));

            return result;
        }
    }
}
