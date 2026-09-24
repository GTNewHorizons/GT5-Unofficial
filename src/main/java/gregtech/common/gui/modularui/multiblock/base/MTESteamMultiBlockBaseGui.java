package gregtech.common.gui.modularui.multiblock.base;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.widget.SteamGaugeWidget;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBlockBase;

public class MTESteamMultiBlockBaseGui extends MTEMultiBlockBaseGui<MTESteamMultiBlockBase<?>> {

    public MTESteamMultiBlockBaseGui(MTESteamMultiBlockBase<?> multiblock) {
        super(multiblock);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        IntSyncValue steamStoredSyncer = new IntSyncValue(multiblock::getTotalSteamStored);
        syncManager.syncValue("steamStored", steamStoredSyncer);
        IntSyncValue maxSteamSyncer = new IntSyncValue(multiblock::getTotalSteamCapacity);
        syncManager.syncValue("maxSteam", maxSteamSyncer);

        return super.build(guiData, syncManager, uiSettings).child(
            new SteamGaugeWidget(steamStoredSyncer, maxSteamSyncer).rightRel(1)
                .top(8));
    }
}
