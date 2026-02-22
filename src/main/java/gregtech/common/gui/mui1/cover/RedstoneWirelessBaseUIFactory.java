package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.CoverRedstoneWirelessBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class RedstoneWirelessBaseUIFactory extends CoverLegacyDataUIFactory {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public RedstoneWirelessBaseUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected int getGUIWidth() {
        return 250;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) getFlagFrequency(coverData.getVariable()),
                        (coverData, state) -> coverData
                            .setVariable(state.intValue() | getFlagCheckbox(coverData.getVariable())),
                        widget -> widget.setBounds(0, CoverRedstoneWirelessBase.MAX_CHANNEL)
                            .setScrollValues(1, 1000, 10)
                            .setFocusOnGuiOpen(true)
                            .setPos(spaceX * 0, spaceY * 0 + 2)
                            .setSize(spaceX * 4 - 3, 12))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofCheck(),
                        coverData -> getFlagCheckbox(coverData.getVariable()) > 0,
                        (coverData, state) -> coverData.setVariable(
                            getFlagFrequency(coverData.getVariable())
                                | (state ? CoverRedstoneWirelessBase.CHECKBOX_MASK : 0)),
                        widget -> widget.setPos(spaceX * 0, spaceY * 2))
                    .setPos(startX, startY))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.freq"))
                    .setPos(startX + spaceX * 4, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.RedstoneWirelessBase.Use_Private_Freq"))
                    .setPos(startX + spaceX * 1, startY + spaceY * 2 + 4));
    }

    private int getFlagFrequency(int coverVariable) {
        return coverVariable & CoverRedstoneWirelessBase.PUBLIC_MASK;
    }

    private int getFlagCheckbox(int coverVariable) {
        return coverVariable & CoverRedstoneWirelessBase.CHECKBOX_MASK;
    }
}
