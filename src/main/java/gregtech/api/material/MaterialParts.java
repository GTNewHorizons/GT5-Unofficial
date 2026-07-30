package gregtech.api.material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gregtech.api.enums.materials2.LegacyWerkstoffIndex;
import gregtech.api.enums.materials2.OreShapes;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.BlockShapes;
import gregtech.api.enums.materials2.CellShapes;
import gregtech.api.enums.materials2.PipeShapes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The bridge between GregTech's legacy [OrePrefixes] domain and MaterialLib's [Shape] domain.
///
/// Code holding a shape calls [MaterialLibAPI#getStack] directly and lets it throw: both sides of a
/// (material, shape) pair are explicitly declared, so a call site naming a pair the material does not
/// generate is a declaration bug, not a data condition. This class serves the callers that do *not* hold a
/// shape -- a prefix read from a saved NBT tag, a werkstoff prefix list, an ore-dictionary name -- where the
/// prefix domain is strictly wider than the shape domain and "no MaterialLib backing for this pair" is an
/// expected answer with a defined legacy behaviour on the other side. Those entry points answer null, and
/// each caller decides what null means for it.
///
/// The ore-dictionary ingredient builders live here too: their material amounts and secondary materials
/// still live on the prefix rather than the shape.
///
/// Amounts are `long` because that is what GregTech's recipe code carries; MaterialLib takes an `int`.
///
/// The membership checks below read [Material#hasShape], which tests the material's *declared* shape
/// instances by identity, while [MaterialLibAPI#getStack] canonicalizes first. The two can only disagree for
/// a material declared with a foreign mod's same-named shape alias; every material here is declared with
/// gregtech's own `MaterialSystem*Shapes` constants. `ShapeItem#getServedMaterials` is the canonical-safe
/// alternative when iterating a shape's materials rather than testing one material.
public class MaterialParts {

    private MaterialParts() {}

    /// A material's full cell, falling back to `cellMolten` when it carries no plain `cell`: a gtPlusPlus
    /// material whose single fluid claimed the molten shape rather than a liquid or gas slot holds its cell
    /// only under `cellMolten`. Null when the material carries neither, or is itself null.
    ///
    /// This exists for the callers resolving an arbitrary material at runtime -- a save-migration table row, a
    /// composition component. A caller that knows its material statically names the shape and calls
    /// [MaterialLibAPI#getStack] directly, so the recipe says which of the two items it means.
    /// [gregtech.loaders.oreprocessing.ProcessingDustGeneration#stackOf]'s ore-dictionary fallback is
    /// deliberately not used here: for a material with no plain `cell` shape it resolves the legacy gtPlusPlus
    /// cell item rather than `cellMolten`.
    public static @Nullable ItemStack cell(@Nullable Material material, long amount) {
        if (material == null) return null;
        if (material.hasShape(CellShapes.cell)) {
            return MaterialLibAPI.getStack(material, CellShapes.cell, (int) amount);
        }
        if (material.hasShape(CellShapes.cellMolten)) {
            return MaterialLibAPI.getStack(material, CellShapes.cellMolten, (int) amount);
        }
        return null;
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

    /// The MaterialLib stack backing a legacy (prefix, material) pair, or **null when the pair has no
    /// MaterialLib backing** -- either the prefix declares no shape at all (a container or tool prefix outside
    /// the cutover) or this particular material generates none of the prefix's candidate shapes. Null is a
    /// normal answer, not a fault: every caller reaching here holds a prefix that arrived at runtime and has a
    /// defined legacy behaviour for the un-backed case (skip the migration, fall back to the unificator, keep
    /// the legacy block). A caller holding a [Shape] statically calls [MaterialLibAPI#getStack] instead and
    /// lets a wrong pair throw.
    ///
    /// When a prefix maps to more than one candidate shape (`cellPlasma`, `pipeTiny`..`pipeHuge`), the first
    /// one `material` generates wins; [#shapes] exposes the full candidate list.
    public static @Nullable ItemStack stack(OrePrefixes prefix, @Nullable Material material, long amount) {
        if (prefix == null || material == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        if (shapes == null) return null;
        for (Shape shape : shapes) {
            if (material.hasShape(shape)) return MaterialLibAPI.getStack(material, shape, (int) amount);
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
        if (prefix == null || material == null) return false;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        if (shapes == null) return false;
        for (Shape shape : shapes) {
            if (material.hasShape(shape)) return true;
        }
        return false;
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
    /// [LegacyWerkstoffIndex#generatesPrefix]). False for a null material.
    public static boolean generatesPrefix(@Nullable Material material, OrePrefixes prefix) {
        if (material == null) return false;
        return prefix.doGenerateItem(material) || LegacyWerkstoffIndex.generatesPrefix(material, prefix);
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
            collectShapes(map, Shapes.class);
            collectShapes(map, CellShapes.class);
            collectShapes(map, BlockShapes.class);
            collectShapes(map, OreShapes.class);
            collectShapes(map, PipeShapes.class);
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
