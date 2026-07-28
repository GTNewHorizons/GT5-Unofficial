package gregtech.api.enums.materials2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

/// The gas-conditional arc-smelting table as declared [Material]-keyed rows: material -> (gas -> result). The
/// data declares exactly two rows -- `Copper` and the `AnyCopper` wildcard marker each arc-smelt into
/// `AnnealedCopper` under `Oxygen`.
public class Materials2ArcSmelting {

    private static Map<Material, Map<Material, Material>> table;

    private Materials2ArcSmelting() {}

    /// The (gas -> result) mapping for `material`, or an empty map when it has no gas-conditional
    /// arc-smelting recipe.
    public static Map<Material, Material> withGas(@Nullable Material material) {
        if (material == null) return Collections.emptyMap();
        return table().getOrDefault(material, Collections.emptyMap());
    }

    private static Map<Material, Map<Material, Material>> table() {
        if (table == null) {
            if (Materials2Materials.Copper == null || Materials2Markers.AnyCopper == null) {
                throw new IllegalStateException("Gas-arc table consulted before Materials2.init");
            }
            Map<Material, Map<Material, Material>> rows = new LinkedHashMap<>();
            rows.put(
                Materials2Materials.Copper,
                Map.of(Materials2Materials.Oxygen, Materials2Materials.AnnealedCopper));
            rows.put(
                Materials2Markers.AnyCopper,
                Map.of(Materials2Materials.Oxygen, Materials2Materials.AnnealedCopper));
            table = rows;
        }
        return table;
    }
}
