package gregtech.common.gui.modularui.widget;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

@FunctionalInterface
public interface WidgetConfigurator<T extends IWidget> {

    void configure(ModularPanel panel, PanelSyncManager syncManager, T widget);
}
