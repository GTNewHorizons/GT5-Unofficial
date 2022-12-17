package gregtech.api.gui;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import gregtech.api.GregTech_API;
import gregtech.api.util.ColorsMetadataSection;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class GT_GUIColorOverride {
    private static final Object NOT_FOUND = new Object();
    private static final LoadingCache<ResourceLocation, Object> cache = CacheBuilder.newBuilder()
            .softValues()
            .build(new CacheLoader<ResourceLocation, Object>() {
                @Override
                public Object load(@Nonnull ResourceLocation key) throws Exception {
                    IResource ir = Minecraft.getMinecraft().getResourceManager().getResource(key);
                    if (ir.hasMetadata()) return ir.getMetadata("colors");
                    // return a dummy object because LoadingCache doesn't like null
                    return NOT_FOUND;
                }
            });
    private static final GT_GUIColorOverride FALLBACK = new GT_GUIColorOverride();
    private ColorsMetadataSection cmSection;

    public static GT_GUIColorOverride get(String fullLocation) {
        if (FMLLaunchHandler.side().isServer()) return FALLBACK;
        return get(new ResourceLocation(fullLocation));
    }

    public static GT_GUIColorOverride get(ResourceLocation path) {
        if (FMLLaunchHandler.side().isServer()) return FALLBACK;
        return new GT_GUIColorOverride(path);
    }

    private GT_GUIColorOverride() {
        cmSection = null;
    }

    /**
     * @deprecated use {@link #get(String)} instead.
     */
    @Deprecated
    public GT_GUIColorOverride(String guiTexturePath) {
        this(new ResourceLocation(guiTexturePath));
    }

    private GT_GUIColorOverride(ResourceLocation resourceLocation) {
        try {
            // this is dumb, but CombTypeTest causes cascading class load
            // and leads to instantiation of GT_CoverBehaviorBase
            if (Minecraft.getMinecraft() == null) return;
            Object metadata = cache.get(resourceLocation);
            if (metadata != NOT_FOUND) cmSection = (ColorsMetadataSection) metadata;
        } catch (ExecutionException | UncheckedExecutionException ignore) {
            // make sure it doesn't cache a failing entry
            cache.invalidate(resourceLocation);
            // this is also dumb, but FMLCommonHandler#getEffectiveSide doesn't work during test
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
