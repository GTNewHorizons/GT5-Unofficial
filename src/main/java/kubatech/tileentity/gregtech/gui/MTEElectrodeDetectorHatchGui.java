package kubatech.tileentity.gregtech.gui;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import kubatech.tileentity.gregtech.hatch.MTEElectrodeDetectorHatch;

public class MTEElectrodeDetectorHatchGui extends MTEHatchBaseGui<MTEElectrodeDetectorHatch> {

    public MTEElectrodeDetectorHatchGui(MTEElectrodeDetectorHatch detector) {
        super(detector);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow col = Flow.column()
            .child(createInvertButtonRow())
            .child(createThresholdFieldRow())
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2)
            .paddingTop(4)
            .paddingLeft(4);
        return super.createContentSection(panel, syncManager).child(col);
    }

    public Flow createInvertButtonRow() {
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(hatch::isInverted, hatch::setInverted);
        return Flow.row()
            .child(
                new ToggleButton().value(invertedSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                    .size(16, 16))
            .child(
                IKey.dynamic(
                    () -> invertedSyncer.getValue() ? translateToLocal("gt.interact.desc.inverted")
                        : translateToLocal("gt.interact.desc.normal"))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }

    public Flow createThresholdFieldRow() {
        return Flow.row()
            .child(
                new TextFieldWidget().setFormatAsInteger(true)
                    .setNumbers(0, 10000)
                    .size(77, 12)
                    .value(new IntSyncValue(hatch::getThreshold, hatch::setThreshold))
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("kubatech.gui.text.electrode_detector")
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }

}
