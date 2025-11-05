package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.modularui2.GTGuis;

public class MTEHatchInputBusDebugGui {

    MTEHatchInputBusDebug base;

    public MTEHatchInputBusDebugGui(MTEHatchInputBusDebug base)
    {
        this.base = base;
    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        return GTGuis.mteTemplatePanelBuilder(base, data, syncManager, uiSettings)
            .doesAddGregTechLogo(true)
            .build()
            .child(IKey.lang("banana").asWidget());
    }
}
