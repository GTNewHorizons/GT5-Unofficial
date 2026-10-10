package gregtech.api.interfaces;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;

/**
 * Implement on a MetaTileEntity to expose physical Integrated Circuit item(s) present alongside a ghost circuit
 * config (e.g. stocked in an ME Input Bus) so they can be shown as part of the ghost circuit suffix in the AE2
 * terminal interface name.
 */
public interface IPhysicalCircuitDisplay {

    /**
     * Returns the configuration numbers of physical Integrated Circuit items currently present, excluding the
     * ghost circuit slot itself. Returns an empty list if none are present.
     */
    List<Integer> getPhysicalCircuitNumbers();

    /**
     * Collects the configuration numbers of the Integrated Circuits found in the given slot range.
     *
     * @param skipSlot Slot to ignore, usually the ghost circuit slot.
     */
    static List<Integer> collectCircuitNumbers(MetaTileEntity machine, int fromSlot, int toSlot, int skipSlot) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = fromSlot; i < toSlot; i++) {
            if (i == skipSlot) continue;
            ItemStack stack = machine.getStackInSlot(i);
            if (GTUtility.isAnyIntegratedCircuit(stack)) {
                numbers.add(stack.getItemDamage());
            }
        }
        return numbers;
    }
}
