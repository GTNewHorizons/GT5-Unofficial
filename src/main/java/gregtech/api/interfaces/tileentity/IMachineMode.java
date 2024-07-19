package gregtech.api.interfaces.tileentity;

public interface IMachineMode {

    /**
     * Gets the numeric value associated with this machine mode
     * 
     * @return Integer representing mode
     */
    int getModeID();

    /**
     * Gets the name associated with this machine mode
     * 
     * @return String representing mode
     */
    String getModeName();

    /**
     * Gets the mode associated with this index
     * 
     * @param index ID
     * @return MachineMode associated with index
     */
    IMachineMode getByID(int index);

    /**
     * Gets the mode that should be next in the sequence
     * 
     * @return MachineMode representing next in sequence
     */
    IMachineMode nextMachineMode();
}
