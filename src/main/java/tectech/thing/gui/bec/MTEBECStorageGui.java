package tectech.thing.gui.bec;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;
import tectech.thing.metaTileEntity.multi.bec.MTEBECStorage;

public class MTEBECStorageGui extends MTEBECMultiblockBaseGui<MTEBECStorage> {

    public MTEBECStorageGui(MTEBECStorage multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent)
            .child(createCondensateWidget(syncManager, parent, multiblock::getContents));
    }

    @Override
    protected SettingsPanelBuilder getSettingsPanelBuilder() {
        return super.getSettingsPanelBuilder().addReadout(
            IKey.lang("GT5U.gui.text.bec-stored"),
            new DoubleSyncValue(multiblock::getAmountStored),
            amount -> IKey.str(NumberFormatUtil.formatFluid(amount)));
    }
}
