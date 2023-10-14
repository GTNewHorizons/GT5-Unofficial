package gregtech.api.gui;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import net.minecraft.item.ItemStack;

public interface GUIHost extends ITileWithModularUI {

    @Nonnull
    @Override
    default ModularWindow createWindow(UIBuildContext uiContext) {
        Objects.requireNonNull(uiContext);
        GUIProvider<?> gui = getGUI(uiContext);
        return gui.openGUI(uiContext);
    }

    /**
     * Width of the GUI when its being displayed
     */
    default int getWidth() {
        return 170;
    }

    default int getHeight() {
        return 192;
    }

    @Nonnull
    GUIProvider<?> getGUI(@Nonnull UIBuildContext uiContext);

    ItemStack getAsItem();

    String getMachineName();

    default boolean hasItemInput() {
        return true;
    }

    default boolean hasItemOutput() {
        return true;
    }

    default boolean hasFluidInput() {
        return true;
    }

    default boolean hasFluidOutput() {
        return true;
    }
}
