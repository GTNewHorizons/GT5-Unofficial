package gregtech.common.gui.modularui2.panel;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.gui.modularui2.GTGuiTextures;

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
        background(GTGuiTextures.BACKGROUND_POPUP);
        child(ButtonWidget.panelCloseButton());
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
