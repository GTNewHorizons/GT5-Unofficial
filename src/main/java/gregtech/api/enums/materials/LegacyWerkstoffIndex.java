package gregtech.api.enums.materials;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTLoggers;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialProperties;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/// Akin to [LegacyMaterialIDIndex], but for old Werkstoff materials.
public class LegacyWerkstoffIndex {

    private static final Int2ObjectMap<Material> INDEX = new Int2ObjectOpenHashMap<>();
    private static int size;

    private LegacyWerkstoffIndex() {}

    public static void init() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            List<Integer> ids = material.getProperty(GTMaterialProperties.WERKSTOFF_IDS);
            if (ids == null) continue;
            for (int id : ids) {
                occupy(id, material);
            }
        }

        // Bartworks' gt-bridge werkstoffe (id = 31766 + the legacy sub id) belong to no material's WERKSTOFF_IDS,
        // but saved casings and parts still address them.
        occupy(31776, Materials.Carbon);
        occupy(31820, Materials.Silver);
        occupy(31850, Materials.Iridium);
        occupy(32083, Materials.Osmiridium);
        occupy(32090, Materials.Naquadah);
        occupy(32091, Materials.NaquadahAlloy);
        occupy(32100, Materials.BlackSteel);
        occupy(32237, Materials.Magnesia);
        occupy(32575, Materials.Wood);

        GTLoggers.GT_FML_LOGGER.info("LegacyWerkstoffIndex populated {} id slots", size);
    }

    private static void occupy(int id, Material material) {
        Material prev = INDEX.get(id);
        if (prev != null) {
            throw new IllegalStateException(
                "Werkstoff id " + id + " of " + material.getName() + " is already occupied by " + prev.getName());
        }
        INDEX.put(id, material);
        size++;
    }

    /// The material occupying a legacy werkstoff id slot; null for an unoccupied id.
    public static @Nullable Material get(int id) {
        return INDEX.get(id);
    }

    /// The first legacy werkstoff id a material carries, or -1 when it carries none.
    public static int idOf(@Nullable Material material) {
        if (material == null) return -1;
        List<Integer> ids = material.getProperty(GTMaterialProperties.WERKSTOFF_IDS);
        return ids == null || ids.isEmpty() ? -1 : ids.get(0);
    }

    /// Whether [GTMaterialProperties#WERKSTOFF_PREFIXES] lists `prefix` for this material. False for a material
    /// that also carries [GTMaterialProperties#OLD_SUB_ID], since GregTech owns its parts.
    public static boolean generatesPrefix(@Nullable Material material, OrePrefixes prefix) {
        if (material == null || material.getProperty(GTMaterialProperties.OLD_SUB_ID) != null) return false;
        List<String> prefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
        return prefixes != null && prefixes.contains(prefix.name());
    }
}
