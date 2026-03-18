package gregtech.common.gui.modularui.item;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.DroneConnectionListPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ProductionPanel;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class DroneRemoteInterfaceGUI {

    private final MTEDroneCentre multiblock;
    PanelSyncManager pSyncManager;

    public DroneRemoteInterfaceGUI(PanelSyncManager syncManager, MTEDroneCentre multiblock) {
        if (NetworkUtils.isClient()) {
            this.multiblock = new MTEDroneCentre("fakeCentre");
        } else {
            this.multiblock = multiblock;
        }
        pSyncManager = syncManager;
        registerSyncValues(syncManager);
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {
        DroneCentreGuiUtil.syncDroneValues(syncManager, multiblock);
    }

    private IWidget createOnButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON))
            .syncHandler("onall")
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweron_all")));
    }

    private IWidget createOffButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF))
            .syncHandler("offall")
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweroff_all")));
    }

    public ModularPanel build() {
        IPanelHandler productionPanel = pSyncManager
            .syncedPanel("productionPanel", true, (k, v) -> new ProductionPanel(pSyncManager, multiblock));
        IPanelHandler machineListPanel = pSyncManager.syncedPanel(
            "machineListPanel",
            true,
            (k, v) -> new DroneConnectionListPanel(pSyncManager, multiblock, productionPanel));
        return new ModularPanel("remoteControl").height(50)
            .width(180)
            .child(ButtonWidget.panelCloseButton())
            .child(
                Flow.column()
                    .sizeRel(1)
                    .childPadding(4)
                    .align(Alignment.Center)
                    .top(4)
                    .child(
                        Flow.row()
                            .coverChildren()
                            .child(
                                IKey.lang("GT5U.gui.text.drone_remote_text")
                                    .asWidget()))
                    .child(
                        Flow.row()
                            .coverChildren()
                            .center()
                            .childPadding(4)
                            .child(
                                new ButtonWidget<>().size(18)
                                    .overlay(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
                                    .tooltipBuilder(t -> t.add(IKey.lang("GT5U.gui.button.drone_open_list")))
                                    .setEnabledIf(var -> multiblock == null || !multiblock.isValid())
                                    .onMousePressed(mouseButton -> {
                                        machineListPanel.openPanel();
                                        return true;
                                    }))
                            .child(createOnButton())
                            .child(createOffButton())));
    }
}
