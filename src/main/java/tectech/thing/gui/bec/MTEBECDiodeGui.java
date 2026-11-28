package tectech.thing.gui.bec;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import tectech.thing.metaTileEntity.multi.bec.MTEBECDiode;

public class MTEBECDiodeGui extends MTEBECMultiblockBaseGui<MTEBECDiode> {

    public MTEBECDiodeGui(MTEBECDiode multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent).child(createCondensateWidget(syncManager, parent));
    }
}
