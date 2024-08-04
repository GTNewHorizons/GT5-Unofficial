package bloodasp.galacticgreg.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Mod where this Generator shall be active. Note: This will only work (obviously) for Dimensions where
 * either: - Gregtech has a hook in the OreGen or - For mods which are addons to GalactiCraft
 *
 */
public class ModContainer {

    private String _mModName;
    private List<ModDimensionDef> _mDimensionLookup;
    private boolean _mEnabled = false;

    /**
     * Internal function
     * 
     * @return The state if the Registry could find the mod or not
     */
    public boolean getEnabled() {
        return _mEnabled;
    }

    /**
     * Internal function
     * 
     * Never set this to true. This is an internal marker which is set by the registry if the mod could be found or not
     * 
     * @param pEnabled
     */
    public void setEnabled(boolean pEnabled) {
        _mEnabled = pEnabled;
    }

    /**
     * Define a new Mod where GT OreGen shall be enabled
     * 
     * @param pModName The modID. Make sure to use the proper mod-id, or it won't load correctly
     */
    public ModContainer(String pModName) {
        _mModName = pModName;
        _mDimensionLookup = new ArrayList<>();
    }

    /**
     * Internal function
     * 
     * @return The mods name
     */
    public String getModName() {
        return _mModName;
    }

    /**
     * Internal function
     * 
     * @return The list of attached dimensions for this mod
     */
    public List<ModDimensionDef> getDimensionList() {
        return _mDimensionLookup;
    }

    /**
     * Adds a new dimension to this modcontainer. Make sure you've added all blocks there first
     * 
     * @param pDimDef The dimension definition to be added
     * @return true if it could be added, false if not
     */
    public boolean addDimensionDef(ModDimensionDef pDimDef) {
        for (ModDimensionDef mdd : _mDimensionLookup) {
            if (mdd.getChunkProviderName()
                .equals(pDimDef.getChunkProviderName())) {
                // Cannot add DimensionDefinition; The Given chunk-provider name is already taken!
                return false;
            }
        }

        // Set the parent modName of this dimension. This will finalize it
        pDimDef.setParentModName(_mModName);
        _mDimensionLookup.add(pDimDef);
        return true;
    }
}
