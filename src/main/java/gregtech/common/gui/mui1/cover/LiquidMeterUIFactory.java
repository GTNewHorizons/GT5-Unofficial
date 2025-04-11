package gregtech.common.gui.mui1.cover;

import java.util.Arrays;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
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
    private int maxCapacity;

    public LiquidMeterUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
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
        final String INVERTED = GTUtility.getDescLoc("inverted");
        final String NORMAL = GTUtility.getDescLoc("normal");

        setMaxCapacity();

        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofRedstone(),
                        CoverLiquidMeter::isInverted,
                        CoverLiquidMeter::setInverted,
                        widget -> widget.addTooltip(0, NORMAL)
                            .addTooltip(1, INVERTED)
                            .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.getThreshold(),
                        (coverData, state) -> coverData.setThresdhold(state.intValue()),
                        widget -> widget.setBounds(0, maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE)
                            .setScrollValues(1000, 144, 100000)
                            .setFocusOnGuiOpen(true)
                            .setPos(spaceX * 0, spaceY * 1 + 2)
                            .setSize(spaceX * 4 + 5, 12))
                    .setPos(startX, startY))
            .widget(
                new TextWidget().setStringSupplier(getCoverString(c -> c.isInverted() ? INVERTED : NORMAL))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(GTUtility.trans("222", "Fluid threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 - 10, startY + spaceY * 1 + 4));
    }

    private void setMaxCapacity() {
        final ICoverable tile = getUIBuildContext().getTile();
        if (!tile.isDead() && tile instanceof IFluidHandler) {
            FluidTankInfo[] tanks = ((IFluidHandler) tile).getTankInfo(ForgeDirection.UNKNOWN);
            maxCapacity = Arrays.stream(tanks)
                .mapToInt(tank -> tank.capacity)
                .sum();
        } else {
            maxCapacity = -1;
        }
    }
}
