package tectech.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.util.TTUtility.toExponentForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import appeng.util.ReadableNumberConverter;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.StackInfo;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.ScrollableOutputFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
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
    public static final int maxItemInputs = 1, maxItemOutputs = ITEM_COLUMNS * itemRows, maxFluidInputs = 2,
        maxFluidOutputs = FLUID_COLUMNS * fluidRows;

    private static final int INPUT_X = centeredX(18 * (maxItemInputs + maxFluidInputs));

    public EyeOfHarmonyFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder,
            neiPropertiesBuilder.neiSpecialInfoFormatter(new EyeOfHarmonySpecialValueFormatter()),
            itemRows,
            fluidRows);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, INPUT_X, ORIGIN_Y, 1, 1);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        // The recipe also has raw star matter, which is not shown
        return UIHelper.getGridPositions(
            Math.min(fluidInputCount, maxFluidInputs),
            INPUT_X + 18 * maxItemInputs,
            ORIGIN_Y,
            maxFluidInputs,
            1);
    }

    private static boolean isGas(ItemStack display, Materials gas) {
        FluidStack fluid = StackInfo.getFluid(display);
        return fluid != null && fluid.isFluidEqual(gas.getGas(1));
    }

    private static long getGasAmount(EyeOfHarmonyRecipe recipe, ItemStack display) {
        if (isGas(display, Materials.Hydrogen)) return recipe.getHydrogenRequirement();
        if (isGas(display, Materials.Helium)) return recipe.getHeliumRequirement();
        return -1;
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

        long gasAmount = getGasAmount(currentRecipe, stack);
        if (gasAmount >= 0) {
            currentTip.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    isGas(stack, Materials.Hydrogen) ? "EOH.Recipe.Hydrogen.In" : "EOH.Recipe.Helium.In",
                    formatNumber(gasAmount)));
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
            // The amounts are drawn as text
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                fixed.customBadge = Collections.emptyList();
            }
        }
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        final EyeOfHarmonyRecipe EOHRecipe = (EyeOfHarmonyRecipe) neiCachedRecipe.mRecipe.mSpecialItems;

        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                long gasAmount = getGasAmount(EOHRecipe, stack.item);
                if (gasAmount >= 0) {
                    drawNEIOverlayText(
                        ReadableNumberConverter.INSTANCE.toWideReadableForm(gasAmount) + "L",
                        stack,
                        0xffffff,
                        0.5f,
                        true,
                        Alignment.BottomRight);
                }
            }
        }

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
