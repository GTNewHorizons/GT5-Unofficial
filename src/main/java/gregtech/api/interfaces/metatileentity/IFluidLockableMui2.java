package gregtech.api.interfaces.metatileentity;

import net.minecraftforge.fluids.Fluid;

/**
 * Implement this interface if your MetaTileEntity supports fluid lock mechanism.
 */
public interface IFluidLockableMui2 {

    void setLockedFluid(Fluid name);

    Fluid getLockedFluid();

    /**
     * Set fluid lock state. Would be useful when you don't necessarily want to change mode when locked fluid is
     * changed.
     */
    void lockFluid(boolean lock);

    boolean isFluidLocked();

    boolean acceptsFluidLock(Fluid name);
}
