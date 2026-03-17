package gregtech.api.materials.bec;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.gtnewhorizon.gtnhlib.color.ImmutableColor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class BECMaterialList {

    public static final BECMaterial THERMAL_CONT_1 = new BECMaterial(
        BECMaterialIds.THERMAL_CONT_1.id,
        "ThermalCont1",
        Arrays.asList(PartOrePrefix.plate));

    public static void init() {
        BECMaterial.ITEM.register();

        if (GTUtility.isClient()) {
            loadResources();
            BECResourceHotswapper.init();
        }
    }

    private static final Gson GSON = new GsonBuilder().create();

    @SideOnly(Side.CLIENT)
    public static void loadResources() {
        Map<String, MatResource> mats = GTDataUtils.loadResourceMerged(
            GSON,
            String.class,
            MatResource.class,
            Mods.GregTech.getResourceLocation("misc/materials/bec-materials.json"));

        BECTextureSet.startMaterialReload();

        for (var e : mats.entrySet()) {
            BECMaterial mat = BECMaterial.MATERIALS_BY_NAME.get(e.getKey());

            if (mat == null) {
                GTMod.GT_FML_LOGGER
                    .error("Could not find BEC material {}, which is referred to in a bec-materials.json", e.getKey());
                continue;
            }

            mat.textureSet = BECTextureSet.getTextureSet(e.getValue().textureSet);

            mat.palettesByPrefix.clear();

            for (PartOrePrefix prefix : PartOrePrefix.VALUES) {
                String palettePath = e.getValue().prefixPalettes.getOrDefault(prefix, e.getValue().palette);

                if (palettePath == null || palettePath.isEmpty()) {
                    GTMod.GT_FML_LOGGER.warn(
                        "Colour palette for material {} (prefix {}) was undefined: it will be skipped",
                        mat.name,
                        prefix);
                    continue;
                }

                palettePath = Paths.get("misc/materials", palettePath)
                    .normalize()
                    .toString()
                    .replace('\\', '/');

                ResourceLocation paletteLocation = Mods.GregTech.getResourceLocation(palettePath);

                Int2ObjectMap<ImmutableColor> palette = IndexedIcon.loadPalette(paletteLocation);

                if (palette == null) {
                    GTMod.GT_FML_LOGGER.warn(
                        "Could not load palette {} for material {} (prefix {})",
                        paletteLocation,
                        mat.name,
                        prefix);
                    continue;
                }

                mat.palettesByPrefix.put(prefix, palette);
            }
        }

        BECTextureSet.endMaterialReload();
    }

    private static class MatResource {

        @SerializedName("texture-set")
        public String textureSet;

        public String palette;

        @SerializedName("prefix-palettes")
        public Map<PartOrePrefix, String> prefixPalettes = Collections.emptyMap();
    }
}
