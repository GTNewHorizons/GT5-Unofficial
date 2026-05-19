package gregtech.common.gui.modularui.singleblock;

import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEBufferBaseGui;
import gregtech.common.tileentities.automation.MTERegulator;

public class MTERegulatorGui extends MTEBufferBaseGui<MTERegulator> {

    public MTERegulatorGui(MTERegulator machine) {
        super(machine);
    }

    @Override
    protected boolean supportsSortStacks() {
        return false;
    }

    @Override
    protected boolean supportsEmitRedstone() {
        return false;
    }

    @Override
    protected boolean supportsInvertRedstone() {
        return false;
    }

    @Override
    protected boolean supportsStocking() {
        return false;
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow corner = super.createLeftCornerFlow(panel, syncManager);

        corner.child(createChargerSlot().marginLeft(18));

        // arrow
        corner.child(
            GTGuiTextures.PICTURE_ARROW_22_RED.asWidget()
                .size(86, 22)
                .marginBottom(1)
                .marginLeft(1));

        return corner;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue[] targetSlotSyncers = IntStream.range(0, 9)
            .mapToObj(i -> syncManager.findSyncHandler("targetSlot", i, IntSyncValue.class))
            .toArray(IntSyncValue[]::new);

        Flow mainRow = Flow.row()
            .marginLeft(4)
            .coverChildren();

        // main inventory grid
        mainRow.child(
            new Grid().coverChildren()
                .gridOfWidthHeight(
                    3,
                    3,
                    ($x, $y, index) -> new ItemSlot()
                        .slot(new ModularSlot(machine.inventoryHandler, index).slotGroup("item_inv"))));

        // filter inventory grid
        mainRow.child(
            new Grid().coverChildren()
                .gridOfWidthHeight(
                    3,
                    3,
                    ($x, $y, index) -> new PhantomItemSlot().slot(new ModularSlot(machine.inventoryHandler, index + 9))
                        .disableThemeBackground(true)
                        .disableHoverThemeBackground(true))
                .background(GTGuiTextures.PICTURE_SLOTS_HOLO_3BY3));

        // target slot selector grid
        mainRow.child(
            new Grid().gridOfWidthHeight(3, 3, ($x, $y, index) -> new ButtonWidget<>().onMousePressed(mouseButton -> {
                targetSlotSyncers[index].setIntValue(
                    GTUtility.clamp(
                        targetSlotSyncers[index].getIntValue()
                            + (mouseButton == 0 ? 1 : -1) * (Interactable.hasShiftDown() ? 16 : 1),
                        0,
                        99));

                return true;
            })
                .disableThemeBackground(true)
                .disableHoverThemeBackground(true)
                .overlay(getTargetSlotText(targetSlotSyncers[index]))
                .hoverOverlay(
                    getTargetSlotText(targetSlotSyncers[index]),
                    new Rectangle().color(Color.withAlpha(Color.WHITE.main, 0x60))
                        .asIcon()
                        .size(16)))
                .coverChildren()
                .background(GTGuiTextures.PICTURE_SLOTS_HOLO_3BY3));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private static @NotNull IKey getTargetSlotText(IntSyncValue targetSlotSyncer) {
        return IKey.dynamic(() -> String.valueOf(targetSlotSyncer.getIntValue()));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("item_inv", 3);

        IntStream.range(0, 9)
            .forEach(
                i -> syncManager.syncValue(
                    "targetSlot",
                    i,
                    new IntSyncValue(() -> machine.getTargetSlot(i), val -> machine.setTargetSlots(val, i))));
    }
}
