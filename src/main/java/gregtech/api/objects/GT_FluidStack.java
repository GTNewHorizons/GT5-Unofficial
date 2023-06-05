package gregtech.api.objects;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;

/**
 * Because Forge fucked this one up royally.
 */
public class GT_FluidStack extends FluidStack {

    private static final Collection<GT_FluidStack> sAllFluidStacks = Collections
        .newSetFromMap(new WeakHashMap<>(10000));
    private final Fluid mFluid;

    public GT_FluidStack(Fluid aFluid, int aAmount) {
        super(aFluid, aAmount);
        mFluid = aFluid;
        if (!GregTech_API.mServerStarted) {
            sAllFluidStacks.add(this);
        }
    }

    public GT_FluidStack(FluidStack aFluid) {
        this(aFluid.getFluid(), aFluid.amount);
    }

    @Override
    public FluidStack copy() {
        return new GT_FluidStack(this);
    }

    @Override
    public String toString() {
        return String.format(
            "GT_FluidStack: %s x %s, ID:%s",
            this.amount,
            this.getFluid()
                .getName(),
            this.getFluidID());
    }
}
