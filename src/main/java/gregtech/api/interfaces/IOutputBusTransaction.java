package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

/**
 * A simulated output bus, used for fallibly ejecting items in one go.
 * This is used for void protection calculations, but it can also be used for general item ejection.
 * You probably want to use {@link gregtech.api.util.ItemEjectionHelper} instead of this.
 * Transactions should never be kept around for longer than a tick. It is assumed that the bus will not change over this
 * object's lifetime.
 */
public interface IOutputBusTransaction {

    IOutputBus getBus();

    default boolean isFiltered() {
        return getBus().isFiltered();
    }

    default boolean isFilteredToItem(GTUtility.ItemId id) {
        return getBus().isFilteredToItem(id);
    }

    /** Short circuit in case this transaction is completely full. */
    boolean hasAvailableSpace();

    /**
     * Fills one slot with the given item, then subtracts the amount stored from the stack's stackSize. This modifies
     * this transaction's internal state, but does not change the original bus. The time complexity of this should be as
     * close to O(1) as possible, because it's called in an O(n*m) loop.
     *
     * @param id    The stack's item id, to avoid allocations.
     * @param stack The stack to inject into this bus.
     * @return True when items were injected into a slot, false otherwise.
     */
    boolean storePartial(GTUtility.ItemId id, ItemStack stack);

    /**
     * Signals to the transaction that this item will never be stored again, allowing it to skip partially filled slots.
     */
    void completeItem(GTUtility.ItemId id);

    /**
     * Commits the changes stored in this transaction back into the real bus. Calling this is the only time the real
     * bus should be modified. After committing a transaction, the transaction is 'spent' and cannot be modified in any
     * way.
     */
    void commit();
}
