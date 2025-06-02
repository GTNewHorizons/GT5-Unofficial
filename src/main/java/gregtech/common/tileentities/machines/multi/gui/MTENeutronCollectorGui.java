package gregtech.common.tileentities.machines.multi.gui;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTENeutronCollector;

public class MTENeutronCollectorGui extends MTEMultiBlockBaseGui {

    private final MTENeutronCollector base;

    public MTENeutronCollectorGui(MTENeutronCollector base) {
        super(base);
        this.base = base;
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        setMachineModeIcons();
        registerSyncValues(syncManager);

        ModularPanel panel = new ModularPanel("MTEMultiBlockBase").size(198, 181)
            .padding(4);
        return panel.child(
            new Column().sizeRel(1)
                .child(createTitleTextStyle(base.getLocalName())));
    }
}
