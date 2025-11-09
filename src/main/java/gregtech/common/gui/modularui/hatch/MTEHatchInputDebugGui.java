package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.metatileentity.implementations.MTEHatchInputDebug;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;

public class MTEHatchInputDebugGui {

    public MTEHatchInputDebug base;

    public MTEHatchInputDebugGui(MTEHatchInputDebug base) {
        this.base = base;
    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        for (int i = 0; i < base.fluidTankList.length; i++) {
            syncManager.syncValue(
                "fluidTanks",
                i,
                SyncHandlers.fluidSlot(base.fluidTankList[i])
                    .phantom(true)
                    .canDrainSlot(true)
                    .canFillSlot(true)
                    .controlsAmount(false));
        }
        return GTGuis.mteTemplatePanelBuilder(base, data, syncManager, uiSettings)
            .doesAddGregTechLogo(false)
            .build()
            .child(
                SlotGroupWidget.builder()
                    .matrix("IIII", "IIII", "IIII", "IIII")
                    .key('I', index -> { return new FluidSlot().syncHandler("fluidTanks", index); })
                    .build()
                    .marginTop(4)
                    .align(Alignment.TopCenter))
            .child(createInfoButton().posRel(0.05f, 0.4f));
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
