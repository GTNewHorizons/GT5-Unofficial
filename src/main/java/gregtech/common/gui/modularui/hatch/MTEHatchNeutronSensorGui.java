package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTEHatchNeutronSensor;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.hatch.base.MTEHatchRedstoneBaseGui;

public class MTEHatchNeutronSensorGui extends MTEHatchRedstoneBaseGui<MTEHatchNeutronSensor> {

    public MTEHatchNeutronSensorGui(MTEHatchNeutronSensor sensor) {
        super(sensor);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow col = Flow.column()
            .child(createThresholdFieldRow())
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2);
        return super.createContentSection(panel, syncManager).child(col);
    }

    public Flow createThresholdFieldRow() {
        return Flow.row()
            .child(
                new TextFieldWidget().formatAsInteger(true)
                    .numbersInt(0, 1200000000)
                    .size(77, 12)
                    .value(new IntSyncValue(machine::getThreshold, machine::setThreshold).allowC2S())
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("gui.NeutronSensor.4")
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }
}
