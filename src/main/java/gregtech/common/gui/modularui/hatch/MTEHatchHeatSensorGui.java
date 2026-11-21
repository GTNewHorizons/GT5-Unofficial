package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchRedstoneBaseGui;
import gregtech.common.tileentities.machines.MTEHatchHeatSensor;

public class MTEHatchHeatSensorGui extends MTEHatchRedstoneBaseGui<MTEHatchHeatSensor> {

    public MTEHatchHeatSensorGui(MTEHatchHeatSensor sensor) {
        super(sensor);
    }

    @Override
    protected Flow createContentColumn() {
        return super.createContentColumn().child(createThresholdFieldRow())
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2);
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
