package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.GregTechAPI;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

public class MTEHatchEnergyTunnelGui extends MTEHatchBaseGui<MTEHatchEnergyTunnel> {

    public MTEHatchEnergyTunnelGui(MTEHatchEnergyTunnel hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainColumn = Flow.column()
            .childPadding(1)
            .center()
            .coverChildren();

        mainColumn.child(
            IKey.str(StatCollector.translateToLocal("GT5U.machines.laser_hatch.amperage"))
                .asWidget());

        mainColumn.child(
            new TextFieldWidget().width(60)
                .value(new IntSyncValue(hatch::getAmperes, amps -> {
                    // If max amperage gets changed, update the multi structure
                    if (baseMetaTileEntity.isServerSide()) GregTechAPI.causeMachineUpdate(
                        baseMetaTileEntity.getWorld(),
                        baseMetaTileEntity.getXCoord(),
                        baseMetaTileEntity.getYCoord(),
                        baseMetaTileEntity.getZCoord());

                    hatch.setAmperes(amps);
                }))
                .setNumbers(0, hatch.maxAmperes)
                .setFormatAsInteger(true)
                .setDefaultNumber(0));

        return super.createContentSection(panel, syncManager).child(mainColumn);
    }
}
