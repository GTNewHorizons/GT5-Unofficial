package gregtech.api.interfaces;

import gregtech.common.tileentities.machines.multi.MTEBiodome;

public interface IBiodomeCompatible {

    /**
     * Biodomes will call this method on IBiodomeCompatible tile entities with a reference to itself whenever
     * its state is updated. The general pattern for IBiodomeCompatible implementations is to store the reference
     * to the biodome whenever this method is called. When running, the tile entity should first ensure the reference
     * is non-null, then query it for {@link MTEBiodome#getDimensionOverride()} to check the biodome's active
     * dimension.
     * <p>
     * The biodome is guaranteed to call this method with a null reference whenever the biodome is removed or
     * shut down. Tile entities should use their regular dimension behavior when the stored reference is null.
     */
    void updateBiodome(MTEBiodome biodome);
}
