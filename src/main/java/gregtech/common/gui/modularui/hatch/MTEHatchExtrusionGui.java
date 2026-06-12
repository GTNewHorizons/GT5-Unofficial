package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.GhostShapeSlotWidget;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;

public class MTEHatchExtrusionGui extends MTEHatchBaseGui<MTEHatchExtrusion> {

    private static final int COLS = 9;

    public MTEHatchExtrusionGui(MTEHatchExtrusion hatch) {
        super(hatch);
    }

    protected int getRows() {
        return 4 + (machine.mTier - 5) * 2;
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + Math.max(0, (getRows() - 4) * SLOT_SIZE);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createItemSlots(syncManager));
    }

    protected Grid createItemSlots(PanelSyncManager syncManager) {
        int itemSlots = machine.getSizeInventory() - 2;
        int rows = (itemSlots + COLS - 1) / COLS;

        return new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(COLS, rows)
            .build();
    }

    private boolean isShape(ItemStack itemStack) {
        return Arrays.stream(MTEHatchExtrusion.extruderShapes)
            .anyMatch(shape -> GTUtility.areStacksEqual(shape, itemStack, true));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {

        GhostShapeSlotWidget shapeSlot = new GhostShapeSlotWidget(machine, syncManager);
        shapeSlot.slot(new ModularSlot(machine.inventoryHandler, machine.shapeSlot) {

            @Override
            public int getItemStackLimit(@NotNull ItemStack stack) {
                return 1;
            }
        }.singletonSlotGroup()
            .filter(this::isShape));

        BooleanSyncValue stackSync = new BooleanSyncValue(() -> !machine.disableSort, v -> machine.disableSort = !v)
            .allowC2S();

        BooleanSyncValue insertionSync = new BooleanSyncValue(
            () -> !machine.disableLimited,
            v -> machine.disableLimited = !v).allowC2S();

        return super.createBottomLeftCornerFlow(panel, syncManager).child(shapeSlot)
            .child(
                CommonButtons.createToggleButton(
                    stackSync,
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    "GT5U.machines.sorting_mode.tooltip"))
            .child(
                CommonButtons.createToggleButton(
                    insertionSync,
                    GTGuiTextures.OVERLAY_BUTTON_ONE_STACK_LIMIT,
                    "GT5U.machines.one_stack_limit.tooltip"));
    }
}
