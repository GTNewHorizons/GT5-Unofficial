package gregtech.common.gui.modularui.widget;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.interfaces.metatileentity.IFluidLockableMui2;

/**
 * Represents both the {@link FluidSlot} itself and the {@link IFluidTank} that {@link FluidSlotSyncHandler} handles.
 * Create a new instance of this, then pass it to its own {@link FluidSlotSyncHandler}.
 */
public class FluidLockSlot extends FluidSlot implements IFluidTank {

    private final IFluidLockableMui2 machine;

    public FluidLockSlot(IFluidLockableMui2 machine) {
        this.machine = machine;
    }

    @Override
    public FluidSlot syncHandler(IFluidTank fluidTank) {
        return syncHandler(new FluidSlotSyncHandler(fluidTank).controlsAmount(false));
    }

    @Override
    public FluidSlot syncHandler(FluidSlotSyncHandler syncHandler) {
        setSyncHandler(syncHandler.controlsAmount(false));
        return this;
    }

    @Override
    public boolean handleDragAndDrop(@NotNull ItemStack draggedStack, int button) {
        return super.handleDragAndDrop(draggedStack, button);
    }

    protected void addToolTip(RichTooltip tooltip) {
        FluidStack fluid = getFluid();
        if (fluid != null) {
            tooltip.addFromFluid(fluid);
            tooltip.spaceLine(2);
            tooltip.addLine(StatCollector.translateToLocal("GT5U.hatch.filter.tooltip.0"));
        } else {
            tooltip.addLine(StatCollector.translateToLocal("GT5U.hatch.filter.none"));
        }
    }

    // IFluidTank implementation

    @Override
    public FluidStack getFluid() {
        return machine.getLockedFluid() != null ? new FluidStack(machine.getLockedFluid(), 1) : null;
    }

    @Override
    public int getFluidAmount() {
        return machine.getLockedFluid() != null ? 1 : 0;
    }

    @Override
    public int getCapacity() {
        return 1000;
    }

    @Override
    public FluidTankInfo getInfo() {
        return machine.getLockedFluid() != null ? new FluidTankInfo(new FluidStack(machine.getLockedFluid(), 1), 1000)
            : null;
    }

    @Override
    public int fill(FluidStack fluidStack, boolean doFill) {
        if (doFill) {
            machine.setLockedFluid(fluidStack.getFluid());
        }
        return 1;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (doDrain) {
            machine.setLockedFluid(null);
        }

        return null;
    }
}
