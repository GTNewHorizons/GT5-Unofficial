package gregtech.common.gui.modularui.multiblock.dronecentre;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ConnectionKeyPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.DroneConnectionListPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ProductionPanel;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class MTEDroneCentreGui extends MTEMultiBlockBaseGui<MTEDroneCentre> {

    public MTEDroneCentreGui(MTEDroneCentre mteDroneCentre) {
        super(mteDroneCentre);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("setkey", new StringSyncValue(multiblock::getKey, multiblock::setKey));
        DroneCentreGuiUtil.syncDroneValues(syncManager, multiblock);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(creatMachineListButton(syncManager))
            .child(createOnButton())
            .child(createOffButton());
    }

    private IWidget createOnButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON))
            .syncHandler("onall")
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweron_all")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    private IWidget createOffButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF))
            .syncHandler("offall")
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweroff_all")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    @Override
    protected IWidget createPowerPanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler keyPanel = syncManager
            .panel("keyPanel", (p_syncManager, syncHandler) -> new ConnectionKeyPanel(syncManager, parent), true);
        return new ButtonWidget<>().size(18, 18)
            .marginTop(4)
            .marginLeft(4)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED)
            .onMousePressed(d -> {
                if (!keyPanel.isPanelOpen()) {
                    keyPanel.openPanel();
                } else {
                    keyPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_key_panel")));
    }

    private IWidget creatMachineListButton(PanelSyncManager syncManager) {
        IPanelHandler productionPanel = syncManager
            .panel("productionPanel", (k, v) -> new ProductionPanel(syncManager, multiblock), true);
        IPanelHandler machineListPanel = syncManager.panel(
            "machineListPanel",
            (k, v) -> new DroneConnectionListPanel(syncManager, multiblock, productionPanel),
            true);

        return new ButtonWidget<>().size(18, 18)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_WHITELIST))
            .onMousePressed(mouseButton -> {
                machineListPanel.openPanel();
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_open_list")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }
}
