package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchSplitterRedstone;

public class MTEHatchSplitterRedstoneGui extends MTEHatchBaseGui<MTEHatchSplitterRedstone> {

    public MTEHatchSplitterRedstoneGui(MTEHatchSplitterRedstone hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue redstone = syncManager.findSyncHandler("redstone", IntSyncValue.class);
        IntSyncValue channel = syncManager.findSyncHandler("channel", IntSyncValue.class);
        return super.createContentSection(panel, syncManager).child(
            new Column().coverChildren()
                .child(
                    new Row().child(new ButtonWidget<>())
                        .child(
                            IKey.dynamic(channel::getStringValue)
                                .asWidget())
                        .child(new ButtonWidget<>()))
                .child(
                    IKey.dynamic(redstone::getStringValue)
                        .asWidget())

        );
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        IntSyncValue redstone = new IntSyncValue(hatch::getRedstoneInput);
        IntSyncValue channel = new IntSyncValue(hatch::getChannel, hatch::setChannel);
        syncManager.syncValue("redstone", redstone);
        syncManager.syncValue("channel", channel);
        super.registerSyncValues(syncManager);
    }
}
