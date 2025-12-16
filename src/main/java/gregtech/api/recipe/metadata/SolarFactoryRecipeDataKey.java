package gregtech.api.recipe.metadata;

import static gregtech.api.util.GTUtility.trans;

import javax.annotation.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.recipe.SolarFactoryRecipeData;
import gregtech.nei.RecipeDisplayInfo;

public class SolarFactoryRecipeDataKey extends RecipeMetadataKey<SolarFactoryRecipeData> {

    private SolarFactoryRecipeDataKey() {
        super(SolarFactoryRecipeData.class, "solar_factory_wafer_data");
    }

    public static final SolarFactoryRecipeDataKey INSTANCE = new SolarFactoryRecipeDataKey();

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        SolarFactoryRecipeData metadata = cast(value);
        if (metadata.tierRequired != 0) {
            recipeInfo.drawText(trans("510", "Tier required: ") + metadata.tierRequired);
        }
    }
}
