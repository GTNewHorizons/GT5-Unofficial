package gregtech.common.gui.mui1.cover;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;
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
        final String INVERTED = GTUtility.trans("INVERTED", "Inverted");
        final String NORMAL = GTUtility.trans("NORMAL", "Normal");

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
                        widget -> widget.addTooltip(0, NORMAL)
                            .addTooltip(1, INVERTED)
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
                new TextWidget().setStringSupplier(getCoverString(c -> c.isInverted() ? INVERTED : NORMAL))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX, 4 + startY + spaceY))
            .widget(
                new TextWidget(GTUtility.trans("222.1", "Energy threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX, startY + spaceY * 3 + 4))

            .widget(new FakeSyncWidget.LongSyncer(() -> {
                CoverEUMeter cover = getCover();
                return cover != null ? cover.getType()
                    .getTileEntityEnergyCapacity(getUIBuildContext().getTile()) : Long.MAX_VALUE;
            }, numericWidget::setMaxValue));
    }
}
