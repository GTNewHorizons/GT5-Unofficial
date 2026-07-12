package gregtech.common.gui.modularui.singleblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
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
    protected boolean supportsPowerSwitch() {
        return false;
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
        return new ItemSlot()
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
    protected ParentWidget<?> createPowerVisualizer(PanelSyncManager syncManager) {
        LongSyncValue steamStoredSyncer = new LongSyncValue(() -> machine.getSteamVar() * 2);
        syncManager.syncValue("steamStored", steamStoredSyncer);
        LongSyncValue maxSteamSyncer = new LongSyncValue(() -> machine.maxSteamStore() * 2);
        syncManager.syncValue("maxSteam", maxSteamSyncer);

        return new SteamGaugeWidget(steamStoredSyncer, maxSteamSyncer).rightRel(1)
            .top(8);
    }

    @Override
    protected void initErrors(PanelSyncManager syncManager) {
        // this has a different tooltip than the normal powerfail error
        BooleanSyncValue powerfailSyncer = new BooleanSyncValue(machine::isStuttering);
        syncManager.syncValue("powerfail", powerfailSyncer);
        errorMap.put(
            powerfailSyncer,
            machine.mTooltipCache.getData(
                "GT5U.machines.stalled_stuttering.tooltip",
                GTUtility.translate("GT5U.machines.powersource.steam")));

        BooleanSyncValue ventingSyncer = new BooleanSyncValue(machine::needsSteamVenting);
        syncManager.syncValue("venting", ventingSyncer);
        errorMap.put(ventingSyncer, machine.mTooltipCache.getData("GT5U.machines.stalled_vent.tooltip"));
    }
}
