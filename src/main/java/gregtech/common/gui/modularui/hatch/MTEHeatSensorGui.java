package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.compressor.MTEHeatSensor;

public class MTEHeatSensorGui extends MTEHatchBaseGui<MTEHeatSensor> {

    public MTEHeatSensorGui(MTEHeatSensor sensor) {
        super(sensor);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow col = Flow.column()
            .child(
                CommonWidgets
                    .createInvertButtonRow(new BooleanSyncValue(machine::isInverted, machine::setInverted).allowC2S()))
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
                    .numbersInt(0, 100)
                    .size(77, 12)
                    .value(new DoubleSyncValue(machine::getThreshold, machine::setThreshold).allowC2S())
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("GT5U.gui.text.heat_sensor")
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }
}
