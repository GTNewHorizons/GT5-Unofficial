package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

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
        syncManager.syncValue("solutionAmount", new IntSyncValue(multiblock::getSolutionAmount));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue machineFormed = syncManager.findSyncHandler("machine", BooleanSyncValue.class);
        IntSyncValue chance = syncManager.findSyncHandler("successChance", IntSyncValue.class);
        IntSyncValue solutionAmount = syncManager.findSyncHandler("solutionAmount", IntSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> machineFormed.getBoolValue()
                            ? "Solution tank: " + solutionAmount.getIntValue() + "/1024 L"
                            : "")).color(Color.WHITE.main))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> machineFormed.getBoolValue() ? "Estimated success chance: " + chance.getIntValue() + "%"
                            : "")).color(Color.WHITE.main));
    }
}
