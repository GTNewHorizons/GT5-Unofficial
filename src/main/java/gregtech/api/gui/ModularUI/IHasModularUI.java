package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

/**
 * Implement this interface to provide {@link ModularWindow}.
 */
public interface IHasModularUI {

    /**
     * Creates UI with ModularUI system. Start building UI with {@link ModularWindow#builder}
     * and call {@link ModularWindow.Builder#build} to finish.
     */
    ModularWindow createWindow(UIBuildContext buildContext);
}
