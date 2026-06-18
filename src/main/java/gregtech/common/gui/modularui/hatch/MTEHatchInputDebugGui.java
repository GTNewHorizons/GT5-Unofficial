package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.metatileentity.implementations.MTEHatchInputDebug;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchInputDebugGui extends MTEHatchBaseGui<MTEHatchInputDebug> {

    public MTEHatchInputDebugGui(MTEHatchInputDebug base) {
        super(base);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager).child(createInfoButton());
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        for (int i = 0; i < machine.fluidTankList.length; i++) {
            syncManager.syncValue(
                "fluidTanks",
                i,
                SyncHandlers.fluidSlot(machine.fluidTankList[i])
                    .phantom(true)
                    .canDrainSlot(true)
                    .canFillSlot(true)
                    .controlsAmount(false));
        }
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new Grid().coverChildren()
                .gridOfWidthHeight(4, 4, ($x, $y, index) -> new FluidSlot().syncHandler("fluidTanks", index))
                .center());
    }

    private IDrawable.DrawableWidget createInfoButton() {
        return new IDrawable.DrawableWidget(GuiTextures.BUBBLE).background(GTGuiTextures.BUTTON_STANDARD)
            .size(18)
            .addTooltipLine("Drag Fluids into the Fluid Slots")
            .addTooltipLine("Fluids in the slots will not be consumed");
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
