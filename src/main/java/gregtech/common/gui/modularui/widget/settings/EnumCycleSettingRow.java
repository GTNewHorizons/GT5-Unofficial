package gregtech.common.gui.modularui.widget.settings;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.EnumCycleButtonWidget;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.EnumCycleSettingRow.EnumCycleWidgets;
import it.unimi.dsi.fastutil.Pair;

@Desugar
record EnumCycleSettingRow<E extends Enum<E>> (IKey label, Class<E> clazz, Supplier<E> getter, Consumer<E> setter,
    WidgetConfigurator<EnumCycleButtonWidget<E>> configure) implements ISettingRow<EnumCycleWidgets<E>> {

    @Desugar
    public record EnumCycleWidgets<E extends Enum<E>> (TextWidget<?> labelWidget, EnumCycleButtonWidget<E> button) {

    }

    @Override
    public Pair<EnumCycleWidgets<E>, Widget<?>> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        EnumCycleWidgets<E> w = new EnumCycleWidgets<>(
            label.asWidget()
                .textAlign(Alignment.CenterRight),
            new EnumCycleButtonWidget<>(clazz).value(new EnumSyncValue<>(clazz, getter, setter)));

        w.button.marginLeft(6);

        if (configure != null) configure.configure(panel, syncManager, w.button);

        return Pair.of(
            w,
            Flow.row()
                .mainAxisAlignment(MainAxis.START)
                .widthRel(1f)
                .height(20)
                .child(w.labelWidget)
                .child(w.button));
    }

    @Override
    public void resize(SettingsPanel settings, EnumCycleWidgets<E> w, Widget<?> widget, int dividerPosition) {
        w.labelWidget.width(dividerPosition);

        int width = 0;

        if (GTUtility.isClient()) {
            for (E e : clazz.getEnumConstants()) {
                String text = e.toString();

                width = Math.max(
                    width,
                    TextRenderer.getFontRenderer()
                        .getStringWidth(text));
            }
        }

        w.button.width(width + 10);
    }
}
