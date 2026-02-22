package gregtech.common.gui.modularui.multiblock.base;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.widget.CircularGaugeDrawable;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;

public class MTESteamMultiBaseGui extends MTEMultiBlockBaseGui<MTESteamMultiBase<?>> {

    public MTESteamMultiBaseGui(MTESteamMultiBase<?> multiblock) {
        super(multiblock);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        IntSyncValue steamStoredSyncer = new IntSyncValue(multiblock::getTotalSteamStored);
        syncManager.syncValue("steamStored", steamStoredSyncer);
        IntSyncValue maxSteamSyncer = new IntSyncValue(multiblock::getTotalSteamCapacity);
        syncManager.syncValue("maxSteam", maxSteamSyncer);

        return super.build(guiData, syncManager, uiSettings).child(
            IDrawable
                .of(multiblock.getThemeTier() != 2 ? GTGuiTextures.STEAM_GAUGE_BG : GTGuiTextures.STEAM_GAUGE_BG_STEEL)
                .asWidget()
                .size(48, 42)
                .left(-48)
                .top(8)
                .tooltipDynamic(
                    t -> t.addLine(
                        String.format("%s/%sL Steam", steamStoredSyncer.getValue(), maxSteamSyncer.getValue())))
                .tooltipAutoUpdate(true))
            .child(
                new CircularGaugeDrawable(() -> (float) steamStoredSyncer.getValue() / maxSteamSyncer.getValue())
                    .asWidget()
                    .widgetTheme(GTWidgetThemes.STEAM_GAUGE_NEEDLE)
                    .size(18, 4)
                    .left(-48 + 21)
                    .top(8 + 21));
    }
}
