package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELaserCutter;

public class MTELaserCutterGui extends MTEMultiBlockBaseGui<MTELaserCutter> {

    public MTELaserCutterGui(MTELaserCutter machine) {
        super(machine);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("coolingFluid", new StringSyncValue(multiblock::getCoolingFluidName));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue machineFormed = syncManager.findSyncHandler("machine", BooleanSyncValue.class);
        StringSyncValue coolingFluid = syncManager.findSyncHandler("coolingFluid", StringSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent).child(
            new TextWidget<>(
                IKey.dynamic(
                    () -> machineFormed.getBoolValue()
                        ? StatCollector
                            .translateToLocalFormatted("GT5U.gui.text.cooling_fluid", coolingFluid.getValue())
                        : "")).color(Color.WHITE.main));
    }
}
