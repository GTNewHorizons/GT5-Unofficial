package gregtech.common.modularui2.panel;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.modularui2.GTWidgetThemes;

/**
 * GT themed popup panel.
 */
public class GTPopUpPanel extends ModularPanel {

    private final boolean disablePanelsBelow;
    private final boolean closeOnOutOfBoundsClick;

    public GTPopUpPanel(@NotNull String name, boolean disablePanelsBelow, boolean closeOnOutOfBoundsClick) {
        super(name);
        this.disablePanelsBelow = disablePanelsBelow;
        this.closeOnOutOfBoundsClick = closeOnOutOfBoundsClick;
        child(ButtonWidget.panelCloseButton());
    }

    @Override
    public WidgetThemeEntry<?> getWidgetThemeInternal(ITheme theme) {
        return theme.getWidgetTheme(GTWidgetThemes.BACKGROUND_POPUP);
    }

    @Override
    public boolean disablePanelsBelow() {
        return disablePanelsBelow;
    }

    @Override
    public boolean closeOnOutOfBoundsClick() {
        return closeOnOutOfBoundsClick;
    }
}
