package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.metatileentity.implementations.MTEHatchInputDebug;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchInputDebugGui extends MTEHatchBaseGui<MTEHatchInputDebug> {

    public MTEHatchInputDebugGui(MTEHatchInputDebug base) {
        super(base);
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftCornerFlow(panel, syncManager).child(createInfoButton());
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        for (int i = 0; i < hatch.fluidTankList.length; i++) {
            syncManager.syncValue(
                "fluidTanks",
                i,
                SyncHandlers.fluidSlot(hatch.fluidTankList[i])
                    .phantom(true)
                    .canDrainSlot(true)
                    .canFillSlot(true)
                    .controlsAmount(false));
        }
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            SlotGroupWidget.builder()
                .matrix("IIII", "IIII", "IIII", "IIII")
                .key('I', index -> { return new FluidSlot().syncHandler("fluidTanks", index); })
                .build()
                .marginTop(4)
                .align(Alignment.TopCenter));
    }

    private IDrawable.DrawableWidget createInfoButton() {
        return new IDrawable.DrawableWidget(GuiTextures.BUBBLE).background(GTGuiTextures.BUTTON_STANDARD)
            .size(18)
            .tooltip(t -> {
                t.addLine("Drag Fluids into the Fluid Slots");
                t.addLine("Fluids in the slots will not be consumed");
            });
    }
}
