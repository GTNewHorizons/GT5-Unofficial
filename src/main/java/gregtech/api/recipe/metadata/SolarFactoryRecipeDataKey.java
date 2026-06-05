package gregtech.api.recipe.metadata;

import javax.annotation.Nullable;

import net.minecraft.util.StatCollector;

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
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted("GT5U.nei.recipe.tier_required", metadata.tierRequired));
        }
    }
}
