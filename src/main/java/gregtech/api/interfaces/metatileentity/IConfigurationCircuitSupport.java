package gregtech.api.interfaces.metatileentity;

import gregtech.api.GregTech_API;
import java.util.List;
import net.minecraft.item.ItemStack;

/**
 *  Implement this interface if your metatileentity supports configuration circuits
 *  to resolve recipe conflicts.
 */
public interface IConfigurationCircuitSupport {
    /**
     *
     * @return Integrated circuit slot index in the machine inventory
     */
    int getCircuitSlot();

    /**
     * Return a list of possible configuration circuit this machine expects.
     *
     * This list is unmodifiable. Its elements are not supposed to be modified in any way!
     */
    default List<ItemStack> getConfigurationCircuits() {
        return GregTech_API.getConfigurationCircuitList(100);
    }

    /**
     *
     * @return True if that machine supports built-in configuration circuit
     */
    boolean allowSelectCircuit();

    /**
     *
     * @return Circuit slot index in GUI container
     */
    default int getCircuitGUISlot() {
        return getCircuitSlot();
    }

    int getCircuitSlotX();

    int getCircuitSlotY();
}
