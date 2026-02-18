package gregtech.common.gui.modularui.cover;

import static gregtech.common.covers.CoverFluidRegulator.TICK_RATE_MAX;
import static gregtech.common.covers.CoverFluidRegulator.TICK_RATE_MIN;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.MathUtils;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverFluidRegulator;
import gregtech.common.covers.modes.MachineProcessingCondition;
import gregtech.common.covers.modes.TransferMode;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;

public class CoverFluidRegulatorGui extends CoverBaseGui<CoverFluidRegulator> {

    private static final NumberFormatMUI numberFormat;

    static {
        numberFormat = new NumberFormatMUI();
        numberFormat.setMaximumFractionDigits(2);
    }

    public CoverFluidRegulatorGui(CoverFluidRegulator cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<TransferMode> ioModeSyncValue = new EnumSyncValue<>(
            TransferMode.class,
            cover::getIOMode,
            cover::setIOMode);
        syncManager.syncValue("io_mode", ioModeSyncValue);
        EnumSyncValue<MachineProcessingCondition> conditionModeSyncValue = new EnumSyncValue<>(
            MachineProcessingCondition.class,
            cover::getMachineProcessingCondition,
            cover::setMachineProcessingCondition);
        syncManager.syncValue("condition_mode", conditionModeSyncValue);
        column.child(
            makeRowLayout().child(positionRow(makeTransferModeRow(ioModeSyncValue)))
                .child(positionRow(makeMachineConditionModeRow(conditionModeSyncValue)))
                .child(positionRow(makeSpeedConfigRow()))
                .child(positionRow(makeAverageSpeedRow()).marginTop(ROW_PADDING)));
    }

    private static Flow makeTransferModeRow(EnumSyncValue<TransferMode> ioModeSyncValue) {
        return Flow.row()
            .child(
                new ParentWidget<>().child(
                    new EnumRowBuilder<>(TransferMode.class).value(ioModeSyncValue)
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT, GTGuiTextures.OVERLAY_BUTTON_IMPORT)
                        .build())
                    .width(80))
            .child(
                IKey.lang("gt.interact.desc.fluid_regulator.ExpImp")
                    .asWidget());
    }

    private static Flow makeMachineConditionModeRow(EnumSyncValue<MachineProcessingCondition> conditionModeSyncValue) {
        return Flow.row()
            .child(
                new ParentWidget<>()
                    .child(
                        new EnumRowBuilder<>(MachineProcessingCondition.class).value(conditionModeSyncValue)
                            .overlay(
                                GTGuiTextures.OVERLAY_BUTTON_CHECKMARK,
                                GTGuiTextures.OVERLAY_BUTTON_USE_PROCESSING_STATE,
                                GTGuiTextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                            .build())
                    .width(80))
            .child(
                IKey.lang("gt.interact.desc.fluid_regulator.Conditional")
                    .asWidget());
    }

    private Flow makeSpeedConfigRow() {
        return Flow.row()
            .child(
                makeNumberField().value(new IntSyncValue(cover::getSpeed, cover::setSpeed))
                    .setNumbers(cover::getMinSpeed, cover::getMaxSpeed)
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("gt.interact.desc.fluid_regulator.L")
                    .asWidget())
            .child(
                makeNumberField(36).value(new IntSyncValue(cover::getTickRateForUi, cover::setTickRateForUi))
                    .setValidator(this::validateTickRateText))
            .child(
                IKey.lang("gt.interact.desc.fluid_regulator.Ticks")
                    .asWidget());
    }

    private @NotNull String validateTickRateText(String tickRateText) {
        return Long
            .toString(
                (long) valiateTickRate(
                    MathUtils.parseExpression(tickRateText, cover.getTickRateForUi(), true)
                        .getResult() == null
                            ? 20
                            : MathUtils.parseExpression(tickRateText, cover.getTickRateForUi(), true)
                                .getResult()
                                .getNumberValue()
                                .doubleValue()));
    }

    /**
     * This extra validation is required to make sure we don't exceed the maximum transfer rate of the cover
     */
    private double valiateTickRate(double val) {
        final int speed = cover.getSpeed();
        final long transferRate = cover.getTransferRate();
        if (val > TICK_RATE_MAX) {
            val = (long) TICK_RATE_MAX;
        } else if (Math.abs(speed) > transferRate * val) {
            val = Math.min(TICK_RATE_MAX, (Math.abs(speed) + transferRate - 1) / transferRate);
        } else if (val < TICK_RATE_MIN) {
            val = TICK_RATE_MIN;
        }
        return val;
    }

    private Flow makeAverageSpeedRow() {
        return Flow.row()
            .collapseDisabledChild()
            .child(
                IKey.dynamic(this::getAverageSpeedText)
                    .color(Color.RED.main)
                    .asWidget()
                    .setEnabledIf((w) -> isAtSpeedLimit()))
            .child(
                IKey.dynamic(this::getAverageSpeedText)
                    .asWidget()
                    .setEnabledIf((w1) -> !isAtSpeedLimit()));
    }

    private boolean isAtSpeedLimit() {
        return Math.abs(cover.getSpeed()) == cover.getMaxSpeed();
    }

    private @NotNull String getAverageSpeedText() {
        return GTUtility.translate("gt.interact.desc.fluid_regulator.Average") + " "
            + numberFormat.format(cover.getTickRateForUi() == 0 ? 0 : cover.getSpeed() * 20d / cover.getTickRateForUi())
            + " "
            + GTUtility.translate("gt.interact.desc.fluid_regulator.L_Sec");
    }
}
