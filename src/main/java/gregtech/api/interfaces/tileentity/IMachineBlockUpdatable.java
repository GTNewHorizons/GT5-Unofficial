package gregtech.api.interfaces.tileentity;

/**
 * You are allowed to include this File in your Download, as I will not change it. Simple Interface for Machines, which
 * need my Machine Blocks for MultiBlockStructures.
 * <p/>
 * Every Machine implementing this Interface will conduct Machine updates.
 */
public interface IMachineBlockUpdatable {

    /**
     * The Machine Update, which is called when the Machine needs an Update of its Parts. I suggest waiting 1-5 seconds
     * before actually checking the Machine Parts. RP-Frames could, for example, cause problems when you insta-check the
     * Machine Parts.
     */
    void onMachineBlockUpdate();

    /**
     * Should recurse?
     */
    default boolean isMachineBlockUpdateRecursive() {
        return true;
    }
}
