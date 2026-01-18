package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;

public class PlasmaDebugPanel {

    private static final int SIZE_W = 78;
    private static final int SIZE_H = 60;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Modules.PLASMA, Panels.PLASMA_DEBUG);

        registerSyncValues(hypervisor);

        panel.size(SIZE_W, SIZE_H)
            .padding(4)
            .relative(hypervisor.getModularPanel(Modules.PLASMA, Panels.MAIN_PLASMA))
            .topRel(0)
            .leftRelOffset(1, -3);

        Flow column = new Column().coverChildren()
            .childPadding(2);

        // Debug max parallel
        IntSyncValue plasmaParallelSyncer = SyncValues.DEBUG_PLASMA_PARALLEL
            .lookupFrom(Modules.PLASMA, Panels.PLASMA_DEBUG, hypervisor);
        column.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(0, Integer.MAX_VALUE)
                .value(new IntValue.Dynamic(plasmaParallelSyncer::getIntValue, plasmaParallelSyncer::setIntValue))
                .setTextAlignment(Alignment.CENTER)
                .size(70, 16)
                .tooltip(t -> t.addLine(translateToLocal("tt.gui.tooltip.plasma_module.debug_window.parallel")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Debug plasma tier
        IntSyncValue fusionTierSyncer = SyncValues.DEBUG_FUSION_TIER
            .lookupFrom(Modules.PLASMA, Panels.PLASMA_DEBUG, hypervisor);
        column.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(0, 2)
                .value(new IntValue.Dynamic(fusionTierSyncer::getIntValue, fusionTierSyncer::setIntValue))
                .setTextAlignment(Alignment.CENTER)
                .size(16)
                .tooltip(t -> t.addLine(translateToLocal("tt.gui.tooltip.plasma_module.debug_window.fusion_tier")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Debug multi-step
        BooleanSyncValue multiStepSyncer = SyncValues.DEBUG_MULTI_STEP
            .lookupFrom(Modules.PLASMA, Panels.PLASMA_DEBUG, hypervisor);
        column.child(
            new ButtonWidget<>().size(16)
                .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
                .overlay(new DynamicDrawable(() -> {
                    if (multiStepSyncer.getBoolValue()) {
                        return GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON;
                    }
                    return GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED;
                }))
                .onMousePressed(d -> {
                    multiStepSyncer.setBoolValue(!multiStepSyncer.getBoolValue());
                    return true;
                })
                .tooltip(t -> t.addLine(translateToLocal("tt.gui.tooltip.plasma_module.debug_window.multi_step")))
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound()));

        panel.child(column);
        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.DEBUG_MULTI_STEP.registerFor(Modules.PLASMA, Panels.PLASMA_DEBUG, hypervisor);
        SyncValues.DEBUG_PLASMA_PARALLEL.registerFor(Modules.PLASMA, Panels.PLASMA_DEBUG, hypervisor);
        SyncValues.DEBUG_FUSION_TIER.registerFor(Modules.PLASMA, Panels.PLASMA_DEBUG, hypervisor);
    }
}
