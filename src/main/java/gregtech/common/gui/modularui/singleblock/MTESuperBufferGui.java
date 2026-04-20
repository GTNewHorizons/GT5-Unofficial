package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.singleblock.base.MTEBufferBaseGui;
import gregtech.common.tileentities.automation.MTESuperBuffer;

public class MTESuperBufferGui extends MTEBufferBaseGui<MTESuperBuffer> {

    public MTESuperBufferGui(MTESuperBuffer machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            GTGuiTextures.PICTURE_SUPER_BUFFER.asWidget()
                .size(54)
                .horizontalCenter());
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftCornerFlow(panel, syncManager).child(
            createArrow(GTGuiTextures.PICTURE_ARROW_22_RED, 50, 22).marginBottom(1)
                .marginLeft(1));
    }
}
