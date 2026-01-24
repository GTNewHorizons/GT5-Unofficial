package tectech.recipe;

import static com.google.common.math.LongMath.pow;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static java.lang.Math.min;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.util.CommonValues.EOH_TIER_FANCY_NAMES;
import static tectech.util.TTUtility.toExponentForm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import appeng.util.ReadableNumberConverter;
import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;
import tectech.loader.ConfigHandler;

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
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        EyeOfHarmonyRecipe EOHRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                if (stack.item.isItemEqual(EOHRecipe.getRecipeTriggerItem())) {
                    drawNEIOverlayText(translateToLocal("NC"), stack);
                }
            }
        }
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
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
                    EOH_TIER_FANCY_NAMES[(int) recipe.getSpacetimeCasingTierRequired()]));

            // Energy Output
            switch (ConfigHandler.visual.EOH_NOTATION) {
                case Numerical -> result.add(
                    StatCollector.translateToLocalFormatted("EOH.Recipe.EU.Out", formatNumber(recipe.getEUOutput())));
                case Scientific -> result.add(
                    StatCollector.translateToLocalFormatted("EOH.Recipe.EU.Out", toExponentForm(recipe.getEUOutput())));
                case SI -> result.add(
                    StatCollector.translateToLocalFormatted(
                        "EOH.Recipe.EU.Out",
                        ReadableNumberConverter.INSTANCE.toWideReadableForm(recipe.getEUOutput())));
            }

            // Energy Input
            switch (ConfigHandler.visual.EOH_NOTATION) {
                case Numerical -> result.add(
                    StatCollector
                        .translateToLocalFormatted("EOH.Recipe.EU.In", formatNumber(recipe.getEUStartCost())));
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

            if (recipe.getOutputItems()
                .size() > maxItemsToRender) {
                result.add(StatCollector.translateToLocal("EOH.Recipe.Warning"));
            }

            return result;
        }
    }
}
