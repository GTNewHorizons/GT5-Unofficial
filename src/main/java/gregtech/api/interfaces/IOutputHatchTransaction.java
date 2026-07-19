package gregtech.api.interfaces;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTUtility;

/**
 * {@inheritDoc}
 * You probably want to use {@link gregtech.api.util.FluidEjectionHelper} instead of this.
 * It is assumed that the Hatch will not change over
 * this object's lifetime.
 */
public interface IOutputHatchTransaction extends IOutputTransaction<GTUtility.FluidId, FluidStack> {

    IOutputHatch getHatch();

    @Override
    default boolean isFiltered() {
        return getHatch().isFiltered();
    }

    @Override
    default boolean isFilteredTo(GTUtility.FluidId id) {
        return getHatch().isFilteredToFluid(id);
    }
}
