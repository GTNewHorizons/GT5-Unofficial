package gregtech.common.gui.mui1.cover;

import static gregtech.common.covers.CoverDoesWork.*;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class DoesWorkUIFactory extends CoverLegacyDataUIFactory {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public DoesWorkUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCover,
                    (id, coverData) -> isEnabled(id, coverData.getVariable()),
                    (id, coverData) -> coverData.setVariable(getNewCoverVariable(id, coverData.getVariable())),
                    getUIBuildContext())
                        .addToggleButton(
                            0,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_PROGRESS)
                                .setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                                .setPos(spaceX * 1, spaceY * 0))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                                .setPos(spaceX * 2, spaceY * 0))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofRedstone(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 1))
                        .setPos(startX, startY))
            .widget(TextWidget.dynamicString(getCoverString(c -> {
                int coverVariable = c.getVariable();

                if (isFlagSet(coverVariable, FLAG_ENABLED)) {
                    return translateToLocal("gt.interact.desc.MachineEnabled");
                } else if (isFlagSet(coverVariable, FLAG_PROGRESS)) {
                    return translateToLocal("gt.interact.desc.MachineIdle");
                } else {
                    return translateToLocal("gt.interact.desc.RecipeProgress");
                }

            }))
                .setSynced(false)
                .setPos(startX + spaceX * 3, 4 + startY + spaceY * 0))
            .widget(
                TextWidget
                    .dynamicString(
                        getCoverString(
                            c -> isFlagSet(c.getVariable(), FLAG_INVERTED)
                                ? translateToLocal("gt.interact.desc.inverted")
                                : translateToLocal("gt.interact.desc.normal")))
                    .setSynced(false)
                    .setPos(startX + spaceX * 3, 4 + startY + spaceY * 1));
    }

    private int getNewCoverVariable(int id, int coverVariable) {
        switch (id) {
            case 0 -> {
                return (coverVariable & ~FLAG_ENABLED) & ~FLAG_PROGRESS;
            }
            case 1 -> {
                return (coverVariable & ~FLAG_ENABLED) | FLAG_PROGRESS;
            }
            case 2 -> {
                return (coverVariable & ~FLAG_PROGRESS) | FLAG_ENABLED;
            }
            case 3 -> {
                if (isFlagSet(coverVariable, FLAG_INVERTED)) {
                    return coverVariable & ~FLAG_INVERTED;
                } else {
                    return coverVariable | FLAG_INVERTED;
                }
            }
        }
        return coverVariable;
    }

    private boolean isEnabled(int id, int coverVariable) {
        return switch (id) {
            case 0 -> !isFlagSet(coverVariable, FLAG_PROGRESS) && !isFlagSet(coverVariable, FLAG_ENABLED);
            case 1 -> isFlagSet(coverVariable, FLAG_PROGRESS);
            case 2 -> isFlagSet(coverVariable, FLAG_ENABLED);
            case 3 -> isFlagSet(coverVariable, FLAG_INVERTED);
            default -> true;
        };
    }
}
