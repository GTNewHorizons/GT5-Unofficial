package goodgenerator.api.recipe;

import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ExtremeHeatExchangerFrontend extends RecipeMapFrontend {

    public ExtremeHeatExchangerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.neiSpecialInfoFormatter(new EHESpecialValueFormatter()));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return Arrays.asList(new Pos2d(26, 13), new Pos2d(26, 37));
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return Arrays.asList(new Pos2d(128, 13), new Pos2d(128, 31), new Pos2d(128, 54));
    }

    private static class EHESpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            FluidStack[] Inputs = recipeInfo.recipe.mFluidInputs;
            FluidStack[] Outputs = recipeInfo.recipe.mFluidOutputs;
            int threshold = recipeInfo.recipe.mSpecialValue;
            return Arrays.asList(
                    StatCollector.translateToLocal("value.extreme_heat_exchanger.0") + " "
                            + GT_Utility.formatNumbers(Inputs[0].amount)
                            + " L/s",
                    StatCollector.translateToLocal("value.extreme_heat_exchanger.1"),
                    GT_Utility.formatNumbers(Outputs[0].amount / 160) + " L/s",
                    StatCollector.translateToLocal("value.extreme_heat_exchanger.2"),
                    GT_Utility.formatNumbers(Outputs[1].amount / 160) + " L/s",
                    StatCollector.translateToLocal("value.extreme_heat_exchanger.4") + " " + threshold + " L/s");
        }
    }
}
