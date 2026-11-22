package gregtech.common.tileentities.machines;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;

public interface ISmartInputHatch {

    /*
     * An interface to allow hatch to emit change notification for instant recipe check
     */
    /**
     * Registers a controller to be notified when this hatch's contents/config change. Hatches that want to push an
     * immediate recipe check implement {@link ISmartInputHatch}, and the controller registers itself here when the
     * hatch is added to the structure. A hatch belongs to a multiblock exactly once. The underlying data structure
     * protects against duplicate watchers.
     * You should always add watchers via {@link MTEMultiBlockBase#addIfSmartInput(IMetaTileEntity)},
     * so that the watchers get properly removed from the hatch when needed.
     * If you don't, manual removal is required!
     */
    void addWatcher(IHatchWatcher watcher);

    void removeWatcher(IHatchWatcher watcher);

    default boolean needsPeriodicChecks() {
        return false;
    }

}
