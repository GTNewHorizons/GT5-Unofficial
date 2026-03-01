package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELargeNeutralizationEngine;

public class MTELargeNeutralizationEngineGui extends MTEMultiBlockBaseGui<MTELargeNeutralizationEngine> {

    public MTELargeNeutralizationEngineGui(MTELargeNeutralizationEngine multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> terminalText = super.createTerminalTextWidget(syncManager, parent);
        BooleanSyncValue checkMachineSyncer = new BooleanSyncValue(() -> multiblock.mMachine);
        syncManager.syncValue("checkMachine", checkMachineSyncer);
        if (!checkMachineSyncer.getBoolValue()) return terminalText;

        IntSyncValue toxicResidueSyncer = new IntSyncValue(() -> multiblock.toxicResidue);
        IntSyncValue residueCapacitySyncer = new IntSyncValue(multiblock::getResidueCapacity);
        FloatSyncValue residuePercentageSyncer = new FloatSyncValue(multiblock::getResidueUsedPercentage);
        syncManager.syncValue("toxicResidue", toxicResidueSyncer);
        syncManager.syncValue("residueCapacitySyncer", residueCapacitySyncer);
        syncManager.syncValue("residuePercentageSyncer", residuePercentageSyncer);
        terminalText.child(
            IKey.dynamic(
                () -> StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.toxic_residue",
                    toxicResidueSyncer.getStringValue(),
                    residueCapacitySyncer.getStringValue(),
                    residuePercentageSyncer.getStringValue()))
                .asWidget());
        IntSyncValue residueDecaySyncer = new IntSyncValue(() -> multiblock.residueDecay);
        IntSyncValue residueIncreaseSyncer = new IntSyncValue(() -> multiblock.residueIncrease);
        IntSyncValue netResidueSyncer = new IntSyncValue(multiblock::getNetResidue);
        syncManager.syncValue("residueDecay", residueDecaySyncer);
        syncManager.syncValue("residueIncrease", residueIncreaseSyncer);
        syncManager.syncValue("netResidue", netResidueSyncer);
        terminalText.child(
            IKey.dynamic(
                () -> StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.residue_change",
                    residueIncreaseSyncer.getStringValue(),
                    residueDecaySyncer.getStringValue(),
                    netResidueSyncer.getStringValue()))
                .asWidget());
        return terminalText;
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        Flow rightPanelGapRow = super.createRightPanelGapRow(parent, syncManager);
        rightPanelGapRow.child(createFluidRegulationPanelButton(syncManager, parent));
        return rightPanelGapRow;
    }

    protected IWidget createFluidRegulationPanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler fluidRegulationPanel = syncManager.syncedPanel(
            "fluidRegulationPanel",
            true,
            (p_syncManager, syncHandler) -> openFluidRegulationControlPanel(p_syncManager, parent));
        return new ButtonWidget<>().size(18, 18)
            .marginLeft(4)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/fluid_regulation_panel"))
            .onMousePressed(d -> {
                if (!fluidRegulationPanel.isPanelOpen()) {
                    fluidRegulationPanel.openPanel();
                } else {
                    fluidRegulationPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.fluid_regulation_panel")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openFluidRegulationControlPanel(PanelSyncManager syncManager, ModularPanel parent) {
        return new ModularPanel("fluidRegulationPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 50)
            .child(
                new Column().sizeRel(1)
                    .padding(3)
                    .child(
                        IKey.lang("GT5U.gui.button.max_fluid")
                            .asWidget()
                            .marginBottom(4))
                    .child(makeMaxFluidConfigurator(syncManager)));

    }

    private IWidget makeMaxFluidConfigurator(PanelSyncManager syncManager) {
        IntSyncValue maxFluidUseSyncer = new IntSyncValue(multiblock::getMaxFluidUse, multiblock::setMaxFluidUse);
        return new Row().widthRel(1)
            .marginBottom(4)
            .height(18)
            .paddingLeft(3)
            .paddingRight(3)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .child(makeMaxFluidConfiguratorTextFieldWidget(maxFluidUseSyncer));
    }

    private IWidget makeMaxFluidConfiguratorTextFieldWidget(IntSyncValue maxFluidUseSyncer) {
        return new TextFieldWidget().value(maxFluidUseSyncer)
            .setTextAlignment(Alignment.Center)
            .setNumbers(0, Integer.MAX_VALUE)
            .size(70, 14)
            .marginBottom(4)
            .marginRight(16);
    }
}
