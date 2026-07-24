package gregtech.api.enums.materials2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Materials;
import gregtech.api.material.MU;

/// The gas-conditional arc-smelting table (`Materials#mArcSmeltIntoWithGas`) as declared [Material]-keyed
/// rows: material -> (gas -> result). The legacy data declares exactly two rows -- `Copper` and the
/// `AnyCopper` wildcard marker each arc-smelt into `AnnealedCopper` under `Oxygen`
/// (`gregtech.loaders.materials.LegacyMaterials#build`'s Copper special case and
/// `gregtech.loaders.materials.LegacyMarkerMaterials`'s `AnyCopper` builder). The legacy map's value passes
/// through the target's own `mArcSmeltInto`, a self-reference for `AnnealedCopper`, so the transcription is
/// the literal declared pair.
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

    /// Verifies the declared table against the live legacy `mArcSmeltIntoWithGas` maps: every facade constant
    /// carrying a non-empty map must match a declared row pair-for-pair, and no declared row may lack a live
    /// counterpart. Throws [IllegalStateException] on any mismatch. Requires both the MaterialLib registry and
    /// the legacy facade to be fully built, so it can only run at boot.
    static void verifyAgainstLegacy() {
        Map<Material, Map<Material, Material>> live = new LinkedHashMap<>();
        for (Materials facade : Materials.getAll()) {
            if (facade.mArcSmeltIntoWithGas.isEmpty()) continue;
            Map<Material, Material> pairs = new LinkedHashMap<>();
            for (Map.Entry<Materials, Materials> entry : facade.mArcSmeltIntoWithGas.entrySet()) {
                pairs.put(MU.material(entry.getKey()), MU.material(entry.getValue()));
            }
            live.put(MU.material(facade), pairs);
        }
        if (!live.equals(table())) {
            throw new IllegalStateException(
                "Declared gas-arc table does not match the live legacy maps: declared " + describe(table())
                    + ", live "
                    + describe(live));
        }
    }

    private static String describe(Map<Material, Map<Material, Material>> rows) {
        StringBuilder text = new StringBuilder("{");
        for (Map.Entry<Material, Map<Material, Material>> row : rows.entrySet()) {
            if (text.length() > 1) text.append(", ");
            text.append(name(row.getKey()))
                .append("=[");
            boolean first = true;
            for (Map.Entry<Material, Material> pair : row.getValue()
                .entrySet()) {
                if (!first) text.append(", ");
                first = false;
                text.append(name(pair.getKey()))
                    .append("->")
                    .append(name(pair.getValue()));
            }
            text.append("]");
        }
        return text.append("}")
            .toString();
    }

    private static String name(@Nullable Material material) {
        return material == null ? "null" : MU.internalName(material);
    }
}
