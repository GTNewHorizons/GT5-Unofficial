package gregtech.api.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * A simulated output transaction, used for fallibly ejecting fluids in one go.
 * This is used for void protection calculations, but it can also be used for general ejection.
 * Transactions should never be kept around for longer than a tick.
 */
public interface IOutputTransaction<ID, T> {

    boolean isFiltered();

    boolean isFilteredTo(ID id);

    /** Short circuit in case this transaction is completely full. */
    boolean hasAvailableSpace();

    /**
     * Fills one slot with the given stack, then subtracts the amount stored from the stack's stackSize. This modifies
     * this transaction's internal state.
     *
     * @param id    The stack's id, to avoid allocations.
     * @param stack The stack to inject.
     * @return True when stacks were injected into a slot, false otherwise.
     */
    boolean storePartial(ID id, @NotNull T stack);

    /**
     * Signals to the transaction that this stack will never be stored again, allowing it to skip partially filled
     * slots.
     */
    void complete(ID id);

    /**
     * Commits the changes stored in this transaction back into the output. Calling this is the only time the real
     * output should be modified. After committing a transaction, the transaction is 'spent' and cannot be modified in
     * any way.
     */
    void commit();

    interface IRecipeCheckAware {

        void setRecipeCheck(boolean isRecipeCheck);
    }

    interface IProtectOutputAware {

        void setProtectOutput(boolean isProtectOutput);
    }

    /**
     * Represents an output that may have a finite capacity but allows for dynamic resource allocation.
     */
    interface IDynamicCapacityOutputAware {

        /**
         * Checks if this output has a finite capacity but allows for dynamic resource allocation.
         *
         * @return true if it is, false otherwise
         */
        boolean isDynamicCapacity();
    }
}
