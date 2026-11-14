package gregtech.api.items.armor.renderer;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;

import gregtech.GTMod;

public class ArmorComponentRegistry {

    public static final Map<String, ArmorComponent> componentCache = new HashMap<>();
    private static final Gson GSON = new Gson();
    public static final String BASE_ARMOR_FOLDER = "textures/model/armor/";

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
                getFromIndexData(indexData, "armor");
                getFromIndexData(indexData, "augment");
            }

            GTMod.GT_FML_LOGGER.info("[GTNH-VoxelArmor] Successfully cached loaded assets");

        } catch (Exception e) {
            GTMod.GT_FML_LOGGER.error("[GTNH-VoxelArmor] Failed to load assets", e);
        }
    }

    public static void getFromIndexData(Map<String, List<String>> indexData, String type) {
        List<String> modelNames = indexData.get(type);
        if (modelNames == null) {
            GTMod.GT_FML_LOGGER.error("[GTNH-VoxelArmor] No matching model names found for {}", type);
            return;
        }

        for (String name : modelNames) {
            BedrockArmorModel model = null;
            BufferedImage img = null;

            try {
                ResourceLocation modelLoc = new ResourceLocation(
                    "gregtech",
                    BASE_ARMOR_FOLDER + type + "_models/" + name + ".geo.json");
                try (InputStreamReader modelReader = new InputStreamReader(
                    Minecraft.getMinecraft()
                        .getResourceManager()
                        .getResource(modelLoc)
                        .getInputStream())) {
                    model = GSON.fromJson(modelReader, BedrockArmorModel.class);
                }
            } catch (Exception e) {
                GTMod.GT_FML_LOGGER.error("[GTNH-VoxelArmor] Error loading model: {}", name, e);
            }

            try {
                ResourceLocation texLoc = new ResourceLocation(
                    "gregtech",
                    BASE_ARMOR_FOLDER + type + "_textures/" + name + ".png");
                try (InputStream texStream = Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(texLoc)
                    .getInputStream()) {
                    img = ImageIO.read(texStream);
                }
            } catch (Exception e) {
                GTMod.GT_FML_LOGGER.error("[GTNH-VoxelArmor] Error loading texture: {}", name, e);
            }

            if (model != null && img != null) {
                componentCache.put(name, new ArmorComponent(name, model, img));
            } else {
                GTMod.GT_FML_LOGGER
                    .warn("[GTNH-VoxelArmor] Skipping armor component '{}' due to missing or invalid files", name);
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
