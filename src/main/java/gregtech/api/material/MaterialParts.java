package gregtech.api.material;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.materials2.Materials2CellShapes;

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
}
