package gregtech.api.enums.materials2;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.material.GTMaterialProperties;

/// The legacy 1000-slot generated-material id spine (`GregTechAPI#sGeneratedMaterials`) rebuilt over
/// MaterialLib materials: every registered material carrying [GTMaterialProperties#OLD_SUB_ID] occupies the
/// slot that property names. The property is GT-owned and set only for the legacy id space -- werkstoff and
/// gtPlusPlus materials key their own id spaces ([GTMaterialProperties#WERKSTOFF_IDS],
/// [GTMaterialProperties#GTPP_STATE]) and never appear here -- so the population is exactly the legacy facade
/// set. Populated at the start of GT's preInit -- listing the registry requires MaterialLib to have resolved
/// it, which happens only after every MaterialRegistrationEvent handler (including [Materials2Materials]'s
/// registration) has completed.
public class Materials2IDIndex {

    private static final Material[] INDEX = new Material[1000];
    private static int size;

    private Materials2IDIndex() {}

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
        GTMod.GT_FML_LOGGER.info("Materials2IDIndex populated {} id slots", size);
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
