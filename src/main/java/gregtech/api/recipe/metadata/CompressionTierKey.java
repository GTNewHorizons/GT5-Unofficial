package gregtech.api.recipe.metadata;

import static gregtech.api.util.GT_Utility.trans;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
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
            case 1 -> recipeInfo.drawText(trans("509", "Requires HIP Unit"));
            case 2 -> recipeInfo.drawText(trans("508", "Requires Stabilized Black Hole"));
        }
    }
}
