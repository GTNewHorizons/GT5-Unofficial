package gregtech.api.material;

import static gregtech.api.enums.GTValues.M;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.ShapeBlock;
import com.ruling_0.materiallib.api.ShapeItem;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.GTShapeStore;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTUtility;

/// Bridges GregTech's [OrePrefixes] and MaterialLib's [Shape]s, for callers holding only a prefix, such as one read
/// from NBT or an oredict name. Code that knows its shape calls [MaterialLibAPI] directly. There are more prefixes
/// than shapes, so these lookups answer null for a pair with no MaterialLib backing.
///
/// Also holds GregTech's material-amount rules for turning an amount into dusts or metal.
///
/// Amounts are `long` to match GregTech's recipe code. MaterialLib takes an `int`.
public class MaterialParts {

    private MaterialParts() {}

    /// Returns the material's full cell, the first of `cell`, `cellGas` and `cellMolten` it carries (see
    /// [CellShapes]). Null when `material` is null or carries none of them.
    public static @Nullable ItemStack cell(@Nullable Material material, long amount) {
        if (material == null) return null;
        return firstShapeStack(material, amount, CellShapes.cell, CellShapes.cellGas, CellShapes.cellMolten);
    }

    /// Same as [#cell] without the `cellMolten` fallback. Throws [IllegalStateException] when the material
    /// carries neither `cell` nor `cellGas`.
    public static ItemStack requireCell(Material material, long amount) {
        ItemStack stack = firstShapeStack(material, amount, CellShapes.cell, CellShapes.cellGas);
        if (stack == null) throw new IllegalStateException(material.getName() + " carries no plain cell shape");
        return stack;
    }

    /// Returns the stack for the first of `candidates` that `material` generates, or null when it generates none.
    private static @Nullable ItemStack firstShapeStack(Material material, long amount, Shape... candidates) {
        for (Shape shape : candidates) {
            if (material.hasShape(shape)) return MaterialLibAPI.getStack(material, shape, (int) amount);
        }
        return null;
    }

    private static Map<String, List<Shape>> prefixToShapes;

    /// Returns the first shape serving `prefix`, or null when none does. To get every candidate, see [#shapes].
    public static @Nullable Shape shape(OrePrefixes prefix) {
        List<Shape> shapes = shapes(prefix);
        return shapes.isEmpty() ? null : shapes.get(0);
    }

    /// Returns every shape serving `prefix`, the one named after the prefix first. Empty when none does or
    /// `prefix` is null. The returned list is shared and must not be modified.
    ///
    /// Throws [IllegalStateException] before GregTech's shapes are declared.
    public static List<Shape> shapes(OrePrefixes prefix) {
        if (prefix == null) return Collections.emptyList();
        List<Shape> shapes = prefixShapes().get(prefix.name());
        return shapes == null ? Collections.emptyList() : shapes;
    }

    /// Returns the stack for the first of `prefix`'s [#shapes] that `material` generates. Null when `material`
    /// is null or generates none of them.
    public static @Nullable ItemStack stack(OrePrefixes prefix, @Nullable Material material, long amount) {
        Shape shape = servedShape(prefix, material);
        return shape == null ? null : MaterialLibAPI.getStack(material, shape, (int) amount);
    }

    /// Returns whether the pair has a MaterialLib stack (see [#stack]).
    public static boolean isCutOver(OrePrefixes prefix, @Nullable Material material) {
        return servedShape(prefix, material) != null;
    }

    /// Returns whether MaterialLib's own item or block of the pair's shape carries `material`. False for a pair
    /// with no MaterialLib backing and for one a foreign item stands in for (`gem` Diamond is the vanilla diamond).
    public static boolean isMinted(OrePrefixes prefix, @Nullable Material material) {
        Shape shape = servedShape(prefix, material);
        return shape != null && isMinted(material, shape);
    }

    /// Same as [#isMinted(OrePrefixes, Material)] for a shape `material` generates. Never resolves a shape
    /// override's item, so it answers before the overriding mod has registered it.
    public static boolean isMinted(Material material, Shape shape) {
        return MINTED.computeIfAbsent(shape, MaterialParts::mintedMaterials)
            .contains(material);
    }

    private static final Map<Shape, Set<Material>> MINTED = new IdentityHashMap<>();

    private static Set<Material> mintedMaterials(Shape shape) {
        Material[] served = shape instanceof ShapeItem item ? item.getServedMaterials()
            : shape instanceof ShapeBlock block ? block.getServedMaterials() : new Material[0];
        Set<Material> minted = Collections.newSetFromMap(new IdentityHashMap<>());
        Collections.addAll(minted, served);
        return minted;
    }

    /// Returns the first of `prefix`'s shapes that `material` generates, or null when it generates none.
    private static @Nullable Shape servedShape(OrePrefixes prefix, @Nullable Material material) {
        if (material == null) return null;
        for (Shape shape : shapes(prefix)) {
            if (material.hasShape(shape)) return shape;
        }
        return null;
    }

    /// Returns `materialAmount` (in [gregtech.api.enums.GTValues#M] units) of `material` as dust. It picks whole
    /// dusts when the amount divides evenly or reaches 16 dusts, else small dusts when it divides into quarters or
    /// reaches 8 dusts, else tiny dusts. Null when the amount is below one tiny dust or the material carries none
    /// of the three.
    public static @Nullable ItemStack dustOf(@Nullable Material material, long materialAmount) {
        if (materialAmount <= 0) return null;
        ItemStack stack = null;
        if (materialAmount % M == 0 || materialAmount >= M * 16) {
            stack = amountPart(material, Shapes.dust, materialAmount / M);
        }
        if (stack == null && ((materialAmount * 4) % M == 0 || materialAmount >= M * 8)) {
            stack = amountPart(material, Shapes.dustSmall, (materialAmount * 4) / M);
        }
        if (stack == null && materialAmount * 9 >= M) {
            stack = amountPart(material, Shapes.dustTiny, (materialAmount * 9) / M);
        }
        return stack;
    }

    /// Same as [#dustOf(Material, long)] for a [MaterialStack]'s material and amount.
    public static @Nullable ItemStack dustOf(@Nullable MaterialStack materialStack) {
        return materialStack == null ? null : dustOf(materialStack.mMaterial, materialStack.mAmount);
    }

    /// Returns `materialAmount` of `material` as metal. It picks blocks when the amount is a whole number of at
    /// least two blocks or reaches 8 blocks, else ingots when it divides evenly or reaches 8 ingots, else nuggets.
    /// Null when the amount is below one nugget or the material carries none of the three.
    public static @Nullable ItemStack ingotOf(@Nullable Material material, long materialAmount) {
        if (materialAmount <= 0) return null;
        ItemStack stack = null;
        if ((materialAmount % (M * 9) == 0 && materialAmount / (M * 9) > 1) || materialAmount >= M * 72) {
            stack = amountPart(material, BlockShapes.block, materialAmount / (M * 9));
        }
        if (stack == null && (materialAmount % M == 0 || materialAmount >= M * 8)) {
            stack = amountPart(material, Shapes.ingot, materialAmount / M);
        }
        if (stack == null && materialAmount * 9 >= M) {
            stack = amountPart(material, Shapes.nugget, (materialAmount * 9) / M);
        }
        return stack;
    }

    /// [#ingotOf], falling back to [#dustOf].
    public static @Nullable ItemStack ingotOrDustOf(@Nullable Material material, long materialAmount) {
        ItemStack stack = ingotOf(material, materialAmount);
        return stack != null ? stack : dustOf(material, materialAmount);
    }

    /// Same as [#ingotOrDustOf(Material, long)] for a [MaterialStack]'s material and amount.
    public static @Nullable ItemStack ingotOrDustOf(@Nullable MaterialStack materialStack) {
        return materialStack == null ? null : ingotOrDustOf(materialStack.mMaterial, materialStack.mAmount);
    }

    /// [#dustOf], falling back to [#ingotOf].
    public static @Nullable ItemStack dustOrIngotOf(@Nullable Material material, long materialAmount) {
        ItemStack stack = dustOf(material, materialAmount);
        return stack != null ? stack : ingotOf(material, materialAmount);
    }

    /// Returns the pair's stack at `amount`, capped at 64, or null when `material` lacks `shape`.
    private static @Nullable ItemStack amountPart(@Nullable Material material, Shape shape, long amount) {
        return GTUtility.copyAmount((int) amount, MaterialLibAPI.findStack(material, shape, 1));
    }

    /// Returns whether `stack`'s [MaterialData#getAssociation] names `material` as its primary material.
    public static boolean isPartOf(@Nullable ItemStack stack, @Nullable Material material) {
        if (material == null) return false;
        ItemData association = MaterialData.getAssociation(stack);
        return association != null && association.mMaterial.mMaterial == material;
    }

    /// Returns the shapes serving each oredict prefix. Where several shapes share a prefix, the shape named after
    /// the prefix comes first and the rest follow in name order.
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
