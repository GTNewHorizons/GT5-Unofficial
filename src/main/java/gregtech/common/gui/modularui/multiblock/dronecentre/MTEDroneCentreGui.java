package gregtech.common.gui.modularui.multiblock.dronecentre;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentTranslation;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.screen.ModularPanel;
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
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ConnectionKeyPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.DroneConnectionListPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.panel.ProductionPanel;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.DroneConnectionListSyncHandler;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.ProductionRecordSyncHandler;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class MTEDroneCentreGui extends MTEMultiBlockBaseGui<MTEDroneCentre> {

    IPanelHandler machineListPanel;

    public MTEDroneCentreGui(MTEDroneCentre mteDroneCentre) {
        super(mteDroneCentre);
    }

    public MTEDroneCentre getMultiblock() {
        return this.multiblock;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("onall", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                multiblock.turnOnAll();
                syncManager.getPlayer()
                    .closeScreen();
                syncManager.getPlayer()
                    .addChatComponentMessage(new ChatComponentTranslation("GT5U.machines.dronecentre.turnon"));
            }
        }));
        syncManager.syncValue("offall", new InteractionSyncHandler().setOnMousePressed(var -> {
            if (!NetworkUtils.isClient()) {
                multiblock.turnOffAll(var.shift);
                syncManager.getPlayer()
                    .closeScreen();
                syncManager.getPlayer()
                    .addChatComponentMessage(
                        new ChatComponentTranslation(
                            var.shift ? "GT5U.machines.dronecentre.forceturnoff"
                                : "GT5U.machines.dronecentre.turnoff"));
            }
        }));
        syncManager.syncValue("setkey", new StringSyncValue(multiblock::getKey, multiblock::setKey));

        DroneConnectionListSyncHandler droneConnectionListSyncHandler = new DroneConnectionListSyncHandler(
            multiblock::getConnectionList);
        EnumSyncValue<DroneCentreGuiUtil.SortMode> sortModeSyncHandler = new EnumSyncValue<>(
            DroneCentreGuiUtil.SortMode.class,
            multiblock::getSortMode,
            multiblock::setSortMode);
        StringSyncValue searchFilterSyncHandler = new StringSyncValue(
            multiblock::getSearchBarText,
            multiblock::setSearchBarText);
        IntSyncValue activeGroupSyncHandler = new IntSyncValue(multiblock::getActiveGroup, multiblock::setActiveGroup);
        BooleanSyncValue searchOriSyncHandler = new BooleanSyncValue(
            multiblock::getSearchOriginalName,
            multiblock::setSearchOriginalName);
        BooleanSyncValue editModeSyncHandler = new BooleanSyncValue(multiblock::getEditMode, multiblock::setEditMode);
        BooleanSyncValue updateSyncHandler = new BooleanSyncValue(multiblock::shouldUpdate, multiblock::setUpdate);

        GenericListSyncHandler<String> groupSyncHandler = new GenericListSyncHandler<>(
            () -> multiblock.group,
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

        IntSyncValue selectTimeSyncHandler = new IntSyncValue(multiblock::getSelectedTime, multiblock::setSelectedTime);
        ProductionRecordSyncHandler productionRecordSyncHandler = new ProductionRecordSyncHandler(
            () -> multiblock.productionDataRecorder);
        syncManager.syncValue("selectTime", selectTimeSyncHandler);
        syncManager.syncValue("productionRecord", productionRecordSyncHandler);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(creatMachineListButton(syncManager))
            .child(createOnButton())
            .child(createOffButton());
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

    private IWidget creatMachineListButton(PanelSyncManager syncManager) {
        IPanelHandler productionPanel = syncManager
            .panel("productionPanel", (k, v) -> new ProductionPanel(syncManager, multiblock), true);
        machineListPanel = syncManager.panel(
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
