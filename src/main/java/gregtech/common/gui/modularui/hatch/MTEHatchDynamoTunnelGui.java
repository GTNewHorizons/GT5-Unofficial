package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.modularui2.WirelessEnergyHatchGui;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class MTEHatchDynamoTunnelGui extends MTEHatchBaseGui<MTEHatchDynamoTunnel> {

    public MTEHatchDynamoTunnelGui(MTEHatchDynamoTunnel hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            WirelessEnergyHatchGui
                .createMainColumn(new IntSyncValue(machine::getAmperes, machine::setAmperes), machine.maxAmperes));
    }
}
