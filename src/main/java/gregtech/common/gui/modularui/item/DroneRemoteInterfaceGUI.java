package gregtech.common.gui.modularui.item;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.DroneConnectionListPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ProductionPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.DroneConnectionListSyncHandler;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.ProductionRecordSyncHandler;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class DroneRemoteInterfaceGUI {

    private final MTEDroneCentre centre;
    PanelSyncManager pSyncManager;

    public DroneRemoteInterfaceGUI(PanelSyncManager syncManager, MTEDroneCentre centre) {
        if (NetworkUtils.isClient()) {
            this.centre = new MTEDroneCentre("fakeCentre");
        } else {
            this.centre = centre;
        }
        pSyncManager = syncManager;
        registerSyncValues(syncManager);
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {
        syncManager.syncValue("onall", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                centre.turnOnAll();
                syncManager.getPlayer()
                    .closeScreen();
                syncManager.getPlayer()
                    .addChatComponentMessage(new ChatComponentTranslation("GT5U.machines.dronecentre.turnon"));
            }
        }));
        syncManager.syncValue("offall", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                centre.turnOffAll(var.shift);
                syncManager.getPlayer()
                    .closeScreen();
                syncManager.getPlayer()
                    .addChatComponentMessage(
                        new ChatComponentTranslation(
                            var.shift ? "GT5U.machines.dronecentre.forceturnoff"
                                : "GT5U.machines.dronecentre.turnoff"));
            }
        }));

        DroneConnectionListSyncHandler droneConnectionListSyncHandler = new DroneConnectionListSyncHandler(
            centre::getConnectionList);
        EnumSyncValue<DroneCentreGuiUtil.SortMode> sortModeSyncHandler = new EnumSyncValue<>(
            DroneCentreGuiUtil.SortMode.class,
            centre::getSortMode,
            centre::setSortMode);
        StringSyncValue searchFilterSyncHandler = new StringSyncValue(
            centre::getSearchBarText,
            centre::setSearchBarText);
        IntSyncValue activeGroupSyncHandler = new IntSyncValue(centre::getActiveGroup, centre::setActiveGroup);
        BooleanSyncValue searchOriSyncHandler = new BooleanSyncValue(
            centre::getSearchOriginalName,
            centre::setSearchOriginalName);
        BooleanSyncValue editModeSyncHandler = new BooleanSyncValue(centre::getEditMode, centre::setEditMode);
        BooleanSyncValue updateSyncHandler = new BooleanSyncValue(centre::shouldUpdate, centre::setUpdate);

        GenericListSyncHandler<String> groupSyncHandler = new GenericListSyncHandler<>(
            () -> centre.group,
            null,
            packetBuffer -> packetBuffer.readStringFromBuffer(32768),
            PacketBuffer::writeStringToBuffer,
            String::equals,
            null);

        syncManager.syncValue("droneList", droneConnectionListSyncHandler);
        syncManager.syncValue("sortMode", sortModeSyncHandler);
        syncManager.syncValue("searchFilter", searchFilterSyncHandler);
        syncManager.syncValue("groupNameList", groupSyncHandler);
        syncManager.syncValue("activeGroup", activeGroupSyncHandler);
        syncManager.syncValue("searchOri", searchOriSyncHandler);
        syncManager.syncValue("editMode", editModeSyncHandler);
        syncManager.syncValue("update", updateSyncHandler);

        IntSyncValue selectTimeSyncHandler = new IntSyncValue(centre::getSelectedTime, centre::setSelectedTime);
        ProductionRecordSyncHandler productionRecordSyncHandler = new ProductionRecordSyncHandler(
            () -> centre.productionDataRecorder);
        syncManager.syncValue("selectTime", selectTimeSyncHandler);
        syncManager.syncValue("productionRecord", productionRecordSyncHandler);
    }

    public ModularPanel build() {
        IPanelHandler productionPanel = pSyncManager
            .panel("productionPanel", (k, v) -> new ProductionPanel(pSyncManager, centre), true);
        IPanelHandler machineListPanel = pSyncManager.panel(
            "machineListPanel",
            (k, v) -> new DroneConnectionListPanel(pSyncManager, centre, productionPanel),
            true);
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
                                    .asWidget())
                            .child(
                                IKey.dynamic(
                                    () -> centre == null || !centre.isValid()
                                        ? StatCollector.translateToLocal("GT5U.gui.text.status.offline")
                                        : StatCollector.translateToLocal("GT5U.gui.text.status.online"))
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
                                    .setEnabledIf(var -> centre == null || !centre.isValid())
                                    .onMousePressed(mouseButton -> {
                                        machineListPanel.openPanel();
                                        return true;
                                    }))));
    }
}
