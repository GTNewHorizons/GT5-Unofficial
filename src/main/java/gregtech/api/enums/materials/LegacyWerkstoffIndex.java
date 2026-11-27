package gregtech.api.enums.materials;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTLoggers;
import gregtech.api.material.GTMaterialProperties;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/// The material occupying each werkstoff id addressed by saved bartworks blocks and items, built from every
/// material's [GTMaterialProperties#WERKSTOFF_IDS]. The werkstoff counterpart of [LegacyMaterialIDIndex].
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

    /// Returns the material occupying a legacy werkstoff id slot, or null for an unoccupied id.
    public static @Nullable Material get(int id) {
        return INDEX.get(id);
    }

    /// Returns the first legacy werkstoff id a material carries, or -1 when it carries none.
    public static int idOf(@Nullable Material material) {
        if (material == null) return -1;
        List<Integer> ids = material.getProperty(GTMaterialProperties.WERKSTOFF_IDS);
        return ids == null || ids.isEmpty() ? -1 : ids.get(0);
    }
}
