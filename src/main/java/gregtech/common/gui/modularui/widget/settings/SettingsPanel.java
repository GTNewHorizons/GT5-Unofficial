package gregtech.common.gui.modularui.widget.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.github.bsideup.jabel.Desugar;

import it.unimi.dsi.fastutil.Pair;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SettingsPanel extends Column {

    @Desugar
    private record RowData(Object state, ISettingRow row, Widget widget) {

    }

    private final List<RowData> rows = new ArrayList<>();

    private final Function<SettingsPanel, Integer> dividerPosition;

    final String syncName;
    int nextSyncId = 1;

    SettingsPanel(ModularPanel panel, PanelSyncManager syncManager, List<ISettingRow<?>> rows,
        Function<SettingsPanel, Integer> dividerPosition, String syncName) {
        this.dividerPosition = dividerPosition;
        this.syncName = syncName;

        for (ISettingRow row : rows) {
            Pair<Object, Widget<?>> p = row.build(panel, syncManager, this);

            child(p.right());

            this.rows.add(new RowData(p.left(), row, p.right()));
        }
    }

    @Override
    public void beforeResize(boolean onOpen) {
        super.beforeResize(onOpen);

        int div = dividerPosition.apply(this);

        for (RowData p : rows) {
            p.row()
                .resize(this, p.state(), p.widget(), div);
        }
    }

    public static SettingsPanelBuilder builder() {
        return new SettingsPanelBuilder();
    }
}
