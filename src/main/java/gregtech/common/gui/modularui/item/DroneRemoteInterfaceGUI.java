package gregtech.common.gui.modularui.item;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.GTMod;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.CameraObservePanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.DroneConnectionListPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ProductionPanel;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class DroneRemoteInterfaceGUI {

    private final MTEDroneCentre multiblock;
    private final EntityPlayer player;
    PanelSyncManager pSyncManager;

    public DroneRemoteInterfaceGUI(PanelSyncManager syncManager, MTEDroneCentre multiblock, EntityPlayer player) {
        if (NetworkUtils.isClient()) {
            this.multiblock = new MTEDroneCentre("fakeCentre");
        } else {
            this.multiblock = multiblock;
        }
        this.player = player;
        pSyncManager = syncManager;
        registerSyncValues(syncManager);
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {
        DroneCentreGuiUtil.syncDroneValues(syncManager, multiblock);
    }

    private IWidget createOnButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
            .syncHandler("onall")
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweron_all")));
    }

    private IWidget createOffButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF)
            .syncHandler("offall")
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweroff_all")));
    }

    public ModularPanel build() {
        IPanelHandler productionPanel = pSyncManager
            .syncedPanel("productionPanel", true, (k, v) -> new ProductionPanel(pSyncManager, multiblock));
        final IPanelHandler[] panels = new IPanelHandler[2]; // 0: machineListPanel, 1: cameraObservePanel

        panels[1] = pSyncManager
            .syncedPanel("cameraObservePanel", true, (k, v) -> new CameraObservePanel(pSyncManager, () -> {
                if (panels[0] != null) {
                    panels[0].openPanel();
                }
            }));

        panels[0] = pSyncManager.syncedPanel(
            "machineListPanel",
            true,
            (k, v) -> new DroneConnectionListPanel(pSyncManager, multiblock, productionPanel, panels[1], v));

        if (pSyncManager.isClient()) {
            if (GTMod.proxy.cameraViewportManager != null
                && GTMod.proxy.cameraViewportManager.activeConnection != null) {
                pSyncManager.addOpenListener(p -> panels[1].openPanel());
            }
        } else {
            if (this.player != null && CameraViewportManager.sessions.containsKey(this.player.getUniqueID())) {
                pSyncManager.addOpenListener(p -> panels[1].openPanel());
            }
        }

        return new ModularPanel("remoteControl").size(180, 50)
            .child(ButtonWidget.panelCloseButton())
            .child(
                Flow.column()
                    .full()
                    .childPadding(6)
                    .paddingTop(4)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_remote_text")
                            .asWidget())
                    .child(
                        Flow.row()
                            .coverChildren()
                            .childPadding(4)
                            .child(
                                new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
                                    .tooltipBuilder(t -> t.add(IKey.lang("GT5U.gui.button.drone_open_list")))
                                    .setEnabledIf(var -> multiblock == null || !multiblock.isValid())
                                    .onMousePressed(mouseButton -> {
                                        if (panels[0] != null) {
                                            panels[0].openPanel();
                                        }
                                        return true;
                                    }))
                            .child(createOnButton())
                            .child(createOffButton())));
    }
}
