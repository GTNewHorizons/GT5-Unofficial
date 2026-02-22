package gregtech.api.recipe.metadata;

import javax.annotation.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public class CentrifugeRecipeKey extends RecipeMetadataKey<Boolean> {

    public static final CentrifugeRecipeKey INSTANCE = new CentrifugeRecipeKey();

    private CentrifugeRecipeKey() {
        super(Boolean.class, "centrifuge_heavy_mode");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        boolean required = cast(value, false);
        recipeInfo.drawText("Heavy Mode Required");
    }
}
