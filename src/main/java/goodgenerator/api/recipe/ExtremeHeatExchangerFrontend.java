package goodgenerator.api.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
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
            int threshold = recipeInfo.recipe.mSpecialValue;
            String[] description = new String[4];
            description[0] = StatCollector.translateToLocal("value.extreme_heat_exchanger.0") + " "
                + formatNumber(Inputs[0].amount)
                + " L/s";
            description[1] = StatCollector.translateToLocal("value.extreme_heat_exchanger.1");
            description[2] = formatNumber(Inputs[1].amount) + " L/s";
            if (!Inputs[0].getUnlocalizedName()
                .contains("plasma")) {
                description[3] = StatCollector.translateToLocal("value.extreme_heat_exchanger.4") + " "
                    + threshold
                    + " L/s";

            }
            return Arrays.asList(description);
        }
    }
}
