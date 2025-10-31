package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverEUMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerCycleButtonWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class EUMeterUIFactory extends CoverUIFactory<CoverEUMeter> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public EUMeterUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected CoverEUMeter adaptCover(Cover cover) {
        if (cover instanceof CoverEUMeter adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        final CoverDataFollowerNumericWidget<CoverEUMeter> numericWidget = new CoverDataFollowerNumericWidget<>();

        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                    .addFollower(
                        new CoverDataFollowerCycleButtonWidget<>(),
                        coverData -> coverData.getType()
                            .ordinal(),
                        (coverData, state) -> coverData.setType(CoverEUMeter.EnergyType.getEnergyType(state)),
                        widget -> widget.setLength(CoverEUMeter.EnergyType.values().length)
                            .addTooltip(
                                state -> CoverEUMeter.EnergyType.getEnergyType(state)
                                    .getTooltip())
                            .setStaticTexture(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                            .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofRedstone(),
                        CoverEUMeter::isInverted,
                        CoverEUMeter::setInverted,
                        widget -> widget.addTooltip(0, translateToLocal("gt.interact.desc.normal.tooltip"))
                            .addTooltip(1, translateToLocal("gt.interact.desc.inverted.tooltip"))
                            .setPos(spaceX * 0, spaceY * 1))
                    .addFollower(
                        numericWidget,
                        coverData -> (double) coverData.getThreshold(),
                        (coverData, state) -> coverData.setThresdhold(state.longValue()),
                        widget -> widget.setScrollValues(1000, 100, 100000)
                            .setFocusOnGuiOpen(true)
                            .setPos(spaceX * 0, spaceY * 2 + 2)
                            .setSize(spaceX * 8, 12))
                    .setPos(startX, startY))
            .widget(
                new TextWidget().setStringSupplier(
                    getCoverString(
                        c -> c.getType()
                            .getTitle()))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX, 4 + startY))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        getCoverString(
                            c -> c.isInverted() ? translateToLocal("gt.interact.desc.inverted")
                                : translateToLocal("gt.interact.desc.normal")))
                    .setPos(startX + spaceX, 4 + startY + spaceY))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.EnergyThreshold"))
                    .setPos(startX, startY + spaceY * 3 + 4))

            .widget(new FakeSyncWidget.LongSyncer(() -> {
                CoverEUMeter cover = getCover();
                return cover != null ? cover.getType()
                    .getTileEntityEnergyCapacity(getUIBuildContext().getTile()) : Long.MAX_VALUE;
            }, numericWidget::setMaxValue));
    }
}
