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

/// Bridges legacy [OrePrefixes] to their cutover MaterialLib [Shape]/[Material] equivalents.
///
/// The prefix-to-shape map reflects [Materials2Shapes]'s, [Materials2CellShapes]'s, [Materials2BlockShapes]'s,
/// [Materials2OreShapes]'s, and [Materials2PipeShapes]'s [Shape] fields (each named identically to the
/// [OrePrefixes] it cuts over to) instead of hand-listing the cutover prefixes, so it always matches whatever
/// those declare. A prefix normally maps to exactly one shape; the exceptions map to an ordered candidate list
/// that [#stack] resolves per material: `cellPlasma` (see [Materials2CellShapes]), and the five
/// `pipeTiny`..`pipeHuge` prefix strings, which the fluid and item pipe families share for disjoint material
/// sets (see [Materials2PipeShapes] -- the item shapes' field names deliberately differ from the prefix names,
/// so they and the `pipeRestrictive*` item shapes are folded under their prefix keys explicitly).
///
/// A material's own data is read through [MaterialUtils].
public class MU {

    private static Map<String, List<Shape>> prefixToShapes;

    private MU() {}

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
            if (material.hasShape(shape)) return MaterialLibAPI.getStack(material, shape, (int) amount);
        }
        return null;
    }

    /// A material's `cell` item, falling back to `cellMolten` when the plain `cell` shape does not resolve --
    /// unlike `cellPlasma` (whose [#stack] candidate list already includes `cellPlasmaLight`), `cell` has no
    /// built-in fallback: a gtpp material whose single fluid claimed [Materials2FluidShapes#fluidMolten]
    /// instead of a liquid/gas cell-eligible slot carries its full cell only under `cellMolten`.
    public static @Nullable ItemStack cellStack(@Nullable Material material, long amount) {
        ItemStack cell = stack(OrePrefixes.cell, material, amount);
        return cell != null ? cell : stack(OrePrefixes.cellMolten, material, amount);
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
