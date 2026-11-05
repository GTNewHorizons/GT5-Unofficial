package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class ItemData {

    private static final MaterialStack[] EMPTY_MATERIALSTACK_ARRAY = new MaterialStack[0];

    public final OrePrefixes mPrefix;
    public final MaterialStack mMaterial;
    @NotNull
    public final MaterialStack[] mByProducts;
    public final boolean hasExplicitComposition;
    public ItemStack mUnificationTarget = null;

    public ItemData(OrePrefixes aPrefix, Materials aMaterial) {
        mPrefix = aPrefix;
        mMaterial = aMaterial == null ? null : new MaterialStack(aMaterial, aPrefix.getMaterialAmount());
        mByProducts = aPrefix.mSecondaryMaterial == null || aPrefix.mSecondaryMaterial.mMaterial == null
            ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[] { aPrefix.mSecondaryMaterial.clone() };
        hasExplicitComposition = false;
    }

    public ItemData(MaterialStack aMaterial, MaterialStack... aByProducts) {
        mPrefix = null;
        mMaterial = aMaterial.mMaterial == null ? null : aMaterial.clone();
        hasExplicitComposition = true;

        if (aByProducts == null) {
            mByProducts = EMPTY_MATERIALSTACK_ARRAY;
        } else {
            MaterialStack[] tByProducts = aByProducts.length < 1 ? EMPTY_MATERIALSTACK_ARRAY
                : new MaterialStack[aByProducts.length];
            int j = 0;
            for (MaterialStack aByProduct : aByProducts)
                if (aByProduct != null && aByProduct.mMaterial != null) tByProducts[j++] = aByProduct.clone();
            mByProducts = j > 0 ? new MaterialStack[j] : EMPTY_MATERIALSTACK_ARRAY;
            System.arraycopy(tByProducts, 0, mByProducts, 0, mByProducts.length);
        }
    }

    public ItemData(Materials aMaterial, long aAmount, MaterialStack... aByProducts) {
        this(new MaterialStack(aMaterial, aAmount), aByProducts);
    }

    public ItemData(Materials aMaterial, long aAmount, Materials aByProduct, long aByProductAmount) {
        this(new MaterialStack(aMaterial, aAmount), new MaterialStack(aByProduct, aByProductAmount));
    }

    /**
     * Builds material composition from multiple ingredients.
     * <p>
     * Materials from all ingredients are merged and sorted by amount. The largest material becomes {@link #mMaterial},
     * with the remaining materials stored as {@link #mByProducts}.
     */
    public ItemData(ItemData... ingredients) {
        ArrayList<MaterialStack> materials = mergeMaterials(ingredients);

        mPrefix = null;
        mMaterial = !materials.isEmpty() ? materials.removeFirst() : null;
        mByProducts = !materials.isEmpty() ? materials.toArray(new MaterialStack[0]) : EMPTY_MATERIALSTACK_ARRAY;
        hasExplicitComposition = true;
    }

    /**
     * Applies explicit material composition to an existing OreDict association.
     * <p>
     * The associated material remains {@link #mMaterial}.
     * Its amount is taken from the explicit composition, all remaining materials become {@link #mByProducts}.
     */
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

    public final MaterialStack getByProduct(int aIndex) {
        return aIndex >= 0 && aIndex < mByProducts.length ? mByProducts[aIndex] : null;
    }

    @Override
    public String toString() {
        if (mPrefix == null || mMaterial == null || mMaterial.mMaterial == null) return "";
        return mPrefix.getName() + mMaterial.mMaterial.mName;
    }
}
