package gregtech.api.enums.materials2;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.material.GTMaterialProperties;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/// The legacy bartworks `Werkstoff` id space rebuilt over MaterialLib materials: every registered material
/// carrying [GTMaterialProperties#WERKSTOFF_IDS] is registered under every id its list names -- a material can
/// cover several legacy ids (two same-name werkstoffe folded into one MaterialLib declaration), unlike
/// [Materials2IDIndex]'s one-id-per-material legacy spine. The property is bartworks-owned data (see its
/// javadoc) but this index itself is gregtech-owned: save games store these ids forever, so the decoding must
/// outlive `bartworks.system.material.Werkstoff`.
///
/// Werkstoff ids are not densely packed like [Materials2IDIndex]'s 1000-slot space -- bartworks' own pools run
/// roughly 1..500, goodgenerator ids start at 10001, gtnhlanth ids at 11000 -- so an [Int2ObjectOpenHashMap]
/// backs the index rather than a fixed array. Populated at the start of GT's preInit -- listing the registry
/// requires MaterialLib to have resolved it, which happens only after every MaterialRegistrationEvent handler
/// has completed.
public class Materials2WerkstoffIndex {

    private static final Int2ObjectMap<Material> INDEX = new Int2ObjectOpenHashMap<>();
    private static int size;

    private Materials2WerkstoffIndex() {}

    public static void init() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            List<Integer> ids = material.getProperty(GTMaterialProperties.WERKSTOFF_IDS);
            if (ids == null) continue;
            for (int id : ids) {
                Material prev = INDEX.get(id);
                if (prev != null) {
                    throw new IllegalStateException(
                        "WERKSTOFF_IDS id " + id
                            + " of "
                            + material.getName()
                            + " is already occupied by "
                            + prev.getName());
                }
                INDEX.put(id, material);
                size++;
            }
        }
        GTMod.GT_FML_LOGGER.info("Materials2WerkstoffIndex populated {} id slots", size);
    }

    /// The material occupying a legacy werkstoff id slot; null for an unoccupied id.
    public static @Nullable Material get(int id) {
        return INDEX.get(id);
    }

    /// The number of occupied slots.
    public static int size() {
        return size;
    }
}
