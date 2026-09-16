package gregtech.common.gui.modularui.multiblock;

import static gregtech.common.tileentities.machines.multi.MTEExothermicHearth.MODE_NO_PYRO;
import static gregtech.common.tileentities.machines.multi.MTEExothermicHearth.MODE_OVERDRIVE;
import static gregtech.common.tileentities.machines.multi.MTEExothermicHearth.MODE_PYRO;

import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEExothermicHearth;

public class MTEExothermicHearthGui extends MTEMultiBlockBaseGui<MTEExothermicHearth> {

    public MTEExothermicHearthGui(MTEExothermicHearth multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("mode", new IntSyncValue(() -> multiblock.heatMode, multiblock::setHeatMode).allowC2S());
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        IntSyncValue modeSync = syncManager.findSyncHandler("mode", IntSyncValue.class);
        return super.createRightPanelGapRow(parent, syncManager).child(
            new CycleButtonWidget().value(modeSync)
                .length(3)
                .tooltipDynamic(t -> {
                    int mode = modeSync.getIntValue();
                    String s = switch (mode) {
                        case MODE_NO_PYRO -> "No Pyrotheum";
                        case MODE_PYRO -> "Pyrotheum";
                        case MODE_OVERDRIVE -> "Overdrive";
                        default -> "How";
                    };
                    t.add(s);
                })
                .overlay(new DynamicDrawable(() -> {
                    int mode = modeSync.getIntValue();
                    return switch (mode) {
                        case MODE_NO_PYRO -> GTGuiTextures.OVERLAY_BUTTON_PYROTHEUM_OFF;
                        case MODE_PYRO -> GTGuiTextures.OVERLAY_BUTTON_PYROTHEUM_ON;
                        case MODE_OVERDRIVE -> GTGuiTextures.OVERLAY_BUTTON_ASSEMBLER_MODE;
                        default -> GTGuiTextures.OVERLAY_ALL_MAINTENANCE_ISSUES;
                    };
                })));
    }
}
