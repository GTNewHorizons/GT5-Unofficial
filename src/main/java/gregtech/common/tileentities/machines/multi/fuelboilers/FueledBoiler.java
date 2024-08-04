package gregtech.common.tileentities.machines.multi.fuelboilers;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;

/**
 * The base class for fuel-based boilers. These burn fuels (like Benzene or Diesel) into Steam, instead of direct
 * combustion - in exchange for the extra step, they burn more efficiently at 1 EU to 2.4L steam.
 */
public abstract class FueledBoiler<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>> extends GT_MetaTileEntity_EnhancedMultiBlockBase<T> {
    protected FueledBoiler(int id, String name, String localizedName) {
        super(id, name, localizedName);
    }
}
