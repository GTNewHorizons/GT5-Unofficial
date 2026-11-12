package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.GregTechAPI;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.WirelessEnergyHatchGuiHelper;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessMulti;

public class MTEHatchWirelessMultiGui extends MTEHatchBaseGui<MTEHatchWirelessMulti> {

    public MTEHatchWirelessMultiGui(MTEHatchWirelessMulti hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager)
            .child(WirelessEnergyHatchGuiHelper.createMainColumn(new IntSyncValue(machine::getAmperes, amps -> {
                machine.setAmperes(amps);

                // If max amperage gets changed, update the multi structure
                if (baseMetaTileEntity.isServerSide()) GregTechAPI.causeMachineUpdate(
                    baseMetaTileEntity.getWorld(),
                    baseMetaTileEntity.getXCoord(),
                    baseMetaTileEntity.getYCoord(),
                    baseMetaTileEntity.getZCoord());
            }).allowC2S(), machine.maxAmperes));
    }
}
