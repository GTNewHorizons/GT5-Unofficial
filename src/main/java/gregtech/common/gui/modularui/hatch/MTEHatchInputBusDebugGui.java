package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEHatchInputBusDebugGui extends MTEHatchBaseGui<MTEHatchInputBusDebug> {

    public MTEHatchInputBusDebugGui(MTEHatchInputBusDebug base) {
        super(base);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.phantomHolder, syncManager).size(4)
                .itemSlotSupplier(PhantomItemSlot::new)
                .canTake(false)
                .build()
                .center());
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager).child(createInfoButton());
    }

    private IDrawable.DrawableWidget createInfoButton() {
        return new IDrawable.DrawableWidget(GuiTextures.BUBBLE).background(GTGuiTextures.BUTTON_STANDARD)
            .size(18)
            .addTooltipLine("Drag Items into the Item Slots")
            .addTooltipLine("Items in the slots will not be consumed");
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
