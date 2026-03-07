package tectech.thing.gui;

import java.util.Arrays;
import java.util.function.IntConsumer;

import javax.annotation.ParametersAreNonnullByDefault;

import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/// Helper class for UI creation of "debug" machines
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DebugUIHelper {

    /**
     * Creates a screen with background {@link GTGuiTextures#PICTURE_SCREEN_BLACK}. This is where the text elements
     * should be added to.
     *
     * @param extraWidth  Extra screen width added to a base of 90.
     * @param extraHeight Extra screen height added to a base of 72.
     */
    public static Flow getScreen(int extraWidth, int extraHeight, int childPadding, Alignment logoAlingment) {
        return new Column().crossAxisAlignment(Alignment.CrossAxis.START)
            .size(90 + extraWidth, 72 + extraHeight)
            .padding(3)
            .childPadding(childPadding)
            .background(GTGuiTextures.PICTURE_SCREEN_BLACK)
            .overlay(
                GTGuiTextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY.asIcon()
                    .size(17)
                    .alignment(logoAlingment));
    }

    /**
     * {@link #getScreen(int, int, int, Alignment)} but with logo alignment of bottom right.
     */
    public static Flow getScreen(int extraWidth, int extraHeight, int childPadding) {
        return getScreen(extraWidth, extraHeight, childPadding, Alignment.BottomRight);
    }

    /**
     * Creates a column of buttons in top-down order. All input arrays must have the same length, and each
     * index of the arrays corresponds to a row in the column.
     *
     * @param offsetShifts The amount the setter will be called with when the button is shift-clicked.
     * @param offsets      The amount the setter will be called with when the button is clicked.
     * @param setters      This will be called when the button is clicked.
     */
    public static Flow getButtonColumn(com.cleanroommc.modularui.api.drawable.IDrawable overlay, int[] offsetShifts,
        int[] offsets, IntConsumer[] setters) {
        if (offsetShifts.length != offsets.length || offsets.length != setters.length)
            throw new IllegalArgumentException("Not all input arrays are of the same length!");

        Flow flow = new Column();

        for (int i = 0; i < setters.length; i++) {
            final int index = i;
            flow.child(
                new com.cleanroommc.modularui.widgets.ButtonWidget<>()
                    .syncHandler(
                        new InteractionSyncHandler().setOnMousePressed(
                            mouseData -> setters[index].accept(mouseData.shift ? offsetShifts[index] : offsets[index])))
                    .overlay(overlay));
        }

        return flow.coverChildren();
    }

    /**
     * Assembles the main content section of the UI. The result will be two column of buttons, the screen, and another
     * two column of buttons. The buttons have the overlays in order: {@link GTGuiTextures#OVERLAY_BUTTON_MINUS_LARGE},
     * {@link GTGuiTextures#OVERLAY_BUTTON_MINUS_SMALL},{@link GTGuiTextures#OVERLAY_BUTTON_PLUS_SMALL},
     * {@link GTGuiTextures#OVERLAY_BUTTON_PLUS_LARGE}.
     *
     * @param screen             The screen in the middle.
     * @param minusColumnSetters The setters for the minus (left side) buttons.
     * @param plusColumnSetters  The setters for the plus (right side) buttons.
     * @param largeShiftOffsets  Shift-click values for the large (outside) buttons.
     * @param largeOffsets       Click values for the large (outside) buttons.
     * @param smallShiftOffsets  Shift-click values for the small (inside) buttons.
     * @param smallOffsets       Click values for the small (inside) buttons.
     */
    public static Flow getContentSection(Flow screen, IntConsumer[] minusColumnSetters, IntConsumer[] plusColumnSetters,
        int[] largeShiftOffsets, int[] largeOffsets, int[] smallShiftOffsets, int[] smallOffsets) {

        return new Row()
            .child(
                getButtonColumn(
                    GTGuiTextures.OVERLAY_BUTTON_MINUS_LARGE,
                    largeShiftOffsets,
                    largeOffsets,
                    minusColumnSetters))
            .child(
                getButtonColumn(
                    GTGuiTextures.OVERLAY_BUTTON_MINUS_SMALL,
                    smallShiftOffsets,
                    smallOffsets,
                    minusColumnSetters))
            .child(screen)
            .child(
                getButtonColumn(
                    GTGuiTextures.OVERLAY_BUTTON_PLUS_SMALL,
                    smallShiftOffsets,
                    smallOffsets,
                    plusColumnSetters))
            .child(
                getButtonColumn(
                    GTGuiTextures.OVERLAY_BUTTON_PLUS_LARGE,
                    largeShiftOffsets,
                    largeOffsets,
                    plusColumnSetters))
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .margin(7, 4);
    }

    /**
     * {@link #getContentSection(Flow, IntConsumer[], IntConsumer[], int[], int[], int[], int[])} but with constants for
     * largeShiftOffsets, largeOffsets, and smallShiftOffsets.
     */
    public static Flow getContentSection(Flow screen, IntConsumer[] minusColumnSetters, IntConsumer[] plusColumnSetters,
        int largeShiftOffset, int largeOffset, int smallShiftOffset, int[] smallOffsets) {

        int[] largeShiftOffsets = new int[minusColumnSetters.length];
        int[] largeOffsets = new int[minusColumnSetters.length];
        int[] smallShiftOffsets = new int[minusColumnSetters.length];

        Arrays.fill(largeShiftOffsets, largeShiftOffset);
        Arrays.fill(largeOffsets, largeOffset);
        Arrays.fill(smallShiftOffsets, smallShiftOffset);

        return getContentSection(
            screen,
            minusColumnSetters,
            plusColumnSetters,
            largeShiftOffsets,
            largeOffsets,
            smallShiftOffsets,
            smallOffsets);
    }
}
