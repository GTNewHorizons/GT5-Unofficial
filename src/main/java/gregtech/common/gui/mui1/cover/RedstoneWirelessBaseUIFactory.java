package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverRedstoneWirelessBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class RedstoneWirelessBaseUIFactory extends CoverUIFactory<CoverRedstoneWirelessBase> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public RedstoneWirelessBaseUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected @Nullable CoverRedstoneWirelessBase adaptCover(Cover cover) {
        if (cover instanceof CoverRedstoneWirelessBase adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @Override
    protected int getGUIWidth() {
        return 250;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder.widget(
            new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                new CoverDataFollowerNumericWidget<>(),
                coverData -> (double) coverData.getFrequency(),
                (coverData, state) -> {
                    coverData.setFrequency(state.intValue());
                    return coverData;
                },
                widget -> widget.setBounds(0, CoverRedstoneWirelessBase.MAX_CHANNEL)
                    .setScrollValues(1, 1000, 10)
                    .setFocusOnGuiOpen(true)
                    .setPos(spaceX * 0, spaceY * 0 + 2)
                    .setSize(spaceX * 4 - 3, 12))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofCheck(),
                    CoverRedstoneWirelessBase::isPrivateChannel,
                    (coverData, state) -> {
                        coverData.setPrivateChannel(state);
                        return coverData;
                    },
                    widget -> widget.setPos(spaceX * 0, spaceY * 2))
                .setPos(startX, startY))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.freq"))
                    .setPos(startX + spaceX * 4, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.RedstoneWirelessBase.Use_Private_Freq"))
                    .setPos(startX + spaceX * 1, startY + spaceY * 2 + 4));
    }
}
