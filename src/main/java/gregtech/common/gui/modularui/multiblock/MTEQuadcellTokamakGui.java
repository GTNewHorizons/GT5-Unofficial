package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEQuadcellTokamak;
import gregtech.common.tileentities.machines.multi.MTEQuadcellTokamak.PlasmaType;

public class MTEQuadcellTokamakGui extends MTEMultiBlockBaseGui<MTEQuadcellTokamak> {

    public MTEQuadcellTokamakGui(MTEQuadcellTokamak multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("ForceDR", new IntSyncValue(() -> multiblock.FORCE_CURRENT_DR, val -> {
            multiblock.FORCE_CURRENT_DR = val;
            multiblock.resetBoosts();
        }).allowC2S());

        syncManager.syncValue("RuniteDR", new IntSyncValue(() -> multiblock.RUNITE_CURRENT_DR, val -> {
            multiblock.RUNITE_CURRENT_DR = val;
            multiblock.resetBoosts();
        }).allowC2S());
        syncManager
            .syncValue("CelestialTungstenDR", new IntSyncValue(() -> multiblock.CELESTIAL_TUNGSTEN_CURRENT_DR, val -> {
                multiblock.CELESTIAL_TUNGSTEN_CURRENT_DR = val;
                multiblock.resetBoosts();
            }).allowC2S());
        syncManager.syncValue("OrikalkumDR", new IntSyncValue(() -> multiblock.ORIKALKUM_CURRENT_DR, val -> {
            multiblock.ORIKALKUM_CURRENT_DR = val;
            multiblock.resetBoosts();
        }).allowC2S());
    }

    @Override
    protected int getTerminalRowHeight() {
        return super.getTerminalRowHeight() + 20;
    }

    @Override
    protected int getTextBoxToInventoryGap() {
        return 2;
    }

    @Override
    protected int getBasePanelHeight() {
        return 203;
    }

    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .size(getTerminalRowWidth(), getTerminalRowHeight())
            .marginBottom(2)
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .paddingTop(4)
                    .paddingBottom(4)
                    .paddingLeft(4)
                    .paddingRight(0)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel).collapseDisabledChild()
                            .setEnabledIf($ -> !multiblock.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 4))
                    .child(
                        createConfigurationTerminalTextWidget(syncManager).collapseDisabledChild()
                            .setEnabledIf($ -> multiblock.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8))
                    .childIf(
                        multiblock.supportsTerminalRightCornerColumn(),
                        () -> createTerminalRightCornerColumn(panel, syncManager)));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createButtonColumn(parent, syncManager).child(createConfigurationButton());
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createTerminalRightCornerColumn(panel, syncManager).setEnabledIf($ -> !multiblock.terminalSwitch);
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .size(0);
    }

    protected Flow createConfigurationTerminalTextWidget(PanelSyncManager syncManager) {
        return Flow.column()
            .horizontalCenter()
            .coverChildren()
            .child(createConfigurationRow(PlasmaType.FORCE, syncManager))
            .child(createConfigurationRow(PlasmaType.RUNITE, syncManager))
            .child(createConfigurationRow(PlasmaType.CELESTIAL, syncManager))
            .child(createConfigurationRow(PlasmaType.ORIKALKUM, syncManager));
    }

    protected Flow createConfigurationRow(MTEQuadcellTokamak.PlasmaType plasma, PanelSyncManager syncManager) {
        IntSyncValue syncValue = plasma.getSyncValue(syncManager);

        return Flow.column()
            .size(150, 16 + 1 + 9)
            .child(
                IKey.str(plasma.getLocalName())
                    .color(plasma.getRGB())
                    .asWidget()
                    .marginBottom(1))
            .child(
                Flow.row()
                    .height(16)
                    .widthRel(1)
                    .child(
                        new FluidDisplayWidget().background(IDrawable.EMPTY)
                            .hoverBackground(IDrawable.EMPTY)
                            .value(plasma.getFluid())
                            .displayAmount(false)
                            .size(16)
                            .marginRight(2))
                    .child(
                        new ParentWidget<>().size(82, 10)
                            .child(
                                GTGuiTextures.SLOT_ITEM_DARK.asWidget()
                                    .size(82, 10))
                            .child(
                                new SliderWidget().size(80, 8)
                                    .margin(1, 1)
                                    .verticalCenter()
                                    .bounds(0, plasma.getMaxDR())
                                    .sliderSize(2, 8)
                                    .sliderTexture(new Rectangle().color(Color.WHITE.main))
                                    .background(IDrawable.EMPTY)
                                    .overlay(new Rectangle().horizontalGradient(Color.GREY.main, plasma.getRGB()))
                                    .value(
                                        new DoubleValue.Dynamic(syncValue::getDoubleValue, syncValue::setDoubleValue))))
                    .child(
                        new TextFieldWidget().size(48, 16)
                            .formatAsInteger(true)
                            .numbersInt(0, plasma.getMaxDR())
                            .value(new IntValue.Dynamic(syncValue::getIntValue, syncValue::setIntValue))
                            .setTextAlignment(Alignment.CENTER)
                            .setTextColor(plasma.getRGB())
                            .tooltip(t -> t.addLine("todo"))
                            .marginLeft(2)));
    }

    protected IWidget createConfigurationButton() {
        return new ButtonWidget<>().size(18)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
                multiblock.terminalSwitch = !multiblock.terminalSwitch;
                return true;
            })
            .tooltipBuilder(t -> t.addLine("Configure plasmas"))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

}
