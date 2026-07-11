package gregtech.api.material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2GtppShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.enums.materials2.Materials2Shapes;

/// Bridges legacy [OrePrefixes]/[Materials] pairs to their stage-05/06/07 cutover MaterialLib [Shape]/[Material]
/// equivalents.
///
/// The prefix-to-shape map reflects [Materials2Shapes]'s, [Materials2CellShapes]'s, [Materials2BlockShapes]'s,
/// [Materials2OreShapes]'s, and [Materials2GtppShapes]'s `shape<PrefixName>` fields instead of hand-listing the
/// cutover prefixes, so it always matches whatever those declare. A prefix
/// normally maps to exactly one shape; `cellPlasma` is the one exception (see [Materials2CellShapes]), mapping
/// to an ordered candidate list that [#stack] resolves per material. The material lookup is keyed by legacy
/// name (`Materials#mName`), preferring [GTMaterialProperties#LEGACY_NAME] over [Material#getName] because
/// MaterialLib sanitizes registration names that contain characters `Names#validate` rejects.
public class MU {

    private static Map<String, List<Shape>> prefixToShapes;
    private static Map<String, Material> legacyNameToMaterial;

    private MU() {}

    /// The MaterialLib shape a legacy item [OrePrefixes] cuts over to, or null if that prefix is not part of
    /// the cutover (e.g. block-kind, or a not-yet-cut-over container prefix). For `cellPlasma`, the shape a
    /// specific material actually generates may differ -- see [#stack].
    public static @Nullable Shape shape(OrePrefixes prefix) {
        if (prefix == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        return shapes == null ? null : shapes.get(0);
    }

    /// The MaterialLib material a legacy [Materials] cuts over to, or null if it has none (materials without
    /// a MaterialLib counterpart never had generated items in the legacy system either).
    public static @Nullable Material material(Materials material) {
        if (material == null) return null;
        return legacyNamedMaterials().get(material.mName);
    }

    /// The cutover MaterialLib stack for a legacy (prefix, material) pair, or null when either side has no
    /// cutover mapping. When a prefix maps to more than one candidate shape (`cellPlasma`), the first one
    /// `material` actually generates is used.
    public static @Nullable ItemStack stack(OrePrefixes prefix, Materials material, long amount) {
        return stack(prefix, material(material), amount);
    }

    /// [#stack] for callers that already hold the MaterialLib [Material] directly instead of a legacy
    /// [Materials] enum constant -- e.g. gtPlusPlus material reconstruction, whose ~200 non-merged materials
    /// have no [Materials] counterpart to look up by.
    public static @Nullable ItemStack stack(OrePrefixes prefix, @Nullable Material material, long amount) {
        if (prefix == null || material == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        if (shapes == null) return null;
        for (Shape shape : shapes) {
            if (material.hasShape(shape)) return MaterialLibAPI.getStack(material, shape, (int) amount);
        }
        return null;
    }

    /// Whether a legacy (prefix, material) pair has a MaterialLib equivalent (see [#stack]). Unlike [#shape],
    /// which answers whether a prefix has cut over at all, this answers per material -- needed because a
    /// fluid-in-container shape's membership does not always mirror every material with a real legacy slot: a
    /// material can hold a legacy `cell` item generated purely from its `CELL` capability flag while never
    /// having a fluid to put in it (MaterialLib's container contract requires a material to also generate one
    /// of the container's fluid shapes, so such a material is left off `shapeCell`'s membership and keeps its
    /// legacy item instead). Legacy construction code should skip a (prefix, material) pair exactly when this
    /// is true, not merely when [#shape] is non-null.
    public static boolean isCutOver(OrePrefixes prefix, Materials material) {
        return stack(prefix, material, 1) != null;
    }

    /// [#isCutOver] for a MaterialLib [Material] held directly -- see [#stack]'s raw-[Material] overload.
    public static boolean isCutOver(OrePrefixes prefix, @Nullable Material material) {
        return stack(prefix, material, 1) != null;
    }

    /// The legacy [Materials] a MaterialLib material was ported from, or null if it has none.
    public static @Nullable Materials materialOf(Material material) {
        if (material == null) return null;
        return Materials.getMaterialsMap()
            .get(legacyName(material));
    }

    /// The legacy `mMetaItemSubID` a material was assigned (block-form metadata index, e.g. the frame and
    /// storage-block variant selector), or -1 if unset -- mirrors legacy `Materials#mMetaItemSubID`'s own
    /// unset default. Ported byte-identically to [GTMaterialProperties#OLD_SUB_ID]; callers reading
    /// block-form metadata (frame tiers, worldgen) use this instead of the legacy field.
    public static int oldSubId(@Nullable Material material) {
        if (material == null) return -1;
        Integer id = material.getProperty(GTMaterialProperties.OLD_SUB_ID);
        return id == null ? -1 : id;
    }

    /// The legacy `Materials#mRGBa`-format `[r, g, b, a]` short array for a material, or null if it has no
    /// [GTMaterialProperties#ARGB] (unported markers). Unpacks the property with the identical shift/mask math
    /// `Materials`'s own constructor uses on the same value -- `LegacyMaterials.build` feeds this exact
    /// property through that constructor via `MaterialBuilder#setARGB`, so this is byte-identical to the
    /// legacy field for every ported material, not merely observationally equal. Unlike
    /// [com.ruling_0.materiallib.api.StandardProperties#TINT], preserves alpha `0x00` (see
    /// [GTMaterialProperties#ARGB]'s javadoc) -- do not substitute this for TINT in ML-side rendering code.
    public static @Nullable short[] rgba(@Nullable Material material) {
        if (material == null) return null;
        Integer argb = material.getProperty(GTMaterialProperties.ARGB);
        if (argb == null) return null;
        return new short[] { (short) ((argb >>> 16) & 0xFF), (short) ((argb >>> 8) & 0xFF), (short) (argb & 0xFF),
            (short) ((argb >>> 24) & 0xFF) };
    }

    /// Whether a material carries a legacy [gregtech.api.enums.SubTag], ported 1:1 to [GTMaterialFlag] of the
    /// same name -- see [GTMaterialProperties#FLAGS]. Mirrors legacy `Materials#contains(SubTag)`/`mSubTags`.
    public static boolean hasFlag(@Nullable Material material, GTMaterialFlag flag) {
        if (material == null) return false;
        EnumSet<GTMaterialFlag> flags = material.getProperty(GTMaterialProperties.FLAGS);
        return flags != null && flags.contains(flag);
    }

    /// [#hasFlag(Material, GTMaterialFlag)] for callers still holding the legacy [Materials] enum constant.
    /// Falls back to a direct legacy `Materials#contains(SubTag)` read when [#material] has no MaterialLib
    /// counterpart -- the ~291 marker materials `LegacyMarkerMaterials` builds directly (e.g. `AnyBronze`,
    /// `AnyCopper`) still carry real legacy SubTags of their own despite never being ML-backed, so treating an
    /// unmapped material as flagless (like the raw [Material] overload does for a genuinely absent property)
    /// would be wrong here. `GTMaterialFlag` names match `SubTag` names 1:1 for every flag this fallback can
    /// reach (mirrors `LegacyMaterials`'s own `legacySubTagName`; the two dynamic bartworks-only exceptions,
    /// `ANAEROBE_GAS`/`NOBLE_GAS`, never apply to a plain `Materials` instance).
    public static boolean hasFlag(@Nullable Materials material, GTMaterialFlag flag) {
        if (material == null) return false;
        Material ml = material(material);
        if (ml != null) return hasFlag(ml, flag);
        return material.contains(SubTag.getNewSubTag(flag.name()));
    }

    private static String legacyName(Material material) {
        String legacyName = material.getProperty(GTMaterialProperties.LEGACY_NAME);
        return legacyName != null ? legacyName : material.getName();
    }

    private static Map<String, List<Shape>> prefixShapes() {
        if (prefixToShapes == null) {
            Map<String, List<Shape>> map = new HashMap<>();
            collectShapes(map, Materials2Shapes.class);
            collectShapes(map, Materials2CellShapes.class);
            collectShapes(map, Materials2BlockShapes.class);
            collectShapes(map, Materials2OreShapes.class);
            collectShapes(map, Materials2GtppShapes.class);
            // cellPlasmaLight is a second candidate shape for the cellPlasma prefix, not a prefix of its own
            // (see Materials2CellShapes); its field name deliberately does not match an OrePrefixes name, so
            // fold it into "cellPlasma"'s candidate list instead of collecting it under its own key.
            if (Materials2CellShapes.shapeCellPlasmaLight != null) {
                map.get("cellPlasma")
                    .add(Materials2CellShapes.shapeCellPlasmaLight);
            }
            prefixToShapes = map;
        }
        return prefixToShapes;
    }

    private static void collectShapes(Map<String, List<Shape>> map, Class<?> shapesClass) {
        for (Field field : shapesClass.getFields()) {
            if (field.getType() != Shape.class || !field.getName()
                .startsWith("shape")) continue;
            Shape shape = readStatic(field);
            if (shape == null) continue;
            String prefixName = Character.toLowerCase(
                field.getName()
                    .charAt(5))
                + field.getName()
                    .substring(6);
            map.computeIfAbsent(prefixName, k -> new ArrayList<>())
                .add(shape);
        }
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
