package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class ShutterUIFactory extends CoverLegacyDataUIFactory {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public ShutterUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCover,
                    (index, coverData) -> index == coverData.getVariable(),
                    (index, coverData) -> coverData.setVariable(index),
                    getUIBuildContext())
                        .addToggleButton(
                            0,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 3))
                        .setPos(startX, startY))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Shutter.Open_Enabled"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Shutter.Open_Disabled"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Shutter.OutAllow"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Shutter.InAllow"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 3));
    }
}
