package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatEnergy;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputer;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;

public class MTELargeFusionComputerGui extends TTMultiblockBaseGui<MTELargeFusionComputer> {

    public MTELargeFusionComputerGui(MTELargeFusionComputer multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).paddingLeft(4)
            .disableCoverChildrenWidth()
            .expanded()
            .child(createPowerWidget(syncManager));
    }

    @Override
    public boolean showMaxParallelRow() {
        return false;
    }

    private ProgressWidget createPowerWidget(PanelSyncManager syncManager) {
        LongSyncValue energyCapSyncer = syncManager.findSyncHandler("energyCap", LongSyncValue.class);
        LongSyncValue storedSyncer = syncManager.findSyncHandler("stored", LongSyncValue.class);
        DoubleSyncValue ratioSyncer = new DoubleSyncValue(
            () -> (double) storedSyncer.getLongValue() / energyCapSyncer.getLongValue());

        return new ProgressWidget().texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
            .height(5)
            .value(ratioSyncer)
            .expanded()
            .tooltipAutoUpdate(true)
            .tooltipDynamic(
                t -> t.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.fusion.stored_eu",
                        formatEnergy(storedSyncer.getLongValue()),
                        formatNumber(ratioSyncer.getDoubleValue() * 100))));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("energyCap", new LongSyncValue(multiblock::maxEUStore));
        syncManager.syncValue("stored", new LongSyncValue(multiblock::getEUVar));
    }
}
