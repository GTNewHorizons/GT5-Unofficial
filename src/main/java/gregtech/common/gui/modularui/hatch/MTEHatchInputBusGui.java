package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;

public class MTEHatchInputBusGui {

    private MTEHatchInputBus hatch;
    private int GUI_WIDTH;
    private int GUI_HEIGHT;
    public MTEHatchInputBusGui(MTEHatchInputBus hatch)
    {
        this.hatch = hatch;
        this.GUI_WIDTH = this.hatch.getGUIWidth();
        this.GUI_HEIGHT = this.hatch.getGUIHeight();

    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings)
    {
        BooleanSyncValue sortStacksSync = new BooleanSyncValue(()-> !hatch.disableSort, val-> hatch.disableSort = !val);
        BooleanSyncValue stackLimitSyncer = new BooleanSyncValue(()-> !hatch.disableLimited, val -> hatch.disableLimited = !val);

        Flow buttonRow = Flow.row().coverChildren().alignY(0.4f);
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(hatch, data, syncManager, uiSettings).build().padding(4);
        panel.size(GUI_WIDTH,GUI_HEIGHT);
        return panel;
    }

    private ToggleButton createToggleButton(BooleanSyncValue value, GTGuiTextures texture, String key)
    {
        return new ToggleButton();
    }



}
