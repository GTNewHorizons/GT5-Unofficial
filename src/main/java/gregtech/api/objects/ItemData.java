package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class ItemData {

    private static final MaterialStack[] EMPTY_MATERIALSTACK_ARRAY = new MaterialStack[0];

    public final List<Object> mExtraData = new GTArrayList<>(false, 1);
    public final OrePrefixes mPrefix;
    public final MaterialStack mMaterial;
    public final MaterialStack[] mByProducts;
    public boolean mBlackListed = false;
    public ItemStack mUnificationTarget = null;

    public ItemData(OrePrefixes aPrefix, Materials aMaterial, boolean aBlackListed) {
        mPrefix = aPrefix;
        mMaterial = aMaterial == null ? null : new MaterialStack(aMaterial, aPrefix.mMaterialAmount);
        mBlackListed = aBlackListed;
        mByProducts = aPrefix.mSecondaryMaterial == null || aPrefix.mSecondaryMaterial.mMaterial == null
            ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[] { aPrefix.mSecondaryMaterial.clone() };
    }

    public ItemData(OrePrefixes aPrefix, Materials aMaterial) {
        this(aPrefix, aMaterial, false);
    }

    public ItemData(MaterialStack aMaterial, MaterialStack... aByProducts) {
        mPrefix = null;
        mMaterial = aMaterial.mMaterial == null ? null : aMaterial.clone();
        mBlackListed = true;
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

    public ItemData(ItemData... aData) {
        mPrefix = null;
        mBlackListed = true;

        ArrayList<MaterialStack> aList = new ArrayList<>(), rList = new ArrayList<>();

        for (ItemData tData : aData) if (tData != null) {
            if (tData.hasValidMaterialData() && tData.mMaterial.mAmount > 0) aList.add(tData.mMaterial.clone());
            for (MaterialStack tMaterial : tData.mByProducts) if (tMaterial.mAmount > 0) aList.add(tMaterial.clone());
        }

        for (MaterialStack aMaterial : aList) {
            boolean temp = true;
            for (MaterialStack tMaterial : rList) if (aMaterial.mMaterial == tMaterial.mMaterial) {
                tMaterial.mAmount += aMaterial.mAmount;
                temp = false;
                break;
            }
            if (temp) rList.add(aMaterial.clone());
        }

        rList.sort((a, b) -> Long.compare(b.mAmount, a.mAmount));

        if (rList.isEmpty()) {
            mMaterial = null;
        } else {
            mMaterial = rList.get(0);
            rList.remove(0);
        }

        mByProducts = rList.toArray(new MaterialStack[0]);
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
        return String.valueOf(mPrefix.name() + mMaterial.mMaterial.mName);
    }
}
