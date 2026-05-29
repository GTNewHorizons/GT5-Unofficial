package gregtech.api.util.client;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

public class ResourceUtils {

    // 14810 (max size as of 2026/29/5) * 1.333 = 19.7k ~= 20k
    private static final Object2BooleanOpenHashMap<ResourceLocation> EXISTS_CACHE = new Object2BooleanOpenHashMap<>(
        20_000);

    /**
     * Checks whether a resource exists.
     *
     * @param resLoc the {@link ResourceLocation} identifying the resource to check
     * @return {@code true} if the resource exists; {@code false} otherwise
     */
    @SideOnly(Side.CLIENT)
    public static boolean resourceExists(@NotNull ResourceLocation resLoc) {
        return EXISTS_CACHE.computeIfAbsent(resLoc, ResourceUtils::checkResource);
    }

    @SideOnly(Side.CLIENT)
    private static boolean checkResource(@NotNull ResourceLocation resLoc) {
        final IResourceManager resMan = Minecraft.getMinecraft()
            .getResourceManager();
        if (resMan instanceof SimpleReloadableResourceManager simple) {
            FallbackResourceManager fallback = simple.domainResourceManagers.get(resLoc.getResourceDomain());
            if (fallback != null) {
                for (IResourcePack rp : fallback.resourcePacks) {
                    if (rp != null && rp.resourceExists(resLoc)) {
                        return true;
                    }
                }
                return false;
            }
        }

        // Fallback in case some mod changed how the resource manager stores ResourcePacks
        IResource res = null;
        try {
            res = resMan.getResource(resLoc);
            return true;
        } catch (IOException ignored) {
            return false;
        } finally {
            if (res != null) {
                try {
                    InputStream stream = res.getInputStream();
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException ignored) {}
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void clearCache() {
        EXISTS_CACHE.clear();
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
    public static @NotNull ResourceLocation getCompleteResourceLocation(@NotNull String basePath, @NotNull String ext,
        @NotNull String resourceKey) {
        final int i = resourceKey.indexOf(':');
        final String domain = (i <= 0) ? "" : resourceKey.substring(0, i);
        final String path = (i < 0) ? resourceKey : resourceKey.substring(i + 1);
        return new ResourceLocation(domain, basePath + path + ext);
    }

    public static @NotNull ResourceLocation getCompleteBlockTextureResourceLocation(@NotNull String resourceKey) {
        return getCompleteResourceLocation("textures/blocks/", ".png", resourceKey);
    }

    public static @NotNull ResourceLocation getCompleteItemTextureResourceLocation(String resourceKey) {
        return getCompleteResourceLocation("textures/items/", ".png", resourceKey);
    }
}
