package gregtech.common.gui.modularui.widget;

import com.gtnewhorizons.modularui.common.widget.FluidNameHolderWidget;

import gregtech.api.interfaces.metatileentity.IFluidLockable;

public class FluidLockWidget extends FluidNameHolderWidget {

    public FluidLockWidget(IFluidLockable fluidLockable) {
        super(fluidLockable::getLockedFluidName, name -> {
            if (fluidLockable.acceptsFluidLock(name)) {
                fluidLockable.setLockedFluidName(name);
                fluidLockable.lockFluid(name != null);
            }
        });
    }
}
