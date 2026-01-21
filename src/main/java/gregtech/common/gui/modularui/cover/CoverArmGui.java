package gregtech.common.gui.modularui.cover;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverArm;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverArmGui extends CoverBaseGui<CoverArm> {

    public CoverArmGui(CoverArm cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {

        column.child(
            makeRowLayout().child(positionRow(createTransferModeRow(syncManager)))
                .child(positionRow(createInternalSlotRow()))
                .child(positionRow(createExternalSlotRow())));
    }

    private Flow createTransferModeRow(PanelSyncManager syncManager) {
        BooleanSyncValue isExportSyncer = new BooleanSyncValue(cover::isExport, cover::setExport);
        syncManager.syncValue("isExport", isExportSyncer);
        return Flow.row()
            .child(createExportButton(isExportSyncer))
            .child(createImportButton(isExportSyncer))
            .child(createTransferModeLabel());
    }

    private IWidget createExportButton(BooleanSyncValue isExportSyncer) {
        return new SelectButton().value(LinkedBoolValue.of(isExportSyncer, true))
            .size(16)
            .marginRight(2)
            .overlay(GTGuiTextures.OVERLAY_EXPORT);
    }

    private IWidget createImportButton(BooleanSyncValue isExportSyncer) {
        return new SelectButton().value(LinkedBoolValue.of(isExportSyncer, false))
            .size(16)
            .marginRight(2)
            .overlay(GTGuiTextures.OVERLAY_IMPORT);
    }

    private IWidget createTransferModeLabel() {
        return IKey
            .dynamic(
                () -> cover.isExport() ? StatCollector.translateToLocal("gt.interact.desc.export")
                    : StatCollector.translateToLocal("gt.interact.desc.import"))
            .asWidget()
            .marginLeft(14);
    }

    private Flow createInternalSlotRow() {
        return Flow.row()
            .child(
                // number field with 'Any' goes here
                makeNumberField(50)
                    .value(
                        new IntSyncValue(() -> cover.getInternalSlotId() - 1, val -> cover.setInternalSlotId(val + 1)))
                    .setDefaultNumber(-1)
                    .setNumbers(
                        -1,
                        cover.getTile()
                            .getSizeInventory() - 1)
                    .marginRight(2))
            .child(
                IKey.lang("gt.interact.desc.arm.Internal_Slot")
                    .asWidget()
                    .tooltipBuilder(t -> t.addLine("-1 means 'Any' sorry for the inconvenience :(")));
    }

    private Flow createExternalSlotRow() {
        return Flow.row()
            .child(
                // number field with 'Any' goes here
                makeNumberField(50)
                    .value(
                        new IntSyncValue(() -> cover.getExternalSlotId() - 1, val -> cover.setExternalSlotId(val + 1)))
                    .setDefaultNumber(-1)
                    .setValidator(this::validateExternalSlotId)
                    .marginRight(2))
            .child(
                IKey.lang("gt.interact.desc.arm.Adjacent_Slot")
                    .asWidget()
                    .tooltipBuilder(t -> t.addLine("-1 means 'Any' sorry for the inconvenience :(")));

    }

    private String validateExternalSlotId(String proposedSlotId) {
        int slotId;
        try {
            slotId = Integer.parseInt(proposedSlotId);
        } catch (Exception e) {
            return "-1";
        }

        int adjacentMaxSlot = getMaxExternalSlot();

        return String.valueOf(Math.max(-1, Math.min(slotId, adjacentMaxSlot)));
    }

    // Stolen from ArmUIFactory to check what the max slot id is for the adjacent tile
    private int getMaxExternalSlot() {
        final ICoverable tile = cover.getTile();
        int adjacentMaxSlot = -1;

        if (tile instanceof TileEntity && !tile.isDead()) {
            TileEntity adj = tile.getTileEntityAtSide(cover.getSide());
            if (adj instanceof IInventory adjInv) {
                adjacentMaxSlot = adjInv.getSizeInventory() - 1;
            }
        }

        return adjacentMaxSlot;
    }
}
