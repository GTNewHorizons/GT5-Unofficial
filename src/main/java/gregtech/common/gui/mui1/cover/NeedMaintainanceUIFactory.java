package gregtech.common.gui.mui1.cover;

import static gregtech.api.util.GTUtility.getDescLoc;

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
        final String[] tooltipText = { maintLoc(1, false), maintLoc(2, false), maintLoc(3, false), maintLoc(4, false),
            maintLoc(5, false), getDescLoc("need_maint_rotor_lo"), getDescLoc("need_maint_rotor_hi") };

        final String[] buttonText = { issueLoc(1), issueLoc(2), issueLoc(3), issueLoc(4), issueLoc(5),
            getDescLoc("issue_rotor_low"), getDescLoc("issue_rotor_dead"), getDescLoc("inverted"),
            getDescLoc("normal") };

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
                            widget -> widget.addTooltip(tooltipText[0])
                                .setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(tooltipText[1])
                                .setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(tooltipText[2])
                                .setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(tooltipText[3])
                                .setPos(spaceX * 0, spaceY * 3))
                        .addToggleButton(
                            4,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(tooltipText[4])
                                .setPos(spaceX * 4 + 4, spaceY * 0))
                        .addToggleButton(
                            5,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(tooltipText[5])
                                .setPos(spaceX * 4 + 4, spaceY * 1))
                        .addToggleButton(
                            6,
                            CoverDataFollowerToggleButtonWidget.ofCheck(),
                            widget -> widget.addTooltip(tooltipText[6])
                                .setPos(spaceX * 4 + 4, spaceY * 2))
                        .addToggleButton(
                            7,
                            CoverDataFollowerToggleButtonWidget.ofRedstone(),
                            widget -> widget.setPos(spaceX * 4 + 4, spaceY * 3))
                        .setPos(startX, startY))
            .widget(
                new TextWidget(buttonText[0]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(buttonText[1]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(buttonText[2]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 2))
            .widget(
                new TextWidget(buttonText[3]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 1, 4 + startY + spaceY * 3))
            .widget(
                new TextWidget(buttonText[4]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(buttonText[5]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(buttonText[6]).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 2))
            .widget(
                TextWidget
                    .dynamicString(getCoverString(c -> isEnabled(7, c.getVariable()) ? buttonText[7] : buttonText[8]))
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

    public static String maintLoc(int count, boolean inverted) {
        return String.format(getDescLoc("need_maint_count"), count, inverted ? getDescLoc("inverted_b") : "");
    }

    private static String issueLoc(int count) {
        return count == 1 ? getDescLoc("issue") : String.format(getDescLoc("issues"), count);
    }
}
