package gregtech.common.gui.modularui.hatch;

import java.util.List;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.DroneConnectionListSyncHandler;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
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
        final ModularPanel panel = new GTBaseGuiBuilder(hatch, guiData, syncManager, uiSettings).setWidth(176)
            .doesAddCoverTabs(false)
            .doesBindPlayerInventory(false)
            .doesAddGregTechLogo(false)
            .build()
            .coverChildrenHeight();

        panel
            .child(
                new TextWidget<>(StatCollector.translateToLocal("GT5U.gui.text.drone_custom_name"))
                    .alignment(Alignment.Center)
                    .pos(0, 7)
                    .size(176, 10))
            .child(createKeyButton(syncManager, panel))
            .child(createDynamicTextWidget(syncManager));
        return panel;
    }

    private IWidget createKeyButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler keyPanel = syncManager.panel(
            "keyPanel",
            (p_syncManager, syncHandler) -> DroneCentreGuiUtil.createConnectionKeyPanel(syncManager, parent),
            true);
        return new ButtonWidget<>().size(12, 12)
            .right(4)
            .top(4)
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

    private IWidget createDynamicTextWidget(PanelSyncManager syncManager) {

        DynamicSyncHandler customNameHandler = new DynamicSyncHandler()
            .widgetProvider((syncManager1, packet) -> createTextArea(syncManager1));

        droneConnectionListSyncHandler.setChangeListener(() -> customNameHandler.notifyUpdate(packet -> {}));
        // We need to trigger initial build manually
        customNameHandler.notifyUpdate(packet -> {});

        return new DynamicSyncedWidget<>().leftRel(0.85f)
            .coverChildrenHeight()
            .pos(4, 20)
            .syncHandler(customNameHandler);
    }

    private IWidget createTextArea(PanelSyncManager syncManager1) {
        Flow column = Flow.column()
            .coverChildren()
            .childPadding(3);
        List<DroneConnection> clientConnections = droneConnectionListSyncHandler.getValue();
        if (clientConnections.isEmpty()) {
            column.child(
                new TextWidget<>(StatCollector.translateToLocal("GT5U.gui.text.drone_no_connection"))
                    .alignment(Alignment.Center)
                    .size(160, 10));
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
                        var -> hatch.findConnection(conn.uuid)
                            .ifPresent(c -> c.setCustomName(var))));
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
        droneConnectionListSyncHandler = new DroneConnectionListSyncHandler(hatch::getConnections);
        syncManager.syncValue("droneConnections", droneConnectionListSyncHandler);
        syncManager.syncValue("setkey", new StringSyncValue(hatch::getKey, hatch::setKey));
    }
}
