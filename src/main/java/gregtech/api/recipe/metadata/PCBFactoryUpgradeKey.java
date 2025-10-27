package gregtech.api.recipe.metadata;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.GTUtility;
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
            recipeInfo.drawText(
                GTUtility.translate("gt.recipe.pcb_factory_upgrade_requirement")
                    + GTUtility.translate("gt.recipe.pcb_factory_upgrade_bio"));
        }
    }
}
