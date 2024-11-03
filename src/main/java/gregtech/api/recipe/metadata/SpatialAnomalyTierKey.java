package gregtech.api.recipe.metadata;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static gregtech.api.util.GTUtility.trans;

/**
 * Tier of Anomaly Types required
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpatialAnomalyTierKey extends RecipeMetadataKey<Integer> {

    public static final SpatialAnomalyTierKey INSTANCE = new SpatialAnomalyTierKey();

    private SpatialAnomalyTierKey() {
        super(Integer.class, "anomaly_type");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        switch (tier) {
            case 1 -> recipeInfo.drawText(trans("600", "Requires Spatial Anomaly"));
            case 2 -> recipeInfo.drawText(trans("601", "Requires Fractal Rift"));
            case 3 -> recipeInfo.drawText(trans("602", "Requires Abnormality Field"));
        }
    }
}
