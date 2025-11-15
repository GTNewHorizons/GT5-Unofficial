package gregtech.common.gui.modularui.multiblock.godforge.panel;

import java.util.Arrays;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.Dialog;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;

public class ManualInsertionPanel {

    private static final int SIZE_W = 189;
    private static final int SIZE_H = 106;

    public static Dialog<?> openDialog(SyncHypervisor hypervisor) {
        Dialog<?> dialog = hypervisor.getDialog(Panels.MANUAL_INSERTION);

        registerSyncValues(hypervisor);

        ModularPanel mainPanel = hypervisor.getModularPanel(Panels.MAIN);
        dialog.setDisablePanelsBelow(true)
            .setDraggable(true)
            .relative(mainPanel)
            .leftRelOffset(0, 5)
            .topRelOffset(0, 5)
            .size(SIZE_W, SIZE_H)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .disableHoverBackground();

        dialog.child(createInputSlots(hypervisor));

        return dialog;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {

    }

    private static SlotGroupWidget createInputSlots(SyncHypervisor hypervisor) {
        PanelSyncManager syncManager = hypervisor.getSyncManager(Panels.MANUAL_INSERTION);
        syncManager.registerSlotGroup("item_inv_manual_insertion", 4);

        ItemStackHandler handler = new ItemStackHandler(
            hypervisor.getData()
                .getStoredUpgradeWindowItems());

        String[] matrix = new String[4];
        String repeat = StringUtils.getRepetitionOf('s', 4);
        Arrays.fill(matrix, repeat);
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key('s', i -> new ItemSlot().slot(new ModularSlot(handler, i).slotGroup("item_inv_manual_insertion")))
            .build()
            .coverChildren()
            .align(Alignment.TopRight)
            .marginTop(6)
            .marginRight(5);
    }
}
