package gregtech.common.gui.modularui.hatch.base;

import java.util.function.Predicate;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.common.gui.modularui.singleblock.base.MTEMachineWithFluidScreenBaseGui;

/**
 * A base class for common hatch implementations. Has configurable corner panels and makes building ui's easier.
 * The main overriding is done in
 * {@link #createContentSection(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager)}
 *
 * For heavily custom UI's, override
 * {@link #build(com.cleanroommc.modularui.factory.PosGuiData, com.cleanroommc.modularui.value.sync.PanelSyncManager, com.cleanroommc.modularui.screen.UISettings)}
 * instead.
 */
public class MTEHatchBaseGui<T extends MTEHatch> extends MTEMachineWithFluidScreenBaseGui<T> {

    public MTEHatchBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected Predicate<FluidStack> getFluidSlotFilter() {
        return machine::isFluidInputAllowed;
    }
}
