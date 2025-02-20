package gregtech.api.gui.modularui2;

import org.jetbrains.annotations.ApiStatus;

import com.cleanroommc.modularui.factory.GuiManager;

import gregtech.api.metatileentity.CommonMetaTileEntity;

public final class GTGuis {

    /**
     * Specifies whether MUI2 should be used at all. This exists because migration to MUI2 is still WIP and lacks cover
     * panel support. Even when this is set to true, individual GUI could still use MUI1.
     *
     * @see CommonMetaTileEntity#useMui2
     */
    public static boolean GLOBAL_SWITCH_MUI2 = false;

    @ApiStatus.Internal
    public static void registerFactories() {
        GuiManager.registerFactory(MetaTileEntityGuiFactory.INSTANCE);
    }
}
