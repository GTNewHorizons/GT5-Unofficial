package gregtech.api.recipe.metadata;

import static gregtech.api.util.GTUtility.trans;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Wafer tier required for Solar Factory recipes
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SolarFactoryKey extends RecipeMetadataKey<Integer> {

    public static final SolarFactoryKey INSTANCE = new SolarFactoryKey();

    private SolarFactoryKey() {
        super(Integer.class, "solar_factory_wafer_tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 0);
        if (tier > 0) {
            recipeInfo.drawText(trans("510", "Minimum wafer tier: ") + tier);
        }
    }
}
