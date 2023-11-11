package gregtech.api.recipe.metadata;

import static gregtech.api.util.GT_Utility.trans;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * If bio upgrade is required for the PCB factory recipe.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PCBFactoryUpgradeKey extends RecipeMetadataKey<PCBFactoryUpgrade> {

    public static final PCBFactoryUpgradeKey INSTANCE = new PCBFactoryUpgradeKey();

    private PCBFactoryUpgradeKey() {
        super(PCBFactoryUpgrade.class, "pcb_factory_bio_upgrade");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        PCBFactoryUpgrade upgrade = cast(value);
        if (upgrade == PCBFactoryUpgrade.BIO) {
            recipeInfo.drawText(trans("337", "Upgrade Required: ") + trans("338", "Bio"));
        }
    }
}
