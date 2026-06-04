package gregtech.common.gui.modularui.singleblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEBasicMachineBronze;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import gregtech.common.gui.modularui.widget.SteamGaugeWidget;

public class MTEBasicMachineBronzeGui extends MTEBasicMachineBaseGui<MTEBasicMachineBronze> {

    public MTEBasicMachineBronzeGui(MTEBasicMachineBronze machine, BasicUIProperties properties) {
        super(machine, properties);
        useGregTechLogo(true);
    }

    @Override
    protected ItemSlot createChargerSlot() {
        String[] tooltipKeys = new String[2];
        if (properties.useSpecialSlot) {
            tooltipKeys[0] = "GT5U.machines.special_slot.tooltip";
            tooltipKeys[1] = "GT5U.machines.special_slot.tooltip.1";
        } else {
            tooltipKeys[0] = "GT5U.machines.unused_slot.tooltip";
            tooltipKeys[1] = "GT5U.machines.unused_slot.tooltip.1";
        }
        return new ItemSlot().marginRight(9)
            .slot(
                new ModularSlot(machine.inventoryHandler, machine.rechargerSlotStartIndex()).changeListener(
                    (_, _, client, init) -> { if (!client && !init) baseMetaTileEntity.markInventoryBeenModified(); })
                    .singletonSlotGroup(1000))
            .backgroundOverlay(
                properties.useSpecialSlot ? slotOverlayFunction.apply(0, false, false, true) : IDrawable.NONE)
            .tooltip(
                t -> t.addLine(GTUtility.translate(tooltipKeys[0]))
                    .addLine(GTUtility.translate(tooltipKeys[1])))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
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
