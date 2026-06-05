package gregtech.api.recipe.metadata;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

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
        recipeInfo.drawText(StatCollector.translateToLocalFormatted("GT5U.nei.recipe.pcb_factory_tier", tier));
    }
}
