package gregtech.api.util.recipe;

import org.jetbrains.annotations.NotNull;

public class SolarFactoryRecipeData implements Comparable<SolarFactoryRecipeData> {

    public final int minimumWaferTier;
    public final int minimumWaferCount;
    public final int tierRequired;

    public SolarFactoryRecipeData(int minimumWaferTier, int minimumWaferCount) {
        this.minimumWaferTier = minimumWaferTier;
        this.minimumWaferCount = minimumWaferCount;
        this.tierRequired = 0;
    }

    public SolarFactoryRecipeData(int minimumWaferTier, int minimumWaferCount, int tierRequired) {
        this.minimumWaferTier = minimumWaferTier;
        this.minimumWaferCount = minimumWaferCount;
        this.tierRequired = tierRequired;
    }

    @Override
    public int compareTo(@NotNull SolarFactoryRecipeData o) {
        return 0;
    }
}
