package gregtech.common.gui.modularui.multiblock.dronecentre;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.GTMod;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.CameraObservePanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.DroneConnectionListPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ProductionPanel;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class MTEDroneCentreGui extends MTEMultiBlockBaseGui<MTEDroneCentre> {

    private EntityPlayer player;

    public MTEDroneCentreGui(MTEDroneCentre mteDroneCentre) {
        super(mteDroneCentre);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        this.player = guiData.getPlayer();
        return super.build(guiData, syncManager, uiSettings);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("setkey", new StringSyncValue(multiblock::getKey, multiblock::setKey).allowC2S());
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
    protected ButtonWidget<?> createPowerPanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler keyPanel = syncManager.syncedPanel(
            "keyPanel",
            true,
            (p_syncManager, syncHandler) -> DroneCentreGuiUtil.createConnectionKeyPanel(syncManager, parent));
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
            .syncedPanel("productionPanel", true, (k, v) -> new ProductionPanel(syncManager, multiblock));

        final IPanelHandler[] panels = new IPanelHandler[2]; // 0: machineListPanel, 1: cameraObservePanel

        panels[1] = syncManager
            .syncedPanel("cameraObservePanel", true, (k, v) -> new CameraObservePanel(syncManager, () -> {
                if (panels[0] != null) {
                    panels[0].openPanel();
                }
            }));

        panels[0] = syncManager.syncedPanel(
            "machineListPanel",
            true,
            (k, v) -> new DroneConnectionListPanel(syncManager, multiblock, productionPanel, panels[1], v));

        if (syncManager.isClient()) {
            if (GTMod.proxy.cameraViewportManager != null && GTMod.proxy.cameraViewportManager.isObservingActive()) {
                syncManager.addOpenListener(p -> panels[1].openPanel());
            }
        } else {
            if (this.player != null && CameraViewportManager.sessions.containsKey(this.player.getUniqueID())) {
                syncManager.addOpenListener(p -> panels[1].openPanel());
            }
        }

        return new ButtonWidget<>().size(18, 18)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_WHITELIST))
            .onMousePressed(mouseButton -> {
                if (panels[0] != null) {
                    panels[0].openPanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_open_list")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }
}
