package gregtech.common.gui.mui1.cover;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.util.GTUtility;
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
        final String[] tooltipText = { GTUtility.trans("056", "Emit if 1 " + "Maintenance Needed"),
            GTUtility.trans("058", "Emit if 2 Maintenance Needed"),
            GTUtility.trans("060", "Emit if 3 Maintenance Needed"),
            GTUtility.trans("062", "Emit if 4 Maintenance Needed"),
            GTUtility.trans("064", "Emit if 5 Maintenance Needed"),
            GTUtility.trans("066", "Emit if rotor needs maintenance low " + "accuracy mod"),
            GTUtility.trans("068", "Emit if rotor needs maintenance high " + "accuracy mod"), };

        final String[] buttonText = { GTUtility.trans("247", "1 Issue"), GTUtility.trans("248", "2 Issues"),
            GTUtility.trans("249", "3 Issues"), GTUtility.trans("250", "4 " + "Issues"),
            GTUtility.trans("251", "5 Issues"), GTUtility.trans("252", "Rotor" + " < 20%"),
            GTUtility.trans("253", "Rotor ≈ 0%"), GTUtility.getDescLoc("inverted"), GTUtility.getDescLoc("normal"), };

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
}
