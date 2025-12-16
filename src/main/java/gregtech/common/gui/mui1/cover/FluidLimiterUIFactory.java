package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverFluidLimiter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;

public class FluidLimiterUIFactory extends CoverUIFactory<CoverFluidLimiter> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public FluidLimiterUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected CoverFluidLimiter adaptCover(Cover cover) {
        if (cover instanceof CoverFluidLimiter adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) Math.round(coverData.getThreshold() * 100),
                    (coverData, val) -> coverData.setThreshold(val.floatValue() / 100),
                    widget -> widget.setBounds(0, 100)
                        .setFocusOnGuiOpen(true)
                        .setPos(startX, startY + spaceY * 2 - 24)
                        .setSize(spaceX * 4 - 3, 12)))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.fluid_limiter.threshold"))
                    .setPos(startX, startY + spaceY * 2 - 35));
    }
}
