package gregtech.api.util.recipe;

public class SolarFactoryRecipeData {

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
}
