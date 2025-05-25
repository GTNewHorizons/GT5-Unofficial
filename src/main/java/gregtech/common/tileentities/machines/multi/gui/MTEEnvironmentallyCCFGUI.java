package gregtech.common.tileentities.machines.multi.gui;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTEEnvironmentallyControlledChemicalFacility;

public class MTEEnvironmentallyCCFGUI extends MTEMultiBlockBaseGui {

    private final MTEEnvironmentallyControlledChemicalFacility base;

    public MTEEnvironmentallyCCFGUI(MTEEnvironmentallyControlledChemicalFacility base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "Parallel_Left",
            new IntSyncValue(() -> base.parallelModuleTierL, dub -> base.parallelModuleTierL = dub));
        syncManager.syncValue(
            "Parallel_Right",
            new IntSyncValue(() -> base.parallelModuleTierR, dub -> base.parallelModuleTierR = dub));
        syncManager.syncValue(
            "Pressure",
            new DoubleSyncValue(() -> base.ECCFCurrentPressure, dub -> base.ECCFCurrentPressure = dub));
        syncManager.syncValue(
            "Temperature",
            new DoubleSyncValue(() -> base.ECCFCurrentTemp, dub -> base.ECCFCurrentTemp = dub));
        syncManager
            .syncValue("Compress", new IntSyncValue(() -> base.compressCoilTier, dub -> base.compressCoilTier = dub));
        syncManager.syncValue("Vacuum", new IntSyncValue(() -> base.vacuumCoilTier, dub -> base.vacuumCoilTier = dub));
        syncManager.syncValue("Heat", new IntSyncValue(() -> base.heatCoilTier, dub -> base.heatCoilTier = dub));
        syncManager.syncValue("Cool", new IntSyncValue(() -> base.coolCoilTier, dub -> base.coolCoilTier = dub));
        syncManager.syncValue("InitTemp", new DoubleSyncValue(() -> base.initialTemp, dub -> base.initialTemp = dub));
        syncManager
            .syncValue("InitPres", new DoubleSyncValue(() -> base.initialPressure, dub -> base.initialPressure = dub));
    }

    @Override
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel ui = super.build(data, syncManager, uiSettings);
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createECCFPanel(syncManager), true);

        return ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            if (!popupPanel.isPanelOpen()) {
                popupPanel.openPanel();
            } else {
                popupPanel.closePanel();
            }
            return true;
        })
            .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_THERMOMETER)
            .disableHoverBackground()
            .tooltip(tooltip -> tooltip.add("Temperature Control"))
            .pos(156, 102)
            .size(18, 18));
    }

    public String pressureConverter(double pressure) {
        if (pressure > 1e7) return String.format("%.1f MPa", pressure / 1e6);
        if (pressure > 1e4) return String.format("%.1f kPa", pressure / 1e3);
        return String.format("%.2f kPa", pressure);
    }

    public String temperatureConverter(double temp) {
        if (temp > 1e4) return String.format("%.1f kK", temp / 1e3);
        return String.format("%.1f K", temp);
    }

    private ParentWidget<?> addTextWidgets(ParentWidget<?> parent, IKey tempText, IKey pressureText, IKey centerText,
        int topOffset) {
        return parent.child(
            new TextWidget(tempText).left(5)
                .top(topOffset)
                .size(54, 10)
                .alignment(Alignment.Center))
            .child(
                new TextWidget(pressureText).right(5)
                    .top(topOffset)
                    .size(54, 10)
                    .alignment(Alignment.Center))
            .child(
                new TextWidget(centerText).center()
                    .top(topOffset)
                    .size(54, 10)
                    .alignment(Alignment.Center));
    }

    public ModularPanel createECCFPanel(PanelSyncManager syncManager) {
        ModularPanel ui = ModularPanel.defaultPanel("gt:eccf")
            .size(176, 207)
            .background(GTGuiTextures.BACKGROUND_STANDARD);

        DoubleSyncValue pressureSyncer = (DoubleSyncValue) syncManager.getSyncHandler("Pressure:0");
        DoubleSyncValue tempSyncer = (DoubleSyncValue) syncManager.getSyncHandler("Temperature:0");
        IntSyncValue heatCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Heat:0");
        IntSyncValue coolCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Cool:0");
        IntSyncValue vacuumCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Vacuum:0");
        IntSyncValue compressCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Compress:0");
        IntSyncValue leftParallelSyncer = (IntSyncValue) syncManager.getSyncHandler("Parallel_Left:0");
        IntSyncValue rightParallelSyncer = (IntSyncValue) syncManager.getSyncHandler("Parallel_Right:0");
        DoubleSyncValue initTemp = (DoubleSyncValue) syncManager.getSyncHandler("InitTemp:0");
        DoubleSyncValue initPres = (DoubleSyncValue) syncManager.getSyncHandler("InitPres:0");

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);

        ParentWidget<?> infoPage = new ParentWidget<>().child(
            GTGuiTextures.PROGRESSBAR_ECCF_TEMPERATURE.asWidget()
                .pos(5, 5)
                .size(54, 48))
            .child(
                GTGuiTextures.PROGRESSBAR_ECCF_PRESSURE.asWidget()
                    .right(5)
                    .top(5)
                    .size(54, 48));

        addTextWidgets(
            infoPage,
            IKey.dynamic(() -> temperatureConverter(tempSyncer.getValue())),
            IKey.dynamic(() -> pressureConverter(pressureSyncer.getValue())),
            IKey.str("Values"),
            57);

        addTextWidgets(
            infoPage,
            IKey.dynamic(() -> temperatureConverter(initTemp.getValue())),
            IKey.dynamic(() -> pressureConverter(initPres.getValue())),
            IKey.str("Initial"),
            57 + 10);

        infoPage.child(new DynamicDrawable(() -> {
            if (coolCoilSyncer.getValue() >= 0) {
                return GTGuiTextures.COOL_MODULE_ECCF_INDICATOR;
            }
            if (heatCoilSyncer.getValue() >= 0) {
                return GTGuiTextures.HEAT_MODULE_ECCF_INDICATOR;
            }
            if (leftParallelSyncer.getValue() >= 0) {
                return GTGuiTextures.PARALLEL_ECCF_INDICATOR_L;
            }
            return GTGuiTextures.EMPTY_ECCF_INDICATOR_L;
        }).asWidget()
            .pos(78 - 12 - 2, 5)
            .size(24, 48))
            .child(new DynamicDrawable(() -> {
                if (compressCoilSyncer.getValue() >= 0) {
                    return GTGuiTextures.PRESSURE_MODULE_ECCF_INDICATOR;
                }
                if (vacuumCoilSyncer.getValue() >= 0) {
                    return GTGuiTextures.VACUUM_MODULE_ECCF_INDICATOR;
                }
                if (rightParallelSyncer.getValue() >= 0) {
                    return GTGuiTextures.PARALLEL_ECCF_INDICATOR_R;
                }
                return GTGuiTextures.EMPTY_ECCF_INDICATOR_R;
            }).asWidget()
                .pos(78 + 12 - 2, 5)
                .size(24, 48));

        infoPage.sizeRel(1.0f);
        return ui.child(
            pagedWidget.addPage(infoPage)
                .sizeRel(1.0f))
            .posRel(0.3f, 0.5f);
    }
}
