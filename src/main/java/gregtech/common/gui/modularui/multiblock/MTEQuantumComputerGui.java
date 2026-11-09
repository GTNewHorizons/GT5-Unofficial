package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import tectech.thing.metaTileEntity.multi.MTEQuantumComputer;

public class MTEQuantumComputerGui extends TTMultiblockBaseGui<MTEQuantumComputer> {

    public MTEQuantumComputerGui(MTEQuantumComputer multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue computationSyncer = syncManager.findSyncHandler("computation", LongSyncValue.class);
        DoubleSyncValue maxHeatSyncer = syncManager.findSyncHandler("maxHeat", DoubleSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.em.computer.cfgo.0",
                        formatNumber(maxHeatSyncer.getDoubleValue())))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .marginTop(2))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.em.computer.cfgo.1",
                        formatNumber(computationSyncer.getLongValue())))
                    .color(Color.WHITE.main)
                    .asWidget());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("computation", new LongSyncValue(multiblock::getAvailableData));
        syncManager.syncValue("maxHeat", new DoubleSyncValue(multiblock::getMaxHeat));
    }
}
