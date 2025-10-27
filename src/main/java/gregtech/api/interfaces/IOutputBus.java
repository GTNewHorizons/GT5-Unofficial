package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.OutputBusType;
import gregtech.api.util.GTUtility;

public interface IOutputBus {

    /** Returns true when this bus can only accept specific items. */
    boolean isFiltered();

    /** Returns true when the given item id matches this busses filter exactly. */
    boolean isFilteredToItem(GTUtility.ItemId id);

    /** Returns this bus's type. Used for output order sorting. */
    OutputBusType getBusType();

    default boolean storePartial(ItemStack stack) {
        return storePartial(stack, false);
    }

    /**
     * Attempt to store as many items as possible into the internal inventory of this output bus. If you need atomicity
     * you should use {@link gregtech.api.interfaces.tileentity.IHasInventory#addStackToSlot(int, ItemStack)}
     *
     * @param stack    The stack to insert. Will be modified by this method (will contain whatever items could not be
     *                 inserted; stackSize will be 0 when everything was inserted).
     * @param simulate When true this bus will not be modified.
     * @return True if the stack was fully inserted into the bus, false otherwise.
     */
    boolean storePartial(ItemStack stack, boolean simulate);

    /**
     * Creates a transaction from this output bus. The transaction copies this bus' state (inventory, etc) when
     * created, and subsequent calls on the transaction modify the copy of this bus. The transaction does not modify
     * this bus' state unless {@link IOutputBusTransaction#commit()} is called.
     */
    IOutputBusTransaction createTransaction();
}
