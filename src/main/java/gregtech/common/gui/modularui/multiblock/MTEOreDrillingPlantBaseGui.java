package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_DISABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_ENABLED_TOOLTIP;
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
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase.WorkState;
import gregtech.common.tileentities.machines.multi.MTEOreDrillingPlantBase;

public class MTEOreDrillingPlantBaseGui extends MTEDrillerBaseGui<MTEOreDrillingPlantBase> {

    public MTEOreDrillingPlantBaseGui(MTEOreDrillingPlantBase base) {
        super(base);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager
            .syncValue("oreListSize", new IntSyncValue(multiblock::getOreBlockCount, multiblock::setClientOreListSize));
        syncManager.syncValue(
            "oreTotalChunks",
            new IntSyncValue(multiblock::getTotalChunkCount, multiblock::setClientTotalChunks));
        syncManager.syncValue(
            "oreCurrentChunk",
            new IntSyncValue(multiblock::getChunkNumber, multiblock::setClientCurrentChunk));
        syncManager.syncValue("oreYHead", new IntSyncValue(multiblock::getYHead, multiblock::setClientYHead));
        syncManager.syncValue("oreVeinName", new StringSyncValue(multiblock::getVeinName, multiblock::setVeinName));
        syncManager.syncValue("oreReplaceCobble", new BooleanSyncValue(multiblock::isReplaceWithCobblestone, val -> {
            if (!baseMetaTileEntity.isActive()) {
                multiblock.setReplaceWithCobblestone(val);
            }
        }).allowC2S());
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue oreListSizeSync = syncManager.findSyncHandler("oreListSize", IntSyncValue.class);
        IntSyncValue totalChunksSync = syncManager.findSyncHandler("oreTotalChunks", IntSyncValue.class);
        IntSyncValue currentChunkSync = syncManager.findSyncHandler("oreCurrentChunk", IntSyncValue.class);
        EnumSyncValue<WorkState, ?> workStateSync = syncManager
            .findSyncHandler("drillerWorkState", EnumSyncValue.class);
        IntSyncValue yHeadSync = syncManager.findSyncHandler("oreYHead", IntSyncValue.class);
        StringSyncValue veinNameSync = syncManager.findSyncHandler("oreVeinName", StringSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.drill_ores_left_chunk",
                        formatNumber(oreListSizeSync.getValue())))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> baseMetaTileEntity.isActive() && oreListSizeSync.getValue() > 0
                            && workStateSync.getValue() == WorkState.AT_BOTTOM))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.drill_ores_left_layer",
                        formatNumber(yHeadSync.getValue()),
                        formatNumber(oreListSizeSync.getValue())))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> baseMetaTileEntity.isActive() && yHeadSync.getValue() > 0
                            && workStateSync.getValue() == WorkState.DOWNWARD))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.drill_chunks_left",
                        formatNumber(currentChunkSync.getValue()),
                        formatNumber(totalChunksSync.getValue())))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> baseMetaTileEntity.isActive() && currentChunkSync.getValue() > 0
                            && workStateSync.getValue() == WorkState.AT_BOTTOM))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.GRAY + StatCollector
                        .translateToLocalFormatted("GT5U.gui.text.drill_current_vein", veinNameSync.getValue()))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> veinNameSync.getValue() != null && (workStateSync.getValue() == WorkState.AT_BOTTOM
                            || workStateSync.getValue() == WorkState.DOWNWARD)));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createReplaceWithCobblestoneToggle(syncManager))
            .child(createChunkRadiusButton(syncManager))
            .child(createWorkAreaToggleButton(syncManager));
    }

    protected ToggleButton createReplaceWithCobblestoneToggle(PanelSyncManager syncManager) {
        BooleanSyncValue cobbleSyncer = syncManager.findSyncHandler("oreReplaceCobble", BooleanSyncValue.class);

        return new ToggleButton().value(cobbleSyncer)
            .overlay(true, new DynamicDrawable(() -> getLockedOverlay(GTGuiTextures.OVERLAY_BUTTON_REPLACE_COBBLE_ON)))
            .overlay(
                false,
                new DynamicDrawable(() -> getLockedOverlay(GTGuiTextures.OVERLAY_BUTTON_REPLACE_COBBLE_OFF)))
            .tooltipBuilder(true, t -> {
                t.addLine(StatCollector.translateToLocal("GT5U.gui.button.ore_drill_cobblestone"));
                t.addLine(
                    GTUtility
                        .getColoredSecondaryTooltip(StatCollector.translateToLocal(BUTTON_FEATURE_ENABLED_TOOLTIP)));
                if (baseMetaTileEntity.isActive()) {
                    t.addLine(StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
                }
            })
            .tooltipBuilder(false, t -> {
                t.addLine(StatCollector.translateToLocal("GT5U.gui.button.ore_drill_cobblestone"));
                t.addLine(
                    GTUtility
                        .getColoredSecondaryTooltip(StatCollector.translateToLocal(BUTTON_FEATURE_DISABLED_TOOLTIP)));
                if (baseMetaTileEntity.isActive()) {
                    t.addLine(StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
                }
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected ButtonWidget<?> createChunkRadiusButton(PanelSyncManager syncManager) {
        IntSyncValue chunkRadiusSyncer = new IntSyncValue(
            multiblock::getChunkRadiusConfig,
            multiblock::setChunkRadiusConfig);
        syncManager.syncValue("oreChunkRadius", chunkRadiusSyncer);

        return new ButtonWidget<>().onMousePressed(mouseButton -> {
            if (!baseMetaTileEntity.isActive()) {
                multiblock.adjustChunkRadius(mouseButton == 0);
            }
            return true;
        })
            .overlay(new DynamicDrawable(() -> getLockedOverlay(GTGuiTextures.OVERLAY_BUTTON_WORK_AREA)))
            .tooltipBuilder(t -> {
                t.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.button.ore_drill_radius_1",
                            formatNumber((long) chunkRadiusSyncer.getValue() << 4))));
                t.addLine(IKey.lang("GT5U.gui.button.ore_drill_radius_2"));
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
        syncManager.syncValue("oreShowWorkArea", showWorkAreaSyncer);

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
