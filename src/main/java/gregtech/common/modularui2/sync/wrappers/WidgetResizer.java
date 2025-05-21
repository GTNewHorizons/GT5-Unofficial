package gregtech.common.modularui2.sync.wrappers;

import java.util.function.Consumer;

import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.WidgetTree;

/**
 * Wraps a setter method of a sync value and calls {@link WidgetTree#resize(Widget)} after the setter is called.
 */
public class WidgetResizer {

    public final Widget<?> widget;

    public static WidgetResizer of(Widget<?> widget) {
        return new WidgetResizer(widget);
    }

    private WidgetResizer(Widget<?> widget) {
        this.widget = widget;
    }

    public <T> Consumer<T> wrap(Consumer<T> setter) {
        return newValue -> {
            setter.accept(newValue);
            widget.scheduleResize();
        };
    }

}
