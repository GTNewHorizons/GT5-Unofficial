package gregtech.common.gui.modularui.singleblock.kinetic;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.common.gui.modularui.singleblock.base.MTEKineticRotorBaseGui;
import gregtech.common.tileentities.machines.basic.MTEKineticWindGenerator;

public class MTEKineticWindGeneratorGui extends MTEKineticRotorBaseGui<MTEKineticWindGenerator> {

    public MTEKineticWindGeneratorGui(MTEKineticWindGenerator machine) {
        super(machine);
    }

    @Override
    protected ListWidget<IWidget, ?> getKineticInfoText(PanelSyncManager syncManager) {
        DoubleSyncValue windSync = syncManager.findSyncHandler("wind", DoubleSyncValue.class);
        return super.getKineticInfoText(syncManager).child(IKey.dynamic(() -> {
            double wind = windSync.getDoubleValue();
            return String
                .format(StatCollector.translateToLocal("GT5U.gui.text.wind_speed"), String.format("%.2f", wind));
        })
            .color(Color.LIGHT_BLUE.main)
            .asWidget()
            .fullWidth());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        DoubleSyncValue windSyncer = new DoubleSyncValue(machine::getWind);
        syncManager.syncValue("wind", windSyncer);
    }
}
