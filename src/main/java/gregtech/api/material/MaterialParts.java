package gregtech.api.material;

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
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.GTShapeStore;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
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
/// The ore-dictionary ingredient builders live here too: their material amounts and secondary materials live
/// on the prefix, not the shape.
///
/// Amounts are `long` because that is what GregTech's recipe code carries; MaterialLib takes an `int`.
///
/// The membership checks below read [Material#hasShape], which tests a material's declared shape instances by
/// identity; `ShapeItem#getServedMaterials` is the canonical-safe alternative for iterating a shape's
/// materials.
public class MaterialParts {

    private MaterialParts() {}

    /// A material's full cell, resolving `cell`, then `cellGas`, then `cellMolten`: a gas material holds its
    /// plain cell under `cellGas` (see [CellShapes]), and a gtPlusPlus material whose single fluid claimed the
    /// molten shape rather than a liquid or gas slot holds its cell only under `cellMolten`. Null when the
    /// material carries none of the three, or is itself null.
    public static @Nullable ItemStack cell(@Nullable Material material, long amount) {
        if (material == null) return null;
        return firstShapeStack(material, amount, CellShapes.cell, CellShapes.cellGas, CellShapes.cellMolten);
    }

    /// The throwing counterpart of [#cell] for call sites declaring that a specific material must carry a
    /// plain cell. Throws [IllegalStateException] naming the material when it carries none.
    public static ItemStack requireCell(Material material, long amount) {
        ItemStack stack = firstShapeStack(material, amount, CellShapes.cell, CellShapes.cellGas);
        if (stack == null) throw new IllegalStateException(material.getName() + " carries no plain cell shape");
        return stack;
    }

    /// The stack for the first of `candidates` that `material` generates, or null when it generates none.
    private static @Nullable ItemStack firstShapeStack(Material material, long amount, Shape... candidates) {
        for (Shape shape : candidates) {
            if (material.hasShape(shape)) return MaterialLibAPI.getStack(material, shape, (int) amount);
        }
        return null;
    }

    private static Map<String, List<Shape>> prefixToShapes;

    /// The MaterialLib shape a legacy item [OrePrefixes] cuts over to, or null if that prefix is not part of
    /// the cutover (e.g. a container prefix with no MaterialLib shape). For a multi-candidate prefix (`cellPlasma`,
    /// `pipeTiny`..`pipeHuge`), the shape a specific material actually generates may differ -- see [#stack];
    /// callers that must see every candidate use [#shapes].
    public static @Nullable Shape shape(OrePrefixes prefix) {
        List<Shape> shapes = shapes(prefix);
        return shapes.isEmpty() ? null : shapes.get(0);
    }

    /// Every candidate shape a legacy [OrePrefixes] cuts over to, in resolution order ([#stack] uses the
    /// first one a material generates); empty when the prefix is not part of the cutover.
    public static List<Shape> shapes(OrePrefixes prefix) {
        if (prefix == null) return Collections.emptyList();
        List<Shape> shapes = prefixShapes().get(prefix.name());
        return shapes == null ? Collections.emptyList() : shapes;
    }

    /// The MaterialLib stack backing a legacy (prefix, material) pair, or null when the pair has no
    /// MaterialLib backing -- either the prefix declares no shape at all, or this particular material generates
    /// none of the prefix's candidate shapes.
    ///
    /// When a prefix maps to more than one candidate shape (`cellPlasma`, `pipeTiny`..`pipeHuge`), the first
    /// one `material` generates wins; [#shapes] exposes the full candidate list.
    public static @Nullable ItemStack stack(OrePrefixes prefix, @Nullable Material material, long amount) {
        Shape shape = servedShape(prefix, material);
        return shape == null ? null : MaterialLibAPI.getStack(material, shape, (int) amount);
    }

    /// Whether a (prefix, material) pair has a MaterialLib equivalent (see [#stack]). Unlike [#shape], which
    /// answers whether a prefix has cut over at all, this answers per material: a material can hold a legacy
    /// `cell` item generated from its `CELL` capability flag while never having a fluid to put in it, which
    /// leaves it off `cell`'s MaterialLib membership.
    public static boolean isCutOver(OrePrefixes prefix, @Nullable Material material) {
        return servedShape(prefix, material) != null;
    }

    /// The first of `prefix`'s candidate shapes that `material` generates, or null when it generates none.
    private static @Nullable Shape servedShape(OrePrefixes prefix, @Nullable Material material) {
        if (material == null) return null;
        for (Shape shape : shapes(prefix)) {
            if (material.hasShape(shape)) return shape;
        }
        return null;
    }

    /// Whether `stack`'s unification association ([GTOreDictUnificator#getAssociation]) names `material` as
    /// its primary material, compared by identity.
    public static boolean isPartOf(@Nullable ItemStack stack, @Nullable Material material) {
        if (material == null) return false;
        ItemData association = GTOreDictUnificator.getAssociation(stack);
        return association != null && association.mMaterial.mMaterial == material;
    }

    /// The crafting-table ingredient for `prefix` and `material`. The returned [ItemData] resolves to an
    /// ore-dictionary name through [ItemData#toString], so the ingredient accepts any matching item, and also
    /// carries the material association a reversible recipe's auto-generated recycling needs. Null when
    /// `prefix` or `material` is null. Use [#namedIngredient] for an ingredient that must not carry an
    /// association.
    public static @Nullable ItemData craftIngredient(OrePrefixes prefix, @Nullable Material material) {
        return prefix == null || material == null ? null : new ItemData(prefix, material);
    }

    /// [#craftIngredient(OrePrefixes,Material)] without the material association: the [ItemData] only names
    /// the ore-dictionary entry, so a reversible recipe derives no recycling output from this ingredient.
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
    /// first and the rest follow in name order, so the candidate order does not depend on declaration order.
    private static Map<String, List<Shape>> prefixShapes() {
        if (prefixToShapes == null) {
            if (GTShapeStore.all()
                .isEmpty()) {
                throw new IllegalStateException("Prefix-shape table consulted before MaterialSystem.init");
            }
            Map<String, List<Shape>> map = new HashMap<>();
            for (Shape shape : GTShapeStore.all()) {
                for (String oreDict : shape.getOreDicts()) {
                    map.computeIfAbsent(oreDict, k -> new ArrayList<>())
                        .add(shape);
                }
            }
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
}
