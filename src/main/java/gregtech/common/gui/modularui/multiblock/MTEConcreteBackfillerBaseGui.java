package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_DISABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_ENABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.MTEConcreteBackfillerBase;
import gregtech.common.tileentities.machines.multi.MTEDrillerBase.WorkState;

public class MTEConcreteBackfillerBaseGui extends MTEDrillerBaseGui<MTEConcreteBackfillerBase> {

    public MTEConcreteBackfillerBaseGui(MTEConcreteBackfillerBase base) {
        super(base);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("backfillerLiquidEnabled", new BooleanSyncValue(multiblock::isLiquidEnabled, val -> {
            if (!baseMetaTileEntity.isActive()) {
                multiblock.setLiquidEnabled(val);
            }
        }).allowC2S());
        syncManager.syncValue("backfillerYHead", new IntSyncValue(multiblock::getYHead, multiblock::setClientYHead));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue yHeadSync = syncManager.findSyncHandler("backfillerYHead", IntSyncValue.class);
        EnumSyncValue<WorkState, ?> workStateSync = syncManager
            .findSyncHandler("drillerWorkState", EnumSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(
                () -> StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.backfiller_current_area",
                    formatNumber(yHeadSync.getValue())))
                .asWidget()
                .fullWidth()
                .marginBottom(2)
                .setEnabledIf(w -> baseMetaTileEntity.isActive() && workStateSync.getValue() == WorkState.UPWARD));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createLiquidFillingToggle(syncManager));
    }

    protected ToggleButton createLiquidFillingToggle(PanelSyncManager syncManager) {
        BooleanSyncValue liquidSyncer = syncManager.findSyncHandler("backfillerLiquidEnabled", BooleanSyncValue.class);

        return new ToggleButton().value(liquidSyncer)
            .overlay(new DynamicDrawable(() -> {
                IDrawable base = liquidSyncer.getValue() ? GTGuiTextures.OVERLAY_BUTTON_LIQUIDMODE
                    : GTGuiTextures.OVERLAY_BUTTON_LIQUIDMODE_OFF;
                return getLockedOverlay(base);
            }))
            .tooltipBuilder(true, t -> {
                t.addLine(StatCollector.translateToLocal("GT5U.gui.button.liquid_filling"));
                t.addLine(
                    GTUtility
                        .getColoredSecondaryTooltip(StatCollector.translateToLocal(BUTTON_FEATURE_ENABLED_TOOLTIP)));
                if (baseMetaTileEntity.isActive()) {
                    t.addLine(StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
                }
            })
            .tooltipBuilder(false, t -> {
                t.addLine(StatCollector.translateToLocal("GT5U.gui.button.liquid_filling"));
                t.addLine(
                    GTUtility
                        .getColoredSecondaryTooltip(StatCollector.translateToLocal(BUTTON_FEATURE_DISABLED_TOOLTIP)));
                if (baseMetaTileEntity.isActive()) {
                    t.addLine(StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
                }
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
