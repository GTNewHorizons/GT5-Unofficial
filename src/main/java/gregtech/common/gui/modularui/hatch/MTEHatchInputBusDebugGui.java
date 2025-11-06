package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;

public class MTEHatchInputBusDebugGui {

    MTEHatchInputBusDebug base;

    public MTEHatchInputBusDebugGui(MTEHatchInputBusDebug base) {
        this.base = base;
    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        return GTGuis.mteTemplatePanelBuilder(base, data, syncManager, uiSettings)
            .doesAddGregTechLogo(false)
            .build()
            .child(
                SlotGroupWidget.builder()
                    .matrix("IIII", "IIII", "IIII", "IIII")
                    .key('I', index -> {
                        return new PhantomItemSlot().slot(
                            new ModularSlot(base.phantomHolder, index).singletonSlotGroup(50 + index)
                                .accessibility(true, false));
                    })
                    .build()
                    .marginTop(4)
                    .align(Alignment.TopCenter))
            .child(createInfoButton().posRel(0.05f, 0.4f));
    }

    private IDrawable.DrawableWidget createInfoButton() {
        return new IDrawable.DrawableWidget(GuiTextures.BUBBLE).background(GTGuiTextures.BUTTON_STANDARD)
            .size(18)
            .tooltip(t -> {
                t.addLine("Drag Items into the Item Slots");
                t.addLine("Items in the slots will not be consumed");
            });
    }
}
