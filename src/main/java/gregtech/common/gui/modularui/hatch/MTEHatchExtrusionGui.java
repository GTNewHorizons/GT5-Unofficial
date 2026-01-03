package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.GhostShapeSlotWidget;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;

public class MTEHatchExtrusionGui extends MTEHatchBaseGui<MTEHatchExtrusion> {

    private static final int COLS = 9;

    public MTEHatchExtrusionGui(MTEHatchExtrusion hatch) {
        super(hatch);
    }

    protected int getRows() {
        return 4 + (hatch.mTier - 5) * 2;
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + Math.max(0, (getRows() - 4) * 18);
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {

        syncManager
            .syncValue("oneStackLimit", new BooleanSyncValue(() -> hatch.oneStackLimit, v -> hatch.oneStackLimit = v));

        syncManager.syncValue("shape", new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(MTEHatchExtrusion.shapeSlot);
            return current != null ? hatch.findMatchingShapeIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < MTEHatchExtrusion.extruderShapes.length) {
                hatch.setShape(MTEHatchExtrusion.extruderShapes[index]);
            } else {
                hatch.setShape(null);
            }
        }));

        return super.createContentSection(panel, syncManager).child(createItemSlots(syncManager));
    }

    protected SlotGroupWidget createItemSlots(PanelSyncManager syncManager) {

        syncManager.registerSlotGroup("item_inv", 1);

        int itemSlots = hatch.getSizeInventory() - 2;
        int rows = (itemSlots + COLS - 1) / COLS;

        String[] matrix = new String[rows];
        Arrays.fill(matrix, "sssssssss");

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key('s', index -> {
                if (index >= itemSlots) return new ItemSlot();

                int actual = index + (index >= MTEHatchExtrusion.shapeSlot ? 1 : 0)
                    + (index >= MTEHatchExtrusion.circuitSlot ? 1 : 0);

                return new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, actual).slotGroup("item_inv"));
            })
            .build()
            .coverChildren()
            .margin(3, 2);
    }

    private ToggleButton createToggleButton(BooleanSyncValue sync, UITexture texture, String tooltipKey) {
        return new ToggleButton().value(sync)
            .overlay(texture)
            .addTooltipLine(GTUtility.translate(tooltipKey));
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {

        syncManager.registerSlotGroup("shape_slot", 1);

        GhostShapeSlotWidget shapeSlot = new GhostShapeSlotWidget(hatch, syncManager);
        shapeSlot.slot(new ModularSlot(hatch.inventoryHandler, MTEHatchExtrusion.shapeSlot).slotGroup("shape_slot"));

        BooleanSyncValue stackSync = new BooleanSyncValue(() -> !hatch.disableSort, v -> hatch.disableSort = !v);

        BooleanSyncValue oneStackSync = new BooleanSyncValue(() -> hatch.oneStackLimit, v -> hatch.oneStackLimit = v);

        return super.createLeftCornerFlow(panel, syncManager).child(shapeSlot)
            .child(
                createToggleButton(
                    stackSync,
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    "GT5U.machines.sorting_mode.tooltip"))
            .child(
                createToggleButton(
                    oneStackSync,
                    GTGuiTextures.OVERLAY_BUTTON_ONE_STACK_LIMIT,
                    "GT5U.machines.one_stack_limit.tooltip"));
    }
}
