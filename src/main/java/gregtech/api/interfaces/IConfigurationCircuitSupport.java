package gregtech.api.interfaces;

/**
 * Implement this interface if your MetaTileEntity supports configuration circuits to resolve recipe conflicts.
 */
public interface IConfigurationCircuitSupport {

    /**
     * @return Integrated circuit slot index in the machine inventory
     */
    int getCircuitSlot();

    /**
     * @return True if that machine supports built-in configuration circuit
     */
    boolean allowSelectCircuit();

    int getCircuitSlotX();

    int getCircuitSlotY();
}
