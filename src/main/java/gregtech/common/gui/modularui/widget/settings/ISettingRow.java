package gregtech.common.gui.modularui.widget.settings;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import it.unimi.dsi.fastutil.Pair;

public interface ISettingRow<W extends IWidget> {

    @NotNull
    Pair<IKey, W> build(ModularPanel panel, PanelSyncManager syncManager, SettingsPanel settings);
}
