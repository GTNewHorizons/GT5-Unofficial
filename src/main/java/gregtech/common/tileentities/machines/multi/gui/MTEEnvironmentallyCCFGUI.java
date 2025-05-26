package gregtech.common.tileentities.machines.multi.gui;

import net.minecraft.util.EnumChatFormatting;

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
        // spotless:off
        syncManager.syncValue("Parallel_Left", new IntSyncValue(() -> base.parallelModuleTierL, dub -> base.parallelModuleTierL = dub));
        syncManager.syncValue("Parallel_Right", new IntSyncValue(() -> base.parallelModuleTierR, dub -> base.parallelModuleTierR = dub));
        syncManager.syncValue("Pressure", new DoubleSyncValue(() -> base.currentPressure, dub -> base.currentPressure = dub));
        syncManager.syncValue("Temperature", new DoubleSyncValue(() -> base.currentTemp, dub -> base.currentTemp = dub));
        syncManager.syncValue("Compress", new IntSyncValue(() -> base.compressCoilTier, dub -> base.compressCoilTier = dub));
        syncManager.syncValue("Vacuum", new IntSyncValue(() -> base.vacuumCoilTier, dub -> base.vacuumCoilTier = dub));
        syncManager.syncValue("Heat", new IntSyncValue(() -> base.heatCoilTier, dub -> base.heatCoilTier = dub));
        syncManager.syncValue("Cool", new IntSyncValue(() -> base.coolCoilTier, dub -> base.coolCoilTier = dub));
        syncManager.syncValue("InitTemp", new DoubleSyncValue(() -> base.initialTemp, dub -> base.initialTemp = dub));
        syncManager.syncValue("InitPres", new DoubleSyncValue(() -> base.initialPressure, dub -> base.initialPressure = dub));
        syncManager.syncValue("LossTemp", new DoubleSyncValue(() -> base.temperatureLossValue, dub -> base.temperatureLossValue = dub));
        syncManager.syncValue("LossPres", new DoubleSyncValue(() -> base.pressureLossValue, dub -> base.pressureLossValue = dub));
        syncManager.syncValue("DeltaTemp", new IntSyncValue(() -> base.deltaTemp, dub -> base.deltaTemp = dub));
        syncManager.syncValue("DeltaPres", new IntSyncValue(() -> base.deltaPressure, dub -> base.deltaPressure = dub));
        syncManager.syncValue("LeakPres", new DoubleSyncValue(() -> base.pressureLeakValue, dub -> base.pressureLeakValue = dub));
        syncManager.syncValue("TempModule", new DoubleSyncValue(() -> base.tempChange, dub -> base.tempChange = dub));
        syncManager.syncValue("PresModule", new DoubleSyncValue(() -> base.presChange, dub -> base.presChange = dub));
        // spotless:on
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

    private String pressureConverter(double pressure, boolean changeFormat) {
        EnumChatFormatting format = EnumChatFormatting.RESET;
        String plus = (changeFormat && pressure > 0) ? "+" : "";
        if (changeFormat) {
            if (pressure > 0) format = EnumChatFormatting.YELLOW;
            if (pressure < 0) format = EnumChatFormatting.RED;
        }
        if (Math.abs(pressure) > 1e10) return format + plus + String.format("%.1f GPa", pressure / 1e9);
        if (Math.abs(pressure) > 1e7) return format + plus + String.format("%.1f MPa", pressure / 1e6);
        if (Math.abs(pressure) > 1e4) return format + plus + String.format("%.1f kPa", pressure / 1e3);
        return format + plus + String.format("%.1f Pa", pressure);
    }

    private String temperatureConverter(double temp, boolean changeFormat) {
        EnumChatFormatting format = EnumChatFormatting.RESET;
        String plus = (changeFormat && temp > 0) ? "+" : "";
        if (changeFormat) {
            if (temp > 0) format = EnumChatFormatting.YELLOW;
            if (temp < 0) format = EnumChatFormatting.RED;
        }
        if (Math.abs(temp) > 1e7) return format + plus + String.format("%.1f MK", temp / 1e6);
        if (Math.abs(temp) > 1e4) return format + plus + String.format("%.1f kK", temp / 1e3);
        return format + plus + String.format("%.1f K", temp);
    }

    private void addTextWidgets(ParentWidget<?> parent, IKey tempText, IKey pressureText, IKey centerText,
        int topOffset) {
        parent.child(
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
            .size(176, 136)
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
        DoubleSyncValue lossTemp = (DoubleSyncValue) syncManager.getSyncHandler("LossTemp:0");
        DoubleSyncValue lossPres = (DoubleSyncValue) syncManager.getSyncHandler("LossPres:0");
        IntSyncValue deltaTemp = (IntSyncValue) syncManager.getSyncHandler("DeltaTemp:0");
        IntSyncValue deltaPres = (IntSyncValue) syncManager.getSyncHandler("DeltaPres:0");
        DoubleSyncValue leakPres = (DoubleSyncValue) syncManager.getSyncHandler("LeakPres:0");
        DoubleSyncValue tempModule = (DoubleSyncValue) syncManager.getSyncHandler("TempModule:0");
        DoubleSyncValue presModule = (DoubleSyncValue) syncManager.getSyncHandler("PresModule:0");

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
            IKey.dynamic(() -> temperatureConverter(tempSyncer.getValue(), false)),
            IKey.dynamic(() -> pressureConverter(pressureSyncer.getValue(), false)),
            IKey.str("Values"),
            57);

        addTextWidgets(
            infoPage,
            IKey.dynamic(() -> temperatureConverter(initTemp.getValue(), false)),
            IKey.dynamic(() -> pressureConverter(initPres.getValue(), false)),
            IKey.str("Initial"),
            57 + 10);

        addTextWidgets(
            infoPage,
            IKey.dynamic(() -> temperatureConverter(deltaTemp.getValue(), true)),
            IKey.dynamic(() -> pressureConverter(deltaPres.getValue(), true)),
            IKey.str("Recipe"),
            57 + 20);

        addTextWidgets(
            infoPage,
            IKey.dynamic(() -> temperatureConverter(lossTemp.getValue(), true)),
            IKey.dynamic(() -> pressureConverter(lossPres.getValue(), true)),
            IKey.str("Loss"),
            57 + 30);

        addTextWidgets(
            infoPage,
            IKey.str("-"),
            IKey.dynamic(() -> pressureConverter(leakPres.getValue(), true)),
            IKey.str("Leak"),
            57 + 40);

        addTextWidgets(
            infoPage,
            IKey.dynamic(() -> temperatureConverter(tempModule.getValue(), true)),
            IKey.dynamic(() -> pressureConverter(presModule.getValue(), true)),
            IKey.str("Modules"),
            57 + 50);

        addTextWidgets(
            infoPage,
            IKey.dynamic(
                () -> temperatureConverter(tempModule.getValue() + lossTemp.getValue() + deltaTemp.getValue(), true)),
            IKey.dynamic(
                () -> pressureConverter(
                    presModule.getValue() + lossPres.getValue() + deltaPres.getValue() + leakPres.getValue(),
                    true)),
            IKey.str("Total"),
            57 + 60);

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
