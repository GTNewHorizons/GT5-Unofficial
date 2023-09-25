package gregtech.api.gui;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

public interface GUIHost extends ITileWithModularUI {

    @Nonnull
    @Override
    default ModularWindow createWindow(UIBuildContext uiContext) {
        GUIProvider<?> gui = getGUI();
        return gui.openGUI(Objects.requireNonNull(uiContext));
    }

    default int getWidth() {
        return 170;
    }

    default int getHeight() {
        return 100;
    }

    @Nonnull
    GUIProvider<?> getGUI();

}
