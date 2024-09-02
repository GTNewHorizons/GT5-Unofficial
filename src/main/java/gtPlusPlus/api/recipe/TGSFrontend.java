package gtPlusPlus.api.recipe;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTETreeFarm;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTETreeFarm.Mode;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TGSFrontend extends RecipeMapFrontend {

    private static final int SLOT_SIZE = 18;
    private static final int CENTER_X = 90;
    private static final int SPECIAL_X = CENTER_X - SLOT_SIZE / 2;
    private static final int SPECIAL_Y = 9;
    private static final int INPUTS_X = CENTER_X - SLOT_SIZE * 3;
    private static final int INPUTS_Y = SPECIAL_Y + SLOT_SIZE + SLOT_SIZE / 2;
    private static final int OUTPUTS_X = CENTER_X + SLOT_SIZE;
    private static final int OUTPUTS_Y = INPUTS_Y;

    public TGSFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder, NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder
                .addNEITransferRect(
                    new Rectangle(INPUTS_X + SLOT_SIZE * 2, INPUTS_Y + SLOT_SIZE / 2, SLOT_SIZE * 2, SLOT_SIZE))
                .progressBarPos(new Pos2d(CENTER_X - 10, INPUTS_Y + SLOT_SIZE / 2)),
            neiPropertiesBuilder.neiSpecialInfoFormatter(new TGSSpecialValueFormatter()));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        // Do not.
    }

    @Override
    public Pos2d getSpecialItemPosition() {
        return new Pos2d(SPECIAL_X, SPECIAL_Y);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(Mode.values().length, INPUTS_X, INPUTS_Y, 2);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(Mode.values().length, OUTPUTS_X, OUTPUTS_Y, 2);
    }

    private static final String[] tooltipInputs = { StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.saw"),
        StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.cutter"),
        StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.shears"),
        StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.knife") };

    private static final String[] tooltipOutputs = { StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.needsSaw"),
        StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.needsCutter"),
        StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.needsShears"),
        StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.needsKnife") };
    private static final String tooltipSapling = StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.sapling");
    private static final String tooltipMultiplier = StatCollector.translateToLocal("gtpp.nei.tgs.tooltip.multiplier");

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {

        /*
         * This gets a little complicated, because we want to assign tooltips to inputs/outputs based on which mode
         * (saw, shears, etc.) they correspond to. But CachedDefaultRecipe does not retain this information for us. This
         * is because some recipes don't output any items for some modes. For example, if a recipe only yields logs and
         * leaves, then the outputs of GTRecipe will be {log, null, leaves}. However, in CachedDefaultRecipe this gets
         * condensed to just {log, leaves}, with null values omitted. So to figure out which item came from which mode,
         * we need to step through both of these arrays simultaneously and match non-null inputs/outputs in GTRecipe to
         * inputs/outputs in CachedDefaultRecipe.
         */

        // The last input in neiCachedRecipe is always the special slot, this is the input sapling.
        if (stack == neiCachedRecipe.mInputs.get(neiCachedRecipe.mInputs.size() - 1).item) {
            currentTip.add(EnumChatFormatting.YELLOW + tooltipSapling);
            super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
            return currentTip;
        }

        GTRecipe.GTRecipe_WithAlt recipe = (GTRecipe.GTRecipe_WithAlt) neiCachedRecipe.mRecipe;

        // Inputs
        int slot = 0;
        for (int mode = 0; mode < Mode.values().length; ++mode) {
            if (mode < recipe.mOreDictAlt.length && recipe.mOreDictAlt[mode] != null) {
                // There is a valid input in this mode.
                if (slot < neiCachedRecipe.mInputs.size() && stack == neiCachedRecipe.mInputs.get(slot).item) {
                    int toolMultiplier = MTETreeFarm.getToolMultiplier(stack, Mode.values()[mode]);
                    currentTip.add(EnumChatFormatting.YELLOW + tooltipInputs[mode]);
                    if (toolMultiplier > 0) {
                        currentTip.add(EnumChatFormatting.YELLOW + tooltipMultiplier + " " + toolMultiplier + "x");
                    }
                    return currentTip;
                }
                ++slot;
            }
        }

        // Outputs
        slot = 0;
        for (int mode = 0; mode < Mode.values().length; ++mode) {
            if (mode < recipe.mOutputs.length && recipe.mOutputs[mode] != null) {
                // There is a valid output in this mode.
                if (slot < neiCachedRecipe.mOutputs.size() && stack == neiCachedRecipe.mOutputs.get(slot).item) {
                    currentTip.add(EnumChatFormatting.YELLOW + tooltipOutputs[mode]);
                    return currentTip;
                }
                ++slot;
            }
        }

        return currentTip;
    }

    private static class TGSSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            return Arrays.asList(
                StatCollector.translateToLocal("gtpp.nei.tgs.info-1"),
                StatCollector.translateToLocal("gtpp.nei.tgs.info-2"),
                StatCollector.translateToLocal("gtpp.nei.tgs.info-3"));
        }
    }
}
