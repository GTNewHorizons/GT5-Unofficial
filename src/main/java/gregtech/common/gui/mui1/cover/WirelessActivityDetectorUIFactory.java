package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.covers.Cover;
import gregtech.common.covers.redstone.CoverWirelessDoesWorkDetector;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class WirelessActivityDetectorUIFactory
    extends AdvancedRedstoneTransmitterBaseUIFactory<CoverWirelessDoesWorkDetector> {

    public WirelessActivityDetectorUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected int getGUIHeight() {
        return 123;
    }

    @Override
    protected CoverWirelessDoesWorkDetector adaptCover(Cover cover) {
        if (cover instanceof CoverWirelessDoesWorkDetector adaptedCover) {
            return adaptedCover;
        }
        return null;
    }

    protected @NotNull CoverDataControllerWidget<CoverWirelessDoesWorkDetector> getDataController() {
        return new CoverDataControllerWidget<>(this::getCover, this.getUIBuildContext());
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        super.addUIWidgets(builder);
        builder.widget(TextWidget.dynamicString(() -> {

            CoverWirelessDoesWorkDetector.ActivityMode mode = getCover().getMode();

            if (mode == CoverWirelessDoesWorkDetector.ActivityMode.MACHINE_ENABLED) {
                return StatCollector.translateToLocal("gt.interact.desc.Wireless_Activity.Enabled");
            } else if (mode == CoverWirelessDoesWorkDetector.ActivityMode.MACHINE_IDLE) {
                return StatCollector.translateToLocal("gt.interact.desc.Wireless_Activity.Idle");
            } else {
                return StatCollector.translateToLocal("gt.interact.desc.Wireless_Activity.Recipe_Progress");
            }

        })
            .setSynced(false)
            .setPos(startX + spaceX * 3, 4 + startY + spaceY * 2))
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
    protected void addUIForDataController(CoverDataControllerWidget<CoverWirelessDoesWorkDetector> controller) {
        super.addUIForDataController(controller);

        controller
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getMode() == CoverWirelessDoesWorkDetector.ActivityMode.RECIPE_PROGRESS,
                (coverData, state) -> coverData.setMode(CoverWirelessDoesWorkDetector.ActivityMode.RECIPE_PROGRESS),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_PROGRESS)
                    .addTooltip(StatCollector.translateToLocal("gt.interact.tooltip.Wireless_Activity.Recipe_Progress"))
                    .setPos(spaceX * 0, spaceY * 2))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getMode() == CoverWirelessDoesWorkDetector.ActivityMode.MACHINE_IDLE,
                (coverData, state) -> coverData.setMode(CoverWirelessDoesWorkDetector.ActivityMode.MACHINE_IDLE),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                    .addTooltip(StatCollector.translateToLocal("gt.interact.tooltip.Wireless_Activity.Idle"))
                    .setPos(spaceX * 1, spaceY * 2))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getMode() == CoverWirelessDoesWorkDetector.ActivityMode.MACHINE_ENABLED,
                (coverData, state) -> coverData.setMode(CoverWirelessDoesWorkDetector.ActivityMode.MACHINE_ENABLED),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                    .addTooltip(StatCollector.translateToLocal("gt.interact.tooltip.Wireless_Activity.Enabled"))
                    .setPos(spaceX * 2, spaceY * 2))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                CoverWirelessDoesWorkDetector::isPhysical,
                CoverWirelessDoesWorkDetector::setPhysical,
                widget -> widget
                    .addTooltip(StatCollector.translateToLocal("gt.cover" + ".wirelessdetector.redstone.tooltip"))
                    .setPos(0, 1 + spaceY * 3));
    }
}
