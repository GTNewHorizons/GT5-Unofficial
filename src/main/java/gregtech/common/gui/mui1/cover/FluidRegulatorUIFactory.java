package gregtech.common.gui.mui1.cover;

import static gregtech.common.covers.CoverFluidRegulator.TICK_RATE_MAX;
import static gregtech.common.covers.CoverFluidRegulator.TICK_RATE_MIN;

import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverFluidRegulator;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class FluidRegulatorUIFactory extends CoverUIFactory<CoverFluidRegulator> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    private static final NumberFormatMUI numberFormat;

    static {
        numberFormat = new NumberFormatMUI();
        numberFormat.setMaximumFractionDigits(2);
    }

    public FluidRegulatorUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected CoverFluidRegulator adaptCover(Cover cover) {
        if (cover instanceof CoverFluidRegulator adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        AtomicBoolean warn = new AtomicBoolean(false);

        builder.widget(
            new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getSpeed() >= 0,
                    (coverData, state) -> coverData.setSpeed(Math.abs(coverData.getSpeed())),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                        .addTooltip(StatCollector.translateToLocal("gt.interact.desc.export.tooltip"))
                        .setPos(spaceX * 0, spaceY * 0))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getSpeed() <= 0,
                    (coverData, state) -> coverData.setSpeed(-Math.abs(coverData.getSpeed())),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                        .addTooltip(StatCollector.translateToLocal("gt.interact.desc.import.tooltip"))
                        .setPos(spaceX * 1, spaceY * 0))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getCondition() == CoverFluidRegulator.Conditional.Always,
                    (coverData, state) -> coverData.setCondition(CoverFluidRegulator.Conditional.Always),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltip(StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.AlwaysOn"))
                        .setPos(spaceX * 0, spaceY * 1))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getCondition() == CoverFluidRegulator.Conditional.Conditional,
                    (coverData, state) -> coverData.setCondition(CoverFluidRegulator.Conditional.Conditional),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_PROCESSING_STATE)
                        .addTooltip(StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.MachProcState"))
                        .setPos(spaceX * 1, spaceY * 1))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getCondition() == CoverFluidRegulator.Conditional.Inverted,
                    (coverData, state) -> coverData.setCondition(CoverFluidRegulator.Conditional.Inverted),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                        .addTooltip(
                            StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.InvertedMachProcState"))
                        .setPos(spaceX * 2, spaceY * 1))
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.getSpeed(),
                    (coverData, state) -> coverData.setSpeed(state.intValue()),
                    widget -> widget.setBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
                        .setValidator(val -> {
                            CoverFluidRegulator cover = getCover();
                            final int tickRate = cover != null ? cover.getTickRateForUi() : 0;
                            final long maxFlow = (cover != null ? (long) cover.getTransferRate() : 0)
                                * GTUtility.clamp(tickRate, TICK_RATE_MIN, TICK_RATE_MAX);
                            warn.set(false);
                            if (val > maxFlow) {
                                val = maxFlow;
                                warn.set(true);
                            } else if (val < -maxFlow) {
                                val = -maxFlow;
                                warn.set(true);
                            }
                            return val;
                        })
                        .setScrollValues(1, 144, 1000)
                        .setFocusOnGuiOpen(true)
                        .setPos(spaceX * 0, spaceY * 2 + 2)
                        .setSize(spaceX * 4 - 3, 12))
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.getTickRateForUi(),
                    (coverData, state) -> coverData.setTickRateForUi(state.intValue()),
                    widget -> widget.setBounds(0, TICK_RATE_MAX)
                        .setValidator(val -> {
                            CoverFluidRegulator cover = getCover();
                            final int speed = cover != null ? cover.getSpeed() : 0;
                            final long transferRate = cover != null ? (long) cover.getTransferRate() : 0;
                            warn.set(false);
                            if (val > TICK_RATE_MAX) {
                                val = (long) TICK_RATE_MAX;
                                warn.set(true);
                            } else if (Math.abs(speed) > transferRate * val) {
                                val = Math.min(TICK_RATE_MAX, (Math.abs(speed) + transferRate - 1) / transferRate);
                                warn.set(true);
                            } else if (val < TICK_RATE_MIN) {
                                val = 1L;
                            }
                            return val;
                        })
                        .setPos(spaceX * 5, spaceY * 2 + 2)
                        .setSize(spaceX * 2 - 3, 12))
                .setPos(startX, startY))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.ExpImp"))
                    .setPos(3 + startX + spaceX * 4, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.Conditional"))
                    .setPos(3 + startX + spaceX * 4, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.L"))
                    .setPos(startX + spaceX * 4, 4 + startY + spaceY * 2))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.Ticks"))
                    .setPos(startX + spaceX * 7, 4 + startY + spaceY * 2))
            .widget(new TextWidget().setTextSupplier(() -> {
                CoverFluidRegulator cover = getCover();
                if (cover == null) return new Text("");
                int tickRate = cover.getTickRateForUi();
                return new Text(
                    StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.Average")
                        + numberFormat.format(tickRate == 0 ? 0 : cover.getSpeed() * 20d / tickRate)
                        + " "
                        + StatCollector.translateToLocal("gt.interact.desc.fluid_regulator.L_Sec"))
                            .color(warn.get() ? COLOR_TEXT_WARN.get() : COLOR_TEXT_GRAY.get());
            })
                .setPos(startX + spaceX * 0, 4 + startY + spaceY * 3));
    }
}
