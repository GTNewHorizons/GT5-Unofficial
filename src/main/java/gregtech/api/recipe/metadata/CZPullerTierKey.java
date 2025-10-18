package gregtech.api.recipe.metadata;

import static gregtech.api.util.GTUtility.trans;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Minimum tier required for the CZ Puller recipe.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CZPullerTierKey extends RecipeMetadataKey<Integer> {

    public static final CZPullerTierKey INSTANCE = new CZPullerTierKey();

    private CZPullerTierKey() {
        super(Integer.class, "CZ_Puller_Tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        recipeInfo.drawText(trans("511", "CZ Puller Tier: ") + tier);
    }
}
