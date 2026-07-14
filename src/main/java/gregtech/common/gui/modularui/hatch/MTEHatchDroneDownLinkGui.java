package gregtech.common.gui.modularui.hatch;

import java.util.List;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.DroneConnectionListSyncHandler;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;

public class MTEHatchDroneDownLinkGui extends MTEHatchBaseGui<MTEHatchDroneDownLink> {

    private DroneConnectionListSyncHandler droneConnectionListSyncHandler;

    public MTEHatchDroneDownLinkGui(MTEHatchDroneDownLink hatch) {
        super(hatch);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);
        final ModularPanel panel = GTGuis.mteTemplatePanelBuilder(machine, guiData, syncManager, uiSettings)
            .setWidth(176)
            .doesAddCoverTabs(false)
            .doesBindPlayerInventory(false)
            .doesAddGregTechLogo(false)
            .build()
            .coverChildrenHeight();

        ParentWidget<?> mainSection = new ParentWidget<>().coverChildrenHeight()
            .fullWidth()
            .padding(4)
            .child(createKeyButton(syncManager, panel));

        Flow mainColumn = Flow.column()
            .childPadding(4)
            .coverChildren()
            .horizontalCenter()
            .child(
                IKey.lang("GT5U.gui.text.drone_custom_name")
                    .asWidget())
            .child(createDynamicTextWidget(syncManager));

        mainSection.child(mainColumn);

        return panel.child(mainSection);
    }

    private IWidget createKeyButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler keyPanel = syncManager.syncedPanel(
            "keyPanel",
            true,
            (p_syncManager, syncHandler) -> DroneCentreGuiUtil.createConnectionKeyPanel(syncManager, parent));
        return new ButtonWidget<>().size(12)
            .topRel(0)
            .rightRel(0)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED)
            .onMousePressed(d -> {
                if (!keyPanel.isPanelOpen()) {
                    keyPanel.openPanel();
                } else {
                    keyPanel.closePanel();
                }
                return true;
            })
            .addTooltipLine(StatCollector.translateToLocal("GT5U.gui.button.drone_key_panel"));
    }

    private IWidget createDynamicTextWidget(PanelSyncManager syncManager) {

        DynamicSyncHandler customNameHandler = new DynamicSyncHandler()
            .widgetProvider((syncManager1, packet) -> createTextArea(syncManager1))
            .allowC2S();

        droneConnectionListSyncHandler.setChangeListener(() -> customNameHandler.notifyUpdate(packet -> {}));
        // We need to trigger initial build manually
        customNameHandler.notifyUpdate(packet -> {});

        return new DynamicSyncedWidget<>().coverChildren()
            .syncHandler(customNameHandler);
    }

    private IWidget createTextArea(PanelSyncManager syncManager1) {
        Flow column = Flow.column()
            .coverChildren()
            .childPadding(3);
        List<DroneConnection> clientConnections = droneConnectionListSyncHandler.getValue();
        if (clientConnections.isEmpty()) {
            column.child(
                IKey.lang("GT5U.gui.text.drone_no_connection")
                    .asWidget());
        } else {
            for (DroneConnection conn : clientConnections) {
                StringSyncValue nameSyncValue = syncManager1.getOrCreateSyncHandler(
                    conn.uuid.toString(),
                    StringSyncValue.class,
                    () -> new StringSyncValue(
                        () -> droneConnectionListSyncHandler.getValue()
                            .stream()
                            .filter(c -> c.uuid.equals(conn.uuid))
                            .findFirst()
                            .map(DroneConnection::getCustomName)
                            .orElse(""),
                        var -> {
                            if (!syncManager1.isClient()) {
                                if (var != null && !var.trim()
                                    .isEmpty()) {
                                    machine.findConnection(conn.uuid)
                                        .ifPresent(c -> {
                                            c.setCustomName(var);
                                            droneConnectionListSyncHandler.notifyUpdate();
                                        });
                                } else {
                                    droneConnectionListSyncHandler.notifyUpdate();
                                }
                            }
                        }).allowC2S());
                column.child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(3)
                        .child(DroneCentreGuiUtil.createHighLightButton(conn, syncManager1))
                        .child(
                            new TextFieldWidget().value(nameSyncValue)
                                .setValidator(s -> s.substring(0, Math.min(s.length(), 50)))
                                .size(140, 16)));
            }
        }
        return column;
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        droneConnectionListSyncHandler = new DroneConnectionListSyncHandler(machine::getConnections);
        syncManager.syncValue("droneConnections", droneConnectionListSyncHandler);
        syncManager.syncValue("setkey", new StringSyncValue(machine::getKey, machine::setKey).allowC2S());
    }
}
