package gregtech.common.gui.mui1.cover;

import java.util.Arrays;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;
import gregtech.common.covers.redstone.CoverWirelessFluidDetector;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class WirelessFluidDetectorUIFactory
    extends AdvancedRedstoneTransmitterBaseUIFactory<CoverWirelessFluidDetector> {

    private int maxCapacity;

    public WirelessFluidDetectorUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected int getGUIHeight() {
        return 123;
    }

    @Override
    protected CoverWirelessFluidDetector adaptCover(Cover cover) {
        if (cover instanceof CoverWirelessFluidDetector adaptedCover) {
            return adaptedCover;
        }
        return null;
    }

    @Override
    protected @NotNull CoverDataControllerWidget<CoverWirelessFluidDetector> getDataController() {
        return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        setMaxCapacity();
        super.addUIWidgets(builder);
        builder.widget(
            new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Wireless_Fluid_Detector.FluidThreshold"))
                .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2))
            .widget(
                TextWidget
                    .dynamicString(
                        getCoverString(
                            c -> c.isPhysical()
                                ? StatCollector.translateToLocal("gt.cover" + ".wirelessdetector.redstone.1")
                                : StatCollector.translateToLocal("gt.cover" + ".wirelessdetector.redstone.0")))
                    .setSynced(false)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(startX + spaceX, 4 + startY + spaceY * 3)
                    .setSize(spaceX * 10, 12));
    }

    @Override
    protected void addUIForDataController(CoverDataControllerWidget<CoverWirelessFluidDetector> controller) {
        super.addUIForDataController(controller);
        controller
            .addFollower(
                new CoverDataFollowerNumericWidget<>(),
                coverData -> (double) coverData.getThreshold(),
                (coverData, state) -> coverData.setThreshold(state.intValue()),
                widget -> widget.setBounds(0, maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE)
                    .setScrollValues(1000, 144, 100000)
                    .setFocusOnGuiOpen(true)
                    .setPos(1, 2 + spaceY * 2)
                    .setSize(spaceX * 5 - 4, 12))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                CoverWirelessFluidDetector::isPhysical,
                CoverWirelessFluidDetector::setPhysical,
                widget -> widget
                    .addTooltip(StatCollector.translateToLocal("gt.cover" + ".wirelessdetector.redstone.tooltip"))
                    .setPos(0, 1 + spaceY * 3));
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
