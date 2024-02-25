package gregtech.api.interfaces.tileentity;

public interface MachineProgress {

    /**
     * allows Machine to work
     */
    void enableWorking();

    /**
     * disallows Machine to work
     */
    void disableWorking();

    /**
     * if the Machine is allowed to Work
     */
    boolean isAllowedToWork();

    default void setAllowedToWork(Boolean allowedToWork) {
        if (allowedToWork) {
            enableWorking();
        } else {
            disableWorking();
        }
    }

    /**
     * gives you the Active Status of the Machine
     */
    boolean isActive();

    /**
     * sets the visible Active Status of the Machine
     */
    void setActive(boolean aActive);

    /**
     * returns if the Machine currently does something.
     */
    boolean hasThingsToDo();
}
