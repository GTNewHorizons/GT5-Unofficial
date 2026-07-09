package gregtech.api.items.armor.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import gregtech.GTMod;

public class ArmorRegistry {

    private static final Map<String, VoxelArmorRenderer> modelCache = new HashMap<>();

    public static VoxelArmorRenderer getOrCompileModel(ArmorComponent baseArmor, List<ArmorComponent> augments,
        float scale) {
        StringBuilder keyBuilder = new StringBuilder(baseArmor.componentId);
        if (augments != null && !augments.isEmpty()) {
            List<String> sortedAugments = augments.stream()
                .map(a -> a.componentId)
                .sorted()
                .toList();

            for (String augId : sortedAugments) {
                keyBuilder.append("+")
                    .append(augId);
            }
        }
        keyBuilder.append("_")
            .append(scale);
        String cacheKey = keyBuilder.toString();

        if (modelCache.containsKey(cacheKey)) {
            return modelCache.get(cacheKey);
        }

        try {
            ArmorModelCompiler.CompileResult result = ArmorModelCompiler.compile(baseArmor, augments);

            DynamicTexture dynTex = new DynamicTexture(result.mergedImage);
            ResourceLocation generatedTexLocation = new ResourceLocation(
                "gregtech_dynamic",
                "armor_atlas_" + cacheKey.hashCode());
            Minecraft.getMinecraft()
                .getTextureManager()
                .loadTexture(generatedTexLocation, dynTex);

            VoxelArmorRenderer newRenderer = new VoxelArmorRenderer(scale, result.mergedJson, generatedTexLocation);

            modelCache.put(cacheKey, newRenderer);
            return newRenderer;

        } catch (Exception e) {
            GTMod.GT_FML_LOGGER.error("[GTNH-VoxelArmor] Error compiling: {}", cacheKey, e);
            return null;
        }
    }

    public static void clearCache() {
        for (VoxelArmorRenderer renderer : modelCache.values()) {
            renderer.deleteOpenGLData();
        }

        modelCache.clear();
    }
}
