package gregtech.api.recipe.metadata;

import static kubatech.api.Variables.numberFormat;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Base success chance for Purification Plant recipes
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamAmount extends RecipeMetadataKey<Long> {

    public static final SteamAmount INSTANCE = new SteamAmount();

    private SteamAmount() {
        super(Long.class, "offer_value");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        long offer = cast(value, 0l);
        recipeInfo.drawText("Offer Value: " + numberFormat.format(offer));
    }
}
