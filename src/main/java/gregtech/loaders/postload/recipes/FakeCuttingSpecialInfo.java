package gregtech.loaders.postload.recipes;

import gregtech.api.util.GTUtility;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakeCuttingSpecialInfo {
    private final Map<FluidStack, Integer> fluidsToDurations = new LinkedHashMap<>();

    public void add(FluidStack fluid, int duration) {
        fluidsToDurations.put(fluid, duration);
    }

    public Integer getDurationForFluid(FluidStack fluid) {
        return fluidsToDurations.entrySet().stream()
            .filter(e -> GTUtility.areFluidsEqual(e.getKey(), fluid))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(-1);
    }
}
