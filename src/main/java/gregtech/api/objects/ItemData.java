package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialUtils;

/// What material an [net.minecraft.item.ItemStack] is made of and how much of it, for any item in the game.
///
/// A [com.ruling_0.materiallib.api.Shape] knows only the items MaterialLib generated. This also covers other
/// mods' items.
///
/// Keyed by [OrePrefixes]. An association's [#material] amount comes from [OrePrefixes#getMaterialAmount]. See
/// [OrePrefixes]'s class javadoc for why prefixes and shapes are not interchangeable.
///
/// An association names an oredict entry and is what unification keys on: a prefix plus a material, or a prefix
/// plus a bare name for entries whose material side is not a material. A composition ([#hasExplicitComposition])
/// lists what a stack is made of, for recycling. It carries no unification name unless it is merged onto an
/// association.
public class ItemData {

    private static final MaterialStack[] EMPTY_MATERIALSTACK_ARRAY = new MaterialStack[0];

    public final OrePrefixes prefix;
    public final MaterialStack material;
    @NotNull
    public final MaterialStack[] byProducts;
    public final boolean hasExplicitComposition;
    private final String oreDictName;
    /// The prefix-and-material name, built on first use since recipe matching reads it for every input.
    private String pairName;

    public ItemData(OrePrefixes prefix, Material material) {
        this(prefix, material, null);
    }

    /// An ingredient that names an ore-dictionary entry and carries no composition, for the entries whose
    /// material side is not a material at all.
    public ItemData(OrePrefixes prefix, String materialName) {
        this.prefix = prefix;
        this.material = null;
        this.byProducts = EMPTY_MATERIALSTACK_ARRAY;
        hasExplicitComposition = false;
        oreDictName = prefix.oreDictName(materialName);
    }

    /// An ingredient that names `oreDictName`, or the pair's own name when null, while counting as `prefix` of
    /// `material`. A [gregtech.api.enums.materials.Wildcard] entry's name belongs to no single material.
    public ItemData(OrePrefixes prefix, Material material, String oreDictName) {
        this.prefix = prefix;
        this.material = material == null ? null : new MaterialStack(material, prefix.getMaterialAmount());
        this.byProducts = prefix.mSecondaryMaterial == null || prefix.mSecondaryMaterial.mMaterial == null
            ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[] { prefix.mSecondaryMaterial.clone() };
        hasExplicitComposition = false;
        this.oreDictName = oreDictName;
    }

    public ItemData(MaterialStack material, MaterialStack... byProducts) {
        this.prefix = null;
        this.material = material.mMaterial == null ? null : material.clone();
        hasExplicitComposition = true;

        if (byProducts == null) {
            this.byProducts = EMPTY_MATERIALSTACK_ARRAY;
        } else {
            MaterialStack[] kept = byProducts.length < 1 ? EMPTY_MATERIALSTACK_ARRAY
                : new MaterialStack[byProducts.length];
            int j = 0;
            for (MaterialStack byProduct : byProducts)
                if (byProduct != null && byProduct.mMaterial != null) kept[j++] = byProduct.clone();
            this.byProducts = j > 0 ? new MaterialStack[j] : EMPTY_MATERIALSTACK_ARRAY;
            System.arraycopy(kept, 0, this.byProducts, 0, this.byProducts.length);
        }
        oreDictName = null;
    }

    public ItemData(Material material, long amount, MaterialStack... byProducts) {
        this(new MaterialStack(material, amount), byProducts);
    }

    public ItemData(Material material, long amount, Material byProduct, long byProductAmount) {
        this(new MaterialStack(material, amount), new MaterialStack(byProduct, byProductAmount));
    }

    /// Builds a material composition from multiple ingredients: their materials are merged and sorted by amount,
    /// the largest becomes [#material] and the rest [#byProducts].
    public ItemData(ItemData... ingredients) {
        ArrayList<MaterialStack> materials = mergeMaterials(ingredients);

        prefix = null;
        material = !materials.isEmpty() ? materials.removeFirst() : null;
        byProducts = !materials.isEmpty() ? materials.toArray(new MaterialStack[0]) : EMPTY_MATERIALSTACK_ARRAY;
        hasExplicitComposition = true;
        oreDictName = null;
    }

    /// Applies an explicit composition to an existing ore-dictionary association. The associated material stays
    /// [#material] with its amount taken from the composition; every other material becomes a by-product. A
    /// name-only association keeps its name.
    public ItemData(@NotNull ItemData association, @NotNull ItemData composition) {
        ArrayList<MaterialStack> materials = mergeMaterials(composition);
        MaterialStack associatedMaterial = null;

        if (association.material != null) {
            for (int i = 0; i < materials.size(); i++) {
                if (materials.get(i).mMaterial == association.material.mMaterial) {
                    associatedMaterial = materials.remove(i);
                    break;
                }
            }
        }

        prefix = association.prefix;
        material = associatedMaterial != null ? associatedMaterial : association.material;
        byProducts = !materials.isEmpty() ? materials.toArray(new MaterialStack[0]) : EMPTY_MATERIALSTACK_ARRAY;
        hasExplicitComposition = true;
        oreDictName = association.oreDictName;
    }

    private ItemData(OrePrefixes prefix, MaterialStack material, @NotNull MaterialStack[] byProducts,
        boolean hasExplicitComposition, String oreDictName) {
        this.prefix = prefix;
        this.material = material;
        this.byProducts = byProducts;
        this.hasExplicitComposition = hasExplicitComposition;
        this.oreDictName = oreDictName;
    }

    /// A copy with every material amount divided by `divisor`.
    public ItemData dividedBy(long divisor) {
        MaterialStack[] dividedByProducts = byProducts.length == 0 ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[byProducts.length];
        for (int i = 0; i < dividedByProducts.length; i++) {
            dividedByProducts[i] = byProducts[i].copy(byProducts[i].mAmount / divisor);
        }
        MaterialStack dividedMaterial = material == null ? null : material.copy(material.mAmount / divisor);
        return new ItemData(prefix, dividedMaterial, dividedByProducts, hasExplicitComposition, oreDictName);
    }

    private static ArrayList<@NotNull MaterialStack> mergeMaterials(ItemData... dataArray) {
        ArrayList<MaterialStack> materials = new ArrayList<>();

        for (ItemData itemData : dataArray) {
            if (itemData == null) continue;

            if (itemData.material != null && itemData.material.mAmount > 0) {
                mergeMaterial(materials, itemData.material);
            }

            for (MaterialStack material : itemData.byProducts) {
                if (material.mAmount > 0) {
                    mergeMaterial(materials, material);
                }
            }
        }

        materials.sort((a, b) -> Long.compare(b.mAmount, a.mAmount));
        return materials;
    }

    private static void mergeMaterial(ArrayList<MaterialStack> materials, MaterialStack material) {
        for (MaterialStack existing : materials) {
            if (existing.mMaterial == material.mMaterial) {
                existing.mAmount += material.mAmount;
                return;
            }
        }
        materials.add(material.clone());
    }

    public final boolean hasValidPrefixMaterialData() {
        return prefix != null && material != null && material.mMaterial != null;
    }

    /// Whether [#toString] names a unification target. True both for a prefix+material association and for the
    /// composition-free name-only form.
    public final boolean hasUnificationName() {
        return oreDictName != null || hasValidPrefixMaterialData();
    }

    /// Whether this is a name-only association (see [#ItemData(OrePrefixes, String)]).
    public final boolean isNameOnly() {
        return oreDictName != null && material == null;
    }

    public final boolean hasValidPrefixData() {
        return prefix != null;
    }

    public final boolean hasValidMaterialData() {
        return material != null && material.mMaterial != null;
    }

    public final ArrayList<MaterialStack> getAllMaterialStacks() {
        ArrayList<MaterialStack> stacks = new ArrayList<>();
        if (hasValidMaterialData()) stacks.add(material);
        stacks.addAll(Arrays.asList(byProducts));
        return stacks;
    }

    public final MaterialStack getByProduct(int index) {
        return index >= 0 && index < byProducts.length ? byProducts[index] : null;
    }

    @Override
    public String toString() {
        if (oreDictName != null) return oreDictName;
        if (prefix == null || material == null || material.mMaterial == null) return "";
        if (pairName == null) pairName = prefix.getName() + MaterialUtils.internalName(material.mMaterial);
        return pairName;
    }
}
