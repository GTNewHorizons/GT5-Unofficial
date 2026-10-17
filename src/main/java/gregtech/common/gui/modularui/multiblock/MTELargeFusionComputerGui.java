package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputer;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;

public class MTELargeFusionComputerGui extends TTMultiblockBaseGui<MTELargeFusionComputer> {

    public MTELargeFusionComputerGui(MTELargeFusionComputer multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue energyCapSyncer = syncManager.findSyncHandler("energyCap", LongSyncValue.class);
        LongSyncValue storedSyncer = syncManager.findSyncHandler("stored", LongSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(
                    () -> StatCollector
                        .translateToLocalFormatted("gui.LargeFusion.0", formatNumber(energyCapSyncer.getLongValue())))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .marginTop(2))
            .child(
                IKey.dynamic(
                    () -> StatCollector
                        .translateToLocalFormatted("gui.LargeFusion.1", formatNumber(storedSyncer.getLongValue())))
                    .color(Color.WHITE.main)
                    .asWidget());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("energyCap", new LongSyncValue(multiblock::maxEUStore));
        syncManager.syncValue("stored", new LongSyncValue(multiblock::getEUVar));
    }
}
