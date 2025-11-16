package gregtech.common.gui.modularui.hatch;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.synchandler.DroneConnectionListSyncHandler;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;

public class MTEHatchDroneDownLinkGui<T extends MTEHatchDroneDownLink> {

    protected final T hatch;
    protected final IGregTechTileEntity baseMetaTileEntity;

    public MTEHatchDroneDownLinkGui(T hatch) {
        this.hatch = hatch;
        this.baseMetaTileEntity = hatch.getBaseMetaTileEntity();
    }

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);
        final ModularPanel panel = new GTBaseGuiBuilder(hatch, guiData, syncManager, uiSettings)
            .setWidth(176)
            .doesAddCoverTabs(false)
            .doesBindPlayerInventory(false)
            .build()
            .coverChildrenHeight();

        panel.child(
            new TextWidget<>(StatCollector.translateToLocal("GT5U.gui.text.drone_custom_name"))
                .alignment(Alignment.Center)
                .pos(0, 7)
                .size(176, 10));
        panel.child(createDynamicTextWidget(syncManager));
        return panel;
    }

    private IWidget createDynamicTextWidget(PanelSyncManager syncManager) {
        DroneConnectionListSyncHandler droneConnectionListSyncHandler = syncManager
            .findSyncHandler("droneConnections", DroneConnectionListSyncHandler.class);

        DynamicSyncHandler customNameHandler = new DynamicSyncHandler().widgetProvider((syncManager1, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            try {
                return createTextArea(packet, syncManager1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        droneConnectionListSyncHandler
            .setChangeListener(() -> notifyCustomNameHandler(customNameHandler, droneConnectionListSyncHandler));
        return new DynamicSyncedWidget<>().leftRel(0.85f)
            .coverChildrenHeight()
            .pos(4, 20)
            .syncHandler(customNameHandler);
    }

    private void notifyCustomNameHandler(DynamicSyncHandler recipeHandler,
        DroneConnectionListSyncHandler droneConnectionListSyncHandler) {
        recipeHandler.notifyUpdate(packet -> {
            List<DroneConnection> clientConnections = droneConnectionListSyncHandler.getValue();
            for (DroneConnection clientConnection : clientConnections) {
                DroneConnection.serialize(packet, clientConnection);
            }
        });
    }

    private IWidget createTextArea(PacketBuffer packet, PanelSyncManager syncManager1) throws IOException {
        Flow column = Flow.column()
            .coverChildren()
            .childPadding(3);
        List<DroneConnection> clientConnections = new ArrayList<>();
        while (packet.isReadable()) {
            DroneConnection connection = DroneConnection.deserialize(packet);
            clientConnections.add(connection);
        }
        if (clientConnections.isEmpty()) {
            column.child(
                new TextWidget<>(StatCollector.translateToLocal("GT5U.gui.text.drone_no_machines"))
                    .alignment(Alignment.Center)
                    .size(160, 10));
        } else {
            for (DroneConnection conn : clientConnections) {
                StringSyncValue nameSyncValue = syncManager1.getOrCreateSyncHandler(
                    conn.uuid.toString(),
                    StringSyncValue.class,
                    () -> new StringSyncValue(
                        () -> hatch.findConnection(conn.uuid)
                            .map(DroneConnection::getCustomName)
                            .orElse(""),
                        var -> hatch.findConnection(conn.uuid)
                            .ifPresent(c -> c.setCustomName(var))));
                column.child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(3)
                        .child(
                            new ButtonWidget<>().size(16, 16)
                                .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                .onMousePressed(mouseButton -> {
                                    EntityPlayer player = syncManager1.getPlayer();
                                    MTEDroneCentre.highlightMachine(player, conn.machineCoord);
                                    player.closeScreen();
                                    return true;
                                })
                                .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_highlight")))
                                .tooltipShowUpTimer(TOOLTIP_DELAY))
                        .child(new TextFieldWidget() {

                            @Override
                            public boolean onMouseRelease(int mouseButton) {
                                if (!isValid()) return false;
                                return super.onMouseRelease(mouseButton);
                            }
                        }.value(nameSyncValue)
                            .setValidator(s -> s.substring(0, Math.min(s.length(), 50)))
                            .size(140, 16)));
            }
        }
        return column;
    }

    public void registerSyncValues(PanelSyncManager syncManager) {
        DroneConnectionListSyncHandler droneConnectionListSyncHandler = new DroneConnectionListSyncHandler(
            hatch::getConnections,
            hatch::setConnections);
        syncManager.syncValue("droneConnections", droneConnectionListSyncHandler);
    }
}
