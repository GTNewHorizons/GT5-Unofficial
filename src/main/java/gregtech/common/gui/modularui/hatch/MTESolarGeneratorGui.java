package gregtech.common.gui.modularui.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
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
import gregtech.common.tileentities.generators.MTESolarGenerator;

public class MTESolarGeneratorGui extends MTETieredMachineBlockBaseGui<MTESolarGenerator> {

    public MTESolarGeneratorGui(MTESolarGenerator machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(18);

        mainRow.child(createStatusColumn(syncManager));

        mainRow.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(2)
                .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CHARGER))
                .build());

        return super.createContentSection(panel, syncManager).child(mainRow)
            .child(
                createLogo().topRel(0)
                    .rightRel(0));
    }

    private Flow createStatusColumn(PanelSyncManager syncManager) {
        BooleanSyncValue daySyncer = new BooleanSyncValue(machine::isDayTime);
        BooleanSyncValue rainSyncer = new BooleanSyncValue(machine::isNoRain);
        BooleanSyncValue skySyncer = new BooleanSyncValue(machine::isSeesSky);
        syncManager.syncValue("day", daySyncer);
        syncManager.syncValue("rain", rainSyncer);
        syncManager.syncValue("sky", skySyncer);

        Flow statusColumn = Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START);

        statusColumn.child(createStatusRow(daySyncer, "GT5U.machines.solarindicator1"));
        statusColumn.child(createStatusRow(rainSyncer, "GT5U.machines.solarindicator2"));
        statusColumn.child(createStatusRow(skySyncer, "GT5U.machines.solarindicator3"));

        return statusColumn;
    }

    private Flow createStatusRow(BooleanSyncValue statusSyncer, String langKey) {
        Flow statusRow = Flow.row()
            .coverChildren()
            .childPadding(2);

        // status icon
        statusRow.child(
            new DynamicDrawable(
                () -> statusSyncer.getBoolValue() ? GTGuiTextures.OVERLAY_BUTTON_CHECKMARK
                    : GTGuiTextures.OVERLAY_BUTTON_CROSS).asWidget());

        // status label
        statusRow.child(
            IKey.lang(langKey)
                .asWidget());

        return statusRow;
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected boolean supportsBottomRightCornerFlow() {
        return false;
    }

    @Override
    protected boolean supportsBottomLeftCornerFlow() {
        return false;
    }

    @Override
    protected ParentWidget<?> createBottomSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomSection(panel, syncManager).child(createEUColumn(syncManager).center());
    }

    private Flow createEUColumn(PanelSyncManager syncManager) {
        LongSyncValue storedSyncer = new LongSyncValue(baseMetaTileEntity::getStoredEU);
        syncManager.syncValue("storedEU", storedSyncer);

        Flow column = Flow.column()
            .coverChildren()
            .childPadding(1);

        // EU amount label
        column.child(
            IKey.dynamic(
                () -> formatNumber(storedSyncer.getLongValue()) + "/"
                    + formatNumber(baseMetaTileEntity.getEUCapacity())
                    + " EU")
                .asWidget());

        // EU amount bars
        column.child(
            new ProgressWidget().value(
                new DoubleSyncValue(() -> (double) storedSyncer.getLongValue() / baseMetaTileEntity.getEUCapacity()))
                .texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
                .size(147, 5));

        return column;
    }
}
