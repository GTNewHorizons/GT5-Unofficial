package kubatech.tileentity.gregtech.gui;

import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CYCLIC;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import kubatech.tileentity.gregtech.hatch.MTEElectrodeDetectorHatch;

public class MTEElectrodeDetectorHatchGui extends MTEHatchBaseGui<MTEElectrodeDetectorHatch> {

    public MTEElectrodeDetectorHatchGui(MTEElectrodeDetectorHatch detector) {
        super(detector);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow col = Flow.column()
            .child(
                CommonWidgets
                    .createInvertButtonRow(new BooleanSyncValue(machine::isInverted, machine::setInverted).allowC2S()))
            .child(createThresholdTypeButtonRow())
            .child(createThresholdFieldRow())
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2);
        return super.createContentSection(panel, syncManager).child(col);
    }

    public Flow createThresholdTypeButtonRow() {
        EnumSyncValue<MTEElectrodeDetectorHatch.ThresholdType, ?> thresholdTypeSyncer = new EnumSyncValue<>(
            MTEElectrodeDetectorHatch.ThresholdType.class,
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
                    .numbersInt(() -> 0, () -> machine.getThresholdType()
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
