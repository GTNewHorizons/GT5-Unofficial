package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEElectrochemicalCutter;

public class MTEElectrochemicalCutterGui extends MTEMultiBlockBaseGui<MTEElectrochemicalCutter> {

    public MTEElectrochemicalCutterGui(MTEElectrochemicalCutter machine) {
        super(machine);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("successChance", new IntSyncValue(multiblock::getSuccessChancePercent));
        syncManager.syncValue("solutionAmount", new IntSyncValue(multiblock::getSolutionAmountKiloLitres));
        syncManager.syncValue("solutionCapacity", new IntSyncValue(multiblock::getSolutionCapacityKiloLitres));
        syncManager.syncValue(
            "dumpSolutionButton",
            new BooleanSyncValue(
                multiblock::isSolutionDumpButtonActive,
                value -> { if (value) multiblock.dumpSolution(); }).allowC2S());
    }

    @Override
    protected Flow createButtonColumn(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createButtonColumn(parent, syncManager).child(
            new ToggleButton().syncHandler("dumpSolutionButton")
                .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_TRASH_CAN)
                .tooltip(tooltip -> tooltip.addLine(IKey.lang("GT5U.gui.button.dump")))
                .size(18, 18));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue machineFormed = syncManager.findSyncHandler("machine", BooleanSyncValue.class);
        IntSyncValue chance = syncManager.findSyncHandler("successChance", IntSyncValue.class);
        IntSyncValue solutionAmount = syncManager.findSyncHandler("solutionAmount", IntSyncValue.class);
        IntSyncValue solutionCapacity = syncManager.findSyncHandler("solutionCapacity", IntSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> machineFormed.getBoolValue() ? StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.solution_tank_amount",
                            solutionAmount.getIntValue(),
                            solutionCapacity.getIntValue()) : "")).color(Color.WHITE.main))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> machineFormed.getBoolValue() ? StatCollector
                            .translateToLocalFormatted("GT5U.gui.text.estimated_success_chance", chance.getIntValue())
                            : "")).color(Color.WHITE.main));
    }
}
