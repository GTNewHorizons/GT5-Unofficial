package gregtech.common.gui.modularui.hatch;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
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
        BooleanSyncValue fluidOutput = new BooleanSyncValue(machine::isFluidOutput);
        syncManager.syncValue("fluidOutput", fluidOutput);

        // Register both widgets so the client and server always agree on slot IDs.
        return super.createContentSection(panel, syncManager)
            .child(createScreen(panel, syncManager).setEnabledIf(widget -> fluidOutput.getBoolValue()))
            .child(
                new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(1)
                    .build()
                    .center()
                    .setEnabledIf(widget -> !fluidOutput.getBoolValue()));
    }

    @Override
    protected FluidSlot createSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createSlot(panel, syncManager).syncHandler(new FluidSlotSyncHandler(getMachineFluidTank()) {

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
