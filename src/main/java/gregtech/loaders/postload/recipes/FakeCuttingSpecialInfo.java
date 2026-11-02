package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.util.GTUtility;
import gregtech.nei.RecipeDisplayInfo;

public class FakeCuttingSpecialInfo {

    private final Map<FluidStack, Integer> fluidsToDurations = new LinkedHashMap<>();

    public void add(FluidStack fluid, int duration) {
        fluidsToDurations.put(fluid, duration);
    }

    public Integer getDurationForFluid(FluidStack fluid) {
        return fluidsToDurations.entrySet()
            .stream()
            .filter(e -> GTUtility.areFluidsEqual(e.getKey(), fluid))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(-1);
    }

    public String getDurationsString(RecipeDisplayInfo recipeInfo) {
        double overclockFactor = (double) recipeInfo.calculator.getDuration() / recipeInfo.recipe.mDuration;

        return fluidsToDurations.values()
            .stream()
            .map(baseDuration -> (baseDuration * overclockFactor) / SECONDS)
            .map(NumberFormatUtil::formatNumber)
            .collect(Collectors.joining("/"));
    }
}
