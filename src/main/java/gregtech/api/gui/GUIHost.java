package gregtech.api.gui;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

public interface GUIHost extends ITileWithModularUI {

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

    default boolean shouldOpen() {
        return true;
    }

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
