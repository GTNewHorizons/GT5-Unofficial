package gregtech.common.tileentities.machines;

public interface ISmartInputHatch {

    /*
     * An interface to allow hatch to emit change notification for instant recipe check
     */

    void addWatcher(IHatchWatcher watcher);

    void removeWatcher(IHatchWatcher watcher);

}
