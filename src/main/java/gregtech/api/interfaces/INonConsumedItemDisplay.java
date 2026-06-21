package gregtech.api.interfaces;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Implement on a MetaTileEntity to expose non-consumed recipe inputs (e.g. molds in the Extruder)
 * so they can be shown as a suffix in the AE2 terminal interface name.
 */
public interface INonConsumedItemDisplay {

    /**
     * Returns the list of non-consumed items from the last matched recipe (items with stackSize == 0
     * in the recipe definition). Returns an empty list if no recipe has been run yet or none exist.
     */
    List<ItemStack> getNonConsumedInputDisplayItems();
}
