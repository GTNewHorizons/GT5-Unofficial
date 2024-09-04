package galacticgreg.api;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.enums.Mods;

/**
 * Defines a Mod where this Generator shall be active. Note: This will only work (obviously) for Dimensions where
 * either: - Gregtech has a hook in the OreGen or - For mods which are addons to GalactiCraft
 *
 */
public class ModContainer {

    private final List<ModDimensionDef> _mDimensionLookup;
    private final Mods mod;

    /**
     * Define a new Mod where GT OreGen shall be enabled
     *
     * @param mod The Mods enum value.
     */
    public ModContainer(Mods mod) {
        this.mod = mod;
        _mDimensionLookup = new ArrayList<>();
    }

    public boolean isModLoaded() {
        return this.mod.isModLoaded();
    }

    /**
     * Internal function
     *
     * @return The mods name
     */
    public String getModName() {
        return mod.ID;
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
        pDimDef.setParentModName(mod.ID);
        _mDimensionLookup.add(pDimDef);
        return true;
    }
}
