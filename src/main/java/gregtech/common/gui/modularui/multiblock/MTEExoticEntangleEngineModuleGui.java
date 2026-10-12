package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.widget.SegmentedBarWidget;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEExoticEntangleEngineModule;

public class MTEExoticEntangleEngineModuleGui extends MTENanochipAssemblyModuleBaseGui<MTEExoticEntangleEngineModule> {

    private static final int PANEL_WIDTH = 104;

    public MTEExoticEntangleEngineModuleGui(MTEExoticEntangleEngineModule multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("stability", new DoubleSyncValue(() -> (double) multiblock.getStability()));
        syncManager.syncValue(
            "temperature",
            new DoubleSyncValue(() -> (double) multiblock.getTemperatureRatio(), multiblock::setTemperatureRatio)
                .allowC2S());
        syncManager.syncValue("enthalpy", new DoubleSyncValue(() -> (double) multiblock.getConditioningEnthalpy()));
        syncManager.syncValue("entropy", new DoubleSyncValue(() -> (double) multiblock.getConditioningEntropy()));
        syncManager.syncValue("temperatureFrozen", new BooleanSyncValue(multiblock::isTemperatureFrozen));
    }

    @Override
    protected ParentWidget<?> createTerminalParentWidget(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
            .paddingLeft(4)
            .paddingRight(0)
            .childPadding(3)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .child(
                createTerminalTextWidget(syncManager, panel)
                    .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                    .collapseDisabledChild())
            .childIf(
                multiblock.supportsTerminalRightCornerColumn(),
                () -> createTerminalRightCornerColumn(panel, syncManager))
            .childIf(
                multiblock.supportsTerminalLeftCornerColumn(),
                () -> createTerminalLeftCornerColumn(panel, syncManager));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(panel, syncManager).child(createStabilityButton(panel, syncManager));
    }

    private ButtonWidget<?> createStabilityButton(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler stabilityPanel = syncManager.syncedPanel(
            "stabilityPanel",
            true,
            (p_syncManager, syncHandler) -> openStabilityPanel(panel, syncManager));

        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_EDIT_PARAMETERS_ENABLED)
            .onMousePressed(d -> {
                if (!stabilityPanel.isPanelOpen()) {
                    stabilityPanel.openPanel();
                } else {
                    stabilityPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.text.nac.exotic_engine.panel")));
    }

    private ModularPanel openStabilityPanel(ModularPanel parent, PanelSyncManager syncManager) {
        DoubleSyncValue stability = syncManager.findSyncHandler("stability", DoubleSyncValue.class);
        DoubleSyncValue temperature = syncManager.findSyncHandler("temperature", DoubleSyncValue.class);
        DoubleSyncValue enthalpy = syncManager.findSyncHandler("enthalpy", DoubleSyncValue.class);
        DoubleSyncValue entropy = syncManager.findSyncHandler("entropy", DoubleSyncValue.class);
        BooleanSyncValue frozen = syncManager.findSyncHandler("temperatureFrozen", BooleanSyncValue.class);

        return new ModularPanel("stabilityPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .coverChildren()
            .padding(6)
            .widgetTheme("backgroundPopup")
            .child(
                Flow.column()
                    .coverChildren()
                    .childPadding(3)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child(new TextWidget<>(IKey.lang("GT5U.gui.text.nac.exotic_engine.panel")).width(PANEL_WIDTH))
                    .child(
                        new SegmentedBarWidget(
                            100,
                            1,
                            new SegmentedBarWidget.SegmentInfo(
                                () -> (int) stability.getDoubleValue(),
                                Color.PURPLE,
                                "GT5U.gui.text.nac.bar.label.stability")).width(PANEL_WIDTH)
                                    .height(10))
                    .child(
                        IKey.dynamic(
                            () -> translateToLocal("GT5U.gui.text.nac.exotic_engine.temperature") + ": "
                                + String.format("%.1f", temperature.getDoubleValue() * 100)
                                + "%")
                            .asWidget())
                    .child(
                        new SliderWidget().background(GTGuiTextures.SLOT_ITEM_STANDARD)
                            .size(PANEL_WIDTH, 8)
                            .sliderSize(8, 8)
                            .bounds(0, 1)
                            .stopper(0.01)
                            .value(temperature)
                            .setEnabledIf(w -> frozen.getBoolValue())
                            .tooltipBuilder(
                                t -> t.addLine(translateToLocal("GT5U.gui.text.nac.exotic_engine.temperature_slider")))
                            .tooltipAutoUpdate(true))
                    .child(
                        IKey.dynamic(
                            () -> translateToLocalFormatted(
                                "GT5U.gui.text.nac.exotic_engine.conditioning",
                                String.format("%+.1f", enthalpy.getDoubleValue()),
                                String.format("%.3f", entropy.getDoubleValue())))
                            .asWidget()));
    }
}
