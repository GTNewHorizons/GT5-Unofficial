package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gregtech.common.tileentities.machines.basic.MTETeleporter;

public class MTETeleporterGui extends MTETieredMachineBlockBaseGui<MTETeleporter> {

    public MTETeleporterGui(MTETeleporter machine) {
        super(machine);
    }

    @Override
    protected boolean supportsPowerSwitch() {
        return false;
    }

    @Override
    protected boolean supportsMuffler() {
        return false;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue xSyncer = new IntSyncValue(machine::getTargetX, machine::setTargetX);
        IntSyncValue ySyncer = new IntSyncValue(machine::getTargetY, machine::setTargetY);
        IntSyncValue zSyncer = new IntSyncValue(machine::getTargetZ, machine::setTargetZ);
        IntSyncValue dimSyncer = new IntSyncValue(machine::getTargetD, machine::setTargetD);

        Flow mainColumn = Flow.column()
            .coverChildren()
            .childPadding(2)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .marginTop(4)
            .marginLeft(4);

        mainColumn.child(createInputRow("x", xSyncer, false));
        mainColumn.child(createInputRow("y", ySyncer, false));
        mainColumn.child(createInputRow("z", zSyncer, false));
        mainColumn.child(createInputRow("dim", dimSyncer, true));

        return super.createContentSection(panel, syncManager).child(mainColumn);
    }

    private Flow createInputRow(String key, IntSyncValue value, boolean isDimRow) {
        Flow row = Flow.row()
            .coverChildren()
            .childPadding(3)
            .collapseDisabledChild();

        // label
        row.child(
            IKey.lang("GT5U.gui.text." + key)
                .asWidget()
                .width(15)
                .textAlign(Alignment.END));

        // input field
        row.child(
            new TextFieldWidget().value(value)
                .setFormatAsInteger(true)
                .setNumbers()
                .setMaxLength(11)
                .size(80, 12));

        // dim validity marker
        row.childIf(
            isDimRow && machine.hasDimensionalTeleportCapability(),
            () -> GTGuiTextures.OVERLAY_BUTTON_CHECKMARK.asWidget()
                .setEnabledIf($ -> GTUtility.isRealDimension(value.getIntValue()))
                .tooltip(t -> t.addLine(GTUtility.translate("GT5U.gui.text.dim.valid")))
                .decoration());
        row.childIf(
            isDimRow && machine.hasDimensionalTeleportCapability(),
            () -> GTGuiTextures.OVERLAY_BUTTON_CROSS.asWidget()
                .setEnabledIf($ -> !GTUtility.isRealDimension(value.getIntValue()))
                .tooltip(t -> t.addLine(GTUtility.translate("GT5U.gui.text.dim.invalid")))
                .decoration());

        return row;
    }
}
