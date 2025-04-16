package gregtech.api.structure;

import gregtech.api.casing.ICasingGroup;

/**
 * The object which tracks a structure's current misc metadata (its casing counts, etc).
 */
public interface IStructureInstance {

    /**
     * Called when a casing is encountered during the structure check.
     */
    void onCasingEncountered(char casing);

    int getCasingTier(ICasingGroup casing, int unset);

    void setCasingTier(ICasingGroup casing, int tier);
}
