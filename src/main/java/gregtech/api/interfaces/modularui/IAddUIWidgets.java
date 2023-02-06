package gregtech.api.interfaces.modularui;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

public interface IAddUIWidgets {

    default void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {}
}
