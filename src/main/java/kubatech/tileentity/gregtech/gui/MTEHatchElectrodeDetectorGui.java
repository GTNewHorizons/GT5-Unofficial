package kubatech.tileentity.gregtech.gui;

import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CYCLIC;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchRedstoneBaseGui;
import kubatech.tileentity.gregtech.hatch.MTEHatchElectrodeDetector;

public class MTEHatchElectrodeDetectorGui extends MTEHatchRedstoneBaseGui<MTEHatchElectrodeDetector> {

    public MTEHatchElectrodeDetectorGui(MTEHatchElectrodeDetector detector) {
        super(detector);
    }

    @Override
    protected Flow createContentColumn() {
        return super.createContentColumn().child(createThresholdTypeButtonRow())
            .child(createThresholdFieldRow())
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2);
    }

    public Flow createThresholdTypeButtonRow() {
        EnumSyncValue<MTEHatchElectrodeDetector.ThresholdType, ?> thresholdTypeSyncer = new EnumSyncValue<>(
            MTEHatchElectrodeDetector.ThresholdType.class,
            machine::getThresholdType,
            machine::setThresholdType).allowC2S();

        return Flow.row()
            .child(
                new CycleButtonWidget().overlay(OVERLAY_BUTTON_CYCLIC)
                    .value(thresholdTypeSyncer)
                    .size(16))
            .child(
                IKey.dynamic(() -> String.valueOf(thresholdTypeSyncer.getValue()))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }

    public Flow createThresholdFieldRow() {
        return Flow.row()
            .child(
                new TextFieldWidget().formatAsInteger(true)
                    .numbersInt(
                        () -> 0,
                        () -> machine.getThresholdType()
                            .getMaxCapacity())
                    .size(77, 12)
                    .value(new IntSyncValue(machine::getThreshold, machine::setThreshold).allowC2S())
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("kubatech.gui.text.electrode_detector")
                    .asWidget()
                    .maxWidth(70))
            .coverChildren()
            .childPadding(2);
    }
}
