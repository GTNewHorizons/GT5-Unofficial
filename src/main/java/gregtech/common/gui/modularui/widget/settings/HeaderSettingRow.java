package gregtech.common.gui.modularui.widget.settings;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.github.bsideup.jabel.Desugar;

import it.unimi.dsi.fastutil.Pair;

@Desugar
record HeaderSettingRow(IKey header) implements ISettingRow<Void> {

    @Override
    public Pair<Void, Widget<?>> build(ModularPanel panel, PanelSyncManager syncManager, SettingsPanel settings) {
        return Pair.of(
            null,
            Flow.row()
                .mainAxisAlignment(MainAxis.CENTER)
                .widthRel(1f)
                .height(20)
                .child(
                    header.asWidget()
                        .textAlign(Alignment.BottomCenter)));
    }

    @Override
    public void resize(SettingsPanel settings, Void v, Widget<?> widget, int dividerPosition) {

    }
}
