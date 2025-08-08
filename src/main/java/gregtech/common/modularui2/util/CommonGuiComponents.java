package gregtech.common.modularui2.util;

import java.util.function.IntFunction;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;

/**
 * Random components useful for creating GUI.
 */
public final class CommonGuiComponents {

    /**
     * Creates 1x1 grid widget placed at the center of the normal-sized GUI.
     *
     * @param widgetCreator Takes index of the grid and returns corresponding widget.
     */
    public static Grid gridTemplate1by1(IntFunction<IWidget> widgetCreator) {
        return new Grid().coverChildren()
            .pos(79, 34)
            .mapTo(1, 1, widgetCreator);
    }

    /**
     * Creates 2x2 grid widget placed at the center of the normal-sized GUI.
     *
     * @param widgetCreator Takes index of the grid and returns corresponding widget.
     */
    public static Grid gridTemplate2by2(IntFunction<IWidget> widgetCreator) {
        return new Grid().coverChildren()
            .pos(70, 25)
            .mapTo(2, 4, widgetCreator);
    }

    /**
     * Creates 3x3 grid widget placed at the center of the normal-sized GUI.
     *
     * @param widgetCreator Takes index of the grid and returns corresponding widget.
     */
    public static Grid gridTemplate3by3(IntFunction<IWidget> widgetCreator) {
        return new Grid().coverChildren()
            .pos(61, 16)
            .mapTo(3, 9, widgetCreator);
    }

    /**
     * Creates 4x4 grid widget placed at the center of the normal-sized GUI.
     *
     * @param widgetCreator Takes index of the grid and returns corresponding widget.
     */
    public static Grid gridTemplate4by4(IntFunction<IWidget> widgetCreator) {
        return new Grid().coverChildren()
            .pos(52, 7)
            .mapTo(4, 16, widgetCreator);
    }
}
