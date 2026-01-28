package gregtech.api.interfaces;

import gregtech.common.tileentities.machines.multi.MTEBiosphere;

public interface IBiosphereCompatible {

    /**
     * Biosphere will call this method with the dimension name that it is currently set to. This method should
     * return true if the tile entity in question is allowed to function in that dimension, used for biosphere info
     * readout.
     */
    boolean setDimensionOverride(String dimensionName, MTEBiosphere biosphere);
}
