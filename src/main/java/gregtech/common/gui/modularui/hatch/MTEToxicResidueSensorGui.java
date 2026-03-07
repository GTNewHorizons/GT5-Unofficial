package gregtech.common.gui.modularui.hatch;

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
import gregtech.common.tileentities.machines.multi.MTEToxicResidueSensor;

public class MTEToxicResidueSensorGui extends MTEHatchBaseGui<MTEToxicResidueSensor> {

    public MTEToxicResidueSensorGui(MTEToxicResidueSensor mteToxicResidueSensor) {
        super(mteToxicResidueSensor);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow col = Flow.column()
            .child(createInvertButtonRow())
            .child(
                IKey.lang("GT5U.gui.text.toxic_residue_sensor")
                    .asWidget())
            .child(
                new TextFieldWidget().value(new IntSyncValue(hatch::getThreshold, hatch::setThreshold))
                    .setNumbers(0, 2500000)
                    .size(77, 12)
                    .setFocusOnGuiOpen(true))
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
}
