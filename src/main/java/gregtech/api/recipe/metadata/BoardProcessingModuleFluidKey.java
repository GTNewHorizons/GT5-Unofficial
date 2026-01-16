package gregtech.api.recipe.metadata;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static gregtech.api.util.GTUtility.trans;

/**
 * Type of immersion fluid required
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BoardProcessingModuleFluidKey extends RecipeMetadataKey<Integer> {

    public static final BoardProcessingModuleFluidKey INSTANCE = new BoardProcessingModuleFluidKey();

    private BoardProcessingModuleFluidKey() {
        super(Integer.class, "fluid_type");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int type = cast(value, 1);
        switch (type) {
            case 1 -> recipeInfo.drawText(trans("511", "Crystal"));
            case 2 -> recipeInfo.drawText(trans("512", "Wet"));
            case 3 -> recipeInfo.drawText(trans("513", "Bio"));
        }
    }
}
