package gregtech.common.gui.modularui.hatch.base;

import java.util.function.Predicate;

import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.common.gui.modularui.singleblock.base.MTEMachineWithFluidScreenBaseGui;

/**
 * A base class for common hatch implementations. Has configurable corner panels and makes building ui's easier.
 * The main overriding is done in
 * {@link #createContentSection(ModularPanel, PanelSyncManager)}
 *
 * For heavily custom UI's, override
 * {@link #build(PosGuiData, PanelSyncManager, UISettings)}
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
