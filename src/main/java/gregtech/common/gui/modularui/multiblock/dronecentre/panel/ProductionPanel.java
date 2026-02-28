package gregtech.common.gui.modularui.multiblock.dronecentre.panel;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.DroneConnectionListSyncHandler;
import gregtech.common.gui.modularui.multiblock.dronecentre.sync.ProductionStatsSyncHandler;
import gregtech.common.gui.modularui.widget.UpdatableToggleButton;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import gregtech.common.tileentities.machines.multi.drone.production.RecordUtil;
import gregtech.common.tileentities.machines.multi.drone.production.StatsBundle;

public class ProductionPanel extends ModularPanel {

    private final MTEDroneCentre centre;

    private int pageIndex;
    private final DynamicSyncHandler productionHandler;
    private final ProductionStatsSyncHandler statsSyncHandler;

    public ProductionPanel(PanelSyncManager syncManager, MTEDroneCentre centre) {
        super("productionPanel");
        this.centre = centre;
        productionHandler = new DynamicSyncHandler().widgetProvider((pSyncManager, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return createProductionArea(syncManager, pSyncManager);
        });

        statsSyncHandler = syncManager.findSyncHandler("productionStats", ProductionStatsSyncHandler.class);
        syncManager.findSyncHandler("selectTime", IntSyncValue.class)
            .setChangeListener(statsSyncHandler::notifyUpdate);
        statsSyncHandler.setChangeListener(() -> productionHandler.notifyUpdate(packet -> {}));

        int heightCoff = syncManager.isClient() ? Minecraft.getMinecraft().currentScreen.height - 40 : 0;

        this.size(270, heightCoff)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .child(ButtonWidget.panelCloseButton())
            .child(
                new Column().margin(10)
                    .child(
                        Flow.row()
                            .widthRel(0.95f)
                            .height(18)
                            .childPadding(4)
                            .child(
                                new UpdatableToggleButton(productionHandler).size(18)
                                    .background(GTGuiTextures.BUTTON_STANDARD)
                                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                                    .value(
                                        new BooleanSyncValue(
                                            () -> this.centre.productionDataRecorder.isActive(),
                                            var -> this.centre.productionDataRecorder.setActive(var))))
                            .child(
                                IKey.lang("GT5U.gui.text.drone_active_production")
                                    .asWidget()
                                    .tooltipBuilder(
                                        t -> t.addLine(IKey.lang("GT5U.gui.tooltip.drone_active_production")))))
                    .child(
                        new DynamicSyncedWidget<>().widthRel(1)
                            .heightRel(0.9f)
                            .syncHandler(productionHandler)));
    }

    @Override
    public void onOpen(ModularScreen screen) {
        super.onOpen(screen);
        productionHandler.notifyUpdate(packet -> {});
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    private IWidget createProductionArea(PanelSyncManager syncManager, PanelSyncManager pSyncManager) {
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
            .widthRel(1f)
            .expanded()
            .setEnabledIf(w -> centre.productionDataRecorder.isActive());
        createPages(syncManager, productionPage);

        return Flow.column()
            .sizeRel(0.95f, 0.95f)
            .center()
            .childPadding(2)
            .marginTop(4)
            .child(createTimeButton(syncManager, pSyncManager))
            .child(
                Flow.row()
                    .widthRel(1)
                    .height(18)
                    .marginBottom(4)
                    .setEnabledIf(w -> centre.productionDataRecorder.isActive())
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
        StatsBundle clientStatsBundle = statsSyncHandler.getValue();
        List<DroneConnection> droneConnectionList = syncManager
            .findSyncHandler("droneList", DroneConnectionListSyncHandler.class)
            .getValue();

        int selectedTime = centre.getSelectedTime();

        long euConsumedRaw = 0;
        Map<ItemStack, Long> itemProducedUnsorted = new HashMap<>();
        Map<FluidStack, Long> fluidConsumedUnsorted = new HashMap<>();

        for (long key : clientStatsBundle.data.keys()) {
            long value = clientStatsBundle.data.get(key);
            if (RecordUtil.isEnergy(key)) {
                euConsumedRaw += value;
            } else if (RecordUtil.isItem(key)) {
                Item item = Item.getItemById(RecordUtil.getId(key));
                if (item != null) {
                    ItemStack is = new ItemStack(item, 1, RecordUtil.getMeta(key));
                    itemProducedUnsorted.put(is, value);
                }
            } else if (RecordUtil.isFluid(key)) {
                Fluid fluid = FluidRegistry.getFluid(RecordUtil.getId(key));
                if (fluid != null) {
                    FluidStack fs = new FluidStack(fluid, 1);
                    fluidConsumedUnsorted.put(fs, value);
                }
            }
        }

        long euConsumed = -euConsumedRaw;

        Map<ItemStack, Long> itemProduced = itemProducedUnsorted.entrySet()
            .stream()
            .sorted(
                Map.Entry.<ItemStack, Long>comparingByValue()
                    .reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map<FluidStack, Long> fluidConsumed = fluidConsumedUnsorted.entrySet()
            .stream()
            .sorted(
                Map.Entry.<FluidStack, Long>comparingByValue()
                    .reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        String time = DroneCentreGuiUtil.TIME_OPTIONS.get(selectedTime);
        productionPage.addPage(
            Flow.column()
                .childPadding(2)
                .expanded()
                .child(
                    IKey.lang("GT5U.gui.text.drone_connectionCount", droneConnectionList.size())
                        .asWidget())
                .child(
                    IKey.lang(
                        droneConnectionList.isEmpty() ? "GT5U.gui.text.drone_no_connection"
                            : "GT5U.gui.text.drone_connectionList")
                        .asWidget()
                        .marginBottom(4))
                .child(createMachineGrid(droneConnectionList)))
            .addPage(
                Flow.column()
                    .childPadding(4)
                    .sizeRel(1)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_power_total", time)
                            .asWidget())
                    .child(
                        IKey.str(formatNumber(euConsumed) + " EU")
                            .asWidget())
                    .child(
                        IKey.lang("GT5U.gui.text.drone_average", time)
                            .asWidget()
                            .setEnabledIf(w -> selectedTime != -1))
                    .child(
                        IKey.str(
                            formatNumber(euConsumed / selectedTime) + " EU/t "
                                + GTUtility.getTierNameWithParentheses(euConsumed / selectedTime))
                            .asWidget()
                            .setEnabledIf(w -> selectedTime != -1)))
            // Todo: Maybe we can put a line-chart here?
            // .child(createSummaryGrid(droneConnectionList)))
            .addPage(
                Flow.column()
                    .childPadding(2)
                    .sizeRel(1)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_item_total", time)
                            .asWidget()
                            .marginBottom(4))
                    .child(createItemGrid(itemProduced)))
            .addPage(
                Flow.column()
                    .childPadding(2)
                    .sizeRel(1)
                    .child(
                        IKey.lang("GT5U.gui.text.drone_fluid_total", time)
                            .asWidget())
                    .child(createFluidGrid(fluidConsumed)));
    }

    private IWidget createTimeButton(PanelSyncManager syncManager, PanelSyncManager pSyncManager) {
        IntSyncValue selectTime = syncManager.findSyncHandler("selectTime", IntSyncValue.class);
        int TIME_HEIGHT = 35;
        Flow row = Flow.row()
            .widthRel(1)
            .height(18)
            .childPadding(4)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .setEnabledIf(w -> centre.productionDataRecorder.isActive());

        DroneCentreGuiUtil.TIME_OPTIONS.forEach(
            (time, label) -> row.child(
                new SelectButton().value(LinkedBoolValue.of(selectTime, time))
                    .width(TIME_HEIGHT)
                    .overlay(IKey.lang(label))));
        return row.child(
            new ButtonWidget<>().syncHandler(
                pSyncManager.getOrCreateSyncHandler(
                    "clear",
                    InteractionSyncHandler.class,
                    () -> new InteractionSyncHandler().setOnMousePressed(var -> {
                        if (!NetworkUtils.isClient()) {
                            centre.productionDataRecorder.clear();
                            statsSyncHandler.notifyUpdate();
                        }
                    })))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.tooltip.drone_production_clear"))));
    }

    private IWidget createMachineGrid(List<DroneConnection> connections) {
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
                        .tooltipBuilder(builder -> DroneCentreGuiUtil.getTooltipFromItemSafely(builder, itemStack)));
            cell.child(new TextWidget<>(": " + itemStack.stackSize));
            cells.add(cell);
        });
        return new Grid().mapTo(6, cells)
            .minElementMarginBottom(2)
            .widthRel(1)
            .expanded()
            .scrollable(new VerticalScrollData());
    }

    private IWidget createItemGrid(Map<ItemStack, Long> itemList) {
        return createStatsGrid(
            itemList,
            0,
            itemStack -> new ItemDisplayWidget().item(itemStack)
                .displayAmount(false)
                .size(16)
                .tooltipBuilder(builder -> DroneCentreGuiUtil.getTooltipFromItemSafely(builder, itemStack)));
    }

    private IWidget createFluidGrid(Map<FluidStack, Long> fluidList) {
        return createStatsGrid(
            fluidList,
            4,
            fluidStack -> new FluidDisplayWidget().value(fluidStack)
                .displayAmount(false)
                .size(16)
                .tooltipBuilder(builder -> builder.addFromFluid(fluidStack)));
    }

    private <T> IWidget createStatsGrid(Map<T, Long> data, int childPadding,
        Function<T, IWidget> displayWidgetFactory) {
        if (data.isEmpty()) {
            return IKey.lang("GT5U.gui.text.drone_no_data")
                .asWidget();
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
                        new TextWidget<>(DroneCentreGuiUtil.formatValueWithUnits(stackSize))
                            .tooltipBuilder(builder -> builder.add(formatNumber(stackSize))));
            })
            .collect(Collectors.toList());

        return new Grid().mapTo(5, cells)
            .minElementMarginBottom(2)
            .sizeRel(1)
            .scrollable(new VerticalScrollData());
    }
}
