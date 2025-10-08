package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerTextFieldWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public abstract class AdvancedWirelessRedstoneBaseUIFactory<C extends CoverAdvancedWirelessRedstoneBase>
    extends CoverUIFactory<C> {

    protected static final int startX = 10;
    protected static final int startY = 25;
    protected static final int spaceX = 18;
    protected static final int spaceY = 18;

    public AdvancedWirelessRedstoneBaseUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected int getGUIWidth() {
        return 250;
    }

    protected abstract @NotNull CoverDataControllerWidget<C> getDataController();

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        final int privateExtraColumn = isShiftPrivateLeft() ? 1 : 5;

        CoverDataControllerWidget<C> dataController = getDataController();
        dataController.setPos(startX, startY);
        addUIForDataController(dataController);

        builder.widget(dataController)
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.freq"))
                    .setPos(startX + spaceX * 5, 4 + startY + spaceY * getFrequencyRow()))
            .widget(
                new TextWidget(translateToLocal("gt.interact.desc.freq"))
                    .setPos(startX + spaceX * privateExtraColumn, 4 + startY + spaceY * getButtonRow()));
    }

    protected void addUIForDataController(CoverDataControllerWidget<C> controller) {
        controller.addFollower(
            new CoverDataFollowerTextFieldWidget<>(),
            CoverAdvancedWirelessRedstoneBase::getFrequency,
            (coverData, state) -> {
                coverData.setFrequency(state);
                return coverData;
            },
            widget -> widget.setPos(1, 2 + spaceY * getFrequencyRow())
                .setSize(spaceX * 5 - 4, 12))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofCheck(),
                coverData -> coverData.getUuid() != null,
                (coverData, state) -> {
                    if (state) {
                        coverData.setUuid(
                            getUIBuildContext().getPlayer()
                                .getUniqueID());
                    } else {
                        coverData.setUuid(null);
                    }
                    return coverData;
                },
                widget -> widget.setPos(0, spaceY * getButtonRow()));
    }

    protected abstract int getFrequencyRow();

    protected abstract int getButtonRow();

    protected abstract boolean isShiftPrivateLeft();
}
