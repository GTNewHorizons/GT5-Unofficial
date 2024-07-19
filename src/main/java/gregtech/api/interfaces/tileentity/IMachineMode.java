package gregtech.api.interfaces.tileentity;

public interface IMachineMode {

    int modeID = 0;
    String modeName = "";

    /**
     * Gets the numeric value associated with this machine mode
     * 
     * @return Integer representing mode
     */
    default int getModeID() {
        return modeID;
    }

    /**
     * Gets the name associated with this machine mode
     * 
     * @return String representing mode
     */
    default String getModeName() {
        return modeName;
    }

    /**
     * Gets the mode that should be next in the sequence
     * 
     * @return MachineMode representing next in sequence
     */
    IMachineMode nextMachineMode();
}
