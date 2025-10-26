package gregtech.api.recipe.metadata;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Tier of advanced compression required
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CompressionTierKey extends RecipeMetadataKey<Integer> {

    public static final CompressionTierKey INSTANCE = new CompressionTierKey();

    private CompressionTierKey() {
        super(Integer.class, "compression_tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        switch (tier) {
            case 1 -> recipeInfo.drawText(GTUtility.translate("gt.recipe.compression_require_hip"));
            case 2 -> recipeInfo.drawText(GTUtility.translate("gt.recipe.compression_require_stabilized_black_hole"));
        }
    }
}
