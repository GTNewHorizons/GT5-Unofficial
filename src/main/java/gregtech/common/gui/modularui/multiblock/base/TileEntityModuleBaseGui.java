package gregtech.common.gui.modularui.multiblock.base;

import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.numberFormat;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.util.GTUtility;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleBase;

public class TileEntityModuleBaseGui<T extends TileEntityModuleBase> extends TTMultiblockBaseGui<T> {

    public TileEntityModuleBaseGui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue euVarSyncer = new LongSyncValue(multiblock::getEUVar);
        syncManager.syncValue("euVar", euVarSyncer);
        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(
                () -> GTUtility
                    .translate("tt.spaceelevator.storedEnergy", numberFormat.format(euVarSyncer.getValue()) + " EU"))
                .asWidget()
                .setEnabledIf(
                    w -> multiblock.getBaseMetaTileEntity()
                        .isAllowedToWork()
                        || multiblock.getBaseMetaTileEntity()
                            .isActive())
                .color(Color.WHITE.main)
                .widthRel(1)
                .marginBottom(2));
    }
}
