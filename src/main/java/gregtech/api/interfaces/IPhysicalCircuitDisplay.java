package gregtech.api.interfaces;

import java.util.List;

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
}
