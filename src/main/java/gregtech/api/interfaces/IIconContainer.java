package gregtech.api.interfaces;

import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A texture's icons on one atlas, drawn as a stack of layers. A consumer draws {@link #getIconPasses} layers from
 * layer 0 upwards, each from {@link #getLayerIcon} and colored either by {@link #getIconColor} or by the consumer's own
 * color modulation, as {@link #isUsingColorModulation} decides.
 * <p>
 * A container with at most a base and one icon over it only needs {@link #getIcon} and {@link #getOverlayIcon}: the
 * layer defaults read that pair, giving two layers, or one when the overlay is null.
 */
public interface IIconContainer {

    /**
     * @return The base icon.
     */
    @SideOnly(Side.CLIENT)
    IIcon getIcon();

    /**
     * @return The icon drawn over the base, or null when there is none.
     */
    @SideOnly(Side.CLIENT)
    IIcon getOverlayIcon();

    /**
     * @return The number of icon layers this container draws.
     */
    @SideOnly(Side.CLIENT)
    default int getIconPasses() {
        return getOverlayIcon() != null ? 2 : 1;
    }

    /**
     * @return The icon at {@code layer}. The default answers {@link #getIcon} for layer 0 and {@link #getOverlayIcon}
     *         for any later one.
     */
    @SideOnly(Side.CLIENT)
    default IIcon getLayerIcon(int layer) {
        return layer == 0 ? getIcon() : getOverlayIcon();
    }

    @SideOnly(Side.CLIENT)
    default int getRenderIconPass() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    default boolean canRenderInPass(int pass) {
        return pass == getRenderIconPass();
    }

    /**
     * @return the Default Texture File for this Icon.
     */
    @SideOnly(Side.CLIENT)
    ResourceLocation getTextureFile();

    /**
     * @return Whether this Icon came from a resource pack override location, which carries its own colors and is
     *         drawn untinted. See {@link com.ruling_0.materiallib.api.ShapeItem#hasOverrideIcon}.
     */
    @SideOnly(Side.CLIENT)
    default boolean hasOverrideIcon() {
        return false;
    }

    /**
     * @return The color layer {@code layer} draws with.
     */
    @SideOnly(Side.CLIENT)
    default short[] getIconColor(int layer) {
        return UNCOLORED_RGBA;
    }

    /**
     * @return Whether layer {@code layer} takes the consumer's own color modulation in place of
     *         {@link #getIconColor}.
     */
    @SideOnly(Side.CLIENT)
    default boolean isUsingColorModulation(int layer) {
        return layer == 0;
    }
}
