package gregtech.api.structure;

import gregtech.api.casing.ICasing;
import gregtech.api.casing.ICasingGroup;
import gregtech.api.metatileentity.implementations.MTEHatch;

/**
 * The object which tracks a structure's current misc metadata (its casing counts, etc).
 */
public interface IStructureInstance<MTE> {

    /**
     * Called when a casing is encountered during the structure check.
     */
    void onCasingEncountered(char casing);

    int getCasingTier(ICasingGroup casing, int unset);

    void setCasingTier(ICasingGroup casing, int tier);

    /**
     * Adds a hatch that must be re-textured after the structure check is complete, for when the casing tier must be
     * fully known to get the proper texture (tiered casings).
     * 
     * @param hatch   The hatch
     * @param casing  The casing it replaces
     * @param context The casing's context, which is used to retrieve the casing's tier
     */
    void addTieredHatch(MTEHatch hatch, ICasing casing, ICasing.CasingElementContext<MTE> context);
}
