package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatEnergy;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer;

public class MTEFusionComputerGui extends MTEMultiBlockBaseGui<MTEFusionComputer> {

    final LongSyncValue storedEUSyncer;
    final LongSyncValue euCapacitySyncer;

    public MTEFusionComputerGui(MTEFusionComputer multiblock) {
        super(multiblock);
        storedEUSyncer = new LongSyncValue(null, multiblock::getStoredEU);
        euCapacitySyncer = new LongSyncValue(null, multiblock::getEUCapacity);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("euStore", storedEUSyncer);
        syncManager.syncValue("euCapacity", euCapacitySyncer);
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .paddingRight(2)
            .paddingLeft(4)
            .height(getTextBoxToInventoryGap())
            .child(createLeftPanelGapRow(parent, syncManager).paddingRight(4))
            .child(
                Flow.row()
                    .reverseLayout()
                    .expanded()
                    .child(
                        // For some reason I couldn't get it to work without this nesting
                        Flow.row()
                            .coverChildrenWidth()
                            .child(createRightPanelGapRow(parent, syncManager)))
                    .child(createProgressWidget()));
    }

    private double getEnergyRatio() {
        return (double) storedEUSyncer.getLongValue() / euCapacitySyncer.getLongValue();
    }

    private ProgressWidget createProgressWidget() {
        ProgressWidget progress = new ProgressWidget().texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
            .height(5)
            .value(new DoubleValue.Dynamic(this::getEnergyRatio, null))
            .expanded();
        progress.tooltipDynamic(tooltip -> {
            tooltip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.fusion.stored_eu",
                    formatEnergy(storedEUSyncer.getLongValue()),
                    formatNumber(getEnergyRatio() * 100)));
            tooltip.markDirty();
        });
        return progress;
    }
}
