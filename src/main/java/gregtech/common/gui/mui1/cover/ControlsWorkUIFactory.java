package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class ControlsWorkUIFactory extends CoverLegacyDataUIFactory {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public ControlsWorkUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCover,
                    (id, coverData) -> !getClickable(id, coverData.getVariable()),
                    (id, coverData) -> coverData.setVariable(getNewCoverVariable(id, coverData.getVariable())),
                    getUIBuildContext())
                        .addToggleButton(
                            0,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_REDSTONE_ON)
                                .setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                .setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CROSS)
                                .setPos(spaceX * 0, spaceY * 2))
                        .setPos(startX, startY))
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                        coverData -> coverData.getVariable() > 2,
                        (coverData, state) -> coverData
                            .setVariable(adjustCoverVariable(state, coverData.getVariable())),
                        widget -> widget.setPos(spaceX * 0, spaceY * 3))
                    .setPos(startX, startY))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.MachContr.EnRedstone"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.MachContr.DisRedstone"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.MachContr.DisMachine"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.MachContr.SafeMode"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 3));
    }

    private int getNewCoverVariable(int id, int coverVariable) {
        if (coverVariable > 2) {
            return id + 3;
        } else {
            return id;
        }
    }

    private boolean getClickable(int id, int coverVariable) {
        return ((id != coverVariable && id != coverVariable - 3) || id == 3);
    }

    private int adjustCoverVariable(boolean safeMode, int coverVariable) {
        if (safeMode && coverVariable <= 2) {
            coverVariable += 3;
        }
        if (!safeMode && coverVariable > 2) {
            coverVariable -= 3;
        }
        return coverVariable;
    }
}
