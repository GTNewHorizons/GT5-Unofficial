package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_DISABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_ENABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase.WorkState;

public class MTEDrillerBaseGui<T extends MTEDrillerBase> extends MTEMultiBlockBaseGui<T> {

    public MTEDrillerBaseGui(T base) {
        super(base);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("chunkLoadingEnabled", new BooleanSyncValue(multiblock::isChunkLoadingEnabled, val -> {
            if (!baseMetaTileEntity.isActive()) {
                multiblock.setChunkLoadingEnabled(val);
            }
        }).allowC2S());
        syncManager.syncValue(
            "drillerWorkState",
            new EnumSyncValue<>(WorkState.class, multiblock::getWorkState, multiblock::setWorkState));
        syncManager.syncValue(
            "drillerShutdownReason",
            new StringSyncValue(multiblock::getShutdownReason, multiblock::setShutdownReason));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        StringSyncValue shutdownReasonSync = syncManager
            .findSyncHandler("drillerShutdownReason", StringSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(shutdownReasonSync::getValue)
                .asWidget()
                .fullWidth()
                .marginBottom(2)
                .setEnabledIf(
                    w -> !baseMetaTileEntity.isActive() && GTUtility.isStringValid(shutdownReasonSync.getValue())));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildrenWidth()
            .fullHeight()
            .childIf(shouldDisplayVoidExcess(), () -> createVoidExcessButton(syncManager))
            .childIf(shouldDisplayInputSeparation(), () -> createInputSeparationButton(syncManager))
            .childIf(shouldDisplayBatchMode(), () -> createBatchModeButton(syncManager))
            .childIf(shouldDisplayRecipeLock(), () -> createLockToSingleRecipeButton(syncManager))
            .child(createChunkLoadingToggle(syncManager))
            .childIf(!machineModeIcons.isEmpty(), () -> createModeSwitchButton(syncManager));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createRetractPipesButton(syncManager));
    }

    @Override
    protected boolean shouldDisplayRecipeLock() {
        return false;
    }

    protected ToggleButton createChunkLoadingToggle(PanelSyncManager syncManager) {
        BooleanSyncValue chunkLoadingSyncer = syncManager
            .findSyncHandler("chunkLoadingEnabled", BooleanSyncValue.class);
        return new ToggleButton().value(chunkLoadingSyncer)
            .overlay(true, new DynamicDrawable(() -> getLockedOverlay(GTGuiTextures.OVERLAY_BUTTON_CHUNK_LOADING)))
            .overlay(false, new DynamicDrawable(() -> getLockedOverlay(GTGuiTextures.OVERLAY_BUTTON_CHUNK_LOADING_OFF)))
            .tooltipBuilder(true, t -> {
                t.addLine(StatCollector.translateToLocal("GT5U.gui.button.chunk_loading"));
                t.addLine(
                    GTUtility
                        .getColoredSecondaryTooltip(StatCollector.translateToLocal(BUTTON_FEATURE_ENABLED_TOOLTIP)));
                if (baseMetaTileEntity.isActive()) {
                    t.addLine(StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
                }
            })
            .tooltipBuilder(false, t -> {
                t.addLine(StatCollector.translateToLocal("GT5U.gui.button.chunk_loading"));
                t.addLine(
                    GTUtility
                        .getColoredSecondaryTooltip(StatCollector.translateToLocal(BUTTON_FEATURE_DISABLED_TOOLTIP)));
                if (baseMetaTileEntity.isActive()) {
                    t.addLine(StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
                }
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected ButtonWidget<?> createRetractPipesButton(PanelSyncManager syncManager) {
        EnumSyncValue<WorkState, ?> workStateSyncer = syncManager
            .findSyncHandler("drillerWorkState", EnumSyncValue.class);
        return new ButtonWidget<>().onMousePressed(mouseButton -> {
            multiblock.abortDrilling();
            return true;
        })
            .overlay(
                new DynamicDrawable(
                    () -> workStateSyncer.getValue() == WorkState.ABORT ? new DrawableStack(
                        GTGuiTextures.OVERLAY_BUTTON_RETRACT_PIPE,
                        GTGuiTextures.OVERLAY_BUTTON_LOCKED) : GTGuiTextures.OVERLAY_BUTTON_RETRACT_PIPE))
            .tooltipBuilder(t -> {
                boolean aborting = workStateSyncer.getValue() == WorkState.ABORT;
                t.addLine(
                    IKey.lang(
                        aborting ? "GT5U.gui.button.drill_retract_pipes_active"
                            : "GT5U.gui.button.drill_retract_pipes"));
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IDrawable getLockedOverlay(IDrawable base) {
        if (baseMetaTileEntity.isActive()) {
            return new DrawableStack(base, GTGuiTextures.OVERLAY_BUTTON_LOCKED);
        }
        return base;
    }
}
