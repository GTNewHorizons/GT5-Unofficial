package gregtech.nei.formatter;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidContainerRegistry;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FuelSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static FuelSpecialValueFormatter INSTANCE = new FuelSpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        int liquidAmount = 0;
        GTRecipe recipe = recipeInfo.recipe;

        if (recipe.mFluidInputs.length > 0) {
            // use recipe's fluid amount
            liquidAmount = recipe.mFluidInputs[0].amount;
        } else {
            // try to find & use first liquid cell
            for (ItemStack stack : recipe.mInputs) {
                liquidAmount = FluidContainerRegistry.getContainerCapacity(stack);
                if (liquidAmount > 0) break;
            }
        }
        // fallback to old amount, that wasn't fluid amount aware
        if (liquidAmount == 0) {
            liquidAmount = 1000;
        }

        return Collections.singletonList(
            StatCollector
                .translateToLocalFormatted("GT5U.nei.fuel", formatNumber(recipe.mSpecialValue * liquidAmount)));
    }
}
