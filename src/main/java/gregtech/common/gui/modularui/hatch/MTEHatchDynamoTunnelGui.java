package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import net.minecraft.util.StatCollector;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class MTEHatchDynamoTunnelGui extends MTEHatchBaseGui<MTEHatchDynamoMulti> {

    public MTEHatchDynamoTunnelGui(MTEHatchDynamoMulti hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentHolderRow(panel, syncManager)
            .child(new Column()
                .mainAxisAlignment(Alignment.MainAxis.CENTER)
                .align(Alignment.CENTER)
                .child(IKey.str(StatCollector
                    .translateToLocal("GT5U.machines.laser_hatch.amperage")).asWidget())
                .child(new TextFieldWidget()
                    .value( new IntSyncValue(hatch::getAmperes,  hatch::setAmperes))
                )
            );
    }
}
