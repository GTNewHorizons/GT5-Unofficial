package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.WirelessEnergyHatchGuiHelper;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class MTEHatchDynamoTunnelGui extends MTEHatchBaseGui<MTEHatchDynamoTunnel> {

    public MTEHatchDynamoTunnelGui(MTEHatchDynamoTunnel hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            WirelessEnergyHatchGuiHelper.createMainColumn(
                new IntSyncValue(machine::getAmperes, machine::setAmperes).allowC2S(),
                machine.maxAmperes));
    }
}
