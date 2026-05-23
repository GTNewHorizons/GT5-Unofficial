package kubatech.tileentity.gregtech.gui;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
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
            .child(createThresholdFieldRow())
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2);
        return super.createContentSection(panel, syncManager).child(col);
    }

    public Flow createThresholdFieldRow() {
        return Flow.row()
            .child(
                new TextFieldWidget().setFormatAsInteger(true)
                    .setNumbers(0, 10000)
                    .size(77, 12)
                    .value(new IntSyncValue(machine::getThreshold, machine::setThreshold))
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("kubatech.gui.text.electrode_detector")
                    .asWidget()
                    .maxWidth(70))
            .coverChildren()
            .childPadding(2);
    }
}
