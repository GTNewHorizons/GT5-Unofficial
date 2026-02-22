package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverLiquidMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class LiquidMeterUIFactory extends CoverUIFactory<CoverLiquidMeter> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;
    private final int maxCapacity;

    public LiquidMeterUIFactory(CoverUIBuildContext buildContext, int maxCapacity) {
        super(buildContext);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected CoverLiquidMeter adaptCover(Cover cover) {
        if (cover instanceof CoverLiquidMeter adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofRedstone(),
                        CoverLiquidMeter::isInverted,
                        CoverLiquidMeter::setInverted,
                        widget -> widget.addTooltip(0, translateToLocal("gt.interact.desc.normal.tooltip"))
                            .addTooltip(1, translateToLocal("gt.interact.desc.inverted.tooltip"))
                            .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.getThreshold(),
                        (coverData, state) -> coverData.setThreshold(state.intValue()),
                        widget -> widget.setBounds(0, maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE)
                            .setScrollValues(1000, 144, 100000)
                            .setFocusOnGuiOpen(true)
                            .setPos(spaceX * 0, spaceY * 1 + 2)
                            .setSize(spaceX * 4 + 5, 12))
                    .setPos(startX, startY))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        getCoverString(
                            c -> c.isInverted() ? translateToLocal("gt.interact.desc.inverted")
                                : translateToLocal("gt.interact.desc.normal")))
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Fluid_Meter.FluidThreshold"))
                    .setPos(startX + spaceX * 5 - 10, startY + spaceY * 1 + 4));
    }
}
