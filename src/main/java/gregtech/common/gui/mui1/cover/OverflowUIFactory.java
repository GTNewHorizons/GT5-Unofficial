package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gtPlusPlus.xmod.gregtech.common.covers.CoverOverflowValve;

public final class OverflowUIFactory extends CoverUIFactory<CoverOverflowValve> {

    // width and height of text input for "Overflow Point" and "Voiding Rate"
    private static final int width = 71;
    private static final int height = 10;

    // fluid input buttons coordinates
    private static final int xFI = 43;
    private static final int yFI = 81;

    // fluid output buttons coordinates
    private static final int xFO = 6;
    private static final int yFO = 81;

    // Overflow Point 2x text + input coordinates
    private static final int xOP = 6;
    private static final int yOP = 27;

    // Voiding Rate 2x text + input coordinates
    private static final int xVR = 6;
    private static final int yVR = 53;

    public OverflowUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected CoverOverflowValve adaptCover(Cover cover) {
        if (cover instanceof CoverOverflowValve adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new TextWidget(StatCollector.translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_overflow_point"))
                    .setPos(xOP, yOP))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_liter"))
                    .setPos(xOP + width + 3, yOP + 11))
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.getOverflowPoint(),
                    (coverData, state) -> coverData.setOverflowPoint(state.intValue()),
                    widget -> ifCoverValid(
                        c -> widget.setBounds(c.getMinOverflowPoint(), c.getMaxOverflowPoint())
                            .setScrollValues(1000, 144, 100000)
                            .setFocusOnGuiOpen(true)
                            .setPos(xOP, yOP + 10)
                            .setSize(width, height))))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_voiding_rate"))
                    .setPos(xVR + 6, yVR))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_l_per_update"))
                    .setPos(xVR + width + 3, yVR + 11))
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.getVoidingRate(),
                    (coverData, state) -> coverData.setVoidingRate(state.intValue()),
                    widget -> ifCoverValid(
                        c -> widget.setBounds(c.getMinOverflowPoint(), c.getMaxOverflowPoint())
                            .setScrollValues(1000, 144, 100000)
                            .setFocusOnGuiOpen(true)
                            .setPos(xVR, yVR + 10)
                            .setSize(width, height))))
            .widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCover,
                    this::getClickable,
                    this::updateData,
                    getUIBuildContext())
                        .addToggleButton(
                            0,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                                .addTooltip(
                                    StatCollector
                                        .translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_allow_fluid_input"))
                                .setPos(xFI + 18, yFI))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                .addTooltip(
                                    StatCollector
                                        .translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_block_fluid_input"))
                                .setPos(xFI, yFI))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_OUTPUT)
                                .addTooltip(
                                    StatCollector
                                        .translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_allow_fluid_output"))
                                .setPos(xFO, yFO))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_OUTPUT)
                                .addTooltip(
                                    StatCollector
                                        .translateToLocal("GTPP.gui.text" + ".cover_overflow_valve_block_fluid_output"))
                                .setPos(xFO + 18, yFO)));
    }

    private boolean getClickable(int id, CoverOverflowValve coverData) {
        return switch (id) {
            case 0 -> coverData.canFluidInput();
            case 1 -> !coverData.canFluidInput();
            case 2 -> coverData.canFluidOutput();
            case 3 -> !coverData.canFluidOutput();
            default -> throw new IllegalStateException("Wrong button id: " + id);
        };
    }

    private CoverOverflowValve updateData(int id, CoverOverflowValve coverData) {
        return switch (id) {
            case 0 -> {
                coverData.setCanFluidInput(true);
                yield coverData;
            }
            case 1 -> {
                coverData.setCanFluidInput(false);
                yield coverData;
            }
            case 2 -> {
                coverData.setCanFluidOutput(true);
                yield coverData;
            }
            case 3 -> {
                coverData.setCanFluidOutput(false);
                yield coverData;
            }
            default -> throw new IllegalStateException("Wrong button id: " + id);
        };
    }
}
