package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GTUtility.formatNumbers;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
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
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
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
import gregtech.common.gui.modularui.synchandler.ProductionRecordSyncHandler;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import gregtech.common.tileentities.machines.multi.drone.production.ProductionRecord;

public class MTEDroneCentreGui extends MTEMultiBlockBaseGui<MTEDroneCentre> {

    private int lastScroll = 0;
    private int pageIndex;
    DynamicSyncHandler droneListHandler, groupHandler, productionHandler;
    IPanelHandler machineListPanel, productionRecordPanel;

    public MTEDroneCentreGui(MTEDroneCentre mteDroneCentre) {
        super(mteDroneCentre);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        DroneConnectionListSyncHandler droneConnectionListSyncHandler = new DroneConnectionListSyncHandler(
            multiblock::getConnectionList);
        EnumSyncValue<SortMode> sortModeSyncHandler = new EnumSyncValue<>(
            SortMode.class,
            multiblock::getSortMode,
            multiblock::setSortMode);
        StringSyncValue searchFilterSyncHandler = new StringSyncValue(
            multiblock::getSearchBarText,
            multiblock::setSearchBarText);
        ProductionRecordSyncHandler productionRecordSyncHandler = new ProductionRecordSyncHandler(
            () -> multiblock.productionDataRecorder);
        IntSyncValue selectTimeSyncHandler = new IntSyncValue(
            () -> multiblock.selectedTime,
            var -> multiblock.selectedTime = var);
        IntSyncValue activeGroupSyncHandler = new IntSyncValue(
            () -> multiblock.activeGroup,
            var -> multiblock.activeGroup = var);
        BooleanSyncValue searchOriSyncHandler = new BooleanSyncValue(
            multiblock::getSearchOriginalName,
            multiblock::setSearchOriginalName);
        BooleanSyncValue editModeSyncHandler = new BooleanSyncValue(multiblock::getEditMode, multiblock::setEditMode);
        GenericListSyncHandler<String> groupSyncHandler = new GenericListSyncHandler<>(
            () -> multiblock.group,
            null,
            packetBuffer -> packetBuffer.readStringFromBuffer(32768),
            PacketBuffer::writeStringToBuffer,
            String::equals,
            null);

        syncManager.syncValue("droneList", droneConnectionListSyncHandler);
        syncManager.syncValue("sort", sortModeSyncHandler);
        syncManager.syncValue("searchFilter", searchFilterSyncHandler);
        syncManager.syncValue("groupNameList", groupSyncHandler);
        syncManager.syncValue("selectTime", selectTimeSyncHandler);
        syncManager.syncValue("activeGroup", activeGroupSyncHandler);
        syncManager.syncValue("searchOri", searchOriSyncHandler);
        syncManager.syncValue("productionRecord", productionRecordSyncHandler);
        syncManager.syncValue("editMode", editModeSyncHandler);
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
        groupHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createGroupTab(syncManager, pSyncManager);
        });

        syncManager.findSyncHandler("droneList", DroneConnectionListSyncHandler.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("sort", EnumSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("searchFilter", StringSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("searchOri", BooleanSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("editMode", BooleanSyncValue.class)
            .setChangeListener(() -> {
                droneListHandler.notifyUpdate(packet -> {});
                groupHandler.notifyUpdate(packet -> {});
            });
        syncManager.findSyncHandler("activeGroup", IntSyncValue.class)
            .setChangeListener(() -> droneListHandler.notifyUpdate(packet -> {}));

        int heightCoff = syncManager.isClient() ? Minecraft.getMinecraft().currentScreen.height - 40 : 0;
        return new ModularPanel("machineListPanel") {

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
        }.width(270)
            .height(heightCoff)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .child(ButtonWidget.panelCloseButton())
            .child(
                new DynamicSyncedWidget<>().width(16)
                    .coverChildrenHeight()
                    .topRel(0.4f)
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
                    .child(createDynamicConnectionWidget(syncManager)));
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
            if (multiblock.editMode && multiblock.activeGroup == i && i != 0) {
                column.child(
                    new TextFieldWidget().size(16)
                        .value(
                            dynamicSyncManager.getOrCreateSyncHandler(
                                "groupName" + i,
                                StringSyncValue.class,
                                () -> new StringSyncValue(
                                    () -> groupSyncValue.getValue()
                                        .get(finalI),
                                    var -> multiblock.group.set(finalI, var)))));
                continue;
            }
            column.child(
                new SelectButton().size(16, 16)
                    .right(0)
                    .overlay(IKey.str(i == 0 ? "All" : String.valueOf(i)))
                    .tooltip(
                        var -> var.add(
                            groupSyncValue.getValue()
                                .get(finalI)))
                    .value(LinkedBoolValue.of(activeGroupSyncHandler, i)));
        }
        return column;
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
                new ToggleButton().size(16)
                    .value(syncManager.findSyncHandler("editMode", BooleanSyncValue.class))
                    .background(GTGuiTextures.BUTTON_STANDARD)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF)
                    .tooltip(t -> t.addLine(IKey.lang("GT5U.gui.button.drone_editmode"))))
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
            return createProductionArea(syncManager);
        });

        syncManager.findSyncHandler("selectTime", IntSyncValue.class)
            .setChangeListener(() -> productionHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("productionRecord", GenericSyncValue.class)
            .setChangeListener(() -> productionHandler.notifyUpdate(packet -> {}));
        syncManager.findSyncHandler("groupNameList", GenericListSyncHandler.class)
            .setChangeListener(() -> groupHandler.notifyUpdate(packet -> {}));

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
                    .marginBottom(4)
                    .childPadding(4)
                    .child(
                        new ToggleButton().size(18)
                            .background(GTGuiTextures.BUTTON_STANDARD)
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                            .value(new BooleanSyncValue(() -> multiblock.productionDataRecorder.active, var -> {
                                multiblock.productionDataRecorder.active = var;
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

    private IWidget createProductionArea(PanelSyncManager syncManager) {
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
            .setEnabledIf(w -> multiblock.productionDataRecorder.active);
        createPages(syncManager, productionPage);

        return Flow.column()
            .sizeRel(0.95f, 0.95f)
            .center()
            .childPadding(2)
            .marginTop(4)
            .child(createTimeButton(syncManager))
            .child(
                Flow.row()
                    .widthRel(1)
                    .height(18)
                    .marginBottom(4)
                    .setEnabledIf(w -> multiblock.productionDataRecorder.active)
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
        GenericSyncValue<ProductionRecord> productionRecord = ((GenericSyncValue<ProductionRecord>) syncManager
            .findSyncHandler("productionRecord"));
        ProductionRecord clientProductionDataRecorder = productionRecord.getValue();
        List<DroneConnection> droneConnectionList = syncManager
            .findSyncHandler("droneList", DroneConnectionListSyncHandler.class)
            .getValue();

        long euConsumed = clientProductionDataRecorder.getEnergyStatsIn(multiblock.selectedTime * 1000L);
        Map<ItemStack, Long> itemProduced = clientProductionDataRecorder
            .getItemStatsIn(multiblock.selectedTime * 1000L);
        Map<FluidStack, Long> fluidConsumed = clientProductionDataRecorder
            .getFluidStatsIn(multiblock.selectedTime * 1000L);
        String time = String.valueOf(multiblock.selectedTime)
            .replace("-1", "All");
        productionPage.addPage(
            Flow.column()
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
                            .setEnabledIf(w -> multiblock.selectedTime != -1))
                    .child(
                        IKey.str(
                            formatNumbers(euConsumed / multiblock.selectedTime) + " EU/t "
                                + GTUtility.getTierNameWithParentheses(euConsumed / multiblock.selectedTime))
                            .asWidget()
                            .setEnabledIf(w -> multiblock.selectedTime != -1)))
            // Todo: Maybe we can put a line-chart here?
            // .child(createSummaryGrid(droneConnectionList)))
            .addPage(
                Flow.column()
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
        Flow row = Flow.row()
            .widthRel(1)
            .height(18)
            .childPadding(4)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .setEnabledIf(w -> multiblock.productionDataRecorder.active);

        Map<String, Integer> timeOptions = new java.util.LinkedHashMap<>();
        timeOptions.put("10s", 10);
        timeOptions.put("1min", 60);
        timeOptions.put("10min", 600);
        timeOptions.put("1h", 3600);
        timeOptions.put("24h", 86400);
        timeOptions.put("All", -1);

        timeOptions.forEach(
            (label, time) -> row.child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, time))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang(label))));

        return row.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            productionHandler.notifyUpdate(packet -> {});
            return true;
        })
            .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
            .tooltip(t -> t.addLine(IKey.lang("GT5U.gui.tooltip.drone_production_refresh"))));
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

        List<Flow> cells = new ArrayList<>();
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
            .minElementMarginBottom(2)
            .widthRel(1)
            .scrollable(new VerticalScrollData())
            .expanded();
    }

    private <T> IWidget createStatsGrid(Map<T, Long> data, int childPadding,
        Function<T, IWidget> displayWidgetFactory) {
        if (data.isEmpty()) {
            return new EmptyWidget();
        }
        List<IWidget> cells = data.entrySet()
            .stream()
            .map(entry -> {
                T item = entry.getKey();
                Long stackSize = entry.getValue();
                return (IWidget) Flow.row()
                    .childPadding(childPadding)
                    .align(Alignment.CenterLeft)
                    .coverChildren()
                    .paddingRight(2)
                    .child(displayWidgetFactory.apply(item))
                    .child(
                        new TextWidget<>(formatValueWithUnits(stackSize))
                            .tooltip(builder -> builder.add(formatNumbers(stackSize))));
            })
            .collect(Collectors.toList());

        return new Grid().mapTo(5, cells)
            .minElementMarginBottom(2)
            .widthRel(1)
            .scrollable(new VerticalScrollData())
            .expanded();
    }

    private IWidget createItemGrid(Map<ItemStack, Long> itemList) {
        return createStatsGrid(
            itemList,
            0,
            itemStack -> new ItemDisplayWidget().item(itemStack)
                .displayAmount(false)
                .size(16)
                .tooltip(builder -> getTooltipFromItemSafely(builder, itemStack)));
    }

    private IWidget createFluidGrid(Map<FluidStack, Long> fluidList) {
        return createStatsGrid(
            fluidList,
            4,
            fluidStack -> new FluidDisplayWidget().fluid(fluidStack)
                .displayAmount(false)
                .size(16)
                .tooltip(builder -> builder.addFromFluid(fluidStack)));
    }

    private IWidget createDynamicConnectionWidget(PanelSyncManager syncManager) {
        DroneConnectionListSyncHandler droneConnectionListSyncHandler = syncManager
            .findSyncHandler("droneList", DroneConnectionListSyncHandler.class);
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
            ListWidget<IWidget, ?> droneListWidget = new DroneListWidget<>().sizeRel(1);

            final Comparator<DroneConnection> sorter = switch (multiblock.sortMode) {
                case NAME -> (o1, o2) -> Collator.getInstance(Locale.UK)
                    .compare(o1.getCustomName(), o2.getCustomName());
                case DISTANCE -> Comparator.comparing(DroneConnection::getDistanceSquared);
                case STATUS -> Comparator.comparing(DroneConnection::isMachineShutdown)
                    .reversed();
            };

            List<Flow> rows = clientConnections.stream()
                .filter(
                    conn -> (conn.getCustomName()
                        .toLowerCase()
                        .contains(
                            multiblock.getSearchBarText()
                                .toLowerCase())
                        || (multiblock.searchOriginalName && conn.getLocalizedName()
                            .toLowerCase()
                            .contains(
                                multiblock.getSearchBarText()
                                    .toLowerCase())))
                        && (conn.group == multiblock.activeGroup || multiblock.editMode))
                .sorted(sorter)
                .map(connection -> {
                    Flow row = Flow.row()
                        .widthRel(1)
                        .coverChildrenHeight()
                        .childPadding(4)
                        .paddingRight(4)
                        .marginBottom(2);
                    if (multiblock.getEditMode())
                        row.child(createGroupButton(connection, droneConnectionListSyncHandler, dynamicSyncManager));
                    row.child(
                        new ItemDisplayWidget().background(IDrawable.EMPTY)
                            .displayAmount(false)
                            .item(connection.getMachineItem())
                            .size(16)
                            .tooltip(var -> getTooltipFromItemSafely(var, connection.getMachineItem())))
                        .child(createHighLightButton(connection, syncManager))
                        .child(createTextButton(connection, dynamicSyncManager, droneConnectionListSyncHandler))
                        .child(
                            createPowerControlButton(connection, droneConnectionListSyncHandler, dynamicSyncManager));
                    if (connection.isMachineShutdown()) row.child(
                        new Widget<>().background(GTGuiTextures.OVERLAY_POWER_LOSS)
                            .tooltip(var -> var.add(connection.getShutdownReason())));
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
                    .map(con -> con.group == multiblock.activeGroup)
                    .findFirst()
                    .orElse(false),
                bool -> multiblock.getConnectionList()
                    .stream()
                    .filter(c -> c.uuid.equals(conn.uuid))
                    .findFirst()
                    .ifPresent(con -> con.group = bool ? multiblock.activeGroup : 0)));
        IDrawable shape = UITexture.fullImage(GregTech.ID, "gui/overlay_slot/slice_shape")
            .asIcon()
            .size(8);
        return new ToggleButton().value(groupSyncHandler)
            .size(16)
            .background(IDrawable.EMPTY)
            .overlay(true, shape, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
            .overlay(false, shape);
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
                bool -> multiblock.getConnectionList()
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
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
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
