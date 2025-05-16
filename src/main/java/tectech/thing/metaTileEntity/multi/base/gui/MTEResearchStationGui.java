package tectech.thing.metaTileEntity.multi.base.gui;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtility;
import tectech.thing.metaTileEntity.multi.MTEResearchStation;

public class MTEResearchStationGui extends TTMultiBlockBaseGui {

    private final MTEResearchStation station;

    public MTEResearchStationGui(MTEMultiBlockBase base) {
        super(base);
        this.station = (MTEResearchStation) base;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        LongSyncValue requiredComputationSyncer = new LongSyncValue(
            () -> station.computationRequired,
            val -> station.computationRequired = val);
        LongSyncValue remainingcomputationSyncer = new LongSyncValue(
            () -> station.computationRemaining,
            val -> station.computationRemaining = val);
        StringSyncValue outputSyncer = new StringSyncValue(() -> {
            if (station.tRecipe != null && station.tRecipe.mOutput != null) {
                return station.tRecipe.mOutput.getDisplayName();
            }
            return "";
        }, val -> station.clientOutputName = val);
        syncManager.syncValue("requiredComputation", requiredComputationSyncer);
        syncManager.syncValue("remainingComputation", remainingcomputationSyncer);
        syncManager.syncValue("output", outputSyncer);
        return super.createTerminalTextWidget(syncManager)
            .child(
                IKey.dynamic(
                    () -> StatCollector
                        .translateToLocalFormatted("GT5U.gui.text.researching_item", station.clientOutputName))
                    .asWidget()

                    .setEnabledIf(
                        widget -> station.computationRequired > 0 && station.clientOutputName != null
                            && !station.clientOutputName.isEmpty()))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.research_progress",
                        station.getComputationConsumed(),
                        station.getComputationRequired(),
                        GTUtility.formatNumbers(station.getComputationProgress())))
                    .asWidget()
                    .widthRel(1)
                    .marginTop(2)
                    .height(18)
                    .setEnabledIf(
                        widget -> station.computationRequired > 0 && station.clientOutputName != null
                            && !station.clientOutputName.isEmpty()));
    }
}
