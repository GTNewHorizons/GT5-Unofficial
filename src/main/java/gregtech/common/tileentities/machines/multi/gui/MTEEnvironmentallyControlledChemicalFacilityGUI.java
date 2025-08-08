package gregtech.common.tileentities.machines.multi.gui;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTEEnvironmentallyControlledChemicalFacility;

public class MTEEnvironmentallyControlledChemicalFacilityGUI extends MTEMultiBlockBaseGui {

    private final MTEEnvironmentallyControlledChemicalFacility base;

    public MTEEnvironmentallyControlledChemicalFacilityGUI(MTEEnvironmentallyControlledChemicalFacility base) {
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
        syncManager.syncValue("Compress", new IntSyncValue(() -> base.compressorTier, dub -> base.compressorTier = dub));
        syncManager.syncValue("Vacuum", new IntSyncValue(() -> base.vacuumTier, dub -> base.vacuumTier = dub));
        syncManager.syncValue("Heat", new IntSyncValue(() -> base.heaterTier, dub -> base.heaterTier = dub));
        syncManager.syncValue("Cool", new IntSyncValue(() -> base.freezerTier, dub -> base.freezerTier = dub));
        syncManager.syncValue("InitTemp", new DoubleSyncValue(() -> base.ambientTemp, dub -> base.ambientTemp = dub));
        syncManager.syncValue("InitPres", new DoubleSyncValue(() -> base.ambientPressure, dub -> base.ambientPressure = dub));
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
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createVoidExcessButton(syncManager))
            .child(createInputSeparationButton(syncManager))
            .childIf(!machineModeIcons.isEmpty(), createModeSwitchButton(syncManager))
            .child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager))
            .child(createConditionControlButton(parent, syncManager))
            .childIf(base.supportsPowerPanel(), createPowerPanelButton(syncManager, parent));
    }

    private IWidget createConditionControlButton(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createECCFPanel(parent, syncManager), true);
        return new ButtonWidget<>().onMousePressed(mouseButton -> {
            if (!popupPanel.isPanelOpen()) {
                popupPanel.openPanel();
            } else {
                popupPanel.closePanel();
            }
            return true;
        })
            .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_THERMOMETER)
            .disableHoverBackground()
            .tooltip(tooltip -> tooltip.add("Condition Control"))
            .marginTop(4)
            .rightRel(0, 6 + 18 + 4, 0)
            .size(18, 18);
    }

    private String valueConverter(double value, boolean changeFormat, String unit) {
        EnumChatFormatting format = EnumChatFormatting.RESET;
        String plus = (changeFormat && (value > 0.1)) ? "+" : "";
        if (changeFormat) {
            if (value > 0) format = EnumChatFormatting.YELLOW;
            if (value < 0) format = EnumChatFormatting.RED;
        }
        if (Math.abs(value) > 1e10) return format + plus + String.format("%.1f G%s", value / 1e9, unit);
        if (Math.abs(value) > 1e7) return format + plus + String.format("%.1f M%s", value / 1e6, unit);
        if (Math.abs(value) > 1e4) return format + plus + String.format("%.1f k%s", value / 1e3, unit);
        return format + plus + String.format("%.1f %s", value, unit);
    }

    private Flow createValueRow(IKey tempValue, IKey presValue, String label) {
        return new Row().marginTop(10)
            .child(
                new TextWidget(tempValue).marginRight(5)
                    .size(54, 10)
                    .alignment(Alignment.Center))
            .child(
                new TextWidget(presValue).marginLeft(5)
                    .size(54, 10)
                    .alignment(Alignment.Center))
            .child(
                new TextWidget(IKey.str(label)).size(54, 10)
                    .alignment(Alignment.Center))
            .height(10);
    }

    private double updateTotalTemp(PanelSyncManager syncManager) {
        DoubleSyncValue tempModule = (DoubleSyncValue) syncManager.getSyncHandler("TempModule:0");
        DoubleSyncValue lossTemp = (DoubleSyncValue) syncManager.getSyncHandler("LossTemp:0");
        IntSyncValue deltaTemp = (IntSyncValue) syncManager.getSyncHandler("DeltaTemp:0");
        return tempModule.getValue() + lossTemp.getValue() + deltaTemp.getValue();
    }

    private double updateTotalPres(PanelSyncManager syncManager) {
        DoubleSyncValue presModule = (DoubleSyncValue) syncManager.getSyncHandler("PresModule:0");
        DoubleSyncValue lossPres = (DoubleSyncValue) syncManager.getSyncHandler("LossPres:0");
        IntSyncValue deltaPres = (IntSyncValue) syncManager.getSyncHandler("DeltaPres:0");
        DoubleSyncValue leakPres = (DoubleSyncValue) syncManager.getSyncHandler("LeakPres:0");
        return presModule.getValue() + lossPres.getValue() + deltaPres.getValue() + leakPres.getValue();
    }

    private Flow createInfoColumn(PanelSyncManager syncManager) {
        DoubleSyncValue tempSyncer = (DoubleSyncValue) syncManager.getSyncHandler("Temperature:0");
        DoubleSyncValue pressureSyncer = (DoubleSyncValue) syncManager.getSyncHandler("Pressure:0");
        DoubleSyncValue initTemp = (DoubleSyncValue) syncManager.getSyncHandler("InitTemp:0");
        DoubleSyncValue initPres = (DoubleSyncValue) syncManager.getSyncHandler("InitPres:0");
        DoubleSyncValue lossTemp = (DoubleSyncValue) syncManager.getSyncHandler("LossTemp:0");
        DoubleSyncValue lossPres = (DoubleSyncValue) syncManager.getSyncHandler("LossPres:0");
        IntSyncValue deltaTemp = (IntSyncValue) syncManager.getSyncHandler("DeltaTemp:0");
        IntSyncValue deltaPres = (IntSyncValue) syncManager.getSyncHandler("DeltaPres:0");
        DoubleSyncValue leakPres = (DoubleSyncValue) syncManager.getSyncHandler("LeakPres:0");
        DoubleSyncValue tempModule = (DoubleSyncValue) syncManager.getSyncHandler("TempModule:0");
        DoubleSyncValue presModule = (DoubleSyncValue) syncManager.getSyncHandler("PresModule:0");

        return new Column().marginTop(43)
            .child(
                createValueRow(
                    IKey.dynamic(() -> valueConverter(tempSyncer.getValue(), false, "K")),
                    IKey.dynamic(() -> valueConverter(pressureSyncer.getValue(), false, "Pa")),
                    "Values"))
            .child(
                createValueRow(
                    IKey.dynamic(() -> valueConverter(initTemp.getValue(), false, "K")),
                    IKey.dynamic(() -> valueConverter(initPres.getValue(), false, "Pa")),
                    "Ambient"))
            .child(
                createValueRow(
                    IKey.dynamic(() -> valueConverter(deltaTemp.getValue(), true, "K")),
                    IKey.dynamic(() -> valueConverter(deltaPres.getValue(), true, "Pa")),
                    "Recipe"))
            .child(
                createValueRow(
                    IKey.dynamic(() -> valueConverter(lossTemp.getValue(), true, "K")),
                    IKey.dynamic(() -> valueConverter(lossPres.getValue(), true, "Pa")),
                    "Loss"))
            .child(
                createValueRow(
                    IKey.str("-"),
                    IKey.dynamic(() -> valueConverter(leakPres.getValue(), true, "Pa")),
                    "Leak"))
            .child(
                createValueRow(
                    IKey.dynamic(() -> valueConverter(tempModule.getValue(), true, "K")),
                    IKey.dynamic(() -> valueConverter(presModule.getValue(), true, "Pa")),
                    "Modules"))
            .child(
                createValueRow(
                    IKey.dynamic(() -> valueConverter(tempModule.getValue(), true, "K")),
                    IKey.dynamic(() -> valueConverter(presModule.getValue(), true, "Pa")),
                    "Modules"))
            .child(
                createValueRow(
                    IKey.dynamic(() -> valueConverter(updateTotalTemp(syncManager), true, "K")),
                    IKey.dynamic(() -> valueConverter(updateTotalPres(syncManager), true, "Pa")),
                    "Total"))
            .coverChildrenHeight();
    }

    public ModularPanel createECCFPanel(ModularPanel parent, PanelSyncManager syncManager) {
        int eccfPanelWidth = 176;
        int eccfPanelHeight = 136;
        ModularPanel ui = ModularPanel.defaultPanel("gt:eccf")
            .size(eccfPanelWidth, eccfPanelHeight)
            .background(GTGuiTextures.BACKGROUND_STANDARD);

        IntSyncValue heatCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Heat:0");
        IntSyncValue coolCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Cool:0");
        IntSyncValue vacuumCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Vacuum:0");
        IntSyncValue compressCoilSyncer = (IntSyncValue) syncManager.getSyncHandler("Compress:0");
        IntSyncValue leftParallelSyncer = (IntSyncValue) syncManager.getSyncHandler("Parallel_Left:0");
        IntSyncValue rightParallelSyncer = (IntSyncValue) syncManager.getSyncHandler("Parallel_Right:0");

        ParentWidget<?> infoPage = new ParentWidget<>().top(5)
            .child(
                GTGuiTextures.PROGRESSBAR_ECCF_TEMPERATURE.asWidget()
                    .left(5)
                    .size(54, 48))
            .child(
                GTGuiTextures.PROGRESSBAR_ECCF_PRESSURE.asWidget()
                    .right(5)
                    .size(54, 48))
            .child(new DynamicDrawable(() -> {
                if (coolCoilSyncer.getValue() >= 0) {
                    return GTGuiTextures.COOLING_ECCF_INDICATOR;
                }
                if (heatCoilSyncer.getValue() >= 0) {
                    return GTGuiTextures.HEATING_ECCF_INDICATOR;
                }
                if (leftParallelSyncer.getValue() >= 0) {
                    return GTGuiTextures.PARALLEL_ECCF_INDICATOR_L;
                }
                return GTGuiTextures.EMPTY_ECCF_INDICATOR_L;
            }).asWidget()
                .size(48, 48)
                .horizontalCenter())
            .child(new DynamicDrawable(() -> {
                if (compressCoilSyncer.getValue() >= 0) {
                    return GTGuiTextures.PRESSURE_ECCF_INDICATOR;
                }
                if (vacuumCoilSyncer.getValue() >= 0) {
                    return GTGuiTextures.VACUUM_ECCF_INDICATOR;
                }
                if (rightParallelSyncer.getValue() >= 0) {
                    return GTGuiTextures.PARALLEL_ECCF_INDICATOR_R;
                }
                return GTGuiTextures.EMPTY_ECCF_INDICATOR_R;
            }).asWidget()
                .size(48, 48)
                .horizontalCenter())
            .child(createInfoColumn(syncManager));

        infoPage.sizeRel(1.0f);
        return ui.child(infoPage.sizeRel(1.0f))
            .relative(parent)
            .leftRel(0, -4, 1)
            .topRel(0, -4, 0);
    }
}
