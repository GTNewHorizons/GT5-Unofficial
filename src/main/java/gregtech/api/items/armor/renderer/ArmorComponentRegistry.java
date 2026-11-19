package gregtech.api.items.armor.renderer;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;

public class ArmorComponentRegistry {

    public static final Map<String, ArmorComponent> componentCache = new HashMap<>();
    public static final String BASE_ARMOR_FOLDER = "textures/model/armor/";
    private static final Gson GSON = new Gson();

    public static void loadAllAssets() {
        clearCache();

        try {
            ResourceLocation indexLoc = new ResourceLocation("gregtech", BASE_ARMOR_FOLDER + "index.json");

            try (InputStreamReader indexReader = new InputStreamReader(
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(indexLoc)
                    .getInputStream())) {

                Map<String, List<String>> indexData = GSON.fromJson(indexReader, Map.class);
                loadModelFiles(indexData.get("models"));
            }

            GT_FML_LOGGER.info("[GTNH-VoxelArmor] Successfully cached loaded assets");

        } catch (Exception e) {
            GT_FML_LOGGER.error("[GTNH-VoxelArmor] Failed to load assets", e);
        }
    }

    private static void loadModelFiles(List<String> modelFiles) {
        if (modelFiles == null) {
            GT_FML_LOGGER.error("[GTNH-VoxelArmor] index.json has no \"models\" list");
            return;
        }

        IResourceManager resourceManager = Minecraft.getMinecraft()
            .getResourceManager();

        for (String modelFile : modelFiles) {
            ResourceLocation modelLocation = new ResourceLocation(
                "gregtech",
                BASE_ARMOR_FOLDER + modelFile + ".bbmodel");

            try (InputStreamReader modelReader = new InputStreamReader(
                resourceManager.getResource(modelLocation)
                    .getInputStream(),
                StandardCharsets.UTF_8)) {

                JsonObject root = new JsonParser().parse(modelReader)
                    .getAsJsonObject();

                BufferedImage texture = BlockbenchModelParser.readEmbeddedTexture(root);
                if (texture == null) {
                    GT_FML_LOGGER.error("[GTNH-VoxelArmor] '{}' has no embedded texture, skipping", modelFile);
                    continue;
                }

                for (Map.Entry<String, BakedArmorModel> component : BlockbenchModelParser.parse(root)
                    .entrySet()) {
                    String componentId = component.getKey()
                        .toLowerCase();
                    componentCache.put(componentId, new ArmorComponent(componentId, component.getValue(), texture));
                }
            } catch (Exception e) {
                GT_FML_LOGGER.error("[GTNH-VoxelArmor] Skipping model file '{}'", modelFile, e);
            }
        }
    }

    public static void clearCache() {
        componentCache.clear();
    }

    public static ArmorComponent getComponent(String key) {
        return componentCache.get(key);
    }
}
