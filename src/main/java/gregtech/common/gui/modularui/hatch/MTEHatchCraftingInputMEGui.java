package gregtech.common.gui.modularui.hatch;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.common.tileentities.machines.MTEHatchCraftingInputME.SLOT_MANUAL_START;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.PatternSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;

public class MTEHatchCraftingInputMEGui extends MTEHatchBaseGui<MTEHatchCraftingInputME> {

    private static final String PATTERN_INV_NAME = "pattern_inv";
    private static final String MANUAL_ITEM_INV_NAME = "manual_item_inv";
    private static final int PATTERN_SLOT_ROW = 4;
    private static final int PATTERN_SLOT_PER_ROW = 9;
    private static final int MANUAL_SLOT_ROW = 3;
    private static final int MANUAL_SLOT_PER_ROW = 3;

    public MTEHatchCraftingInputMEGui(MTEHatchCraftingInputME hatch) {
        super(hatch);
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + SLOT_SIZE;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createSlots(syncManager));
    }

    private Grid createSlots(PanelSyncManager syncManager) {
        syncManager.registerSlotGroup(PATTERN_INV_NAME, PATTERN_SLOT_ROW);

        return new Grid().coverChildren()
            .gridOfWidthHeight(
                PATTERN_SLOT_PER_ROW,
                PATTERN_SLOT_ROW,
                ($x, $y, index) -> new PatternSlot()
                    .slot(new ModularSlot(machine.inventoryHandler, index).changeListener((itemStack, _, client, _) -> {
                        if (!client) {
                            machine.onPatternChange(index, itemStack);
                        }
                    })
                        .slotGroup(PATTERN_INV_NAME)));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager).child(createOptimizerButton())
            .child(createShowPatternButton())
            .child(createExportButton())
            .child(createDoublePatternButton())
            .child(createManualItemsButton(syncManager));
    }

    private ToggleButton createOptimizerButton() {
        BooleanSyncValue optimizerSync = new BooleanSyncValue(
            () -> !machine.disablePatternOptimization,
            val -> machine.disablePatternOptimization = !val).allowC2S();

        return new ToggleButton().value(optimizerSync)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_PATTERN_OPTIMIZE)
            .addTooltipLine(GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.optimize_pattern"))
            .addTooltip(true, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.optimize_pattern.enable"))
            .addTooltip(false, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.optimize_pattern.disabled"));
    }

    private ToggleButton createShowPatternButton() {
        BooleanSyncValue showPatternSync = new BooleanSyncValue(
            () -> machine.showPattern,
            val -> machine.showPattern = val).allowC2S();

        return new ToggleButton().value(showPatternSync)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
            .addTooltip(true, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.show_pattern.enable"))
            .addTooltip(false, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.show_pattern.disabled"));
    }

    private ButtonWidget<?> createExportButton() {
        InteractionSyncHandler exportSyncHandler = new InteractionSyncHandler().setOnMousePressed(mouseDelta -> {
            if (!mouseDelta.isClient() && mouseDelta.mouseButton == 0) {
                machine.refundAll(false);
            }
        });

        return new ButtonWidget<>().syncHandler(exportSyncHandler)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT)
            .addTooltipLine(GTUtility.translate("GT5U.gui.tooltip.hatch.crafting_input_me.export"));
    }

    private ButtonWidget<?> createDoublePatternButton() {
        InteractionSyncHandler doubleSyncHandler = new InteractionSyncHandler().setOnMousePressed(mouseDelta -> {
            if (!mouseDelta.isClient()) {
                int val = mouseDelta.shift ? 1 : 0;
                if (mouseDelta.mouseButton == 1) val |= 0b10;
                machine.doublePatterns(val);
            }
        });

        return new ButtonWidget<>().syncHandler(doubleSyncHandler)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_X2)
            .addTooltipLine(GTUtility.translate("gui.tooltips.appliedenergistics2.DoublePatterns"));
    }

    private ButtonWidget<?> createManualItemsButton(PanelSyncManager syncManager) {
        IPanelHandler popupPanel = syncManager
            .syncedPanel("manual_slots_panel", true, (manager, handler) -> createManualSlotUI(manager));

        return new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_PLUS_LARGE)
            .addTooltipLine(GTUtility.translate("GT5U.gui.tooltip.hatch.crafting_input_me.place_manual_items"))
            .onMousePressed(mouseButton -> {
                popupPanel.openPanel();
                return popupPanel.isPanelOpen();
            });
    }

    private ModularPanel createManualSlotUI(PanelSyncManager syncManager) {
        return createPopUpPanel("manual_slots_panel").size(68, 76)
            .bottomRelOffset(0.5f, 52)
            .child(
                new ItemSlotGridBuilder(machine.inventoryHandler, syncManager)
                    .size(MANUAL_SLOT_PER_ROW, MANUAL_SLOT_ROW)
                    .slotGroupKey(MANUAL_ITEM_INV_NAME)
                    .indexOffset(SLOT_MANUAL_START)
                    .build()
                    .marginTop(16)
                    .horizontalCenter());
    }
}
