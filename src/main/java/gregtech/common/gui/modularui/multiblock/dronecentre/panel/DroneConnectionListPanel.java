package gregtech.common.gui.modularui.multiblock.dronecentre.panel;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPacketWriter;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.GTMod;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.data.drone.CameraViewportClientManager;
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
    private final DynamicSyncHandler titleBarHandler;
    IPanelHandler productionPanel;
    IPanelHandler cameraObservePanel;
    private final IPanelHandler machineListPanel;
    private int lastScroll;

    public DroneConnectionListPanel(PanelSyncManager syncManager, MTEDroneCentre centre, IPanelHandler productionPanel,
        IPanelHandler cameraObservePanel, IPanelHandler machineListPanel) {
        super("machineListPanel");
        this.centre = centre;
        this.productionPanel = productionPanel;
        this.cameraObservePanel = cameraObservePanel;
        this.machineListPanel = machineListPanel;
        DynamicSyncedWidget<?> dynamicWidget = new DynamicSyncedWidget<>().widthRel(0.95f)
            .expanded();
        droneListHandler = new DynamicSyncHandler() {

            // save scroll data before executing update (runs while old widget is still on-screen and fully resized)
            @Override
            public void notifyUpdate(IPacketWriter packetWriter) {
                if (dynamicWidget.hasChildren()) {
                    IWidget child = dynamicWidget.getChildren()
                        .getFirst();
                    if (child instanceof DroneListWidget<?, ?>list && list.getScrollData()
                        .getScrollSize() != 0) {
                        lastScroll = list.getScrollY();
                    }
                }
                super.notifyUpdate(packetWriter);
            }
        }.widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            if (dynamicWidget.hasChildren()) {
                IWidget child = dynamicWidget.getChildren()
                    .getFirst();
                if (child instanceof DroneListWidget<?, ?>list && list.getScrollData()
                    .getScrollSize() != 0) {
                    lastScroll = list.getScrollY();
                }
            }
            return createListArea(syncManager, pSyncManager);
        })
            .allowC2S();
        dynamicWidget.syncHandler(droneListHandler);

        DynamicSyncedWidget<?> titleBarWidget = new DynamicSyncedWidget<>().widthRel(0.95f)
            .coverChildrenHeight();
        titleBarHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createTitleBar(syncManager, pSyncManager);
        })
            .allowC2S();
        titleBarWidget.syncHandler(titleBarHandler);

        groupHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createGroupTab(syncManager, pSyncManager);
        })
            .allowC2S();

        syncManager.findSyncHandler("sortMode", EnumSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("activeGroup", IntSyncValue.class)
            .setChangeListener(() -> {
                centre.setRenamingActiveGroup(false);
                titleBarHandler.notifyUpdate(packet -> {});
                droneListHandler.notifyUpdate(packet -> {});
            });
        syncManager.findSyncHandler("droneList", DroneConnectionListSyncHandler.class)
            .setChangeListener(() -> {
                if (!this.centre.shouldUpdate()) return;
                droneListHandler.notifyUpdate(packet -> {});
            });
        syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
            .setChangeListener(() -> {
                groupHandler.notifyUpdate(packet -> {});
                titleBarHandler.notifyUpdate(packet -> {});
                droneListHandler.notifyUpdate(packet -> {});
            });

        int heightCoff = syncManager.isClient() ? Minecraft.getMinecraft().currentScreen.height - 40 : 0;
        this.width(270)
            .height(heightCoff)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .child(ButtonWidget.panelCloseButton())
            .child(
                new DynamicSyncedWidget<>().width(54)
                    .heightRel(0.8f)
                    .topRel(0.5f)
                    .leftRel(0f, 0, 1f)
                    .syncHandler(groupHandler))
            .child(
                Flow.column()
                    .fullWidth()
                    .center()
                    .padding(8, 4)
                    .heightRel(0.95f)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_title")
                            .asWidget()
                            .scale(2))
                    .child(createFunctionArea(syncManager))
                    .child(titleBarWidget)
                    .child(dynamicWidget));

    }

    @Override
    public void onOpen(ModularScreen screen) {
        super.onOpen(screen);
        droneListHandler.notifyUpdate(packet -> {});
        titleBarHandler.notifyUpdate(packet -> {});
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
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_searchoriname"))))
            .child(
                new ButtonWidget<>().size(16)
                    .overlay(
                        GTGuiTextures.OVERLAY_BUTTON_REDSTONESNIFFERLOCATE.asIcon()
                            .size(12))
                    .onMousePressed(mouseButton -> {
                        productionPanel.openPanel();
                        return true;
                    })
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_production"))))
            .child(
                new UpdatableToggleButton(droneListHandler, groupHandler, titleBarHandler).size(16)
                    .value(syncManager.findSyncHandler("editMode", BooleanSyncValue.class))
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF)
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_editmode"))))
            .child(
                new ToggleButton().size(16)
                    .value(syncManager.findSyncHandler("update", BooleanSyncValue.class))
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
            .full()
            .childPadding(4);

        ListWidget<IWidget, ?> groupListWidget = new ListWidget<>().fullWidth()
            .expanded()
            .scrollDirection(new VerticalScrollData(true));

        int size = groupSyncValue.getValue()
            .size();
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            String name = finalI == 0 ? "All"
                : groupSyncValue.getValue()
                    .get(finalI);
            if (name == null) {
                name = String.valueOf(finalI);
            }
            String displayName = name;
            if (name.length() > 5) {
                displayName = name.substring(0, 4) + "..";
            }
            groupListWidget.child(
                new SelectButton().size(54, 16)
                    .right(0)
                    .marginBottom(4)
                    .overlay(IKey.str(displayName))
                    .tooltipBuilder(
                        var -> var.add(
                            finalI == 0 ? "All"
                                : groupSyncValue.getValue()
                                    .get(finalI)))
                    .value(LinkedBoolValue.of(activeGroupSyncHandler, i)));
        }

        column.child(groupListWidget);

        column.child(
            new ButtonWidget<>().size(54, 16)
                .overlay(
                    IKey.str("+")
                        .alignment(Alignment.CENTER))
                .syncHandler(syncManager.findSyncHandler("addNewGroup", InteractionSyncHandler.class))
                .tooltipBuilder(t -> t.add(IKey.lang("GT5U.gui.button.drone_add_group"))));

        return column;
    }

    private IWidget createTitleBar(PanelSyncManager syncManager, PanelSyncManager dynamicSyncManager) {
        int activeGroup = centre.getActiveGroup();
        if (activeGroup == 0) {
            return Flow.row()
                .fullWidth()
                .height(18)
                .childPadding(4)
                .child(
                    IKey.lang("GT5U.gui.text.drone_group_all")
                        .asWidget()
                        .scale(1.2f)
                        .textAlign(Alignment.CENTER)
                        .expanded());
        }

        if (centre.getRenamingActiveGroup()) {
            StringSyncValue nameSyncValue = dynamicSyncManager.getOrCreateSyncHandler(
                "groupName" + activeGroup,
                StringSyncValue.class,
                () -> new StringSyncValue(
                    () -> (String) syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
                        .getValue()
                        .get(activeGroup),
                    var -> {
                        if (!syncManager.isClient()) {
                            if (var != null && !var.trim()
                                .isEmpty()) {
                                centre.group.set(activeGroup, var);
                            }
                            syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
                                .notifyUpdate();
                        }
                    }).allowC2S());
            return Flow.row()
                .fullWidth()
                .height(18)
                .childPadding(4)
                .child(new TextFieldWidget() {

                    @Override
                    public void onRemoveFocus(ModularGuiContext context) {
                        super.onRemoveFocus(context);
                        centre.setRenamingActiveGroup(false);
                        syncManager.findSyncHandler("renamingActiveGroup", BooleanSyncValue.class)
                            .notifyUpdate();
                        titleBarHandler.notifyUpdate(packet -> {});
                    }
                }.height(16)
                    .expanded()
                    .value(nameSyncValue));
        }

        String groupName = (String) syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
            .getValue()
            .get(activeGroup);
        Flow row = Flow.row()
            .fullWidth()
            .height(18)
            .childPadding(4);
        if (centre.getEditMode()) {
            row.child(
                new ButtonWidget<>().size(16)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                    .syncHandler(syncManager.findSyncHandler("toggleSelectAll", InteractionSyncHandler.class))
                    .tooltipBuilder(t -> t.add(IKey.lang("GT5U.gui.button.drone_toggle_select_all"))));
        }
        row.child(
            new ButtonWidget<>().size(16)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_PRINT)
                .onMousePressed(mouseButton -> {
                    if (mouseButton == 0) {
                        centre.setRenamingActiveGroup(true);
                        syncManager.findSyncHandler("renamingActiveGroup", BooleanSyncValue.class)
                            .notifyUpdate();
                        titleBarHandler.notifyUpdate(packet -> {});
                    }
                    return true;
                })
                .tooltipBuilder(t -> t.add(IKey.lang("GT5U.gui.button.drone_rename_group"))))
            .child(
                IKey.str(groupName)
                    .asWidget()
                    .scale(1.2f)
                    .textAlign(Alignment.CENTER)
                    .expanded())
            .child(
                new ButtonWidget<>().size(16)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                    .syncHandler(syncManager.findSyncHandler("deleteGroup", InteractionSyncHandler.class))
                    .tooltipBuilder(t -> t.add(IKey.lang("GT5U.gui.button.drone_delete_group"))));
        return row;
    }

    private IWidget createListArea(PanelSyncManager syncManager, PanelSyncManager dynamicSyncManager) {
        DroneConnectionListSyncHandler droneConnectionListSyncHandler = syncManager
            .findSyncHandler("droneList", DroneConnectionListSyncHandler.class);
        List<DroneConnection> clientConnections = droneConnectionListSyncHandler.getValue();
        if (clientConnections.isEmpty()) {
            return IKey.lang("GT5U.gui.text.drone_no_connection")
                .asWidget()
                .textAlign(Alignment.CENTER)
                .center()
                .widthRel(0.95f)
                .scale(2);
        }

        ListWidget<IWidget, ?> droneListWidget = new DroneListWidget<>(lastScroll).full();
        final Comparator<DroneConnection> sorter = switch (centre.getSortMode()) {
            case NAME -> (o1, o2) -> Collator.getInstance(Locale.UK)
                .compare(o1.getCustomName(), o2.getCustomName());
            case DISTANCE -> Comparator.comparing(DroneConnection::getDistanceSquared);
            case STATUS -> Comparator.comparing(DroneConnection::isMachineShutdown)
                .reversed();
        };

        List<Flow> rows = clientConnections.stream()
            .filter(conn -> DroneCentreGuiUtil.matchesSearchFilter(conn, centre) && isInActiveGroup(conn))
            .sorted(sorter)
            .map(connection -> {
                Flow row = Flow.row()
                    .fullWidth()
                    .coverChildrenHeight()
                    .childPadding(4)
                    .paddingRight(4)
                    .marginBottom(2);
                if (centre.getEditMode())
                    row.child(createGroupButton(connection, droneConnectionListSyncHandler, dynamicSyncManager));
                row.child(
                    new ItemDisplayWidget().disableThemeBackground(true)
                        .disableHoverThemeBackground(true)
                        .displayAmount(false)
                        .item(connection.getMachineItem())
                        .size(16)
                        .tooltipBuilder(
                            var -> DroneCentreGuiUtil.getTooltipFromItemSafely(var, connection.getMachineItem())))
                    .child(DroneCentreGuiUtil.createHighLightButton(connection, dynamicSyncManager))
                    .child(createObserveButton(connection, dynamicSyncManager))
                    .child(createTextButton(connection, droneConnectionListSyncHandler, dynamicSyncManager))
                    .child(createPowerControlButton(connection, droneConnectionListSyncHandler, dynamicSyncManager));
                if (connection.isMachineShutdown()) row.child(
                    new Widget<>().background(GTGuiTextures.OVERLAY_POWER_LOSS)
                        .tooltipBuilder(var -> var.add(connection.getShutdownReason())));
                row.child(createCustomNameTextField(connection, dynamicSyncManager, droneConnectionListSyncHandler));
                return row;
            })
            .toList();

        rows.forEach(droneListWidget::child);

        return droneListWidget;
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
                    .map(con -> (con.getGroupMask() & (1L << centre.getActiveGroup())) != 0)
                    .findFirst()
                    .orElse(false),
                bool -> {
                    if (!dynamicSyncManager.isClient()) {
                        centre.getConnectionList()
                            .stream()
                            .filter(c -> c.uuid.equals(conn.uuid))
                            .findFirst()
                            .ifPresent(con -> {
                                int active = centre.getActiveGroup();
                                long mask = con.getGroupMask();
                                if (bool) {
                                    con.setGroupMask(mask | (1L << active));
                                } else {
                                    con.setGroupMask(mask & ~(1L << active));
                                }
                                droneConnectionListSyncHandler.notifyUpdate();
                            });
                    }
                }).allowC2S());
        return new ToggleButton().value(groupSyncHandler)
            .size(16)
            .disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .tooltipBuilder(var -> var.add(IKey.lang("GT5U.gui.button.drone_select_group")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS);
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
                    })).allowC2S());
        return new UpdatableToggleButton(droneListHandler).size(16)
            .value(selectSyncValue)
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
                    })).allowC2S());
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
                var -> {
                    if (!dynamicSyncManager.isClient()) {
                        if (var != null && !var.trim()
                            .isEmpty()) {
                            centre.getConnectionList()
                                .stream()
                                .filter(connection -> connection.uuid.equals(conn.uuid))
                                .findFirst()
                                .ifPresent(c -> {
                                    c.setCustomName(var);
                                    droneConnectionListSyncHandler.notifyUpdate();
                                });
                        } else {
                            droneConnectionListSyncHandler.notifyUpdate();
                        }
                    }
                }).allowC2S());
        return new TextFieldWidget().expanded()
            .value(nameSyncValue);
    }

    private IWidget createObserveButton(DroneConnection conn, PanelSyncManager dynamicSyncManager) {
        boolean sameDim = conn.getMachineWorld() == dynamicSyncManager.getPlayer().dimension;
        ButtonWidget<?> button = new ButtonWidget<>().size(16)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_HIGHLIGHT_BLOCK);

        if (!sameDim) {
            button.tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_observe.diff_dim")));
        } else {
            button.tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_observe")))
                .onMousePressed(mouseButton -> {
                    if (mouseButton == 0) {
                        ((CameraViewportClientManager) GTMod.proxy.cameraViewportManager).startObserving(conn);
                        cameraObservePanel.openPanel();
                        if (machineListPanel != null) {
                            machineListPanel.closePanel();
                        }
                    }
                    return true;
                });
        }
        return button;
    }

    private boolean isInActiveGroup(DroneConnection conn) {
        return centre.getActiveGroup() == 0 || (conn.getGroupMask() & (1L << centre.getActiveGroup())) != 0
            || centre.getEditMode();
    }
}
