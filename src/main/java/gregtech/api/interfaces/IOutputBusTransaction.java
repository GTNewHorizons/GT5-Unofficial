package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

/**
 * {@inheritDoc}
 * You probably want to use {@link gregtech.api.util.ItemEjectionHelper} instead of this.
 * It is assumed that the bus will not change over this object's lifetime.
 */
public interface IOutputBusTransaction extends IOutputTransaction<GTUtility.ItemId, ItemStack> {

    IOutputBus getBus();

    @Override
    default boolean isFiltered() {
        return getBus().isFiltered();
    }

    @Override
    default boolean isFilteredTo(GTUtility.ItemId id) {
        return getBus().isFilteredToItem(id);
    }
}
