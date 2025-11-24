package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.util.GTUtility.formatNumbers;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

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
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.synchandler.DroneConnectionListSyncHandler;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import gregtech.common.tileentities.machines.multi.drone.ProductionDataRecorder;

public class MTEDroneCentreGui extends MTEMultiBlockBaseGui<MTEDroneCentre> {

    private int lastScroll = 0;
    private int pageIndex;
    DynamicSyncHandler droneListHandler, productionHandler;
    IPanelHandler machineListPanel, productionRecordPanel;

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
        syncManager.syncValue(
            "productionInfo",
            new GenericSyncValue<>(
                () -> multiblock.productionDataRecorder,
                null,
                ProductionDataRecorder::deserialize,
                ProductionDataRecorder::serialize,
                ProductionDataRecorder::isEqual,
                null));
        syncManager
            .syncValue("selectTime", new IntSyncValue(() -> multiblock.selectTime, var -> multiblock.selectTime = var));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(creatMachineListButton(syncManager))
            .child(createOnButton(syncManager))
            .child(createOffButton(syncManager));
    }

    private IWidget creatMachineListButton(PanelSyncManager syncManager) {
        productionRecordPanel = syncManager.panel("productionPanel", (k, v) -> openProductionPanel(syncManager), true);
        machineListPanel = syncManager.panel("machineListPanel", (k, v) -> openMachineListPanel(syncManager), true);
        return new ButtonWidget<>().size(18, 18)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_WHITELIST))
            .onMousePressed(mouseButton -> {
                machineListPanel.openPanel();
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_open_list")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    private IWidget createOnButton(PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(18, 18)
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
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweron_all")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    private IWidget createOffButton(PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(18, 18)
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
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_poweroff_all")))
            .setEnabledIf(widget -> baseMetaTileEntity.isActive());
    }

    private ModularPanel openMachineListPanel(PanelSyncManager syncManager) {
        droneListHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
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
                    .child(
                        IKey.lang("GT5U.gui.text.drone_title")
                            .asWidget()
                            .scale(2))
                    .child(createFunctionArea(syncManager))
                    .child(createDynamicConnectionWidget(syncManager)));
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
                new ButtonWidget<>().size(16)
                    .overlay(
                        new DrawableStack(
                            GTGuiTextures.BUTTON_STANDARD,
                            GTGuiTextures.OVERLAY_BUTTON_REDSTONESNIFFERLOCATE.asIcon()
                                .size(15, 15)))
                    .onMousePressed(mouseButton -> {
                        productionRecordPanel.openPanel();
                        return true;
                    })
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_production"))))
            .child(
                new TextFieldWidget().height(16)
                    .expanded()
                    .value(syncManager.findSyncHandler("searchFilter", StringSyncValue.class)));
    }

    private ModularPanel openProductionPanel(PanelSyncManager syncManager) {
        productionHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createProductionArea(syncManager, pSyncManager);
        });

        syncManager.findSyncHandler("selectTime", IntSyncValue.class)
            .setChangeListener(() -> productionHandler.notifyUpdate(packet -> {}));

        int heightCoff = syncManager.isClient() ? Minecraft.getMinecraft().currentScreen.height - 40 : 0;

        return new ModularPanel("productionPanel") {

            @Override
            public void onOpen(ModularScreen screen) {
                super.onOpen(screen);
                productionHandler.notifyUpdate(packet -> {});
            }

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(270, heightCoff)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .child(ButtonWidget.panelCloseButton())
            .child(
                Flow.row()
                    .widthRel(0.95f)
                    .height(18)
                    .top(4)
                    .left(4)
                    .childPadding(4)
                    .child(
                        new ToggleButton().size(18)
                            .background(GTGuiTextures.BUTTON_STANDARD)
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                            .value(new BooleanSyncValue(() -> multiblock.productionDataRecorder.isActive(), var -> {
                                multiblock.productionDataRecorder.setActive(var);
                                productionHandler.notifyUpdate(packet -> {});
                            })))
                    .child(
                        IKey.lang("GT5U.gui.text.drone_active_production")
                            .asWidget()
                            .tooltip(t -> t.addLine(IKey.lang("GT5U.gui.tooltip.drone_active_production")))))
            .child(
                new DynamicSyncedWidget<>().widthRel(1)
                    .pos(0, 20)
                    .expanded()
                    .syncHandler(productionHandler));
    }

    private IWidget createProductionArea(PanelSyncManager syncManager, PanelSyncManager dynamicSyncManager) {
        PagedWidget.Controller controller = new PagedWidget.Controller() {

            @Override
            public void setPage(int page) {
                super.setPage(page);
                pageIndex = page;
            }
        };
        PagedWidget<?> productionPage = new PagedWidget() {

            @Override
            public void afterInit() {
                setPage(pageIndex);
            }
        };
        productionPage.controller(controller)
            .widthRel(0.95f)
            .expanded()
            .setEnabledIf(w -> multiblock.productionDataRecorder.isActive());
        createPages(syncManager, productionPage);

        return Flow.column()
            .sizeRel(0.95f, 0.95f)
            .center()
            .childPadding(2)
            .child(
                Flow.row()
                    .widthRel(1)
                    .height(18)
                    .childPadding(4)
                    .setEnabledIf(w -> multiblock.productionDataRecorder.isActive())
                    .child(
                        IKey.lang("GT5U.gui.text.drone_maxrecord")
                            .asWidget())
                    .child(
                        new TextFieldWidget().setNumbers(1, 100000)
                            .value(
                                dynamicSyncManager.getOrCreateSyncHandler(
                                    "maxRecord",
                                    IntSyncValue.class,
                                    () -> new IntSyncValue(
                                        () -> multiblock.productionDataRecorder.maxRecords,
                                        var -> multiblock.productionDataRecorder.maxRecords = var)))
                            .tooltip(var -> var.add(IKey.lang("GT5U.gui.tooltip.drone_maxrecord"))))
                    .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                        productionHandler.notifyUpdate(packet -> {});
                        return true;
                    })
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                        .tooltip(t -> t.addLine(IKey.lang("GT5U.gui.tooltip.drone_production_refresh")))))
            .child(createTimeButton(syncManager))
            .child(
                Flow.row()
                    .widthRel(1)
                    .height(18)
                    .marginBottom(4)
                    .setEnabledIf(w -> multiblock.productionDataRecorder.isActive())
                    .child(
                        new PageButton(0, controller).widthRel(0.25f)
                            .overlay(IKey.lang("GT5U.gui.text.drone_production_connection")))
                    .child(
                        new PageButton(1, controller).widthRel(0.25f)
                            .overlay(IKey.lang("GT5U.gui.text.drone_production_power")))
                    .child(
                        new PageButton(2, controller).widthRel(0.25f)
                            .overlay(IKey.lang("GT5U.gui.text.drone_production_item")))
                    .child(
                        new PageButton(3, controller).widthRel(0.25f)
                            .overlay(IKey.lang("GT5U.gui.text.drone_production_fluid"))))
            .child(productionPage);
    }

    private void createPages(PanelSyncManager syncManager, PagedWidget<?> productionPage) {
        ProductionDataRecorder clientProductionDataRecorder = ((GenericSyncValue<ProductionDataRecorder>) syncManager
            .findSyncHandler("productionInfo")).getValue();
        List<DroneConnection> droneConnectionList = syncManager
            .findSyncHandler("droneConnections", DroneConnectionListSyncHandler.class)
            .getValue();

        long euConsumed = clientProductionDataRecorder.getEuConsumedIn(multiblock.selectTime);
        LinkedHashMap<ItemStack, Long> itemProduced = clientProductionDataRecorder
            .getItemsProducedIn(multiblock.selectTime);
        LinkedHashMap<FluidStack, Long> fluidConsumed = clientProductionDataRecorder
            .getFluidsProducedIn(multiblock.selectTime);
        String time = multiblock.selectTime == -1 ? "All" : String.valueOf(multiblock.selectTime);
        productionPage.addPage(
            Flow.column()
                .padding(4)
                .childPadding(2)
                .widthRel(1)
                .expanded()
                .child(
                    IKey.lang("GT5U.gui.text.drone_connectionCount", droneConnectionList.size())
                        .asWidget())
                .child(
                    IKey.lang("GT5U.gui.text.drone_connectionList")
                        .asWidget()
                        .marginBottom(4))
                .child(createMachineGrid(droneConnectionList)))
            .addPage(
                Flow.column()
                    .childPadding(4)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_power_total", time)
                            .asWidget())
                    .child(
                        IKey.str(formatNumbers(euConsumed) + " EU")
                            .asWidget())
                    .child(
                        IKey.lang("GT5U.gui.text.drone_average", time)
                            .asWidget()
                            .setEnabledIf(w -> multiblock.selectTime != -1))
                    .child(
                        IKey.str(
                            formatNumbers(euConsumed / multiblock.selectTime) + " EU/t "
                                + GTUtility.getTierNameWithParentheses(euConsumed / multiblock.selectTime))
                            .asWidget()
                            .setEnabledIf(w -> multiblock.selectTime != -1)))
            // Todo: Maybe we can put a line-chart here?
            // .child(createSummaryGrid(droneConnectionList)))
            .addPage(
                Flow.column()
                    .padding(4)
                    .childPadding(2)
                    .widthRel(1)
                    .expanded()
                    .child(
                        IKey.lang("GT5U.gui.text.drone_item_total", time)
                            .asWidget()
                            .marginBottom(4))
                    .child(createItemGrid(itemProduced)))
            .addPage(
                Flow.column()
                    .padding(4)
                    .childPadding(2)
                    .widthRel(1)
                    .expanded()
                    .child(
                        IKey.lang("GT5U.gui.text.drone_fluid_total", time)
                            .asWidget())
                    .child(createFluidGrid(fluidConsumed)));
    }

    private IWidget createTimeButton(PanelSyncManager syncManager) {
        IntSyncValue selectTime = syncManager.findSyncHandler("selectTime", IntSyncValue.class);
        int TIME_HEIGHT = 35;
        return Flow.row()
            .widthRel(1)
            .height(18)
            .childPadding(4)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .setEnabledIf(w -> multiblock.productionDataRecorder.isActive())
            .child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, 100))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang("5s")))
            .child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, 1200))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang("1min")))
            .child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, 12000))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang("10min")))
            .child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, 72000))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang("1h")))
            .child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, 1728000))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang("24h")))
            .child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, -1))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang("All")));
    }

    private IWidget createMachineGrid(List<DroneConnection> connections) {
        if (connections.isEmpty()) {
            return new EmptyWidget();
        }
        HashMap<String, ItemStack> machineStack = new HashMap<>();
        connections.forEach(connection -> {
            ItemStack machine = connection.getMachineItem();
            String key = Item.itemRegistry.getNameForObject(machine.getItem()) + ":" + machine.getItemDamage();
            ItemStack result = machineStack.get(key);
            if (result == null)
                machineStack.put(key, new ItemStack(machine.getItem(), machine.stackSize, machine.getItemDamage()));
            else result.stackSize += machine.stackSize;
        });

        List<IWidget> cells = new ArrayList<>();
        machineStack.forEach((key, itemStack) -> {
            Flow cell = Flow.row()
                .childPadding(4)
                .align(Alignment.CenterLeft)
                .coverChildren()
                .paddingRight(2)
                .child(
                    new ItemDisplayWidget().item(itemStack)
                        .displayAmount(false)
                        .size(16)
                        .tooltip(builder -> getTooltipFromItemSafely(builder, itemStack)));
            cell.child(new TextWidget<>(": " + itemStack.stackSize));
            cells.add(cell);
        });
        return new Grid().mapTo(6, cells)
            .marginTop(24)
            .minElementMarginRight(4)
            .minElementMarginBottom(2)
            .widthRel(1)
            .coverChildrenHeight()
            .align(Alignment.CenterLeft);
    }

    private IWidget createItemGrid(LinkedHashMap<ItemStack, Long> itemList) {
        if (itemList.isEmpty()) {
            return new EmptyWidget();
        }
        List<IWidget> cells = new ArrayList<>();
        itemList.forEach((itemStack, stackSize) -> {
            Flow cell = Flow.row()
                .align(Alignment.CenterLeft)
                .coverChildren()
                .paddingRight(2)
                .child(
                    new ItemDisplayWidget().item(itemStack)
                        .displayAmount(false)
                        .size(16)
                        .tooltip(builder -> getTooltipFromItemSafely(builder, itemStack)));
            cell.child(
                new TextWidget<>(formatValueWithUnits(stackSize))
                    .tooltip(builder -> builder.add(formatNumbers(stackSize))));
            cells.add(cell);
        });
        return new Grid().mapTo(4, cells)
            .marginTop(24)
            .minElementMarginRight(4)
            .minElementMarginBottom(2)
            .widthRel(1)
            .coverChildrenHeight()
            .align(Alignment.CenterLeft);
    }

    private IWidget createFluidGrid(LinkedHashMap<FluidStack, Long> fluidList) {
        if (fluidList.isEmpty()) {
            return new EmptyWidget();
        }
        List<IWidget> cells = new ArrayList<>();
        fluidList.forEach((fluidStack, stackSize) -> {
            Flow cell = Flow.row()
                .childPadding(4)
                .align(Alignment.CenterLeft)
                .coverChildren()
                .paddingRight(2)
                .child(
                    new FluidDisplayWidget().fluid(fluidStack)
                        .displayAmount(false)
                        .size(16)
                        .tooltip(builder -> builder.addFromFluid(fluidStack)));
            cell.child(new TextWidget<>(": " + stackSize));
            cells.add(cell);
        });
        return new Grid().mapTo(6, cells)
            .marginTop(24)
            .minElementMarginRight(4)
            .minElementMarginBottom(2)
            .widthRel(1)
            .coverChildrenHeight()
            .align(Alignment.CenterLeft);
    }

    private IWidget createDynamicConnectionWidget(PanelSyncManager syncManager) {
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
            return IKey.lang("GT5U.gui.text.drone_no_connection")
                .asWidget()
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
                                mte.getBaseMetaTileEntity()
                                    .setShutdownStatus(true);
                                mte.getBaseMetaTileEntity()
                                    .setShutDownReason(ShutDownReasonRegistry.NONE);
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
        if (!conn.isSelected) return IKey.str(conn.getCustomName())
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

    public String formatValueWithUnits(long value) {
        if (value < 1000) {
            return String.valueOf(value);
        }
        int exp = (int) (Math.log(value) / Math.log(1000));
        char unit = "KMGTP".charAt(exp - 1);
        double formattedValue = value / Math.pow(1000, exp);
        if (formattedValue == Math.floor(formattedValue)) {
            return String.format(Locale.ROOT, "%.0f%c", formattedValue, unit);
        } else {
            return String.format(Locale.ROOT, "%.1f%c", formattedValue, unit);
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
