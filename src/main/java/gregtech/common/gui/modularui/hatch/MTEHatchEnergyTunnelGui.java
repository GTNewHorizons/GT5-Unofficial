package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

public class MTEHatchEnergyTunnelGui extends MTEHatchBaseGui<MTEHatchEnergyTunnel> {

    public MTEHatchEnergyTunnelGui(MTEHatchEnergyTunnel hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentHolderRow(panel, syncManager).child(
            new Column().mainAxisAlignment(Alignment.MainAxis.CENTER)
                .align(Alignment.CENTER)
                .child(
                    IKey.str(StatCollector.translateToLocal("GT5U.machines.laser_hatch.amperage"))
                        .asWidget())
                .child(new TextFieldWidget().value(new IntSyncValue(hatch::getAmperes, amps -> {
                    // If max amperage gets changed, update the multi structure
                    IGregTechTileEntity igte = hatch.getBaseMetaTileEntity();

                    if (igte.isServerSide()) {
                        GregTechAPI
                            .causeMachineUpdate(igte.getWorld(), igte.getXCoord(), igte.getYCoord(), igte.getZCoord());
                    }

                    hatch.setAmperes(amps);
                }))
                    .setNumbers(0, hatch.maxAmperes)
                    .setFormatAsInteger(true)
                    .setDefaultNumber(0)));
    }
}
