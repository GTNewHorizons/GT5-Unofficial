package gregtech.common.gui.modularui.widget.settings;

import java.util.function.Function;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.sync.IValueSyncHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.github.bsideup.jabel.Desugar;

import gregtech.common.gui.modularui.widget.settings.ReadoutSettingRow.ReadoutWidgets;
import it.unimi.dsi.fastutil.Pair;

@Desugar
public record ReadoutSettingRow<S extends SyncHandler & IValueSyncHandler<T>, T> (IKey label, S value,
    Function<T, IKey> format) implements ISettingRow<ReadoutWidgets> {

    @Desugar
    public record ReadoutWidgets(TextWidget<?> labelWidget, TextWidget<?> readout) {

    }

    @Override
    public Pair<ReadoutWidgets, Widget<?>> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        syncManager.syncValue(settings.syncName, settings.nextSyncId++, value);

        ReadoutWidgets w = new ReadoutWidgets(
            label.asWidget()
                .textAlign(Alignment.CenterRight),
            IKey.dynamicKey(() -> format.apply(value.getValue()))
                .asWidget()
                .marginRight(2)
                .marginLeft(8)
                .expanded());

        return Pair.of(
            w,
            Flow.row()
                .mainAxisAlignment(MainAxis.START)
                .widthRel(1f)
                .height(20)
                .child(w.labelWidget)
                .child(w.readout));
    }

    @Override
    public void resize(SettingsPanel settings, ReadoutWidgets w, Widget widget, int dividerPosition) {
        w.labelWidget.width(dividerPosition);
    }
}
