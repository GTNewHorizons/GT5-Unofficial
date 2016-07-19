package miscutil.core.xmod.gregtech.api.interfaces.internal;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public interface Interface_IconContainer {
    /**
     * @return A regular Icon.
     */
    public IIcon getIcon();

    /**
     * @return Icon of the Overlay (or null if there is no Icon)
     */
    public IIcon getOverlayIcon();

    /**
     * @return the Default Texture File for this Icon.
     */
    public ResourceLocation getTextureFile();
}