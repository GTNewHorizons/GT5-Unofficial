package gregtech.api.util.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ResourceUtils {

    /**
     * Checks whether a resource exists.
     *
     * @param resLoc the {@link ResourceLocation} identifying the resource to check
     * @return {@code true} if the resource exists; {@code false} otherwise
     */
    @SideOnly(Side.CLIENT)
    public static boolean resourceExists(@NotNull ResourceLocation resLoc) {
        final IResourceManager resMan = Minecraft.getMinecraft()
            .getResourceManager();
        try {
            resMan.getResource(resLoc);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    /**
     * Get the complete resource location from the short local resource location key.
     *
     * @param basePath    The base path for the resource (e.g.: textures/blocks/)
     * @param ext         The file name extension of the resource (e.g.: .png)
     * @param resourceKey The local resource location key (without textures/blocks|items and without the filename
     *                    extension)
     * @return The complete ResourceLocation pointing to the resource file
     */
    public static ResourceLocation getCompleteResourceLocation(String basePath, String ext, String resourceKey) {
        final int i = resourceKey.indexOf(':');
        final String domain = (i <= 0) ? "" : resourceKey.substring(0, i);
        final String path = (i < 0) ? resourceKey : resourceKey.substring(i + 1);
        return new ResourceLocation(domain, basePath + path + ext);
    }

    public static ResourceLocation getCompleteBlockTextureResourceLocation(String resourceKey) {
        return getCompleteResourceLocation("textures/blocks/", ".png", resourceKey);
    }

    public static ResourceLocation getCompleteItemTextureResourceLocation(String resourceKey) {
        return getCompleteResourceLocation("textures/items/", ".png", resourceKey);
    }
}
