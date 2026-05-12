package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class MTEHatchDynamoTunnelGui extends MTEHatchBaseGui<MTEHatchDynamoTunnel> {

    public MTEHatchDynamoTunnelGui(MTEHatchDynamoTunnel hatch) {
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
                .value(new IntSyncValue(hatch::getAmperes, hatch::setAmperes))
                .setNumbers(0, hatch.maxAmperes)
                .setDefaultNumber(0)
                .setFormatAsInteger(true));

        return super.createContentSection(panel, syncManager).child(mainColumn);
    }
}
