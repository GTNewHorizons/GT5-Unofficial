package gregtech.common.gui.modularui.multiblock;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.synchandler.DroneConnectionListSyncHandler;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class MTEDroneCentreGui extends MTEMultiBlockBaseGui<MTEDroneCentre> {

    private int lastScroll = 0;

    public MTEDroneCentreGui(MTEDroneCentre mteDroneCentre) {
        super(mteDroneCentre);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("droneConnections", new DroneConnectionListSyncHandler(multiblock::getConnectionList));
        syncManager
            .syncValue("sort", new EnumSyncValue<>(SortMode.class, multiblock::getSortMode, multiblock::setSortMode));
        syncManager
            .syncValue("searchFilter", new StringSyncValue(multiblock::getSearchBarText, multiblock::setSearchBarText));
        syncManager.syncValue(
            "searchOri",
            new BooleanSyncValue(multiblock::getSearchOriginalName, multiblock::setSearchOriginalName));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(creatMachineListButton(syncManager))
            .child(createOnButton(syncManager))
            .child(createOffButton(syncManager));
    }

    private IWidget creatMachineListButton(PanelSyncManager syncManager) {
        IPanelHandler machineListPanel = syncManager
            .panel("machineListPanel", (k, v) -> openMachineListPanel(syncManager), true);
        return new ButtonWidget<>().size(18, 18)
            .playClickSound(true)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_WHITELIST))
            .onMousePressed(mouseButton -> {
                machineListPanel.openPanel();
                return true;
            })
            .tooltipBuilder(t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.drone_open_list")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    private IWidget createOnButton(PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(18, 18)
            .playClickSound(true)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON))
            .syncHandler(new InteractionSyncHandler().setOnMousePressed(var -> {
                if (!NetworkUtils.isClient()) {
                    multiblock.turnOnAll();
                    syncManager.getPlayer()
                        .closeScreen();
                    syncManager.getPlayer()
                        .addChatComponentMessage(new ChatComponentTranslation("GT5U.machines.dronecentre.turnon"));
                }
            }))
            .tooltipBuilder(t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.drone_poweron_all")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    private IWidget createOffButton(PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(18, 18)
            .playClickSound(true)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF))
            .syncHandler(new InteractionSyncHandler().setOnMousePressed(var -> {
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
            }))
            .tooltipBuilder(t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.drone_poweroff_all")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    private ModularPanel openMachineListPanel(@NotNull PanelSyncManager syncManager) {
        DynamicSyncHandler droneListHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createListArea(syncManager, pSyncManager);
        });

        syncManager.findSyncHandler("sort", EnumSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("searchFilter", StringSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("searchOri", BooleanSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        int heightCoff = syncManager.isClient() ? Minecraft.getMinecraft().currentScreen.height - 40 : 0;
        return new ModularPanel("machineListPanel") {

            @Override
            public void onOpen(ModularScreen screen) {
                super.onOpen(screen);
                droneListHandler.notifyUpdate(packet -> {});
            }

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.width(270)
            .height(heightCoff)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .child(ButtonWidget.panelCloseButton())
            .child(
                Flow.column()
                    .widthRel(1)
                    .center()
                    .padding(8, 4)
                    .heightRel(0.95f)
                    .child(new TextWidget<>(IKey.lang("GT5U.gui.text.drone_title")).scale(2))
                    .child(createFunctionArea(syncManager))
                    .child(createConnectionArea(syncManager, droneListHandler)));
    }

    private IWidget createFunctionArea(PanelSyncManager syncManager) {
        return Flow.row()
            .heightRel(0.1f)
            .widthRel(0.95f)
            .childPadding(4)
            .marginBottom(4)
            .child(
                new CycleButtonWidget().size(16)
                    .value(syncManager.findSyncHandler("sort", EnumSyncValue.class))
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .stateOverlay(SortMode.NAME, GTGuiTextures.OVERLAY_BUTTON_TRANSPOSE)
                    .stateOverlay(SortMode.DISTANCE, GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE)
                    .stateOverlay(SortMode.STATUS, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF)
                    .tooltip(
                        t -> t.addLine(
                            IKey.dynamic(
                                () -> StatCollector.translateToLocal(
                                    "GT5U.gui.button.drone_" + multiblock.sortMode.toString()
                                        .toLowerCase())))))
            .child(
                new ToggleButton().size(16)
                    .value(syncManager.findSyncHandler("searchOri", BooleanSyncValue.class))
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                    .tooltip(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_searchoriname"))))
            .child(
                new TextFieldWidget().height(16)
                    .expanded()
                    .value(syncManager.findSyncHandler("searchFilter", StringSyncValue.class)));
    }

    private IWidget createConnectionArea(PanelSyncManager syncManager, DynamicSyncHandler droneListHandler) {
        DroneConnectionListSyncHandler droneConnectionListSyncHandler = syncManager
            .findSyncHandler("droneConnections", DroneConnectionListSyncHandler.class);
        DynamicSyncedWidget<?> dynamicWidget = new DynamicSyncedWidget<>().widthRel(0.95f)
            .expanded()
            .syncHandler(droneListHandler);
        droneConnectionListSyncHandler.setChangeListener(() -> {
            if (dynamicWidget.hasChildren()) {
                IWidget child = dynamicWidget.getChildren()
                    .get(0);
                if (child instanceof ListWidget list) {
                    this.lastScroll = list.getScrollY();
                }
            }
            droneListHandler.notifyUpdate(packet -> {});
        });
        return dynamicWidget;
    }

    private IWidget createListArea(PanelSyncManager syncManager, PanelSyncManager dynamicSyncManager) {
        DroneConnectionListSyncHandler droneConnectionListSyncHandler = syncManager
            .findSyncHandler("droneConnections", DroneConnectionListSyncHandler.class);
        List<DroneConnection> clientConnections = droneConnectionListSyncHandler.getValue();
        if (clientConnections.isEmpty()) {
            return new TextWidget<>(StatCollector.translateToLocal("GT5U.gui.text.drone_no_connection"))
                .alignment(Alignment.CENTER)
                .align(Alignment.CENTER)
                .widthRel(0.95f)
                .scale(2);
        } else {
            ListWidget<IWidget, ?> droneListWidget = new DroneListWidget<>().sizeRel(1);
            // Filter
            clientConnections = clientConnections.stream()
                .filter(
                    conn -> conn.getCustomName()
                        .toLowerCase()
                        .contains(
                            multiblock.getSearchBarText()
                                .toLowerCase())
                        || (multiblock.searchOriginalName && conn.getLocalizedName()
                            .toLowerCase()
                            .contains(
                                multiblock.getSearchBarText()
                                    .toLowerCase())))
                .collect(Collectors.toList());
            // Sort
            switch (multiblock.sortMode) {
                case NAME -> clientConnections = clientConnections.stream()
                    .sorted(
                        (o1, o2) -> Collator.getInstance(Locale.UK)
                            .compare(o1.getCustomName(), o2.getCustomName()))
                    .collect(Collectors.toList());
                case DISTANCE -> clientConnections = clientConnections.stream()
                    .sorted(Comparator.comparing(DroneConnection::getDistanceSquared))
                    .collect(Collectors.toList());
                case STATUS -> clientConnections = clientConnections.stream()
                    .sorted(
                        Comparator.comparing(DroneConnection::isMachineShutdown)
                            .reversed()
                            .thenComparing(DroneConnection::getCustomName))
                    .collect(Collectors.toList());
            }
            // Add row
            List<Flow> rows = new ArrayList<>();
            for (DroneConnection connection : clientConnections) {
                Flow row = Flow.row()
                    .widthRel(1)
                    .coverChildrenHeight()
                    .childPadding(4)
                    .paddingRight(4)
                    .marginBottom(2)
                    .child(
                        new ItemDisplayWidget().background(IDrawable.EMPTY)
                            .displayAmount(false)
                            .item(connection.getMachineItem())
                            .size(16)
                            .tooltip(var -> getTooltipFromItemSafely(var, connection.getMachineItem())))
                    .child(createHighLightButton(connection, syncManager))
                    .child(createTextButton(connection, dynamicSyncManager, droneConnectionListSyncHandler))
                    .child(createPowerControlButton(connection, droneConnectionListSyncHandler, dynamicSyncManager));
                if (connection.isMachineShutdown()) row.child(
                    new Widget<>().background(GTGuiTextures.OVERLAY_POWER_LOSS)
                        .tooltip(var -> var.add(connection.getShutdownReason())));
                row.child(createCustomNameTextField(connection, dynamicSyncManager, droneConnectionListSyncHandler));
                rows.add(row);
            }
            rows.forEach(droneListWidget::child);
            return droneListWidget;
        }
    }

    private IWidget createTextButton(DroneConnection connection, PanelSyncManager dynamicSyncManager,
        DroneConnectionListSyncHandler droneConnectionListSyncHandler) {
        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_PRINT)
            .syncHandler(
                dynamicSyncManager.getOrCreateSyncHandler(
                    "nameSync" + connection.uuid.toString(),
                    InteractionSyncHandler.class,
                    () -> new InteractionSyncHandler().setOnMousePressed(var -> {
                        if (!NetworkUtils.isClient()) {
                            multiblock.getConnectionList()
                                .stream()
                                .filter(c -> c.uuid.equals(connection.uuid))
                                .findFirst()
                                .ifPresent(c -> c.isSelected = !c.isSelected);
                            droneConnectionListSyncHandler.notifyUpdate();
                        }
                    })))
            .tooltip(t -> t.add(IKey.lang("GT5U.gui.button.drone_setname")));
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
                    .map(
                        con -> con.getLinkedMachine()
                            .map(MTEMultiBlockBase::isAllowedToWork)
                            .orElse(false))
                    .findFirst()
                    .orElse(false),
                bool -> droneConnectionListSyncHandler.getValue()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .findFirst()
                    .flatMap(DroneConnection::getLinkedMachine)
                    .ifPresent(mte -> {
                        if (bool) mte.enableWorking();
                        else if (mte.isAllowedToWork()) {
                            if (mte.maxProgresstime() > 0) {
                                mte.disableWorking();
                            } else mte.stopMachine(ShutDownReasonRegistry.NONE);
                        }
                    })));
        return new ToggleButton().value(powerSwitchSyncer)
            .size(16)
            .overlay(new DynamicDrawable(() -> {
                if (conn.machineStatus) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON;
                return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF;
            }));
    }

    protected IWidget createCustomNameTextField(DroneConnection conn, PanelSyncManager dynamicSyncManager,
        DroneConnectionListSyncHandler droneConnectionListSyncHandler) {
        if (!conn.isSelected) return new TextWidget<>(conn.getCustomName()).expanded();
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
                var -> multiblock.getConnectionList()
                    .stream()
                    .filter(connection -> connection.uuid.equals(conn.uuid))
                    .findFirst()
                    .ifPresent(c -> c.setCustomName(var))));
        return new TextFieldWidget().expanded()
            .value(nameSyncValue);
    }

    public static IWidget createHighLightButton(DroneConnection conn, PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
            .onMousePressed(mouseButton -> {
                EntityPlayer player = syncManager.getPlayer();
                MTEDroneCentre.highlightMachine(player, conn.getMachineCoord());
                player.closeScreen();
                return true;
            })
            .tooltip(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_highlight")));
    }

    private void getTooltipFromItemSafely(RichTooltip tooltipBuilder, ItemStack itemStack) {
        List<String> lines = itemStack
            .getTooltip(MCHelper.getPlayer(), MCHelper.getMc().gameSettings.advancedItemTooltips);

        for (int i = 0; i < lines.size(); ++i) {
            if (i == 0) {
                lines.set(
                    i,
                    itemStack.getItem()
                        .getRarity(itemStack).rarityColor + lines.get(i));
            } else {
                lines.set(i, EnumChatFormatting.GRAY + lines.get(i));
            }
        }
        tooltipBuilder.add(lines.get(0))
            .newLine();
        if (lines.size() > 1) {
            tooltipBuilder.spaceLine();
            int i = 1;

            for (int n = lines.size(); i < n; ++i) {
                tooltipBuilder.add(lines.get(i))
                    .newLine();
            }
        }
    }

    class DroneListWidget<I extends IWidget, W extends ListWidget<I, W>> extends ListWidget<I, W> {

        public DroneListWidget() {
            super();
        }

        @Override
        public void postResize() {
            super.postResize();
            if (lastScroll > 0) {
                if (getScrollArea().getScrollY() != null) {
                    getScrollArea().getScrollY()
                        .scrollTo(getScrollArea(), lastScroll);
                }
                lastScroll = 0;
            }
        }
    }

    public enum SortMode {
        DISTANCE,
        NAME,
        STATUS
    }
}
