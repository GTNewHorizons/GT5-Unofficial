package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public abstract class AdvancedRedstoneTransmitterBaseUIFactory<C extends CoverAdvancedRedstoneTransmitterBase>
    extends AdvancedWirelessRedstoneBaseUIFactory<C> {

    public AdvancedRedstoneTransmitterBaseUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected int getFrequencyRow() {
        return 0;
    }

    @Override
    protected int getButtonRow() {
        return 1;
    }

    @Override
    protected boolean isShiftPrivateLeft() {
        return true;
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        super.addUIWidgets(builder);
        builder.widget(
            TextWidget
                .dynamicString(
                    getCoverString(
                        c -> c.isInverted() ? translateToLocal("gt.interact.desc.inverted")
                            : translateToLocal("gt.interact.desc.normal")))
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_GRAY.get())
                .setPos(startX + spaceX * 10, 4 + startY + spaceY * getButtonRow()));
    }

    @Override
    protected void addUIForDataController(CoverDataControllerWidget<C> controller) {
        super.addUIForDataController(controller);
        controller.addFollower(
            CoverDataFollowerToggleButtonWidget.ofRedstone(),
            CoverAdvancedRedstoneTransmitterBase::isInverted,
            (coverData, state) -> {
                coverData.setInverted(state);
                return coverData;
            },
            widget -> widget.addTooltip(0, translateToLocal("gt.interact.desc.normal"))
                .addTooltip(1, translateToLocal("gt.interact.desc.inverted"))
                .setPos(spaceX * 9, spaceY * getButtonRow()));
    }
}
