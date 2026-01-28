package gregtech.api.interfaces;

import gregtech.common.tileentities.machines.multi.MTEBiosphere;

public interface IBiosphereCompatible {

    /**
     * Biospheres will call this method on IBiosphereCompatible tile entities with a reference to itself whenever
     * its state is updated. The general pattern for IBiosphereCompatible implementations is to store the reference
     * to the biosphere whenever this method is called. When running, the tile entity should first ensure the reference
     * is non-null, then query it for {@link MTEBiosphere#getDimensionOverride()} to check the biosphere's active
     * dimension.
     * <p>
     * The biosphere is guaranteed to call this method with a null reference whenever the biosphere is removed or
     * shut down. Tile entities should use their regular dimension behavior when the stored reference is null.
     */
    void setDimensionOverride(MTEBiosphere biosphere);
}
