package gregtech.common.gui.modularui.singleblock;

import java.util.stream.IntStream;

import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.ShortSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gregtech.common.tileentities.debug.MTEDebugStructureWriter;

public class MTEDebugStructureWriterGui extends MTETieredMachineBlockBaseGui<MTEDebugStructureWriter> {

    public MTEDebugStructureWriterGui(MTEDebugStructureWriter machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ShortSyncValue[] numberSyncers = IntStream.range(0, 6)
            .mapToObj(
                index -> new ShortSyncValue(() -> machine.getNumber(index), number -> machine.setNumber(index, number)))
            .toArray(ShortSyncValue[]::new);
        InteractionSyncHandler printHandler = new InteractionSyncHandler().setOnMousePressed(
            mouseData -> { if (baseMetaTileEntity.isServerSide()) machine.printStructure(MCHelper.getPlayer()); });
        BooleanSyncValue transposeSyncer = new BooleanSyncValue(machine::getTranspose, machine::setTranspose);
        BooleanSyncValue showHighlightBoxSyncer = new BooleanSyncValue(
            machine::getShowHighlightBox,
            machine::setShowHighlightBox);

        Flow mainRow = Flow.row()
            .sizeRel(1)
            .padding(4)
            .childPadding(10)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.child(getButtonColumn(printHandler, transposeSyncer, showHighlightBoxSyncer));
        mainRow.child(getTextColumn(true, numberSyncers));
        mainRow.child(getTextColumn(false, numberSyncers));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private Flow getTextColumn(boolean isOriginColumn, ShortSyncValue[] numberSyncers) {
        Flow textColumn = Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START);

        // header text of this column
        textColumn.child(
            IKey.lang(
                isOriginColumn ? "GT5U.machines.debugstructurewriter.gui.origin"
                    : "GT5U.machines.debugstructurewriter.gui.size")
                .asWidget()
                .marginBottom(3));

        int offset = isOriginColumn ? 0 : 3;
        for (int i = 0; i < 3; i++) {
            final int index = offset + i; // needed for the lambda
            String axis = switch (i) {
                case (1) -> "B";
                case (2) -> "C";
                default -> "A";
            };

            Flow coordinateRow = Flow.row()
                .coverChildren();

            // number input field for this coordinate
            coordinateRow.child(
                new TextFieldWidget().value(numberSyncers[index])
                    .setTextAlignment(Alignment.CenterRight)
                    .size(45, 14)
                    .marginRight(2)
                    .setMaxLength(6)
                    .setFormatAsInteger(true)
                    .setNumbers(isOriginColumn ? Short.MIN_VALUE : 0, Short.MAX_VALUE));

            // text widget for this coordinate
            coordinateRow.child(
                IKey.str(axis)
                    .asWidget());

            textColumn.child(coordinateRow);
        }

        return textColumn;
    }

    private Flow getButtonColumn(InteractionSyncHandler printHandler, BooleanSyncValue transposeSyncer,
        BooleanSyncValue showHighlightBoxSyncer) {
        Flow buttonColumn = Flow.col()
            .coverChildren()
            .childPadding(3);

        // button for printing
        buttonColumn.child(
            new com.cleanroommc.modularui.widgets.ButtonWidget<>().syncHandler(printHandler)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_PRINT)
                .tooltip(t -> t.addLine(IKey.lang("GT5U.machines.debugstructurewriter.gui.print.tooltip"))));

        // button for toggling transpose
        buttonColumn.child(
            new ToggleButton().value(transposeSyncer)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_TRANSPOSE)
                .tooltip(t -> t.addLine(IKey.lang("GT5U.machines.debugstructurewriter.gui.transpose.tooltip"))));

        // button for toggling the bounding box
        buttonColumn.child(
            new ToggleButton().value(showHighlightBoxSyncer)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_BOUNDING_BOX)
                .tooltip(t -> t.addLine(IKey.lang("GT5U.machines.debugstructurewriter.gui.highlight.tooltip"))));

        return buttonColumn;
    }

    @Override
    protected boolean supportsPowerSwitch() {
        return false;
    }
}
