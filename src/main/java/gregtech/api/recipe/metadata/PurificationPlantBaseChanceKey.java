package gregtech.api.recipe.metadata;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Base success chance for Purification Plant recipes
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationPlantBaseChanceKey extends RecipeMetadataKey<Float> {

    public static final PurificationPlantBaseChanceKey INSTANCE = new PurificationPlantBaseChanceKey();

    private PurificationPlantBaseChanceKey() {
        super(Float.class, "purification_plant_base_chance");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        double chance = cast(value, 0.0f);
        recipeInfo.drawText(StatCollector.translateToLocalFormatted("GT5U.nei.purified_water.base_chance", chance));
    }
}
