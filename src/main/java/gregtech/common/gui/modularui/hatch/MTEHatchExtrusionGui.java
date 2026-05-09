package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
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

        syncManager.syncValue("shape", new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(hatch.shapeSlot);
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

    protected Grid createItemSlots(PanelSyncManager syncManager) {
        int itemSlots = hatch.getSizeInventory() - 2;
        int rows = (itemSlots + COLS - 1) / COLS;

        syncManager.registerSlotGroup("item_inv", rows);

        return new Grid().coverChildren()
            .gridOfWidthHeight(
                COLS,
                rows,
                ($x, $y, index) -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index) {

                    @Override
                    public int getItemStackLimit(@NotNull ItemStack stack) {
                        return 1;
                    }
                }.slotGroup("item_inv")
                    .filter(this::isShape)))
            .margin(3, 2);
    }

    private boolean isShape(ItemStack itemStack) {
        return Arrays.stream(MTEHatchExtrusion.extruderShapes)
            .anyMatch(shape -> GTUtility.areStacksEqual(shape, itemStack, true));
    }

    private ToggleButton createToggleButton(BooleanSyncValue sync, UITexture texture, String tooltipKey) {
        return new ToggleButton().value(sync)
            .overlay(texture)
            .addTooltipLine(GTUtility.translate(tooltipKey));
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {

        GhostShapeSlotWidget shapeSlot = new GhostShapeSlotWidget(hatch, syncManager);
        shapeSlot.slot(new ModularSlot(hatch.inventoryHandler, hatch.shapeSlot) {

            @Override
            public int getItemStackLimit(@NotNull ItemStack stack) {
                return 1;
            }
        }.singletonSlotGroup()
            .filter(this::isShape));

        BooleanSyncValue stackSync = new BooleanSyncValue(() -> !hatch.disableSort, v -> hatch.disableSort = !v);

        BooleanSyncValue insertionSync = new BooleanSyncValue(
            () -> !hatch.disableLimited,
            v -> hatch.disableLimited = !v);

        return super.createLeftCornerFlow(panel, syncManager).child(shapeSlot)
            .child(
                createToggleButton(
                    stackSync,
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    "GT5U.machines.sorting_mode.tooltip"))
            .child(
                createToggleButton(
                    insertionSync,
                    GTGuiTextures.OVERLAY_BUTTON_ONE_STACK_LIMIT,
                    "GT5U.machines.one_stack_limit.tooltip"));
    }
}
