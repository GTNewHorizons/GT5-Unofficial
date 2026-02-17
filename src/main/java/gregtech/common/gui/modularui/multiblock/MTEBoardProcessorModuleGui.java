package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static kubatech.api.Variables.numberFormat;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IRichTextBuilder;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;

import cpw.mods.fml.relauncher.Side;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEBoardProcessorModule;

public class MTEBoardProcessorModuleGui extends MTENanochipAssemblyModuleBaseGui<MTEBoardProcessorModule> {

    public MTEBoardProcessorModuleGui(MTEBoardProcessorModule multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("processedItems", new IntSyncValue(multiblock::getProcessedItems));
        syncManager.syncValue("impurity", new DoubleSyncValue(multiblock::getImpurityPercentage));
        syncManager.syncValue("euMult", new FloatSyncValue(multiblock::getEuMultiplier));
        syncManager.syncValue(
            "automationPercentage",
            new IntSyncValue(multiblock::getAutoFlushPercentage, multiblock::setAutoFlushPercentage));

        syncManager.registerSyncedAction("fillTank", Side.SERVER, p -> multiblock.fillTank());
        syncManager.registerSyncedAction("flushTank", Side.SERVER, p -> multiblock.flushTank());

    }

    @Override
    protected ParentWidget<?> createTerminalParentWidget(ModularPanel panel, PanelSyncManager syncManager) {

        DoubleSyncValue impurity = syncManager.findSyncHandler("impurity", DoubleSyncValue.class);

        final FluidStackTank fluidTank = new FluidStackTank(
            multiblock::getStoredFluidStack,
            multiblock::setStoredFluidStack,
            multiblock::getCapacity);

        final FluidStackTank impurityFluidTank = new FluidStackTank(
            multiblock::getImpurityFluidStack,
            multiblock::setImpurityFluidStack,
            multiblock::getCapacity);

        final FluidSlot fluidSlot = new FluidSlot().syncHandler(
            new FluidSlotSyncHandler(fluidTank).canFillSlot(false)
                .controlsAmount(false))
            .alwaysShowFull(false)
            .size(36, 94)
            .tooltipBuilder(IRichTextBuilder::clearText);

        final FluidSlot impurityFluidSlot = new FluidSlot()
            .syncHandler(
                new FluidSlotSyncHandler(impurityFluidTank).canFillSlot(false)
                    .controlsAmount(false))
            .alwaysShowFull(false)
            .size(36, 94)
            .tooltipBuilder(t -> {
                t.clearText();
                if (fluidTank.getFluid() != null) {
                    t.addLine(
                        EnumChatFormatting.BLUE + translateToLocal("GT5U.tooltip.nac.module.processor.immersion_fluid")
                            + ": "
                            + EnumChatFormatting.GRAY
                            + fluidTank.getFluid()
                                .getLocalizedName());
                } else {
                    t.addLine(EnumChatFormatting.BLUE + translateToLocal("GT5U.tooltip.nac.module.processor.empty"));
                }
                t.addLine(
                    EnumChatFormatting.GREEN + translateToLocal("GT5U.tooltip.nac.module.processor.impurity")
                        + ": "
                        + numberFormat.format(impurity.getDoubleValue() * 100)
                        + "%");
            })
            .background(IDrawable.EMPTY)
            .pos(151, 0);

        return new Row().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
            .paddingTop(4)
            .paddingBottom(4)
            .paddingLeft(4)
            .paddingRight(0)
            .childPadding(3)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .child(
                createTerminalTextWidget(syncManager, panel)
                    .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                    .collapseDisabledChild())
            .childIf(
                multiblock.supportsTerminalRightCornerColumn(),
                () -> createTerminalRightCornerColumn(panel, syncManager))
            .childIf(
                multiblock.supportsTerminalLeftCornerColumn(),
                () -> createTerminalLeftCornerColumn(panel, syncManager))
            .child(fluidSlot)
            .child(impurityFluidSlot);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {

        return super.createLeftPanelGapRow(parent, syncManager).child(
            new ButtonWidget<>().size(18, 18)
                .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/arrow_green_down"))
                .playClickSound(true)
                .onMousePressed(d -> {
                    syncManager.callSyncedAction("flushTank", $ -> {});
                    return false;
                })
                .tooltipBuilder(t -> t.addLine(translateToLocal("GT5U.tooltip.nac.module.processor.flush_tank")))
                .tooltipShowUpTimer(TOOLTIP_DELAY))
            .child(createAutomationButton(syncManager, parent));
    }

    @NotNull
    private ModularPanel openAutoPanel(ModularPanel parent, PanelSyncManager syncManager) {
        return new ModularPanel("automationPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(193, 134)
            .widgetTheme("backgroundPopup")
            .child(createAutomationPanelColumn(syncManager));
    }

    private Flow createAutomationPanelColumn(PanelSyncManager syncManager) {

        IntSyncValue automationPercentage = syncManager.findSyncHandler("automationPercentage", IntSyncValue.class);

        return new Column().coverChildrenHeight()
            .align(Alignment.CENTER)
            .childPadding(4)
            .child(new TextWidget<>(translateToLocal("GT5U.gui.text.nac.module.processor.flush_tank_auto") + ":"))
            .child(
                new TextFieldWidget().setNumbers(1, 100)
                    .setTextAlignment(Alignment.CENTER)
                    .setDefaultNumber(100)
                    .value(automationPercentage)
                    .size(60, 18))
            .child(new TextWidget<>(translateToLocal("GT5U.gui.text.nac.module.processor.impurity")));

    }

    protected IWidget createAutomationButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler automationPanel = syncManager
            .syncedPanel("automationPanel", true, (p_syncManager, syncHandler) -> openAutoPanel(parent, syncManager));
        return new ButtonWidget<>().size(18, 18)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/cyclic"))
            .onMousePressed(d -> {
                if (!automationPanel.isPanelOpen()) {
                    automationPanel.openPanel();
                } else {
                    automationPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(translateToLocal("GT5U.tooltip.nac.module.processor.edit_flush_tank")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @Override
    protected int getTerminalWidgetWidth() {
        return 148;
    }

}
