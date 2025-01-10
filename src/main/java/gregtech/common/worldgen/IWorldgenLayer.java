package gregtech.common.worldgen;

import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneCategory;

/** A worldgen layer that generates ore. */
public interface IWorldgenLayer {

    int getMinY();

    int getMaxY();

    /** Gets the RNG weight. */
    int getWeight();

    /** Gets the density. 1 = 100%, 0.5 = 50%, 0 = 0% */
    float getDensity();

    /** Checks if this layer can generate in the given dimension. */
    boolean canGenerateIn(String dimName);

    /** Checks if this layer can generate in the given stone category. */
    boolean canGenerateIn(IStoneCategory stoneType);

    /** Whether this layer can only generate in specific stone types. */
    boolean isStoneSpecific();

    /** If this layer generates big ore. Ignores small ore. */
    boolean generatesBigOre();

    /** Checks if this layer contains this material */
    boolean contains(IOreMaterial ore);

    /**
     * Selects an ore for worldgen.
     * 
     * @param k The weight, 0-1.
     * @return The ore.
     */
    IOreMaterial getOre(float k);

    String getName();
}
