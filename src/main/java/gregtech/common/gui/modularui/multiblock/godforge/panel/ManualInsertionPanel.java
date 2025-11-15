package gregtech.common.gui.modularui.multiblock.godforge.panel;

import java.util.Arrays;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.Dialog;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncActions;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;

public class ManualInsertionPanel {

    private static final int SIZE_W = 190;
    private static final int SIZE_H = 115;

    private static final int BUTTON_W = 180;
    private static final int BUTTON_H = 18;

    public static Dialog<?> openDialog(SyncHypervisor hypervisor) {
        Dialog<?> dialog = hypervisor.getDialog(Panels.MANUAL_INSERTION);

        registerSyncValues(hypervisor);

        ModularPanel mainPanel = hypervisor.getModularPanel(Panels.MAIN);
        dialog.setDisablePanelsBelow(true)
            .setDraggable(true)
            .relative(mainPanel)
            .leftRelOffset(0, 4)
            .topRelOffset(0, 4)
            .size(SIZE_W, SIZE_H)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .disableHoverBackground();

        // Inputs grid
        dialog.child(createInputSlots(hypervisor));

        EnumSyncValue<ForgeOfGodsUpgrade> upgradeSyncer = SyncValues.UPGRADE_CLICKED
            .lookupFrom(Panels.UPGRADE_TREE, hypervisor);

        // Required inputs and input progress columns
        // todo

        // Consume inputs button
        dialog.child(
            new ButtonWidget<>().background(GTGuiTextures.BUTTON_STANDARD)
                .overlay(
                    IKey.lang("gt.blockmachines.multimachine.FOG.consumeUpgradeMats")
                        .alignment(Alignment.CENTER)
                        .scale(0.75f))
                .disableHoverBackground()
                .disableHoverOverlay()
                .onMousePressed(d -> {
                    SyncActions.PAY_UPGRADE_COST
                        .callFrom(Panels.MANUAL_INSERTION, hypervisor, upgradeSyncer.getValue());
                    return true;
                })
                .size(BUTTON_W, BUTTON_H)
                .align(Alignment.BottomLeft)
                .marginBottom(5)
                .marginLeft(5)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound()));

        return dialog;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncActions.PAY_UPGRADE_COST.registerFor(Panels.MANUAL_INSERTION, hypervisor);
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
