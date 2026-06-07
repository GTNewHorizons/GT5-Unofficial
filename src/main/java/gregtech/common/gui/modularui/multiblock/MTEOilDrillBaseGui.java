package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase.WorkState;
import gregtech.common.tileentities.machines.multi.MTEOilDrillBase;

public class MTEOilDrillBaseGui extends MTEDrillerBaseGui<MTEOilDrillBase> {

    public MTEOilDrillBaseGui(MTEOilDrillBase base) {
        super(base);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager
            .syncValue("oilFluidType", new StringSyncValue(multiblock::getFluidName, multiblock::setClientFluidType));
        syncManager.syncValue(
            "oilFlowPerTick",
            new IntSyncValue(multiblock::getFlowRatePerTick, multiblock::setClientFlowPerTick));
        syncManager
            .syncValue("oilFlowPerOp", new IntSyncValue(multiblock::getOilFlow, multiblock::setClientFlowPerOperation));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        StringSyncValue fluidTypeSync = syncManager.findSyncHandler("oilFluidType", StringSyncValue.class);
        IntSyncValue flowPerTickSync = syncManager.findSyncHandler("oilFlowPerTick", IntSyncValue.class);
        IntSyncValue flowPerOpSync = syncManager.findSyncHandler("oilFlowPerOp", IntSyncValue.class);
        EnumSyncValue<WorkState, ?> workStateSync = syncManager
            .findSyncHandler("drillerWorkState", EnumSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.GRAY + StatCollector
                        .translateToLocalFormatted("GT5U.gui.text.pump_fluid_type", fluidTypeSync.getValue()))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> baseMetaTileEntity.isActive() && workStateSync.getValue() == WorkState.AT_BOTTOM))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.GRAY
                        + StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.pump_rate.1",
                            EnumChatFormatting.AQUA + formatNumber(flowPerTickSync.getValue()))
                        + EnumChatFormatting.GRAY
                        + StatCollector.translateToLocal("GT5U.gui.text.pump_rate.2"))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> baseMetaTileEntity.isActive() && workStateSync.getValue() == WorkState.AT_BOTTOM))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.GRAY
                        + StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.pump_recovery.1",
                            EnumChatFormatting.AQUA + formatNumber(flowPerOpSync.getValue()))
                        + EnumChatFormatting.GRAY
                        + StatCollector.translateToLocal("GT5U.gui.text.pump_recovery.2"))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> baseMetaTileEntity.isActive() && workStateSync.getValue() == WorkState.AT_BOTTOM));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createChunkRangeButton(syncManager))
            .child(createWorkAreaToggleButton(syncManager));
    }

    protected ButtonWidget<?> createChunkRangeButton(PanelSyncManager syncManager) {
        IntSyncValue chunkRangeSyncer = new IntSyncValue(
            multiblock::getChunkRangeConfig,
            multiblock::setChunkRangeConfig);
        syncManager.syncValue("oilChunkRange", chunkRangeSyncer);

        return new ButtonWidget<>().onMousePressed(mouseButton -> {
            if (!baseMetaTileEntity.isActive()) {
                multiblock.adjustChunkRange(mouseButton == 0);
            }
            return true;
        })
            .overlay(new DynamicDrawable(() -> getLockedOverlay(GTGuiTextures.OVERLAY_BUTTON_WORK_AREA)))
            .tooltipBuilder(t -> {
                t.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.button.oil_drill_radius_1",
                            formatNumber((long) chunkRangeSyncer.getValue() << 4))));
                t.addLine(IKey.lang("GT5U.gui.button.oil_drill_radius_2"));
                if (baseMetaTileEntity.isActive()) {
                    t.addLine(IKey.lang("GT5U.gui.button.forbidden_while_running"));
                }
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected ToggleButton createWorkAreaToggleButton(PanelSyncManager syncManager) {
        BooleanSyncValue showWorkAreaSyncer = new BooleanSyncValue(
            multiblock::isShowWorkArea,
            multiblock::setShowWorkArea).allowC2S();
        syncManager.syncValue("oilShowWorkArea", showWorkAreaSyncer);

        return new ToggleButton().value(showWorkAreaSyncer)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_SHOW_WORK_AREA)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_SHOW_WORK_AREA)
            .background(true, GTGuiTextures.BUTTON_STANDARD_PRESSED)
            .background(false, GTGuiTextures.BUTTON_STANDARD)
            .tooltip(true, t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.work_area_preview_on")))
            .tooltip(false, t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.work_area_preview_off")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
