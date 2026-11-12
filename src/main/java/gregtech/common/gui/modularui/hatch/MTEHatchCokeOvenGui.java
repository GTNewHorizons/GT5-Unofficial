package gregtech.common.gui.modularui.hatch;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.metatileentity.implementations.MTEHatchCokeOven;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEHatchCokeOvenGui extends MTEHatchBaseGui<MTEHatchCokeOven> {

    public MTEHatchCokeOvenGui(MTEHatchCokeOven hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> content = super.createContentSection(panel, syncManager);
        if (machine.isFluidOutput()) {
            return content.child(createScreen(panel, syncManager, machine.getFluidTank()));
        }
        return content.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(1)
                .canPut(machine.isInput())
                .build()
                .center());
    }

    @Override
    protected FluidSlot createFluidSlot(ModularPanel panel, PanelSyncManager syncManager, IFluidTank fluidTank) {
        return super.createFluidSlot(panel, syncManager, fluidTank).syncHandler(new FluidSlotSyncHandler(fluidTank) {

            @Override
            public void setValue(@Nullable FluidStack value, boolean setSource, boolean sync) {
                if (setSource && baseMetaTileEntity.isClientSide()) {
                    // Client synchronization must bypass the tank's output-only fill policy.
                    machine.setDrainableStack(copyFluid(value));
                    setSource = false;
                }
                super.setValue(value, setSource, sync);
            }
        }.canFillSlot(false));
    }
}
