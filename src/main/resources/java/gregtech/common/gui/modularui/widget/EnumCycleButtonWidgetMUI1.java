package gregtech.common.gui.modularui.widget;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.gtnewhorizons.modularui.common.widget.VanillaButtonWidget;

import gregtech.api.util.GTUtility;

public class EnumCycleButtonWidgetMUI1<T extends Enum<T>> extends VanillaButtonWidget {

    private final T[] values;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public EnumCycleButtonWidgetMUI1(Class<T> clazz, Supplier<T> getter, Consumer<T> setter) {
        this.values = clazz.getEnumConstants();
        this.getter = getter;
        this.setter = setter;

        setDisplayString(
            getter.get()
                .toString());

        setOnClick((clickData, widget) -> {
            int offset = 0;

            if (clickData.mouseButton == 0) {
                offset = 1;
            } else if (clickData.mouseButton == 1) {
                offset = -1;
            }

            T current = getter.get();

            T value = values[GTUtility.mod(current.ordinal() + offset, values.length)];

            setter.accept(value);

            ((VanillaButtonWidget) widget).setDisplayString(value.toString());
            widget.notifyTooltipChange();
        });
    }

    public EnumCycleButtonWidgetMUI1<T> setTooltip(Function<T, String> tooltip) {
        dynamicTooltip(() -> Arrays.asList(tooltip.apply(getter.get())));

        return this;
    }
}
