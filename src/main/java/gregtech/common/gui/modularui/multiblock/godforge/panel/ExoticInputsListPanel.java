package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule.NUMBER_OF_INPUTS;
import static tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule.RECIPE_REFRESH_LIMIT;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncActions;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;

public class ExoticInputsListPanel {

    private static final int SIZE_W = 100;
    private static final int SIZE_H = 60;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Modules.EXOTIC, Panels.EXOTIC_INPUTS_LIST);

        registerSyncValues(hypervisor);

        panel.size(SIZE_W, SIZE_H)
            .relative(hypervisor.getModularPanel(Modules.EXOTIC, Panels.MAIN_EXOTIC))
            .leftRelOffset(0, -SIZE_W)
            .topRelOffset(0, 47);

        Flow column = new Column().coverChildren()
            .marginTop(6)
            .alignX(0.5f);

        // Title
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.expectedinputs")
                .style(EnumChatFormatting.BLACK)
                .alignment(Alignment.CENTER)
                .asWidget()
                .widthRel(1));

        // Create fluid slots

        // Panel rows
        column.child(createFirstRow().marginTop(3));
        column.child(createSecondRow(hypervisor));
        panel.child(column);

        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.EXOTIC_INPUTS_TICKER.registerFor(Modules.EXOTIC, Panels.EXOTIC_INPUTS_LIST, hypervisor);

        SyncActions.REFRESH_EXOTIC_RECIPE
            .registerFor(Modules.EXOTIC, Panels.EXOTIC_INPUTS_LIST, hypervisor, hypervisor.getModule(Modules.EXOTIC));

        for (int i = 0; i < NUMBER_OF_INPUTS; i++) {
            hypervisor.getSyncManager(Modules.EXOTIC, Panels.EXOTIC_INPUTS_LIST)
                .syncValue(
                    "exotic_fluid_tanks",
                    i,
                    SyncHandlers.fluidSlot(hypervisor.getModule(Modules.EXOTIC).tankHandler.getFluidTank(i))
                        .canDrainSlot(false)
                        .canFillSlot(false));
        }
    }

    private static Flow createFirstRow() {
        Flow row = new Row().size(72, 18)
            .alignX(0.5f);

        // Slots 0-3
        row.child(
            SlotGroupWidget.builder()
                .matrix("SSSS")
                .key('S', index -> new FluidSlot().syncHandler("exotic_fluid_tanks", index))
                .build());

        return row;
    }

    private static Flow createSecondRow(SyncHypervisor hypervisor) {
        IPanelHandler possibleInputsPanel = Panels.EXOTIC_POSSIBLE_INPUTS_LIST
            .getFrom(Panels.EXOTIC_INPUTS_LIST, hypervisor);
        LongSyncValue tickerSyncer = SyncValues.EXOTIC_INPUTS_TICKER
            .lookupFrom(Modules.EXOTIC, Panels.EXOTIC_INPUTS_LIST, hypervisor);

        Flow row = new Row().size(92, 18)
            .alignX(0.5f);

        // Refresh button
        row.child(
            new ButtonWidget<>().size(18)
                .marginRight(1)
                .background(GTGuiTextures.TT_OVERLAY_CYCLIC_BLUE)
                .onMousePressed(d -> {
                    if (tickerSyncer.getLongValue() > RECIPE_REFRESH_LIMIT) {
                        SyncActions.REFRESH_EXOTIC_RECIPE
                            .callFrom(Modules.EXOTIC, Panels.EXOTIC_INPUTS_LIST, hypervisor, null);
                    }
                    return true;
                })
                .tooltipDynamic(t -> {
                    if (tickerSyncer.getLongValue() > RECIPE_REFRESH_LIMIT) {
                        t.addLine(translateToLocal("fog.button.reciperefresh.tooltip"));
                        return;
                    }
                    t.addLine(
                        translateToLocal("fog.button.refreshtimer.tooltip") + " "
                            + (int) Math.ceil((RECIPE_REFRESH_LIMIT - tickerSyncer.getLongValue()) / 20d)
                            + " "
                            + translateToLocal("fog.button.seconds"));
                })
                .tooltipAutoUpdate(true)
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound()));

        // Slots 4-6
        row.child(
            SlotGroupWidget.builder()
                .matrix("SSS")
                .key('S', index -> new FluidSlot().syncHandler("exotic_fluid_tanks", index + 4))
                .build());

        // All possible inputs panel button
        row.child(
            new ButtonWidget<>().size(18)
                .marginLeft(1)
                .background(
                    GTGuiTextures.PICTURE_INFO.asIcon()
                        .size(16)
                        .margin(1))
                .onMousePressed(d -> {
                    if (!possibleInputsPanel.isPanelOpen()) {
                        possibleInputsPanel.openPanel();
                    } else {
                        possibleInputsPanel.closePanel();
                    }
                    return true;
                })
                .tooltip(t -> t.addLine(translateToLocal("fog.button.possibleexoticinputs.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound()));

        return row;
    }
}
