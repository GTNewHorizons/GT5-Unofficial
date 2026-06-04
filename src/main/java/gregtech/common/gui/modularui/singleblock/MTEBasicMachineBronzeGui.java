package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.metatileentity.implementations.MTEBasicMachineBronze;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import gregtech.common.gui.modularui.widget.SteamGaugeWidget;

public class MTEBasicMachineBronzeGui extends MTEBasicMachineBaseGui<MTEBasicMachineBronze> {

    public MTEBasicMachineBronzeGui(MTEBasicMachineBronze machine, BasicUIProperties properties) {
        super(machine, properties);
        useGregTechLogo(true);
    }

    @Override
    protected boolean supportsChargerSlot() {
        return false;
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        LongSyncValue steamStoredSyncer = new LongSyncValue(() -> machine.getSteamVar() * 2);
        syncManager.syncValue("steamStored", steamStoredSyncer);
        LongSyncValue maxSteamSyncer = new LongSyncValue(() -> machine.maxSteamStore() * 2);
        syncManager.syncValue("maxSteam", maxSteamSyncer);

        return super.build(guiData, syncManager, uiSettings).child(
            new SteamGaugeWidget(steamStoredSyncer, maxSteamSyncer).rightRel(1)
                .top(8));
    }
}
