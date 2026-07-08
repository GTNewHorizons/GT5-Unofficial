package gregtech.api.material;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;

/// Bridges legacy [OrePrefixes]/[Materials] pairs to their stage-05 cutover MaterialLib [Shape]/[Material]
/// equivalents.
///
/// The prefix-to-shape map reflects [Materials2Shapes]'s `shape<PrefixName>` fields instead of hand-listing
/// the cutover prefixes, so it always matches whatever `gen_shapes.py` emits. The material lookup is keyed by
/// legacy name (`Materials#mName`), preferring [GTMaterialProperties#LEGACY_NAME] over
/// [Material#getName] because MaterialLib sanitizes registration names that contain characters
/// `Names#validate` rejects.
public class MU {

    private static Map<String, Shape> prefixToShape;
    private static Map<String, Material> legacyNameToMaterial;

    private MU() {}

    /// The MaterialLib shape a legacy item [OrePrefixes] cuts over to, or null if that prefix is not part of
    /// the stage-05 cutover (e.g. block-kind, container, or complex tool/armor prefixes).
    public static @Nullable Shape shape(OrePrefixes prefix) {
        if (prefix == null) return null;
        return prefixShapes().get(prefix.name());
    }

    /// The MaterialLib material a legacy [Materials] cuts over to, or null if it has none (materials without
    /// a MaterialLib counterpart never had generated items in the legacy system either).
    public static @Nullable Material material(Materials material) {
        if (material == null) return null;
        return legacyNamedMaterials().get(material.mName);
    }

    /// The cutover MaterialLib stack for a legacy (prefix, material) pair, or null when either side has no
    /// cutover mapping.
    public static @Nullable ItemStack stack(OrePrefixes prefix, Materials material, long amount) {
        Shape shape = shape(prefix);
        Material mat = material(material);
        if (shape == null || mat == null) return null;
        return MaterialLibAPI.getStack(mat, shape, (int) amount);
    }

    /// The legacy [Materials] a MaterialLib material was ported from, or null if it has none.
    public static @Nullable Materials materialOf(Material material) {
        if (material == null) return null;
        return Materials.getMaterialsMap()
            .get(legacyName(material));
    }

    private static String legacyName(Material material) {
        String legacyName = material.getProperty(GTMaterialProperties.LEGACY_NAME);
        return legacyName != null ? legacyName : material.getName();
    }

    private static Map<String, Shape> prefixShapes() {
        if (prefixToShape == null) {
            Map<String, Shape> map = new HashMap<>();
            for (Field field : Materials2Shapes.class.getFields()) {
                if (field.getType() != Shape.class || !field.getName()
                    .startsWith("shape")) continue;
                Shape shape = readStatic(field);
                if (shape != null) map.put(
                    Character.toLowerCase(
                        field.getName()
                            .charAt(5))
                        + field.getName()
                            .substring(6),
                    shape);
            }
            prefixToShape = map;
        }
        return prefixToShape;
    }

    private static Map<String, Material> legacyNamedMaterials() {
        if (legacyNameToMaterial == null) {
            Map<String, Material> map = new HashMap<>();
            for (Field field : Materials2Materials.class.getFields()) {
                if (field.getType() != Material.class) continue;
                Material material = readStatic(field);
                if (material != null) map.put(legacyName(material), material);
            }
            legacyNameToMaterial = map;
        }
        return legacyNameToMaterial;
    }

    @SuppressWarnings("unchecked")
    private static <T> T readStatic(Field field) {
        try {
            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
