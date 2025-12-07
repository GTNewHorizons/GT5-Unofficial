package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchInputBusDebugGui extends MTEHatchBaseGui<MTEHatchInputBusDebug> {

    public MTEHatchInputBusDebugGui(MTEHatchInputBusDebug base) {
        super(base);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            SlotGroupWidget.builder()
                .matrix("IIII", "IIII", "IIII", "IIII")
                .key('I', index -> {
                    return new PhantomItemSlot().slot(
                        new ModularSlot(hatch.phantomHolder, index).singletonSlotGroup(50 + index)
                            .accessibility(true, false));
                })
                .build()
                .marginTop(4)
                .align(Alignment.TopCenter));
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftCornerFlow(panel, syncManager).child(createInfoButton());
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
