package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialUtils;

/// What material an [ItemStack] is made of and how much of it, for any item in the game.
///
/// This is GregTech's cross-mod material index. A [com.ruling_0.materiallib.api.Shape] knows only the items
/// MaterialLib itself generated; this answers for a foreign mod's items too.
///
/// Keyed by [OrePrefixes]: an association is created from an ore-dictionary name, and a tenth of the prefixes
/// named here have no shape at all. [#mMaterial]'s amount comes from [OrePrefixes#getMaterialAmount]; see
/// [OrePrefixes]'s class javadoc for how the two spaces divide.
///
/// An association names an ore-dictionary entry (prefix plus material, or prefix plus a bare name for the
/// entries whose material side is not a material at all) and is what unification keys on. A composition
/// ([#hasExplicitComposition]) lists what a stack is made of, for recycling; it never carries a unification name
/// unless it was merged onto an existing association.
public class ItemData {

    private static final MaterialStack[] EMPTY_MATERIALSTACK_ARRAY = new MaterialStack[0];

    public final OrePrefixes mPrefix;
    public final MaterialStack mMaterial;
    @NotNull
    public final MaterialStack[] mByProducts;
    public final boolean hasExplicitComposition;
    private final String oreDictName;

    public ItemData(OrePrefixes prefix, Material material) {
        mPrefix = prefix;
        mMaterial = material == null ? null : new MaterialStack(material, prefix.getMaterialAmount());
        mByProducts = prefix.mSecondaryMaterial == null || prefix.mSecondaryMaterial.mMaterial == null
            ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[] { prefix.mSecondaryMaterial.clone() };
        hasExplicitComposition = false;
        oreDictName = null;
    }

    /// An ingredient that names an ore-dictionary entry and carries no composition, for the entries whose
    /// material side is not a material at all.
    public ItemData(OrePrefixes prefix, String materialName) {
        mPrefix = prefix;
        mMaterial = null;
        mByProducts = EMPTY_MATERIALSTACK_ARRAY;
        hasExplicitComposition = false;
        oreDictName = prefix.oreDictName(materialName);
    }

    /// An ingredient that names `oreDictName` while counting as `prefix` of `material`, for a
    /// [gregtech.api.enums.materials.Wildcard] entry whose name belongs to no single material.
    public ItemData(OrePrefixes prefix, Material material, String oreDictName) {
        mPrefix = prefix;
        mMaterial = new MaterialStack(material, prefix.getMaterialAmount());
        mByProducts = prefix.mSecondaryMaterial == null || prefix.mSecondaryMaterial.mMaterial == null
            ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[] { prefix.mSecondaryMaterial.clone() };
        hasExplicitComposition = false;
        this.oreDictName = oreDictName;
    }

    public ItemData(MaterialStack material, MaterialStack... byProducts) {
        mPrefix = null;
        mMaterial = material.mMaterial == null ? null : material.clone();
        hasExplicitComposition = true;

        if (byProducts == null) {
            mByProducts = EMPTY_MATERIALSTACK_ARRAY;
        } else {
            MaterialStack[] tByProducts = byProducts.length < 1 ? EMPTY_MATERIALSTACK_ARRAY
                : new MaterialStack[byProducts.length];
            int j = 0;
            for (MaterialStack byProduct : byProducts)
                if (byProduct != null && byProduct.mMaterial != null) tByProducts[j++] = byProduct.clone();
            mByProducts = j > 0 ? new MaterialStack[j] : EMPTY_MATERIALSTACK_ARRAY;
            System.arraycopy(tByProducts, 0, mByProducts, 0, mByProducts.length);
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
    /// the largest becomes [#mMaterial] and the rest [#mByProducts].
    public ItemData(ItemData... ingredients) {
        ArrayList<MaterialStack> materials = mergeMaterials(ingredients);

        mPrefix = null;
        mMaterial = !materials.isEmpty() ? materials.removeFirst() : null;
        mByProducts = !materials.isEmpty() ? materials.toArray(new MaterialStack[0]) : EMPTY_MATERIALSTACK_ARRAY;
        hasExplicitComposition = true;
        oreDictName = null;
    }

    /// Applies an explicit composition to an existing ore-dictionary association. The associated material stays
    /// [#mMaterial] with its amount taken from the composition; every other material becomes a by-product. A
    /// name-only association keeps its name.
    public ItemData(@NotNull ItemData association, @NotNull ItemData composition) {
        ArrayList<MaterialStack> materials = mergeMaterials(composition);
        MaterialStack associatedMaterial = null;

        if (association.mMaterial != null) {
            for (int i = 0; i < materials.size(); i++) {
                if (materials.get(i).mMaterial == association.mMaterial.mMaterial) {
                    associatedMaterial = materials.remove(i);
                    break;
                }
            }
        }

        mPrefix = association.mPrefix;
        mMaterial = associatedMaterial != null ? associatedMaterial : association.mMaterial;
        mByProducts = !materials.isEmpty() ? materials.toArray(new MaterialStack[0]) : EMPTY_MATERIALSTACK_ARRAY;
        hasExplicitComposition = true;
        oreDictName = association.oreDictName;
    }

    private ItemData(OrePrefixes prefix, MaterialStack material, @NotNull MaterialStack[] byProducts,
        boolean hasExplicitComposition, String oreDictName) {
        mPrefix = prefix;
        mMaterial = material;
        mByProducts = byProducts;
        this.hasExplicitComposition = hasExplicitComposition;
        this.oreDictName = oreDictName;
    }

    /// A copy with every material amount divided by `divisor`.
    public ItemData dividedBy(long divisor) {
        MaterialStack[] byProducts = mByProducts.length == 0 ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[mByProducts.length];
        for (int i = 0; i < byProducts.length; i++) {
            byProducts[i] = mByProducts[i].copy(mByProducts[i].mAmount / divisor);
        }
        MaterialStack material = mMaterial == null ? null : mMaterial.copy(mMaterial.mAmount / divisor);
        return new ItemData(mPrefix, material, byProducts, hasExplicitComposition, oreDictName);
    }

    private static ArrayList<@NotNull MaterialStack> mergeMaterials(ItemData... dataArray) {
        ArrayList<MaterialStack> materials = new ArrayList<>();

        for (ItemData itemData : dataArray) {
            if (itemData == null) continue;

            if (itemData.mMaterial != null && itemData.mMaterial.mAmount > 0) {
                mergeMaterial(materials, itemData.mMaterial);
            }

            for (MaterialStack material : itemData.mByProducts) {
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
        return mPrefix != null && mMaterial != null && mMaterial.mMaterial != null;
    }

    /// Whether [#toString] names a unification target. True both for a prefix+material association and for the
    /// composition-free name-only form.
    public final boolean hasUnificationName() {
        return oreDictName != null || hasValidPrefixMaterialData();
    }

    /// Whether this is a name-only association (see [#ItemData(OrePrefixes, String)]).
    public final boolean isNameOnly() {
        return oreDictName != null && mMaterial == null;
    }

    public final boolean hasValidPrefixData() {
        return mPrefix != null;
    }

    public final boolean hasValidMaterialData() {
        return mMaterial != null && mMaterial.mMaterial != null;
    }

    public final ArrayList<MaterialStack> getAllMaterialStacks() {
        ArrayList<MaterialStack> rList = new ArrayList<>();
        if (hasValidMaterialData()) rList.add(mMaterial);
        rList.addAll(Arrays.asList(mByProducts));
        return rList;
    }

    public final MaterialStack getByProduct(int index) {
        return index >= 0 && index < mByProducts.length ? mByProducts[index] : null;
    }

    @Override
    public String toString() {
        if (oreDictName != null) return oreDictName;
        if (mPrefix == null || mMaterial == null || mMaterial.mMaterial == null) return "";
        return mPrefix.getName() + MaterialUtils.internalName(mMaterial.mMaterial);
    }
}
