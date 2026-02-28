package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

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
}
