package gregtech.api.recipe.metadata;

import static gregtech.api.util.GTUtility.trans;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Wafer tier required for Solar Factory recipes
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SolarFactoryKey extends RecipeMetadataKey<Integer> {

    public static final SolarFactoryKey INSTANCE = new SolarFactoryKey();

    private SolarFactoryKey() {
        super(Integer.class, "pcb_factory_tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        switch (tier) {
            case 1 -> recipeInfo.drawText(trans("510", "Minimum wafer: T1"));
            case 2 -> recipeInfo.drawText(trans("511", "Minimum wafer: T2"));
            case 3 -> recipeInfo.drawText(trans("512", "Minimum wafer: T3"));
            case 4 -> recipeInfo.drawText(trans("513", "Minimum wafer: T4"));
            case 5 -> recipeInfo.drawText(trans("514", "Minimum wafer: T5"));
            case 6 -> recipeInfo.drawText(trans("515", "Minimum wafer: T6"));
            case 7 -> recipeInfo.drawText(trans("516", "Minimum wafer: T7"));
        }
    }
}
