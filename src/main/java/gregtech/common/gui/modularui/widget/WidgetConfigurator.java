package gregtech.common.gui.modularui.widget;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

public interface WidgetConfigurator<T> {

    void configure(ModularPanel panel, PanelSyncManager syncManager, T widget);
}
