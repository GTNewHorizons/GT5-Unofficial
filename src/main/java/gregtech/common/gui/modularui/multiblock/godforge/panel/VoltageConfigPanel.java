package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.LongValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;

public class VoltageConfigPanel {

    private static final int SIZE_W = 138;
    private static final int SIZE_H = 98;

    public static ModularPanel openModulePanel(SyncHypervisor hypervisor, Modules<?> module) {
        ModularPanel panel = hypervisor.getModularPanel(module, Panels.VOLTAGE_CONFIG);

        registerSyncValues(module, hypervisor);

        panel.size(SIZE_W, SIZE_H)
            .relative(hypervisor.getModularPanel(module, module.getMainPanel()))
            .topRel(0)
            .leftRelOffset(1, -3);

        Flow column = new Column().sizeRel(1);

        // Title
        column.child(
            IKey.lang("GT5U.gui.text.power_panel")
                .style(EnumChatFormatting.UNDERLINE, EnumChatFormatting.BLACK)
                .alignment(Alignment.CENTER)
                .asWidget()
                .widthRel(1)
                .marginTop(7)
                .marginBottom(5));

        column.child(createMaxParallelGroup(hypervisor, module).marginBottom(5));
        column.child(createVoltageGroup(hypervisor, module));
        panel.child(column);

        return panel;
    }

    private static void registerSyncValues(Modules<?> module, SyncHypervisor hypervisor) {
        SyncValues.MODULE_CALCULATED_MAX_PARALLEL.registerFor(module, Panels.VOLTAGE_CONFIG, hypervisor);
        SyncValues.MODULE_SET_MAX_PARALLEL.registerFor(module, Panels.VOLTAGE_CONFIG, hypervisor);
        SyncValues.MODULE_ALWAYS_MAX_PARALLEL.registerFor(module, Panels.VOLTAGE_CONFIG, hypervisor);
        SyncValues.MODULE_PROCESSING_VOLTAGE.registerFor(module, Panels.VOLTAGE_CONFIG, hypervisor);
        SyncValues.MODULE_VOLTAGE_CONFIG.registerFor(module, Panels.VOLTAGE_CONFIG, hypervisor);
    }

    private static Flow createMaxParallelGroup(SyncHypervisor hypervisor, Modules<?> module) {
        IntSyncValue maxParallelSyncer = SyncValues.MODULE_CALCULATED_MAX_PARALLEL
            .lookupFrom(module, Panels.VOLTAGE_CONFIG, hypervisor);
        IntSyncValue setMaxParallelSyncer = SyncValues.MODULE_SET_MAX_PARALLEL
            .lookupFrom(module, Panels.VOLTAGE_CONFIG, hypervisor);
        BooleanSyncValue alwaysMaxParallelSyncer = SyncValues.MODULE_ALWAYS_MAX_PARALLEL
            .lookupFrom(module, Panels.VOLTAGE_CONFIG, hypervisor);

        Flow column = new Column().size(130, 32)
            .alignX(0.5f);

        // Header
        column.child(
            IKey.lang("GTPP.CC.parallel")
                .style(EnumChatFormatting.BLACK)
                .alignment(Alignment.CENTER)
                .asWidget()
                .widthRel(1)
                .height(14)
                .alignX(0.5f));

        Flow row = new Row().size(90, 18)
            .alignX(0.5f);

        // Max parallel text field
        row.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(
                    () -> alwaysMaxParallelSyncer.getBoolValue() ? maxParallelSyncer.getIntValue() : 1,
                    maxParallelSyncer::getIntValue)
                .value(new IntValue.Dynamic(setMaxParallelSyncer::getIntValue, setMaxParallelSyncer::setIntValue))
                .setScrollValues(1, 4, 64)
                .setTextAlignment(Alignment.CENTER)
                .tooltipDynamic(t -> {
                    int maxParallel = maxParallelSyncer.getIntValue();
                    if (alwaysMaxParallelSyncer.getBoolValue()) {
                        t.addLine(translateToLocalFormatted("GT5U.gui.text.lockedvalue", maxParallel));
                    } else {
                        t.addLine(translateToLocalFormatted("GT5U.gui.text.rangedvalue", 1, maxParallel));
                    }
                })
                .tooltipAutoUpdate(true)
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .size(70, 18)
                .marginRight(4));

        // Always max parallel button
        row.child(
            new ButtonWidget<>().size(16)
                .margin(1)
                .overlay(new DynamicDrawable(() -> {
                    if (alwaysMaxParallelSyncer.getBoolValue()) {
                        return GTGuiTextures.OVERLAY_BUTTON_CHECKMARK;
                    }
                    return GTGuiTextures.OVERLAY_BUTTON_CROSS;
                }))
                .onMousePressed(d -> {
                    alwaysMaxParallelSyncer.setValue(!alwaysMaxParallelSyncer.getBoolValue());
                    setMaxParallelSyncer.setValue(maxParallelSyncer.getIntValue());
                    return true;
                })
                .tooltip(t -> t.addLine(translateToLocal("GT5U.gui.button.max_parallel")))
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound()));

        column.child(row);
        return column;
    }

    private static ParentWidget<?> createVoltageGroup(SyncHypervisor hypervisor, Modules<?> module) {
        LongSyncValue processingVoltageSyncer = SyncValues.MODULE_PROCESSING_VOLTAGE
            .lookupFrom(module, Panels.VOLTAGE_CONFIG, hypervisor);
        BooleanSyncValue voltageConfigSyncer = SyncValues.MODULE_VOLTAGE_CONFIG
            .lookupFrom(module, Panels.VOLTAGE_CONFIG, hypervisor);

        ParentWidget<?> parent = new ParentWidget<>().size(130, 32)
            .alignX(0.5f);

        // Header
        parent.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.voltageinfo")
                .style(EnumChatFormatting.BLACK)
                .alignment(Alignment.CENTER)
                .asWidget()
                .widthRel(1)
                .height(14)
                .align(Alignment.TopCenter));

        // Info widget
        parent.child(
            GTGuiTextures.PICTURE_INFO.asWidget()
                .size(8)
                .alignX(1)
                .alignY(0.2f)
                .tooltip(t -> {
                    t.addLine(translateToLocal("fog.text.tooltip.voltageadjustment"));
                    t.addLine(translateToLocal("fog.text.tooltip.voltageadjustment.1"));
                })
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .setEnabledIf($ -> voltageConfigSyncer.getBoolValue()));

        // Voltage per parallel text field
        parent.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbersLong(() -> 2_000_000_000L, () -> Long.MAX_VALUE)
                .value(
                    new LongValue.Dynamic(processingVoltageSyncer::getLongValue, processingVoltageSyncer::setLongValue))
                .setScrollValues(1, 4, 64)
                .setTextAlignment(Alignment.CENTER)
                .size(130, 18)
                .setTooltipOverride(true)
                .align(Alignment.BottomCenter)
                .setEnabledIf($ -> voltageConfigSyncer.getBoolValue()));

        // X for when voltage config is not yet unlocked
        parent.child(
            GTGuiTextures.OVERLAY_BUTTON_CROSS.asWidget()
                .size(20)
                .align(Alignment.BottomCenter)
                .tooltip(t -> t.addLine(translateToLocal("fog.button.voltageconfig.tooltip.02")))
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .setEnabledIf($ -> !voltageConfigSyncer.getBoolValue()));

        return parent;
    }
}
