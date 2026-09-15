package gregtech.api.recipe.metadata;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

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
            recipeInfo
                .drawText(translateToLocalFormatted("GT5U.nei.solar_factory.tier_required", metadata.tierRequired));
        }
    }
}
