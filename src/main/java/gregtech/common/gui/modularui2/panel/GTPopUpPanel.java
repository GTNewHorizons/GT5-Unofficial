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
    private final Runnable onClose;

    public GTPopUpPanel(@NotNull String name, boolean disablePanelsBelow, boolean closeOnOutOfBoundsClick,
        Runnable onClose) {
        super(name);
        this.disablePanelsBelow = disablePanelsBelow;
        this.closeOnOutOfBoundsClick = closeOnOutOfBoundsClick;
        this.onClose = onClose;
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

    @Override
    public void onClose() {
        if (this.onClose != null) {
            this.onClose.run();
        }
        super.onClose();
    }
}
