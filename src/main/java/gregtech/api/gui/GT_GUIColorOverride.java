package gregtech.api.gui;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.GregTech_API;
import gregtech.api.util.ColorsMetadataSection;

public class GT_GUIColorOverride {

    private static final Object NOT_FOUND = new Object();
    private static final LoadingCache<ResourceLocation, Object> cache = CacheBuilder.newBuilder()
        .softValues()
        .build(new CacheLoader<>() {

            @Override
            public Object load(@Nonnull ResourceLocation key) throws Exception {
                IResource ir = Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(key);
                if (ir.hasMetadata()) return ir.getMetadata("colors");
                // return a dummy
                // object because
                // LoadingCache
                // doesn't like null
                return NOT_FOUND;
            }
        });
    private static final GT_GUIColorOverride FALLBACK = new GT_GUIColorOverride();
    private ColorsMetadataSection cmSection;

    public static GT_GUIColorOverride get(String fullLocation) {
        // see other get for more info
        if (FMLLaunchHandler.side() != Side.CLIENT) return FALLBACK;
        return new GT_GUIColorOverride(new ResourceLocation(fullLocation));
    }

    public static GT_GUIColorOverride get(ResourceLocation path) {
        // use dummy fallback if there isn't such thing as a resource pack.
        // #side() usually has two possible return value, but since this might be called by test code, it might
        // also return null when in test env. Using #isClient will cause a NPE. A plain inequality test won't.
        // FMLCommonHandler's #getSide() might trigger a NPE when in test env, so no.
        if (FMLLaunchHandler.side() != Side.CLIENT) return FALLBACK;
        return new GT_GUIColorOverride(path);
    }

    private GT_GUIColorOverride() {
        cmSection = null;
    }

    private GT_GUIColorOverride(ResourceLocation resourceLocation) {
        try {
            Object metadata = cache.get(resourceLocation);
            if (metadata != NOT_FOUND) cmSection = (ColorsMetadataSection) metadata;
        } catch (ExecutionException | UncheckedExecutionException ignore) {
            // make sure it doesn't cache a failing entry
            cache.invalidate(resourceLocation);
        }
    }

    public int getTextColorOrDefault(String textType, int defaultColor) {
        return sLoaded() ? cmSection.getTextColorOrDefault(textType, defaultColor) : defaultColor;
    }

    public int getGuiTintOrDefault(String key, int defaultColor) {
        return sLoaded() ? cmSection.getGuiTintOrDefault(key, defaultColor) : defaultColor;
    }

    public boolean sGuiTintingEnabled() {
        return sLoaded() ? cmSection.sGuiTintingEnabled() : GregTech_API.sColoredGUI;
    }

    public boolean sLoaded() {
        return cmSection != null;
    }

    public static void onResourceManagerReload() {
        cache.invalidateAll();
    }
}
