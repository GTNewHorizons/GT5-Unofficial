package gregtech.api.material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// Resolves a material's items from the MaterialLib [Shape] that backs them.
///
/// [MaterialLibAPI#getStack] throws for a material that does not generate the shape, which is the wrong
/// contract for most of GregTech: a missing part is a data condition recipe generation is expected to skip,
/// not a fault. [#stack] guards with [Material#hasShape] and answers null instead; [#require] is the
/// throwing form, for a caller whose material came *from* the shape's own membership and where a miss is a
/// bug.
///
/// The guard is exact rather than conservative: `ShapeRegistry` binds each shape's served materials by
/// routing every entry of [Material#getShapes] through the same canonical-shape mapping [MaterialLibAPI#
/// getStack] applies before its own membership check, so `hasShape` cannot report true where `getStack`
/// would throw.
///
/// Amounts are `long` because that is what GregTech's recipe code carries; MaterialLib takes an `int`.
///
/// The legacy [OrePrefixes]-keyed entry points remain for the callers that hold a prefix at runtime rather
/// than a shape -- the save-migration transformers and the prefix-domain registration loops -- and for the
/// ore-dictionary ingredients, whose material amounts and secondary materials still live on the prefix.
public class MaterialParts {

    private MaterialParts() {}

    /// The stack of `material` in `shape` at `amount`, or null when either is absent or the material does not
    /// generate the shape.
    public static @Nullable ItemStack stack(Shape shape, @Nullable Material material, long amount) {
        if (shape == null || material == null || !material.hasShape(shape)) return null;
        return MaterialLibAPI.getStack(material, shape, (int) amount);
    }

    /// [#stack] for a material already known to generate `shape` -- MaterialLib's own throw is left in place,
    /// so a miss surfaces rather than silently dropping whatever was being built.
    public static ItemStack require(Shape shape, Material material, long amount) {
        return MaterialLibAPI.getStack(material, shape, (int) amount);
    }

    /// A material's full cell, falling back to `cellMolten` when it carries no plain `cell`: a gtPlusPlus
    /// material whose single fluid claimed the molten shape rather than a liquid or gas slot holds its cell
    /// only under `cellMolten`.
    public static @Nullable ItemStack cell(@Nullable Material material, long amount) {
        ItemStack cell = stack(Materials2CellShapes.cell, material, amount);
        return cell != null ? cell : stack(Materials2CellShapes.cellMolten, material, amount);
    }

    /// A material's plasma cell. The two plasma cell shapes share the `cellPlasma` oredict prefix and differ
    /// only in volume, and membership is a per-material choice -- 73 materials take the full-size shape and
    /// 51 the light one, with no overlap -- so naming either statically is wrong for the other half.
    public static @Nullable ItemStack plasmaCell(@Nullable Material material, long amount) {
        ItemStack plasma = stack(Materials2CellShapes.cellPlasma, material, amount);
        return plasma != null ? plasma : stack(Materials2CellShapes.cellPlasmaLight, material, amount);
    }

    private static Map<String, List<Shape>> prefixToShapes;

    /// The MaterialLib shape a legacy item [OrePrefixes] cuts over to, or null if that prefix is not part of
    /// the cutover (e.g. a container prefix with no MaterialLib shape). For a multi-candidate prefix (`cellPlasma`,
    /// `pipeTiny`..`pipeHuge`), the shape a specific material actually generates may differ -- see [#stack];
    /// callers that must see every candidate use [#shapes].
    public static @Nullable Shape shape(OrePrefixes prefix) {
        if (prefix == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        return shapes == null ? null : shapes.get(0);
    }

    /// Every candidate shape a legacy [OrePrefixes] cuts over to, in resolution order ([#stack] uses the
    /// first one a material generates); empty when the prefix is not part of the cutover.
    public static List<Shape> shapes(OrePrefixes prefix) {
        if (prefix == null) return Collections.emptyList();
        List<Shape> shapes = prefixShapes().get(prefix.name());
        return shapes == null ? Collections.emptyList() : shapes;
    }

    /// The cutover MaterialLib stack for a (prefix, material) pair, or null when either side has no cutover
    /// mapping. When a prefix maps to more than one candidate shape (`cellPlasma`), the first one `material`
    /// actually generates is used.
    public static @Nullable ItemStack stack(OrePrefixes prefix, @Nullable Material material, long amount) {
        if (prefix == null || material == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        if (shapes == null) return null;
        for (Shape shape : shapes) {
            ItemStack stack = MaterialParts.stack(shape, material, amount);
            if (stack != null) return stack;
        }
        return null;
    }

    /// Whether a (prefix, material) pair has a MaterialLib equivalent (see [#stack]). Unlike [#shape], which
    /// answers whether a prefix has cut over at all, this answers per material -- needed because a
    /// fluid-in-container shape's membership does not always mirror every material with a real legacy slot: a
    /// material can hold a legacy `cell` item generated purely from its `CELL` capability flag while never
    /// having a fluid to put in it (MaterialLib's container contract requires a material to also generate one
    /// of the container's fluid shapes, so such a material is left off `cell`'s membership and keeps its
    /// legacy item instead). Legacy construction code should skip a (prefix, material) pair exactly when this
    /// is true, not merely when [#shape] is non-null.
    public static boolean isCutOver(OrePrefixes prefix, @Nullable Material material) {
        return stack(prefix, material, 1) != null;
    }

    /// Whether `stack`'s unification association ([GTOreDictUnificator#getAssociation]) names `material` as
    /// its primary material, compared by identity.
    public static boolean isPartOf(@Nullable ItemStack stack, @Nullable Material material) {
        if (material == null) return false;
        ItemData association = GTOreDictUnificator.getAssociation(stack);
        return association != null && association.mMaterial.mMaterial == material;
    }

    /// The crafting-table ingredient for `prefix` and `material`, built directly from the MaterialLib
    /// [Material]. Returns the [ItemData] that [gregtech.api.util.GTModHandler#addCraftingRecipe] resolves
    /// to an ore-dictionary name (through [ItemData#toString]) so the ingredient still accepts any matching
    /// item, while also carrying the material association that drives a reversible recipe's auto-generated
    /// recycling recipes. A bare ore-dictionary [String] ingredient supplies only the name, not that
    /// association, so a reversible recipe built from one silently loses its recycling; this preserves
    /// both. Null when `prefix` or `material` is null. Callers whose ingredient should not carry a
    /// material association -- e.g. a marker material such as a superconductor wire that unifies under its
    /// own name without being composed of it -- use [#namedIngredient] instead.
    public static @Nullable ItemData craftIngredient(OrePrefixes prefix, @Nullable Material material) {
        return prefix == null || material == null ? null : new ItemData(prefix, material);
    }

    /// [#craftIngredient(OrePrefixes,Material)] without the material association: the [ItemData] only names
    /// the ore-dictionary entry, so a reversible recipe derives no recycling output from this ingredient.
    /// The superconductor marker ingredients use this form -- their wires unify under the marker name but
    /// are not composed of the marker.
    public static @Nullable ItemData namedIngredient(OrePrefixes prefix, @Nullable Material material) {
        return prefix == null || material == null ? null : new ItemData(prefix, MaterialUtils.internalName(material));
    }

    /// Whether a [Material] generates `prefix` -- either through gregtech's own part autogen (see
    /// [OrePrefixes#doGenerateItem(Material)]) or the werkstoff part set (see
    /// [Materials2WerkstoffIndex#generatesPrefix]). False for a null material.
    public static boolean generatesPrefix(@Nullable Material material, OrePrefixes prefix) {
        if (material == null) return false;
        return prefix.doGenerateItem(material) || Materials2WerkstoffIndex.generatesPrefix(material, prefix);
    }

    /// The ore-dictionary-unified [ItemStack] for a [Material] at `prefix` and `amount` (see
    /// [GTOreDictUnificator#get]); null for a null material.
    public static @Nullable ItemStack partOf(@Nullable Material material, OrePrefixes prefix, int amount) {
        return material == null ? null : GTOreDictUnificator.get(prefix, material, amount);
    }

    /// The shapes serving each ore-dictionary prefix, keyed by the prefix strings the shapes themselves
    /// declare. Where several shapes share a prefix (`cellPlasma` holds both plasma cell sizes, and a fluid
    /// pipe shares `pipeTiny`..`pipeHuge` with an item pipe), the shape whose own name is the prefix comes
    /// first and the rest follow in name order, so the candidate order does not depend on field or reflection
    /// order.
    private static Map<String, List<Shape>> prefixShapes() {
        if (prefixToShapes == null) {
            Map<String, List<Shape>> map = new HashMap<>();
            collectShapes(map, Materials2Shapes.class);
            collectShapes(map, Materials2CellShapes.class);
            collectShapes(map, Materials2BlockShapes.class);
            collectShapes(map, Materials2OreShapes.class);
            collectShapes(map, Materials2PipeShapes.class);
            for (Map.Entry<String, List<Shape>> entry : map.entrySet()) {
                String prefix = entry.getKey();
                entry.getValue()
                    .sort(
                        Comparator.comparing(
                            (Shape shape) -> !shape.getName()
                                .equals(prefix))
                            .thenComparing(Shape::getName));
            }
            prefixToShapes = map;
        }
        return prefixToShapes;
    }

    private static void collectShapes(Map<String, List<Shape>> map, Class<?> shapesClass) {
        for (Field field : shapesClass.getFields()) {
            if (field.getType() != Shape.class) continue;
            Shape shape = readStatic(field);
            if (shape == null) continue;
            for (String oreDict : shape.getOreDicts()) {
                map.computeIfAbsent(oreDict, k -> new ArrayList<>())
                    .add(shape);
            }
        }
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
