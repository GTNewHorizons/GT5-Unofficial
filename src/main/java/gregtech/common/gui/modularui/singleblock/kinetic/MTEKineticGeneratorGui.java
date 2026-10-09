package gregtech.common.gui.modularui.singleblock.kinetic;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gregtech.common.tileentities.generators.MTEKineticGenerator;

public class MTEKineticGeneratorGui extends MTETieredMachineBlockBaseGui<MTEKineticGenerator> {

    private static final int KU_METER_WIDTH = 110;

    public MTEKineticGeneratorGui(MTEKineticGenerator machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.col()
            .child(mainSection(syncManager))
            .child(createEUBar(syncManager));
    }

    private Flow mainSection(PanelSyncManager syncManager) {
        LongSyncValue kuSyncer = new LongSyncValue(machine::getKuIn);
        syncManager.syncValue("kuIn", kuSyncer);

        return Flow.row()
            .childPadding(8)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .height(52)
            .child(
                Flow.col()
                    .width(KU_METER_WIDTH)
                    .mainAxisAlignment(Alignment.MainAxis.CENTER)
                    .childPadding(2)
                    .child(
                        IKey.dynamic(
                            () -> formatNumber(kuSyncer.getLongValue()) + "/" + formatNumber(machine.maxKU()) + " KU")
                            .alignment(Alignment.CENTER)
                            .asWidget()
                            .height(12)
                            .fullWidth()
                            .horizontalCenter())
                    .child(
                        new ProgressWidget()
                            .value(new DoubleSyncValue(() -> (double) kuSyncer.getLongValue() / machine.maxKU()))
                            .texture(GTGuiTextures.PROGRESSBAR_KU_METER, KU_METER_WIDTH)
                            .size(KU_METER_WIDTH, 24))
                    .marginBottom(3))
            .child(
                new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(2)
                    .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CHARGER))
                    .build());
    }

    private ParentWidget<?> createEUBar(PanelSyncManager syncManager) {
        LongSyncValue euSyncer = new LongSyncValue(baseMetaTileEntity::getStoredEU);
        syncManager.syncValue("storedEu", euSyncer);

        return new ParentWidget<>().fullWidth()
            .coverChildrenHeight()
            .child(
                Flow.col()
                    .horizontalCenter()
                    .coverChildren()
                    .childPadding(2)
                    .child(
                        IKey.dynamic(
                            () -> formatNumber(euSyncer.getLongValue()) + "/"
                                + formatNumber(baseMetaTileEntity.getEUCapacity())
                                + " EU")
                            .asWidget()
                            .height(10))
                    .child(
                        new ProgressWidget()
                            .value(
                                new DoubleSyncValue(
                                    () -> (double) euSyncer.getLongValue() / baseMetaTileEntity.getEUCapacity()))
                            .texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
                            .size(147, 5)));
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }
}
