package gregtech.common.gui.modularui.widget.settings;

import java.util.List;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;

import it.unimi.dsi.fastutil.Pair;

public class SettingsPanel extends ListWidget<IWidget, SettingsPanel> {

    final String syncName;
    int nextSyncId = 0;

    SettingsPanel(ModularPanel panel, PanelSyncManager syncManager, List<ISettingRow<?>> rows, String syncName,
        int maxHeight) {
        this.syncName = syncName;

        scrollDirection(new VerticalScrollData());
        coverChildrenWidth();
        maxSize(maxHeight);

        Grid mainGrid = new Grid().coverChildren()
            .minElementMargin(2)
            .columnAlignments(new Alignment[] { Alignment.CenterRight, Alignment.CenterLeft });

        for (ISettingRow<?> sr : rows) {
            Pair<IKey, ? extends IWidget> p = sr.build(panel, syncManager, this);

            mainGrid.nextRow()
                .child(
                    p.left()
                        .asWidget())
                .child(p.right());
        }

        child(mainGrid);
    }

    public static SettingsPanelBuilder builder() {
        return new SettingsPanelBuilder();
    }
}
