package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class NeedMaintainanceUIFactory extends CoverLegacyDataUIFactory {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public NeedMaintainanceUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCover,
                    (index, coverData) -> isEnabled(index, coverData.getVariable()),
                    (index, coverData) -> coverData.setVariable(getNewCoverVariable(index, coverData.getVariable())),
                    getUIBuildContext())
                        .addToggleButton(
                            0,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(getMaintenanceIssuesCount(1, false))
                                .setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(getMaintenanceIssuesCount(2, false))
                                .setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(getMaintenanceIssuesCount(3, false))
                                .setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(getMaintenanceIssuesCount(4, false))
                                .setPos(spaceX * 0, spaceY * 3))
                        .addToggleButton(
                            4,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(getMaintenanceIssuesCount(5, false))
                                .setPos(spaceX * 4 + 4, spaceY * 0))
                        .addToggleButton(
                            5,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(translateToLocal("gt.interact.desc.need_maint_rotor_lo"))
                                .setPos(spaceX * 4 + 4, spaceY * 1))
                        .addToggleButton(
                            6,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(translateToLocal("gt.interact.desc.need_maint_rotor_hi"))
                                .setPos(spaceX * 4 + 4, spaceY * 2))
                        .addToggleButton(
                            7,
                            CoverDataFollowerToggleButtonWidget.ofRedstone(),
                            widget -> widget.setPos(spaceX * 4 + 4, spaceY * 3))
                        .setPos(startX, startY))
            .widget(
                new TextWidget(getIssuesCount(1)).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(getIssuesCount(2)).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(getIssuesCount(3)).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 2))
            .widget(
                new TextWidget(getIssuesCount(4)).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 3))
            .widget(
                new TextWidget(getIssuesCount(5)).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 0))
            .widget(
                TextWidget.localised("gt.interact.desc.issue_rotor_low")
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 1))
            .widget(
                TextWidget.localised("gt.interact.desc.issue_rotor_dead")
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 2))
            .widget(
                TextWidget
                    .dynamicString(
                        getCoverString(
                            c -> isEnabled(7, c.getVariable()) ? translateToLocal("gt.interact.desc.inverted")
                                : translateToLocal("gt.interact.desc.normal")))
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 3));
    }

    private int getNewCoverVariable(int id, int coverVariable) {
        final boolean checked = (coverVariable & 0x1) > 0;
        if (id == 7) {
            if (checked) return coverVariable & ~0x1;
            else return coverVariable | 0x1;
        }
        return (coverVariable & 0x1) | (id << 1);
    }

    private boolean isEnabled(int id, int coverVariable) {
        if (id == 7) return (coverVariable & 0x1) > 0;
        return (coverVariable >>> 1) == id;
    }

    // TODO make this private when MUI1 cover UIs are removed
    public static String getMaintenanceIssuesCount(int count, boolean inverted) {
        return translateToLocalFormatted(
            "gt.interact.desc.need_maint_count",
            count,
            inverted ? translateToLocal("gt.interact.desc.inverted_b") : "");
    }

    private static String getIssuesCount(int count) {
        return count == 1 ? translateToLocal("gt.interact.desc.issue")
            : translateToLocalFormatted("gt.interact.desc.issues", count);
    }
}
