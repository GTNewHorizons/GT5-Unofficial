package gregtech.common.gui.modularui.widget.settings;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;

import it.unimi.dsi.fastutil.Pair;

public interface ISettingRow<State> {

    Pair<State, Widget<?>> build(ModularPanel panel, PanelSyncManager syncManager, SettingsPanel settings);

    void resize(SettingsPanel settings, State state, Widget<?> widget, int dividerPosition);
}
