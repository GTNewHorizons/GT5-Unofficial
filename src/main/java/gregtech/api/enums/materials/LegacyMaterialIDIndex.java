package gregtech.api.enums.materials;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.material.GTMaterialProperties;

/// The legacy IDs the materials had, maps to [GTMaterialProperties#OLD_SUB_ID].
public class LegacyMaterialIDIndex {

    private static final Material[] INDEX = new Material[1000];
    private static int size;

    private LegacyMaterialIDIndex() {}

    public static void init() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            Integer id = material.getProperty(GTMaterialProperties.OLD_SUB_ID);
            if (id == null) continue;
            if (id < 0 || id >= INDEX.length) {
                throw new IllegalStateException(
                    "OLD_SUB_ID " + id + " of " + material.getName() + " is outside the 1000-slot id space");
            }
            if (INDEX[id] != null) {
                throw new IllegalStateException(
                    "OLD_SUB_ID " + id
                        + " of "
                        + material.getName()
                        + " is already occupied by "
                        + INDEX[id].getName());
            }
            INDEX[id] = material;
            size++;
        }
        GTMod.GT_FML_LOGGER.info("LegacyMaterialIDIndex populated {} id slots", size);
    }

    /// The material occupying a legacy id slot; null for an empty slot or an out-of-range id.
    public static @Nullable Material get(int id) {
        return id < 0 || id >= INDEX.length ? null : INDEX[id];
    }

    /// The number of occupied slots.
    public static int size() {
        return size;
    }
}
