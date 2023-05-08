package gregtech.api.interfaces.metatileentity;

import net.minecraftforge.fluids.Fluid;

/**
 * Implement this interface if your MetaTileEntity supports fluid lock mechanism.
 */
@SuppressWarnings({ "BooleanMethodIsAlwaysInverted" })
public interface IFluidLockable {

    /**
     * Use {@link Fluid#getName()} instead of {@link Fluid#getUnlocalizedName()} for fluid name
     */
    void setLockedFluidName(String name);

    String getLockedFluidName();

    /**
     * Set fluid lock state. Would be useful when you don't necessarily want to change mode when locked fluid is
     * changed.
     */
    void lockFluid(boolean lock);

    boolean isFluidLocked();

    boolean acceptsFluidLock(String name);
}
