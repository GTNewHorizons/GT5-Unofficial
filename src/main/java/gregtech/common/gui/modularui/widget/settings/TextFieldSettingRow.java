package gregtech.common.gui.modularui.widget.settings;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import it.unimi.dsi.fastutil.Pair;

@Desugar
record TextFieldSettingRow(IKey label, WidgetConfigurator<TextFieldWidget> configure)
    implements ISettingRow<TextFieldSettingRow.TextFieldWidgets> {

    @Desugar
    public record TextFieldWidgets(TextWidget<?> labelWidget, TextFieldWidget editor) {

    }

    @Override
    public Pair<TextFieldWidgets, Widget<?>> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        TextFieldWidgets w = new TextFieldWidgets(
            label.asWidget()
                .textAlign(Alignment.CenterRight),
            new TextFieldWidget().marginLeft(6)
                .marginRight(2)
                .width(80)
                .expanded());

        if (configure != null) configure.configure(panel, syncManager, w.editor);

        return Pair.of(
            w,
            Flow.row()
                .mainAxisAlignment(MainAxis.START)
                .widthRel(1f)
                .height(22)
                .child(w.labelWidget)
                .child(w.editor));
    }

    @Override
    public void resize(SettingsPanel settings, TextFieldWidgets w, Widget<?> widget, int dividerPosition) {
        w.labelWidget.width(dividerPosition);
    }
}
