package gregtech.common.gui.modularui.singleblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.clamp;

import java.util.stream.IntStream;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.ShortSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gregtech.common.tileentities.debug.MTEDebugStructureWriter;

public class MTEDebugStructureWriterGui extends MTETieredMachineBlockBaseGui<MTEDebugStructureWriter> {

    private static final short MIN_ORIGIN = Short.MIN_VALUE;
    private static final short MIN_SIZE = 1;
    private static final short MAX_ORIGIN = Short.MAX_VALUE;
    private static final short MAX_SIZE = Short.MAX_VALUE;

    public MTEDebugStructureWriterGui(MTEDebugStructureWriter machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ShortSyncValue[] numberSyncers = IntStream.range(0, 6)
            .mapToObj(
                index -> new ShortSyncValue(() -> machine.getNumber(index), number -> machine.setNumber(index, number)))
            .toArray(ShortSyncValue[]::new);

        Flow mainRow = Flow.row()
            .sizeRel(1)
            .paddingTop(4)
            .paddingLeft(4)
            .childPadding(5)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.child(createInputSection(true, numberSyncers));
        mainRow.child(createInputSection(false, numberSyncers));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private Flow createInputSection(boolean isOriginColumn, ShortSyncValue[] numberSyncers) {
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
                    .size(isOriginColumn ? 45 : 40, 14)
                    .marginRight(2)
                    .setMaxLength(isOriginColumn ? 6 : 5)
                    .setFormatAsInteger(true)
                    .setNumbers(isOriginColumn ? MIN_ORIGIN : MIN_SIZE, isOriginColumn ? MAX_ORIGIN : MAX_SIZE));

            // text widget for this coordinate
            coordinateRow.child(
                IKey.str(axis)
                    .asWidget());

            // adder button
            coordinateRow.child(
                new ButtonWidget<>()
                    .syncHandler(
                        new InteractionSyncHandler().setOnMousePressed(
                            t -> {
                                if (baseMetaTileEntity.isServerSide())
                                    onAddButtonPressed(t, isOriginColumn, numberSyncers[index]);
                            }))
                    .size(14)
                    .overlay(GuiTextures.ADD)
                    .tooltip(t -> createAddButtonTooltip(t, isOriginColumn, axis)));

            // multiplier button
            coordinateRow.child(
                new ButtonWidget<>()
                    .syncHandler(
                        new InteractionSyncHandler().setOnMousePressed(
                            t -> {
                                if (baseMetaTileEntity.isServerSide())
                                    onMultButtonPressed(t, isOriginColumn, numberSyncers[index]);
                            }))
                    .size(14)
                    .overlay(GuiTextures.CLOSE)
                    .tooltip(t -> createMultButtonTooltip(t, isOriginColumn, axis)));

            textColumn.child(coordinateRow);
        }

        textColumn.childIf(isOriginColumn, this::createButtonRow);

        return textColumn;
    }

    private void createAddButtonTooltip(RichTooltip t, boolean isOriginColumn, String axis) {
        t.addLine(
            EnumChatFormatting.GREEN + "(Shift) "
                + EnumChatFormatting.RESET
                + EnumChatFormatting.AQUA
                + "Left"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Right"
                + EnumChatFormatting.RESET
                + " Click to "
                + EnumChatFormatting.AQUA
                + "Increment"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Decrement");
        t.addLine((isOriginColumn ? " Origin " : " Size ") + axis + " value by 1 " + EnumChatFormatting.GREEN + "(16)");
        t.addLine(
            EnumChatFormatting.GRAY + ""
                + EnumChatFormatting.ITALIC
                + (isOriginColumn ? "Origin: " : "Size: ")
                + formatNumber(isOriginColumn ? MIN_ORIGIN : MIN_SIZE)
                + " - "
                + formatNumber(isOriginColumn ? MAX_ORIGIN : MAX_SIZE));
    }

    private void createMultButtonTooltip(RichTooltip t, boolean isOriginColumn, String axis) {
        t.addLine(
            EnumChatFormatting.GREEN + "(Shift) "
                + EnumChatFormatting.RESET
                + EnumChatFormatting.AQUA
                + "Left"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Right"
                + EnumChatFormatting.RESET
                + " Click to "
                + EnumChatFormatting.AQUA
                + "Multiply"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Divide");
        t.addLine((isOriginColumn ? " Origin " : " Size ") + axis + " value by 2 " + EnumChatFormatting.GREEN + "(16)");
        t.addLine(
            EnumChatFormatting.GRAY + ""
                + EnumChatFormatting.ITALIC
                + (isOriginColumn ? "Origin: " : "Size: ")
                + formatNumber(isOriginColumn ? MIN_ORIGIN : MIN_SIZE)
                + " - "
                + formatNumber(isOriginColumn ? MAX_ORIGIN : MAX_SIZE));
    }

    private void onAddButtonPressed(MouseData mouseData, boolean isOriginColumn, ShortSyncValue numberSyncer) {

        int changedNumber = numberSyncer.getShortValue();
        int delta = mouseData.shift ? 16 : 1;

        switch (mouseData.mouseButton) {
            case 0 -> changedNumber = Math.min(changedNumber + delta, isOriginColumn ? MAX_ORIGIN : MAX_SIZE);
            case 1 -> changedNumber = Math.max(changedNumber - delta, isOriginColumn ? MIN_ORIGIN : MIN_SIZE);
        }

        numberSyncer.setIntValue(changedNumber);
    }

    private void onMultButtonPressed(MouseData mouseData, boolean isOriginColumn, ShortSyncValue numberSyncer) {

        int changedNumber = numberSyncer.getShortValue();
        int delta = mouseData.shift ? 16 : 2;

        if (changedNumber < 0) {
            switch (mouseData.mouseButton) {
                case 0 -> changedNumber = clamp(changedNumber * delta, MIN_ORIGIN, -1);
                case 1 -> changedNumber = clamp(changedNumber / delta, MIN_ORIGIN, -1);
            }
        } else if (changedNumber > 0) {
            switch (mouseData.mouseButton) {
                case 0 -> changedNumber = clamp(changedNumber * delta, 1, isOriginColumn ? MAX_ORIGIN : MAX_SIZE);
                case 1 -> changedNumber = clamp(changedNumber / delta, 1, isOriginColumn ? MAX_ORIGIN : MAX_SIZE);
            }
        }

        numberSyncer.setIntValue(changedNumber);
    }

    private Flow createButtonRow() {
        BooleanSyncValue transposeSyncer = new BooleanSyncValue(machine::getTranspose, machine::setTranspose);
        BooleanSyncValue showHighlightBoxSyncer = new BooleanSyncValue(
            machine::getShowHighlightBox,
            machine::setShowHighlightBox);

        Flow buttonRow = Flow.row()
            .coverChildren()
            .marginTop(3);

        // button for printing
        buttonRow.child(
            new com.cleanroommc.modularui.widgets.ButtonWidget<>()
                .syncHandler(
                    new InteractionSyncHandler().setOnMousePressed(
                        mouseData -> {
                            if (baseMetaTileEntity.isServerSide()) machine.printStructure(MCHelper.getPlayer());
                        }))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_PRINT)
                .tooltip(t -> t.addLine(IKey.lang("GT5U.machines.debugstructurewriter.gui.print.tooltip"))));

        // button for toggling transpose
        buttonRow.child(
            new ToggleButton().value(transposeSyncer)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_TRANSPOSE)
                .tooltip(t -> t.addLine(IKey.lang("GT5U.machines.debugstructurewriter.gui.transpose.tooltip"))));

        // button for toggling the bounding box
        buttonRow.child(
            new ToggleButton().value(showHighlightBoxSyncer)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_BOUNDING_BOX)
                .tooltip(t -> t.addLine(IKey.lang("GT5U.machines.debugstructurewriter.gui.highlight.tooltip"))));

        return buttonRow;
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
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + 5;
    }
}
