package gregtech.api.interfaces;

import static gregtech.api.enums.GT_Values.UNCOLORED_RGBA;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IIconContainer {

    /**
     * @return A regular Icon.
     */
    @SideOnly(Side.CLIENT)
    IIcon getIcon();

    /**
     * @return Icon of the Overlay (or null if there is no Icon)
     */
    @SideOnly(Side.CLIENT)
    IIcon getOverlayIcon();

    /**
     * @return the Amount of Render Passes for this Icon.
     */
    @SideOnly(Side.CLIENT)
    default int getIconPasses() {
        return 1;
    }

    /**
     * @return the Default Texture File for this Icon.
     */
    @SideOnly(Side.CLIENT)
    ResourceLocation getTextureFile();

    @SideOnly(Side.CLIENT)
    default short[] getIconColor(int aRenderPass) {
        return UNCOLORED_RGBA;
    }

    @SideOnly(Side.CLIENT)
    default boolean isUsingColorModulation(int aRenderPass) {
        return aRenderPass == 0;
    }
}
