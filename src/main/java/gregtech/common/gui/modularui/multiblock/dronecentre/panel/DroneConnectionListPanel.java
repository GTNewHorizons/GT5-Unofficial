package gregtech.common.gui.modularui.multiblock.dronecentre.panel;

import static gregtech.api.enums.Mods.GregTech;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPacketWriter;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.DroneConnectionListSyncHandler;
import gregtech.common.gui.modularui.multiblock.dronecentre.widget.DroneListWidget;
import gregtech.common.gui.modularui.widget.UpdatableToggleButton;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class DroneConnectionListPanel extends ModularPanel {

    private final MTEDroneCentre centre;
    private final DynamicSyncHandler droneListHandler;
    private final DynamicSyncHandler groupHandler;
    IPanelHandler productionPanel;
    private int lastScroll;

    public DroneConnectionListPanel(PanelSyncManager syncManager, MTEDroneCentre centre,
        IPanelHandler productionPanel) {
        super("machineListPanel");
        this.centre = centre;
        this.productionPanel = productionPanel;
        DynamicSyncedWidget<?> dynamicWidget = new DynamicSyncedWidget<>().widthRel(0.95f)
            .expanded();
        droneListHandler = new DynamicSyncHandler() {

            // save scroll data before executing update
            @Override
            public void notifyUpdate(IPacketWriter packetWriter) {
                if (dynamicWidget.hasChildren()) {
                    IWidget child = dynamicWidget.getChildren()
                        .get(0);
                    if (child instanceof DroneListWidget<?, ?>list) {
                        lastScroll = list.getScrollY();
                    }
                }
                super.notifyUpdate(packetWriter);
            }
        }.widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createListArea(syncManager, pSyncManager);
        });
        dynamicWidget.syncHandler(droneListHandler);
        groupHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createGroupTab(syncManager, pSyncManager);
        });

        syncManager.findSyncHandler("sortMode", EnumSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("activeGroup", IntSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("droneList", DroneConnectionListSyncHandler.class)
            .setChangeListener(() -> {
                if (!this.centre.shouldUpdate()) return;
                droneListHandler.notifyUpdate(packet -> {});
            });
        syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
            .setChangeListener(() -> groupHandler.notifyUpdate(packet -> {}));

        int heightCoff = syncManager.isClient() ? Minecraft.getMinecraft().currentScreen.height - 40 : 0;
        this.width(270)
            .height(heightCoff)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .child(ButtonWidget.panelCloseButton())
            .child(
                new DynamicSyncedWidget<>().width(16)
                    .coverChildrenHeight()
                    .topRel(0.5f)
                    .leftRel(0f, 0, 1f)
                    .syncHandler(groupHandler))
            .child(
                Flow.column()
                    .widthRel(1)
                    .center()
                    .padding(8, 4)
                    .heightRel(0.95f)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_title")
                            .asWidget()
                            .scale(2))
                    .child(createFunctionArea(syncManager))
                    .child(dynamicWidget));

    }

    @Override
    public void onOpen(ModularScreen screen) {
        super.onOpen(screen);
        droneListHandler.notifyUpdate(packet -> {});
        groupHandler.notifyUpdate(packet -> {});
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    private IWidget createFunctionArea(PanelSyncManager syncManager) {
        return Flow.row()
            .heightRel(0.1f)
            .widthRel(0.95f)
            .childPadding(4)
            .marginBottom(4)
            .child(
                new CycleButtonWidget().size(16)
                    .value(syncManager.findSyncHandler("sortMode", EnumSyncValue.class))
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .stateOverlay(DroneCentreGuiUtil.SortMode.NAME, GTGuiTextures.OVERLAY_BUTTON_TRANSPOSE)
                    .stateOverlay(DroneCentreGuiUtil.SortMode.DISTANCE, GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE)
                    .stateOverlay(DroneCentreGuiUtil.SortMode.STATUS, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF)
                    .tooltipBuilder(
                        t -> t.addLine(
                            IKey.dynamic(
                                () -> StatCollector.translateToLocal(
                                    "GT5U.gui.button.drone_" + centre.getSortMode()
                                        .toString()
                                        .toLowerCase())))))
            .child(
                new UpdatableToggleButton(droneListHandler).size(16)
                    .value(syncManager.findSyncHandler("searchOri", BooleanSyncValue.class))
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_searchoriname"))))
            .child(
                new ButtonWidget<>().size(16)
                    .overlay(
                        new DrawableStack(
                            GTGuiTextures.BUTTON_STANDARD,
                            GTGuiTextures.OVERLAY_BUTTON_REDSTONESNIFFERLOCATE.asIcon()
                                .size(15, 15)))
                    .onMousePressed(mouseButton -> {
                        productionPanel.openPanel();
                        return true;
                    })
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_production"))))
            .child(
                new UpdatableToggleButton(droneListHandler, groupHandler).size(16)
                    .value(syncManager.findSyncHandler("editMode", BooleanSyncValue.class))
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF)
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_editmode"))))
            .child(
                new ToggleButton().size(16)
                    .value(syncManager.findSyncHandler("update", BooleanSyncValue.class))
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                    .tooltipBuilder(
                        t -> t.addLine(IKey.lang("GT5U.gui.button.drone_pause.1"))
                            .addLine(IKey.lang("GT5U.gui.button.drone_pause.2"))))
            .child(new TextFieldWidget() {

                @Override
                public void onRemoveFocus(ModularGuiContext context) {
                    super.onRemoveFocus(context);
                    droneListHandler.notifyUpdate(packet -> {});
                }
            }.height(16)
                .expanded()
                .value(syncManager.findSyncHandler("searchFilter", StringSyncValue.class)));
    }

    private IWidget createGroupTab(PanelSyncManager syncManager, PanelSyncManager dynamicSyncManager) {
        GenericListSyncHandler<String> groupSyncValue = syncManager
            .findSyncHandler("groupNameList", GenericListSyncHandler.class);
        IntSyncValue activeGroupSyncHandler = syncManager.findSyncHandler("activeGroup", IntSyncValue.class);

        Flow column = Flow.column()
            .width(16)
            .coverChildrenHeight()
            .childPadding(4);
        for (int i = 0; i < 8; i++) {
            final int finalI = i;
            if (centre.getEditMode() && centre.getActiveGroup() == i && i != 0) {
                column.child(
                    new TextFieldWidget().size(16)
                        .value(
                            dynamicSyncManager.getOrCreateSyncHandler(
                                "groupName" + i,
                                StringSyncValue.class,
                                () -> new StringSyncValue(
                                    () -> groupSyncValue.getValue()
                                        .get(finalI),
                                    var -> centre.group.set(finalI, var)))));
                continue;
            }
            column.child(
                new SelectButton().size(16, 16)
                    .right(0)
                    .overlay(IKey.str(i == 0 ? "All" : String.valueOf(i)))
                    .tooltipBuilder(
                        var -> var.add(
                            finalI == 0 ? "All"
                                : groupSyncValue.getValue()
                                    .get(finalI)))
                    .value(LinkedBoolValue.of(activeGroupSyncHandler, i)));
        }
        return column;
    }

    private IWidget createListArea(PanelSyncManager syncManager, PanelSyncManager dynamicSyncManager) {
        DroneConnectionListSyncHandler droneConnectionListSyncHandler = syncManager
            .findSyncHandler("droneList", DroneConnectionListSyncHandler.class);
        List<DroneConnection> clientConnections = droneConnectionListSyncHandler.getValue();
        if (clientConnections.isEmpty()) {
            return IKey.lang("GT5U.gui.text.drone_no_connection")
                .asWidget()
                .alignment(Alignment.CENTER)
                .align(Alignment.CENTER)
                .widthRel(0.95f)
                .scale(2);
        } else {
            ListWidget<IWidget, ?> droneListWidget = new DroneListWidget<>(lastScroll).sizeRel(1);

            final Comparator<DroneConnection> sorter = switch (centre.getSortMode()) {
                case NAME -> (o1, o2) -> Collator.getInstance(Locale.UK)
                    .compare(o1.getCustomName(), o2.getCustomName());
                case DISTANCE -> Comparator.comparing(DroneConnection::getDistanceSquared);
                case STATUS -> Comparator.comparing(DroneConnection::isMachineShutdown)
                    .reversed();
            };

            List<Flow> rows = clientConnections.stream()
                .filter(conn -> matchesSearchFilter(conn) && isInActiveGroup(conn))
                .sorted(sorter)
                .map(connection -> {
                    Flow row = Flow.row()
                        .widthRel(1)
                        .coverChildrenHeight()
                        .childPadding(4)
                        .paddingRight(4)
                        .marginBottom(2);
                    if (centre.getEditMode())
                        row.child(createGroupButton(connection, droneConnectionListSyncHandler, dynamicSyncManager));
                    row.child(
                        new ItemDisplayWidget().background(IDrawable.EMPTY)
                            .displayAmount(false)
                            .item(connection.getMachineItem())
                            .size(16)
                            .tooltipBuilder(
                                var -> DroneCentreGuiUtil.getTooltipFromItemSafely(var, connection.getMachineItem())))
                        .child(DroneCentreGuiUtil.createHighLightButton(connection, syncManager))
                        .child(createTextButton(connection, droneConnectionListSyncHandler, dynamicSyncManager))
                        .child(
                            createPowerControlButton(connection, droneConnectionListSyncHandler, dynamicSyncManager));
                    if (connection.isMachineShutdown()) row.child(
                        new Widget<>().background(GTGuiTextures.OVERLAY_POWER_LOSS)
                            .tooltipBuilder(var -> var.add(connection.getShutdownReason())));
                    row.child(
                        createCustomNameTextField(connection, dynamicSyncManager, droneConnectionListSyncHandler));
                    return row;
                })
                .collect(Collectors.toList());

            rows.forEach(droneListWidget::child);
            return droneListWidget;
        }
    }

    private IWidget createGroupButton(DroneConnection conn,
        DroneConnectionListSyncHandler droneConnectionListSyncHandler, PanelSyncManager dynamicSyncManager) {
        BooleanSyncValue groupSyncHandler = dynamicSyncManager.getOrCreateSyncHandler(
            "group" + conn.uuid.toString(),
            BooleanSyncValue.class,
            () -> new BooleanSyncValue(
                () -> droneConnectionListSyncHandler.getValue()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .map(con -> con.getGroup() == centre.getActiveGroup())
                    .findFirst()
                    .orElse(false),
                bool -> centre.getConnectionList()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .findFirst()
                    .ifPresent(con -> con.setGroup(bool ? centre.getActiveGroup() : 0))));
        IDrawable shape = UITexture.fullImage(GregTech.ID, "gui/overlay_slot/slice_shape")
            .asIcon()
            .size(8);
        return new ToggleButton().value(groupSyncHandler)
            .size(16)
            .background(IDrawable.EMPTY)
            .overlay(true, shape, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
            .overlay(false, shape);
    }

    private IWidget createTextButton(DroneConnection conn,
        DroneConnectionListSyncHandler droneConnectionListSyncHandler, PanelSyncManager dynamicSyncManager) {
        BooleanSyncValue selectSyncValue = dynamicSyncManager.getOrCreateSyncHandler(
            "select" + conn.uuid.toString(),
            BooleanSyncValue.class,
            () -> new BooleanSyncValue(
                () -> droneConnectionListSyncHandler.getValue()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .map(DroneConnection::isSelected)
                    .findFirst()
                    .orElse(false),
                bool -> centre.getConnectionList()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .findFirst()
                    .ifPresent(con -> {
                        con.setSelect(bool);
                        droneConnectionListSyncHandler.notifyUpdate();
                    })));
        return new UpdatableToggleButton(droneListHandler).size(16)
            .value(selectSyncValue)
            .background(GTGuiTextures.BUTTON_STANDARD)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_PRINT)
            .tooltipBuilder(t -> t.add(IKey.lang("GT5U.gui.button.drone_setname")));
    }

    private IWidget createPowerControlButton(DroneConnection conn,
        DroneConnectionListSyncHandler droneConnectionListSyncHandler, PanelSyncManager dynamicSyncManager) {
        BooleanSyncValue powerSwitchSyncer = dynamicSyncManager.getOrCreateSyncHandler(
            "power" + conn.uuid.toString(),
            BooleanSyncValue.class,
            () -> new BooleanSyncValue(
                () -> droneConnectionListSyncHandler.getValue()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .map(DroneConnection::isActive)
                    .findFirst()
                    .orElse(false),
                bool -> centre.getConnectionList()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .findFirst()
                    .map(DroneConnection::getLinkedMachine)
                    .ifPresent(mte -> {
                        if (bool) mte.enableWorking();
                        else if (mte.isAllowedToWork()) {
                            if (mte.maxProgresstime() > 0) {
                                mte.disableWorking();
                                mte.getBaseMetaTileEntity()
                                    .setShutdownStatus(true);
                                mte.getBaseMetaTileEntity()
                                    .setShutDownReason(ShutDownReasonRegistry.NONE);
                            } else mte.stopMachine(ShutDownReasonRegistry.NONE);
                        }
                    })));
        return new UpdatableToggleButton(droneListHandler).value(powerSwitchSyncer)
            .size(16)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
    }

    private IWidget createCustomNameTextField(DroneConnection conn, PanelSyncManager dynamicSyncManager,
        DroneConnectionListSyncHandler droneConnectionListSyncHandler) {
        if (!conn.isSelected()) return IKey.str(conn.getCustomName())
            .asWidget()
            .expanded();
        StringSyncValue nameSyncValue = dynamicSyncManager.getOrCreateSyncHandler(
            "name" + conn.uuid.toString(),
            StringSyncValue.class,
            () -> new StringSyncValue(
                () -> droneConnectionListSyncHandler.getValue()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .findFirst()
                    .map(DroneConnection::getCustomName)
                    .orElse(""),
                var -> centre.getConnectionList()
                    .stream()
                    .filter(connection -> connection.uuid.equals(conn.uuid))
                    .findFirst()
                    .ifPresent(c -> c.setCustomName(var))));
        return new TextFieldWidget().expanded()
            .value(nameSyncValue);
    }

    private boolean matchesSearchFilter(DroneConnection conn) {
        String searchText = centre.getSearchBarText()
            .toLowerCase();
        return conn.getCustomName()
            .toLowerCase()
            .contains(searchText)
            || (centre.getSearchOriginalName() && conn.getLocalizedName()
                .toLowerCase()
                .contains(searchText));
    }

    private boolean isInActiveGroup(DroneConnection conn) {
        return centre.getActiveGroup() == 0 || conn.getGroup() == centre.getActiveGroup() || centre.getEditMode();
    }
}
