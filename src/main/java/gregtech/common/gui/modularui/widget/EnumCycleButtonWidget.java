package gregtech.common.gui.modularui.widget;

import java.util.function.Function;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IIntValue;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.EnumValue;
import com.cleanroommc.modularui.widgets.AbstractCycleButtonWidget;

import gregtech.api.util.GTDataUtils;

public class EnumCycleButtonWidget<E extends Enum<E>> extends AbstractCycleButtonWidget<EnumCycleButtonWidget<E>> {

    private final Class<E> clazz;

    public EnumCycleButtonWidget(Class<E> clazz) {
        this.clazz = clazz;

        overlay(e -> IKey.str(e.toString()));
    }

    @Override
    public EnumCycleButtonWidget<E> value(IIntValue<?> value) {
        return super.value(value);
    }

    public EnumCycleButtonWidget<E> value(EnumValue<E> value) {
        return super.value(value);
    }

    public EnumCycleButtonWidget<E> overlay(Function<E, IDrawable> overlay) {
        this.overlay = GTDataUtils.mapToArray(clazz.getEnumConstants(), IDrawable[]::new, overlay);
        return this;
    }

    public EnumCycleButtonWidget<E> hoverOverlay(Function<E, IDrawable> overlay) {
        this.hoverOverlay = GTDataUtils.mapToArray(clazz.getEnumConstants(), IDrawable[]::new, overlay);
        return this;
    }

    public EnumCycleButtonWidget<E> background(Function<E, IDrawable> overlay) {
        this.background = GTDataUtils.mapToArray(clazz.getEnumConstants(), IDrawable[]::new, overlay);
        return this;
    }

    public EnumCycleButtonWidget<E> hoverBackground(Function<E, IDrawable> overlay) {
        this.hoverBackground = GTDataUtils.mapToArray(clazz.getEnumConstants(), IDrawable[]::new, overlay);
        return this;
    }

    public EnumCycleButtonWidget<E> tooltip(Function<E, RichTooltip> overlay) {
        this.tooltip = GTDataUtils.mapToArray(clazz.getEnumConstants(), RichTooltip[]::new, overlay);
        return this;
    }
}
