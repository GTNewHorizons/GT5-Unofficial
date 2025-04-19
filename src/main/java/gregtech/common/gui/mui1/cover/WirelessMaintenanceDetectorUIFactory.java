package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.covers.Cover;
import gregtech.common.covers.redstone.CoverWirelessMaintenanceDetector;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class WirelessMaintenanceDetectorUIFactory
    extends AdvancedRedstoneTransmitterBaseUIFactory<CoverWirelessMaintenanceDetector> {

    private static final String[] extraTexts = new String[] { "No Issues", ">= 1 Issue", ">= 2 Issues", ">= 3 Issues",
        ">= 4 Issues", ">= 5 Issues", "Rotor < 20%", "Rotor ≈ 0%" };

    public WirelessMaintenanceDetectorUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected int getGUIHeight() {
        return 175;
    }

    @Override
    protected CoverWirelessMaintenanceDetector adaptCover(Cover cover) {
        if (cover instanceof CoverWirelessMaintenanceDetector adaptedCover) {
            return adaptedCover;
        }
        return null;
    }

    @Override
    protected @NotNull CoverDataControllerWidget<CoverWirelessMaintenanceDetector> getDataController() {
        return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        super.addUIWidgets(builder);
        for (int i = 0; i < 8; i++) {
            builder.widget(
                new TextWidget(extraTexts[i]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * (i % 2 == 0 ? 1 : 7), 4 + startY + spaceY * (2 + i / 2)));
        }
        builder.widget(
            TextWidget
                .dynamicString(
                    getCoverString(
                        c -> c.isPhysical() ? StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.1")
                            : StatCollector.translateToLocal("gt.cover" + ".wirelessdetector.redstone.0")))
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_GRAY.get())
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(startX + spaceX, 4 + startY + spaceY * 6)
                .setSize(spaceX * 10, 12));
    }

    @Override
    protected void addUIForDataController(CoverDataControllerWidget<CoverWirelessMaintenanceDetector> controller) {
        super.addUIForDataController(controller);
        for (int i = 0; i < 8; i++) {
            final int index = i;
            controller.addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getMode() == CoverWirelessMaintenanceDetector.MaintenanceMode.values()[index],
                (coverData, state) -> coverData
                    .setMode(CoverWirelessMaintenanceDetector.MaintenanceMode.values()[index]),
                widget -> widget.setToggleTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK, GTUITextures.TRANSPARENT)
                    .setPos(spaceX * (index % 2 == 0 ? 0 : 6), spaceY * (2 + index / 2)));
        }
        controller.addFollower(
            CoverDataFollowerToggleButtonWidget.ofDisableable(),
            CoverWirelessMaintenanceDetector::isPhysical,
            CoverWirelessMaintenanceDetector::setPhysical,
            widget -> widget
                .addTooltip(StatCollector.translateToLocal("gt.cover" + ".wirelessdetector.redstone.tooltip"))
                .setPos(0, 1 + spaceY * 6));
    }
}
