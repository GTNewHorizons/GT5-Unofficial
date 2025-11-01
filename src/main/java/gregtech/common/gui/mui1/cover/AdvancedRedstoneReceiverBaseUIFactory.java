package gregtech.common.gui.mui1.cover;

import java.util.Arrays;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneReceiverBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class AdvancedRedstoneReceiverBaseUIFactory
    extends AdvancedWirelessRedstoneBaseUIFactory<CoverAdvancedRedstoneReceiverBase> {

    public AdvancedRedstoneReceiverBaseUIFactory(CoverUIBuildContext buildContext) {
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
        return false;
    }

    @Override
    protected CoverAdvancedRedstoneReceiverBase adaptCover(Cover cover) {
        if (cover instanceof CoverAdvancedRedstoneReceiverBase adaptedCover) {
            return adaptedCover;
        }
        return null;
    }

    @Override
    protected @NotNull CoverDataControllerWidget<CoverAdvancedRedstoneReceiverBase> getDataController() {
        return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        super.addUIWidgets(builder);
        builder.widget(
            new TextWidget(StatCollector.translateToLocal("GT5U.gui.tooltip.redstone_receiver.GateMode"))
                .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2));
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIForDataController(CoverDataControllerWidget<CoverAdvancedRedstoneReceiverBase> controller) {
        super.addUIForDataController(controller);
        controller
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getGateMode() == CoverAdvancedRedstoneReceiverBase.GateMode.AND,
                (coverData, state) -> coverData.setMode(CoverAdvancedRedstoneReceiverBase.GateMode.AND),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_AND)
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.tooltip.redstone_receiver.AND_Gate"))
                    .setPos(spaceX * 0, spaceY * 2))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getGateMode() == CoverAdvancedRedstoneReceiverBase.GateMode.NAND,
                (coverData, state) -> coverData.setMode(CoverAdvancedRedstoneReceiverBase.GateMode.NAND),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_NAND)
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.tooltip.redstone_receiver.NAND_Gate"))
                    .setPos(spaceX * 1, spaceY * 2))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getGateMode() == CoverAdvancedRedstoneReceiverBase.GateMode.OR,
                (coverData, state) -> coverData.setMode(CoverAdvancedRedstoneReceiverBase.GateMode.OR),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_OR)
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.tooltip.redstone_receiver.OR_Gate"))
                    .setPos(spaceX * 2, spaceY * 2))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getGateMode() == CoverAdvancedRedstoneReceiverBase.GateMode.NOR,
                (coverData, state) -> coverData.setMode(CoverAdvancedRedstoneReceiverBase.GateMode.NOR),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_NOR)
                    .addTooltip(StatCollector.translateToLocal("GT5U.gui.tooltip.redstone_receiver.NOR_Gate"))
                    .setPos(spaceX * 3, spaceY * 2))
            .addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.getGateMode() == CoverAdvancedRedstoneReceiverBase.GateMode.SINGLE_SOURCE,
                (coverData, state) -> coverData.setMode(CoverAdvancedRedstoneReceiverBase.GateMode.SINGLE_SOURCE),
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ANALOG)
                    .addTooltips(
                        Arrays.asList(
                            GTUtility.breakLines(
                                StatCollector.translateToLocal("GT5U.gui.tooltip.redstone_receiver.analog"))))
                    .setPos(spaceX * 4, spaceY * 2));
    }
}
