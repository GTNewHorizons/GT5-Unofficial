package gregtech.nei.formatter;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidContainerRegistry;

import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FuelSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static FuelSpecialValueFormatter INSTANCE = new FuelSpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        return Collections.singletonList(
            StatCollector.translateToLocalFormatted(
                "GT5U.nei.fuel",
                formatNumber(
                    recipeInfo.recipe.mSpecialValue
                        * FluidContainerRegistry.getContainerCapacity(recipeInfo.recipe.mInputs[0]))));
    }
}
