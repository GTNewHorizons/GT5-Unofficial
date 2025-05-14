package goodgenerator.blocks.tileEntity.gui;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import net.minecraft.util.StatCollector;
import tectech.thing.metaTileEntity.multi.base.gui.TTMultiBlockBaseGui;

import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.numberFormat;

public class MTELargeFusionComputerGui extends TTMultiBlockBaseGui {
    public MTELargeFusionComputerGui(MTEMultiBlockBase base) {
        super(base);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        LongSyncValue storedEnergySyncer = new LongSyncValue(base::getEUVar, base::setEUVar);
        LongSyncValue energyCapacitySyncer = new LongSyncValue(base::maxEUStore);
        syncManager.syncValue("storedEnergy", storedEnergySyncer);
        syncManager.syncValue("energyCapacity", energyCapacitySyncer);

        return super.createTerminalTextWidget(syncManager)
            .child(
                IKey.dynamic(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.0") + " "
                            + numberFormat.format(storedEnergySyncer.getValue())
                            + " EU")
                    .asWidget()
                    .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(w -> base.getErrorDisplayID() == 0))
            .child(
                IKey.dynamic(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.1") + " "
                            + numberFormat.format(energyCapacitySyncer.getValue())
                            + " EU")
                    .asWidget()
                    .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(w -> base.getErrorDisplayID() == 0));
    }
}
