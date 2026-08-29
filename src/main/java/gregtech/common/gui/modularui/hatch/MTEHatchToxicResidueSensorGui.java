package gregtech.common.gui.modularui.hatch;

import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CYCLIC;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchRedstoneBaseGui;
import gregtech.common.tileentities.machines.multi.MTEHatchToxicResidueSensor;

public class MTEHatchToxicResidueSensorGui extends MTEHatchRedstoneBaseGui<MTEHatchToxicResidueSensor> {

    public MTEHatchToxicResidueSensorGui(MTEHatchToxicResidueSensor mteHatchToxicResidueSensor) {
        super(mteHatchToxicResidueSensor);
    }

    @Override
    protected Flow createContentColumn() {
        return super.createContentColumn().child(createThresholdTypeButtonRow())
            .child(
                IKey.lang("GT5U.gui.text.toxic_residue_sensor")
                    .asWidget())
            .child(
                new TextFieldWidget().value(new IntSyncValue(machine::getThreshold, machine::setThreshold).allowC2S())
                    .numbersInt(
                        0,
                        machine.getThresholdType()
                            .getMaxCapacity())
                    .size(77, 12)
                    .setFocusOnGuiOpen(true));
    }

    public Flow createThresholdTypeButtonRow() {
        EnumSyncValue<MTEHatchToxicResidueSensor.ThresholdType, ?> thresholdTypeSyncer = new EnumSyncValue<>(
            MTEHatchToxicResidueSensor.ThresholdType.class,
            machine::getThresholdType,
            machine::setThresholdType).allowC2S();

        return Flow.row()
            .child(
                addToxicResidueTypeTooltips(
                    new CycleButtonWidget().overlay(OVERLAY_BUTTON_CYCLIC)
                        .value(thresholdTypeSyncer)
                        .size(16)))
            .child(
                IKey.dynamic(() -> String.valueOf(thresholdTypeSyncer.getValue()))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }

    private CycleButtonWidget addToxicResidueTypeTooltips(CycleButtonWidget button) {
        for (MTEHatchToxicResidueSensor.ThresholdType toxicResidueType : MTEHatchToxicResidueSensor.ThresholdType
            .values()) {
            button.addTooltip(toxicResidueType.ordinal(), toxicResidueType.getTooltip());
        }
        return button;
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
