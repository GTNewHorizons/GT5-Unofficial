package gregtech.api.interfaces;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GTUtility;

/**
 * A simulated output Hatch, used for fallibly ejecting fluids in one go.
 * This is used for void protection calculations, but it can also be used for general fluid ejection.
 * You probably want to use {@link gregtech.api.util.FluidEjectionHelper} instead of this.
 * Transactions should never be kept around for longer than a tick. It is assumed that the Hatch will not change over
 * this object's lifetime.
 */
public interface IOutputHatchTransaction {

    IOutputHatch getHatch();

    default boolean isFiltered() {
        return getHatch().isFiltered();
    }

    default boolean isFilteredToFluid(GTUtility.FluidId id) {
        return getHatch().isFilteredToFluid(id);
    }

    /** Short circuit in case this transaction is completely full. */
    boolean hasAvailableSpace();

    /**
     * Fills one slot with the given fluid, then subtracts the amount stored from the stack's stackSize. This modifies
     * this transaction's internal state, but does not change the original Hatch. The time complexity of this should be
     * as close to O(1) as possible, because it's called in an O(n*m) loop.
     *
     * @param id    The stack's fluid id, to avoid allocations.
     * @param stack The stack to inject into this Hatch.
     * @return True when fluids were injected into a slot, false otherwise.
     */
    boolean storePartial(GTUtility.FluidId id, @NotNull FluidStack stack);

    /**
     * Signals to the transaction that this fluid will never be stored again, allowing it to skip partially filled
     * slots.
     */
    void completeFluid(GTUtility.FluidId id);

    /**
     * Commits the changes stored in this transaction back into the real Hatch. Calling this is the only time the real
     * Hatch should be modified. After committing a transaction, the transaction is 'spent' and cannot be modified in
     * any way.
     */
    void commit();
}
