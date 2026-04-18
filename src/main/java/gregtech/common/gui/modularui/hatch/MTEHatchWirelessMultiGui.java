package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.GregTechAPI;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessMulti;

public class MTEHatchWirelessMultiGui extends MTEHatchBaseGui<MTEHatchWirelessMulti> {

    public MTEHatchWirelessMultiGui(MTEHatchWirelessMulti hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue amperageSyncer = new IntSyncValue(hatch::getAmperes, amps -> {
            // If max amperage gets changed, update the multi structure
            if (baseMetaTileEntity.isServerSide()) GregTechAPI.causeMachineUpdate(
                baseMetaTileEntity.getWorld(),
                baseMetaTileEntity.getXCoord(),
                baseMetaTileEntity.getYCoord(),
                baseMetaTileEntity.getZCoord());

            hatch.setAmperes(amps);
        });

        Flow mainColumn = Flow.column()
            .coverChildren()
            .center()
            .childPadding(4);

        // amperage label
        mainColumn.child(
            new TextWidget<>(translateToLocal("GT5U.machines.laser_hatch.amperage")).textAlign(Alignment.Center));

        // amperage text field
        mainColumn.child(
            new TextFieldWidget().value(amperageSyncer)
                .setNumbers(1, hatch.maxAmperes)
                .setFormatAsInteger(true)
                .setTextAlignment(Alignment.Center)
                .setMaxLength((int) Math.ceil(Math.log10(hatch.maxAmperes)))
                .setScrollValues(1, 4, 64)
                .setTextAlignment(Alignment.Center)
                .width(70));

        return super.createContentSection(panel, syncManager).child(mainColumn);
    }
}
