package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import tectech.thing.metaTileEntity.multi.MTEMicrowave;

public class MTEMicrowaveGui extends TTMultiblockBaseGui<MTEMicrowave> {

    public MTEMicrowaveGui(MTEMicrowave multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue timerSyncer = syncManager.findSyncHandler(MTEMicrowave.TIMER_PARAMETER, IntSyncValue.class);
        IntSyncValue powerSyncer = syncManager.findSyncHandler(MTEMicrowave.POWER_PARAMETER, IntSyncValue.class);
        IntSyncValue remainingSyncer = syncManager.findSyncHandler("timerProgress", IntSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.tm.microwave.damage",
                        formatNumber(multiblock.getDamageForPower(powerSyncer.getIntValue()))))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .marginTop(2))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.tm.microwave.timer",
                        formatNumber(remainingSyncer.getIntValue()),
                        formatNumber(timerSyncer.getIntValue())))
                    .color(Color.WHITE.main)
                    .asWidget());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager
            .syncValue("timerProgress", new IntSyncValue(multiblock::getTimerProgress, multiblock::setTimerProgress));
    }
}
