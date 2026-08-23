package gregtech.common.gui.modularui.hatch;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.modularui2.widget.GhostShapeSlotWidget;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;

public class MTEHatchExtrusionGui extends MTEHatchInputBusGui {

    public MTEHatchExtrusionGui(MTEHatchExtrusion hatch) {
        super(hatch);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        MTEHatchExtrusion extrusionHatch = (MTEHatchExtrusion) machine;

        GhostShapeSlotWidget shapeSlot = new GhostShapeSlotWidget(extrusionHatch, syncManager);
        shapeSlot.slot(new ModularSlot(machine.inventoryHandler, extrusionHatch.shapeSlot) {

            @Override
            public int getItemStackLimit(@NotNull ItemStack stack) {
                return 1;
            }
        }.singletonSlotGroup());

        return super.createBottomLeftCornerFlow(panel, syncManager).child(shapeSlot);
    }
}
