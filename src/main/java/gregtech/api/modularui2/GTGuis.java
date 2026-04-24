package gregtech.api.modularui2;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.ClientGUI;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.CommonMetaTileEntity;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.modularui2.panel.GTPopUpPanel;

/**
 * Centralized place for starting creation of GUI/panel.
 */
public final class GTGuis {

    /**
     * Specifies whether MUI2 should be used at all. This exists because migration to MUI2 is still WIP and lacks cover
     * panel support. Even when this is set to true, individual GUI could still use MUI1.
     *
     * @see CommonMetaTileEntity#useMui2
     */
    public static boolean GLOBAL_SWITCH_MUI2 = true;

    /**
     * Creates builder object for MetaTileEntity GUI template. Call {@link GTBaseGuiBuilder#build} to retrieve panel.
     *
     * @see GTBaseGuiBuilder
     */
    public static GTBaseGuiBuilder mteTemplatePanelBuilder(IMetaTileEntity mte, PosGuiData data,
        PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(mte, data, syncManager, uiSettings);
    }

    /**
     * Creates popup panel with GT style.
     */
    public static ModularPanel createPopUpPanel(@NotNull String name) {
        return createPopUpPanel(name, false, false);
    }

    /**
     * Creates popup panel with GT style.
     */
    public static ModularPanel createPopUpPanel(@NotNull String name, boolean disablePanelsBelow,
        boolean closeOnOutOfBoundsClick) {
        return new GTPopUpPanel(name, disablePanelsBelow, closeOnOutOfBoundsClick);
    }

    /**
     * Opens client-only GUI with the supplied panel. Recipe viewer is enabled.
     */
    @SideOnly(Side.CLIENT)
    public static void openClientOnlyScreen(ModularPanel panel) {
        openClientOnlyScreen(panel, true);
    }

    /**
     * Opens client-only GUI with the supplied panel. Can specify whether to enable the recipe viewer.
     */
    @SideOnly(Side.CLIENT)
    public static void openClientOnlyScreen(ModularPanel panel, boolean enableRecipeViewer) {
        ModularScreen screen = new GTModularScreen(panel, GTGuiThemes.STANDARD);

        UISettings settings = new UISettings();
        if (enableRecipeViewer) settings.getRecipeViewerSettings()
            .enable();

        ClientGUI.open(screen, settings);
    }

    @ApiStatus.Internal
    public static void registerFactories() {
        GuiManager.registerFactory(MetaTileEntityGuiHandler.INSTANCE);
        GuiManager.registerFactory(CoverUIFactory.INSTANCE);
        GuiManager.registerFactory(ToolboxSelectGuiFactory.INSTANCE);
    }
}
