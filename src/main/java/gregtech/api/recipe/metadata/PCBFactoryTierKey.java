package gregtech.api.recipe.metadata;

import static gregtech.api.util.GTUtility.trans;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Minimum tier required for the PCB factory recipe.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PCBFactoryTierKey extends RecipeMetadataKey<Integer> {

    public static final PCBFactoryTierKey INSTANCE = new PCBFactoryTierKey();

    private PCBFactoryTierKey() {
        super(Integer.class, "pcb_factory_tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        recipeInfo.drawText(trans("336", "PCB Factory Tier: ") + tier);
    }
}
