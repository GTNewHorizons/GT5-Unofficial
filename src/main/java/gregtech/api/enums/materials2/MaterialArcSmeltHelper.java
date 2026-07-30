package gregtech.api.enums.materials2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

/// The gas-conditional arc-smelting table as declared [Material]-keyed rows: material -> (gas -> result). The
/// data declares exactly two rows -- `Copper` and the `AnyCopper` wildcard marker each arc-smelt into
/// `AnnealedCopper` under `Oxygen`.
public class MaterialArcSmeltHelper {

    private static Map<Material, Map<Material, Material>> table;

    private MaterialArcSmeltHelper() {}

    /// The (gas -> result) mapping for `material`, or an empty map when it has no gas-conditional
    /// arc-smelting recipe.
    public static Map<Material, Material> withGas(@Nullable Material material) {
        if (material == null) return Collections.emptyMap();
        return table().getOrDefault(material, Collections.emptyMap());
    }

    private static Map<Material, Map<Material, Material>> table() {
        if (table == null) {
            if (Materials.Copper == null || MaterialFacades.AnyCopper == null) {
                throw new IllegalStateException("Gas-arc table consulted before MaterialSystem.init");
            }
            Map<Material, Map<Material, Material>> rows = new LinkedHashMap<>();
            rows.put(
                Materials.Copper,
                Map.of(Materials.Oxygen, Materials.AnnealedCopper));
            rows.put(
                MaterialFacades.AnyCopper,
                Map.of(Materials.Oxygen, Materials.AnnealedCopper));
            table = rows;
        }
        return table;
    }
}
