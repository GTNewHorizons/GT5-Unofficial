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
                .child(
                    new TextFieldWidget().value(new IntSyncValue(hatch::getAmperes, hatch::setAmperes))
                        .setNumbers(0, hatch.maxAmperes)
                        .setFormatAsInteger(true)
                        .setDefaultNumber(0)));
    }
}
